package benchmark.cpu;

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
 * Benchmark for reassociation technique for instruction level parallelism (ILP).
 *
 * Benchmark                                              Mode  Cnt      Score      Error  Units
 * ReassociationAndILPBenchmark.product                   avgt   25   1736.120 ± 4405.752  ns/op
 * ReassociationAndILPBenchmark.reassociatedBadlyProduct  avgt   25  15440.504 ± 6915.719  ns/op
 * ReassociationAndILPBenchmark.reassociatedProduct       avgt   25   8028.077 ± 2223.636  ns/op
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class ReassociationAndILPBenchmark {

    private static final int ARR_LENGTH = 10_000;

    private static final int TEN_PERCENTS = 10;
    private static final int ONE_HUNDRED_PERCENTS = 100;

    @State(Scope.Thread)
    public static class ArrPerThread {

        public int[] arr1;
        public int[] arr2;

        @Setup(Level.Invocation)
        public void setUp() {
            arr1 = ArrayUtils.generateRandomArray(ARR_LENGTH);
            arr2 = Arrays.copyOf(arr1, arr1.length);
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            arr1 = null;
            arr2 = null;
        }
    }

    @Benchmark
    @Group("product")
    @GroupThreads(2)
    public void product(ArrPerThread state) {
        final int[] arr = state.arr1;
        final int arrLength = arr.length;
        int res = 1;

        for (int i = 0; i < arrLength; ++i) {
            res *= arr[i];
        }
    }

    @Benchmark
    @Group("reassociatedProduct")
    @GroupThreads(2)
    public void reassociatedProduct(ArrPerThread state) {
        final int[] arr = state.arr1;
        final int arrLength = arr.length;
        int res = 1;

        int i = 0;
        for (; i < arrLength - 2; i += 2) {
            res = res * (arr[i] * arr[i + 1]);
        }

        for (; i < arrLength; ++i) {
            res *= arr[i];
        }
    }

    @Benchmark
    @Group("reassociatedBadlyProduct")
    @GroupThreads(2)
    public void reassociatedBadlyProduct(ArrPerThread state) {
        final int[] arr = state.arr1;
        final int arrLength = arr.length;
        int res = 1;

        int i = 0;
        for (; i < arrLength - 2; i += 2) {
            res = (res * arr[i]) * arr[i + 1];
        }

        for (; i < arrLength; ++i) {
            res *= arr[i];
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ReassociationAndILPBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
