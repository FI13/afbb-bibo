/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.afbb.bibo.servlet.server;

import de.afbb.bibo.servlet.server.servlet.MainServlet;
import javax.servlet.Servlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author philipp
 * todo: not all temporary files get deleted
 * todo: openkm is sometimes broken, after restart everything seems fine
 * todo: sometimes WebsocketRequest.data.get('id') is double - can not cast to String on WebSocket.loginUser(173)
 */
public class MyServer {

    private static MyServer instance;

    private static final Server server = new Server(8080);

    private static final Logger log = LoggerFactory.getLogger(MyServer.class);
    
    public static MyServer getInstance() {
        if (instance == null) {
            instance = new MyServer();
        }
        return instance;
    }

    private ServletContextHandler newServletContextHandler(
            String contextPath, Servlet servlet) {
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath(contextPath);
        handler.addServlet(new ServletHolder(servlet), "/*");
        return handler;
    }

    /**
     * Starts the server. It creates two ServletContextHandler, one for the SSE
     * and one for the standard handling, and combines them in a
     * ContextHandlerCollection. Then it connects it with the Server
     *
     * @throws Exception
     */
    public void start()
            throws Exception {
        while (true) {

            server.setStopAtShutdown(true);

            ServletContextHandler standard = newServletContextHandler("/", new MainServlet());


            server.setHandler(standard);

            log.info("started Server");
            server.start();
            server.join();

            //server stopped. going to be restarted
            log.warn("restarting Jetty...");
        }

    }

    /**
     * Stops the Server.
     *
     * @param async
     * @throws Exception
     */
    public void stop()
            throws Exception {
        new Thread() {
            @Override
            public void run() {
                try {
                    server.stop();
                } catch (Exception ex) {
                    log.info("Failed to stop Jetty", ex);
                }
            }
        }.start();
    }

    private MyServer() {
    }

    public void resetServer() throws Exception {
        stop();
    }

    public static void main(String[] args)
            throws Exception {
        try {

            // 2015-05-06 kr , experimental: try to move server initialization to a separate thread to start faster in here
            // and make sure WebSockets and the import jobs don't affect each other too much. unsure whether this really works
            // well.
            final Thread serverThread = new Thread() {
                public void run() {
                    try {
                        MyServer.getInstance().start();
                    } catch (Exception ex) {
                        log.error("{}", ex);
                    }
                }
            };

            final Thread dbInitThread = new Thread() {
                public void run() {
                    //init database
                }
            };

            serverThread.start();
            dbInitThread.start();

        } catch (Exception ex) {
            log.error("{}", ex);
        }
    }
}
