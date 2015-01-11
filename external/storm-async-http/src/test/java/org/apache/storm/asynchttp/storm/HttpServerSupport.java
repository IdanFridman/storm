/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.apache.storm.asynchttp.storm;

import com.ning.http.client.*;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.Enumeration;

/*
 * Copyright 2010 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
public class HttpServerSupport {

    public final static String TEXT_HTML_CONTENT_TYPE_WITH_UTF_8_CHARSET = "text/html; charset=UTF-8";

    protected final Logger log = LoggerFactory.getLogger(HttpServerSupport.class);
    transient protected Server server;
    protected int port1;

    public final static int TIMEOUT = 30;

    public static class EchoHandler extends AbstractHandler {

        @Override
        public void handle(String pathInContext,
                           Request request,
                           HttpServletRequest httpRequest,
                           HttpServletResponse httpResponse) throws IOException, ServletException {
            String strParam = httpRequest.getParameter("str");
            httpResponse.getOutputStream().print(strParam);

            httpResponse.setStatus(200);
            httpResponse.getOutputStream().flush();
            httpResponse.getOutputStream().close();
        }
    }

    public void stop () throws Exception {
        server.stop();
    }

    protected int findFreePort() throws IOException {
        ServerSocket socket = null;

        try {
            socket = new ServerSocket(0);

            return socket.getLocalPort();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    protected String getTargetUrl() {
        return String.format("http://127.0.0.1:%d/foo/test", port1);
    }

    public AbstractHandler configureHandler() throws Exception {
        return new EchoHandler();
    }

    public void start () throws Exception {
        server = new Server();

        port1 = findFreePort();

        Connector listener = new SelectChannelConnector();

        listener.setHost("127.0.0.1");
        listener.setPort(port1);

        server.addConnector(listener);

        server.setHandler(configureHandler());
        server.start();
        log.info("Local HTTP server started successfully");
    }
}