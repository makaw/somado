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
import java.util.Map;
import javax.swing.ListModel;

/**
 *
 * Ogólny interfejs szablonu słowników
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów słównika, implementująca interfejs IData
 * 
 */
public interface IGlossary<E extends IData> {

    /**
     * Metoda pobiera model dla komponentu JList
     * @return Model dla komponentu JList
     */
    ListModel<E> getListModel();
    
    /**
     * Metoda pobiera model dla komponentu JList
     * @param params Zestaw (mapa) parametrów - filtrów
     * @return Model dla komponentu JList
     */
    ListModel<E> getListModel(Map<String, String> params);
    
  
    /**
     * Metoda zwraca indeks slownikowy podanego obiektu
     * @param elementId atrybut ID (bazodanowy) szukanego obiektu
     * @return indeks slownikowy szukanego obiektu
     */    
    int getItemsIndex(int elementId);
    
    
    /**
     * Metoda zwraca obiekt firmy dla podanego indeksu slownika
     * @param elementId indeks slownika
     * @return Obiekt firmy
     */    
    E getItem(int elementId);
   
    
    
    /**
     * Metoda ma zwracac domyslny (pierwszy na liscie) element slownika
     * @return Domyslny element slownika
     */
    E getDefaultItem();   
    
    
}
