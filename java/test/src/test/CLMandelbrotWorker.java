package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Sudhabindu on 01-Jul-17.
 */
public class CLMandelbrotWorker {

    private static final int DEFAULT_PORT = 1729;
    private static final String CLOSE_CONNECTION_COMMAND = "close";
    private static final String SHUT_DOWN_COMMAND = "shutdown";
    private static final String TASK_COMMAND = "task";
    private static final String RESULT_COMMAND = "result";

    private static boolean shutDownCommandReceived;

    public static void main(String args[]){

        int port = DEFAULT_PORT;
        if (args.length > 0){

            try{
                port = Integer.parseInt(args[0]);
                if (port < 0 || port > 65535){
                    throw new NumberFormatException();
                }
            }
            catch (NumberFormatException e){
                port = DEFAULT_PORT;
            }
        }

        System.out.println("Starting with listening on port: " + port);
        while (shutDownCommandReceived == false){

            ServerSocket listener = null;
            try{
                listener = new ServerSocket(port);
            }
            catch (Exception e){
                System.out.println("Can't listen on port: " + port);
                System.exit(1);
            }

            Socket connection;
            try{
                connection = listener.accept();
                connection.close();
                System.out.println("Connected to: " + connection.getInetAddress());
                handleConnection(connection);
            }
            catch (Exception e){
                System.out.println("Server shut down with error");
                System.out.println("Error: " + e);
                System.exit(2);
            }

        }

        System.out.println("Shutting down normally");
    }

    public static CLMandelbrotTask readTask(String command){

        try{
            Scanner scanner = new Scanner(command);
            scanner.next();
            CLMandelbrotTask result = new CLMandelbrotTask();
            result.id = scanner.nextInt();
            result.maxIterations = scanner.nextInt();
            result.y = scanner.nextDouble();
            result.xmin = scanner.nextDouble();
            result.dx = scanner.nextDouble();
            result.count = scanner.nextInt();

            return result;
        }

        catch (Exception e){
            System.out.println("Illegal data found while parsing data received");
        }

        return null;
    }

    public static String writeResults(CLMandelbrotTask task){

        StringBuffer buffer = new StringBuffer();
        buffer.append(RESULT_COMMAND);
        buffer.append(" ");
        buffer.append(task.id);
        buffer.append(" ");
        buffer.append(task.count);
        for(int i = 0; i < task.count; i++){
            buffer.append(" ");
            buffer.append(task.results[i]);
        }

        return buffer.toString();
    }

    public static void handleConnection(Socket connection){

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter out = new PrintWriter(connection.getOutputStream());

            while (true){
                String line = in.readLine();
                if (line == null){
                    throw new Exception("Connection terminated unexpectedly");
                }
                if (line.startsWith(CLOSE_CONNECTION_COMMAND)){
                    System.out.println("Received close command");
                    break;
                }
                else if(line.startsWith(SHUT_DOWN_COMMAND)){
                    System.out.println("Received shut down command");
                    shutDownCommandReceived = true;
                    break;
                }
                else if (line.startsWith(TASK_COMMAND)){
                    CLMandelbrotTask task = readTask(line);
                    task.compute();
                    out.println(writeResults(task));
                    out.flush();
                }
                else{
                    throw new Exception("Illegal command received");
                }
            }
        }
        catch (Exception e){
            System.out.println("Client connection ended with error: ");
            System.out.println(e);
        }
        finally {
            try{
                connection.close();
            }
            catch (Exception e){

            }
        }
    }

}
