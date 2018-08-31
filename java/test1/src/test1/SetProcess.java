/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

/**
 *
 * @author Sudhabindu
 */
public class SetProcess {
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(true){
            System.out.println("Enter the input: q to quit");
            String s;
            s = in.nextLine();
            if (s.equalsIgnoreCase("q")){
                break;
            }
            s = s.replaceAll("\\s+", "");
            processInput(s);
        }
        
    }
    
    public static void processInput(String s){
        ArrayList<Integer> acl = new ArrayList<Integer>();
        ArrayList<Integer> acr = new ArrayList<Integer>();
        for(int i = 0; i < s.length(); i++){
            if (s.charAt(i) == '['){
                acl.add(i);
            }
            else if(s.charAt(i) == ']'){
                acr.add(i);
            }
        }
        if (acl.size() != 2 || acr.size() != 2){
            System.out.println("Illegal input");
            return;
        }
        
        String s1 = s.substring(acl.get(0) + 1, acr.get(0));
        String s2 = s.substring(acl.get(1) + 1, acr.get(1));
        String op = s.substring(acr.get(0) + 1, acl.get(1));
        Scanner p;
        TreeSet<Integer> ts1, ts2;
        ts1 = new TreeSet<>();
        ts2 = new TreeSet<>();
        p = new Scanner(s1);
        p.useDelimiter("\\D+");
        while(p.hasNext()){
            int n = p.nextInt();
            ts1.add(n);
        }
        
        p.close();
        p = new Scanner(s2);
        p.useDelimiter("\\D+");
        while(p.hasNext()){
            int n = p.nextInt();
            ts2.add(n);
        }
        switch(op){
            case "+":
                ts1.addAll(ts2);
                break;
            case "-":
                ts1.removeAll(ts2);
                break;
            case "*":
                ts1.retainAll(ts2);
                break;
            default:
                System.out.println("Enter a logical operator");
                return;
        }
        
        System.out.println(ts1);
        
    }
}
