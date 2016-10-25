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
 * Interfejs klas wywołujących operacje wykonywane z paskiem postępu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IProgressInvoker {
    
  /** Rozpoczęcie wykonywania operacji
   * @param progress Interfejs paska postępu
   */  
  void start(IProgress progress);
  
  
}
