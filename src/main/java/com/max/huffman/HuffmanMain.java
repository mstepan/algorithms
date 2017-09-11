package com.max.huffman;


import com.max.huffman.decoder.HuffmanDecoder;
import com.max.huffman.encoder.HuffmanEncoder;
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

        Path encodedPath = Paths.get("/Users/mstepan/repo/algorithms/src/main/java/com/max/huffman/out.bin");
        Files.deleteIfExists(encodedPath);
        Files.createFile(encodedPath);

        HuffmanEncoder.encode(inPath, encodedPath);

        assert encodedPath.toFile().exists() : "File to encode: '" + encodedPath + "' doesn't exist";
        Path decodedPath = Paths.get("/Users/mstepan/repo/algorithms/src/main/java/com/max/huffman/in-decoded.txt");
        Files.deleteIfExists(decodedPath);
        Files.createFile(decodedPath);

        HuffmanDecoder.decode(encodedPath, decodedPath);

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
