/* Record
 * Author: Adrian
 * Creation date: 07/01/2022
 */
MACHINE
    Record
    
SEES
    String
    
USES
    User, Food
    
SETS
    RECORD; MEALS = {breakfast, lunch, dinner, snack}
    
VARIABLES
    records,
    recordUser,
    recordFood,
    recordDate,
    recordMeal,
    recordQuantity
    
INVARIANT
    records <: RECORD &
    recordUser : records --> users &
    recordFood : records --> foods &
    recordDate : records --> INTEGER &
    recordMeal : records >-> MEALS &
    recordQuantity : records --> NATURAL &
    ! rcrd . (rcrd : records => 
        recordMeal(rcrd) : MEALS &
        recordQuantity(rcrd) > 0
    )
    
INITIALISATION
    records,
    recordUser,
    recordFood,
    recordDate,
    recordMeal,
    recordQuantity := {}, {}, {}, {}, {}, {}
    
OPERATIONS
    newRecord <-- createFoodRecord(userValue, foodValue, dateValue, mealValue, quantityValue) = 
    PRE
        userValue : users &
        foodValue : foods &
        dateValue : INTEGER &
        mealValue : MEALS & 
        quantityValue : NATURAL &
        quantityValue > 0
    THEN
        ANY rcrd
        WHERE rcrd : RECORD - records
        THEN
            records := records \/ {rcrd} ||
            recordUser(rcrd) := userValue ||
            recordFood(rcrd) := foodValue ||
            recordDate(rcrd) := dateValue ||
            recordMeal(rcrd) := mealValue ||
            recordQuantity(rcrd) := quantityValue ||
            newRecord := rcrd
        END
    END;
    
    setRecordQuantity(recordValue, quantityValue) =
    PRE
        recordValue : records & 
        quantityValue : NATURAL & 
        quantityValue > 0
    THEN
        recordQuantity(recordValue) := quantityValue
    END;
    
    userValue <-- getRecordUser(recordValue) =
    PRE
        recordValue : records
    THEN
        userValue := recordUser(recordValue)
    END;
    
    foodValue <-- getRecordFood(recordValue) =
    PRE
        recordValue : records
    THEN
        foodValue := recordFood(recordValue)
    END;
    
    dateValue <-- getRecordDate(recordValue) =
    PRE
        recordValue : records
    THEN
        dateValue := recordDate(recordValue)
    END;
    
    mealValue <-- getRecordMeal(recordValue) =
    PRE
        recordValue : records
    THEN
        mealValue := recordMeal(recordValue)
    END;
    
    quantityValue <-- getRecordQuantity(recordValue) =
    PRE
        recordValue : records
    THEN
        quantityValue := recordQuantity(recordValue)
    END;
    
    // CHECK
    deleteRecord(recordValue) = 
    PRE
        recordValue : records
    THEN
        records := records - {recordValue} ||
        recordUser := {recordValue} <<| recordUser ||
        recordFood := {recordValue} <<| recordFood ||
        recordDate := {recordValue} <<| recordDate ||
        recordMeal := {recordValue} <<| recordMeal ||
        recordQuantity := {recordValue} <<| recordQuantity
    END;
    
    deleteRecordsWithUser(userValue) = 
    PRE
        userValue: users
    THEN
        records := records - recordUser ~ [ { userValue } ] ||
        recordUser := recordUser |>> { userValue } ||
        recordFood := recordUser ~ [ { userValue } ] <<| recordFood ||
        recordDate := recordUser ~ [ { userValue } ] <<| recordDate ||
        recordMeal := recordUser ~ [ { userValue } ] <<| recordMeal ||
        recordQuantity := recordUser ~ [ { userValue } ] <<| recordQuantity
    END;
    
    deleteRecordsWithFood(foodValue) = 
    PRE
        foodValue : foods
    THEN
        records := records - recordFood ~ [ { foodValue } ] ||
        recordFood := recordFood |>> { foodValue } ||
        recordUser := recordFood ~ [ { foodValue } ] <<| recordUser ||
        recordDate := recordFood ~ [ { foodValue } ] <<| recordDate ||
        recordMeal := recordFood ~ [ { foodValue } ] <<| recordMeal ||
        recordQuantity := recordFood ~ [ { foodValue } ] <<| recordQuantity
    END
    
END
