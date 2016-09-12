/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package datamodel.tablemodels;

import datamodel.IData;
import java.util.Iterator;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * Ogólny szablon obiektu modelu tabeli 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa obiektów prezentowanych przy pomocy tabeli
 * 
 */
@SuppressWarnings("serial")
public abstract class TableModel<E extends IData> extends AbstractTableModel implements ITableModel<E> {
       
  /** Tablica z pobranymi danymi */
  protected Object[][] data;
  /** Lista obiektów danego typu */
  protected List<E> items;
          
    
  /**
   * Metoda ma zwrocic element z danego wiersza w tabeli
   * @param index Indeks (wiersz) tabeli
   * @return Obiekt z wiersza tabeli
   */
  @Override
  public E getElement(int index) {
      
    return items.get(index);
      
  }
  
  
  /**
   * Metoda ma zwrocic indeks(nr wiersza) danego obiektu
   * @param element Szukany obiekt
   * @return Indeks (nr wiersza)
   */    
  @Override
  public int getIndex(E element) {
      
     E tmp = null;
     boolean stop = false;
      
     Iterator<E> iterator = items.iterator();
     while (!stop && iterator.hasNext()) {
       tmp = iterator.next();
       stop = (tmp.getId().equals(element.getId()));
     }       
    
    return stop ? items.indexOf(tmp) : -1;
      
  }        
      
  
  @Override
  public int getRowCount() {
      
    return data.length;
    
  }
  

  @Override
  public Object getValueAt(int row, int col) {
      
    try {  
      
      return data[row][col];
      
    }
    
    // po zmianie modelu lub jezeli BD zwraca null
    catch (ArrayIndexOutOfBoundsException e) {
        
       return ""; 
        
    }
    
  }

  
  
  @Override
  public Class<?> getColumnClass(int c) {
      
    try {  
      
      return getValueAt(0, c).getClass();
      
    }
    
    catch (NullPointerException e) {
        
      return (new String()).getClass();
        
    }
    
  }


  @Override
  public boolean isCellEditable(int row, int col) {
      
    return false;
    
  }
  
  
  @Override
  public boolean isHighlightedCell(int col, int row) {
      
    return false;  
      
  }
    
    
  @Override
  public void setValueAt(Object value, int row, int col) {

    data[row][col] = value;
    fireTableCellUpdated(row, col);

  } 
    
  
  
}    
    


