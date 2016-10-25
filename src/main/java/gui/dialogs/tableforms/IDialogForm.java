/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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
