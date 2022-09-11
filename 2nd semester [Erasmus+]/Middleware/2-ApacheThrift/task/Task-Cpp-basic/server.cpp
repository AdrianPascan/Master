// Standard library headers
#include <memory>
#include <iostream>
#include <string>
#include <sstream>
#include <mutex>
#include <stdlib.h>
#include <cstdlib>

// Thrift headers
#include <thrift/protocol/TProtocol.h>
#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/protocol/TMultiplexedProtocol.h>
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TTransportUtils.h>
#include <thrift/server/TServer.h>
#include <thrift/server/TThreadedServer.h>
#include <thrift/processor/TMultiplexedProcessor.h>
#include <thrift/TProcessor.h>
#include <thrift/Thrift.h>

// Generated headers
// #include "gen-cpp/Task.h"
#include "gen-cpp/Login.h"
#include "gen-cpp/Search.h"
#include "gen-cpp/Reports.h"
#include "gen-cpp/Task_types.h"

#include "database.hpp"
#include "items.hpp"
#include "summary.hpp"

using namespace apache::thrift;
using namespace apache::thrift::transport;
using namespace apache::thrift::protocol;
using namespace apache::thrift::server;
using namespace std;

// Implementation of the Login service
class LoginHandler: public Task2::LoginIf{

    // Each connection gets assigned an id
    // That allows us to see how for each connection, a new handler is used
    unsigned connectionId;
    bool* loggedIn;
    int32_t expectedKey;

public:
    LoginHandler(unsigned connectionId, bool* loggedIn):
        connectionId(connectionId), loggedIn(loggedIn){
            *loggedIn = false;
            expectedKey = abs(rand()) % 2147483647;
        }

    // Implementation of logIn
    void logIn(const std::string& userName, const int32_t key) {
        cout << "LOGIN.logIn" << endl;

        if (key != expectedKey) {
            Task2::InvalidKeyException ike;
            ike.invalidKey = key;
            ike.expectedKey = expectedKey;

            cout << "Keys do not match for user " << userName << ": expected " << expectedKey << ", got " << key << "." << endl;
            cout << "END_OF_LOGIN.logIn" << endl;

            throw ike;
        }

        *loggedIn = true;

        cout << "Keys match for user " << userName << " (" << key << ")." << endl;
        cout << "END_OF_LOGIN.logIn" << endl;
    }

    // Implementation of logOut
    void logOut() {
        cout << "LOGIN.logOut" << endl;

        *loggedIn = false;

        cout << "END_OF_LOGIN.logOut" << endl;
    }
};

// Implementation of the Search service
class SearchHandler: public Task2::SearchIf{

    // Each connection gets assigned an id
    // That allows us to see how for each connection, a new handler is used
    unsigned connectionId;
    bool* loggedIn;
    shared_ptr<SummaryBuilder> summaryBuilder;
    shared_ptr<Database> database;
    size_t lastDatabaseIndex;
    set<string> itemTypes;
    int32_t itemLimit;
    int32_t fetchedItemCount;

public:
    SearchHandler(unsigned connectionId, bool* loggedIn, shared_ptr<SummaryBuilder> summaryBuilder, shared_ptr<Database> database):
        connectionId(connectionId), loggedIn(loggedIn), summaryBuilder(summaryBuilder), database(database), lastDatabaseIndex(0),
        itemTypes(set<string>()), itemLimit(-1), fetchedItemCount(0)
        {}
        
    // Implementation of fetch
    void fetch(Task2::FetchResult& _return) {
        cout << "SEARCH.fetch" << endl;

        if (!*loggedIn) {
            Task2::ProtocolException pe;
            pe.message = "User not logged in.";

            cout << "ProtocolException: " << pe.message << endl;
            cout << "END_OF_SEARCH.fetch" << endl;

            throw pe;
        }

        // Simulate long-running search
        if (rand() % 2 == 0) {
            cout << "Server is still searching for items in the database..." << endl;

            _return.status = Task2::FetchStatus::PENDING;
        }
        else {
            cout << "Fetching one item of type A from the database..." << endl;

            vector<Item*> output;
            lastDatabaseIndex = database.get()->search({"ItemA"}, output, lastDatabaseIndex, 1);

            cout << "Item of type A fetched from the database." << endl;

            if (output.empty()) {
                cout << "WARNING: All items of type A were fetched from the database." << endl;

                _return.status = Task2::FetchStatus::ENDED;
            } else {
                _return.status = Task2::FetchStatus::ITEM;
                _return.item = *((Task2::ItemA*) output.back());
                summaryBuilder.get()->add(*((ItemA*) output.back()));

                cout << "Processed item of type A." << endl;
            }
        }

        cout << "END_OF_SEARCH.fetch" << endl;
    }
};

