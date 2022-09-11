/* Food
 * Author: Adrian
 * Creation date: 07/01/2022
 */
MACHINE
    Food
    
SEES
    String
    
SETS
    FOOD

VARIABLES
    foods,
    foodId,
    foodName,
    foodCaloriesPer100g,
    foodCarbsPer100g,
    foodFatPer100g,
    foodProteinPer100g,
    foodSaltPer100g
    
INVARIANT
    foods <: FOOD &
    foodId : foods >-> NATURAL1 &
    foodName : foods --> STR &
    foodCaloriesPer100g : foods --> NATURAL &
    foodCarbsPer100g : foods --> NATURAL &
    foodFatPer100g : foods --> NATURAL &
    foodProteinPer100g : foods --> NATURAL &
    foodSaltPer100g : foods --> NATURAL &
    !fd . (fd : foods => 
        foodCaloriesPer100g(fd) <= 100 &
        foodCarbsPer100g(fd) <= 100 &
        foodFatPer100g(fd) <= 100 &
        foodProteinPer100g(fd) <= 100 &
        foodSaltPer100g(fd) <= 100
    )
    
INITIALISATION
    foods,
    foodId,
    foodName,
    foodCaloriesPer100g,
    foodCarbsPer100g,
    foodFatPer100g,
    foodProteinPer100g,
    foodSaltPer100g := {}, {}, {}, {}, {}, {}, {}, {}
    
OPERATIONS
    newFood <-- createFood(nameValue, caloriesPer100gValue, carbsPer100gValue, fatPer100gValue, proteinPer100gValue, saltPer100gValue) = 
    PRE
        nameValue : STR &
        caloriesPer100gValue : NATURAL &
        carbsPer100gValue : NATURAL &
        fatPer100gValue : NATURAL & 
        proteinPer100gValue : NATURAL &
        saltPer100gValue: NATURAL &
        caloriesPer100gValue <= 100 &
        carbsPer100gValue <= 100 &
        fatPer100gValue <= 100 & 
        proteinPer100gValue <= 100 &
        saltPer100gValue <= 100
    THEN
        ANY fd
        WHERE fd : FOOD - foods
        THEN
            foods := foods \/ {fd} ||
            ANY idValue
            WHERE 
                idValue : NATURAL1 & 
                idValue /: ran(foodId)
            THEN 
                foodId(fd) := idValue
            END ||
            foodName(fd) := nameValue ||
            foodCaloriesPer100g(fd) := caloriesPer100gValue ||
            foodCarbsPer100g(fd) := carbsPer100gValue ||
            foodFatPer100g(fd) := fatPer100gValue ||
            foodProteinPer100g(fd) := proteinPer100gValue ||
            foodSaltPer100g(fd) := saltPer100gValue ||
            newFood := fd
        END
    END;
    
    setFoodName(foodValue, nameValue) = 
    PRE
        foodValue : foods &
        nameValue : STR
    THEN
        foodName(foodValue) := nameValue
    END;
    
    setFoodCaloriesPer100g(foodValue, caloriesPer100gValue) = 
    PRE
        foodValue : foods &
        caloriesPer100gValue : NATURAL &
        caloriesPer100gValue <= 100
    THEN
        foodCaloriesPer100g(foodValue) := caloriesPer100gValue
    END;
    
    setFoodCarbsPer100g(foodValue, carbsPer100gValue) = 
    PRE
        foodValue : foods &
        carbsPer100gValue : NATURAL &
        carbsPer100gValue <= 100
    THEN
        foodCarbsPer100g(foodValue) := carbsPer100gValue
    END;
    
    setFoodFatPer100g(foodValue, fatPer100gValue) = 
    PRE
        foodValue : foods &
        fatPer100gValue : NATURAL &
        fatPer100gValue <= 100
    THEN
        foodFatPer100g(foodValue) := fatPer100gValue
    END;
    
    setFoodProteinPer100g(foodValue, proteinPer100gValue) = 
    PRE
        foodValue : foods &
        proteinPer100gValue : NATURAL &
        proteinPer100gValue <= 100
    THEN
        foodProteinPer100g(foodValue) := proteinPer100gValue
    END;
    
    setFoodSaltPer100g(foodValue, saltPer100gValue) = 
    PRE
        foodValue : foods &
        saltPer100gValue : NATURAL &
        saltPer100gValue <= 100
    THEN
        foodSaltPer100g(foodValue) := saltPer100gValue
    END;
    
    idValue <-- getFoodId(foodValue) =
    PRE
        foodValue : foods
    THEN
        idValue := foodId(foodValue)
    END;
    
    nameValue <-- getFoodName(foodValue) =
    PRE
        foodValue : foods
    THEN
        nameValue := foodName(foodValue)
    END;
    
    caloriesPer100gValue <-- getFoodCaloriesPer100g(foodValue) =
    PRE
        foodValue : foods
    THEN
        caloriesPer100gValue := foodCaloriesPer100g(foodValue)
    END;
    
    carbsPer100gValue <-- getFoodCarbsPer100g(foodValue) =
    PRE
        foodValue : foods
    THEN
        carbsPer100gValue := foodCarbsPer100g(foodValue)
    END;
    
    fatPer100gValue <-- getFoodFatPer100g(foodValue) =
    PRE
        foodValue : foods
    THEN
        fatPer100gValue := foodFatPer100g(foodValue)
    END;
    
    proteinPer100gValue <-- getFoodProteinPer100g(foodValue) =
    PRE
        foodValue : foods
    THEN
        proteinPer100gValue := foodProteinPer100g(foodValue)
    END;
    
    saltPer100gValue <-- getFoodSaltPer100g(foodValue) =
    PRE
        foodValue : foods
    THEN
        saltPer100gValue := foodSaltPer100g(foodValue)
    END;
    
    deleteFood(foodValue) = 
    PRE
        foodValue : foods
    THEN
        foods := foods - { foodValue } ||
        foodId := { foodValue } <<| foodId ||
        foodName := { foodValue } <<| foodName ||
        foodCaloriesPer100g := { foodValue } <<| foodCaloriesPer100g ||
        foodCarbsPer100g := { foodValue } <<| foodCarbsPer100g ||
        foodFatPer100g := { foodValue } <<| foodFatPer100g ||
        foodProteinPer100g := { foodValue } <<| foodProteinPer100g ||
        foodSaltPer100g := { foodValue } <<| foodSaltPer100g
    END;
    
    updateFood(foodRecordValue, nameValue, caloriesPer100gValue, carbsPer100gValue, fatPer100gValue, proteinPer100gValue, saltPer100gValue) = 
    PRE
        foodRecordValue : foods &
        nameValue : STR &
        caloriesPer100gValue : NATURAL &
        carbsPer100gValue : NATURAL &
        fatPer100gValue : NATURAL & 
        proteinPer100gValue : NATURAL &
        saltPer100gValue : NATURAL &
        caloriesPer100gValue <= 100 &
        carbsPer100gValue <= 100 &
        fatPer100gValue <= 100 & 
        proteinPer100gValue <= 100 &
        saltPer100gValue <= 100
    THEN
        foodName(foodRecordValue) := nameValue ||
        foodCaloriesPer100g(foodRecordValue) := caloriesPer100gValue ||
        foodCarbsPer100g(foodRecordValue) := carbsPer100gValue ||
        foodFatPer100g(foodRecordValue) := fatPer100gValue ||
        foodProteinPer100g(foodRecordValue) := proteinPer100gValue ||
        foodSaltPer100g(foodRecordValue) := saltPer100gValue
    END

END
