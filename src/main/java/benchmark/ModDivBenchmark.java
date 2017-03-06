package benchmark;

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

import java.util.concurrent.TimeUnit;

/**
 * Mod/div benchmark.
 */
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class ModDivBenchmark {

    @State(Scope.Thread)
    public static class DataState {

        public int[] arr;

        @Setup(Level.Invocation)
        public void setUp() {
            arr = ArrayUtils.generateRandomArray(1000, 20);
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            arr = null;
        }
    }

    @Benchmark
    @Group("mod")
    @GroupThreads(4)
    public void mod(DataState state) {
        for (int val : state.arr) {
            int res = val % 10;
        }
    }

    @Benchmark
    @Group("modSub")
    @GroupThreads(4)
    public void modSub(DataState state) {
        for (int val : state.arr) {
            int res = (val < 10) ? val : val - 10;
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ModDivBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }
}
