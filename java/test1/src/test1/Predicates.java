/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author Sudhabindu
 */
public class Predicates {
    
    public static <T> void remove(Collection<T> coll, Predicate<T> pred){
        Iterator<T> iter = coll.iterator();
        while(iter.hasNext()){
            T item = iter.next();
            if (pred.test(item)){
                iter.remove();
            }
        }
    }
    
    public static <T> void retain(Collection<T> coll, Predicate<T> pred){
        Iterator<T> iter = coll.iterator();
        while(iter.hasNext()){
            T item = iter.next();
            if(!pred.test(item)){
                iter.remove();
            }
        }
    }
    
    public static <T> List<T> collect(Collection<T> coll, Predicate<T> pred){
        List<T> l = new ArrayList<>();
        Iterator<T> iter = coll.iterator();
        while(iter.hasNext()){
            T item = iter.next();
            if (pred.test(item)){
                l.add(item);
            }
        }
        return l;
    }
    
    public static <T> int find(ArrayList<T> list, Predicate<T> pred){
       for(int i = 0; i < list.size(); i++){
           T item = list.get(i);
           if(pred.test(item)){
               return i;
           }
       }
       return -1;
    }
}
