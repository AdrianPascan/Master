/* Customer
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Customer
    
SEES
    String

SETS
    CUSTOMER
    
VARIABLES
    customers,
    customerName, 
    customerPostalAddress,
    customerEmailAddress,
    customerPhoneNumber
    
INVARIANT
    customers <: CUSTOMER &
    customerName : customers --> STR &
    customerPostalAddress : customers --> STR &
    customerEmailAddress : customers --> STR &
    customerPhoneNumber : customers --> STR
    
INITIALISATION
    customers,
    customerName, 
    customerPostalAddress,
    customerEmailAddress,
    customerPhoneNumber := {}, {}, {}, {}, {}
    
OPERATIONS
    newCustomer <-- createCustomer(nameVal, postalAddressVal, emailAddressVal, phoneNumberVal) =
    PRE
        nameVal : STR &
        postalAddressVal : STR &
        emailAddressVal : STR &
        phoneNumberVal : STR
    THEN
        ANY cstmr
        WHERE cstmr : CUSTOMER - customers
        THEN
            customers := customers \/ { cstmr } ||
            customerName(cstmr) := nameVal ||
            customerPostalAddress(cstmr) := postalAddressVal ||
            customerEmailAddress(cstmr) := emailAddressVal ||
            customerPhoneNumber(cstmr) := phoneNumberVal ||
            newCustomer := cstmr
        END
    END

END
