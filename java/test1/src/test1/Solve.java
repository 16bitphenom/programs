/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

import java.util.Scanner;

/**
 *
 * @author Sudhabindu
 */
public class Solve {
    
    public static void main(String[] args) {
        solve();
    }
    
    public static void solve(){
        Scanner in = null;
        try{
            in = new Scanner(System.in);
            double A, B, C, x;
            System.out.println("Enter A: ");
            A = in.nextDouble();
            System.out.println("Enter B: ");
            B = in.nextDouble();
            System.out.println("Enter C: ");
            C = in.nextDouble();
            x = root(A, B, C);
            System.out.println("x = " + x);
            
            System.err.println("\n\n Do you want to continue?");
            String response = in.next();
            while(!response.equalsIgnoreCase("y") && !response.equalsIgnoreCase("yes") ){
                System.out.println("Please enter y or yes");
                response = in.next();
            }
            if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")){
                solve();
            }
            else{
                System.out.println("Quitting...");
            }
        }
        catch(Exception e){
            e.printStackTrace();
            
        }
        
    }
    
    public static double root(double A, double B, double C) throws IllegalArgumentException{
        if (A == 0){
            throw new IllegalArgumentException("A can\'t be zero!");
        }
        else{
            double disc = (B * B) - (4 * A * C);
            if (disc < 0){
                throw new IllegalArgumentException("Discriminant >= 0");
            }
            return (-B  + Math.sqrt(disc)) / (2 * A);
        }
    }
}
