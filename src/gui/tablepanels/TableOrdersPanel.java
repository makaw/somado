/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.tablepanels;


import datamodel.Order;
import datamodel.OrderState;
import datamodel.tablemodels.OrdersTableModel;
import gui.GUI;
import gui.ImageRes;
import gui.TableMouseAdapter;
import gui.TablePanel;
import gui.dialogs.DeliveryOpenDialog;
import gui.dialogs.tableforms.OrderEditModDialog;
import gui.dialogs.tableforms.OrderEditNewDialog;
import gui.mapview.MapDialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * Szablon obiektu centralnego panelu dla listy zamówień
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableOrdersPanel extends TablePanel {
    
  /** Dolny przycisk dodania nowego elementu */  
  private JButton addButton;
  /** Dolny przycisk edycji */
  private JButton editButton;
  /** Dolny przycisk podglądu na mapie */
  private JButton mapButton;
  /** Dolny przycisk do otwarcia nowej dostawy */
  private JButton deliveryButton;
  
  
    
  public TableOrdersPanel(String titleLabel, final GUI frame) {
             
     super(titleLabel, frame);
     completeTablePanel();
     afterModelChange();
     
     table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

         @Override
         public void valueChanged(ListSelectionEvent e) {
           
            boolean sel = (table.getSelectedRow()>=0);            
            if (sel) table.setComponentPopupMenu(new TableOrdersContextMenu(frame));         
            mapButton.setEnabled(sel);
            editButton.setEnabled(sel);
            
            if (sel) {
              OrdersTableModel oModel = (OrdersTableModel) (table.getModel());     
              Order tmp = new Order(oModel.getElement(table.convertRowIndexToModel(table.getSelectedRow())));
              if (tmp.getState() != OrderState.NEW) editButton.setEnabled(false);
            }
           
         }
         
         
     });     
     
  } 
  
  
  /**
   * Konieczne operacje po kazdej zmianie modelu
   */
  @Override
  final public void afterModelChange() {
 
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);     
    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);      
      
    // zmniejszenie i wyśr. kolumny "numer" [1]  
    table.getColumnModel().getColumn(1).setWidth(90);
    table.getColumnModel().getColumn(1).setMaxWidth(90);
    table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
    // zmniejszenie kolumny "waga" [5]
    table.getColumnModel().getColumn(5).setWidth(100);
    table.getColumnModel().getColumn(5).setMaxWidth(120);
    table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
    // zmniejszenie i wyśr. kolumny "data" [0]
    table.getColumnModel().getColumn(0).setMinWidth(120);
    table.getColumnModel().getColumn(0).setMaxWidth(130);
    table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    // zmniejszenie kolumny "komentarz" [6]
    table.getColumnModel().getColumn(6).setWidth(90);
    table.getColumnModel().getColumn(6).setMaxWidth(90);
    // wycentrowanie i zmniejszenie kolumny "stan"[3]
    table.getColumnModel().getColumn(3).setMaxWidth(100);
    table.getColumnModel().getColumn(3).setWidth(80);
    // łamanie tekstu i zwiększanie wysokości wiersza dla kolumny produkty
    setWrapRowRenderer();
    // kolorowanie pola stanu i wycentrowanie
    setStateCellRenderer();
    ((DefaultTableCellRenderer)table.getColumnModel().getColumn(3).getCellRenderer())
            .setHorizontalAlignment(JLabel.CENTER);
                     
    refreshAllElementsCountPanel();    
    
  }
         
  
  /**
   * Metoda ustanawia pola filtrowania
   * @param p Panel do umieszczenia komponentow pol filtrowania
   */
  @Override
  protected void setFiltersPanel(JPanel p) {    
      
    p.add(new JLabel("Numer:"));
    filters.addField("number", new JTextField(11));
    
    p.add(new JLabel("  "));
    
    p.add(new JLabel("Nazwa odbiorcy:"));
    filters.addField("customer_name", new JTextField(11));
    
    p.add(new JLabel("  "));
    
    p.add(new JLabel("Miejscowo\u015b\u0107:"));
    filters.addField("customer_city", new JTextField(11));    
    
    p.add(new JLabel("  "));
    
    JComboBox<OrderState> stateField  = new JComboBox<>();      
    stateField.setModel(new DefaultComboBoxModel<>(OrderState.values()));
    stateField.setFont(GUI.BASE_FONT);
    stateField.setSelectedIndex(0);
            
    p.add(new JLabel("Stan:"));
    filters.addField("state_id", stateField);        
    
  }
  
  
  /**
   * Metoda zwraca nowa instancje modelu tabeli
   * @param params Mapa parametrow (filtrow)
   * @return Nowy obiekt modelu tabeli
   */
  @Override
  protected AbstractTableModel getNewTableModel(Map<String, String> params) {

     return new OrdersTableModel(frame.getDatabaseShared(), params);
      
  }
   
  
  /**
   * Metoda zwraca listener myszy dla wierszy tabeli odpowiedni dla modelu
   * @return Listener myszy odpowiedni dla modelu
   */  
  @Override
  protected MouseAdapter getTableListener() {
      
    return new TableMouseAdapter() {
       
       @Override
       protected void doubleClickAction() {}
       
    };
      
  }
  
  
  /**
   * Metoda zwraca panel z osadzonymi przyciskami dostepnych akcji
   * @return Panel z przyciskami dostepnych akcji
   */
  @Override
  protected JPanel getButtonsPanel() {
      
    JPanel p = new JPanel(new GridLayout(1, 5, 10, 5));
    p.setPreferredSize(new Dimension(GUI.FRAME_WIDTH-100, 50));
    p.setOpaque(false);
    p.setBorder(new EmptyBorder(10, 10, 10, 10));         
  
    
    editButton = new JButton("Edytuj zam\u00f3wienie", ImageRes.getIcon("icons/form_edit.png"));
    editButton.setEnabled(false);
    editButton.setFocusPainted(false);
    editButton.setMaximumSize(new Dimension(100, 20));
      
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new OrderEditModDialog(frame);
          
       }
    });               
    
    p.add(editButton);
    
      
    addButton = new JButton("Nowe zam\u00f3wienie", ImageRes.getIcon("icons/form_add.png"));
    addButton.setFocusPainted(false);
      
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new OrderEditNewDialog(frame);
          
       }
    });         
      
    p.add(addButton);    
      
    
    mapButton = new JButton("Zobacz na mapie", ImageRes.getIcon("icons/map.png"));
    mapButton.setFocusPainted(false);
    mapButton.setEnabled(false);  
    
    mapButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         JTable parentTable = ((TablePanel) frame.getActiveDataPanel()).getTable();
         OrdersTableModel oModel = (OrdersTableModel) (parentTable.getModel());     
         Order oTmp = oModel.getElement(parentTable.convertRowIndexToModel(parentTable.getSelectedRow()));          
          
         new MapDialog(frame, oTmp.getCustomer());
          
       }
    });         
      
    p.add(mapButton);      
    
    p.add(new JLabel(" "));
    
    
    deliveryButton = new JButton("Nowa dostawa", ImageRes.getIcon("icons/delivery.png"));
    deliveryButton.setFocusPainted(false);
    deliveryButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           
           new DeliveryOpenDialog(frame);
            
        }
    });
    
    
    p.add(deliveryButton);

    return p;    
        

  }
  
 
    
  
    
}
