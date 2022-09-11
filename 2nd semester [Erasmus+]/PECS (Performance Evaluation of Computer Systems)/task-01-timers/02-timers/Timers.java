import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Timers {

    public static void main(String[] args) {
        worker();
    }

    private static void initializeArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = 0;
        }
    }

    private static void writeFile(String filename, String header, List<Integer> sizes, List<Long> times, List<Long> times2) {
        System.out.printf("Writing file %s ...%n", filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.format("%s\n", header));
            for (int i = 0; i < sizes.size(); i++) {
                writer.write(String.format("%d,%d,%d\n", sizes.get(i), times.get(i), times2.get(i)));
            }
        } catch (IOException ioException) {
            System.out.printf("Error writing file %s .%n", filename);
        }
        System.out.printf("Written file %s .%n", filename);
    }

    public static void worker() {
        List<Integer> sizes = List.of(100, 500, 1000, 5000, 10000, 50000, 100000, 500000, 1000000, 5000000, 10000000, 50000000);
        List<Long> times = new ArrayList<>();
        List<Long> times2 = new ArrayList<>();
        long start, stop, time;

        for (int size : sizes) {
            System.out.printf("Measuring array initialization of size %s...%n", size);

            start = System.currentTimeMillis();
            initializeArray(size);
            stop = System.currentTimeMillis();
            time = stop - start;
            times.add(time);

            start = System.nanoTime();
            initializeArray(size);
            stop = System.nanoTime();
            time = stop - start;
            times2.add(time);

            System.out.printf("Measured array initialization of size %s.%n", size);
        }

        writeFile("timers_java.csv", "size,currentTimeMillis,nanoTime", sizes, times, times2);
    }
}
