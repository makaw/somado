/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import datamodel.Vehicle;
import datamodel.VehicleModel;
import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import somado.Database;
import somado.Settings;

/**
 *
 * Szablon obiektu modelu tabeli z listą pojazdów
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class VehiclesTableModel extends TableModel<Vehicle> {
       
  /** Nazwy (nagłówki) kolumn tabeli */
  private String[] columnNames = { "Model", "\u0141adowno\u015b\u0107 [t]", "Zu\u017cycie paliwa [l/100km]]", 
                                   "Rok prod.", "Nr rejestr.", "Uwagi", "Dost\u0119pny" };
  /** Nazwy pól filtrów */
  private String[] filterNames = { "name", "registration_no" };
  
  /** Indeks kolumny stanu (dostępności) */
  final private static int STATE_COL_INDEX = 6;
  /** Liczba wszystkich elementów */
  final private int allElementsNum;
          
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   * @param params Mapa parametrow (filtry)
   */  
  public VehiclesTableModel(Database database, Map<String, String> params) {
      
    if (params == null) {
        
       params = new HashMap<>();
       for (String filterName : filterNames)  params.put(filterName, "");

    }  
    
    
    items = new ArrayList<>();
      
    int perPage = Integer.parseInt(Settings.getValue("items_per_page"));   
    int num = 0;    
    
    String query = "SELECT COUNT(DISTINCT(v.id)) FROM dat_vehicles AS v "
            + " INNER JOIN glo_vehicle_models AS vm ON vm.id = v.vehicle_model_id "
            + " WHERE vm.name LIKE ? AND v.registration_no LIKE ? ";
    
    try {

       PreparedStatement ps = database.prepareQuery(query);
              
       ps.setString(1, "%"+params.get("name")+"%");
       ps.setString(2, "%"+params.get("registration_no")+"%");
       
       ResultSet rs = ps.executeQuery();
       
       if (rs.first()) num = rs.getInt(1);
    
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }  
    
    allElementsNum = num;
    
    if (num>perPage) num = perPage;
    
    data = new Object[num][columnNames.length];  
    
    query = "SELECT v.*, "
          + "vm.id AS vm_id, vm.name AS vm_name, vm.maximum_load AS vm_maximum_load, "
          + "vm.avg_fuel_consumption AS vm_avg_fuel_consumption "
          + "FROM dat_vehicles AS v "
          + "INNER JOIN glo_vehicle_models AS vm ON vm.id=v.vehicle_model_id "
          + " WHERE vm.name LIKE ? AND v.registration_no LIKE ? "         
          + " LIMIT ?;";

    try {
               
      PreparedStatement ps = database.prepareQuery(query);
      
      ps.setString(1, "%"+params.get("name")+"%");
      ps.setString(2, "%"+params.get("registration_no")+"%");
      ps.setInt(3, perPage);
              
      ResultSet rs = ps.executeQuery();
      
      int i = 0;
      
      while(rs.next()) {
          
         data[i][0] = rs.getString("vm_name");
         data[i][1] = String.format("%.2f", rs.getDouble("vm_maximum_load"));
         data[i][2] = String.format("%.2f", rs.getDouble("vm_avg_fuel_consumption"));
         data[i][3] = rs.getInt("year");       
         data[i][4] = rs.getString("registration_no");
         data[i][5] = rs.getString("comment");
         data[i][6] = rs.getBoolean("capable") ? "Tak" : "Nie";
                  
         items.add(new Vehicle(rs, new VehicleModel(rs, "vm_")));
         
         i++;         
           
       }
           
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }     

    
  }  
  
  
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   */
  public VehiclesTableModel(Database database) {
      
     this(database, null);  
 
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
      
    return true;  
      
  }
  
  

  /**
   * Metoda zwraca tytul naglowka menu kontekstowego
   * @param rowIndex Indeks wybranego wiersza tabeli
   * @return Tytul naglowka menu kontekstowego
   */  
  @Override
  public String getPopupMenuTitle(int rowIndex) {
      
     return getValueAt(rowIndex,3).toString();
      
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
      
     return items.get(row).isCapable() ? new Color(0x336600) : Color.RED;
      
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
    


