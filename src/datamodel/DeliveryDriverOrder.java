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
import org.jxmapviewer.viewer.GeoPosition;


/**
 *
 * Szablon obiektu reprezentującego pojedyncze zamówienie z dostawy (tylko odczyt z BD)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class DeliveryDriverOrder implements IData {
    
   /** ID rekordu w BD */    
   private final Integer id;
   /** Opis odbiorcy towaru (nazwa, adres) */
   private final String customerDesc;
   /** Długość geograficzna punktu odbioru towaru */
   private final double customerLongitude;
   /** Szerokość geograficzna punktu odbioru towaru */
   private final double customerLatitude;
   /** Etykieta na mapie punktu odbioru towaru */
   private final String customerLabel;
   /** Nr zamówienia */
   private final String orderNumber;
   /** Łączna waga produktów w zamówieniu */
   private final double orderWeight;
   /** Czy zamówienie dostarczono w ramach tej dostawy */
   private final boolean done;
   /** Czas przejazdu odcinka */
   private final double time;
   /** Długość odcinka */
   private final double distance;
   /** Lista koordynat dla geometrii odcinka */
   private List<GeoPosition> geometryCoords;
   /** Lista koordynat dla dodatkowej geometrii odcinka */
   private List<GeoPosition> additionalGeometryCoords;
    
   
  /**
   * Konstruktor (odczyt z BD)
   * @param rs Zestaw wyników zapytania do BD
   * @throws SQLException Błąd SQL
   */      
   public DeliveryDriverOrder(ResultSet rs) throws SQLException {
       
     this.id = rs.getInt("id");  
     this.customerDesc = rs.getString("customer_desc");
     this.customerLongitude = rs.getDouble("customer_longitude");
     this.customerLatitude = rs.getDouble("customer_latitude");
     this.customerLabel = rs.getString("customer_label");
     this.orderNumber = rs.getString("order_number");
     this.orderWeight = rs.getDouble("order_weight");
     this.done = rs.getBoolean("done");
     this.time = rs.getDouble("time");
     this.distance = rs.getDouble("distance");
     
       
   }
   
   
   /**
    * Konstruktor do kopiowania obiektu
    * @param order Kopiowany obiekt
    */
   public DeliveryDriverOrder(DeliveryDriverOrder order) {
       
     this.id = order.id;
     this.customerDesc = order.customerDesc;
     this.customerLongitude = order.customerLongitude;
     this.customerLatitude = order.customerLatitude;
     this.customerLabel = order.customerLabel;
     this.orderNumber = order.orderNumber;
     this.orderWeight = order.orderWeight;
     this.done = order.done;
     this.time = order.time;
     this.distance = order.distance;
     this.geometryCoords = order.geometryCoords;
     this.additionalGeometryCoords = order.additionalGeometryCoords;              
       
   }
   

   @Override
    public Integer getId() {
        return id;
    }

    public String getCustomerDesc() {
        return customerDesc;
    }

    public double getCustomerLongitude() {
        return customerLongitude;
    }

    public double getCustomerLatitude() {
        return customerLatitude;
    }

    public String getCustomerLabel() {
        return customerLabel;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public double getOrderWeight() {
        return orderWeight;
    }

    public boolean isDone() {
        return done;
    }
    
    

    public double getTime() {
        return time;
    }

    public double getDistance() {
        return distance;
    }

    public List<GeoPosition> getGeometryCoords() {
        return geometryCoords;
    }

    public List<GeoPosition> getAdditionalGeometryCoords() {
        return additionalGeometryCoords;
    }

    public void setGeometryCoords(List<GeoPosition> geometryCoords) {
        this.geometryCoords = geometryCoords;
    }

    public void setAdditionalGeometryCoords(List<GeoPosition> additionalGeometryCoords) {
        this.additionalGeometryCoords = additionalGeometryCoords;
    }
   
   
   
   
   
    
}
