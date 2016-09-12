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
import somado.IConf;


/**
 *
 * Szablon obiektu reprezentującego model pojazdu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class VehicleModel implements IDataEditable {
    
    
   /** Klucz w tabeli BD */ 
   private Integer id;
   /** Nazwa modelu pojazdu */
   private String name;
   /** Ładowność [t] */
   private double maximumLoad;
   /** Średnie zużycie paliwa [l/100km] */
   private double avgFuelConsumption;
   
   
   /**
    * Konstruktor
    * @param id Id rekordu w bazie danych
    * @param name Nazwa 
    * @param maximumLoad Ładowność [t]
    * @param avgFuelConsumption Średnie zużycie paliwa [l/100km]
    */
   public VehicleModel(int id, String name, double maximumLoad, double avgFuelConsumption) {
       
     this.id = id;
     this.name = name;
     this.maximumLoad = maximumLoad;
     this.avgFuelConsumption = avgFuelConsumption;
       
   }
   
   /**
    * Konstruktor obiektu modelu pojazdu (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @param prefix Prefix pól dot. modelu pojazdu w zapytaniu
    * @throws SQLException Błąd SQL
    */   
   public VehicleModel(ResultSet rs, String prefix) throws SQLException {
       
     this.id = rs.getInt(prefix + "id");
     this.name = rs.getString(prefix + "name");
     this.maximumLoad = rs.getDouble(prefix + "maximum_load");
     this.avgFuelConsumption = rs.getDouble(prefix + "avg_fuel_consumption");
       
   }
   
   
   /**
    * Konstruktor obiektu produktu (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @throws SQLException Błąd SQL
    */   
   public VehicleModel(ResultSet rs) throws SQLException {  
       
     this(rs, "");  
       
   } 
   
    /**
     * Konstruktor pustego obiektu (nowego)
     */
    public VehicleModel() {
        
      this(0, "", IConf.MIN_VEHICLE_MAXLOAD, IConf.MIN_VEHICLE_FUEL_CONSUMPTION);  
        
    }   
   

   @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaximumLoad() {
        return maximumLoad;
    }

    public void setMaximumLoad(double maximumLoad) {
        this.maximumLoad = maximumLoad;
    }

    public double getAvgFuelConsumption() {
        return avgFuelConsumption;
    }

    public void setAvgFuelConsumption(double avgFuelConsumption) {
        this.avgFuelConsumption = avgFuelConsumption;
    }

    
    
     
    
   
   @Override
   public String toString() {
       
     return name;
       
   }

   
   @Override
   public void verify() throws Exception {
    
      if (name.isEmpty())  throw new Exception("Nie podano nazwy modelu pojazdu.");       
      if (maximumLoad<IConf.MIN_VEHICLE_MAXLOAD || maximumLoad>IConf.MAX_VEHICLE_MAXLOAD) 
          throw new Exception("Nieprawid\u0142owa \u0142adowno\u015b\u0107 pojazdu.");
       
   }
    
    
}

