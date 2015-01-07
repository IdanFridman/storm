package org.apache.storm.asynchttp.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import org.apache.storm.asynchttp.bolt.AsyncHttpBolt;
import org.junit.Test;

public class StormTopology {


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
        System.out.println("test");
        this.runTopology();

    }





}