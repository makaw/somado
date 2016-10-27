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
                
   /** ID użytkownika (baza danych) */
   private int id = 1;

   public Integer getId() {
       return id;
   }

   /**
    * Czy użytkownik zautoryzowany
    * @return True jeżeli tak
    */
   public boolean isAuthorized() {
       return id>0;
   }
   
   
   public boolean isAdmin() {
	   return true;
   }
   
    
    
}
