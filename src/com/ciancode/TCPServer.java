package com.ciancode;

/**
 * Created by cian on 11/05/15.
 */
import java.io.*;
import java.net.*;
class TCPServer {
    public static void main(String argv[]) throws Exception {
        String clientSentence;
        String response = "HTTP/1.1 204 NO CONTENT\r\n" +
                "Content-Length: 0\r\n" +
                "Connection: close\r\n";
        ServerSocket serverSock = new ServerSocket(2121);
        while (true) {
            System.out.println("Waiting on input");
            Socket connectionSocket = serverSock.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            PrintWriter out = new PrintWriter(connectionSocket.getOutputStream(), false);
            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);
            //capitalizedSentence = clientSentence.toUpperCase() + '\n';

            out.write(response);
            out.flush();
            out.close();
        }
    }
}

