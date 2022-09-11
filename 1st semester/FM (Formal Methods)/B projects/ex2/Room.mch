/* Room
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Room
    
USES
    RoomType
    
SETS
    ROOM
    
VARIABLES
    rooms,
    roomNumber,
    roomType
    
INVARIANT
    rooms <: ROOM &
    roomNumber : rooms --> NATURAL &
    roomType : rooms --> roomTypes
    
INITIALISATION
    rooms,
    roomNumber,
    roomType := {}, {}, {}
    
OPERATIONS
    newRoom <-- createRoom(numberVal, roomTypeVal) =
    PRE
        numberVal : NATURAL &
        roomTypeVal : roomTypes
    THEN
        ANY rm
        WHERE rm : ROOM - rooms
        THEN
            rooms := rooms \/ { rm } ||
            roomNumber(rm) := numberVal ||
            roomType(rm) := roomTypeVal ||
            newRoom := rm
        END
    END
    
END
