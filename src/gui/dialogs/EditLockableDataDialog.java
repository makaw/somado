/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import datamodel.IData;
import datamodel.Lock;
import datamodel.glossaries.GlossLocks;
import gui.GUI;
import gui.SimpleDialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import somado.Settings;


/**
 *
 * Ogolny szablon obiektu okienka edycji obiektu z możliwością zablokowania
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class EditLockableDataDialog extends SimpleDialog {
     
  /** Blokada innego uzytkownika */  
  private Lock lock;
  /** Blokada zalogowanego uzytkownika */
  private Lock userLock;
  /** True jezeli otwierany do edycji obiekt jest zablokowany przez innego usera */
  protected boolean locked;
  
  /**
   * Konstruktor
   * @param frame Ref. do GUI
   * @param title Nagłówek okna
   */
  public EditLockableDataDialog(GUI frame, String title) {
      
    super(frame, title);
    locked = false;
    
  }
  
  
  @Override
  protected void showDialog(int width, int height) {
          
    if (lock != null) {
        
      String error = "Ten obiekt jest obecnie modyfikowany\n"
              + (lock.getUserLogin().equals(frame.getUser().getLogin()) ? 
                "w innym otwartym oknie aplikacji\n"
              : "przez innego u\u017cytkownika:\n" + lock.getUserLogin() + " ")
              + "(start: " + Settings.DATETIME_NS_FORMAT.format(lock.getDateAdd()) + ").\n\n"
              + "Mo\u017cliwo\u015b\u0107 dokonywania zmian\n"
              + "zosta\u0142a tymczasowo zablokowana.";
      new WarningDialog(frame, error, 210); 
      locked = true;
      setTitle(getTitle() + " (zablokowane)");
        
    }
    
    super.showDialog(width, height);
    
  }
  
  /**
   * Wyświetlenie okna bez sprawdzania blokady
   * @param width Szerokość okna
   * @param height Wysokość okna
   */
  protected void showDialogNoCheck(int width, int height) {
      
    super.showDialog(width, height);  
      
  }
  
  /**
   * Ustawienie obiektow blokad
   * @param object Otwierany do edycji obiekt
   */
  protected final void checkLock(IData object) {    
      
    try {
      if (object.getId() == 0) return;  
    }
    catch (NullPointerException e) {  return; }
      
    final GlossLocks glossLocks = new GlossLocks(frame.getDatabaseShared(), object);
    lock = glossLocks.getDefaultItem();
    if (lock != null) return;// && !lock.getUserLogin().equals(User.getLogin())
    
    userLock = new Lock(object, frame.getUser());    
    if (glossLocks.addItem(userLock)) {
      
      // usuniecie blokady po zamknieciu okna
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  
      addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {              
          glossLocks.deleteItem(userLock);   
        }
      });
      
    }

  }  

  
  protected final Lock getLock() {
    return lock;
  }
  
  
    
    
}
