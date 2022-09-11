/* FluidRecord
 * Author: Adrian
 * Creation date: 07/01/2022
 */
MACHINE
    FluidRecord
    
SEES
    String
    
USES
    User
    
SETS
    FLUID_RECORD
    
VARIABLES
    fluidRecords,
    fluidRecordUser,
    fluidRecordDate,
    fluidRecordVolume
    
INVARIANT
    fluidRecords <: FLUID_RECORD &
    fluidRecordUser: fluidRecords --> users &
    fluidRecordDate: fluidRecords --> INTEGER &
    fluidRecordVolume: fluidRecords --> NATURAL &
    ! rcrd . (rcrd : fluidRecords => fluidRecordVolume(rcrd) > 0 )
    
INITIALISATION
    fluidRecords,
    fluidRecordUser,
    fluidRecordDate,
    fluidRecordVolume := {}, {}, {}, {}
    
OPERATIONS
    newFluidRecord <-- createFluidRecord(userValue, dateValue, volumeValue) = 
    PRE
        userValue : users &
        userEnabled(userValue) = TRUE &
        dateValue : INTEGER &
        volumeValue : NATURAL &
        volumeValue > 0
    THEN
        ANY rcrd
        WHERE rcrd : FLUID_RECORD - fluidRecords
        THEN
            fluidRecords := fluidRecords \/ {rcrd} ||
            fluidRecordUser(rcrd) := userValue ||
            fluidRecordDate(rcrd) := dateValue ||
            fluidRecordVolume(rcrd) := volumeValue ||
            newFluidRecord := rcrd
        END
    END;
    
    setFluidRecordVolume(fluidRecordValue, volumeValue) =
    PRE
        fluidRecordValue : fluidRecords & 
        userEnabled(fluidRecordUser(fluidRecordValue)) = TRUE &
        volumeValue : NATURAL & 
        volumeValue > 0
    THEN
        fluidRecordVolume(fluidRecordValue) := volumeValue
    END;
    
    userValue <-- getFluidRecordUser(fluidRecordValue) =
    PRE
        fluidRecordValue : fluidRecords
    THEN
        userValue := fluidRecordUser(fluidRecordValue)
    END;
    
    dateValue <-- getFluidRecordDate(fluidRecordValue) =
    PRE
        fluidRecordValue : fluidRecords
    THEN
        dateValue := fluidRecordDate(fluidRecordValue)
    END;
    
    volumeValue <-- getFluidRecordVolume(fluidRecordValue) =
    PRE
        fluidRecordValue : fluidRecords
    THEN
        volumeValue := fluidRecordVolume(fluidRecordValue)
    END;
    
    deleteFluidRecord(fluidRecordValue) = 
    PRE
        fluidRecordValue : fluidRecords &
        userEnabled(fluidRecordUser(fluidRecordValue)) = TRUE
    THEN
        fluidRecords := fluidRecords - {fluidRecordValue} ||
        fluidRecordUser := {fluidRecordValue} <<| fluidRecordUser ||
        fluidRecordDate := {fluidRecordValue} <<| fluidRecordDate ||
        fluidRecordVolume := {fluidRecordValue} <<| fluidRecordVolume
    END;
    
    deleteFluidRecordsWithUser(userValue) = 
    PRE
        userValue: users &
        userEnabled(userValue) = TRUE
    THEN
        fluidRecords := fluidRecords - fluidRecordUser ~ [ { userValue } ] ||
        fluidRecordUser := fluidRecordUser |>> { userValue } ||
        fluidRecordDate := fluidRecordUser ~ [ { userValue } ] <<| fluidRecordDate ||
        fluidRecordVolume := fluidRecordUser ~ [ { userValue } ] <<| fluidRecordVolume
    END;
    
    updateFluidRecord(fluidRecordValue, volumeValue) = 
    PRE
        fluidRecordValue : fluidRecords &
        userEnabled(fluidRecordUser(fluidRecordValue)) = TRUE &
        volumeValue : NATURAL &
        volumeValue > 0
    THEN
        fluidRecordVolume(fluidRecordValue) := volumeValue
    END
    
END
