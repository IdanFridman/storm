package org.apache.storm.asynchttp.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import org.apache.storm.asynchttp.bolt.AsyncHttpBolt;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class StormTopologyTest {
    private HttpServerSupport httpServerSupport = new HttpServerSupport();
    private HttpTestResult httpTestResult = new HttpTestResult();

    private static final Logger LOG = LoggerFactory.getLogger(StormTopologyTest.class);

    public  void runTopology(String url, String ... strings) throws AlreadyAliveException, InvalidTopologyException {

        final TopologyBuilder builder = buildTopolgy(url, strings);
        final Config config = new Config();
        config.setDebug(false);
        final LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("local-async", config, builder.createTopology());
    }

    private TopologyBuilder buildTopolgy(String url, String ... strings) {

        final Config config = new Config();
        final TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("checkins", new MySpout(strings));
        builder.setBolt("async-http-bolt", new AsyncHttpBolt()
                .withRequestMapper(new TestRequestMapper(url))
                .withResponseMapper(new TestResponseMapper())).shuffleGrouping("checkins");
        builder.setBolt("response-bolt", new ResponseBolt(httpTestResult)).shuffleGrouping("async-http-bolt");
        return builder;
    }

    @Test
    public  void run() throws Exception {
        httpServerSupport.start();
        this.runTopology(httpServerSupport.getTargetUrl(), "success");
        httpTestResult.waitResult(1, TimeUnit.MINUTES);
        assertEquals(200, httpTestResult.getStatus());
        assertEquals("success", httpTestResult.getBody());
        httpServerSupport.stop();
    }
}