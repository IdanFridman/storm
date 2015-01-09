package org.apache.storm.asynchttp.storm;

import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.ning.http.client.Response;
import org.apache.storm.asynchttp.bolt.mapper.ResponseMapper;
import org.junit.Assert;

import javax.naming.spi.Resolver;
import java.io.IOException;
import java.util.List;

/**
 * Created by andrew on 9/1/15.
 */
public class TestResponseMapper implements ResponseMapper {
    @Override
    public List<Object> getTuplesFromResponse(Response response) {
        try {
            return new Values(response.getStatusCode(), response.getResponseBody());
        } catch (IOException e) {
            Assert.fail(e.getMessage());
            return null;
        }
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("status","body");
    }
}
