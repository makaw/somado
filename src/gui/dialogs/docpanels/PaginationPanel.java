/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.docpanels;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


/**
 *
 * Ogólny szablon obiektu stronicującego panelu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class PaginationPanel extends JPanel {
    
   /** Liczba stron */ 
   private final int numPages;  
   /** Bieżąca strona */
   private int currentPage;
   /** Etykieta z liczbą rekordów */
   private final JLabel rowsCountLabel;
   /** Etykieta z numerem strony */
   private final JLabel pageLabel;
   /** Przycisk poprzedniej strony */
   private final JButton bPrev;
   /** Przycisk następnej strony */
   private final JButton bNext;
   
   
   /**
    * Konstruktor
    * @param numPages Liczba stron
    * @param closeButton Zamykający okno przycisk (lub inny komponent)
    */
   public PaginationPanel(int numPages, Component closeButton) {
    
      super(new GridLayout(1, 6));

      this.numPages = numPages;
      this.currentPage = 1;
     
      this.rowsCountLabel = new JLabel(" ");
      add(this.rowsCountLabel);
         
      pageLabel = new JLabel("Strona " + String.valueOf(currentPage) + "/" + String.valueOf(numPages));
     
      add(pageLabel);
      pageLabel.setBorder(new EmptyBorder(0, 10, 0, 30));
     
    
      bPrev = new JButton("\u25c0 poprzednia");
      bPrev.setEnabled(currentPage>1);
      bPrev.setFocusPainted(false);
      add(bPrev);
          
      bNext = new JButton("\u25b6 nast\u0119pna");
      bNext.setEnabled(numPages>currentPage);
      bNext.setFocusPainted(false);
      add(bNext);
      
      
      try {  
        add(closeButton);
      }
      catch (NullPointerException e) {}
 
     
      setMaximumSize(new Dimension(800, 45));
      setOpaque(false);
      setBorder(new EmptyBorder(5, 5, 5, 5));
     
   }
   
   /**
    * Metoda ustawia listenery na przyciskach
    */
   public void addListeners() {
       
      bNext.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            currentPage++;
            refresh(1);
            bPrev.setEnabled(currentPage>1);
            bNext.setEnabled(numPages>currentPage);
            pageLabel.setText("Strona " + String.valueOf(currentPage) + "/" + String.valueOf(numPages));
         }
          
     });
      
     bPrev.addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
            currentPage--;
            refresh(-1);
            bPrev.setEnabled(currentPage>1);
            bNext.setEnabled(numPages>currentPage);
            pageLabel.setText("Strona " + String.valueOf(currentPage) + "/" + String.valueOf(numPages));   
         }
          
     });             
       
       
       
   }

   
   /** 
    * Metoda odświeżająca panel danych po zmianie strony 
    * @param direction Kierunek, <0 lewo, >0 prawo
    */
   protected abstract void refresh(int direction);

   
   
   public int getCurrentPage() {
       
      return currentPage;
      
   }
   
   
   /**
    * Ustawienie tekstu etykiety z liczba rekordow
    * @param txt Tekst etykiety
    */
   public void setRowsCountLabelText(String txt) {
       
     rowsCountLabel.setText(txt);  
       
   }
   
   
    
}
