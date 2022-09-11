/* Jukebox
 * Author: Adrian
 * Creation date: 13/01/2022
 */
MACHINE
    Jukebox

SETS
    TRACK
    
CONSTANTS
    trackId, limit
    
PROPERTIES
    trackId : TRACK >-> NATURAL1 &
    limit : NATURAL1
    
VARIABLES
    credits, playset
    
INVARIANT
    credits : NATURAL &
    credits <= limit &
    playset <: TRACK 
    
INITIALISATION
    credits, playset := 0, {}
    
OPERATIONS
    pay(noCredits) = 
    PRE
        noCredits : NATURAL & 
        credits + noCredits <= limit
    THEN
        credits := credits + noCredits
    END;
    
    select(tId) = 
    PRE
        tId : ran(trackId) &
        trackId~(tId)/:playset &
        credits > 0
    THEN
        playset := playset \/ { trackId~(tId)} ||
        CHOICE
            credits := credits - 1
        OR
            skip
        END
    END;
    
    tId <-- play = 
    PRE
        playset /=  {}
    THEN
        ANY tr
        WHERE tr : playset
        THEN
            playset := playset - {tr} ||
            tId := trackId(tr)
        END
    END
END
