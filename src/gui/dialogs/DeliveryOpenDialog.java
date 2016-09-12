/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import datamodel.Delivery;
import datamodel.Driver;
import datamodel.OrderState;
import datamodel.Pack;
import datamodel.tablemodels.DriversDeliveryTableModel;
import datamodel.tablemodels.OrdersDeliveryPlanTableModel;
import gui.SimpleDialog;
import gui.GUI;
import gui.formfields.DateField;
import gui.formfields.FormRowPad;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import somado.IConf;
import somado.Settings;
import spatial_vrp.DeliveryPlan;



/**
 *
 * Szablon obiektu wywołującego okienko otwarcia planu nowej dostawy (wybór zamówień i kierowców)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DeliveryOpenDialog extends SimpleDialog {
    
   /** Tabela z listą zamówień */
   private JTable ordersTable;
   
   /** Tabela z listą kierowców */
   private JTable driversTable;   

   
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    */ 
   public DeliveryOpenDialog(GUI frame) {
        
     super(frame, IConf.APP_NAME + "- planowanie nowej dostawy");
     super.showDialog(750, 480);
            
   } 
   
   
    
   /**
    * Metoda wyświetlająca zawartość okienka
    */
   @Override
   protected void getContent()  {   
            
      JPanel p = new JPanel();
      p.setOpaque(false);
      p.setBorder(new EmptyBorder(5, 5, 5, 5));
      p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
      
      final OrdersDeliveryPlanTableModel ordersModel = new OrdersDeliveryPlanTableModel(frame.getDatabaseShared());
      
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(JLabel.CENTER);
      DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
      rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        
      ordersTable = new JTable(ordersModel);
      ordersTable.setRowSorter(null);
      ordersTable.setFocusable(false);
      ordersTable.setRowSelectionAllowed(false);
      ordersTable.setAutoCreateRowSorter(false);
      ordersTable.setRowHeight(22); 
      // zmniejszenie kolumn z checkboksem [0] 
      ordersTable.getColumnModel().getColumn(0).setMinWidth(50);
      ordersTable.getColumnModel().getColumn(0).setMaxWidth(50);
      // zmniejszenie kolumny "data" [1]  i wyśrodkowanie
      ordersTable.getColumnModel().getColumn(1).setMinWidth(120);
      ordersTable.getColumnModel().getColumn(1).setMaxWidth(130);     
      ordersTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
      // zmniejszenie kolumny "numer" [2] i wyśrodkowanie
      ordersTable.getColumnModel().getColumn(2).setWidth(90);
      ordersTable.getColumnModel().getColumn(2).setMaxWidth(90);
      ordersTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
      // zmniejszenie kolumny "waga" [4] i wyr. do prawej
      ordersTable.getColumnModel().getColumn(4).setWidth(100);
      ordersTable.getColumnModel().getColumn(4).setMaxWidth(120);
      ordersTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
      
      JScrollPane scroll = new JScrollPane(ordersTable, 
              JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      
      scroll.setMaximumSize(new Dimension(800, 200));     
      scroll.setPreferredSize(new Dimension(600, 200));     
      
              
      JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p1.setOpaque(false);
      p1.setMaximumSize(new Dimension(800, 32));
      p1.add(new JLabel("Zam\u00f3wienia \"" + OrderState.NEW.toString() + "\" z dost\u0119pnymi produktami (" 
              + String.valueOf(ordersModel.getAllElementsCount()) + "):"));
      p.add(p1);
      
      p.add(scroll);
            
      
      final DriversDeliveryTableModel driversModel = new DriversDeliveryTableModel(frame.getDatabaseShared());
      
      driversTable = new JTable(driversModel);
      driversTable.setRowSorter(null);
      driversTable.setFocusable(false);
      driversTable.setRowSelectionAllowed(false);
      driversTable.setAutoCreateRowSorter(false);
      driversTable.setRowHeight(22); 
      // zmniejszenie kolumn z checkboksem [0] [4]
      driversTable.getColumnModel().getColumn(0).setMinWidth(50);
      driversTable.getColumnModel().getColumn(0).setMaxWidth(50);
      driversTable.getColumnModel().getColumn(5).setMinWidth(60);
      driversTable.getColumnModel().getColumn(5).setMaxWidth(60);
      // zwiększenie kolumny z nazwiskiem [1]
      driversTable.getColumnModel().getColumn(1).setMinWidth(150);
      driversTable.getColumnModel().getColumn(1).setPreferredWidth(150);
      // wyśrodkowanie nr rej [2]
      driversTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
      // zwiększenie kolumny z modelem pojazdu [3]
      driversTable.getColumnModel().getColumn(3).setMinWidth(150);
      driversTable.getColumnModel().getColumn(3).setPreferredWidth(150);      
      // zmniejszenie kolumny "ładowność" [4], wyr. do prawej
      driversTable.getColumnModel().getColumn(4).setWidth(100);
      driversTable.getColumnModel().getColumn(4).setMaxWidth(120);
      driversTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
           
      scroll = new JScrollPane(driversTable, 
              JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      
      scroll.setMaximumSize(new Dimension(800, 200));     
      scroll.setPreferredSize(new Dimension(600, 200));     
                    
      p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
      p1.setOpaque(false);
      p1.setMaximumSize(new Dimension(800, 32));
      p1.add(new JLabel("Dost\u0119pni kierowcy (" + String.valueOf(driversModel.getAllElementsCount()) + "):"));
      p.add(p1);
      
      p.add(scroll);
                  
      
      final DateField deliveryDateField = new DateField(new Date());
      p.add(new FormRowPad("Data dostawy: ", deliveryDateField));
      
      
      JButton startButton = new JButton("Planuj dostaw\u0119");      
      startButton.setFocusPainted(false);
      startButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
                                     
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            List<Pack> packsList = ordersModel.getSelectedItems();
            List<Driver> driversList = driversModel.getSelectedItems();
            
            if (((Date)deliveryDateField.getSelectedElement()).before(cal.getTime())) {
                
              cal.add(Calendar.DATE, 1);  
              new ErrorDialog(frame, "Podano nieprawid\u0142ow\u0105 dat\u0119 dostawy. Prosz\u0119 wybra\u0107 "
                      + "dat\u0119 dzisiejsz\u0105 (" + Settings.DATE_FORMAT.format(cal.getTime()) + ") "
                      + "lub p\u00f3\u017aniejsz\u0105.");  
                
            }
            
            else if (packsList.isEmpty()) {
                
               new ErrorDialog(frame, "Nie wybrano \u017cadnego zam\u00f3wienia.");                 
                
            }
            
            else if (driversList.isEmpty()) {
                
               new ErrorDialog(frame, "Nie wybrano \u017cadnego kierowcy.");                 
                
            }            
            
            else {
             
              DeliveryPlan delivery = new DeliveryPlan(frame, packsList, driversList);
              
              if (delivery.isDone()) {
                  
                dispose();
                new DeliveryPlanDialog(frame, delivery, (Date)deliveryDateField.getSelectedElement());
                
              }
              
            }
           
             
         }
      });
      
      
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(10, 0, 5, 0)); 
      p2.add(startButton);
      p2.add(new JLabel(" "));
      p2.add(new CloseButton("Anuluj"));
            
      p.add(p2);
      
      add(p);      
      
   }


   
}

