package com.max.algs.util;

import org.apache.log4j.Logger;

import java.io.Closeable;
import java.io.IOException;

public final class IOUtils {

    private static final Logger LOG = Logger.getLogger(IOUtils.class);

    private IOUtils() {
        super();
    }

    public static void closeSilently(Closeable stream) {
        try {
            if (stream != null) {
                stream.close();
            }
        }
        catch (IOException ioEx) {
            LOG.error(ioEx);
        }
    }

}
