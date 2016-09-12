/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;


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
          case A_STAR:  return "Szybszy, ale mo\u017ce nie znale\u017a\u0107 najlepszego rozwi\u0105zania";
          case DIJKSTRA: return "Wolniejszy, ale zawsze znajduje najlepsze rozwi\u0105zanie";          
          
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
