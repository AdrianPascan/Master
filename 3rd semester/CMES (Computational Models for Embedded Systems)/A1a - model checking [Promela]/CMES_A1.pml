/*
If a student uses an electric scooter or a bike to get to the university they will recieve a green cupon that gives them free access for one entrance to the UBB library or gym.

Actors: S - Student, G - Guardian.
S starts in the "entrance" state and G starts in the "waiting" state.
When S asks G for a library or gym ticket, G is going to switch to the "serving" state.
Next G asks S if they have a green cupon.
If S does not have a green cupon, G will asks S to pay for the ticket and S will pay the ticket.
G will let S enter the library or the gym of the university.
*/

mtype {SWantsTicket, doYouHaveACupon, yes, no, youHaveToPay, SPaid, youCanEnter};

chan signal = [0] of {mtype};

bool want_a_ticket = false;
bool ask_for_cupon = false;
bool answer_is_yes = false;
bool answer_is_no = false;
bool is_paying = false;
bool can_enter = false;

active proctype Student() {
	nextInLine: atomic {
		printf("Student wants to enter in library or gym. \n");
		want_a_ticket = true;
		signal!SWantsTicket;
		goto answering;
	};
	answering: {
		if
		::	signal?doYouHaveACupon -> atomic {
				printf("Yes I have a cupon. \n");
				answer_is_yes = true;
				signal!yes;
				goto entering;
			};
		::	signal?doYouHaveACupon -> atomic {
				printf("No I do not have a cupon. \n");
				answer_is_no = true;
				signal!no;
				goto paying;
			};
		fi;
	};
	paying: {
		signal?youHaveToPay -> atomic {
			printf("The Student pays the ticket. \n");
			is_paying = true;
			signal!SPaid;
			goto entering;
		};
	};
	entering: {
		signal?youCanEnter -> atomic {
			printf("The Student entered the library or gym. \n");
		};
	};
}

active proctype Guardian() {
	waiting: atomic {
		signal?SWantsTicket -> atomic {
			printf("Guardian asks Student if they have a green cupon. \n");
			ask_for_cupon = true;
			signal!doYouHaveACupon;
			goto waitingAnswer;
		};
	};
	waitingAnswer: {
		if
		::	signal?yes -> atomic {
				goto giveTicket;
			};

		::	signal?no -> atomic {
				printf("The Student must pay the ticket. \n");
				goto askForPayment;
			};
		fi;
	};
	askForPayment: atomic {
		printf("The Guardian asks the Student to pay the ticket. \n");
		signal!youHaveToPay;
		goto waitingForPayment;
	};
	waitingForPayment: {
		signal?SPaid -> atomic {
			printf("The Student paid the ticket. \n");
			goto giveTicket;
		};
	};
	giveTicket: atomic {
		printf("The Guardian gives the Student a ticket. \n");
		can_enter = true;
		signal!youCanEnter;
	};
}


//Always if the answer is yes, the student isn't paying
//[](answer_is_yes-> []!is_paying)

//Always if the answer is yes or the student is paying, they can enter
//[]((answer_is_yes || is_paying)-> []can_enter)

//Always if they want a ticket, they will eventually be asked for a code
//[](want_a_ticket-> <> ask_for_cupon)