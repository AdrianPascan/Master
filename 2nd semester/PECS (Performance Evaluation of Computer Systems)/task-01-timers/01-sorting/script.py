import random
from timeit import timeit
from statistics import mean

MIN = 0
MAX = 100
REPETITIONS = 3


def generate_integers(count):
    print("Generating {} integers in range [{}, {}]...".format(count, MIN, MAX))
    integers = []
    for _ in range(count):
        integers.append(random.randint(MIN, MAX))
    print("Generated {} integers.".format(count))
    return integers


def sample(count):
    print("Sampling {} times for {} integers...".format(REPETITIONS, count))
    integers = generate_integers(count)

    times = []
    for repetition in range(REPETITIONS):
        print("Repetition no. {}".format(repetition))
        time = timeit(lambda: integers.sort())
        times.append(time)

    mean_time = mean(times)
    print("Sampled times for {} integers, mean time is {}".format(count, mean_time))

    return mean_time


def compute_mean_diff(times):
    if len(times) <= 1:
        return 0

    diffs = []
    for index in range(len(times) - 1):
        diff = times[index + 1] - times[index]
        diffs.append(diff)
    return mean(diffs)


def write_file(filename, header, counts, times):
    print("Writing file {}...".format(filename))
    with open(filename, "w") as out_file:
        out_file.write("{}\n".format(header))
        for count, time in zip(counts, times):
            out_file.write("{},{}\n".format(count, time))
    print("Wrote file {}.".format(filename))


def worker():
    # 10000 ... 90000
    counts = list(range(10000, 90001, 10000))
    times = [sample(count) for count in counts]

    # 100000
    mean_diff = compute_mean_diff(times)
    estimates = [times[-1] + mean_diff]
    print("Estimate for 100000 integers = ", estimates[-1])

    counts.append(100000)
    times.append(sample(100000))

    estimate_errors = [abs(estimates[-1] - times[-1])]
    print("Estimate error for 100000 integers = ", estimate_errors[-1])
    time_100000 = times[-1]
    mean_diff = compute_mean_diff(times)

    # 1 million ... 10 millions
    counts_mil = list(range(1000000, 10000001, 1000000))
    for index, count in enumerate(counts_mil):
        time = sample(count)
        estimate = time_100000 * 10 + index * mean_diff
        estimate_error = abs(estimate - time)
        print("Estimate for {} integers = {}".format(count, estimate))
        print("Estimate error for {} integers = {}".format(count, estimate_error))
        times.append(time)
        estimates.append(estimate)
        estimate_errors.append(estimate_error)

    counts.extend(counts_mil)
    estimate_counts = [100000]
    estimate_counts.extend(counts_mil)

    print("Estimate errors = ", estimate_errors)
    print("Average estimate error = ", mean(estimate_errors))

    write_file("times.csv", "no_of_integers,sorting_time", counts, times)
    write_file("estimates.csv", "no_of_integers,estimated_sorting_time", counts, estimates)


if __name__ == '__main__':
    worker()
