package com.apache.storm.asynchttp.bolt;


import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;

public class AsyncHttpBoltTest extends AbstractBasicTest {

    @Override
    public AsyncHttpClient getAsyncHttpClient(AsyncHttpClientConfig config) {
        return null;
    }
}