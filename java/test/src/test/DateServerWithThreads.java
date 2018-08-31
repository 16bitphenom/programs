package test;

import com.sun.corba.se.spi.activation.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Sudhabindu on 30-Jun-17.
 */
public class DateServerWithThreads {

    public static final int DEFAULT_PORT = 32007;

    public static void main(String args[]){

        ServerSocket listener;
        Socket connection;

        try{
            listener = new ServerSocket(DEFAULT_PORT);
            System.out.println("LISTENING ON PORT: " + DEFAULT_PORT);
            while(true){
                connection = listener.accept();
                ConnectionHandler handler = new ConnectionHandler(connection);
                handler.start();
            }
        }
        catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public static class ConnectionHandler extends Thread{

        Socket client;

        public ConnectionHandler(Socket connection){
            this.client = connection;
        }

        public void run(){

            PrintWriter out;
            Date now;
            String clientAddress = client.getInetAddress().toString();
            System.out.println("Connection from: " + clientAddress);
            try{
                out = new PrintWriter(client.getOutputStream());
                now = new Date();
                out.println(now.toString());
                out.flush();
                client.close();
            }
            catch (Exception e){
                System.out.println("Error on connection with: " + clientAddress);
            }

        }
    }
}
