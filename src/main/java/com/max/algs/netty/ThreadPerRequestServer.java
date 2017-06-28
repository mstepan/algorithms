package com.max.algs.netty;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public final class ThreadPerRequestServer {

    private static final Logger LOG = Logger.getLogger(ThreadPerRequestServer.class);
    private static final int PORT = 7777;
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private ThreadPerRequestServer() throws Exception {

        ServerSocket serverSock = new ServerSocket(PORT);

        try {
            LOG.info("'ServerNettyMain' started at port " + PORT);

            while (!Thread.currentThread().isInterrupted()) {
                Socket sock = serverSock.accept();
                System.out.println("Connection from client accepted");
                new Thread(new SessionHandler(sock)).start();
            }
        }
        finally {
            serverSock.close();
        }

        System.out.printf("Main done: java-%s %n", System.getProperty("java.version"));
    }

    private static String handleResponse(String request) {
        return "Reponse: " + request + LINE_SEPARATOR;
    }

    public static void main(String[] args) {
        try {
            new ThreadPerRequestServer();
        }
        catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
        }
    }

    private static class SessionHandler implements Runnable {

        final Socket sock;

        public SessionHandler(Socket sock) {
            this.sock = sock;
        }

        @Override
        public void run() {

            try (InputStreamReader inStream = new InputStreamReader(sock.getInputStream());
                 BufferedReader reader = new BufferedReader(inStream)) {

                try (PrintWriter out = new PrintWriter(sock.getOutputStream(), true)) {

                    String request;

                    while ((request = reader.readLine()) != null) {

                        if ("exit".equals(request)) {
                            break;
                        }

                        String response = handleResponse(request);

                        out.write(response);
                        out.flush();
                    }
                }
            }
            catch (IOException ioEx) {
                LOG.error(ioEx.getMessage(), ioEx);
            }
            finally {
                try {
                    sock.close();
                }
                catch (IOException ioEx) {
                    LOG.error(ioEx.getMessage(), ioEx);
                }
            }
        }
    }

}

