/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

import java.awt.Color;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

/**
 *
 * @author Sudhabindu
 */
public class Test1 {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
      Object list = new CustomClass();
      Object obj;
      Class c = list.getClass();
      try{
          obj = c.newInstance();
          Method m = c.getMethod("doSomething", new Class[]{String.class});
          m.invoke(list, new Object[]{"My label"});
      }
      catch(Exception e){
          e.printStackTrace();
      }

        
    }
}
