/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.tablepanels;


import datamodel.tablemodels.DriversTableModel;
import gui.GUI;
import gui.ImageRes;
import gui.TableMouseAdapter;
import gui.TablePanel;
import gui.dialogs.tableforms.DriverEditModDialog;
import gui.dialogs.tableforms.DriverEditNewDialog;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Map;
import javax.swing.JButton;
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
 * Szablon obiektu centralnego panelu dla listy kierowców
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableDriversPanel extends TablePanel {
    
  /** Dolny przycisk dodania nowego elementu */  
  private JButton addButton;
  /** Dolny przycisk edycji */
  private JButton editButton;
    
    
  public TableDriversPanel(String titleLabel, final GUI frame) {
             
     super(titleLabel, frame);
     completeTablePanel();
     afterModelChange();
     
     table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

         @Override
         public void valueChanged(ListSelectionEvent e) {
           
            boolean sel = (table.getSelectedRow()>=0);
            
            if (sel) table.setComponentPopupMenu(new TableDriversContextMenu(frame));
            editButton.setEnabled(sel);
           
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
      
    // szerokość kolumn z loginem, nazwiskiem i imieniem [0][1][2]
    table.getColumnModel().getColumn(0).setMinWidth(100);
    table.getColumnModel().getColumn(0).setPreferredWidth(100);
    table.getColumnModel().getColumn(1).setMinWidth(150);
    table.getColumnModel().getColumn(1).setPreferredWidth(150);
    table.getColumnModel().getColumn(2).setMinWidth(100);
    table.getColumnModel().getColumn(2).setPreferredWidth(100);
    // zmniejszenie kolumn "dostępność"[6] i [7]    
    table.getColumnModel().getColumn(6).setMaxWidth(80);
    table.getColumnModel().getColumn(7).setMaxWidth(80);    
    // wycentrowanie kolumny "nr rej." (4)
    table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    // szerokość kolumny z modelem pojazdu [3]
    table.getColumnModel().getColumn(3).setMinWidth(150);
    table.getColumnModel().getColumn(3).setPreferredWidth(150);           
    // wycentrowanie i zmniejszenie kolumny "dostępność"[5]
    table.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );  
    table.getColumnModel().getColumn(5).setMaxWidth(80);
    
    // kolorowanie i wycentrowanie pól stanu
    setStateCellRenderer();
    ((DefaultTableCellRenderer)table.getColumnModel().getColumn(6).getCellRenderer())
            .setHorizontalAlignment(JLabel.CENTER);  
    ((DefaultTableCellRenderer)table.getColumnModel().getColumn(7).getCellRenderer())
            .setHorizontalAlignment(JLabel.CENTER);
                     
    refreshAllElementsCountPanel();    
    
  }
         
  
  /**
   * Metoda ustanawia pola filtrowania
   * @param p Panel do umieszczenia komponentow pol filtrowania
   */
  @Override
  protected void setFiltersPanel(JPanel p) {    
      
    p.add(new JLabel("Nazwisko:"));
    filters.addField("surname", new JTextField(11));
    
    p.add(new JLabel("  "));
    
    p.add(new JLabel("Nr rejestracyjny:"));
    filters.addField("registration_no", new JTextField(11));
    
  }
  
  
  /**
   * Metoda zwraca nowa instancje modelu tabeli
   * @param params Mapa parametrow (filtrow)
   * @return Nowy obiekt modelu tabeli
   */
  @Override
  protected AbstractTableModel getNewTableModel(Map<String, String> params) {

     return new DriversTableModel(frame.getDatabaseShared(), params);
      
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
  
    
    editButton = new JButton("Edytuj kierowc\u0119", ImageRes.getIcon("icons/form_edit.png"));
    editButton.setEnabled(false);
    editButton.setFocusPainted(false);
    editButton.setMaximumSize(new Dimension(100, 20));
      
    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new DriverEditModDialog(frame);
          
       }
    });               
    
    p.add(editButton);
    
      
    addButton = new JButton("Nowy kierowca", ImageRes.getIcon("icons/form_add.png"));
    addButton.setFocusPainted(false);
      
    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
           
         new DriverEditNewDialog(frame);
          
       }
    });         
      
    p.add(addButton);    
    
    for (int i=0; i<3; i++) p.add(new JLabel());

    return p;    
        

  }
  
 
    
  
    
}
