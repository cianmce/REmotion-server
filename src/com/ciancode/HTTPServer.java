package com.ciancode;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;

/**
 * Created by cian on 31/05/15.
 */
public class HTTPServer {


        public void start() throws Exception {
            int port = 1234;
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/press/char/", new HandlePressChar());
            server.createContext("/press/key/", new HandlePressKey());
            server.createContext("/", new HandleUnsupported());
            server.setExecutor(null); // creates a default executor
            server.start();
            System.out.println("Starting server: "+port);
        }

        static class HandlePressChar implements HttpHandler {
            private HttpExchange httpExchange;
            @Override
            public void handle(HttpExchange t) throws IOException {
                httpExchange = t;
                String route = "/press/char/";
                URI uri = t.getRequestURI();
                String path = uri.getPath();
                if(path.length()>route.length()) {
                    char key = path.split(route)[1].charAt(0);
                    // Check if code or char
                    Action action = new Action();
                    action.press_ascii(key);

                    respond("OK", 200);
                    System.out.println("Pressed: "+key);
                }else{
                    respond("<h1>Error</h1>No Key provided", 400);
                }
            }
            private void respond(String response, int status) throws IOException {
                httpExchange.sendResponseHeaders(status, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        static class HandlePressKey implements HttpHandler {
            private HttpExchange httpExchange;
            @Override
            public void handle(HttpExchange t) throws IOException {
                httpExchange = t;
                String route = "/press/key/";
                URI uri = t.getRequestURI();
                String path = uri.getPath();
                if(path.length()>route.length()) {
                    String key = path.split(route)[1];
                    // Check if code or char
                    Action action = new Action();
                    if(action.press_special_key(key)) {
                        respond("OK", 200);
                        System.out.println("Pressed: " + key);
                    }else{
                        respond("<h1>Error</h1>Unable to press key: "+key, 404);
                        System.out.println("Can't Press: " + key);
                    }
                }else{
                    respond("<h1>Error</h1>No Key provided", 400);
                }
            }
            private void respond(String response, int status) throws IOException {
                httpExchange.sendResponseHeaders(status, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        static class HandleUnsupported implements HttpHandler {
            @Override
            public void handle(HttpExchange t) throws IOException {
                URI uri = t.getRequestURI();
                String path = uri.getPath();
                String response = "<h1>Error</h1>Route: \""+path+"\" is not supported";

                t.sendResponseHeaders(404, response.length());
                System.out.println("Unsupported");

                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
        public static String getBody(HttpExchange t) throws IOException{
            InputStream inStream = t.getRequestBody();

            InputStreamReader is = new InputStreamReader(inStream);
            StringBuilder sb=new StringBuilder();
            BufferedReader br = new BufferedReader(is);
            String read = br.readLine();

            while(read != null) {
                sb.append(read);
                read =br.readLine();
            }
            return sb.toString();

        }


}
