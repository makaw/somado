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
import somado.User;


/**
 *
 * Interfejs szablonu obiektu reprezentujacego edytowalny slownik
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów słównika, implementująca interfejs IData
 * 
 */
public interface IGlossaryEditable<E extends IData> extends IGlossary<E> {

    /**
     * Metoda dodaje nowy obiekt do slownika
     * @param element Nowy element
     * @param user Ref. do obiektu zalogowanego uzytkownika
     * @return true jezeli OK
     */
    boolean addItem(E element, User user);

    
   /**
    * Metoda zwraca ostatni blad (logika lub BD)
    * @return Komunikat ostatniego bledu
    */    
    String getLastError();

    
    /**
     * Metoda modyfikuje pozycje w slowniku
     * @param element Dane po modyfikacji
     * @param user Ref. do obiektu zalogowanego uzytkownika
     * @return true jezeli OK
     */
    boolean updateItem(E element, User user);
    
    
    /**
     * Metoda usuwa element ze slownika
     * @param element Element do usuniecia
     * @param user Ref. do obiektu zalogowanego uzytkownika
     * @return true jezeli OK
     */
    boolean deleteItem(E element, User user);


    
}
