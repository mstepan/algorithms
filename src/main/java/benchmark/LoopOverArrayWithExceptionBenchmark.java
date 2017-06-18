package benchmark;

import com.max.algs.util.ArrayUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Micro benchmark for iterating over array with normal for-each and exception.
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class LoopOverArrayWithExceptionBenchmark {


    private static final int[] ARR = ArrayUtils.generateRandomArray(100);

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LoopOverArrayWithExceptionBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    @Group("loop")
    @GroupThreads(4)
    public void loop() {
        int sum = 0;

        for (int i = 0, arrLength = ARR.length; i < arrLength; i++) {
            sum += ARR[i];
        }
    }

    @Benchmark
    @Group("loopWithException")
    @GroupThreads(4)
    public void loopWithException() {

        int sum = 0;

        int i = 0;

        while (true) {
            try {
                sum += ARR[i];
                ++i;
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                break;
            }
        }
    }

}
