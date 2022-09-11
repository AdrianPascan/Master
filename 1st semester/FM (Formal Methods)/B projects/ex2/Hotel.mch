/* Hotel
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Hotel
    
SEES
    String
    
SETS
    HOTEL
    
VARIABLES
    hotels,
    hotelName
    
INVARIANT
    hotels <: HOTEL &
    hotelName : hotels --> STR
    
INITIALISATION
    hotels,
    hotelName := {}, {}
    
OPERATIONS
    newHotel <-- createHotel(nameVal) =
    PRE
        nameVal : STR
    THEN
        ANY htl
        WHERE htl : HOTEL - hotels
        THEN
            hotels := hotels \/ { htl } ||
            hotelName(htl) := nameVal ||
            newHotel := htl
        END
    END

END
