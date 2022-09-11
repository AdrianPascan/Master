from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.protocol import TMultiplexedProtocol
# Generated module
from Example import Example

# Connect to server by TCP socket
# socket = TSocket.TSocket('localhost', 5000)
socket = TSocket.TSocket(host='lab.d3s.mff.cuni.cz', port=5000)
## Use buffering
transport = TTransport.TBufferedTransport(socket)
## Use a binary protocol to serialize data
muxProtocol = TBinaryProtocol.TBinaryProtocol(transport)
## Use a multiplexed protocol to select a service by name
protocol = TMultiplexedProtocol.TMultiplexedProtocol(muxProtocol, "Example")
## Proxy object
client = Example.Client(protocol)
## Open the connection
transport.open()

## Send a string and print the response
text = "Hello from python"
print("Calling Example with: " + text)

response = client.ping(text)
print("Response: " + response)

# Close the connection
transport.close()
