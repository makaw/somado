/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package somado;

import datamodel.IData;


/**
 *
 * Wyliczenie dostępnych ról użytkownika systemowego
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public enum UserRole implements IData {   
   
   /** Administrator */ 
   ADMIN(1), 
   /** Dyspozytor */
   DISPATCHER(2), 
   /** Kierowca */
   DRIVER(3), 
   /** Stały klient */
   CUSTOMER(4); 
   
    
   /** ID roli dla BD */ 
   private final Integer id;
   

   /**
    * Konstruktor
    * @param id ID roli dla BD
    */
   UserRole(int id) {

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
         case ADMIN: return "administrator";
         case DISPATCHER: return "dyspozytor";
         case DRIVER: return "kierowca";
         case CUSTOMER: return "sta\u0142y klient";
         
     }  
    
   }
   
   
   /**
    * Metoda zwraca odpowiedni obiekt dla ID z BD
    * @param id ID dla BD
    * @return Obiekt roli użytkownika
    */
   public static UserRole get(int id) {
       
     for(UserRole e : values()) if(e.id.equals(id)) return e;
     
     return null;
        
   }   


    
}
