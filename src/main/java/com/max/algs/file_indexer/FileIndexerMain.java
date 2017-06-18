package com.max.algs.file_indexer;

import org.apache.log4j.Logger;

/**
 * Created by mstepan on 12/20/14.
 */
public final class FileIndexerMain {

    private static final Logger LOG = Logger.getLogger(FileIndexerMain.class);


    private FileIndexerMain() throws Exception {
        LOG.info("FileIndexer successfully completed");
    }

    public static void main(String[] args) {
        try {
            new FileIndexerMain();
        }
        catch (final Exception ex) {
            LOG.error("Exception occurred", ex);
        }
    }
}
