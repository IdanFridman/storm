
package com.apache.storm.asynchttp.bolt;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.apache.storm.asynchttp.bolt.mapper.RequestMapper;
import com.apache.storm.asynchttp.bolt.mapper.ResponseMapper;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;


/**
 * Created by idan on 1/7/15.
 */

public class AsyncHttpBolt extends BaseRichBolt{

    private static final Logger LOG = LoggerFactory.getLogger(AsyncHttpBolt.class);


    private RequestMapper requestMapper;
    private ResponseMapper responseMapper;
    private OutputCollector outputCollector;

    public AsyncHttpBolt withRequestMapper(RequestMapper requestMapper) {
        this.requestMapper = requestMapper;
        return this;
    }

    public AsyncHttpBolt withResponseMapper(ResponseMapper responseMapper) {
        this.responseMapper = responseMapper;
        return this;
    }



    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        outputCollector=collector;

    }

    @Override
    public void execute(final Tuple input) {
        AsyncHttpClient.BoundRequestBuilder builder = requestMapper.getRequestFromTuple(input);
        builder.execute(new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(Response response) throws Exception {
                List<Object> output = responseMapper.getTuplesFromResponse(response);
                if(output!=null)
                {
                    emitTuple(input, output);
                }
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                super.onThrowable(t);
            }
        });
    }

    private void emitTuple(Tuple input, List<Object> output) {
        synchronized (outputCollector) {
            outputCollector.emit(input, output);
            outputCollector.ack(input);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(responseMapper.getOutputFields());
    }
}

