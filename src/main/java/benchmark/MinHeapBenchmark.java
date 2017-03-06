package benchmark;

import com.max.algs.ds.heap.MinHeap;
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

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Micro benchmark for a D-ary min heap.
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class MinHeapBenchmark {

    private static final int ITERATIONS_COUNT = 100_000;

    private static final int THREADS_COUNT = 2;

    @Benchmark
    @Group("minHeap2")
    @GroupThreads(THREADS_COUNT)
    public void minHeap2() {
        simulateMinHeap(2);
    }

    @Benchmark
    @Group("minHeap3")
    @GroupThreads(THREADS_COUNT)
    public void minHeap3() {
        simulateMinHeap(3);
    }

    @Benchmark
    @Group("minHeap4")
    @GroupThreads(THREADS_COUNT)
    public void minHeap4() {
        simulateMinHeap(4);
    }

    @Benchmark
    @Group("minHeap5")
    @GroupThreads(THREADS_COUNT)
    public void minHeap5() {
        simulateMinHeap(5);
    }

    @Benchmark
    @Group("minHeap6")
    @GroupThreads(THREADS_COUNT)
    public void minHeap6() {
        simulateMinHeap(6);
    }

    @Benchmark
    @Group("minHeap7")
    @GroupThreads(THREADS_COUNT)
    public void minHeap7() {
        simulateMinHeap(7);
    }

    @Benchmark
    @Group("minHeap8")
    @GroupThreads(THREADS_COUNT)
    public void minHeap8() {
        simulateMinHeap(8);
    }

    @Benchmark
    @Group("minHeap9")
    @GroupThreads(THREADS_COUNT)
    public void minHeap9() {
        simulateMinHeap(9);
    }

    private static void simulateMinHeap(int arity) {
        Random rand = new Random();

        MinHeap<Integer> heap = new MinHeap<>(arity);

        for (int i = 0; i < ITERATIONS_COUNT; i++) {
            int value = rand.nextInt(ITERATIONS_COUNT);
            heap.add(value);
        }

        while (!heap.isEmpty()) {
            heap.poll();
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MinHeapBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
