package com.max.ioc.services;

import org.apache.log4j.Logger;

public class DBService {

    private static final Logger LOG = Logger.getLogger(DBService.class);

    public void call() {
        LOG.info("Calling DB");
    }

}
