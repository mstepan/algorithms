package com.max.algs.riak;


import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.RiakCluster;
import com.basho.riak.client.core.RiakNode;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;
import org.apache.log4j.Logger;

import java.net.UnknownHostException;

/**
 * Store and retrieve object from Riak.
 */
public class RiakMain {

    private static final Logger LOG = Logger.getLogger(RiakMain.class);

    private static RiakCluster createRiakCluster() throws UnknownHostException {

        RiakNode node = new RiakNode.Builder()
                .withRemoteAddress("127.0.0.1")
                .withRemotePort(8087)
                .build();

        RiakCluster cluster = new RiakCluster.Builder(node)
                .build();

        cluster.start();

        return cluster;
    }

    private RiakMain() throws Exception {

        RiakClient client = new RiakClient(createRiakCluster());

        RiakObject quoteObject = new RiakObject()
                .setContentType("text/plain")
                .setValue(BinaryValue.create("Some text value 123"));

        Namespace customerBucket = new Namespace("customer");

        Location quoteObjectLocation = new Location(customerBucket, "key1");

        StoreValue storeOp = new StoreValue.Builder(quoteObject)
                .withLocation(quoteObjectLocation)
                .build();

        StoreValue.Response response = client.execute(storeOp);

        FetchValue fetchOp = new FetchValue.Builder(quoteObjectLocation)
                .build();
        RiakObject fetchedObject = client.execute(fetchOp).getValue(RiakObject.class);

        LOG.info(fetchedObject.getValue().toString());

        LOG.info("Main completed...");
    }


    public static void main(String[] args) {
        try {
            new RiakMain();
        }
        catch (Exception ex) {
            LOG.error("Exception occurred", ex);
        }
    }
}
