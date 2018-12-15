/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package somado;


/**
 *
 * Szablon obiektu reprezentującego użytkownika (pozostałość ...)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class User {     

   public Integer getId() {
       return 1;
   }

   
   public boolean isAuthorized() {
       return true;
   }
   
   
   public boolean isAdmin() {
	   return true;
   }       
    
}
