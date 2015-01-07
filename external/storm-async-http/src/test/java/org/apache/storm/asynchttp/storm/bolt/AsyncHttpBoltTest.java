package org.apache.storm.asynchttp.storm.bolt;


import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import org.junit.Assert;
import org.junit.Test;

public class AsyncHttpBoltTest extends AbstractBasicTest {

    @Override
    public AsyncHttpClient getAsyncHttpClient(AsyncHttpClientConfig config) {
        return new AsyncHttpClient(config);
    }


    @Test
    public void testEmpty() {

    }
}