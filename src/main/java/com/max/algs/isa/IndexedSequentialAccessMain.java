package com.max.algs.isa;


import java.nio.file.Paths;

public final class IndexedSequentialAccessMain {


    private IndexedSequentialAccessMain() throws Exception {
        IpsMasterIndex index = IpsMasterIndex.createFromDataFile(Paths.get("/Users/mstepan/Desktop/ips.txt"),
                false);
        index.writeToFile(Paths.get("/Users/mstepan/Desktop/ips-index.data"));
        index = null;

        IpsMasterIndex indexFromFile = IpsMasterIndex.readFromFile(Paths.get("/Users/mstepan/Desktop/ips-index.data"));

        String[] arr = {
                "10.11.12.13", "133.211.0.0", "192.168.56.78", // no existed IPs
                "10.25.67.89", "250.133.100.50", "57.89.54.67" // existed IPS
        };

        for (String ipToCheck : arr) {
            System.out.printf("contains('%s') = %b %n", ipToCheck, indexFromFile.contains(ipToCheck));
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new IndexedSequentialAccessMain();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

