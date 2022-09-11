/* TrackingSystem
 * Author: Adrian
 * Creation date: 07/01/2022
 */
MACHINE
    TrackingSystem
    
INCLUDES
    FoodRecord, FluidRecord, User, Food
    
PROMOTES
    createUser,
    getUserId, getUserEmail,
    changeUserEmail, changeUserPassword,
    createFood,
    updateFood,
    getFoodId, getFoodName, getFoodCaloriesPer100g, getFoodCarbsPer100g, getFoodFatPer100g, getFoodProteinPer100g, getFoodSaltPer100g,
    setFoodName, setFoodCaloriesPer100g, setFoodCarbsPer100g, setFoodFatPer100g, setFoodProteinPer100g, setFoodSaltPer100g,
    createFoodRecord,
    deleteFoodRecord,
    updateFoodRecord,
    getFoodRecordUser, getFoodRecordFood, getFoodRecordDate, getFoodRecordMeal, getFoodRecordQuantity,
    setFoodRecordQuantity,
    createFluidRecord,
    deleteFluidRecord,
    updateFluidRecord,
    getFluidRecordUser, getFluidRecordDate, getFluidRecordVolume,
    setFluidRecordVolume
    
OPERATIONS
    deleteUserAndRecords(userValue) = 
    PRE
        userValue: users
    THEN
        deleteFoodRecordsWithUser(userValue) ||
        deleteFluidRecordsWithUser(userValue) ||
        deleteUser(userValue)
    END;
    
    deleteFoodAndRecords(foodValue) = 
    PRE
        foodValue : foods
    THEN
        deleteFoodRecordsWithFood(foodValue) ||
        deleteFood(foodValue)
    END

END
