/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import spatial_vrp.RoutePoint;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import somado.Settings;

/**
 *
 * Szablon obiektu modelu tabeli z listą punktów wyznaczonej trasy dla planu dostawy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class RoutePointsTableModel extends TableModel<RoutePoint> {

  /** Nazwy (nagłówki) kolumn tabeli  */
  private String[] columnNames = { "\u0023", "Odbiorca", "Nr zam.", "Waga [kg]", "\u0141adunek [t]", 
      "Czas", "\u0141\u0105czny czas", "km", "\u03a3 km"};  
  
  /** Liczba wszystkich elementów */
  final private int allElementsNum;               
  /** Łączny czas przejazdu przez wszystkie punkty */
  private double totalTime = 0.0;  
  /** Łączny dystans przejazdu przez wszystkie punkty */
  private double totalDistance = 0.0;
  /** Mapa porządkowych punktów odbioru */
  private final Map<Integer, String> labels;
    
  
  /**
   * Konstruktor modelu dla planu dostawy (z gotowej listy)
   * @param items Lista zamówień
   */  
  public RoutePointsTableModel(List<RoutePoint> items) {
                 
    this.items = items;
    allElementsNum = items.size();
    data = new Object[allElementsNum][columnNames.length]; 
    labels = new HashMap<>();
    
    int i = 0, pointNo = 0;
    double totalWeight = 0.0;
    
    Iterator<RoutePoint> iterator = items.iterator();
    while (iterator.hasNext()) {
              
       RoutePoint point = new RoutePoint(iterator.next());
       
       if (point.getDistance() > 0.0) pointNo++;
       String label = (point.getOrder().getCustomer().getId().equals(Settings.getDepot().getId()))
               ? "M" : String.valueOf(pointNo);
       if (!labels.containsKey(point.getOrder().getCustomer().getId())) 
         labels.put(point.getOrder().getCustomer().getId(), label);
       
       data[i][0] = label;
       data[i][1] = point.getOrder().getCustomer().toString();
       data[i][2] = point.getOrder().getNumber();
       double weight = point.getOrder().getTotalWeight();
       totalWeight += weight;
       data[i][3] = (weight > 0.0) ? String.format("%.2f", weight) : null;
       data[i][4] = -totalWeight;
       double time = point.getTime();
       totalTime += time;       
       data[i][5] = Settings.formatTime(time);
       data[i][6] = Settings.formatTime(totalTime);
       double distance = point.getDistance();
       totalDistance += distance;
       data[i][7] = String.format("%.1f", distance / 1000.0);
       data[i][8] = String.format("%.1f", totalDistance / 1000.0);
       
       i++;
                
     }   
    
     // uzupełnienie załadunku
     for (int j=0; j<i; j++) data[j][4] = String.format("%.3f", (totalWeight + (double)data[j][4])/1000.0);
    
  }  
  
  
  /**
   * Metoda zwraca treść etykiety (liczbę porządkową lub M) dla punktu odbioru
   * @param index Bazodanowe ID odbiorcy towaru
   * @return Treść etykiety
   */
  public String getLabel(int index) {
      
    try {  
      return labels.get(index);
    }
    catch (Exception e) {
      return "";  
    }
      
  }

  
  public double getTotalTime() {
      return totalTime;
  }


  public double getTotalDistance() {
      return totalDistance;
  }
  
      
  
  
  
  /**
   * Metoda zwraca domyslne sortowanie ASC
   * @return indeks kolumny do domyslnego sortowania
   */
  @Override
  public int getDefaultSortOrder() {
      
     return 0;
      
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
  public boolean isWrapColumn(int col) {
      
    return false;  
      
  }
  
  
}    
    


