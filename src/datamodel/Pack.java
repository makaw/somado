/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * Szablon obiektu reprezentującego "paczkę" - zamówienia pogrupowane wg odbiorców towaru
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Pack  implements IData {
    
   /** ID w DB */ 
   private Integer id;  
   /** Obiekt odbiorcy towaru */
   private final Customer customer;
   /** Lista zamówień */
   private final List<Order> orders;
   /** Łączna waga przesyłki */
   private double totalWeight;

   
   /**
    * Konstruktor
    * @param customer Obiekt odbiorcy towaru
    */
   public Pack(Customer customer) {
       
     this.id = 0;  
     this.customer = customer;
     this.orders = new ArrayList<>();     
     this.totalWeight = 0.0;
       
   }
   
   
   /**
    * Dodanie zamówienia do przesyłki
    * @param order Obiekt zamówienia
    */
   public void addOrder(Order order) {
       
     orders.add(order);
     for (OrderItem o : order.getProducts()) totalWeight += o.getTotalWeight();

   }
   

   /**
    * Zwrócenie listy zamówień (jako nowy obiekt)
    * @return Lista zamówień
    */
   public List<Order> getOrdersList() {
       
     return new ArrayList<>(orders);
       
   }   
   
   

   public Customer getCustomer() {
       return customer;
   }


   public double getTotalWeight() {
       return totalWeight;
   }


   @Override
   public Integer getId() {
       return id;
   }

   public void setId(int id) {
       this.id = id;
   }
   
  
    
}
