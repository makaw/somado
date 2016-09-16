/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package datamodel.docs;


/**
 *
 * Interfejs pojedynczego wiersza dokumentu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IDocRow {

    /** 
     * Metoda zwraca treść wiersza 
     * @return Treść wiersza
     */
    String getText();

    /** 
     * Metoda zwraca treść nagłówka wiersza
     * @return Treść nagłówka wiersza
     */
    String getHeader();
    
}
