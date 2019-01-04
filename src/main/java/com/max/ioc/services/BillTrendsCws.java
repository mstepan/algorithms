package com.max.ioc.services;

public class BillTrendsCws {

    private final UtilityApiProxyClient uapClient;
    private final DBService dbService;

    public BillTrendsCws(UtilityApiProxyClient uapClient, DBService dbService) {
        this.uapClient = uapClient;
        this.dbService = dbService;
    }

    public String getBill() {

        uapClient.call();
        dbService.call();

        return "SUCCESS";
    }

}
