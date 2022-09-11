// Standard library headers
#include <memory>
#include <iostream>
#include <string>
#include <sstream>
#include <mutex>

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
#include "gen-cpp/Example.h"

using namespace apache::thrift;
using namespace apache::thrift::transport;
using namespace apache::thrift::protocol;
using namespace apache::thrift::server;
using namespace std;

// Implementation of the Example service
class ExampleHandler: public ExampleIf{

    // Each connection gets assigned an id
    // That allows us to see how for each connection, a new handler is used
    unsigned connectionId;

public:
    ExampleHandler(unsigned connectionId):
        connectionId(connectionId){}

    // Implementation of ping
    void ping(std::string& _return, const std::string& text) {
        cout << "Client sent: " << text << endl;

        // Generate the response
        ostringstream ss;
        ss << "Connection id " << connectionId << ". ";
        ss << "Text has length " << text.length() << ".";

        cout << "Responding with: " << ss.str() << endl;

        // Return value is handled by an output parameter
        _return = ss.str();
    }
};

// This factory creates a new handler for each conection
class PerConnectionExampleProcessorFactory: public TProcessorFactory{
    // We assign each handler an id
    unsigned connectionIdCounter;
    mutex lock;

public:
    PerConnectionExampleProcessorFactory(): connectionIdCounter(0) {}

    // The counter is incremented for each connection
    unsigned assignId() {
        lock_guard<mutex> counterGuard(lock);
        return ++connectionIdCounter;
    }

    // This metod is called for each connection
    virtual std::shared_ptr<TProcessor> getProcessor(const TConnectionInfo& connInfo) {
        // Assign a new id to this connection
        unsigned connectionId = assignId();
        // Create a handler for the Example service
        shared_ptr<ExampleHandler> handler(new ExampleHandler(connectionId));
        // Create a processor for the Example service
        shared_ptr<TProcessor> processor(new ExampleProcessor(handler));
        // Add the processor to a multiplexed processor
        // This allows extending this server by adding more services
        shared_ptr<TMultiplexedProcessor> muxProcessor(new TMultiplexedProcessor());
        muxProcessor->registerProcessor("Example", processor);
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
        shared_ptr<TProcessorFactory> processorFactory(new PerConnectionExampleProcessorFactory());

        // Start the server
        TThreadedServer server(processorFactory, serverTransport, transportFactory, protocolFactory);
        server.serve();
    }
    catch (TException& tx) {
        cout << "ERROR: " << tx.what() << endl;
    }

}