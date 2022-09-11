/* Client
 * Author: Adrian
 * Creation date: 24/01/2022
 */
MACHINE
    Client
    
SETS
    CLIENT; CATEGORY = {normal, dubious, friend}
    
CONSTANTS
    discount
    
PROPERTIES
    discount : CATEGORY --> NATURAL &
    discount = {normal |-> 0, dubious |-> 0, friend |-> 20}
    
VARIABLES
    clients,
    clientCategory,
    clientAllowance,
    clientDiscount
    
INVARIANT
    clients <: CLIENT &
    clientCategory : clients --> CATEGORY &
    clientAllowance : clients --> NATURAL &
    clientDiscount : clients --> NATURAL &
    !clnt . (clnt : clients => clientDiscount(clnt) = discount(clientCategory(clnt)) )
    
INITIALISATION
    clients,
    clientCategory, 
    clientAllowance,
    clientDiscount := {}, {}, {}, {}
    
OPERATIONS
    newClient <-- createClient(categoryVal, allowanceVal) = 
    PRE
        categoryVal : CATEGORY &
        allowanceVal : NATURAL
    THEN
        ANY clnt
        WHERE clnt : CLIENT - clients
        THEN
            clients := clients \/ { clnt} ||
            clientCategory(clnt) := categoryVal ||
            clientAllowance(clnt) := allowanceVal ||
            clientDiscount(clnt) := discount(categoryVal) ||
            newClient := clnt
        END
    END

END
