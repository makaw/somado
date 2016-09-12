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
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import somado.Settings;

/**
 *
 * Szablon obiektu reprezentującego dostawę towaru (tylko odczyt z BD)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Delivery implements IData {

    
  /** ID rekordu w BD */  
  private final Integer id;
  /** Data dostawy */
  private Date deliveryDate;
  /** Maksymalny czas jazdy kierowcy w ramach dostawy */
  private final double driverMaxWorkTime;
  /** Algorytm użyty do wyznaczenia najkrótszych ścieżek */
  private final ShortestPathAlgorithm shortestPathAlgorithm;
  /** Czy użyto dodatkowej geometrii */
  private final boolean additionalGeometry;
  /** Nazwa i adres magazynu */
  private final String depotDesc;
  /** Długość geograficzna magazynu */
  private final double depotLongitude;
  /** Szerokość geograficzna magazynu */
  private final double depotLatitude;
  /** Łączny koszt dostawy tj. ilość zużytego paliwa */
  private final double totalCost;
  /** Stan dostawy: true jeżeli aktywna, false jeżeli zakończona */
  private final boolean active;
  /** Data zatwierdzenia dostawy */
  private Date dateAdd;
  /** Data zakończenia dostawy */
  private Date dateEnd;
  /** Lista tras/kierowców w ramach dostawy */
  private List<DeliveryDriver> drivers;
  
  /**
   * Konstruktor (odczyt z BD)
   * @param rs Zestaw wyników zapytania do BD
   * @throws SQLException Błąd SQL
   */
  public Delivery(ResultSet rs) throws SQLException {
      
     this.id = rs.getInt("id");  
     try {
       this.deliveryDate = Settings.DATE_FORMAT.parse(rs.getString("delivery_date"));
     } catch (ParseException e) {
       this.deliveryDate = null;
       System.err.println(e.getMessage());
     }        
     this.driverMaxWorkTime = rs.getDouble("driver_max_work_time");
     this.shortestPathAlgorithm = ShortestPathAlgorithm.get(rs.getString("shortest_path_algorithm"));
     this.additionalGeometry = rs.getBoolean("additional_geometry");
     this.depotDesc = rs.getString("depot_desc");
     this.depotLongitude = rs.getDouble("depot_longitude");
     this.depotLatitude = rs.getDouble("depot_latitude");
     this.totalCost = rs.getDouble("total_cost");
     this.active = rs.getBoolean("active");
     try {
       this.dateAdd = Settings.DATETIME_FORMAT.parse(rs.getString("date_add"));
     } catch (ParseException e) {
       this.dateAdd = null;
       System.err.println(e.getMessage());
     }           
     if (!this.active) try {
       this.dateEnd = Settings.DATETIME_FORMAT.parse(rs.getString("date_end"));
     } catch (NullPointerException | ParseException e) {
       this.dateEnd = null;
       System.err.println(e.getMessage());
     }   
      
  }

  @Override
    public Integer getId() {
        return id;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public double getDriverMaxWorkTime() {
        return driverMaxWorkTime;
    }

    public ShortestPathAlgorithm getShortestPathAlgorithm() {
        return shortestPathAlgorithm;
    }

    public boolean isAdditionalGeometry() {
        return additionalGeometry;
    }

    public String getDepotDesc() {
        return depotDesc;
    }

    public double getDepotLongitude() {
        return depotLongitude;
    }

    public double getDepotLatitude() {
        return depotLatitude;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public boolean isActive() {
        return active;
    }

    public Date getDateAdd() {
        return dateAdd;
    }

    public Date getDateEnd() {
        return dateEnd;
    }
    
    

    public List<DeliveryDriver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DeliveryDriver> drivers) {
        this.drivers = drivers;
    }
    

    
}
