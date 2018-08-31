/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Sudhabindu
 */
public class PhoneDirectory {
    
    private static TreeMap<String, String> phoneBook;
    private static File dataFile = null;
    private static BufferedReader inF = null;
    private static PrintWriter outF = null;

    public static void main(String[] args) {
        
        dataFile = new File("phonedata.txt");
        phoneBook = new TreeMap<>();
        try{
            if (!dataFile.exists()){
                dataFile.createNewFile();
            }
            inF = new BufferedReader(new FileReader(dataFile));
            
            if (dataFile.length() != 0){
                String line;
                while ((line = inF.readLine()) != null) {
                    int pos = line.indexOf("$");
                    if (pos <= 0){
                        System.out.println("illegal data found");
                        System.exit(1);
                    }
                    String s1 = line.substring(0, pos);
                    String s2 = line.substring(pos + 1);
                    phoneBook.put(s1, s2);
                }
            }
        }
        catch(IOException e){
            System.out.println("Some error in opening the file");
            e.printStackTrace();
        }
        
        Scanner in = new Scanner(System.in);
        
        while (true){
            System.out.println("\n\nSelect an option: ");
            System.out.println("1: Add an entry: ");
            System.out.println("2: Edit an entry: ");
            System.out.println("3: Remove an entry: ");
            System.out.println("4: Print out the whole directory");
            System.out.println("5: Exit");
            System.out.print(">> ");
            int option = in.nextInt();
            switch(option){
            case 1:
                addEntry();
                break;
            case 2:
                editEntry();
                break;
            case 3:
                removeEntry();
                break;
            case 4:
                printBook();
                break;
            case 5:
                System.out.println("Exiting...");
                try{
                    outF = new PrintWriter(dataFile);
                    if (phoneBook.size() != 0){
                        for(String name: phoneBook.keySet()){
                            outF.write(name + "$" + phoneBook.get(name) + "\n");
                        }
                    }
                    outF.flush();
                    inF.close();
                    outF.close();
                    
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                System.exit(0);
                break;
             default:
                 System.out.println("Please select between 1-5");
            }
        }
       
    }
    
    private static void addEntry(){
        
        Scanner in = new Scanner(System.in);
        String name, number;
        System.out.println("Enter the name: ");
        name = in.nextLine().toLowerCase();
        System.out.println("Enter the number: ");
        number = in.nextLine().toLowerCase();
        
        if (name.indexOf("$") >= 0 || number.indexOf("$") >= 0){
            System.out.println("Name or number can\'t contain $");
            return;
        }
        
        if (phoneBook.keySet().contains(name)){
            System.out.println("Name already exists, Edit?");
            String response = in.nextLine();
            if (!response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("y")){
                return;
            }
            phoneBook.remove(name);
        }
        phoneBook.put(name, number);
        System.out.println("Entry added");
    }
    
    private static void editEntry(){
        
        Scanner in = new Scanner(System.in);
        String name, number;
        System.out.println("Enter the name you want to edit: ");
        name = in.nextLine().toLowerCase();
        if (name.indexOf("$") >= 0){
            System.out.println("Name can\'t have $");
            return;
        }
        
        if (!phoneBook.keySet().contains(name)){
            System.out.println("Name not in record, add new");
            String response = in.nextLine().toLowerCase();
            if(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes")){
                return;
            }
        }
        
        System.out.println("Enter the number: ");
        number = in.nextLine().toLowerCase();
        if (number.indexOf("$") >= 0){
            System.out.println("Number can\'t contain $");
            return;
        }
        phoneBook.put(name, number);
        System.out.println("Entry edited");
        
    }
    
    private static void removeEntry(){
        
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the name you want to remove");
        String name = in.nextLine().toLowerCase();
        if (!phoneBook.keySet().contains(name)){
            System.out.println("Name not in records");
            return;
        }
        phoneBook.remove(name);
        System.out.println("Entry removed");
    }
    
    private static void printBook(){
        
        if (phoneBook.size() == 0){
            System.out.println("Phonebook empty");
            return;
        }
        
        System.out.println("Name           Number");
        for(String name: phoneBook.keySet()){
            System.out.println(name + "     " + phoneBook.get(name));
        }
    }
    
}
