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
 * Szablon obiektu reprezentującego odbiorcę towaru (pozycja ze słownika)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Customer implements IDataEditable {
    
    
    /** Klucz w tabeli w BD */
    private Integer id;
    /** Nazwa firmy */
    private String name;
    /** Miasto */
    private String city;
    /** Ulica */
    private String street;
    /** Kod pocztowy */
    private String postcode;
    /** Długość geograficzna (po geokodowaniu) */
    private double longitude;
    /** Szerokość geograficzna (po geokodowaniu) */
    private double latitude;
    /** ID najbliższego węzła w sieci drogowej */
    private int roadNodeId;
    /** Indeks dla definicji problemu VRP */
    private int index = -1;

    
    /**
     * Konstruktor obiektu odbiorcy towaru (z BD)
     * @param rs Zestaw wyników zapytania do BD
     * @param prefix Prefix pól dot. odbiorcy towaru w zapytaniu
     * @throws SQLException Błąd SQL
     */
    public Customer(ResultSet rs, String prefix) throws SQLException {
      
      id = rs.getInt(prefix+"id");  
      name = rs.getString(prefix+"name");      
      city = rs.getString(prefix+"city");
      street = rs.getString(prefix+"street");
      postcode = rs.getString(prefix+"postcode");
      longitude = rs.getDouble(prefix+"longitude");      
      latitude = rs.getDouble(prefix+"latitude");
      
    }
    
  
    /**
     * Konstruktor obiektu odbiorcy towaru (z BD)
     * @param rs Zestaw wyników zapytania do BD
     * @throws SQLException Błąd SQL
     */    
    public Customer(ResultSet rs) throws SQLException {
        
      this(rs, "");  
        
    }
    
    /**
     * Konstruktor obiektu odbiorcy towaru (edycja)
     * @param id Id rekordu w bazie
     * @param name Nazwa
     * @param city Miasto
     * @param street Adres (ulica+nr)
     * @param postcode Kod pocztowy
     * @param longitude Długość geograficzna 
     * @param latitude Szerokość geograficzna 
     */
    public Customer(Integer id, String name, String city, String street, String postcode, 
                double longitude, double latitude) {
      
      this.id = id; 
      this.name = name;
      this.city = city;
      this.street = street;
      this.postcode = postcode;
      this.longitude = longitude;
      this.latitude = latitude;
      
    }    
    
    /**
     * Konstruktor kopiujący obiekt
     * @param customer Kopiowany obiekt
     */
    public Customer(Customer customer) {
        
      this(customer.id, customer.name, customer.city, customer.street, 
              customer.postcode, customer.longitude, customer.latitude);  
        
    }
    
    
    /**
     * Konstruktor nowego odbiorcy towaru
     */
    public Customer() {
      
      this(0, "", "", "", "", 0.0, 0.0);
      
    }        
        
   
    
    @Override
    public String toString() {

       try {  
           
         if (id == 0) return "";  
       
         return name + (name.isEmpty() ? "" : ", ") + postcode + " " 
                + city + (city.isEmpty() ? "" : ", ") + street;
       }
          
       catch (NullPointerException e) {
           
         return "";
         
       }
       
    }

    
    @Override
    public Integer getId() {
        return id;
    }    
    
    public void setId(int id) {
       this.id = id;         
    }
   
    public String getName() {
        return name;
    }
    
    
    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getRoadNodeId() {
        return roadNodeId;
    }

    public void setRoadNodeId(int roadNodeId) {
        this.roadNodeId = roadNodeId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    
    

    /**
     * Weryfikacja wprowadzonych danych
     * @throws Exception Wyjatek: komunikat bledu
     */
    @Override
    public void verify() throws Exception {
      
      if (name.isEmpty())  throw new Exception("Nie podano nazwy odbiorcy towaru.");
      if (city.isEmpty())  throw new Exception("Nie podano miasta odbiorcy towaru.");
      
    }        

    
    
}
