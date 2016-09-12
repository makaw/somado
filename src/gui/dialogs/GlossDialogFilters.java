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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * Szablon obiektu filtrów dla okienka z lista elementow
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów głównego komponentu listy słownika
 *  
 */
public class GlossDialogFilters<E extends IData> implements DocumentListener  {
    
    /** Lista pol tekstowych powiazanych z filtrami */
    private final HashMap<String, JTextField> textFields;
    /** Komponent listy (filtrowany) */
    private final JList<E> objList;
    /** Nadrzedne okienko dialogowe */
    private final IGlossDialog<E> glossDialog;
    /** Nadrzedny panel */
    private final JPanel mainPanel;
    
    
    /**
     * Konstruktor
     * @param glossDialog Ref. do nadrzednego okienka dialogowego
     * @param mainPanel Ref. do nadrzednego panelu
     */
    public GlossDialogFilters(IGlossDialog<E> glossDialog, JPanel mainPanel) {
        
      textFields = new HashMap<>(); 
      this.objList = glossDialog.getObjList();
      this.glossDialog = glossDialog;
      this.mainPanel = mainPanel;
        
    }
    
    
    /**
     * Dodanie pola filtrowania
     * @param textFieldName nazwa pola (indeks)
     * @param textField obiekt pola tekstowego
     */   
    public void addField(String textFieldName, JTextField textField) {
       
      textFields.put(textFieldName, textField);
      textField.getDocument().addDocumentListener(this);
      mainPanel.add(textField);
        
    }       
    
    
    /**
     * Aktualizacja listy (przez zaktualizowanie modelu)
     */
    private void doUpdate() {
           
      objList.removeAll();
      HashMap<String, String> filters = new HashMap<>();
         
      for (Map.Entry<String, JTextField> entry: textFields.entrySet()) { 
         
        filters.put(entry.getKey(), entry.getValue().getText());
           
      }
 
      objList.setModel(glossDialog.getListModel(filters));         
         
    }
    
    
    
    /**
     * Ustawienie filtrow oraz aktualizacja 
     * listy (przez zaktualizowanie modelu)
     * @param elementId id elementu (DB)
     */    
    public void doUpdate(int elementId) {
         
      // wyczyszczenie pol filtrow  
      for (Map.Entry<String, JTextField> entry: textFields.entrySet()) 
          entry.getValue().setText("");
        
      doUpdate();
      
      // przewiniecie i ustawienie zaznaczenia na zmodyfikowany/dodany rekord
      int index = glossDialog.getGlossary().getItemsIndex(elementId);
      objList.setSelectedIndex(index);
      objList.ensureIndexIsVisible(index);
        
    }  
    
    
         
    @Override
    public void changedUpdate(DocumentEvent e) {  
      doUpdate(); 
    }
       
    @Override
    public void removeUpdate(DocumentEvent e) {  
      doUpdate(); 
    }
       
    @Override
    public void insertUpdate(DocumentEvent e) { 
      doUpdate(); 
    }    
    
    
    
}
