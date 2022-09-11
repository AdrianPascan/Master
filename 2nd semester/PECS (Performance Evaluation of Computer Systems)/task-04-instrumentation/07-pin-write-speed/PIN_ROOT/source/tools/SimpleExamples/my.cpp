#include <iostream>
#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <stdlib.h>
#include <errno.h>
#include <sys/types.h>
#include <sys/syscall.h>
#include "pin.H"

bool writeThread = false;
ADDRINT writtenBytesCount = 0;
UINT64 writeTime = 0;
UINT64 startTime = 0;
UINT64 stopTime = 0;

void injectOSyncAndODirectFlagsOnOpen(CONTEXT *ctxt, SYSCALL_STANDARD std) {
	// flags = 1st argument (0-indexing)
	ADDRINT flags = PIN_GetSyscallArgument(ctxt, std, 1);
	PIN_SetSyscallArgument(ctxt, std, 1, flags | O_SYNC | O_DIRECT);
}

void injectOSyncFlagOnOpenat(CONTEXT *ctxt, SYSCALL_STANDARD std) {
	// Note: injecting O_DIRECT flag here leads to error ('error while loading shared libraries: /lib64/libc.so.6: cannot read file data: Invalid argument')
	
	// flags = 2nd argument (0-indexing)
	ADDRINT flags = PIN_GetSyscallArgument(ctxt, std, 2);
	PIN_SetSyscallArgument(ctxt, std, 2, flags | O_SYNC);
}

/*
Entry function before system call execution. Checks all system call numbers but hooks
open, openat and write.
*/
void syscallEntryCallback(THREADID threadIndex, CONTEXT *ctxt, SYSCALL_STANDARD std, void *v)
{
	ADDRINT scnum = PIN_GetSyscallNumber(ctxt, std);

	if (scnum == __NR_open)
	{
		fprintf(stdout, "OPEN systemcall (%lu)\n", scnum);
		injectOSyncAndODirectFlagsOnOpen(ctxt, std);
	}
	if (scnum == __NR_openat)
	{
		fprintf(stdout, "OPENAT systemcall (%lu)\n", scnum);
		injectOSyncFlagOnOpenat(ctxt, std);
	}
	if (scnum == __NR_write)
	{
		fprintf(stdout, "WRITE systemcall (%lu)\n", scnum);
		writeThread = true;
		
		// START MEASURING TIME
		if (OS_RETURN_CODE_TIME_QUERY_FAILED == OS_Time(&startTime)) {
			fprintf(stdout, "\tERROR ON OS_Time start\n");
		}
	}
}

/*
Exit function after system call execution. Grabs the system call return value.
*/
void syscallExitCallback(THREADID threadIndex, CONTEXT *ctxt, SYSCALL_STANDARD std, void *v)
{
	if (writeThread) {
		// STOP MEASURING TIME
		if (OS_RETURN_CODE_TIME_QUERY_FAILED == OS_Time(&stopTime)) {
			fprintf(stdout, "\tERROR ON OS_Time stop\n");
		}
		else {
			fprintf(stdout, "write time = %lu 1us ticks\n", stopTime - startTime);
			writeTime += stopTime - startTime;
			fprintf(stdout, "write time in total = %lu 1us ticks\n", writeTime);
		}

		// MEASURE THROUGHPUT
		ADDRINT writtenBytesNo = PIN_GetSyscallReturn(ctxt, std);
		fprintf(stdout, "written bytes: %lu\n", writtenBytesNo);

		writtenBytesCount += writtenBytesNo;
		fprintf(stdout, "written bytes in total: %lu\n", writtenBytesCount);

		writeThread = false;
	}
}

int Usage()
{
	fprintf(stdout, "../../../pin -t obj-intel64/my.so -- sample program");
	return -1;
}

int32_t main(int32_t argc, char *argv[])
{
    if (PIN_Init(argc, argv))
    {
        return Usage();
    }

    fprintf(stdout, "PIN_AddSyscallEntryFunction call\n");
    PIN_AddSyscallEntryFunction(&syscallEntryCallback, NULL);

    fprintf(stdout, "PIN_AddSyscallExitFunction call\n");
    PIN_AddSyscallExitFunction(&syscallExitCallback, NULL);

    fprintf(stdout, "PIN_StartProgram call\n");
    PIN_StartProgram();

    return(0);
}