package test;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Sudhabindu on 30-Jun-17.
 */
public class DateServerWithThreadPool {

    public static final int DEFAULT_PORT = 32007;
    public static final int QUEUE_SIZE = 10;
    public static final int THREAD_POOL_SIZE = 5;
    public static ArrayBlockingQueue<Socket> queue;

    public static void main(String args[]){

        ServerSocket listener;
        Socket connection;
        try{
            listener = new ServerSocket(DEFAULT_PORT);
            System.out.println("LISTENING ON PORT: " + DEFAULT_PORT);
            queue = new ArrayBlockingQueue<Socket>(QUEUE_SIZE);
            for(int i = 0; i < THREAD_POOL_SIZE; i++){
                new ConnectionHandler();
            }
            while (true){
                connection = listener.accept();
                try{
                    queue.put(connection);
                }
                catch (InterruptedException e){

                }

            }
        }
        catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    public static class ConnectionHandler extends Thread{

        Socket client;
        public ConnectionHandler(){
            setDaemon(true);
            start();
        }

        public void run(){
            while(true){
                try {
                    client = queue.take();
                }
                catch (InterruptedException e){

                }

                String clientAddr = client.getInetAddress().toString();
                try{
                    PrintWriter out;

                    System.out.println("Connection from: " + clientAddr);
                    System.out.println("Handled by: " + this);
                    out = new PrintWriter(client.getOutputStream());
                    Date now = new Date();
                    out.println(now.toString());
                    out.flush();
                    client.close();
                }
                catch (Exception e){
                    System.out.println("Error in connection from: " + clientAddr);
                }
            }
        }
    }

}
