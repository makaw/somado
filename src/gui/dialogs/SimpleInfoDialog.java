/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import gui.BgPanel;
import gui.SimpleDialog;
import gui.GUI;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import somado.IConf;


/**
 *
 * Ogolny szablon obiektu wywołującego informacyjne okienko dialogowe
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class SimpleInfoDialog extends SimpleDialog {
    
   private final String textHeader; 
   private final String text; 
   private final int height;
   private final Icon icon;
   private final boolean vertScroll;
   
    
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param title Tytul okienka
    * @param textHeader
    * @param text Treść komunikatu
    * @param height Wysokość okienka 
    * @param icon Ikona okienka
    * @param vertScroll True jezeli pole tekstowe ma byc przewijane
    */ 
   public SimpleInfoDialog(GUI frame, String title, String textHeader, String text, 
                             int height, Icon icon, boolean vertScroll) {
       
     super(frame, IConf.APP_NAME + (!title.isEmpty() ? " - " : "") + title);
     this.textHeader = textHeader;
     this.text = text.replace("\n", System.getProperty("line.separator"));
     this.height = height;
     this.icon = icon;
     this.vertScroll = vertScroll;
     
            
   } 
   
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param title Tytul okienka
    * @param textHeader
    * @param text Treść komunikatu
    * @param height Wysokość okienka 
    * @param icon Ikona okienka
    */    
   public SimpleInfoDialog(GUI frame, String title, String textHeader, String text, 
                             int height, Icon icon) {
    
    this(frame, title, textHeader, text, height, icon, true);    
    
   }   
   
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param text Treść komunikatu
    * @param height Wysokość okienka 
    * @param icon Ikona okienka
    */    
   public SimpleInfoDialog(GUI frame, String text, int height, Icon icon) {   
       
     this(frame, "", "", text, height, icon, false);  
       
   }
   
   
   /**
    * Wywolanie okienka dialogowego
    */
   protected void showDialog() {
            
     super.showDialog(350, height);         
       
   }

   
   
   /**
    * Dolny panel z przyciskami
    * @return Dolny panel
    */
   protected abstract JPanel buttonPanel();
   

   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   

      
      JPanel mainPanel = new BgPanel("bg_dialog.jpg");
      
      if (!textHeader.isEmpty()) {
        JLabel head = new JLabel(textHeader, JLabel.LEFT);
        head.setBorder(new EmptyBorder(0, 0, 0, 62));
        mainPanel.add(head);
      }
      
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
     
      JLabel ico = new JLabel(icon);
      ico.setBorder(new EmptyBorder(0, 15, 0, 25));
      p.add(ico);
      
      JTextArea tx = new JTextArea(text);
      tx.setLineWrap(true);
      tx.setWrapStyleWord(true);
      tx.setEditable(false);
      tx.setOpaque(false);
      tx.setRows(5);
      tx.setBackground(p.getBackground());
      tx.setBorder(new EmptyBorder(10, 0, 10, 10));
      JScrollPane sc = new JScrollPane(tx);
      sc.setOpaque(false);
      sc.setBorder(null);
      sc.getViewport().setOpaque(false);
      sc.getViewport().setBorder(null);
      sc.setPreferredSize(new Dimension(250, height-95));
      sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
      sc.setVerticalScrollBarPolicy(vertScroll ? ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED 
                                    : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
      p.add(sc);
        
      mainPanel.add(p);
      mainPanel.add(buttonPanel());
      
      add(mainPanel);
      
      
   }
    
}

