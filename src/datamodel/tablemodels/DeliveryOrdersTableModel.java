/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import datamodel.DeliveryDriverOrder;
import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import somado.Settings;

/**
 *
 * Szablon obiektu modelu tabeli z listą punktów wyznaczonej trasy dla zatwierdzonej dostawy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DeliveryOrdersTableModel extends TableModel<DeliveryDriverOrder> {

  /** Nazwy (nagłówki) kolumn tabeli  */
  private String[] columnNames = { "\u0023", "Odbiorca", "Nr zam.", "Waga [kg]", "\u0141adunek [t]", 
      "Czas", "\u0141\u0105czny czas", "km", "\u03a3 km"};  
  
  /** Liczba wszystkich elementów */
  final private int allElementsNum;               
  /** Łączny czas przejazdu przez wszystkie punkty */
  private double totalTime = 0.0;  
  /** Łączny dystans przejazdu przez wszystkie punkty */
  private double totalDistance = 0.0;
  /** Łączna waga dostarczanych przesyłek */
  private double totalWeight = 0.0;  
  /** Indeks kolumny stanu (kolor na nr zamówienia) */
  private final static int STATE_COL_INDEX = 2;
  
  
  /**
   * Konstruktor modelu dla planu dostawy (z gotowej listy)
   * @param items Lista zamówień
   */  
  public DeliveryOrdersTableModel(List<DeliveryDriverOrder> items) {
                 
    this.items = items;
    allElementsNum = items.size();
    data = new Object[allElementsNum][columnNames.length]; 
    
    int i = 0;
    
    Iterator<DeliveryDriverOrder> iterator = items.iterator();
    while (iterator.hasNext()) {
              
       DeliveryDriverOrder point = new DeliveryDriverOrder(iterator.next());              
       
       data[i][0] = point.getCustomerLabel();
       data[i][1] = point.getCustomerDesc();
       data[i][2] = point.getOrderNumber();
       double weight = point.getOrderWeight();
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
      
    return (col == STATE_COL_INDEX);
    
  }
  
  
  /**
   * Metoda ma zwrocic kolor tekstu w polu stanu w danym wierszu
   * @param row Indeks wiersza
   * @return Kolor tekstu w polu stanu
   */    
  @Override
  public Color getStateCellColor(int row) {
      
     return  items.get(row).isDone() ? new Color(0x336600) : Color.RED;
      
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
    


