/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;

import datamodel.IData;
import datamodel.glossaries.IGlossary;
import gui.GUI;
import gui.SimpleDialog;
import java.awt.Color;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;


/**
 *
 * Rozszerzenie klasy do budowy prostych okienek dialogowych, do wykorzystania 
 * w okienkach edycji danych
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów komponentu listy słownika
 * 
 */
@SuppressWarnings("serial")
public abstract class SimpleFormDialog<E extends IData> extends SimpleDialog {
   
    
    
    protected SimpleFormDialog(GUI frame, String title) {
        
      super(frame, title);  
        
    }
    
    
    /**
     * Metoda zwraca liste oparta na modelu, ktora wyglada jak zwykle wypunktowanie
     * @param gloss Model listy
     * @param bgColor Kolor tla
     * @return Komponent listy
     */
    @SuppressWarnings("serial")
    protected JComponent getGlossList(IGlossary<E> gloss, Color bgColor) {
        
      JList<E> objList = new JList<>(gloss.getListModel());
      objList.setFont(GUI.BASE_FONT);
      
      // wylaczenie mozliwosci wyboru elementu      
      objList.setSelectionModel(new DefaultListSelectionModel() {
          @Override
          public void setSelectionInterval(int index0, int index1) {
            super.setSelectionInterval(-1, -1); 
          }
      });
      
      objList.setBackground(bgColor);
      objList.setVisibleRowCount(3);
      objList.setOpaque(false);
                  
      JScrollPane spT = new JScrollPane(objList);
      spT.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
      spT.setOpaque(false);
      spT.getViewport().setOpaque(false);
      spT.setBorder(new EmptyBorder(0, 0, 10, 0));  
        
      return spT;
        
    }
        
    
}

 