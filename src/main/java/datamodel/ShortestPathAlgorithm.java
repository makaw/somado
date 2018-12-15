/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package datamodel;

import somado.Lang;

/**
 *
 * Wyliczenie dostępnych algorytmów wyznaczania najkrótszej ścieżki
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public enum ShortestPathAlgorithm {   
   
   /** Algorytm A* */ 
   A_STAR, 
   /** Algorytm Dijkstry */
   DIJKSTRA;      
    
   @Override
   public String toString() {
       
     switch (this) {
         
         default:
         case A_STAR: return "A*";
         case DIJKSTRA: return "Dijkstra";
         
     }  
    
   }   
   
   
   /**
    * Metoda zwraca dodatkowy opis algorytmu 
    * @return Opis algorytmu
    */
   public String getDescription() {
       
      switch (this) {
          
          default:
          case A_STAR:  return Lang.get("Data.Algorithm.FasterBut");    
          case DIJKSTRA: return Lang.get("Data.Algorithm.SlowerBut");          
          
      }        
       
   }
   
   
   /**
    * Metoda zwraca odpowiedni obiekt 
    * @param name Nazwa algorytmu
    * @return Obiekt określający algorytm
    */   
   public static ShortestPathAlgorithm get(String name) {
       
     switch (name) {
         
       default:  
       case "A*" :  return A_STAR;
       case "Dijkstra" : return DIJKSTRA;
         
     }  
       
   }
   
    
}
