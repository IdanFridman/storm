package org.apache.storm.asynchttp.storm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

public class MySpout extends BaseRichSpout {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MySpout.class);
    private String [] strings;
    private int nextEmitIndex;
    private SpoutOutputCollector outputCollector;

    public MySpout(String ... strings) {
        this.strings = strings;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("str"));
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.outputCollector = spoutOutputCollector;
        this.nextEmitIndex = 0;
    }

    @Override
    public void nextTuple() {
        if (nextEmitIndex < strings.length) {
            String string = strings[nextEmitIndex];
            Values tuple = new Values(string);
            logger.debug("Emitting:" + tuple);
            outputCollector.emit(tuple);
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}