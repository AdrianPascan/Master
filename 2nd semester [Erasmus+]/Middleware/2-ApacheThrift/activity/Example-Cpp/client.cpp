// Standard library headers
#include <memory>
#include <iostream>
#include <string>

// Thrift headers
#include <thrift/protocol/TProtocol.h>
#include <thrift/protocol/TBinaryProtocol.h>
#include <thrift/protocol/TMultiplexedProtocol.h>
#include <thrift/transport/TSocket.h>
#include <thrift/transport/TTransportUtils.h>
#include <thrift/Thrift.h>

// Generated headers
#include "gen-cpp/Example.h"

using namespace apache::thrift::transport;
using namespace apache::thrift::protocol;
using namespace apache::thrift;
using namespace std;

int main(){
    // Connect to server by TCP socket
//     shared_ptr<TTransport> socket(new TSocket("localhost", 5000));
//     !!! LAB. ACTIVITY !!!
    shared_ptr<TTransport> socket(new TSocket("lab.d3s.mff.cuni.cz", 5000));
    // Use buffering
    shared_ptr<TTransport> transport(new TBufferedTransport(socket));
    // Use a binary protocol to serialize data
    shared_ptr<TProtocol> muxProtocol(new TBinaryProtocol(transport));
    // Use a multiplexed protocol to select a service by name
    // shared_ptr<TProtocol> clientProtocol(new TMultiplexedProtocol(muxProtocol, "Example"));
//     !!! LAB. ACTIVITY !!!
    shared_ptr<TProtocol> clientProtocol(new TMultiplexedProtocol(muxProtocol, "ActualExample"));

    // Proxy object
    ExampleClient client(clientProtocol);
    try {
        // Open the connection
        transport->open();

        // Send a string and print the response
        string text = "Hello from C++";
        cout << "Calling Example with: " << text << endl;
        string response;
        client.ping(response, text);
        cout << "Response: " << response << endl;

        // Close the connection
        transport->close();
    }
    catch (TException& tx) {
        cout << "ERROR: " << tx.what() << endl;
    }

}
