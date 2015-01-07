package org.apache.storm.asynchttp.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import org.apache.log4j.PropertyConfigurator;
import org.apache.storm.asynchttp.bolt.AsyncHttpBolt;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StormTopology {

    private static final Logger LOG = LoggerFactory.getLogger(StormTopology.class);



    static String facebookGraphUrl = "https://graph.facebook.com/";

    public  void runTopology() throws AlreadyAliveException, InvalidTopologyException {

        final TopologyBuilder builder = buildTopolgy();
        final Config config = new Config();
        config.setDebug(false);
        final LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("local-async", config, builder.createTopology());
    }

    private static TopologyBuilder buildTopolgy() {

        final Config config = new Config();
        final TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("checkins", new MySpout());
        builder.setBolt("async-http-bot", new AsyncHttpBolt());
       /* builder.setBolt("heatmap-builder", new HeatMapBuilderBolt(), 4).fieldsGrouping("geocode-lookup",
                new Fields("city")).addConfigurations(config);*/
        return builder;
    }

    @Test
    public  void run() throws AlreadyAliveException, InvalidTopologyException {
       throw new RuntimeException("test");
       // this.runTopology();

    }





}