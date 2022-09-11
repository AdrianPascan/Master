/* Reservation
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Reservation
    
SEES
    String
    
USES
    Customer, RoomType, Room
    
SETS
    RESERVATION
    
VARIABLES
    reservations,
    reservationReference,
    reservationCheckIn,
    reservationCheckOut,
    reservationCustomer,
    reservationRoomType, 
    reservationRoom
    
INVARIANT
    reservations <: RESERVATION & 
    reservationReference : reservations --> STR &
    reservationCheckIn : reservations --> INTEGER &
    reservationCheckOut : reservations --> INTEGER &
    reservationCustomer : reservations --> customers &
    reservationRoomType : reservations --> roomTypes &
    reservationRoom: reservations --> ROOM &
    ! rsrvtn . (rsrvtn : reservations => reservationCheckIn(rsrvtn) < reservationCheckOut(rsrvtn) )

INITIALISATION
    reservations,
    reservationReference,
    reservationCheckIn,
    reservationCheckOut,
    reservationCustomer,
    reservationRoomType,
    reservationRoom := {}, {}, {}, {}, {}, {}, {}
    
OPERATIONS
    newReservation <-- createReservation(referenceVal, checkInVal, checkOutVal, roomTypeVal, customerVal) =
    PRE
        referenceVal : STR &
        referenceVal /: ran(reservationReference) &
        checkInVal : INTEGER &
        checkOutVal : INTEGER &
        checkInVal < checkOutVal &
        roomTypeVal : roomTypes &
        customerVal : customers
    THEN
        ANY rsrvtn
        WHERE rsrvtn : RESERVATION - reservations
        THEN
            reservations := reservations \/ { rsrvtn } ||
            reservationReference(rsrvtn) := referenceVal ||
            reservationCheckIn(rsrvtn) := checkInVal ||
            reservationCheckOut(rsrvtn) := checkOutVal ||
            reservationCustomer(rsrvtn) := customerVal ||
            reservationRoomType(rsrvtn) := roomTypeVal ||
            ANY rm
            WHERE rm : ROOM - rooms
            THEN
                reservationRoom(rsrvtn) := rm
            END ||
            newReservation := rsrvtn
        END
    END;
    
    cancelReservation(reservationVal) =
    PRE
        reservationVal : reservations
    THEN
        reservations := reservations - { reservationVal } ||
        reservationReference := { reservationVal } <<| reservationReference ||
        reservationCheckIn := { reservationVal } <<| reservationCheckIn ||
        reservationCheckOut := { reservationVal } <<| reservationCheckOut ||
        reservationCustomer := { reservationVal } <<| reservationCustomer ||
        reservationRoomType := { reservationVal } <<| reservationRoomType ||
        reservationRoom := { reservationVal } <<| reservationRoom
    END;
    
    allocateRoomToReservation(reservationVal, roomVal) =
    PRE
        reservationVal : reservations &
        reservationRoom(reservationVal) : ROOM - rooms &
        roomVal : rooms &
        ! rsrvtn . (rsrvtn : reservations => 
            reservationRoom(rsrvtn) /= roomVal or
            min({ reservationCheckOut(reservationVal), reservationCheckOut(rsrvtn) }) - max ({ reservationCheckIn(reservationVal), reservationCheckIn(reservationVal) }) <= 0
        )
    THEN
        reservationRoom(reservationVal) := roomVal
    END
    
END
