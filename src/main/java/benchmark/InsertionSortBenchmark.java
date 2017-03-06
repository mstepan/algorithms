package benchmark;

import com.max.algs.sorting.InsertionSort;
import com.max.algs.util.ArrayUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Micro benchmark for insertion sort.
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class InsertionSortBenchmark {

    @State(Scope.Thread)
    public static class ArrPerThread {

        int[] arr1;
        int[] arr2;

        @Setup(Level.Invocation)
        public void setUp() {
            arr1 = ArrayUtils.generateRandomArray(1_000);
            arr2 = Arrays.copyOf(arr1, arr1.length);
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            arr1 = null;
            arr2 = null;
        }
    }

    @Benchmark
    @Group("sortWithSentinel")
    @GroupThreads(4)
    public void sortWithSentinel(ArrPerThread state) {
        InsertionSort.sortWithSentinel(state.arr1);
    }

    @Benchmark
    @Group("sort")
    @GroupThreads(4)
    public void sort(ArrPerThread state) {
        InsertionSort.sort(state.arr2);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(InsertionSortBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
