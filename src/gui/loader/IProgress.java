/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.loader;


/**
 *
 * Interfejs operacji wykonywanych z paskiem postępu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IProgress {
    
  /**
   * Ustawienie wartosci procentowej dla paska postepu
   * @param value Wartosc procentowa paska postepu
   * @return False jeżeli przerwano wątek
   */
   boolean setProgress(int value);  
   
   /** 
    * Pobranie bieżącej wartości procentowej
    * @return Bieżąca wartość procentowa
    */
   int getProgress();
   
  /**
   * Zamknięcie okienka
   */
   void hideComponent();
    
}
