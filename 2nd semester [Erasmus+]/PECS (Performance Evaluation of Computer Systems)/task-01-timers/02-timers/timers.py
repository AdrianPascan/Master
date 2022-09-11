from timeit import timeit
from time import perf_counter


def sample(count):
    print("Sampling range({}) using timeit...".format(count))
    time = timeit(lambda: range(count))
    print("Sampled range({}) using timeit.".format(count))
    return time


def sample2(count):
    print("Sampling range({}) using perf_counter...".format(count))
    start = perf_counter()
    range(count)
    stop = perf_counter()
    time = stop - start
    print("Sampled range({}) using perf_counter.".format(count))
    return time


def write_file(filename, header, counts, times, times2):
    print("Writing file {}...".format(filename))
    with open(filename, "w") as out_file:
        out_file.write("{}\n".format(header))
        for count, time, time2 in zip(counts, times, times2):
            out_file.write("{},{},{}\n".format(count, time, time2))
    print("Wrote file {}.".format(filename))


def worker():
    counts = [100, 500, 1000, 5000, 10000, 50000, 100000, 500000, 1000000, 5000000, 10000000, 50000000, 100000000]
    times = [sample(count) for count in counts]
    times2 = [sample2(count) for count in counts]

    write_file("timers_python.csv", "count,timeit,perf_counter", counts, times, times2)


if __name__ == '__main__':
    worker()
