package benchmark;

import com.max.algs.hashing.robin_hood.RobinHoodHashMap;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Micro benchmark for Robin Hood hash map vs jdk hash map.
 */
@State(Scope.Group)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(5)
public class RobinHoodHashMapBenchmark {

    private static final int ARR_LENGTH = 100_000;
    private static final Integer VALUE = 1;

    @State(Scope.Thread)
    public static class ArrPerThread {

        private int[] arr;
        private Map<Integer, Integer> robinMap;
        private Map<Integer, Integer> jdkMap;

        @Setup(Level.Invocation)
        public void setUp() {
            arr = ArrayUtils.generateRandomArray(ARR_LENGTH);
            robinMap = new RobinHoodHashMap<>();
            jdkMap = new HashMap<>();

            for (int val : arr) {
                robinMap.put(val, VALUE);
                jdkMap.put(val, VALUE);
            }
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            arr = null;
            robinMap = null;
            jdkMap = null;
        }
    }

    @Benchmark
    @Group("robinHood")
    @GroupThreads(2)
    public void robinHood(ArrPerThread state) {

        Map<Integer, Integer> map = state.robinMap;

        for (int val : state.arr) {
            map.get(val);
        }
    }

    @Benchmark
    @Group("jdk")
    @GroupThreads(2)
    public void jdk(ArrPerThread state) {

        Map<Integer, Integer> map = state.jdkMap;

        for (int val : state.arr) {
            map.get(val);
        }
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(RobinHoodHashMapBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
