package org.apache.storm.asynchttp.bolt.mapper;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import com.ning.http.client.Response;

import java.util.List;

/**
 * Created by idan on 1/7/15.
 */
public interface ResponseMapper {

    public List<Object> getTuplesFromResponse(Response tuple);
    public Fields getOutputFields();

}
