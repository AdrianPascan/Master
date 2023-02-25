/*
The request department can demand a number of pizzas smaller than the pizzas the company needs to produce.
The production department can be request do make maximum 3 pizzas at once. There are three pizza types which
are picked randomly.
The request department receives confirmation on the created pizzas and ensures all pizzas the company 
needs to produce will be produced.
If the production company was requested to make pizzas above the threshold they will deny the request.

Actors: R - Request department, P - Production department.

R starts in requestPizza and P starts in waitPizzaRequest.
After R made a request to P and switches to waitingForAnswer, P checks if the request is valid and if so it 
produced the requested number of pizzas. In the end it signals the R that pizzas were made.
R acknowledge and receives the pizzas. It moves to requestPizza and makes another request until all pizzas 
necessary were made.
*/
mtype {producePizza, pizzaCreated, stoppedPizzaProduction};

chan signal = [0] of {mtype};

int pizzasToProduce = 12;
int producedPizzas = 0;

int pizzaToProduce = 1;


active proctype RequestDepartment () {
	requestPizza: atomic {
		pizzaToProduce = 1;
		do
			:: pizzaToProduce < 3 -> pizzaToProduce++
			:: break
		od;
		printf("Requesting pizza %d \n", pizzaToProduce);
		signal!producePizza;
		goto waitingForAnswer;
	};
	waitingForAnswer : atomic {
		if ::signal?pizzaCreated -> atomic {
			printf("Received pizza\n");
			goto requestPizza;
		};
		::signal?stoppedPizzaProduction -> atomic {
			printf("Can't request pizzas anymore\n");
		};
		fi
	};
}

active proctype ProductionDepartment () {
	waitPizzaRequest : atomic {
		if ::signal?producePizza -> atomic {
			if ::producedPizzas < pizzasToProduce -> atomic {
				producedPizzas++;
				printf("Produced pizzas %d \n", producedPizzas);
				if ::pizzaToProduce == 1 -> atomic {
					printf("Producing Capriciosa\n");
				};
				 ::pizzaToProduce == 2 -> atomic {
					printf("Producing Quattro Formaggi\n");
				};
				::pizzaToProduce == 3 -> atomic {
					printf("Producing Prosciutto e Funghi\n");
				};
				fi;
				signal!pizzaCreated;
				goto waitPizzaRequest;
			};
			:: producedPizzas == pizzasToProduce -> atomic {
				signal!stoppedPizzaProduction;
			};
			fi;
		};
		fi;
 	};
}


// [](producedPizzas<=pizzasToProduce)
// <>(producedPizzas==pizzasToProduce)