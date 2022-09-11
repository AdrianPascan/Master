#include <stdlib.h>
#include <time.h>
#include "harness.h"

volatile int li_var1 = 42;
volatile int li_var2 = 1;
volatile int li_var3 = 2;
volatile int li_var4 = 3;
volatile int li_var5 = 4;
volatile int li_var6 = 5;

class load_instructions : public workload {
public:
    const char *name () override { return "load_instructions"; }
    int execute () override { return li_var1 + li_var2 + li_var3 + li_var4 + li_var5 + li_var6; }
};


float spo_var1 = 3.325;
float spo_var2 = 12.25;
float spo_var3 = 87.75;
float spo_var4 = 6.625;
float spo_var5 = 0.333;

class single_precision_operations : public workload {
public:
    const char *name () override { return "single_precision_operations"; }
    int execute () override { return spo_var1 + spo_var2 * spo_var3 - spo_var4 / spo_var5; }
};


class branch_predictions : public workload {
public:
    const char *name () override { return "branch_predictions"; }
    int execute () override {
        if (true) return 1;
        else return 0; 
    }
};


volatile int tlbdm_var[10][10] = {
    {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
    {10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
    {20, 21, 22, 23, 24, 25, 26, 27, 28, 29},
    {30, 31, 32, 33, 34, 35, 36, 37, 38, 39},
    {40, 41, 42, 43, 44, 45, 46, 47, 48, 49},
    {50, 51, 52, 53, 54, 55, 56, 57, 58, 59},
    {60, 61, 62, 63, 64, 65, 66, 67, 68, 69},
    {70, 71, 72, 73, 74, 75, 76, 77, 78, 79},
    {80, 81, 82, 83, 84, 85, 86, 87, 88, 89},
    {90, 91, 92, 93, 94, 95, 96, 97, 98, 99}
};

class translation_lookaside_buffer_data_misses : public workload {
public:
    const char *name () override { return "translation_lookaside_buffer_data_misses"; }
    int execute () override {
        int sampleSum = 0;
        for (int sample = 0; sample < 100; sample++) {
            sampleSum += tlbdm_var[rand()%10][rand()%10];
        }
        return sampleSum; 
    }
};


int main(int argc, char *argv[]) {
    srand(time(NULL));

    harness_init();
    harness_run (new load_instructions(), "PAPI_LD_INS");
    harness_run (new single_precision_operations(), "PAPI_SP_OPS");
    harness_run (new branch_predictions(), "PAPI_BR_PRC");
    harness_run (new translation_lookaside_buffer_data_misses(), "PAPI_TLB_DM");
    harness_done();

    return (0);
}
