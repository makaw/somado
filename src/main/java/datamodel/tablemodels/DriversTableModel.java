/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package datamodel.tablemodels;

import datamodel.Driver;
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
 * Szablon obiektu modelu tabeli z listą kierowców
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DriversTableModel extends TableModel<Driver> {
       
  /** Nazwy (nagłówki) kolumn tabeli */
  private String[] columnNames = { "Nazwisko", "Imi\u0119", "Model pojazdu", "Nr rejestr.",
                                   "Uwagi", "Dost\u0119pny", "Pojazd dost." };
  /** Nazwy pól filtrów */
  private String[] filterNames = { "surname", "registration_no" };

  /** Indeks 1.kolumny stanu (dostępności) */
  final private static int STATE_COL_INDEX1 = 5;
  /** Indeks 2.kolumny stanu (dostępności pojazdu) */
  final private static int STATE_COL_INDEX2 = 6;  
  /** Liczba wszystkich elementów */
  final private int allElementsNum;
          
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   * @param params Mapa parametrow (filtry)
   */  
  public DriversTableModel(Database database, Map<String, String> params) {
      
    if (params == null) {
        
       params = new HashMap<>();
       for (String filterName : filterNames)  params.put(filterName, "");

    }  
    
    
    items = new ArrayList<>();
      
    int perPage = Integer.parseInt(Settings.getValue("items_per_page"));   
    int num = 0;
    
    
    String query = "SELECT COUNT(DISTINCT(d.id)) FROM dat_drivers AS d "
          + "LEFT OUTER JOIN dat_vehicles AS v ON v.id=d.vehicle_id "
          + "WHERE d.surname LIKE ? "
          + "AND (CASE WHEN v.id IS NOT NULL THEN v.registration_no LIKE ? ELSE 1=1 END) ";

    try {

       PreparedStatement ps = database.prepareQuery(query);
       
       ps.setString(1, "%"+params.get("surname")+"%");
       ps.setString(2, "%"+params.get("registration_no")+"%");
      
       ResultSet rs = ps.executeQuery();
       
       if (rs.next()) num = rs.getInt(1);
       rs.close();
       
    
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }  
    
    allElementsNum = num;
    
    if (num>perPage) num = perPage;
    
    data = new Object[num][columnNames.length];  
    
    query = "SELECT d.firstname, d.surname, d.id, d.comment, d.available, "
          + "vm.id AS vehicle_model_id, vm.name AS vehicle_model_name, vm.maximum_load AS vehicle_model_maximum_load, "
          + "vm.avg_fuel_consumption AS vehicle_model_avg_fuel_consumption, "
          + "v.id AS vehicle_id, v.year AS vehicle_year, v.registration_no AS vehicle_registration_no, "
          + "v.comment AS vehicle_comment, v.capable AS vehicle_capable FROM dat_drivers AS d "
          + "LEFT OUTER JOIN dat_vehicles AS v ON v.id=d.vehicle_id "
          + "LEFT OUTER JOIN glo_vehicle_models AS vm ON vm.id=v.vehicle_model_id "
          + "WHERE d.surname LIKE ? AND "
          + "(CASE WHEN v.id IS NOT NULL THEN v.registration_no LIKE ? ELSE 1=1 END) " 
          + "LIMIT ?;";
    
    try {
                
      PreparedStatement ps = database.prepareQuery(query);
            
      ps.setString(1, "%"+params.get("surname")+"%");
      ps.setString(2, "%"+params.get("registration_no")+"%");
      ps.setInt(3, perPage);
              
      ResultSet rs = ps.executeQuery();
      
      int i = 0;
      
      while(rs.next()) {
          
         data[i][0] = rs.getString("surname");
         data[i][1] = rs.getString("firstname");
         data[i][2] = rs.getString("vehicle_model_name") == null ? "-" : rs.getString("vehicle_model_name");         
         data[i][3] = rs.getString("vehicle_registration_no") == null ? "-" : rs.getString("vehicle_registration_no");
         data[i][4] = rs.getString("comment");
         data[i][5] = rs.getBoolean("available") ? "Tak" : "Nie";
         data[i][6] = rs.getBoolean("vehicle_capable") ? "Tak" : "Nie";
                 
         items.add(i, new Driver(rs));
         
         i++;         
           
       }
      
       rs.close();
           
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }     

    
  }  
  
  
  /**
   * Konstruktor modelu
   * @param database Ref. do obiektu bazy danych
   */
  public DriversTableModel(Database database) {
      
     this(database, null);  
 
  }
    
  
  
  /**
   * Metoda zwraca domyslne sortowanie 
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
      
     return (!((String)getValueAt(rowIndex,1)).isEmpty() ? getValueAt(rowIndex, 1) + " " : "")
            + (!((String)getValueAt(rowIndex,0)).isEmpty() ? getValueAt(rowIndex, 0) : "");
      
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
      
    return (col == STATE_COL_INDEX1 || col == STATE_COL_INDEX2);
      
  }
  
  
  /**
   * Metoda ma zwrocic kolor tekstu w polu stanu w danym wierszu
   * @param row Indeks wiersza
   * @return Kolor tekstu w polu stanu
   */    
  @Override
  public Color getStateCellColor(int row) {
      
     return items.get(row).isAvailable() && items.get(row).getVehicle().isCapable() ? new Color(0x336600) : Color.RED;
      
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
    


