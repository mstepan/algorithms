package benchmark;

import com.max.algs.string.matching.NewShiftAndMatcher;
import com.max.algs.string.matching.ShiftOrMatcher;
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

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Micro benchmark for a shell sort vs jdk-sort algorithm.
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class ShiftAndOrMatcherBenchmark {

    private static final String[] DNA_LETTERS = {
            "A", "C"
    };

    private static String generateRandomDNA(int length) {

        Random rand = new Random();
        StringBuilder buf = new StringBuilder(length);

        for (int i = 0; i < length; ++i) {
            buf.append(DNA_LETTERS[rand.nextInt(DNA_LETTERS.length)]);
        }

        return buf.toString();
    }

    @State(Scope.Thread)
    public static class StringPerThread {

        public String pattern;
        public String text;

        @Setup(Level.Invocation)
        public void setUp() {
            pattern = generateRandomDNA(20);
            text = generateRandomDNA(1_000_000);
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            pattern = null;
            text = null;
        }
    }

    @Benchmark
    @Group("shiftAnd")
    @GroupThreads(4)
    public void shiftAnd(StringPerThread state) {
        NewShiftAndMatcher.find(state.pattern, state.text);
    }

    @Benchmark
    @Group("shiftOr")
    @GroupThreads(4)
    public void shiftOr(StringPerThread state) {
        ShiftOrMatcher.find(state.pattern, state.text);
    }

    @Benchmark
    @Group("indexOfJdk")
    @GroupThreads(4)
    public void indexOfJdk(StringPerThread state) {
        int index = state.text.indexOf(state.pattern);
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ShiftAndOrMatcherBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
