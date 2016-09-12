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
 * Lokalizacja (wynik geokodowania)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GeoAddress implements IData {
    
  /** Klucz ID w BD (dla cache) */  
  private int id;
  /** Nazwa do wyświetlenia */
  private String displayName;
  /** Długość geograficzna */
  private double longitude;
  /** Szerokość geograficzna */
  private double latitude;


  /**
   * Konstruktor 
   * @param rs Zestaw wyników zapytania do BD
   * @throws SQLException Błąd SQL
   */
  public GeoAddress(ResultSet rs) throws SQLException {
        
    this.id = rs.getInt("id");
    this.displayName = rs.getString("display_name");    
    this.longitude = rs.getDouble("longitude");
    this.latitude = rs.getDouble("latitude");
        
  }
  
  /**
   * Konstruktor
   * @param displayName Nazwa do wyświetlenia
   * @param longitude Długość geograficzna
   * @param latitude Szerokość geograficzna
   */
  public GeoAddress(String displayName, double longitude, double latitude) {
      
    this.displayName = displayName;    
    this.longitude = longitude;      
    this.latitude = latitude;
      
  }

  @Override
  public Integer getId() {
      return id;
  }

  public void setId(int id) {
      this.id = id;
  }

  public String getDisplayName() {
      return displayName;
  }

  public void setDisplayName(String displayName) {
      this.displayName = displayName;
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
  
  @Override
  public String toString() {
      
    return displayName;  
      
  }
  

    
}
