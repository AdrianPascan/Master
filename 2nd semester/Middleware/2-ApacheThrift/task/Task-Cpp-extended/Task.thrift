// Interface for a client-server application
// Do not modify, except when required by comments

// You may change the namespace for a particular language
namespace * Task2

// Thrown when an invalid key is used for login
exception InvalidKeyException{
    // The invalid key that was used
    1: i32 invalidKey
    // The expected key that will succeed
    2: i32 expectedKey
}

// Thrown when the client calls remote functions with wrong arguments or in wrong order
exception ProtocolException{
    1: string message
}
// Service handling user identification
service Login{
    // Lets the user log in to be able to use other services
    // throws InvalidKeyError if the key is not correct
    // throws ProtocolException if already logged in
    void logIn(1: string userName, 2: i32 key) throws (1: InvalidKeyException invalidKeyException, 2: ProtocolException protocolException)

    oneway void logOut();
}

// One type of items that the application works with
struct ItemA{
  1: required string fieldX
  2: required list<i16> fieldY
  3: optional i32 fieldZ
}

// TODO: add other types of items
struct ItemB{
  1: required i16 fieldX
  2: optional list<string> fieldY
  3: required set<string> fieldZ
}

struct ItemC{
  1: required bool fieldX
}

// Wrapper struct for items
// type = 'ItemA'|'ItemB'|'ItemC'
struct Item{
  1: optional ItemA itemA;
  2: optional ItemB itemB;
  3: optional ItemC itemC;
  4: required string type;
}

// Status of fetching items
enum FetchStatus{
    // The search is still running and may return more items later
    // Call fetchResult again after waiting for a while
    PENDING = 1
    // The item field of FetchResult contains an item
    ITEM = 2
    // All items were fetched
    ENDED = 3
}

// Result of an attempt to fetch an item
struct FetchResult{
    // Tells whether an item is fetched, the search is still running or all items were fetched
    1: FetchStatus status
    // If status is ITEM, contains an item
    2: ItemA item
}

// Result of an attempt to fetch multiple items
struct FetchMultipleResult{
    // Tells whether items are fetched, the search is still running or all items were fetched
    1: FetchStatus status
    // If status is ITEM, contains items
    2: list<Item> items
}

// Service handling item search
service Search{
    // Fetches a single item.
    // Returns a result that can either contain an item,
    // or indicate that the call must be repeated later to get the item,
    // or indicate that no more items are available.
    // By repeatedly calling this function, the client can fetch all items one-by-one.
    // Throws ProtocolException if not logged in
    FetchResult fetch() throws (1: ProtocolException protocolException);

    // TODO: modify service Search and struct FetchResult so that
    // 1) The client of the service can initiate a search, specifying which types of items to search for and limit
    // 2) After initiating a search, the client can use the fetch function to fetch multiple items at the same time.
    //    The client can fetch all items by repeatedly calling this function until it returns status ENDED.
    // You may define new types (struct, unions, typedefs) for use by this service.
    // You must maintain full run-time compatibility with the previous version of the interface.

    // Initiates the multiple search by specifying:
    // types of items to be fetched (itemTypes)
    // max. count of items to be fetched (itemLimit).
    // Throws ProtocolException if not logged in or multiple search already initiated or itemLimit < 1 or invalid item types
    void initiateMultiple(1: set<string> itemTypes, 2: i32 itemLimit) throws (1: ProtocolException protocolException);

    // Fetches max. itemCount items.
    // Returns a result that can either contain items,
    // or indicate that the call must be repeated later to get the items,
    // or indicate that no more items are available.
    // By repeatedly calling this function, the client can fetch all items.
    // Throws ProtocolException if not logged in or multiple search not initiated or itemCount < 1
    FetchMultipleResult fetchMultiple(1: i32 itemCount) throws (1: ProtocolException protocolException);
}

// Type of a summary
typedef map<string, set<string>> Summary

// Service for receiving a summary
service Reports{
    // Sends the summary to the server
    // throws ProtocolException if not logged in, or no search was performed
    // returns true if the contents of the summary match the last search
    // returns false otherwise
    bool saveSummary(1: Summary summary) throws (1: ProtocolException protocolException);
}
