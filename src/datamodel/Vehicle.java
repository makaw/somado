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
import java.util.Calendar;
import somado.IConf;


/**
 *
 * Szablon obiektu reprezentującego konkretny pojazd
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Vehicle implements IDataEditable {

   /** Klucz w tabeli w BD */  
   private Integer id;
   /** Model pojazdu  */
   private VehicleModel vehicleModel;
   /** Nr rejestracyjny */
   private String registrationNo;  
   /** Rok produkcji */
   private int year;
   /** Komentarz */
   private String comment;
   /** Dostępność (sprawność) pojazdu */
   private boolean capable;
   
   
   /**
    * Konstruktor obiektu pojazdu (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @param vehicleModel Model pojazdu 
    * @param prefix Prefix pól dot. pojazdu w zapytaniu
    * @throws SQLException Błąd SQL
    */
   public Vehicle(ResultSet rs, VehicleModel vehicleModel, String prefix) throws SQLException {
       
     this.id = rs.getInt(prefix+"id");
     this.year = rs.getInt(prefix+"year");
     this.registrationNo = rs.getString(prefix+"registration_no");
     this.comment = rs.getString(prefix+"comment");
     this.capable = rs.getBoolean(prefix+"capable");
     this.vehicleModel = vehicleModel;                  
     
   }
   
   /**
    * Konstruktor obiektu pojazdu (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @param vehicleModel Model pojazdu 
    * @throws SQLException Błąd SQL
    */
   public Vehicle(ResultSet rs, VehicleModel vehicleModel) throws SQLException {
   
     this(rs, vehicleModel, "");
     
   }
       

   /**
    * Konstruktor
    * @param id Id rekordu w bazie danych
    * @param vehicleModel Model pojazdu
    * @param year Rok produkcji pojazdu
    * @param registrationNo Nr rejestracyjny
    * @param comment Komentarz
    * @param capable Dostępność (sprawność) pojazdu
    */
   public Vehicle(Integer id, VehicleModel vehicleModel, int year, String registrationNo, 
                   String comment, boolean capable) {
       
      this.id = id;
      this.vehicleModel = vehicleModel;
      this.year = year;
      this.registrationNo = registrationNo;
      this.comment = comment;
      this.capable = capable;
      
   }
   
   /**
    * Konstruktor tworzący kopię obiektu
    * @param vehicle Kopiowany obiekt
    */
   public Vehicle(Vehicle vehicle) {
       
     this(vehicle.id, vehicle.vehicleModel, vehicle.year, vehicle.registrationNo, vehicle.comment, vehicle.capable);         
       
   }
   
   /**
    * Pusty konstruktor (nowy pojazd)
    */
   public Vehicle() {
       
     this(0, new VehicleModel(), Calendar.getInstance().get(Calendar.YEAR), "", "", true);  
       
   }
   
   
   
   @Override
   public String toString() {
       
      if (id==0) return "(brak pojazdu)"; 
      return registrationNo + ": " + vehicleModel.getName() + " (" + String.valueOf(year) + ")";
       
   }
           
   

   @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VehicleModel getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(VehicleModel vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

        
    
    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCapable() {
        return capable;
    }

    public void setCapable(boolean capable) {
        this.capable = capable;
    }
    
    
    
    
    /**
     * Weryfikacja wprowadzonych danych
     * @throws Exception Wyjatek: komunikat bledu
     */    
    @Override
    public void verify() throws Exception {
        
      if (vehicleModel.getId()==0) throw new Exception("Nie wybrano modelu pojazdu.");  
      if (registrationNo.isEmpty())  throw new Exception("Nie podano nr rejestracyjnego pojazdu.");  
      if (year<IConf.MIN_VEHICLE_YEAR || year>Calendar.getInstance().get(Calendar.YEAR))
          throw new Exception("Nieprawid\u0142owy rok produkcji pojazdu.");
 
    }

   
   
    
}
