from curses.ascii import US
from unicodedata import name
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.protocol import TMultiplexedProtocol

import time

# Generated module
from Task2 import Login, Search, Reports, ttypes


HOST = 'localhost'
PORT = 5000
# HOST = 'lab.d3s.mff.cuni.cz'
# PORT = 5001
USER = "abcdef"
KEY = 0
SLEEP = 1


#Login.logIn
def logIn(loginClient):
    print("LOGIN.logIn")

    try:
        loginClient.logIn("abcdef", 0)
        print("User {} logged in with initial key {}.".format(USER, KEY))
    except ttypes.InvalidKeyException as ike:
        print("Initial key {} invalid for user {}.".format(ike.invalidKey, USER))
        loginClient.logIn("abcdef", ike.expectedKey)
        print("User {} logged in with expected key {}.".format(USER, ike.expectedKey))

    print("END_OF_LOGIN.logIn")


# Login.logOut
def logOut(loginClient):
    print("LOGIN.logOut")

    loginClient.logOut()
    print("User {} logged out.".format(USER))

    print("END_OF_LOGIN.logOut")


# Search.fetch
def fetch(searchClient):
    print("SEARCH.fetch")
    
    items = []
    while True:
        fetchResult = searchClient.fetch()
        if fetchResult.status == ttypes.FetchStatus.ENDED:
            print("Ended.")
            break
        elif fetchResult.status == ttypes.FetchStatus.PENDING:
            print("Sleeping for {} seconds...".format(SLEEP))
            time.sleep(SLEEP)
        elif fetchResult.status == ttypes.FetchStatus.ITEM:
            print("Got an item!")
            items.append(fetchResult.item)
        else:
            print("Unknown fetch status.")

    print("END_OF_SEARCH.fetch")

    return items


def list_to_string(l):
    return ",".join(map(str, l))


def create_summary(items):
    if not items:
        return {}
    summary = {
        "fieldX": set(),
        "fieldY": set(),
        "fieldZ": set()
    }
    for item in items:
        summary["fieldX"].add(item.fieldX)
        summary["fieldY"].add(list_to_string(item.fieldY))
        if item.fieldZ:
            summary["fieldZ"].add(str(item.fieldZ))
    return summary
    

#REPORTS.saveSummary
def saveSummary(reportsClient, items):
    print("REPORTS.saveSummary")
    
    summary = create_summary(items)
    accurate = reportsClient.saveSummary(summary)
    print("Summary is{}accurate.".format(" " if accurate else " NOT "))

    print("END_OF_REPORTS.saveSummary")



if __name__ == "__main__":
    # Connect to server by TCP socket
    socket = TSocket.TSocket(HOST, PORT)
    ## Use buffering
    transport = TTransport.TBufferedTransport(socket)
    ## Use a binary protocol to serialize data
    muxProtocol = TBinaryProtocol.TBinaryProtocol(transport)
    ## Use a multiplexed protocol to select a service by name
    loginProtocol = TMultiplexedProtocol.TMultiplexedProtocol(muxProtocol, "Login")
    searchProtocol = TMultiplexedProtocol.TMultiplexedProtocol(muxProtocol, "Search")
    reportsProtocol = TMultiplexedProtocol.TMultiplexedProtocol(muxProtocol, "Reports")
    ## Proxy object
    loginClient = Login.Client(loginProtocol)
    searchClient = Search.Client(searchProtocol)
    reportsClient = Reports.Client(reportsProtocol)
    ## Open the connection
    transport.open()

    try:
        logIn(loginClient)    
        items = fetch(searchClient)
        saveSummary(reportsClient, items)
        logOut(loginClient)
    except ttypes.ProtocolException as pe:
        print("ProtocolException:", pe.message)

    # Close the connection
    transport.close()