/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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

