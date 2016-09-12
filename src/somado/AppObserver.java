/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

import java.util.Observable;

/**
 *
 * Szablon obiektu służącego do przekazywania informacji pomiędzy wątkami 
 * przy wykorzystaniu wzorca "Obserwatora" (Observer)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class AppObserver extends Observable  {
    
   /** Opis (klucz) przesyłanego obiektu */ 
   protected String key;
   /** Przesyłany obiekt */
   protected Object object;
    
   
   /**
    * Metoda przesyłająca dany obiekt do obserwujących obiektów
    * @param key Opis(klucz) przesyłanego obiektu
    * @param object Przesyłany obiekt
    */
   public void sendObject(String key, Object object) {

      this.key = key;
      this.object = object;
      setChanged();
      notifyObservers(this);
      
   }
   
   /**
    * Metoda pobierająca klucz przesyłanego obiektu
    * @return Klucz przesyłanego obiektu
    */
   public String getKey() {
       
     return key;  
       
   }
   
   /**
    * Metoda zwracająca przesyłany obiekt
    * @return Przesyłany obiekt
    */
   public Object getObject() {
       
     return object;  
       
   }
   
 
    
}
