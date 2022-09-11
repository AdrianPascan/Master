/* RoomType
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    RoomType
    
SEES
    String
    
USES
    Hotel
    
SETS
    ROOM_TYPE
    
VARIABLES
    roomTypes,
    roomTypeHotel,
    roomTypeName,
    roomTypePrice
    
INVARIANT
    roomTypes <: ROOM_TYPE &
    roomTypeHotel : roomTypes --> hotels &
    roomTypeName : roomTypes --> STR &
    roomTypePrice : roomTypes --> NATURAL
    
INITIALISATION
    roomTypes,
    roomTypeHotel,
    roomTypeName,
    roomTypePrice := {}, {}, {}, {}
    
OPERATIONS
    newRoomType <-- createRoomType(hotelVal, nameVal, priceVal) =
    PRE
        hotelVal : hotels &
        nameVal : STR &
        priceVal : NATURAL
    THEN
        ANY rmtp
        WHERE rmtp : ROOM_TYPE - roomTypes
        THEN
            roomTypes := roomTypes \/ { rmtp } ||
            roomTypeHotel(rmtp) := hotelVal ||
            roomTypeName(rmtp) := nameVal ||
            roomTypePrice(rmtp) := priceVal ||
            newRoomType := rmtp
        END
    END

END
