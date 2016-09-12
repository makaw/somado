/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Szablon obiektu reprezentującego kierowcę
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Driver implements IDataEditable {
  
   /** ID w bazie danych */
   private Integer id;
   /** Użytkownik systemowy */
   private UserData userData;
   /** Przypisany pojazd */
   private Vehicle vehicle;
   /** Komentarz */
   private String comment;
   /** Dostępność */
   private boolean available;
   /** Czy wraca do magazynu (zmienna dla planowania dostawy) */
   private boolean returnToDepot = true;
   
   
   /**
    * Konstruktor obiektu kierowcy (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @param prefix Prefix pól dot. kierowcy w zapytaniu
    * @throws SQLException Błąd SQL
    */
   public Driver(ResultSet rs, String prefix) throws SQLException {
       
     this.id = rs.getInt(prefix+"id");  
     this.userData = new UserData(rs, prefix+"user_");
     this.vehicle = new Vehicle(rs, new VehicleModel(rs, prefix+"vehicle_model_"),  prefix+"vehicle_");
     this.comment = rs.getString(prefix+"comment");
     this.available = rs.getBoolean(prefix+"available");
     
   }
   
   
   /**
    * Konstruktor obiektu kierowcy (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @throws SQLException Błąd SQL
    */   
   public Driver(ResultSet rs) throws SQLException {
       
     this(rs, "");  
       
   }
   
   
   /**
    * Konstruktor obiektu kierowcy 
    * @param id Id rekordu w bazie danych
    * @param userData Dane użytkownika systemowego - kierowcy
    * @param vehicle Pojazd
    * @param comment Komentarz
    * @param available Dostępność kierowcy
    */
   public Driver(int id, UserData userData, Vehicle vehicle, String comment, boolean available) {
       
     this.id = id;  
     this.userData = userData;
     this.vehicle = vehicle;
     this.comment = comment;
     this.available = available;
              
   }
   
   
   /**
    * Konstruktor tworzący kopię obiektu
    * @param driver Kopiowany obiekt
    */
   public Driver(Driver driver) {
       
      this(driver.id, driver.userData, driver.vehicle, driver.comment, driver.available); 
       
   }
   
   
   /**
    * Pusty konstruktor (nowy kierowca)
    */
   public Driver() {
       
     this(0, new UserData(), new Vehicle(), "", true);  
       
   }
   
   
   @Override
   public String toString() {
       
      return userData.getSurname() + " " + userData.getFirstname() + " (" + vehicle.getRegistrationNo() + ")";
       
   }
            

   @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }
   

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    
    public boolean isReturnToDepot() {
        return returnToDepot;
    }

    public void setReturnToDepot(boolean returnToDepot) {
        this.returnToDepot = returnToDepot;
    }
    

    
    
    
    /**
     * Weryfikacja wprowadzonych danych
     * @throws Exception Wyjatek: komunikat bledu
     */    
    @Override
    public void verify() throws Exception {
    
    }
    
   
    
}
