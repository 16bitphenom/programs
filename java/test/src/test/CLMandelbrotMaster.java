package test;


import jdk.nashorn.internal.scripts.JO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Sudhabindu on 01-Jul-17.
 */
public class CLMandelbrotMaster {

    private static final int DEFAULT_PORT = 1729;
    private static final String TASK_COMMAND = "task";
    private static final String CLOSE_CONNECTION_COMMAND = "close";
    private static final String SHUTDOWN_COMMAND = "shutdown";
    private static final String RESULT_COMMAND = "result";

    private static ConcurrentLinkedQueue<CLMandelbrotTask> tasks;
    private static int tasksCompleted;
    private static int rows, columns;
    private static int maxIterations;
    private static int[][] mandelbrotData;

    public static void main(String args[]){

        long startTime = System.currentTimeMillis();
        createJobs();

        if (args.length == 0){
            System.out.println("Performing the task in this computer only");
            while (true){
                CLMandelbrotTask task = tasks.poll();
                if (task == null){
                    break;
                }
                task.compute();
                finishTask(task);
            }
        }
        else{
            WorkerConnection[] workers = new WorkerConnection[args.length];
            for(int i = 0; i < workers.length; i++){
                String host = args[i];
                int port = DEFAULT_PORT;
                int pos = host.indexOf(':');
                if (pos > 0){
                    String portString = host.substring(pos + 1);
                    host = host.substring(0, pos);
                    try{
                        port = Integer.parseInt(portString);
                    }
                    catch (NumberFormatException e){

                    }
                }
                workers[i] = new WorkerConnection(i + 1, host, port);
            }

            for(int i = 0; i < workers.length; i++){
                if(workers[i].isAlive()){
                    try{
                        workers[i].join();
                    }
                    catch (InterruptedException e){

                    }

                }
            }

            if (tasksCompleted != rows){
                System.out.print("Something went wrong, only " + tasksCompleted);
                System.out.println(" tasks out of " + rows + "tasks completed");
                System.exit(1);

            }
        }

        long elaspedTime = System.currentTimeMillis() - startTime;
        System.out.println("Finished in " + elaspedTime / 1000.0 + " seconds");
        saveImage();
    }

    private static void createJobs() {
        double xmin = -0.9548900066789311; // Region of xy-plane shown in the image.
        double xmax = -0.9548895970332226;
        double ymin = 0.2525416221154478;
        double ymax = 0.25254192934972913;
        maxIterations = 10000;
        rows = 768;
        columns = 1024;
        mandelbrotData = new int[rows][columns];
        double dx = (xmax - xmin)/(columns+1);
        double dy = (ymax - ymin)/(rows+1);
        tasks = new ConcurrentLinkedQueue<CLMandelbrotTask>();
        for (int j = 0; j < rows; j++) {  // Add tasks to the task list.
            CLMandelbrotTask task;
            task = new CLMandelbrotTask();
            task.id = j;
            task.maxIterations = maxIterations;
            task.y = ymax-j*dy;
            task.xmin = xmin;
            task.dx = dx;
            task.count = columns;
            tasks.add(task);
        }
    }

    private static void resassignTask(CLMandelbrotTask task){

        tasks.add(task);
    }

    synchronized private static void finishTask(CLMandelbrotTask task){

        int row = task.id;
        System.arraycopy(task.results, 0, mandelbrotData[row], 0, columns);
        tasksCompleted++;
    }

    private static String writeTask(CLMandelbrotTask task){

        StringBuffer buffer = new StringBuffer();
        buffer.append(TASK_COMMAND);
        buffer.append(" ");
        buffer.append(task.id);
        buffer.append(" ");
        buffer.append(task.maxIterations);
        buffer.append(" ");
        buffer.append(task.y);
        buffer.append(" ");
        buffer.append(task.xmin);
        buffer.append(" ");
        buffer.append(task.dx);
        buffer.append(" ");
        buffer.append(task.count);
        buffer.append(" ");
        return buffer.toString();
    }

    private static void readResults(String data, CLMandelbrotTask task) throws Exception{

        Scanner scanner = new Scanner(data);
        scanner.next();
        int id = scanner.nextInt();
        if (task.id != id){
            throw new Exception("Illegal task id received from worker");
        }
        int count = scanner.nextInt();
        if(task.count != count){
            throw new Exception("Illegal task count received from worker");
        }
        task.results = new int[count];
        for(int i = 0; i < count; i++){
            task.results[i] = scanner.nextInt();
        }
    }

