package org.apache.storm.asynchttp.storm;

import backtype.storm.tuple.Tuple;
import com.ning.http.client.AsyncHttpClient;
import org.apache.storm.asynchttp.bolt.mapper.RequestMapper;

/**
 * Created by andrew on 9/1/15.
 */
public class TestRequestMapper implements RequestMapper {
    transient HttpServerSupport httpServerSupport = null;

    @Override
    public AsyncHttpClient.BoundRequestBuilder getRequestFromTuple(Tuple tuple) {
        ensureServer();
        String str=tuple.getStringByField("str");
        return new AsyncHttpClient().prepareGet(httpServerSupport.getTargetUrl()).addQueryParam("str", str);
    }

    synchronized public void ensureServer() {
        if (httpServerSupport != null)
            return;

        httpServerSupport = new HttpServerSupport();
    }
}
