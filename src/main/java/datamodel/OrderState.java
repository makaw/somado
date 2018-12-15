/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package datamodel;

import java.awt.Color;

import somado.Lang;


/**
 *
 * Wyliczenie dostępnych stanów zamówienia
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public enum OrderState implements IData {   
   
   /** Nowe zamówienie */
   NEW(1), 
   /** Zamówienie w dostawie */
   DELIVERY(2), 
   /** Zamówienie zrealizowane (dostarczone) */
   DONE(3), 
   /** Zamówienie anulowane */
   CANCELLED(4); 
   
    
   /** ID stanu dla BD */ 
   private final Integer id;
   

   /**
    * Konstruktor
    * @param id ID stanu dla BD
    */
   OrderState(int id) {

      this.id = id; 

   }
   

   @Override
   public Integer getId() {
      return id;
   }         
    
    
   @Override
   public String toString() {
       
     switch (this) {
         
         default:
         case NEW: return Lang.get("Data.OrderState.New");
         case DELIVERY: return Lang.get("Data.OrderState.Delivery");
         case DONE: return Lang.get("Data.OrderState.Done");
         case CANCELLED: return Lang.get("Data.OrderState.Cancelled");
         
     }  
    
   }
   
   /**
    * Metoda zwraca kolor przyporządkowany do stanu
    * @return Kolor
    */
   public Color getColor() {

     switch (this) {
         
         default:
         case NEW: return new Color(0x0923aa);
         case DELIVERY: return new Color(0x990000);
         case DONE: return new Color(0x006633);
         case CANCELLED: return new Color(0x666666);
         
     }         

   }
   
   /**
    * Metoda zwraca odpowiedni obiekt dla ID z BD
    * @param id ID dla BD
    * @return Obiekt stanu
    */
   public static OrderState get(int id) {
       
     for(OrderState e : values()) if(e.id.equals(id)) return e;
     
     return null;
        
   }   


    
}
