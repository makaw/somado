/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;

import gui.GUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import datamodel.Vehicle;
import datamodel.VehicleModel;
import datamodel.tablemodels.VehiclesTableModel;
import gui.formfields.FormRowPad;
import gui.formfields.FormTabbedPane;
import gui.TableFilters;
import gui.TablePanel;
import gui.dialogs.EditLockableDataDialog;
import gui.dialogs.ErrorDialog;
import gui.formfields.VehicleModelField;
import gui.tablepanels.TableVehiclesPanel;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import somado.IConf;


/**
 *
 * Szablon okienka dodawania/modyfikacji pojazdu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class VehicleEditDialog extends EditLockableDataDialog implements IDialogForm {
    
   /** Model tabeli z lista pojazdow */ 
   private final VehiclesTableModel tableModel;  
   /** Obiekt edytowanego/dodawanego pojazdu */
   protected final Vehicle vehicle;
   /** Nadrzedny komponent tabeli */
   private final JTable parentTable;
   /** Nadrzedny panel z tabela */
   protected final TablePanel parentTablePanel;
   /** Filtry nadrzednej tabeli */
   protected final TableFilters parentTableFilters;
   /** Indeks zaznaczonego wiersza nadrzednej tabeli */
   private final int selectedTableRow;
   /** True jezeli konieczne jest odswiezenie powiazanej tabeli */
   private boolean needRefresh = false;
   /** Ostatni blad (DB lub logika) */
   protected String lastError = "";

    
   
   /**
    * Konstruktor przy otwieraniu okna z tabeli
    * @param frame Referencja do GUI
    * @param title Tytul okna
    * @param edit True jezeli to edycja 
    */
   public VehicleEditDialog(GUI frame, String title, boolean edit) {
        
     super(frame, IConf.APP_NAME + " - " + title);
     
     this.parentTablePanel = (TablePanel) frame.getActiveDataPanel();
     this.parentTable = parentTablePanel.getTable();
     this.parentTableFilters = parentTablePanel.getTableFilters();
     
     tableModel = (parentTable != null) ? (VehiclesTableModel)(parentTable.getModel()) : null;
     
     selectedTableRow = edit ? parentTable.convertRowIndexToModel(parentTable.getSelectedRow()) : -1;   
     
     this.vehicle = edit ? new Vehicle(tableModel.getElement(selectedTableRow)) : new Vehicle();
     
     checkLock(this.vehicle);
      
     super.showDialog(645, 350);
         
   }
   
   

  
   
   @Override
   protected void getContent() {
   
     JPanel p = new JPanel(new GridLayout(1,1));
     p.setOpaque(false);
     p.setBorder(new EmptyBorder(5, 5, 5, 5));
       
     p.add(new FormPanel(this));
     add(p);
   
   }
   
  
  
  
  /**
   * Metoda zapisujaca do BD
   * @param item Dane do zapisania
   * @return true jezeli OK
   */
  protected abstract boolean saveItem(Vehicle item);
  
  
  /**
   * Metoda odswieza liste po zapisie do BD
   * @param item Zmienione dane
   */
  protected void refreshItemsList(Vehicle item) {              
      
     // wyczyszczenie filtrow i nowy model 
     parentTableFilters.clearFields();
     VehiclesTableModel vModel = new VehiclesTableModel(frame.getDatabaseShared());
     parentTablePanel.getTable().setModel(vModel);
       
     // domyslne sortowanie
     ((TableVehiclesPanel)parentTablePanel).afterModelChange();
     parentTablePanel.getTable().getRowSorter().toggleSortOrder(vModel.getDefaultSortOrder());
     if (!vModel.getDefaultSortOrderAsc())
       parentTablePanel.getTable().getRowSorter().toggleSortOrder(vModel.getDefaultSortOrder()); 
       
     // ustawienie zaznaczenia
     try {
       int rowIndex = parentTablePanel.getTable().getRowSorter().convertRowIndexToView(vModel.getIndex(vehicle));
       parentTablePanel.getTable().setRowSelectionInterval(rowIndex, rowIndex);
       parentTablePanel.getTable().scrollRectToVisible(parentTablePanel.getTable().getCellRect(rowIndex, 0, true));
     }
     // brak zaznaczenia (-1)
     catch (ArrayIndexOutOfBoundsException e) {}
       
  }
  
 
  
  /**
   * Klasa wewnetrzna - szablon calego formularza
   */
  @SuppressWarnings("serial")
  private class FormPanel extends JPanel {
      
     /** Kolor tla aktywnej zakladki */
     private final Color bgColor = new Color(0xddecfa);  
     private final JPanel tabPaneAudit;
     

     FormPanel(JDialog dialog) {
         
        super();
       
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        final JPanel buttonsPanel = new JPanel();
        
        JTabbedPane tabPane = new FormTabbedPane(bgColor);        
        JPanel tabPane1 = (JPanel)tabPane.add("Dane pojazdu", new JPanel());
        tabPaneAudit = (frame.getUser().isAdmin() && vehicle.getId()!=0) ? 
                (JPanel)tabPane.add("Historia zmian", new JPanel()) : new JPanel();     

        
        tabPane.addChangeListener(new ChangeListener() { 
            @Override
            public void stateChanged(ChangeEvent evt) { 
                JTabbedPane pane = (JTabbedPane) evt.getSource(); 
                Component sel = pane.getSelectedComponent();
                buttonsPanel.setVisible(!sel.equals(tabPaneAudit));
            } 
        });
        
        
        tabPane1.add(new JLabel(" "));
        
        final VehicleModelField vmField = new VehicleModelField(frame, vehicle.getVehicleModel());
        vmField.setEditable(!locked);
        tabPane1.add(new FormRowPad("Model pojazdu:", vmField));
        
        final JTextField yearField = new JTextField(22);
        yearField.setText(String.valueOf(vehicle.getYear()));
        yearField.setEditable(!locked);
        tabPane1.add(new FormRowPad("Rok produkcji:", yearField));
        
        final JTextField regNoField = new JTextField(22);
        regNoField.setText(vehicle.getRegistrationNo());
        regNoField.setEditable(!locked);
        tabPane1.add(new FormRowPad("Nr rejestracyjny:", regNoField));        
        
        final JTextArea commentField = new JTextArea(vehicle.getComment());
        commentField.setLineWrap(true);
        commentField.setWrapStyleWord(true);
        commentField.setRows(3);
        commentField.setEditable(!locked);
      
        JScrollPane sp = new JScrollPane(commentField);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setMaximumSize(new Dimension(600, 60));
        sp.setPreferredSize(new Dimension(600, 60));
    
        tabPane1.add(new FormRowPad("Uwagi:                      ", sp));
        
        final JCheckBox capableField = new JCheckBox("Pojazd dost\u0119pny");
        capableField.setSelected(vehicle.isCapable());
        capableField.setEnabled(!locked);
        capableField.setOpaque(false);
        capableField.setFocusPainted(false);
        capableField.setFont(GUI.BASE_FONT);
        tabPane1.add(new FormRowPad("Dost\u0119pno\u015b\u0107: ", capableField));
        
        if (vehicle.getId()!=0) refreshAuditPanel();
       
        add(tabPane);
                        
        
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(new EmptyBorder(12, 0, 2, 0));

        JButton saveButton = new JButton(" Zapisz ");
        saveButton.setFocusPainted(false);
        saveButton.setEnabled(!locked);
        
        saveButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(final ActionEvent e) {
            
           int year = 0;
           try {
             year = Integer.parseInt(yearField.getText());  
           }
           // ew. wyjatek bedzie rzucony w klasie Vehicle
           catch (Exception ex) {}
           
           Vehicle vehicleUpd = new Vehicle(vehicle.getId(), (VehicleModel)vmField.getSelectedElement(), 
                   year, regNoField.getText(), commentField.getText(), capableField.isSelected());      
                   
           if (!saveItem(vehicleUpd)) {
               
              new ErrorDialog(frame, lastError);  
   
           }
           
           else {
               
             needRefresh = true;  
             refreshItemsList(vehicleUpd);
             frame.getDataPanel(GUI.TAB_DRIVERS).setChanged(true);
             frame.getDataPanel(GUI.TAB_VEHICLES).setChanged(true);
             dispose();
               
           }
           
          }
        });            
                
        buttonsPanel.add(saveButton);
        buttonsPanel.add(new JLabel(" "));
        buttonsPanel.add(new JLabel(" "));
        buttonsPanel.add(new CloseButton(" Anuluj "));        
        add(buttonsPanel);      
        
         
     }
     
     
     private void refreshAuditPanel()  {
      
       tabPaneAudit.removeAll();
       tabPaneAudit.add(new JLabel(" "));   
       try {
         tabPaneAudit.add(getAuditPanel(bgColor));
       }
       catch (NullPointerException e) {}

    }
          
      
  }
    
  
  /** Metoda ma zwracac panel z historia zmian
   * @param bgColor Kolor tla
   * @return Panel z historia zmian
   */
  protected abstract JPanel getAuditPanel(Color bgColor);
  
  
  /**
   * Metoda okresla czy konieczne jest odswiezenie tabeli-rodzica
   * @return True jezeli konieczne jest odswiezenie
   */
  @Override
  public boolean isNeedRefresh() {
      
    return needRefresh;  
      
  }
  
 /** 
   * (Niezaimplementowana) Metoda zwraca indeks BD ostatnio dodanego elementu
   * @return Indeks BD ostatnio dodanego elementu
   */
  @Override
  public int getAddedIndex() {
      
     return -1;  
      
  }
    
}



