package com.max.ioc;


import com.max.ioc.container.ContextManager;
import com.max.ioc.services.BillTrendsCws;
import com.max.ioc.services.DBService;
import com.max.ioc.services.UtilityApiProxyClient;
import org.apache.log4j.Logger;

public final class InjectorMain {

    private static final Logger LOG = Logger.getLogger(InjectorMain.class);

    private InjectorMain() {

        ContextManager.INST.register(DBService.class);
        ContextManager.INST.register(BillTrendsCws.class);
        ContextManager.INST.register(UtilityApiProxyClient.class);

        BillTrendsCws billTrends = ContextManager.INST.create(BillTrendsCws.class);

        String billRes = billTrends.getBill();

        LOG.info(billRes);

        LOG.info("Main done... java-" + System.getProperty("java.version"));
    }

    public static void main(String[] args) {
        try {
            new InjectorMain();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

}
