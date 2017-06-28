package spoj.brackets;


import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

/**
 * http://www.spoj.com/problems/BRCKTS/
 */
public class BracketsMain {

    private static final Logger LOG = Logger.getLogger(BracketsMain.class);

    private BracketsMain() throws Exception {

//        Path path = Paths.get("/Users/mstepan/repo/incubator/algorithms/src/main/java/spoj/brackets/in.txt");

//        try (BufferedReader reader = Files.newBufferedReader(path)) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            for (int i = 0; i < 10; ++i) {

                System.out.println("Test " + (i + 1) + ":");

                int bracketsCount = Integer.parseInt(reader.readLine());

                char[] brackets = reader.readLine().toCharArray();

                BracketsTree tree = new BracketsTree(Arrays.copyOf(brackets, bracketsCount));

                int operationsCount = Integer.parseInt(reader.readLine());

                for (int j = 0; j < operationsCount; ++j) {
                    int invertIndex = Integer.parseInt(reader.readLine());

                    if (invertIndex == 0) {
                        System.out.println(tree.isBalanced() ? "YES" : "NO");
                    }
                    else {
                        tree.invert(invertIndex - 1);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new BracketsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static class BracketNode {

        final BracketNode left;
        final BracketNode right;
        BracketNode parent;
        int openCnt;
        int closedCnt;

        BracketNode(char ch) {
            assert ch == '(' || ch == ')';

            left = null;
            right = null;

            if (ch == '(') {
                openCnt = 1;
            }
            else {
                closedCnt = 1;
            }
        }

        BracketNode(BracketNode left, BracketNode right) {
            assert left != null && right != null;

            this.left = left;
            this.right = right;

            left.parent = this;
            right.parent = this;

            int minReduce = Math.min(left.openCnt, right.closedCnt);
            openCnt = left.openCnt - minReduce + right.openCnt;
            closedCnt = right.closedCnt - minReduce + left.closedCnt;
        }

        private void recalculateCounters() {
            int minReduce = Math.min(left.openCnt, right.closedCnt);
            openCnt = left.openCnt - minReduce + right.openCnt;
            closedCnt = right.closedCnt - minReduce + left.closedCnt;
        }

        public void invert() {
            int temp = openCnt;
            openCnt = closedCnt;
            closedCnt = temp;
            parent.update();
        }

        public void update() {

            recalculateCounters();

            if (parent != null) {
                parent.update();
            }
        }

        @Override
        public String toString() {
            return openCnt + "/" + closedCnt;
        }
    }

    private static final class BracketsTree {
        private final BracketNode[] nodes;
        private final BracketNode root;

        public BracketsTree(char[] arr) {
            nodes = new BracketNode[arr.length];

            for (int i = 0; i < arr.length; ++i) {
                nodes[i] = new BracketNode(arr[i]);
            }

            this.root = buildFromArrayOfNodes(nodes);
        }

        private BracketNode buildFromArrayOfNodes(BracketNode[] nodes) {

            Deque<BracketNode> curLevel = new ArrayDeque<>();

            Arrays.stream(nodes).forEach(curLevel::add);

            Deque<BracketNode> nextLevel = new ArrayDeque<>();

            while (true) {

                while (curLevel.size() > 1) {

                    BracketNode left = curLevel.poll();
                    BracketNode right = curLevel.poll();

                    BracketNode parent = new BracketNode(left, right);
                    nextLevel.add(parent);
                }

                if (!curLevel.isEmpty()) {
                    nextLevel.add(curLevel.poll());
                }

                curLevel.addAll(nextLevel);
                nextLevel.clear();

                if (curLevel.size() == 1) {
                    break;
                }
            }

            return curLevel.poll();
        }

        public void invert(int index) {
            nodes[index].invert();
        }

        public boolean isBalanced() {
            return root.openCnt == 0 && root.closedCnt == 0;
        }
    }
}
