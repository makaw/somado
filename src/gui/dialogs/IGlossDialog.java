/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import datamodel.IData;
import datamodel.glossaries.IGlossary;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;


/**
 *
 * Interfejs szablonu okienka dialogowego ze słownikiem
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów głównego komponentu listy słownika
 * 
 */
public interface IGlossDialog<E extends IData> {

    /**
     * Metoda zwracajaca aktualne filtry
     * @return Obiekt filtrow
     */
    GlossDialogFilters<E> getFilters();

    
    /**
     * Metoda zwraca referencje do obiektu odpowiedniego slownika
     * @return Ref. do obiektu slownika
     */
    IGlossary<E> getGlossary();
    

    /**
     * Metoda zwracajaca liste elementow slownika (jako komponent)
     * @return Komponent listy elementow slownika
     */
    JList<E> getObjList();

    /**
     * Metoda zwracajaca wybrany element
     * @return Wybrany element
     */
    E getSelectedElement();  
    
    /**
     * Metoda udostępnia indywidualny model listy
     * @return  Model listy danego słownika
     */
    DefaultListModel<E> getListModel();
    
    /**
     * Metoda udostępnia indywidualny model listy
     * @param filters Ustawione filtry (mapa)
     * @return  Model listy danego słownika
     */    
    DefaultListModel<E> getListModel(Map<String, String> filters);
    
}
