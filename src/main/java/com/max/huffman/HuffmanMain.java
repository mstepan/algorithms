package com.max.huffman;


import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class HuffmanMain {


    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    
    private HuffmanMain() throws IOException {

        Path inPath = Paths.get("/Users/mstepan/repo/algorithms/src/main/java/com/max/huffman/in.txt");
        assert inPath.toFile().exists() : "File doesn't exists: '" + inPath + "'";

        Path encodedPath = Paths.get("/Users/mstepan/repo/algorithms/src/main/java/com/max/huffman/encoded.bin");
        Files.deleteIfExists(encodedPath);
        Files.createFile(encodedPath);

        HuffmanEncoder.encode(inPath, encodedPath);

        LOG.info("HuffmanMain done...");
    }

    public static void main(String[] args) {
        try {
            new HuffmanMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }
}
