package benchmark.cpu;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.GroupThreads;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 'double' vs 'float' numbers addition.
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class DoubleVsFloatBenchmark {

    private static final int ITERATIONS_COUNT = 10_000_000;

    @Benchmark
    @Group("floatAdd")
    @GroupThreads(2)
    public void floatAdd() {
        float res = 0.0F;

        float offset = 3.3F;

        for (int i = 0; i < ITERATIONS_COUNT; ++i) {
            res += offset;
        }
    }

    @Benchmark
    @Group("doubleAdd")
    @GroupThreads(2)
    public void doubleAdd() {
        double res = 0.0;

        double offset = 3.3;

        for (int i = 0; i < ITERATIONS_COUNT; ++i) {
            res += offset;
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DoubleVsFloatBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
