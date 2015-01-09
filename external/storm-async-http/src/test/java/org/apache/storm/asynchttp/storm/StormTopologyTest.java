package org.apache.storm.asynchttp.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.apache.storm.asynchttp.bolt.AsyncHttpBolt;
import org.apache.storm.asynchttp.bolt.mapper.RequestMapper;
import org.apache.storm.asynchttp.bolt.mapper.ResponseMapper;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class StormTopologyTest {
    private HttpTestResult httpTestResult = new HttpTestResult();

    private static final Logger LOG = LoggerFactory.getLogger(StormTopologyTest.class);

    public  void runTopology(String ... strings) throws AlreadyAliveException, InvalidTopologyException {

        final TopologyBuilder builder = buildTopolgy(strings);
        final Config config = new Config();
        config.setDebug(false);
        final LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("local-async", config, builder.createTopology());
    }

    private TopologyBuilder buildTopolgy(String ... strings) {

        final Config config = new Config();
        final TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("checkins", new MySpout(strings));
        builder.setBolt("async-http-bolt", getAsyncHttpBolt()).shuffleGrouping("checkins");
        builder.setBolt("response-bolt", new ResponseBolt(httpTestResult)).shuffleGrouping("async-http-bolt");
        return builder;
    }

    private AsyncHttpBolt getAsyncHttpBolt() {
        return new AsyncHttpBolt()
                .withRequestMapper(new TestRequestMapper())
                .withResponseMapper(new TestResponseMapper());
    }

    @Test
    public  void run() throws AlreadyAliveException, InvalidTopologyException, InterruptedException {

        this.runTopology("success");
        httpTestResult.waitResult(1, TimeUnit.MINUTES);
        assertEquals(200, httpTestResult.getStatus());
        assertEquals("success", httpTestResult.getBody());
    }
}