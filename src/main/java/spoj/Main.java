package spoj;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * http://www.spoj.com/problems
 */
public class Main {


    private Main() throws Exception {

        String filePath = "/Users/mstepan/repo/incubator/algorithms/src/main/java/spoj/in.txt";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))) {
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int count = Integer.parseInt(reader.readLine());
            System.out.println("count: " + count);
        }
    }

    public static void main(String[] args) {
        try {
            new Main();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
