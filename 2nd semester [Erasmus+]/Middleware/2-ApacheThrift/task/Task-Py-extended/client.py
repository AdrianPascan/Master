from curses.ascii import US
from unicodedata import name
from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.protocol import TMultiplexedProtocol

import time
import sys

# Generated module
from Task2 import Login, Search, Reports, ttypes


HOST = 'localhost'
PORT = 5000
# HOST = 'lab.d3s.mff.cuni.cz'
# PORT = 5001

USER = "abcdef"
KEY = 0
SLEEP = 1
ITEM_TYPES = {"ItemA", "ItemB", "ItemC"}  # set collection
ITEM_LIMIT = 30
ITEM_COUNT = 3


#Login.logIn
def logIn(loginClient):
    print("LOGIN.logIn")

    try:
        loginClient.logIn(USER, KEY)
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


# Search.initiateMultiple
def initiateMultiple(searchClient):
    print("SEARCH.initiateMultiple")
    
    searchClient.initiateMultiple(ITEM_TYPES, ITEM_LIMIT)
    print("Multiple search initialized.")

    print("END_OF_SEARCH.initiateMultiple")


# Search.fetchMultiple
def fetchMultiple(searchClient):
    print("SEARCH.fetchMultiple")
    
    items = []
    while True:
        fetchMultipleResult = searchClient.fetchMultiple(ITEM_COUNT)
        if fetchMultipleResult.status == ttypes.FetchStatus.ENDED:
            print("Ended.")
            break
        elif fetchMultipleResult.status == ttypes.FetchStatus.PENDING:
            print("Sleeping for {} seconds...".format(SLEEP))
            time.sleep(SLEEP)
        elif fetchMultipleResult.status == ttypes.FetchStatus.ITEM:
            print("Got {} items!".format(len(fetchMultipleResult.items)))
            for item in fetchMultipleResult.items:
                if item.type == 'ItemA':
                    items.append(item.itemA)
                elif item.type == 'ItemB':
                    items.append(item.itemB)
                elif item.type == 'ItemC':
                    items.append(item.itemC)
                else:
                    print("Unknowm item type: {}".format(item.type))
        else:
            print("Unknown fetch status.")

    print("END_OF_SEARCH.fetchMultiple")

    return items


def list_to_string(l):
    return ",".join(map(str, l))


def set_to_string(s):
    l = list(s)
    l.sort()
    return list_to_string(l)


def bool_to_string(b):
    return "true" if b else "false"


def create_summary(items):
    print("Creating summary...")

    # No items
    if not items:
        return {}

    # At least one item
    summary = {
        "fieldX": set(),
        "fieldY": set(),
        "fieldZ": set()
    }
    for item in items:
        if isinstance(item, ttypes.ItemA):
            summary["fieldX"].add(str(item.fieldX))
            summary["fieldY"].add(str(list_to_string(item.fieldY)))
            if item.fieldZ:
                summary["fieldZ"].add(str(item.fieldZ))
        elif isinstance(item, ttypes.ItemB):
            summary["fieldX"].add(str(item.fieldX))
            if item.fieldY:
                summary["fieldY"].add(str(list_to_string(item.fieldY)))
            summary["fieldZ"].add(str(set_to_string(item.fieldZ)))
        elif isinstance(item, ttypes.ItemC):
            summary["fieldX"].add(str(bool_to_string(item.fieldX)))
        else:
            print("Unknown item type:", type(item))

    if not summary["fieldX"]:
        del summary["fieldX"]
    if not summary["fieldY"]:
        del summary["fieldY"]
    if not summary["fieldZ"]:
        del summary["fieldZ"]

    print("Summary created.")
        
    return summary
    

#REPORTS.saveSummary
def saveSummary(reportsClient, items):
    print("REPORTS.saveSummary")
    
    summary = create_summary(items)
    accurate = reportsClient.saveSummary(summary)
    print("Summary is{}accurate.".format(" " if accurate else " NOT "))

    print("END_OF_REPORTS.saveSummary")


def process_args():
    print("Processing command line arguments...")

    # First command line argument is the filename, 'client.py'
    # The next command line arguments should be: itemType{1,3} itemLimit, itemCount

    # No command line arguments
    if len(sys.argv) <= 1:
        print("No command line arguments, using the default values.")
        return

    # At least one command line argument

    # Skip the filename
    argv = sys.argv[1:]

    if not (3 <= len(argv) <= 5):
        print("Invalid no. of command line arguments, using the default values.")
        return

    # Process item limit
    try:
        itemLimit = int(argv[-2])
        if itemLimit < 1:
            print("Item limit must be at least 1.")
            itemLimit = ITEM_LIMIT
    except ValueError:
        print("Item limit must be an integer.")

    # Process item count
    try:
        itemCount = int(argv[-1])
        if itemCount < 1:
            itemCount = ITEM_COUNT
    except ValueError:
        print("Item count must be an integer.")

    # Process item types
    itemTypes = set()
    for itemType in argv[:-2]:
        if itemType != "ItemA" and itemType != "ItemB" and itemType != "ItemC":
            print("Skipping over invalid item type '{}'.".format(itemType))
        else:
            itemTypes.add(itemType)
    if not itemTypes:
        print("WARNING: All item types are invalid.")
        itemTypes = ITEM_TYPES

    print("Command line arguments processed.")

    return itemTypes, itemLimit, itemCount


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
        args = process_args()
        if args:
            ITEM_TYPES, ITEM_LIMIT, ITEM_COUNT = args

        print("ITEM_TYPES=", ITEM_TYPES, type(ITEM_TYPES))
        print("ITEM_LIMIT=", ITEM_LIMIT, type(ITEM_LIMIT))
        print("ITEM_COUNT=", ITEM_COUNT, type(ITEM_COUNT))

        logIn(loginClient)
        initiateMultiple(searchClient)
        items = fetchMultiple(searchClient)
        saveSummary(reportsClient, items)
        logOut(loginClient)
    except ttypes.ProtocolException as pe:
        print("ProtocolException:", pe.message)

    # Close the connection
    transport.close()