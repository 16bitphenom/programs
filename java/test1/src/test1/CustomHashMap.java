/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

import com.sun.glass.ui.Size;
import java.util.Scanner;

/**
 *
 * @author Sudhabindu
 */
public class CustomHashMap {
    
    private String[] list;
    private int size;
    private static int TOT = 10;
    
    public CustomHashMap(int size){
        this.list = new String[size];
        for(int i = 0; i < size; i++){
            this.list[i] = null;
        }
        this.size = size;
    }
    
    public String[] getNodes(){
        return this.list;
    }
    
    public String getNode(int pos){
        return this.list[pos];
    }
    
    public int getSize(){
        return this.size;
    }
    
    public void setNode(String val, int pos){
        this.list[pos] = val;
    }
    
    
    
    public String get(String key){
        int hc = key.hashCode() % TOT;
        return this.list[hc];
    }
    
    public void put(String key, String val){
        int hc = key.hashCode() % TOT;
        this.list[hc] = val;
    }
    
    public void removeKey(String key){
        int hc = key.hashCode() % TOT;
        this.setNode(null, hc);
    }
    
    public boolean containsKey(String key){
        int hc = key.hashCode() % TOT;
        return !(this.list[hc] == null);
    }
    
    public int size(){
        int count = 0;
        for(int i = 0; i < TOT; i++){
            if (this.list[i] != null){
                count++;
            }
        }
        return count;
    }
    
    public static void main(String[] args) {
        
        CustomHashMap hm = new CustomHashMap(TOT);
        Scanner in = new Scanner(System.in);
        hm.put("joy", "990");
        hm.put("jod", "232");
        System.out.println(hm.get("joy"));
        System.out.println(hm.size());
        System.out.println(hm.containsKey("joy"));
       
    }
    
}
