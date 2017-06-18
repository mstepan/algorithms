package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Micro benchmark for adding value to beginning of collection.
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class AddToBeginningOfCollectionBenchmark {


    private final Random rand = ThreadLocalRandom.current();

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(AddToBeginningOfCollectionBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    @Group("module")
    @GroupThreads(4)
    public void mode() {
        boolean isEven = (rand.nextInt() % 2) == 0;
    }

    @Benchmark
    @Group("data")
    @GroupThreads(4)
    public void binaryMode() {
        boolean isEven = (rand.nextInt() & 1) == 0;
    }

}
