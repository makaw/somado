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
import java.awt.Color;

/**
 *
 * Interfejs szablonow obiektu modelu tabeli z danymi
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa opisywana przez model tabeli
 * 
 */
public interface ITableModel<E extends IData> {

    /**
     * Metoda zwraca indeks kolumny do domyslnego sortowania
     * @return indeks kolumny do domyslnego sortowania
     */
    int getDefaultSortOrder();
    
    /**
     * Metoda zwraca porzadek domyslnego sortowania
     * @return true jezeli sortowanie narastajaco
     */
    boolean getDefaultSortOrderAsc();    
    
    /**
     * Metoda ma zwrocic element z danego wiersza w tabeli
     * @param index Indeks (wiersz) tabeli
     * @return Obiekt z wiersza tabeli
     */
    E getElement(int index);
    
    /**
     * Metoda ma zwrocic indeks(nr wiersza) danego obiektu
     * @param element Szukany obiekt
     * @return Indeks (nr wiersza)
     */
    int getIndex(E element);
    
    /**
     * Metoda ma zwrocic tytul naglowka menu kontekstowego
     * @param rowIndex Indeks wybranego wiersza tabeli
     * @return Tytul naglowka menu kontekstowego
     */
    String getPopupMenuTitle(int rowIndex);
    
    /** 
     * Metoda ma odpowiedziec czy dana kolumna zawiera pole stanu
     * @param col Indeks kolumny
     * @return True jezeli zawiera pole stanu
     */
    boolean isStateColumn(int col);
    

    /**
     * Metoda ma odpowiedziec czy dana kolumna wymaga łamania wierszy tekstu
     * @param col Indeks kolumny
     * @return True jeżeli wymaga łamiana tekstu
     */
    boolean isWrapColumn(int col);    
    
    /** 
     * Metoda ma odpowiedziec czy dana komórka ma być wyróżniona (kolor czerwony)
     * @param col Indeks kolumny
     * @param row Indeks wiersza
     * @return True jezeli ma być wyróżnionia
     */
     boolean isHighlightedCell(int col, int row);    
     
    
    /**
     * Metoda ma zwrocic kolor tekstu w polu stanu w danym wierszu
     * @param row Indeks wiersza
     * @return Kolor tekstu w polu stanu
     */
    Color getStateCellColor(int row);
        
    
    /**
     * Metoda ma zwrocic maksymalna dostepna ilosc elementow
     * @return maks. ilosc elementow modelu
     */
    int getAllElementsCount();

    
}
