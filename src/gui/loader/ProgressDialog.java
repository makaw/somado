/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.loader;


import gui.GUI;
import gui.ImageRes;
import gui.SimpleDialog;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


/**
 *
 * Okienko z paskiem postępu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class ProgressDialog extends SimpleDialog implements IProgress {
    
    
  /** Tekst nad paskiem postepu */
  private final String progressBarText;  
  /** Obiekt paska postepu */
  protected final  JProgressBar progressBar;
  /** True jeżeli postęp nie jest mierzalny procentowo (nie pokazywać %) */
  private final boolean indeterminate;
  /** Przycisk "anuluj" */
  private final JButton cancelButton;
  
  
  /**
   * Konstruktor dla klas dziedziczących
   * @param frame Ref. do GUI
   * @param progressBarText Tekst nad paskiem postepu
   * @param indeterminate True jeżeli postęp nie jest mierzalny procentowo (nie pokazywać %) 
   */
  public ProgressDialog(GUI frame, String progressBarText, boolean indeterminate) {
            
    super(frame, "Prosz\u0119 czeka\u0107 ...");
    this.progressBarText = progressBarText;     
    this.indeterminate = indeterminate;
    progressBar = new JProgressBar();
    progressBar.setIndeterminate(indeterminate);
    progressBar.setStringPainted(!indeterminate);
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
  
    cancelButton = new CloseButton("Anuluj");
    cancelButton.setIcon(ImageRes.getIcon("icons/clear.png"));
    
    
  }        
  

  @Override
  protected void getContent() {
         
    JPanel p2 = new JPanel();
    p2.setPreferredSize(new Dimension(440, 110));
    p2.setOpaque(false);
    
    JPanel p = new JPanel(new GridLayout(1, 1));  
    p.setOpaque(false);    
    progressBar.setOpaque(false);    
    Border border = BorderFactory.createTitledBorder(progressBarText);
    ((TitledBorder)border).setTitleFont(GUI.BASE_FONT);
    progressBar.setBorder(border);
    p.add(progressBar);
    p.setBorder(new EmptyBorder(5, 10, 5, 10));
    p.setPreferredSize(new Dimension(440, 60));
    p.setMaximumSize(new Dimension(440, 60));
    
    p2.add(p);    
    p2.add(cancelButton);
    add(p2);
    
  }

  
  protected JButton getCancelButton() {
      return cancelButton;
  }
  
  
  /**
   * Pobranie bieżącej wartości procentowej
   * @return Bieżąca wartość
   */
  @Override
  public int getProgress() {
      
    return (indeterminate) ? 0 : progressBar.getValue();
      
  }
  
  

  /**
   * Ustawienie wartosci procentowej dla paska postepu
   * @param value Wartosc procentowa paska postepu
   * @return False jeżeli przerwano wątek
   */
  @Override
  public boolean setProgress(int value) {
        
    if (!indeterminate) progressBar.setValue(value);  
    else if (value>=100) progressBar.setIndeterminate(false);
    
     // przerwa żeby można było zatrzymać wątek
     if (value%10 == 0 || (value<10)) try {
        Thread.sleep(1L);
     } catch (InterruptedException ex) {
        return false;
     }       
          
     return true;
    
  }
      
    
  /**
   * Wyświetlenie okienka (blokujące)
   */
  public void showComponent() {
       
    super.showDialog(450, 130);        
        
  }
  
  /**
   * Zamknięcie okienka
   */
  @Override
  public void hideComponent() {
      
    setCursor(null);  
    try {
      dispose();  
    }
    catch (Exception e) {}
      
  }
  
  
    
}
