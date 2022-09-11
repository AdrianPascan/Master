/* User
 * Author: Adrian
 * Creation date: 07/01/2022
 */
MACHINE
    User
    
SEES
    String
    
SETS
    USER

VARIABLES
    users,
    userId,
    userEmail,
    userPassword,
    userEnabled
    
INVARIANT
    users <: USER &
    userId : users >-> NATURAL1 &
    userEmail : users --> STR &
    userPassword : users --> STR &
    userEnabled : users --> BOOL
    
INITIALISATION
    users,
    userId,
    userEmail,
    userPassword,
    userEnabled := {}, {}, {}, {}, {}
    
OPERATIONS
    newUser <-- createUser(emailValue, passwordValue, enabledValue) =
    PRE
        emailValue : STR &
        emailValue /: ran(userEmail) &
        passwordValue : STR &
        enabledValue : BOOL
    THEN
        ANY usr
        WHERE usr : USER - users
        THEN
            users := users \/ { usr } ||
            ANY idValue
            WHERE 
                idValue : NATURAL1 &
                idValue /: ran(userId)
            THEN
                userId(usr) := idValue
            END ||
            userEmail(usr) := emailValue ||
            userPassword(usr) := passwordValue ||
            userEnabled(usr) := enabledValue ||
            newUser := usr
        END
    END;
    
    changeUserEmail(userValue, passwordValue, newEmailValue) = 
    PRE
        userValue : users &
        passwordValue : STR & 
        newEmailValue : STR &
        newEmailValue /: ran(userEmail) &
        userPassword(userValue) = passwordValue
    THEN
        userEmail(userValue) := newEmailValue
    END;
    
    changeUserPassword(userValue, passwordValue, newPasswordValue) = 
    PRE
        userValue : users &
        passwordValue : STR & 
        newPasswordValue : STR &
        userPassword(userValue) = passwordValue
    THEN
        userPassword(userValue) := passwordValue
    END;
    
    changeUserEnabled(userValue, passwordValue, newEnabledValue) =
    PRE
        userValue : users &
        passwordValue : STR & 
        newEnabledValue : BOOL &
        userPassword(userValue) = passwordValue
    THEN
        userEnabled(userValue) := newEnabledValue
    END;
    
    idValue <-- getUserId(userValue) =
    PRE
        userValue : users
    THEN
        idValue := userId(userValue)
    END;
    
    emailValue <-- getUserEmail(userValue) = 
    PRE
        userValue : users
    THEN
        emailValue := userEmail(userValue)
    END;
    
    enabledValue <-- isUserEnabled(userValue) =
    PRE
        userValue: users
    THEN
        enabledValue := userEnabled(userValue)
    END;
    
    deleteUser(userValue) = 
    PRE
        userValue : users
    THEN
        users := users - { userValue } ||
        userId := { userValue } <<| userId ||
        userEmail := { userValue } <<| userEmail ||
        userPassword := { userValue } <<| userPassword ||
        userEnabled := { userValue } <<| userEnabled
    END
    
END
