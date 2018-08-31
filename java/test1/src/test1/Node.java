/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

/**
 *
 * @author Sudhabindu
 */
public class Node {
    
    private String data;
    private Node next;

        public Node(String data) {
            this.data = data;
            this.next = null;
        }
        
        public String getData(){
            return this.data;
        }
        
        public Node getNext(){
            return this.next;
        }
        
        public void setData(String data){
            this.data = data;
        }
        
        public void setNext(Node next){
            this.next = next;
        }
    
}
