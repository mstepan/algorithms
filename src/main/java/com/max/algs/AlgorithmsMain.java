package com.max.algs;

import org.apache.log4j.Logger;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

import java.util.HashMap;

public final class AlgorithmsMain {

    private static final Logger LOG = Logger.getLogger(AlgorithmsMain.class);

    private AlgorithmsMain() throws Exception {

        System.out.println(VM.current().details());
        System.out.println(ClassLayout.parseClass(HashMap.class).toPrintable());

        LOG.info("Main done... java-" + System.getProperty("java.version"));
    }


    public static void main(String[] args) {
        try {
            new AlgorithmsMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
