# all: server

# Generate source files from Thrift IDL
thrift: Task.thrift
	thrift --gen cpp Task.thrift

# Compile server from main source and generated sources
server: server.cpp gen-cpp/Login.cpp gen-cpp/Reports.cpp gen-cpp/Search.cpp gen-cpp/Task_types.cpp database.hpp items.hpp string_conversions.hpp summary.hpp
	g++ --std=c++17 -I/<THRIFT_HOME>/include -L/<THRIFT_HOME>/lib -Igen-cpp -o server server.cpp gen-cpp/Login.cpp gen-cpp/Reports.cpp gen-cpp/Search.cpp gen-cpp/Task_types.cpp database.hpp items.hpp string_conversions.hpp summary.hpp -lthrift

clean:
	rm -f server
	rm -rf gen-cpp


.PHONY: all clean
