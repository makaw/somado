/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;

/**
 *
 * Ogolny interfejs szablonu obiektu okna dialogowego do modyfikacji danych z tabel
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */

public interface IDialogForm {

    /** 
     * Metoda zwraca indeks BD ostatnio dodanego elementu
     * @return Indeks BD ostatnio dodanego elementu
     */
    int getAddedIndex();

    /**
     * Metoda okresla czy konieczne jest odswiezenie tabeli-rodzica
     * @return True jezeli konieczne jest odswiezenie
     */
    boolean isNeedRefresh();
    
}
