package com.max.algs.ds.dht;

import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@Ignore
public class DistributedHashtableTest {


//	private static final Logger LOG = Logger.getLogger( DistributedHashtableTest.class );


    @Test
    public void put() throws Exception {

        try (DistributedHashtable table = new DistributedHashtable()) {


            table.addNode("http://localhost:9090/cache");
            table.addNode("http://localhost:9091/cache");
            table.addNode("http://localhost:9092/cache");
            table.addNode("http://localhost:9093/cache");
            table.addNode("http://localhost:9094/cache");

//			LOG.info( table );

            TimeUnit.SECONDS.sleep(2);

            assertFalse(table.contains("1"));
            assertFalse(table.contains("2"));
            assertFalse(table.contains("3"));
            assertFalse(table.contains("4"));
            assertFalse(table.contains("5"));

            TimeUnit.SECONDS.sleep(2);

            table.put("1", "one");
            table.put("2", "two");
            table.put("3", "three");

            TimeUnit.SECONDS.sleep(2);

            assertEquals("one", table.get("1"));
            assertEquals("two", table.get("2"));
            assertEquals("three", table.get("3"));

            assertTrue(table.contains("1"));
            assertTrue(table.contains("2"));
            assertTrue(table.contains("3"));
            assertFalse(table.contains("4"));
            assertFalse(table.contains("5"));

            table.remove("2");

            assertTrue(table.contains("1"));
            assertFalse(table.contains("2"));
            assertTrue(table.contains("3"));
            assertFalse(table.contains("4"));
            assertFalse(table.contains("5"));


            for (int i = 100; i < 2000; i++) {
                table.put("k" + String.valueOf(i), "v" + String.valueOf(i));
            }

            table.addNode("http://localhost:9095/cache");
            table.addNode("http://localhost:9096/cache");

            TimeUnit.SECONDS.sleep(3);


            for (int i = 100; i < 2000; i++) {
                assertTrue(table.contains("k" + String.valueOf(i)));
            }

        }

    }

}
