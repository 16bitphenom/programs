/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

import com.sun.org.apache.xerces.internal.dom.TextImpl;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Sudhabindu
 */
public class Concordance {
    
    static TreeMap<String, ArrayList<Integer>> con;
    static int lineCount;
    public static void main(String[] args) {
        System.out.println("Enter the filename and press return");
        TextIO.getln();
        lineCount = 0;
        try{
            if (TextIO.readUserSelectedFile() == false){
                System.out.println("No input file selected. Exiting");
                System.exit(1);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
        con = new TreeMap<>();
        String word = readNextWord();
        System.out.println("Proceeding");
        while(word != null){
            System.out.println("Ding");
            word = word.toLowerCase();
            if (word.equals("the") || word.length() < 3){
                continue;
            }
            ArrayList<Integer> lines = con.get(word);
            if (lines == null){
                lines = new ArrayList<>();
                lines.add(lineCount);
                con.put(word, lines);
            }
            else{
                lines.add(lineCount);
            }
        }
        
        System.out.println("The words with the line numbers: ");
        for(String key: con.keySet()){
            System.out.print(key);
            System.out.print(": ");
            ArrayList<Integer> list = con.get(key);
            for(int i: list){
                System.out.print("" + i + " ");
            }
        }
    }
    
    public static String readNextWord(){
        char ch = TextIO.peek();
        while(ch != TextIO.EOF && Character.isLetter(ch) && ch != TextIO.EOLN){
            TextIO.getAnyChar();
        }
        if (ch == TextIO.EOF){
            return null;
        }
        if (ch == TextIO.EOLN){
            lineCount++;
        }
        
        String word = "";
        while (true){
            word += TextIO.getAnyChar();
            ch = TextIO.peek();
            if (ch == '\''){
                TextIO.getAnyChar();
                ch = TextIO.peek();
                if (Character.isLetter(ch)){
                    word += "\'" + TextIO.getAnyChar();
                    ch = TextIO.peek();
                    
                }
                else{
                    break;
                }
            }
            if (!Character.isLetter(ch)){
                break;
            }
        }
        return word;
    }
}
