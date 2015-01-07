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
import org.apache.storm.asynchttp.storm.bolt.AbstractBasicTest;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.trident.state.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class StormTopologyTest extends AbstractBasicTest implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(StormTopologyTest.class);

    static String facebookGraphUrl = "https://graph.facebook.com/";

    public  void runTopology() throws AlreadyAliveException, InvalidTopologyException {

        final TopologyBuilder builder = buildTopolgy();
        final Config config = new Config();
        config.setDebug(false);
        final LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("local-async", config, builder.createTopology());
    }

    private TopologyBuilder buildTopolgy() {

        final Config config = new Config();
        final TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("checkins", new MySpout());
        builder.setBolt("async-http-bot", getAsyncHttpBolt());
       /* builder.setBolt("heatmap-builder", new HeatMapBuilderBolt(), 4).fieldsGrouping("geocode-lookup",
                new Fields("city")).addConfigurations(config);*/
        return builder;
    }

    private AsyncHttpBolt getAsyncHttpBolt() {
        return new AsyncHttpBolt().withRequestMapper(new RequestMapper() {
            @Override
            public AsyncHttpClient.BoundRequestBuilder getRequestFromTuple(Tuple tuple) {
                String str=tuple.getStringByField("str");
                return new AsyncHttpClient().prepareGet(getTargetUrl()).addQueryParam("str", str);
            }
        }).withResponseMapper(new ResponseMapper() {
            @Override
            public List<Object> getTuplesFromResponse(Response response) {
                try {
                    return new Values(response.getStatusCode(), response.getResponseBody());
                } catch (IOException e) {
                    Assert.fail(e.getMessage());
                    return null;
                }
            }


            @Override
            public Fields getOutputFields() {
                return new Fields("status","body");
            }
        });



    }

    @Test
    public  void run() throws AlreadyAliveException, InvalidTopologyException {
        System.out.printf("test");
        this.runTopology();

    }





}