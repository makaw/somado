/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.docs;

import java.util.List;
import somado.User;


/**
 *
 * Interfejs szablonu obiektu reprezentującego rozszerzalny
 * i stronicowany dokument
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <K> Klasa wiersza dokumentu
 * @param <V> Klasa kompletnego obiektu przedstawianego przez wiersz dokumentu 
 * 
 */
public interface IEditableDoc<K extends IDocRow, V> {

    /**
     * Metoda dodaje nowy wiersz do dokumentu
     * @param element Nowy element
     * @param user ref. do obiektu zalogowanego uzytkownika
     * @return true jezeli OK
     */
    boolean addElement(V element, User user);

    /**
     * Metoda pobiera zawartosc dokumentu (kolejne linie)
     * @return Lista wierszy dokumentu
     */
    List<K> getDocumentText();

    /**
     * Metoda zwraca wskazany wiersz dokumentu
     * @param elementId indeks wiersza
     * @return Wskazany wiersz
     */
    V getElement(int elementId);

    /**
     * Metoda zwraca ostatni blad (logika lub BD)
     * @return Komunikat ostatniego bledu
     */
    String getLastError();
    
  
    /**
     * Metoda zwraca laczna ilosc wierszy
     * @return Laczna ilosc wierszy
     */
    int getRowsCount();
      
   
    /**
     * Metoda zwraca aktualny nr strony
     * @return Aktualny nr strony
     */
    int getStartPage();
 
    
    /**
     * Metoda zwraca limit wierszy na strone
     * @return Limit wierszy na strone
     */
    int getDbLimit();
      
   
   /**
    * Przejscie do nastepnej strony
    */
   void nextPage();
  
  
   /**
    * Przejscie do poprzedniej strony
    */
   void prevPage();
     
    
    
}
