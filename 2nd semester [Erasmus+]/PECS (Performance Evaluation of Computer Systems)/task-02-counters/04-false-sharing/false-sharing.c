#define _GNU_SOURCE

#include <stdio.h>
#include <stdlib.h>
#include <malloc.h>
#include <pthread.h>
#include <inttypes.h>
#include <papi.h>

#define LOOPS 1000000

#define PAPI_CHECKED(call) \
	do { \
		int _papi_rc = call; \
		if (_papi_rc != PAPI_OK) { \
			fprintf(stderr, "Error: %s returned %d, aborting.\n", #call, _papi_rc); \
			exit(1); \
		} \
	} while (0)


typedef struct {
    int index;
    int cpu;
    uint64_t timestamp;
} run_info_t;

static void run_info_print(run_info_t *info) {
    printf("CPU%2d [ %10llu ]", info->cpu, (unsigned long long) info->timestamp);
}


static volatile int *FALSE_SHARING_VARIABLE;

static pthread_barrier_t barrier;


static inline uint64_t rdtsc() {
    uint32_t lo, hi;
    __asm__ __volatile__ ("rdtsc" : "=a" (lo), "=d" (hi) : : );
    return ((uint64_t) hi << 32 | (uint64_t) lo);
}

static void *worker(void *arg) {
    run_info_t *info = arg;

    pthread_barrier_wait(&barrier);

    uint64_t begin = rdtsc();
    for (int i = 0; i < LOOPS; i++) {
        FALSE_SHARING_VARIABLE[info->index] += i;
    }
    uint64_t end = rdtsc();

    info->timestamp = end - begin;

    return NULL;
}


static void pthread_create_with_affinity(pthread_t *thread,
        void *(*worker) (void *),
        void *arg,
        int affinity) {

    pthread_attr_t attr;
    pthread_attr_init(&attr);

    cpu_set_t cpus;
    CPU_ZERO(&cpus);
    CPU_SET(affinity, &cpus);

    pthread_attr_setaffinity_np(&attr, sizeof(cpu_set_t), &cpus);

    pthread_create(thread, &attr, worker, arg);
}

int main(int argc, char *argv[]) {
    if (argc == 1) {
        fprintf(stderr, "Usage: %s distance CPU [CPU [CPU ... ]]\n", argv[0]);
        return 1;
    }

    int count = argc - 2;

    int distance = atoi(argv[1]);

    int *cpus = malloc(sizeof(int) * count);
    for (int i = 0; i < count; i++) {
        cpus[i] = atoi(argv[i + 2]);
    }

    run_info_t *info_one = malloc(sizeof(run_info_t) * count);
    run_info_t *info_two = malloc(sizeof(run_info_t) * count);

    FALSE_SHARING_VARIABLE = memalign(64, sizeof(int) * 256);

    PAPI_library_init(PAPI_VER_CURRENT);
    printf("%5s %5s %6s %12s\n", "CPU_1", "CPU_2", "events", "instructions");
    
    PAPI_CHECKED(PAPI_set_cmp_granularity(PAPI_GRN_THR, 0));

    for (int i = 0; i < count; i++) {
        pthread_t thread_one, thread_two;
        int events = PAPI_NULL;
        PAPI_option_t opt;

        info_one[i].index = 0;
        info_one[i].cpu = 0;

        info_two[i].index = distance;
        info_two[i].cpu = cpus[i];

        PAPI_CHECKED(PAPI_create_eventset(&events));
        PAPI_CHECKED(PAPI_assign_eventset_component(events, 0));

        memset(&opt, 0x0, sizeof(PAPI_option_t));
        opt.inherit.inherit = PAPI_INHERIT_ALL;
        opt.inherit.eventset = events;
        PAPI_CHECKED(PAPI_set_opt(PAPI_INHERIT, &opt));

        PAPI_CHECKED(PAPI_add_event(events, PAPI_CA_SHR));
	    PAPI_CHECKED(PAPI_add_event(events, PAPI_L1_DCM));
	    PAPI_CHECKED(PAPI_add_event(events, PAPI_L2_DCM));
        // // NOT AVAILABLE:
        // PAPI_CHECKED(PAPI_add_event(events, PAPI_CA_INV));
	    // PAPI_CHECKED(PAPI_add_event(events, PAPI_L3_DCM));

        long long values[2] = {0, 0};

        pthread_barrier_init(&barrier, NULL, 2);

        pthread_create_with_affinity(&thread_one, worker, &info_one[i], 0);
        pthread_create_with_affinity(&thread_two, worker, &info_two[i], cpus[i]);

        PAPI_start(events);

        pthread_join(thread_one, NULL);
        pthread_join(thread_two, NULL);

        PAPI_CHECKED(PAPI_stop(events, &values));

        pthread_barrier_destroy(&barrier);

        PAPI_CHECKED(PAPI_cleanup_eventset(events));
	    PAPI_CHECKED(PAPI_destroy_eventset(&events));

        printf("%5d %5d %6lld %12lld\n", 0, cpus[i], values[0], values[1]);
    }

    printf("\n");
    PAPI_shutdown();

    for (int i = 0; i < count; i++) {
        run_info_print(&info_one[i]);
        printf("  :  ");
        run_info_print(&info_two[i]);
        puts("");
    }

    free(cpus);
    free(info_one);
    free(info_two);
    free((void**) FALSE_SHARING_VARIABLE);

    return 0;
}
