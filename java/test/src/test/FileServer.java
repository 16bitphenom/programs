package test;

/**
 * Created by Sudhabindu on 18-Jun-17.
 */
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FileServer {

    public static String HANDSHAKE = "FILESHARE";
    public static final int DEFAULT_PORT = 1729;

    public static void main(String args[]){
        int port;
        ServerSocket listener;
        Socket connection = null;
        Scanner incoming;
        PrintWriter outgoing;
        String messageIn;
        String directoryName;
        Scanner userInput = new Scanner(System.in);

        System.out.println("Enter the port number or return: ");
        String line = userInput.nextLine();
        if (line.length() == 0){
            port = DEFAULT_PORT;
        }
        else {
            port = Integer.parseInt(line);
        }
        if (port < 0 || port > 65535){
            System.out.println("Illegal port number");
            System.exit(1);
        }

        System.out.println("Enter directory");
        directoryName = userInput.nextLine();
        File diectory = new File(directoryName);
        if (!diectory.exists()){
            System.out.println("The specified file is not a directory");
            return;
        }
        if (!diectory.isDirectory()){
            System.out.println("The specified directory doesn\'t exist");
        }

        try {
            listener = new ServerSocket(port);
            System.out.println("Listening on port: " + port);
            while (true){
                connection = listener.accept();
                incoming = new Scanner(connection.getInputStream());
                outgoing = new PrintWriter(connection.getOutputStream());
                messageIn = incoming.nextLine();
                if (!messageIn.equals(HANDSHAKE)){
                    System.out.println("Incompatible client");
                    return;
                }
                outgoing.println(HANDSHAKE);
                outgoing.flush();
                messageIn = incoming.nextLine();
                if (messageIn.equalsIgnoreCase("index")){
                    System.out.println("OK   " + connection.getInetAddress() + " " + messageIn);
                    sendIndex(outgoing, diectory);
                }
                else if(messageIn.toLowerCase().startsWith("get")){
                    String filename = messageIn.substring(4).trim();
                    File file = new File(diectory, filename);
                    if (!file.exists() || file.isDirectory()){
                        outgoing.println("file exists/not a file");
                        System.out.println("Error");
                        return;
                    }
                    outgoing.println("OK");
                    outgoing.flush();
                    System.out.println("OK   " + connection.getInetAddress() + " " + messageIn);
                    sendFile(outgoing, file);
                }
                else{
                    System.out.println("ERROR   " + connection.getInetAddress() + " " + messageIn);
                    return;
                }
            }

        }
        catch (IOException e){
            System.out.println("Server shut down unexpectedly");
            System.out.println("Error: "+ e);
        }
        finally {
            try{
                connection.close();
            }
            catch (Exception e){
                System.out.println("Error: " + e);
            }

        }

    }

    public static void sendIndex(PrintWriter outgoing, File directory) throws IOException{

        String[] fileList = directory.list();
        for(int i = 0; i < fileList.length; i++){
            outgoing.println(fileList[i]);
        }
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError()){
            throw new IOException("Error while transmitting data");
        }
    }

    public static void sendFile(PrintWriter outgoing, File file) throws IOException{
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            while(true){
                String line = in.readLine();
                if(line == null){
                    break;
                }
                outgoing.println(line);
            }
            outgoing.flush();
            outgoing.close();
            if(outgoing.checkError()){
                throw new IOException("Error while transmitting data");
            }
        }
        catch (IOException e){
            System.out.println("Error while reading from file");
            System.out.println("Error: " + e);
        }

    }
}
