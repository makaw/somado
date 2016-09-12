/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.tablepanels;


import datamodel.tablemodels.DeliveriesTableModel;
import gui.GUI;
import gui.ImageRes;
import gui.TableMouseAdapter;
import gui.TablePanel;
import gui.dialogs.DeliveryOpenDialog;
import gui.dialogs.DeliveryViewDialog;
import gui.formfields.DateField;
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
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * Szablon obiektu centralnego panelu dla listy dostaw
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableDeliveriesPanel extends TablePanel {
    
  /** Dolny przycisk nowej dostawy */  
  private JButton addButton;
  /** Dolny przycisk podglądu */
  private JButton viewButton;
    
    
  public TableDeliveriesPanel(String titleLabel, final GUI frame) {
             
     super(titleLabel, frame);
     completeTablePanel();
     afterModelChange();
     
     table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

         @Override
         public void valueChanged(ListSelectionEvent e) {
           
            boolean sel = (table.getSelectedRow()>=0);
            
            if (sel) table.setComponentPopupMenu(new TableDeliveriesContextMenu(frame));
            viewButton.setEnabled(sel);
           
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
    
    // zmniejszenie i wyśr. kolumny "data" [0]
    table.getColumnModel().getColumn(0).setMinWidth(140);
    table.getColumnModel().getColumn(0).setMaxWidth(170);
    table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    // zmniejszenie i wyśr. kolumn [1] - [3]
    for (int i=1;i<=3;i++) {
      table.getColumnModel().getColumn(i).setPreferredWidth(90);
      table.getColumnModel().getColumn(i).setMaxWidth(120);
      table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
    // zmniejszenie i wyr. do prawej kolumn [4][5] i [7]
    for (int i=4;i<=7; i++) {
      if (i==6) continue;  
      table.getColumnModel().getColumn(i).setPreferredWidth(100);
      table.getColumnModel().getColumn(i).setMaxWidth(120);
      table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
    }
    // zmniejszenie i wyśr. kolumny "czas" [6]
    table.getColumnModel().getColumn(6).setMinWidth(120);
    table.getColumnModel().getColumn(6).setMaxWidth(130);
    table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
    
    // kolorowanie i wycentrowanie pola stanu
    setStateCellRenderer();
    ((DefaultTableCellRenderer)table.getColumnModel().getColumn(8).getCellRenderer())
            .setHorizontalAlignment(JLabel.CENTER);
                     
    refreshAllElementsCountPanel();    
    
  }
         
  
  /**
   * Metoda ustanawia pola filtrowania
   * @param p Panel do umieszczenia komponentow pol filtrowania
   */
  @Override
  protected void setFiltersPanel(JPanel p) {             
      
    p.add(new JLabel("Data dostawy:"));
    filters.addField("delivery_date", new JTextField(11));    
    
    p.add(new JLabel("      "));  
      
    JComboBox<String> stateField  = new JComboBox<>();      
    stateField.setModel(new DefaultComboBoxModel<>(new String[]{"Aktywna      ", "Zako\u0144czona  "}));
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

     return new DeliveriesTableModel(frame.getDatabaseShared(), params);
      
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
  
    
    viewButton = new JButton("Podgl\u0105d dostawy", ImageRes.getIcon("icons/map.png"));
    viewButton.setEnabled(false);
    viewButton.setFocusPainted(false);
    viewButton.setMaximumSize(new Dimension(100, 20));
      
    viewButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new DeliveryViewDialog(frame);
          
       }
    });               
    
    p.add(viewButton);
    
      
    addButton = new JButton("Nowa dostawa", ImageRes.getIcon("icons/delivery.png"));
    addButton.setFocusPainted(false);
      
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new DeliveryOpenDialog(frame);
          
       }
    });         
      
    p.add(addButton);    
    
    for (int i=0; i<3; i++) p.add(new JLabel());

    return p;    
        

  }
  
 
    
  
    
}
