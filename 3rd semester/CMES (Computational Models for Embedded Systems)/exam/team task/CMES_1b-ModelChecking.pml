/*
	Statement: Coffee Machine
*/


mtype = {makeEspresso, makeLatte, makeFlatWhite, makeCapuccino, makeMacchiato}

chan signal = [0] of {mtype};
byte noCoffeeProduced = 0;
byte noCoffeeToProduce = 12;


active proctype Person() {

	starting: atomic { 
		printf("Person wants a ton of coffee");
		signal!makeEspresso;
        signal!makeMacchiato;
        signal!makeMacchiato;
        signal!makeLatte;
        signal!makeCapuccino;
        signal!makeCapuccino;
        signal!makeFlatWhite;
        signal!makeMacchiato;
        signal!makeCapuccino;
        signal!makeEspresso;
        signal!makeLatte;
        signal!makeLatte;
	};

}

active proctype CoffeeMachine() {

	waiting: atomic {
		if
		:: signal?makeEspresso -> atomic {
			noCoffeeProduced = noCoffeeProduced + 1;
            if
            :: noCoffeeProduced == noCoffeeToProduce -> atomic {
                goto descale;
                };
            :: noCoffeeProduced < noCoffeeToProduce -> atomic {
                goto waiting;
                };
            fi;
            };

        :: signal?makeCapuccino -> atomic {
			noCoffeeProduced = noCoffeeProduced + 1;
            if
            :: noCoffeeProduced == noCoffeeToProduce -> atomic {
                goto descale;
                };
            :: noCoffeeProduced < noCoffeeToProduce -> atomic {
                goto waiting;
                };
            fi;
            };
        
        :: signal?makeFlatWhite -> atomic {
			noCoffeeProduced = noCoffeeProduced + 1;
            if
            :: noCoffeeProduced == noCoffeeToProduce -> atomic {
                goto descale;
                };
            :: noCoffeeProduced < noCoffeeToProduce -> atomic {
                goto waiting;
                };
            fi;
            };

        :: signal?makeMacchiato -> atomic {
			noCoffeeProduced = noCoffeeProduced + 1;
            if
            :: noCoffeeProduced == noCoffeeToProduce -> atomic {
                goto descale;
                };
            :: noCoffeeProduced < noCoffeeToProduce -> atomic {
                goto waiting;
                };
            fi;
            };
        :: signal?makeLatte -> atomic {
			noCoffeeProduced = noCoffeeProduced + 1;
            if
            :: noCoffeeProduced == noCoffeeToProduce -> atomic {
                goto descale;
                };
            :: noCoffeeProduced < noCoffeeToProduce -> atomic {
                goto waiting;
                };
            fi;
            };
    	fi;
	};

    descale: atomic {
        noCoffeeProduced = 0;
        
    };

}