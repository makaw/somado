/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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