    private static class WorkerConnection extends Thread{

        int id;
        String host;
        int port;

        WorkerConnection(int id, String host, int port){

            this.id = id;
            this.host = host;
            this.port = port;
            start();
        }

        public void run(){

            int tasksCompleted = 0;
            Socket socket;
            try{
                socket = new Socket(host, port);
            }
            catch (Exception e){
                System.out.println("Thread: " + id + " couldn\'t open connection to: " + host + ":" + port);
                System.out.println("Error: " + e);
                return;
            }

            CLMandelbrotTask currentTask = null;
            CLMandelbrotTask nextTask = null;

            try{
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                currentTask = tasks.poll();
                if (currentTask != null){
                    String taskString = writeTask(currentTask);
                    out.println(taskString);
                    out.flush();
                }

                while (currentTask != null){
                    String resultString = in.readLine();
                    if (resultString == null){
                        throw new IOException("The connection closed unexpectedly");
                    }
                    if (!resultString.startsWith(RESULT_COMMAND)){
                        throw new IOException("Illegal data sent by the worker");
                    }

                    nextTask = tasks.poll();
                    if (nextTask != null){
                        String taskString = writeTask(nextTask);
                        out.println(taskString);
                        out.flush();
                    }
                    readResults(resultString, currentTask);
                    finishTask(currentTask);
                    tasksCompleted++;
                    currentTask = nextTask;
                    nextTask = null;
                }
                out.println(CLOSE_CONNECTION_COMMAND);
                out.flush();
            }

            catch (Exception e){
                System.out.println("Thread: " + id + " terminated because of an error");
                System.out.println("Error: " + e);
                e.printStackTrace();

                if (currentTask != null){
                    resassignTask(currentTask);
                }
                if (nextTask != null){
                    resassignTask(nextTask);
                }
            }

            finally{
                System.out.println("Thread: " + id + " ending after completing " + tasksCompleted + " tasks");
                try{
                    socket.close();
                }
                catch (Exception e){

                }
            }

        }

    }

    private static void saveImage(){

        Scanner in = new Scanner(System.in);
        System.out.println();
        while (true){
            System.out.println("Computation complete, Do you want to save the image?");
            String line = in.nextLine().trim().toLowerCase();
            if (line.equals("no") || line.equals("n")){
                break;
            }
            else if (line.equals("yes") || line.equals("y")){
                JFileChooser fileDialog = new JFileChooser();
                fileDialog.setSelectedFile(new File("CLMandelbrot_image.png"));
                fileDialog.setDialogTitle("Select a file to save:");
                int option = fileDialog.showSaveDialog(null);
                if (option != JFileChooser.APPROVE_OPTION){
                    return;
                }
                File selectedFile = fileDialog.getSelectedFile();
                if (selectedFile.exists()){
                    int response = JOptionPane.showConfirmDialog(null,
                            "The file already exists, do you want to overrite?", null, JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                    if (response != JOptionPane.YES_OPTION){
                        return;
                    }
                }
                try{
                    int[] pallete = new int[250];
                    for(int i = 0; i < 250; i++){
                        Color c = new Color(i, i, i);
                        pallete[i] = c.getRGB();
                    }
                    BufferedImage OSI = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_RGB);
                    int[] rgb = new int[columns];
                    for(int row = 0; row < rows; row++){
                        for(int col = 0; col < columns; col++){
                            if (mandelbrotData[row][col] == maxIterations){
                                rgb[col] = 0;
                            }
                            else{
                                rgb[col] = pallete[(int)((mandelbrotData[row][col] * 250.0) / maxIterations)];
                            }
                        }
                        OSI.setRGB(0, row, columns, 1, rgb, 0, 1024);
                    }
                    boolean hasPng = ImageIO.write(OSI, "png", selectedFile );
                    if (!hasPng){
                        throw new Exception("PNG format not available???");
                    }
                }
                catch (Exception e){
                    System.out.println("Sorry but an exception occured while saving the image");
                    e.printStackTrace();
                }
                break;
            }
            else{
                System.out.println("Type yes or no");
            }
        }
    }

}
