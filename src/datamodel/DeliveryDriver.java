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
import java.util.List;

/**
 *
 * Szablon obiektu reprezentującego pojedynczą trasę pojedynczego kierowcy z dostawy (tylko odczyt z BD)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class DeliveryDriver implements IData {
    
   /** ID rekordu w BD */   
   private final Integer id;
   /** Login użytkownika systemowego - kierowcy obsługującego trasę */
   private final String userLogin;
   /** Nazwisko i imię kierowcy */
   private final String driverDesc;
   /** Nr rejestracyjny samochodu */
   private final String vehicleRegistrationNo;
   /** Opis samochodu (model, rocznik) */
   private final String vehicleDesc;
   /** Czy kierowca wraca do magazynu */
   private final boolean returnToDepot;
   /** Lista obsługiwanych na tej trasie zamówień */
   private List<DeliveryDriverOrder> orders;
   
   
  /**
   * Konstruktor (odczyt z BD)
   * @param rs Zestaw wyników zapytania do BD
   * @throws SQLException Błąd SQL
   */   
   public DeliveryDriver(ResultSet rs) throws SQLException {
       
      this.id = rs.getInt("id");
      this.userLogin = rs.getString("user_login");
      this.driverDesc = rs.getString("driver_desc");
      this.vehicleRegistrationNo = rs.getString("vehicle_registration_no");
      this.vehicleDesc = rs.getString("vehicle_desc");
      this.returnToDepot = rs.getBoolean("return_to_depot");
              
   }

   @Override
    public Integer getId() {
        return id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getDriverDesc() {
        return driverDesc;
    }

    public String getVehicleRegistrationNo() {
        return vehicleRegistrationNo;
    }
    
    

    public String getVehicleDesc() {
        return vehicleDesc;
    }

    public boolean isReturnToDepot() {
        return returnToDepot;
    }

    public List<DeliveryDriverOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DeliveryDriverOrder> orders) {
        this.orders = orders;
    }
   
   
   @Override
   public String toString() {
        
     return userLogin + ", " + driverDesc + ", " + vehicleDesc;
        
   }
    
    
}
