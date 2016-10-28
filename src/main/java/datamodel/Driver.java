/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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
   /** Imię */
   private String firstname;
   /** Nazwisko */
   private String surname;
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
     this.firstname = rs.getString(prefix + "firstname");
     this.surname = rs.getString(prefix + "surname");
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
    * @param firstname Imię
    * z
    * @param vehicle Pojazd
    * @param comment Komentarz
    * @param available Dostępność kierowcy
    */
   public Driver(int id, String firstname, String surname, Vehicle vehicle, String comment, boolean available) {
       
     this.id = id;  
     this.firstname = firstname;
     this.surname = surname;
     this.vehicle = vehicle;
     this.comment = comment;
     this.available = available;
              
   }
   
   
   /**
    * Konstruktor tworzący kopię obiektu
    * @param driver Kopiowany obiekt
    */
   public Driver(Driver driver) {
       
      this(driver.id, driver.firstname, driver.surname, driver.vehicle, driver.comment, driver.available); 
       
   }
   
   
   /**
    * Pusty konstruktor (nowy kierowca)
    */
   public Driver() {
       
     this(0, "", "", new Vehicle(), "", true);  
       
   }
   
   
   @Override
   public String toString() {
       
      return surname + " " + firstname + 
    		  (vehicle.getId() > 0 ? " (" + vehicle.getRegistrationNo() + ")" : "");
       
   }
            

   @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    

    
    
    
    public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}

	

	/**
     * Weryfikacja wprowadzonych danych
     * @throws Exception Wyjatek: komunikat bledu
     */    
    @Override
    public void verify() throws Exception {
    
      if (surname.isEmpty()) throw new Exception("Nie podano nazwiska.");	
    	
    }
    
   
    
}
