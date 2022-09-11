const thrift = require('thrift');
// Generated module
const Example = require('./gen-nodejs/Example.js');

// Connect to server by TCP socket
const connection = thrift.createConnection('localhost', 5000, {
    // Use buffering
    transport: thrift.TBufferedTransport,
    // Use a binary protocol to serialize data
    protocol: thrift.TBinaryProtocol
})

// Use a multiplexed protocol to select a service by name
const multiplexer = new thrift.Multiplexer();
const exampleClient = multiplexer.createClient("Example", Example, connection);

// Send a string and print the response
const text = "Hello from Node";
console.log("Calling Example with: " + text);

exampleClient.ping(text, function(err, response){
    if(err) {
        console.error(err);
    }
    else {
        console.log("Response: " + response);
        // Close the connection
        connection.end();
    }
});

