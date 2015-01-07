package org.apache.storm.asynchttp.bolt.mapper;

import backtype.storm.tuple.Fields;
import com.ning.http.client.Response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by idan on 1/7/15.
 */
public interface ResponseMapper extends Serializable {

    public List<Object> getTuplesFromResponse(Response tuple);
    public Fields getOutputFields();

}