// Implementation of the Reports service
class ReportsHandler: public Task2::ReportsIf{

    // Each connection gets assigned an id
    // That allows us to see how for each connection, a new handler is used
    unsigned connectionId;
    bool* loggedIn;
    shared_ptr<SummaryBuilder> summaryBuilder;

public:
    ReportsHandler(unsigned connectionId, bool* loggedIn, shared_ptr<SummaryBuilder> summaryBuilder):
        connectionId(connectionId), loggedIn(loggedIn), summaryBuilder(summaryBuilder){}

    // Implementation of saveSummary
    bool saveSummary(const Task2::Summary& summary) {
        cout << "REPORTS.saveSummary" << endl;

        if (!*loggedIn) {
            Task2::ProtocolException pe;
            pe.message = "User not logged in.";

            cout << "ProtocolException: " << pe.message << endl;
            cout << "END_OF_REPORTS.saveSummary" << endl;

            throw pe;
        }
        
        cout << "Summary is" << (summary == summaryBuilder.get()->get() ? string(" ") : string(" NOT ")) << "accurate." << endl;

        cout << "END_OF_REPORTS.saveSummary" << endl;

        return summary == summaryBuilder.get()->get();
    }
};

// This factory creates a new handler for each conection
class PerConnectionProcessorFactory: public TProcessorFactory{
    // We assign each handler an id
    unsigned connectionIdCounter;
    mutex lock;

public:
    PerConnectionProcessorFactory(): connectionIdCounter(0) {}

    // The counter is incremented for each connection
    unsigned assignId() {
        lock_guard<mutex> counterGuard(lock);
        return ++connectionIdCounter;
    }

    // This metod is called for each connection
    virtual std::shared_ptr<TProcessor> getProcessor(const TConnectionInfo& connInfo) {
        bool loggedIn;
        shared_ptr<SummaryBuilder> summaryBuilder(new SummaryBuilder());

        mt19937 randomEngine(7);
        shared_ptr<Database> database(new Database(20, randomEngine));

        // Assign a new id to this connection
        unsigned connectionId = assignId();
        // Create a handler for the Login service
        shared_ptr<LoginHandler> loginHandler(new LoginHandler(connectionId, &loggedIn));
        // Create a processor for the Login service
        shared_ptr<TProcessor> loginProcessor(new Task2::LoginProcessor(loginHandler));
        // Create a handler for the Search service
        shared_ptr<SearchHandler> searchHandler(new SearchHandler(connectionId, &loggedIn, summaryBuilder, database));
        // Create a processor for the Search service
        shared_ptr<TProcessor> searchProcessor(new Task2::SearchProcessor(searchHandler));
        // Create a handler for the Reports service
        shared_ptr<ReportsHandler> reportsHandler(new ReportsHandler(connectionId, &loggedIn, summaryBuilder));
        // Create a processor for the Reports service
        shared_ptr<TProcessor> reportsProcessor(new Task2::ReportsProcessor(reportsHandler));
        // Add the processors to a multiplexed processor
        // This allows extending this server by adding more services
        shared_ptr<TMultiplexedProcessor> muxProcessor(new TMultiplexedProcessor());
        muxProcessor->registerProcessor("Login", loginProcessor);
        muxProcessor->registerProcessor("Search", searchProcessor);
        muxProcessor->registerProcessor("Reports", reportsProcessor);
        // Use the multiplexed processor
        return muxProcessor;
    }
};


int main(){
    
    try{
        // Accept connections on a TCP socket
        shared_ptr<TServerTransport> serverTransport(new TServerSocket(5000));
        // Use buffering
        shared_ptr<TTransportFactory> transportFactory(new TBufferedTransportFactory());
        // Use a binary protocol to serialize data
        shared_ptr<TProtocolFactory> protocolFactory(new TBinaryProtocolFactory());
        // Use a processor factory to create a processor per connection
        shared_ptr<TProcessorFactory> processorFactory(new PerConnectionProcessorFactory());

        // Start the server
        TThreadedServer server(processorFactory, serverTransport, transportFactory, protocolFactory);
        server.serve();
    }
    catch (TException& tx) {
        cout << "ERROR: " << tx.what() << endl;
    }

    return 0;

}