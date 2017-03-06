package com.max.algs.jol;

import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.util.VMSupport;

import java.io.PrintWriter;

/**
 * Print Integer[] array layout in jvm memory.
 */
public class CheckArrayLayout {

    public static void main(String[] args) throws Exception {
        System.out.println(VMSupport.vmDetails());

        PrintWriter pw = new PrintWriter(System.out, true);

        Integer[] arr = new Integer[10];
        for (int i = 0; i < 10; i++) {
            arr[i] = i;
        }

        String last = null;
        for (int c = 0; c < 100; c++) {
            String current = GraphLayout.parseInstance(arr).toPrintable();

            if (last == null || !last.equalsIgnoreCase(current)) {
                pw.println(current);
                last = current;
            }

            System.gc();
        }

        pw.close();
    }


}
