/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import datamodel.tablemodels.ITableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


/**
 * Ogolny szablon obiektu menu kontekstowego dla wierszy tabel
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class TableContextMenu extends JPopupMenu {
    
   
   /** Referencja do GUI */
   protected final GUI frame;
   /** Nagłówek menu */
   private final JLabel hLabel;   
   
   /**
    * Konstruktor
    * @param frame Ref. do GUI
    */
   public TableContextMenu(GUI frame) {
       
      super();
      
      JPanel header = new JPanel(new GridLayout(1,1));
      hLabel = new JLabel("", JLabel.CENTER);
      header.setBackground(new Color(0xe5e5e5));
      hLabel.setMaximumSize(new Dimension(EmptyPanel.SCREEN_WIDTH, 40));
      header.setBorder(new LineBorder(new Color(0xccccff), 1));
      header.add(hLabel);
      add(header);
      
      header = new JPanel(new GridLayout(1,1));
      header.setBorder(new EmptyBorder(0, 0, 4, 0));
      header.setPreferredSize(new Dimension(250, 1));
      add(header);
      
      this.frame = frame;
       
      final JTable table = ((TablePanel) frame.getActiveDataPanel()).getTable();
      if (table == null || table.getSelectedRow() == -1) return;
      
      // poprawia dzialanie menu kontekstowego
      // zmiana wiersza w razie prawgo klikniecia na inny niz aktualnie wybrany
      addPopupMenuListener(new PopupMenuListener() {

          @Override
          public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
              
            try {
          
             int index = table.rowAtPoint(((Point) table.getClientProperty("popupTriggerLocation")));
              
             if (index != table.getSelectedRow()) {
                 
                table.setRowSelectionInterval(index, index);
                index = table.convertRowIndexToModel(index);                  
                hLabel.setText(((ITableModel) table.getModel()).getPopupMenuTitle(index));    
                
             }          
            
            }
            catch (NullPointerException ex) {  }   
              
            updateMenuItems();
            
          }

          @Override
          public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

          @Override
          public void popupMenuCanceled(PopupMenuEvent e) {}

      });
      
      // ustawienie naglowka menu kontekstowego      
      int index = table.convertRowIndexToModel(table.getSelectedRow());    
      hLabel.setText(((ITableModel) table.getModel()).getPopupMenuTitle(index));
   
   }
   
   
   /**
    * Wewnetrzna klasa reprezentujaca element menu
    */
   @SuppressWarnings("serial")
   protected class ContextMenuItem extends JMenuItem {
       
      public ContextMenuItem(String title, String icoName) {
          
         super(title); 
         if (icoName != null) setIcon(ImageRes.getIcon(icoName));
         setFont(GUI.BASE_FONT);
          
      }
      
      public ContextMenuItem(String title) {
          
        this(title, null);  
          
      }

   }
 
   /**
    * Mozliwosc zmiany pozycji gotowego menu przed pokazaniem
    */
   protected abstract void updateMenuItems();
  
   
    
}
