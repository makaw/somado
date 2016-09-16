/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.formfields;


/**
 *
 * Interfejs szablonow obiektow slownikowych pol (komponentow)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IFormField {
    
  /**
   * Metoda zwraca wybrany ze slownika obiekt
   * @return Wybrany ze slownika obiekt
   */
  public Object getSelectedElement();
  
  /**
   * Metoda ustawia dana wartosc
   * @param element Wartosc do ustawienia
   */
  void setSelectedElement(Object element);
  
    
}
