package benchmark;

import com.max.algs.epi.string.matching.BoyerMooreStringMatching;
import com.max.algs.epi.string.matching.KMPStringMatching;
import com.max.algs.epi.string.matching.RabinKarpStringMatching;
import com.max.algs.string.StringUtils;
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
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Exact string matching micro benchmark.
 * <p>
 * <p>
 * Benchmark                                        Mode  Cnt       Score       Error  Units
 * ExactStringMatchingBenchmark.boyerMoore          avgt   25   62953.231 ±  8234.703  ns/op
 * ExactStringMatchingBenchmark.boyerMooreExtended  avgt   25  101156.232 ± 15917.701  ns/op
 * ExactStringMatchingBenchmark.jdkMatching         avgt   25       1.358 ±     0.055  ns/op
 * ExactStringMatchingBenchmark.kmp                 avgt   25   64679.764 ±  6826.037  ns/op
 * ExactStringMatchingBenchmark.kmpOptimized        avgt   25   70894.011 ±  7731.971  ns/op
 * ExactStringMatchingBenchmark.rabinKarp           avgt   25  109570.032 ± 15042.335  ns/op
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class ExactStringMatchingBenchmark {

    private static final Random RAND = ThreadLocalRandom.current();

    @State(Scope.Thread)
    public static class PerThreadData {

        public String str;
        public String pattern;

        @Setup(Level.Iteration)
        public void setUp() {
            // generate random DNA string(text)
            str = StringUtils.generateDNAString(1000 + RAND.nextInt(10_000));

            // generate random DNA sting(pattern)
            pattern = StringUtils.generateDNAString(500 + RAND.nextInt(500));
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            str = null;
            pattern = null;
        }
    }

    @Benchmark
    @Group("jdkMatching")
    @GroupThreads(2)
    @SuppressWarnings("unused")
    public void jdkMatching(PerThreadData data) {
        int index = data.str.indexOf(data.pattern);
    }


    @Benchmark
    @Group("kmp")
    @GroupThreads(2)
    public void kmp(PerThreadData data) {
        KMPStringMatching.find(data.str, data.pattern, false);
    }

    @Benchmark
    @Group("kmpOptimized")
    @GroupThreads(2)
    public void kmpOptimized(PerThreadData data) {
        KMPStringMatching.find(data.str, data.pattern, true);
    }

    @Benchmark
    @Group("boyerMoore")
    @GroupThreads(2)
    public void boyerMoore(PerThreadData data) {
        BoyerMooreStringMatching.find(data.str, data.pattern);
    }

    @Benchmark
    @Group("boyerMooreExtended")
    @GroupThreads(2)
    public void boyerMooreExtended(PerThreadData data) {
        BoyerMooreStringMatching.findExtended(data.str, data.pattern);
    }

    @Benchmark
    @Group("rabinKarp")
    @GroupThreads(2)
    public void rabinKarp(PerThreadData data) {
        RabinKarpStringMatching.find(data.str, data.pattern);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(ExactStringMatchingBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
