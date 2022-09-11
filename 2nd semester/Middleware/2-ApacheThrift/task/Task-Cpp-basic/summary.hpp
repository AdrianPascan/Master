#ifndef SUMMARY_HPP
#define SUMMARY_HPP

#include <string>
#include <map>

#include "items.hpp"
#include "string_conversions.hpp"

/*

Generates a summary required by the Reports service.
Use the add methods to add the items to the report.
Then use get to get a map from field names to sets of values.

*/

class SummaryBuilder{
    std::map<std::string, std::set<std::string>> summary;
public:
    void add(const ItemA& itemA){
        summary["fieldX"].insert(itemA.fieldX);
        summary["fieldY"].insert(to_string_commas(itemA.fieldY));
        if(itemA.fieldZ)
            summary["fieldZ"].insert(std::to_string(*itemA.fieldZ));
    }
    void add(const ItemB& itemB){
        summary["fieldX"].insert(std::to_string(itemB.fieldX));
        if(itemB.fieldY)
            summary["fieldY"].insert(to_string_commas(*itemB.fieldY));
        summary["fieldZ"].insert(to_string_commas(itemB.fieldZ));
        
    }
    void add(const ItemC& itemC){
        summary["fieldX"].insert(itemC.fieldX ? "true" : "false");
    }

    const std::map<std::string, std::set<std::string>>& get() const {
        return summary;
    }
};

#endif