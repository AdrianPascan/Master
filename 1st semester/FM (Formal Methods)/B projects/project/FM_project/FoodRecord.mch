/* FoodRecord
 * Author: Adrian
 * Creation date: 07/01/2022
 */
MACHINE
    FoodRecord
    
SEES
    String, Meal
    
USES
    User, Food
    
SETS
    FOOD_RECORD
    
VARIABLES
    foodRecords,
    foodRecordUser,
    foodRecordFood,
    foodRecordDate,
    foodRecordMeal,
    foodRecordQuantity
    
INVARIANT
    foodRecords <: FOOD_RECORD &
    foodRecordUser : foodRecords --> users &
    foodRecordFood : foodRecords --> foods &
    foodRecordDate : foodRecords --> INTEGER &
    foodRecordMeal : foodRecords --> MEALS &
    foodRecordQuantity : foodRecords --> NATURAL &
    ! rcrd . (rcrd : foodRecords => 
        foodRecordMeal(rcrd) : MEALS &
        foodRecordQuantity(rcrd) > 0
    )
    
INITIALISATION
    foodRecords,
    foodRecordUser,
    foodRecordFood,
    foodRecordDate,
    foodRecordMeal,
    foodRecordQuantity := {}, {}, {}, {}, {}, {}
    
OPERATIONS
    newFoodRecord <-- createFoodRecord(userValue, foodValue, dateValue, mealValue, quantityValue) = 
    PRE
        userValue : users &
        userEnabled(userValue) = TRUE &
        foodValue : foods &
        dateValue : INTEGER &
        mealValue : MEALS & 
        quantityValue : NATURAL &
        quantityValue > 0
    THEN
        ANY rcrd
        WHERE rcrd : FOOD_RECORD - foodRecords
        THEN
            foodRecords := foodRecords \/ { rcrd } ||
            foodRecordUser(rcrd) := userValue ||
            foodRecordFood(rcrd) := foodValue ||
            foodRecordDate(rcrd) := dateValue ||
            foodRecordMeal(rcrd) := mealValue ||
            foodRecordQuantity(rcrd) := quantityValue ||
            newFoodRecord := rcrd
        END
    END;
    
    setFoodRecordQuantity(foodRecordValue, quantityValue) =
    PRE
        foodRecordValue : foodRecords & 
        userEnabled(foodRecordUser(foodRecordValue)) = TRUE &
        quantityValue : NATURAL & 
        quantityValue > 0
    THEN
        foodRecordQuantity(foodRecordValue) := quantityValue
    END;
    
    userValue <-- getFoodRecordUser(foodRecordValue) =
    PRE
        foodRecordValue : foodRecords
    THEN
        userValue := foodRecordUser(foodRecordValue)
    END;
    
    foodValue <-- getFoodRecordFood(foodRecordValue) =
    PRE
        foodRecordValue : foodRecords
    THEN
        foodValue := foodRecordFood(foodRecordValue)
    END;
    
    dateValue <-- getFoodRecordDate(foodRecordValue) =
    PRE
        foodRecordValue : foodRecords
    THEN
        dateValue := foodRecordDate(foodRecordValue)
    END;
    
    mealValue <-- getFoodRecordMeal(foodRecordValue) =
    PRE
        foodRecordValue : foodRecords
    THEN
        mealValue := foodRecordMeal(foodRecordValue)
    END;
    
    quantityValue <-- getFoodRecordQuantity(foodRecordValue) =
    PRE
        foodRecordValue : foodRecords
    THEN
        quantityValue := foodRecordQuantity(foodRecordValue)
    END;
    
    deleteFoodRecord(foodRecordValue) = 
    PRE
        foodRecordValue : foodRecords & 
        userEnabled(foodRecordUser(foodRecordValue)) = TRUE
    THEN
        foodRecords := foodRecords - { foodRecordValue } ||
        foodRecordUser := { foodRecordValue } <<| foodRecordUser ||
        foodRecordFood := { foodRecordValue } <<| foodRecordFood ||
        foodRecordDate := { foodRecordValue } <<| foodRecordDate ||
        foodRecordMeal := { foodRecordValue } <<| foodRecordMeal ||
        foodRecordQuantity := { foodRecordValue } <<| foodRecordQuantity
    END;
    
    deleteFoodRecordsWithUser(userValue) = 
    PRE
        userValue: users &
        userEnabled(userValue) = TRUE
    THEN
        foodRecords := foodRecords - foodRecordUser ~ [ { userValue } ] ||
        foodRecordUser := foodRecordUser |>> { userValue } ||
        foodRecordFood := foodRecordUser ~ [ { userValue } ] <<| foodRecordFood ||
        foodRecordDate := foodRecordUser ~ [ { userValue } ] <<| foodRecordDate ||
        foodRecordMeal := foodRecordUser ~ [ { userValue } ] <<| foodRecordMeal ||
        foodRecordQuantity := foodRecordUser ~ [ { userValue } ] <<| foodRecordQuantity
    END;
    
    deleteFoodRecordsWithFood(foodValue) = 
    PRE
        foodValue : foods
    THEN
        foodRecords := foodRecords - foodRecordFood ~ [ { foodValue } ] ||
        foodRecordFood := foodRecordFood |>> { foodValue } ||
        foodRecordUser := foodRecordFood ~ [ { foodValue } ] <<| foodRecordUser ||
        foodRecordDate := foodRecordFood ~ [ { foodValue } ] <<| foodRecordDate ||
        foodRecordMeal := foodRecordFood ~ [ { foodValue } ] <<| foodRecordMeal ||
        foodRecordQuantity := foodRecordFood ~ [ { foodValue } ] <<| foodRecordQuantity
    END;
    
    updateFoodRecord(foodRecordValue, quantityValue) = 
    PRE
        foodRecordValue : foodRecords &
        userEnabled(foodRecordUser(foodRecordValue)) = TRUE &
        quantityValue : NATURAL &
        quantityValue > 0
    THEN
        foodRecordQuantity(foodRecordValue) := quantityValue
    END
    
END

