/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.tableforms;

import datamodel.Customer;
import datamodel.Order;
import datamodel.OrderItem;
import datamodel.OrderState;
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
import javax.swing.border.EmptyBorder;
import datamodel.tablemodels.OrdersTableModel;
import gui.formfields.FormRowPad;
import gui.formfields.FormTabbedPane;
import gui.TableFilters;
import gui.TablePanel;
import gui.dialogs.EditLockableDataDialog;
import gui.dialogs.ErrorDialog;
import gui.formfields.CustomerField;
import gui.formfields.OrderProductsField;
import gui.tablepanels.TableOrdersPanel;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import somado.IConf;
import somado.Settings;


/**
 *
 * Szablon okienka dodawania/modyfikacji zamówień
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class OrderEditDialog extends EditLockableDataDialog implements IDialogForm {
    
   /** Model tabeli z listą zamówień */ 
   private final OrdersTableModel tableModel;  
   /** Obiekt edytowanego/dodawanego zamówienia */
   protected final Order order;
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
   public OrderEditDialog(GUI frame, String title, boolean edit) {
        
     super(frame, IConf.APP_NAME + " - " + title);
     
     this.parentTablePanel = (TablePanel) frame.getActiveDataPanel();
     this.parentTable = parentTablePanel.getTable();
     this.parentTableFilters = parentTablePanel.getTableFilters();
     
     tableModel = (parentTable != null) ? (OrdersTableModel)(parentTable.getModel()) : null;
     
     selectedTableRow = edit ? parentTable.convertRowIndexToModel(parentTable.getSelectedRow()) : -1;   
     
     this.order = edit ? new Order(tableModel.getElement(selectedTableRow)) : new Order();
     
     if (this.order.getState() != OrderState.NEW) return;
     
     checkLock(this.order);
      
     super.showDialog(645, edit ? 490 : 470);
         
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
  protected abstract boolean saveItem(Order item);
  
  
  /**
   * Metoda odswieza liste po zapisie do BD
   * @param item Zmienione dane
   */
  protected void refreshItemsList(Order item) {              
      
     // wyczyszczenie filtrow i nowy model 
     parentTableFilters.clearFields(true);
     OrdersTableModel oModel = new OrdersTableModel(frame.getDatabaseShared(), parentTableFilters.getParams());
     parentTablePanel.getTable().setModel(oModel);
       
     // domyslne sortowanie
     ((TableOrdersPanel)parentTablePanel).afterModelChange();
     parentTablePanel.getTable().getRowSorter().toggleSortOrder(oModel.getDefaultSortOrder());
     if (!oModel.getDefaultSortOrderAsc())
       parentTablePanel.getTable().getRowSorter().toggleSortOrder(oModel.getDefaultSortOrder()); 
       
     // ustawienie zaznaczenia
     try {
       int rowIndex = parentTablePanel.getTable().getRowSorter().convertRowIndexToView(oModel.getIndex(order));
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
        JPanel tabPane1 = (JPanel)tabPane.add("Dane zam\u00f3wienia", new JPanel());
        tabPaneAudit = (frame.getUser().isAdmin() && order.getId()!=0) ?  
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

        JLabel txt;
        
        if (order.getId()>0) {
           
           txt = new JLabel(order.getNumber());
           txt.setFont(GUI.BASE_FONT);
           tabPane1.add(new FormRowPad("Nr zam\u00f3wienia:", txt));  
            
        }                
        
        
        txt = new JLabel(Settings.DATETIME_FORMAT.format(order.getDateAdd()));
        txt.setFont(GUI.BASE_FONT);
        tabPane1.add(new FormRowPad("Data i godzina:", txt));        
        
        txt = new JLabel(order.getState().toString());
        txt.setForeground(order.getState().getColor());
        txt.setFont(GUI.BASE_FONT);
        tabPane1.add(new FormRowPad("Stan zam\u00f3wienia:", txt));           
        
        final CustomerField customerField = new CustomerField(frame, order.getCustomer());
        customerField.setEditable(!locked);
        tabPane1.add(new FormRowPad("Odbiorca towaru:", customerField));
        
        final OrderProductsField productsField = new OrderProductsField(frame, order);
        productsField.setEnabled(!locked);
        tabPane1.add(new FormRowPad("Produkty:               ", productsField));            
        
        final JTextArea commentField = new JTextArea(order.getComment());
        commentField.setLineWrap(true);
        commentField.setWrapStyleWord(true);
        commentField.setRows(3);
        commentField.setEditable(!locked);
      
        JScrollPane sp = new JScrollPane(commentField);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        sp.setMaximumSize(new Dimension(600, 60));
        sp.setPreferredSize(new Dimension(600, 60));
    
        tabPane1.add(new FormRowPad("Uwagi:                      ", sp));
        
        
        if (order.getId()!=0) refreshAuditPanel();
       
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
                    
           @SuppressWarnings("unchecked") 
           Order orderUpd = new Order(order.getId(), order.getNumber(), 
                   (List<OrderItem>)productsField.getSelectedElement(), 
                   (Customer) customerField.getSelectedElement(), order.getState(), 
                   commentField.getText(), order.getDateAdd());
                   
           if (!saveItem(orderUpd)) {
               
              new ErrorDialog(frame, lastError);  
   
           }
           
           else {
               
             needRefresh = true;  
             refreshItemsList(orderUpd);
             frame.getDataPanel(GUI.TAB_ORDERS).setChanged(true);
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



