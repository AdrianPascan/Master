/* System
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    System
    
INCLUDES
    Reservation, Room, RoomType, Hotel, Customer
    
PROMOTES
    createCustomer,
    createHotel,
    createRoomType,
    createRoom,
    createReservation, cancelReservation, allocateRoomToReservation

END
