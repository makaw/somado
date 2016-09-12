/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;



/**
 *
 * Ogolny interfejs obiektow reprezentujacych podstawowe
 * edytowalne struktury danych
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IDataEditable extends IData {
    
    
    /**
     * Weryfikacja wprowadzonych danych
     * @throws Exception Wyjatek: komunikat bledu
     */
    void verify() throws Exception;
    
    
}

