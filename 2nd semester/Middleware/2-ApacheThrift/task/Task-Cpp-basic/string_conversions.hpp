#ifndef STRING_CONVERSIONS_HPP
#define STRING_CONVERSIONS_HPP

#include <vector>
#include <set>
#include <string>
#include <sstream>

// Converts a vector of values into a comma-separated string
template <typename T>
std::string to_string_commas(const std::vector<T>& vs){
    std::stringstream ss;
    bool first = true;
    for(const auto& v: vs){
        if(first){
            first = false;
        }
        else{
            ss << ',';
        }
        ss << v;
    }
    return ss.str();
}

// Converts a set of values into a comma-separated string, where items are in ascending order
template <typename T>
std::string to_string_commas(const std::set<T>& vs){
    std::vector<T> s(vs.begin(), vs.end());
    return to_string_commas(s);
}

// Splits a comma-separated string and converts it to a set
std::set<std::string> to_set_commas(const std::string& input) {
    std::set<std::string> result_set;
    size_t index = 0;
    while(index != std::string::npos){
        size_t next = input.find(',', index);
        if(next == std::string::npos){
            result_set.insert(input.substr(index, std::string::npos));
            break;
        }
        else{
            result_set.insert(input.substr(index, next - index));
            index = next + 1;
        }
    }
    return result_set;
}
#endif