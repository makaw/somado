/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
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
