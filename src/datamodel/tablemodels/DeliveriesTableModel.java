/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import datamodel.Delivery;
import datamodel.DeliveryDriver;
import datamodel.DeliveryDriverOrder;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jxmapviewer.viewer.GeoPosition;
import somado.Database;
import somado.Settings;
import somado.UserRole;

/**
 *
 * Szablon obiektu modelu tabeli z listą dostaw
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DeliveriesTableModel extends TableModel<Delivery> {
       
  /** Nazwy (nagłówki) kolumn tabeli */
  private String[] columnNames = { "Data dostawy", "Kierowcy", "Zamówienia", "Dostarczono",
                                   "Waga[t]", "Paliwo[l]", "Czas", "Dystans [km]", "Stan" };
  /** Nazwy pól filtrów */
  private String[] filterNames = { "delivery_date", "state_id" };

  /** Indeks kolumny stanu  */
  final private static int STATE_COL_INDEX = 8;
  
  /** Liczba wszystkich elementów */
  final private int allElementsNum;
          
  /** Ref. do obiektu globalnej bazy danych */
  private final Database database;
  
  
  
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   * @param params Mapa parametrow (filtry)
   */  
  public DeliveriesTableModel(Database database, Map<String, String> params) {
      
    this.database = database;   
      
    if (params == null) {
        
       params = new HashMap<>();
       for (String filterName : filterNames)  params.put(filterName, "");

    }  
    
    
    items = new ArrayList<>();
      
    int perPage = Integer.parseInt(Settings.getValue("items_per_page"));   
    int num = 0;
    
     // filtr stanu dostaw
    boolean state = true;
    try {
      state = Integer.parseInt(params.get("state_id")) == 0;
    }
    catch (NumberFormatException e) {}
    
    
    String query = "SELECT COUNT(*) FROM dat_deliveries WHERE active = ? "
            + "AND DATE_FORMAT(delivery_date, '%Y-%m-%d')  LIKE ? ;";
    
    try {

       PreparedStatement ps = database.prepareQuery(query);
       ps.setBoolean(1, state);
       ps.setString(2, "%"+params.get("delivery_date")+"%");
       
       ResultSet rs = ps.executeQuery();
       
       if (rs.first()) num = rs.getInt(1);
       
    
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }  
    
    allElementsNum = num;
    
    if (num>perPage) num = perPage;
    
    data = new Object[num][columnNames.length];  
    
    query = "SELECT d.*, (SELECT COUNT(id) FROM dat_deliveries_drivers WHERE delivery_id = d.id) AS drivers_num "        
            + " FROM dat_deliveries AS d WHERE d.active = ? "
            + " AND DATE_FORMAT(d.delivery_date, '%Y-%m-%d') LIKE ? ORDER BY d.date_add DESC LIMIT ?;";
    
    try {
                
      PreparedStatement ps = database.prepareQuery(query);

      ps.setBoolean(1, state);      
      ps.setString(2, "%"+params.get("delivery_date")+"%");
      ps.setInt(3, perPage);
      
      ResultSet rs = ps.executeQuery();
      
      int i = 0;
      
      while(rs.next()) {
          
         ps = database.prepareQuery("SELECT SUM(o.order_weight) AS orders_weight, "
                 + "SUM(time) AS orders_time, SUM(distance) AS orders_distance "
                 + "FROM dat_deliveries_drivers_orders AS o INNER JOIN dat_deliveries_drivers AS dr "
                 + "ON dr.id = o.delivery_driver_id WHERE dr.delivery_id = ?");
         
         ps.setInt(1, rs.getInt("id"));
         
         ResultSet rs2 = ps.executeQuery();
         rs2.next(); 
         
         ps = database.prepareQuery("SELECT COUNT(o.id) AS orders_num "
                 + "FROM dat_deliveries_drivers_orders AS o INNER JOIN dat_deliveries_drivers AS dr "
                 + "ON dr.id = o.delivery_driver_id WHERE dr.delivery_id = ? AND o.customer_label<>'M' ");
         
         ps.setInt(1, rs.getInt("id"));
         
         ResultSet rs3 = ps.executeQuery();
         rs3.next();        
         
         ps = database.prepareQuery("SELECT COUNT(o.id) AS orders_done "
                 + "FROM dat_deliveries_drivers_orders AS o INNER JOIN dat_deliveries_drivers AS dr "
                 + "ON dr.id = o.delivery_driver_id WHERE dr.delivery_id = ? AND o.customer_label<>'M' AND o.done='1' ");
         
         ps.setInt(1, rs.getInt("id"));
         
         ResultSet rs4 = ps.executeQuery();
         rs4.next();               
         
         data[i][0] = rs.getString("delivery_date");         
         data[i][1] = rs.getInt("drivers_num");
         data[i][2] = rs3.getInt("orders_num");
         data[i][3] = rs4.getInt("orders_done");
         data[i][4] = String.format("%.3f", rs2.getDouble("orders_weight")/1000.0);
         data[i][5] = String.format("%.2f", rs.getDouble("total_cost"));
         data[i][6] = Settings.formatTime((int) rs2.getDouble("orders_time"));
         data[i][7] = String.format("%.2f", rs2.getDouble("orders_distance")/1000.0);
         data[i][8] = rs.getBoolean("active") ? "Aktywna" : "Zako\u0144czona";
                 
         items.add(i, new Delivery(rs));
         
         i++;         
           
       }
           
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }     

    
  }  
  
  
  /**
   * Uzupełnienie obiektu dostawy o listy tras/kierowców, zamówień i koordynaty geometrii
   * @param item Obiekt dostawy
   * @throws java.sql.SQLException Błąd SQL
   */
  public void itemComplete(Delivery item) throws SQLException {
      
    List<DeliveryDriver> drivers = new ArrayList<>();  
      
    PreparedStatement ps = database.prepareQuery("SELECT d.*, (SELECT login FROM sys_users WHERE id = d.driver_user_id "
            + "AND role = ?) AS user_login FROM dat_deliveries_drivers AS d WHERE d.delivery_id = ? ORDER BY id;");
    
    ps.setInt(1, UserRole.DRIVER.getId());
    ps.setInt(2, item.getId());
    
    ResultSet rs = ps.executeQuery();
    
    while (rs.next()) {
        
       DeliveryDriver driver = new DeliveryDriver(rs); 
       List<DeliveryDriverOrder> orders = new ArrayList<>();
       
       ps = database.prepareQuery("SELECT o.* FROM dat_deliveries_drivers_orders AS o WHERE o.delivery_driver_id = ? "
               + "ORDER BY o.id");
       ps.setInt(1, rs.getInt("id"));
       
       ResultSet rs2 = ps.executeQuery();
       
       while (rs2.next()) {
           
          DeliveryDriverOrder order = new DeliveryDriverOrder(rs2);
                    
          List<GeoPosition> geometryCoords = new ArrayList<>();
          List<GeoPosition> additionalGeometryCoords = new ArrayList<>();
          
          ps = database.prepareQuery("SELECT * FROM dat_deliveries_orders_geom WHERE delivery_order_id = ? ORDER BY id;");
          ps.setInt(1, rs2.getInt("id"));
          
          ResultSet rs3 = ps.executeQuery();
          while (rs3.next()) {
              
             GeoPosition coords = new GeoPosition(rs3.getDouble("latitude"), rs3.getDouble("longitude")); 
             if (rs3.getBoolean("additional")) additionalGeometryCoords.add(coords);
             else geometryCoords.add(coords);
              
          }
          
          order.setGeometryCoords(geometryCoords);
          order.setAdditionalGeometryCoords(additionalGeometryCoords);
          orders.add(order);
           
       }
       
       driver.setOrders(orders);
       drivers.add(driver);
       
        try {
          Thread.sleep(2);
        } catch (InterruptedException ex) {
          throw new SQLException("Przerwano wczytywanie dostawy."); 
        }
        
    }
    
    item.setDrivers(drivers);
      
  }
  
  
  
  
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   */
  public DeliveriesTableModel(Database database) {
      
     this(database, null);  
 
  }
    
  
  
  /**
   * Metoda zwraca domyslne sortowanie ASC
   * @return indeks kolumny do domyslnego sortowania
   */
  @Override
  public int getDefaultSortOrder() {
      
     return  1;
      
  }

  /**
   * Metoda zwraca porzadek domyslnego sortowania
   * @return true jezeli sortowanie narastajaco
   */
  @Override
  public boolean getDefaultSortOrderAsc() {
      
    return true;  
      
  }
  
  

  /**
   * Metoda zwraca tytul naglowka menu kontekstowego
   * @param rowIndex Indeks wybranego wiersza tabeli
   * @return Tytul naglowka menu kontekstowego
   */  
  @Override
  public String getPopupMenuTitle(int rowIndex) {
      
     return (String)getValueAt(rowIndex,0);
      
  }
        
  

  @Override
  public int getColumnCount() {
      
    return columnNames.length;
    
  }
  
  
  @Override
  public String getColumnName(int col) {
      
    return columnNames[col];
    
  }
  

 /** 
   * Metoda ma odpowiedziec czy dana kolumna zawiera pole stanu
   * @param col Indeks kolumny
   * @return True jezeli zawiera pole stanu
   */    
  @Override
  public boolean isStateColumn(int col) {
      
    return (col == STATE_COL_INDEX);
      
  }
  
  
  /**
   * Metoda ma zwrocic kolor tekstu w polu stanu w danym wierszu
   * @param row Indeks wiersza
   * @return Kolor tekstu w polu stanu
   */    
  @Override
  public Color getStateCellColor(int row) {
      
     return items.get(row).isActive() ? new Color(0x336600) : Color.DARK_GRAY;
      
  }  
  
  
  /**
   * Metoda ma zwrocic maksymalna dostepna ilosc elementow
   * @return maks. ilosc elementow modelu
   */
  @Override
  public int getAllElementsCount() {
      
     return allElementsNum; 
      
  }
    
  
  
  @Override
  public boolean isWrapColumn(int col) {
      
    return false; 
      
  }
  
  
}    
    


