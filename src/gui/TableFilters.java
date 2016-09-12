/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import datamodel.IData;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 * Szablon obiektu filtrów dla tabeli-listy z centralnego panelu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class TableFilters implements DocumentListener, ActionListener {
    
    /** Mapa pól filtrujących klucz-komponent */
    private final HashMap<String, JComponent> textFields;
    /** Filtrowana tabela z danymi */
    private final JTable objTable;
    /** Panel zawierający filtrujące pola */
    private final JPanel mainPanel;
    /** Panel zawierający filtrowaną tabelę */
    private final TablePanel tablePanel;
    
    
    /**
     * Konstruktor
     * @param objTable Filtrowana tabela
     * @param tablePanel Panel zawierający filtrowaną tabelę
     * @param mainPanel Panel do ustawienia filtrujących pól
     */
    public TableFilters(JTable objTable,  TablePanel tablePanel, JPanel mainPanel) {
        
      textFields = new HashMap<>(); 
      this.objTable = objTable;
      this.tablePanel = tablePanel;
      this.mainPanel = mainPanel;
        
    }
    
    
    /**
     * Metoda zeruje wszystkie filtry tabeli
     * @param leftComboValue True jeżeli nie zerować pola typu JComboBox
     */
    public void clearFields(boolean leftComboValue) {
        
       for (Map.Entry<String, JComponent> entry: textFields.entrySet()) {
           
         if (entry.getValue() instanceof JTextField)  ((JTextField)entry.getValue()).setText("");
         else if (entry.getValue() instanceof JComboBox && !leftComboValue)
             ((JComboBox)entry.getValue()).setSelectedIndex(0);
           
       }
       
    }
    
    /**
     * Metoda zeruje wszystkie filtry tabeli
     */
    public void clearFields() {
        
       clearFields(false);  
        
    }
    
    
    /**
     * Metoda ustawia wartosc pola filtrujacego
     * @param key Nazwa (klucz) pola
     * @param text Wartosc pola (napis)
     */
    public void setFieldText(String key, String text) {
        
       JComponent field = textFields.get(key);
       if (field == null) return;
       
       if (field instanceof JTextField) ((JTextField) field).setText(text);
        
    }
        
    
    
    /**
     * Metoda zwraca hashmape parametrow filtrow
     * @return Mapa parametrow filtrow
     */
    public Map<String, String> getParams() {
        
       Map<String, String> params = new HashMap<>(); 
        
       for (Map.Entry<String, JComponent> entry: textFields.entrySet()) { 
         
         if (entry.getValue() instanceof JTextField)  
           params.put(entry.getKey(), ((JTextField) (entry.getValue())).getText());         
         else if (entry.getValue() instanceof JComboBox) {
           int index;            
           try {
             index = ((IData) ((JComboBox) (entry.getValue())).getSelectedItem()).getId();
           }
           catch (ClassCastException e) {
             index = ((JComboBox) (entry.getValue())).getSelectedIndex();  
           }
           params.put(entry.getKey(), String.valueOf(index));        
         }
       } 
        
       return params; 
       
    }
    
       
    /**
     * Metoda dodaje nowe pole filtrujace
     * @param textFieldName Nazwa klucza filtra
     * @param textField Komponent filtrujacy (JTextField lub JComboBox)
     */
    public void addField(String textFieldName, JComponent textField) {
       
      textFields.put(textFieldName, textField);
      if (textField instanceof JTextField)  ((JTextField)textField).getDocument().addDocumentListener(this);
      else if (textField instanceof JComboBox) ((JComboBox)textField).addActionListener(this);
      mainPanel.add(textField);
        
    }       
    
    
    /**
     * Aktualizacja modelu tabeli w zwiazku ze zmianami filtrow
     */
    private void doUpdate() {
        
       Map<String, String> params = getParams();
           
       // zachowanie kluczy sortowania
       List<? extends SortKey> sortKeys = objTable.getRowSorter().getSortKeys();
       // zmiana modelu
       objTable.setModel(tablePanel.getNewTableModel(params));
       // przywrocenie szerokosci kolumn i kluczy sortowania
       tablePanel.afterModelChange();
       objTable.getRowSorter().setSortKeys(sortKeys);
         
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

    @Override
    public void actionPerformed(ActionEvent e) {
      doUpdate();   
    }
    
    
    
}
