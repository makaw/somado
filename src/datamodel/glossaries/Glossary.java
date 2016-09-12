/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.glossaries;

import datamodel.IData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import somado.Database;
import somado.Settings;


/**
 *
 * Ogolny szablon obiektow słowników
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów słównika, implementująca interfejs IData
 * 
 * 
 */
public abstract class Glossary<E extends IData> implements IGlossary<E> {

  /** Referencja do obiektu bazy danych */
  protected final Database database;
  /** Limit pobieranych danych */
  protected final int dbLimit;
  /** Mapa firm - pozycji w słowniku */
  protected final List<E> items;
  
  
  
  /**
   * Konstruktor
   * @param database Ref. do bazy danych
   */
  public Glossary(Database database) {
      
    this.database = database;    
    this.dbLimit = Settings.getIntValue("items_per_page_gloss");  
    items = new ArrayList<>();
           
  }    
  
  /**
   * Pusty konstruktor (dla metod nie wymagających połączenia z BD)
   */
  public Glossary() {
      
    this(null);  
      
  }
  
  
  
  /**
   * Metoda zwraca obiekt firmy dla podanego indeksu slownika
   * @param itemId indeks slownika
   * @return Obiekt firmy
   */
  @Override
  public E getItem(int itemId) {

     try {        
       return items.get(itemId);        
     }     
     catch (IndexOutOfBoundsException e) {         
       return null;           
     }
      
  }  
  
  
    
  /**
   * Metoda zwraca indeks slownikowy podanego obiektu
   * @param itemId atrybut ID (bazodanowy) szukanego obiektu
   * @return indeks slownikowy szukanego obiektu
   */  
  @Override
  public int getItemsIndex(int itemId) {
      
    E tmp = null;
    boolean stop = false;
      
    Iterator<E> objIterator = items.iterator();
    while (!stop && objIterator.hasNext()) {
       tmp = objIterator.next();
       stop = (tmp.getId().equals(itemId));
    }  
    
    return stop ? items.indexOf(tmp) : -1;
      
  }      

  
    
}
