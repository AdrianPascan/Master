#ifndef ITEMS_HPP
#define ITEMS_HPP

/*

This file defines classes that are used to represent various data searched by the server.
It is a simple class hierarchy with a common abstract parent.

*/

#include <string>
#include <set>
#include <vector>
#include <optional>

// Base class for various types of items
class Item{
public:
    virtual ~Item(){};
    virtual const char* typeName() const = 0;
};

class ItemA: public Item{
public:
    std::string fieldX;
    std::vector<int16_t> fieldY;
    std::optional<int32_t> fieldZ;

    virtual const char* typeName() const {
        return "ItemA";
    }
};
class ItemB: public Item{
public:
    int16_t fieldX;
    std::optional<std::vector<std::string> > fieldY;
    std::set<std::string> fieldZ;

    virtual const char* typeName() const {
        return "ItemB";
    }
};
class ItemC: public Item{
public:
    bool fieldX;

    virtual const char* typeName() const {
        return "ItemC";
    }
};
#endif