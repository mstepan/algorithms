package benchmark.ds;

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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 3, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class SynchListOrLinkedBlockingQueueBenchmark {


    private final Random rand = ThreadLocalRandom.current();

    private final List<Integer> list = Collections.synchronizedList(new LinkedList<>());

    private final Queue<Integer> queue = new LinkedBlockingQueue<>();

    @Benchmark
    @Group("synchronised_list")
    @GroupThreads(4)
    public void addToList() {

        boolean removeElement = rand.nextInt(10) == 0;

        if (removeElement) {
            list.remove(0);
        }
        else {
            list.add(rand.nextInt());
        }
    }

    @Benchmark
    @Group("linked_blocking_queue")
    @GroupThreads(4)
    public void addToQueue() {

        boolean removeElement = rand.nextInt(10) == 0;

        if (removeElement) {
            queue.poll();
        }
        else {
            queue.add(rand.nextInt());
        }

    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(SynchListOrLinkedBlockingQueueBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
