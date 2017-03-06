package benchmark;

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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Micro benchmark for finding first unique character in java.lang.String.
 */
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(5)
public class FindFirstUniqueCharacterInStringBenchmark {

    private static final int STR_LENGTH = 100;

    @State(Scope.Thread)
    public static class PerThreadData {

        public String str1;
        public String str2;

        @Setup(Level.Invocation)
        public void setUp() {
            str1 = StringUtils.generateAsciiString(STR_LENGTH);
            str2 = new String(str1.toCharArray());
        }

        @TearDown(Level.Invocation)
        public void tearDown() {
            str1 = null;
            str2 = null;
        }
    }

    @Benchmark
    @Group("firstUnique")
    @GroupThreads(2)
    public void firstUnique(PerThreadData data) {
        firstUnique(data.str1);
    }

    private static Optional<Character> firstUnique(String str) {

        Set<Character> all = new HashSet<>();
        Set<Character> unique = new LinkedHashSet<>();

        char ch;
        for (int i = 0, length = str.length(); i < length; ++i) {
            ch = str.charAt(i);

            if (all.add(ch)) {
                unique.add(ch);
            }
            else {
                unique.remove(ch);
            }
        }

        if (unique.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(unique.iterator().next());
    }

    @Benchmark
    @Group("firstUniqueSmallStr")
    @GroupThreads(2)
    public void firstUniqueSmallStr(PerThreadData data) {
        firstUniqueSmallStr(data.str2);
    }

    private static Character firstUniqueSmallStr(String str) {

        final int strLength = str.length();
        char ch;

        MAIN:
        for (int i = 0; i < strLength - 1; ++i) {
            ch = str.charAt(i);
            for (int j = i + 1; j < strLength; ++j) {
                if (str.charAt(j) == ch) {
                    continue MAIN;
                }
            }

            return ch;
        }

        return null;
    }


    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(FindFirstUniqueCharacterInStringBenchmark.class.getSimpleName())
                .threads(Runtime.getRuntime().availableProcessors())
                .build();

        new Runner(opt).run();
    }

}
