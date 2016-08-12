/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.proxy;

import java.io.Closeable;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

class TextResponseEmbedHttpServer implements Closeable {

  private static final String HOST = "http://127.0.0.1";
  private static final String PATH = "/";

  private final int port;

  private Server server;

  TextResponseEmbedHttpServer(int port, String textResponse) {
    this.server = new Server(port);
    this.port = port;

    server.setHandler(new TextJettyHandler(textResponse));

    try {
      server.start();
    } catch (Exception ex) {
      throw new RuntimeException("Starting Jetty server interrupted by internal exception", ex);
    }
  }

  String getPath() {
    return HOST + ":" + port + PATH;
  }

  @Override
  public void close() {
    try {
      server.stop();
    } catch (Exception ex) {
      throw new RuntimeException("Shutting down Jetty server interrupted by internal exception",
          ex);
    }
  }

  private class TextJettyHandler extends AbstractHandler {

    private final String text;

    private TextJettyHandler(String text) {
      this.text = text;
    }

    @Override
    public void handle(String s, Request request, HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) throws IOException, ServletException {
      httpServletResponse.setContentType("text/html; charset=utf-8");
      httpServletResponse.setStatus(HttpServletResponse.SC_OK);
      httpServletResponse.getWriter().println(text);
      request.setHandled(true);
    }
  }
}
