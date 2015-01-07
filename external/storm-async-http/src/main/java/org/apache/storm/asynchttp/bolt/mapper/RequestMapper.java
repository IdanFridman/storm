package org.apache.storm.asynchttp.bolt.mapper;

import backtype.storm.tuple.Tuple;
import com.ning.http.client.AsyncHttpClient;

import java.io.Serializable;

/**
 * Created by idan on 1/7/15.
 */
public interface RequestMapper extends Serializable {

    public AsyncHttpClient.BoundRequestBuilder getRequestFromTuple(Tuple tuple);
}
