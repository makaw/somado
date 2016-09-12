/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;

import java.awt.Color;


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
         case NEW: return "nowe";
         case DELIVERY: return "w dostawie";
         case DONE: return "dostarczone";
         case CANCELLED: return "anulowane";
         
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
