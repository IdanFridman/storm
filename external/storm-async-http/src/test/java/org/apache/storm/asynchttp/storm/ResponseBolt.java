package org.apache.storm.asynchttp.storm;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import junit.framework.Assert;

/**
 * Created by idan on 1/7/15.
 */
public class ResponseBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        HttpTestResult.setResult(input.getIntegerByField("status"), input.getStringByField("body"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
