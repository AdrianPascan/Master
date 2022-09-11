/* Competition
 * Author: Adrian
 * Creation date: 25/01/2022
 */
MACHINE
    Competition
    
SETS
    CONTESTANT
    
CONSTANTS
    maxno
    
PROPERTIES
    maxno : NATURAL1
    
VARIABLES
    contestants,
    entryNos
    
INVARIANT
    contestants <: CONTESTANT &
    card(contestants) <= maxno &
    entryNos : contestants +-> NATURAL &
    ! cntstnt . (cntstnt : contestants => 
        entryNos(cntstnt) <= card(contestants)
    )
    
INITIALISATION
    contestants,
    entryNos := {}, {}

OPERATIONS
    register_contestant = 
    PRE
        card(contestants) <= maxno
    THEN
        ANY cntstnt
        WHERE cntstnt : CONTESTANT - contestants
        THEN
            contestants := contestants \/ { cntstnt }
        END
    END;
    
    assign_entry_no(cntstnt) = 
    PRE
        cntstnt : contestants
    THEN
        ANY entryNo
        WHERE entryNo : NATURAL &
            entryNo /: ran(entryNos) &
            entryNo <= card(contestants)
        THEN
            entryNos(cntstnt) := entryNo
        END
    END
    
END
