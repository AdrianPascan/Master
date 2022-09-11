#ifndef DATABASE_HPP
#define DATABASE_HPP

/*
This file contains classes that can be used to generate random data for use by the server.
You can create a random set of data using the Database class like this:

    std::random_device random_device;
    std::mt19937 random_engine(random_device());
    Database database(MAX_SIZE, random_engine);

Then use the search method to retreive items of specified types:

    int index = 0;
    vector<Item*> found;

    index = database.search(std::set{std::string("ItemA")}, found, index, COUNT)

This method will start searching the database from the specified index,
find at most COUNT items of the specified type, and store them in vector found.
It will return the index from where to continue searching for more items.

*/

#include <random>
#include <memory>

#include "items.hpp"

// Defines random distributions for generating random data
struct RandomDistributions{
    // i16 fields
    std::uniform_int_distribution<> dist_int16;
    // i32 fields
    std::uniform_int_distribution<> dist_int32;
    // length of set, vector, string fields
    std::uniform_int_distribution<> dist_length;
    // string characters
    std::uniform_int_distribution<> dist_char;
    // boolean
    std::uniform_int_distribution<> dist_bool;

    RandomDistributions():
       dist_int16 (0, 30000),
       dist_int32(0, 2000000000),
       dist_length(0, 100),
       dist_char('a', 'z'),
       dist_bool(0, 1) {}

    // Stores a random string
    void random_string(std::string& target, std::mt19937& random_engine){
        target.resize(dist_length(random_engine));
        for(char& i: target){
            i = dist_char(random_engine);
        }
    }
};

// Base class for generating random instances of Item 
class ItemGenerator {
protected:
    std::mt19937& random_engine;
    RandomDistributions distributions;
public:
    ItemGenerator(std::mt19937& random_engine):
        random_engine(random_engine){}
    virtual std::unique_ptr<Item> generate() = 0;
};

// Generates instances of ItemA
class ItemAGenerator: public ItemGenerator{
public:
    ItemAGenerator(std::mt19937& random_engine):
        ItemGenerator(random_engine){}

    virtual std::unique_ptr<Item> generate(){
        std::unique_ptr<ItemA> itemA = std::make_unique<ItemA>();

        // std::string fieldX
        distributions.random_string(itemA->fieldX, random_engine);
        
        // std::vector<int16_t> fieldY
        itemA->fieldY.resize(distributions.dist_length(random_engine));
        for(int16_t& i: itemA->fieldY){
            i = distributions.dist_int16(random_engine);
        }
        // std::optional<int32_t> fieldZ
        if(distributions.dist_bool(random_engine)){
            itemA->fieldZ = distributions.dist_int32(random_engine);
        }

        return itemA;
    }
};

// Generates instances of ItemB
class ItemBGenerator: public ItemGenerator{
public:
    ItemBGenerator(std::mt19937& random_engine):
        ItemGenerator(random_engine){}
    virtual std::unique_ptr<Item> generate(){
        std::unique_ptr<ItemB> itemB = std::make_unique<ItemB>();

        // int16_t fieldX
        itemB->fieldX = distributions.dist_int16(random_engine);

        // std::optional<std::vector<std::string> > fieldY

        if(distributions.dist_bool(random_engine)){
            itemB->fieldY = std::optional(std::vector<std::string>());
            size_t c_size = distributions.dist_length(random_engine);
            for(int i = 0; i < c_size; ++i){
                std::string s;
                distributions.random_string(s, random_engine);
                (*itemB->fieldY).push_back(s);
            }
        }

        // std::set<std::string> fieldZ
        size_t b_size = distributions.dist_length(random_engine);
        for(int i = 0; i < b_size; ++i){
            std::string s;
            distributions.random_string(s, random_engine);
            itemB->fieldZ.insert(s);
        }

        return itemB;
     
    }
};

// Generates instances of ItemC
class ItemCGenerator: public ItemGenerator{
public:
    ItemCGenerator(std::mt19937& random_engine):
        ItemGenerator(random_engine){}
    virtual std::unique_ptr<Item> generate(){
        std::unique_ptr<ItemC> itemC = std::make_unique<ItemC>();
        // bool fieldX
        itemC->fieldX = distributions.dist_bool(random_engine);

        return itemC;
    }
};

class Database{
    std::mt19937& random_engine;
    std::vector<std::unique_ptr<Item> > items;
    
public:
    Database(size_t size_limit, std::mt19937& random_engine):
        random_engine(random_engine){
        fillItems(size_limit);
    }

    // Fills the list of items (search result)
    void fillItems(size_t size_limit){
        items.clear();

        ItemAGenerator genA(random_engine);
        ItemBGenerator genB(random_engine);
        ItemCGenerator genC(random_engine);

        std::vector<ItemGenerator*> generators {&genA, &genB, &genC};

        std::uniform_int_distribution<> gen_select(0, generators.size() - 1);
        std::uniform_int_distribution<> size_select(10, std::max((size_t)10,size_limit));

        unsigned size = size_select(random_engine);
        for(unsigned i = 0; i < size; ++i){
            // Select a random item generator and call it
            unsigned gen_index = gen_select(random_engine);
            items.push_back(generators[gen_index]->generate());
        }
    }

    // Searches the database for items of the specified types, and stores pointers to these items into the specified vector.
    // The search starts from a specified index and continues forwards until it finds output_limit items, or reaches the end
    // It returns the index from where to continue the search
    size_t search(std::set<std::string> types, std::vector<Item*>& output, size_t start_index, size_t output_limit) const {
        size_t n = start_index;
        for(; n < items.size() && output.size() < output_limit; ++n){
            if(types.count(items[n]->typeName())){
                output.push_back(items[n].get());
            }
        }
        return n;
    }
};

#endif