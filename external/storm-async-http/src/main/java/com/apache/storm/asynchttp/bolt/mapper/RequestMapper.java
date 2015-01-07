package com.apache.storm.asynchttp.bolt.mapper;

import backtype.storm.tuple.Tuple;
import com.ning.http.client.AsyncHttpClient;

/**
 * Created by idan on 1/7/15.
 */
public interface RequestMapper {

    public AsyncHttpClient.BoundRequestBuilder getRequestFromTuple(Tuple tuple);
}
