package benchmark;

import com.max.algs.primes.PrimeUtilities;
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
 * Micro benchmark for Sieve of Eratosthenes (normal and segmented implementations).
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class SieveOfEratosthenesBenchmark {

    private static final int N = 100_000_000;

    @Benchmark
    @Group("countPrimesSegmentedWithBoolean")
    @GroupThreads(2)
    public void countPrimesSegmentedWithBoolean() {
        PrimeUtilities.countPrimesSegmentedWithBoolean(N);
    }

    @Benchmark
    @Group("countPrimesSegmented")
    @GroupThreads(2)
    public void countPrimesSegmented() {
        PrimeUtilities.countPrimesSegmented(N);
    }

//    @Benchmark
//    @Group("countPrimes")
//    @GroupThreads(2)
//    public void countPrimes() {
//        PrimeUtilities.countPrimes(N);
//    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SieveOfEratosthenesBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
