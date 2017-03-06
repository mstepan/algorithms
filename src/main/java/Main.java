import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {


    private static int countPairs(int[] arr) {

        assert arr != null : "null 'arr' argument";

        int from = 0;
        int to = arr.length - 1;

        while (from < arr.length && arr[from] == 0) {
            ++from;
        }

        while (to >= 0 && arr[to] == 0) {
            --to;
        }

        if (from > to) {
            return 0;
        }

        int cnt = 1;

        int i = from + 1;

        while (i <= to) {

            if (arr[i] == 1) {
                ++cnt;
                if (arr[i - 1] == 0) {
                    ++cnt;
                }
                ++i;
            }
            else if (arr[i] == 0 && arr[i - 1] == 0) {
                while (arr[i] == 0) {
                    ++i;
                }
                ++i;
                ++cnt;
            }
            else {
                ++i;
            }
        }

        return cnt;
    }


    public static void main(String[] args) {
        try {

//            BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Integer numbers = Integer.parseInt(reader.readLine().trim());

            int[] arr = new int[numbers];

            String[] pairs = reader.readLine().trim().split("\\s+");

            for (int i = 0; i < arr.length; i++) {
                arr[i] = Integer.parseInt(pairs[i]);
            }

            System.out.println(countPairs(arr));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
