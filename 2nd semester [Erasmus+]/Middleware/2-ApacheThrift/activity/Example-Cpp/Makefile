all: client server

# Generate source files from Thrift IDL
gen-cpp/Example.cpp: Example.thrift
	thrift --gen cpp Example.thrift

# Compile server from main source and generated sources
server: server.cpp gen-cpp/Example.cpp
	g++ --std=c++17 -I/afs/ms/u/p/pascana/Desktop/Middleware/L2/thrift/include -L/afs/ms/u/p/pascana/Desktop/Middleware/L2/thrift/lib -Igen-cpp -o server server.cpp gen-cpp/Example.cpp -lthrift

# Compile client from main source and generated sources
client: client.cpp gen-cpp/Example.cpp
	g++ --std=c++17 -I/afs/ms/u/p/pascana/Desktop/Middleware/L2/thrift/include -L/afs/ms/u/p/pascana/Desktop/Middleware/L2/thrift/lib -Igen-cpp -o client client.cpp gen-cpp/Example.cpp -lthrift

clean:
	rm -f client
	rm -f server
	rm -rf gen-cpp


.PHONY: all clean
