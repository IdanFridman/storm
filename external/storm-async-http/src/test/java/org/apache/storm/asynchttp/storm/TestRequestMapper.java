package org.apache.storm.asynchttp.storm;

import backtype.storm.tuple.Tuple;
import com.ning.http.client.AsyncHttpClient;
import org.apache.storm.asynchttp.bolt.mapper.RequestMapper;

/**
 * Created by andrew on 9/1/15.
 */
public class TestRequestMapper implements RequestMapper {
    String targetUrl;

    public TestRequestMapper(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    @Override
    public AsyncHttpClient.BoundRequestBuilder getRequestFromTuple(Tuple tuple) {
        String str=tuple.getStringByField("str");
        return new AsyncHttpClient().prepareGet(targetUrl).addQueryParam("str", str);
    }
}
