/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import datamodel.Driver;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import somado.Database;
import somado.UserRole;

/**
 *
 * Szablon obiektu modelu tabeli z listą dostępnych kierowców dla otwarcia nowej dostawy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DriversDeliveryTableModel extends TableModel<Driver> {
       
  /** Nazwy (nagłówki) kolumn tabeli */
  private String[] columnNames = { "", "Nazwisko i imię", "Numer rej.", "Pojazd", "\u0141adowno\u015b\u0107 [t]",
                                    "Powr\u00f3t" };
  
  /** Liczba wszystkich elementów */
  final private int allElementsNum;                 
  
  
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   */  
  public DriversDeliveryTableModel(Database database) {
           
    items = new ArrayList<>();
    int num = 0;    
        
        
    String query = "SELECT COUNT(DISTINCT(d.id)) FROM sys_users AS u "
          + "INNER JOIN dat_drivers AS d ON d.user_id=u.id "
          + "LEFT OUTER JOIN dat_vehicles AS v ON v.id=d.vehicle_id "
          + "WHERE u.role = ? AND v.capable='1' AND d.available='1'";
    
    try {

       PreparedStatement ps = database.prepareQuery(query);
       
       ps.setInt(1, UserRole.DRIVER.getId());
       
       ResultSet rs = ps.executeQuery();
       
       if (rs.first()) num = rs.getInt(1);
       
    
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }  
    
    allElementsNum = num;    
    
    data = new Object[num][columnNames.length];
    
    query = "SELECT u.id AS user_id, u.login AS user_login, u.firstname AS user_firstname, u.surname AS user_surname, "
          + "u.role AS user_role, u.blocked AS user_blocked, d.id, d.comment, d.available, "
          + "vm.id AS vehicle_model_id, vm.name AS vehicle_model_name, vm.maximum_load AS vehicle_model_maximum_load, "
          + "vm.avg_fuel_consumption AS vehicle_model_avg_fuel_consumption, "
          + "v.id AS vehicle_id, v.year AS vehicle_year, v.registration_no AS vehicle_registration_no, "
          + "v.comment AS vehicle_comment, v.capable AS vehicle_capable FROM sys_users AS u "
          + "INNER JOIN dat_drivers AS d ON d.user_id=u.id "
          + "LEFT OUTER JOIN dat_vehicles AS v ON v.id=d.vehicle_id "
          + "LEFT OUTER JOIN glo_vehicle_models AS vm ON vm.id=v.vehicle_model_id "
          + "WHERE u.role = ? AND v.capable='1' AND d.available='1';";
    
    try {
                
      PreparedStatement ps = database.prepareQuery(query);
      
      ps.setInt(1, UserRole.DRIVER.getId());
              
      ResultSet rs = ps.executeQuery();
            
      
      int i = 0;
      
      while(rs.next()) {          
                     
         data[i][0] = true;
         data[i][1] = rs.getString("user_surname") + " " + rs.getString("user_firstname");
         data[i][2] = rs.getString("vehicle_registration_no");
         data[i][3] = rs.getString("vehicle_model_name");  
         data[i][4] = rs.getDouble("vehicle_model_maximum_load");
         data[i][5] = true;        
         
         items.add(i, new Driver(rs));
         
         i++;         
           
       }
           
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }     

    
  }  
  
  
  /**
   * Zwraca listę zaznaczonych kierowców
   * @return Lista utworzonych przesyłek
   */
  public List<Driver> getSelectedItems() {
      
    ArrayList<Driver> drivers = new ArrayList<>();
    for (int i=0; i<items.size(); i++) 
      if ((boolean)data[i][0]) {          
         Driver tmp = items.get(i);
         tmp.setReturnToDepot((boolean)data[i][5]);
         drivers.add(tmp);
      }                            
    
    return drivers;
    
  }
       
  
  /**
   * Metoda zwraca domyslne sortowanie ASC
   * @return indeks kolumny do domyslnego sortowania
   */
  @Override
  public int getDefaultSortOrder() {
      
     return  0;
      
  }

  /**
   * Metoda zwraca porzadek domyslnego sortowania
   * @return true jezeli sortowanie narastajaco
   */
  @Override
  public boolean getDefaultSortOrderAsc() {
      
    return false;  
      
  }
  
  


  @Override
  public String getPopupMenuTitle(int rowIndex) {
      
     return "";
      
  }
    
  
  
  

  @Override
  public int getColumnCount() {
      
    return columnNames.length;
    
  }  
  

  @Override
  public String getColumnName(int col) {
      
    return columnNames[col];
    
  }  

    
  @Override
  public void setValueAt(Object value, int row, int col) {

    data[row][col] = value;
    fireTableCellUpdated(row, col);

  }    
  
  
 /** 
   * Metoda ma odpowiedziec czy dana kolumna zawiera pole stanu
   * @param col Indeks kolumny
   * @return True jezeli zawiera pole stanu
   */    
  @Override
  public boolean isStateColumn(int col) {
      
    return false;
    
  }
  
  
  /**
   * Metoda ma zwrocic kolor tekstu w polu stanu w danym wierszu
   * @param row Indeks wiersza
   * @return Kolor tekstu w polu stanu
   */    
  @Override
  public Color getStateCellColor(int row) {
      
     return null;
      
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
  public boolean isCellEditable(int row, int col) {
  
    // w pierwszej kolumnie checkbox do zaznaczenia zamówienia, w ostatniej do zaznaczenia powrotu
    return col == 0 || col ==5;
    
  }
  
  
  @Override
  public boolean isWrapColumn(int col) {
      
    return false;  
      
  }
  
  
}    
    


