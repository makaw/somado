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
import datamodel.DeliveryDriver;
import datamodel.glossaries.GlossDeliveries;
import datamodel.tablemodels.DeliveriesTableModel;
import datamodel.tablemodels.DeliveryOrdersTableModel;
import gui.GUI;
import gui.ImageRes;
import gui.SimpleDialog;
import gui.TableStateCellRenderer;
import gui.TablePanel;
import gui.formfields.FormRowPad;
import gui.formfields.FormTabbedPane;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import gui.mapview.MapDialog;
import gui.tablepanels.TableDeliveriesPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import somado.IConf;
import somado.Settings;
import spatial_vrp.DeliveryPlan;


/**
 *
 * Szablon obiektu wywołującego okienko z przygotowanym planem dostawy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DeliveryViewDialog extends SimpleDialog implements IProgressInvoker {
    
   /** Obiekt dostawy */ 
   private Delivery delivery;
   
   /**
    * Konstruktor,wyświetlenie okienka
    * @param frame Referencja do GUI
    */     
    public DeliveryViewDialog(GUI frame) {
     
      super(frame, IConf.APP_NAME + " - podgl\u0105d dostawy");           
    
      new Loader(frame, "Trwa wczytywanie danych ", this, false).load();
        
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
      
      JPanel p0 = new JPanel(new GridLayout(1, 3, 50, 0));
      p0.setOpaque(false);
      JLabel txt = new JLabel(delivery.isActive() ? "Aktywna" : "Zako\u0144czona");
      if (delivery.isActive()) txt.setForeground(new Color(0x336600));
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Stan dostawy: ", txt, 120));  
      
      txt = new JLabel(Settings.DATETIME_NS_FORMAT.format(delivery.getDateAdd()));
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Zatwierdzenie: ", txt, 160));  
      
      if (!delivery.isActive()) {
          
        txt = new JLabel(Settings.DATETIME_NS_FORMAT.format(delivery.getDateEnd()));
        txt.setFont(GUI.BASE_FONT);
        p0.add(new FormRowPad("Zamkni\u0119cie: ", txt, 160));    
          
      }
      else p0.add(new JLabel());
      p.add(p0);
        
      p0 = new JPanel(new GridLayout(1, 3, 50, 0));
      p0.setOpaque(false);
      txt = new JLabel(Settings.DATE_FORMAT.format(delivery.getDeliveryDate()));
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Data dostawy: ", txt, 120));  
      txt = new JLabel(Settings.formatTime(delivery.getDriverMaxWorkTime()));
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Maks. czas jazdy kierowcy:", txt, 200));
      txt = new JLabel(delivery.getShortestPathAlgorithm().toString());
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Algorytm najkr.\u015bcie\u017cek:", txt, 190));      
         
      p0.setMinimumSize(new Dimension(800, 45));
      p0.setSize(new Dimension(800, 45));
      //p0.setAlignmentX(JLabel.LEFT);
      p.add(p0);
      
      p0 = new JPanel(new GridLayout(1, 3, 50, 0));
      p0.setOpaque(false);
      
      JLabel orderNumberLabel = new JLabel();
      orderNumberLabel.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Zam\u00f3wienia:", orderNumberLabel, 120));
      
      txt = new JLabel(String.valueOf(delivery.getDrivers().size()));
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Liczba kierowc\u00f3w:", txt, 200)); 
      
      txt = new JLabel(delivery.isAdditionalGeometry() ? "tak" : "nie");
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Dodatkowa geometria:", txt, 190));  
      
      p0.setMinimumSize(new Dimension(800, 45));
      p0.setSize(new Dimension(800, 45));
      //p0.setAlignmentX(JLabel.LEFT);
      p.add(p0);     
      
      JScrollPane scroll;
      JTabbedPane tabPane = new FormTabbedPane(new Color(0xddecfa));          
       
      
      p0 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p0.setOpaque(false);
      p0.setBorder(new LineBorder(new Color(0xc6c6c6), 1));
      p0.setBorder(BorderFactory.createTitledBorder(p0.getBorder(), "  Razem:  "));
      p0.setMinimumSize(new Dimension(800, 50));
      p0.setSize(new Dimension(800, 50));
      
      JLabel totalTimeLabel = new JLabel();
      totalTimeLabel.setText("00:00:00");
      totalTimeLabel.setFont(GUI.BASE_FONT);
      totalTimeLabel.setBorder(new EmptyBorder(0, 0, 0, 35));
      p0.add(new JLabel("Czas jazdy: "));
      p0.add(totalTimeLabel);
      
      JLabel totalDistanceLabel = new JLabel();
      totalDistanceLabel.setText("0 km");
      totalDistanceLabel.setFont(GUI.BASE_FONT);
      totalDistanceLabel.setBorder(new EmptyBorder(0, 0, 0, 35));
      p0.add(new JLabel("Dystans: "));
      p0.add(totalDistanceLabel);  
      
      txt = new JLabel(String.format("%.2f", delivery.getTotalCost()) + " l");
      txt.setFont(GUI.BASE_FONT);
      p0.add(new JLabel("    Zu\u017cycie paliwa: "));
      p0.add(txt);
            
      p0.setAlignmentX(JLabel.LEFT);
      p.add(p0);
      p.add(new JLabel("  "));
      
      DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
      centerRenderer.setHorizontalAlignment(JLabel.CENTER);
      DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
      rightRenderer.setHorizontalAlignment(JLabel.RIGHT);      
                
      double totalTime = 0.0;
      double totalDistance = 0.0;
            
      if (!delivery.getDrivers().isEmpty()) {                          
        
        int totalOrdersNumber = 0;  
        
        for (final DeliveryDriver driver : delivery.getDrivers()) {
          
          JPanel tPanel = new JPanel();
          tPanel.setOpaque(false);
          tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.Y_AXIS));    
            
          DeliveryOrdersTableModel model = new  DeliveryOrdersTableModel(driver.getOrders());
                   
          int ordersNumber = model.getAllElementsCount() - (driver.isReturnToDepot() ? 2 : 1);
          totalOrdersNumber += ordersNumber;
          
          totalTime += model.getTotalTime();
          totalDistance += model.getTotalDistance();
          
          JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
          p1.setPreferredSize(new Dimension(950, 40));
          p1.setOpaque(false);
          p1.add(new JLabel("Kierowca:"));
          txt = new JLabel(driver.getDriverDesc());
          txt.setMaximumSize(new Dimension(140, 40));
          txt.setPreferredSize(new Dimension(140, 40));
          txt.setFont(GUI.BASE_FONT);
          txt.setBorder(new EmptyBorder(0, 0, 0, 8));
          p1.add(txt);
          p1.add(new JLabel("Pojazd:"));
          txt = new JLabel(driver.getVehicleDesc());
          txt.setFont(GUI.BASE_FONT);
          txt.setMaximumSize(new Dimension(355, 40));
          txt.setPreferredSize(new Dimension(355, 40));
          txt.setBorder(new EmptyBorder(0, 0, 0, 8));
          p1.add(txt);
          
          p1.add(new JLabel("Powr\u00f3t: "));
          txt = new JLabel(driver.isReturnToDepot() ? "tak" : "nie");
          txt.setFont(GUI.BASE_FONT);
          if (!driver.isReturnToDepot()) txt.setForeground(Color.RED);
          txt.setMaximumSize(new Dimension(55, 40));
          txt.setPreferredSize(new Dimension(55, 40));
          txt.setBorder(new EmptyBorder(0, 0, 0, 15));
          p1.add(txt);
          
          
          // przycisk z podglądem trasy
          if (ordersNumber > 0) {
            JButton mapButton = new JButton("Podgl\u0105d trasy", ImageRes.getIcon("icons/map.png"));
            mapButton.setFocusPainted(false);            
            mapButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
            
                  new MapDialog(frame, driver);
                    
                }
            });            
            p1.add(mapButton);          
          }
          
          tPanel.add(p1);
          
          p1 = new JPanel(new FlowLayout());
          p1.setOpaque(false);
          p1.setPreferredSize(new Dimension(800, 10));
          tPanel.add(p1);
          
          JTable rTable = new JTable(model);
          rTable.setRowSorter(null);
          rTable.setFocusable(false);
          rTable.setRowSelectionAllowed(false);
          rTable.setAutoCreateRowSorter(false);
          rTable.setRowHeight(22); 
          // zmniejszenie i wyśrodkowanie kolumny Lp. [0]
          rTable.getColumnModel().getColumn(0).setWidth(20);
          rTable.getColumnModel().getColumn(0).setMaxWidth(40);          
          rTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
          // zwiększenie kolumny "odbiorca" [1]
          rTable.getColumnModel().getColumn(1).setMinWidth(300);  
          // zmniejszenie i wyrównanie do prawej kolumny "waga" [3] i "ładunek" [4]
          rTable.getColumnModel().getColumn(3).setWidth(80);
          rTable.getColumnModel().getColumn(3).setMaxWidth(100);    
          rTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
          rTable.getColumnModel().getColumn(4).setWidth(80);
          rTable.getColumnModel().getColumn(4).setMaxWidth(100);    
          rTable.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);          
          // wyśrodkowanie kolumn i z czasem [5][6]          
          rTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
          rTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
          // zmniejszenie i wyrównanie do prawej kolumn z odległością [7][8]
          rTable.getColumnModel().getColumn(7).setWidth(80);
          rTable.getColumnModel().getColumn(7).setMaxWidth(100);    
          rTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
          rTable.getColumnModel().getColumn(8).setWidth(80);
          rTable.getColumnModel().getColumn(8).setMaxWidth(100);    
          rTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);     
          // kolor + wyśr na kolumnę z nr zam [2]
          rTable.getColumnModel().getColumn(2).setCellRenderer(new TableStateCellRenderer()); 
          ((DefaultTableCellRenderer)rTable.getColumnModel().getColumn(2).getCellRenderer())
                  .setHorizontalAlignment(JLabel.CENTER);  
           
          scroll = new JScrollPane(rTable, 
                 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      
          scroll.setMaximumSize(new Dimension(980, 320));     
          scroll.setPreferredSize(new Dimension(980, 320));             
          tPanel.add(scroll);
          
          tabPane.add(driver.getVehicleRegistrationNo() + " ("+ String.valueOf(ordersNumber)+")", tPanel);
          
        }
        
        
        // zaktualizowanie etykiet          
        orderNumberLabel.setText(String.valueOf(totalOrdersNumber));  
        totalTimeLabel.setText(Settings.formatTime(totalTime));
        totalDistanceLabel.setText(String.format("%.1f", totalDistance/1000.0) + " km");      
      
 
      }
      
      else  orderNumberLabel.setText("0");  
             

      p.add(tabPane);
        
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(10, 0, 5, 0)); 

      JButton endButton = new JButton("Zako\u0144cz dostaw\u0119");
      endButton.setFocusPainted(false);
      endButton.setEnabled(delivery.isActive());
      endButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
      
            boolean res = new ConfirmDialog(frame, "Czy na pewno zako\u0144czy\u0107 dostaw\u0119 ?").isConfirmed();
            if (res) {
                
              GlossDeliveries gloss = new GlossDeliveries(frame.getDatabaseShared());
              if (gloss.deleteItem(delivery, frame.getUser())) {
              
                new InfoDialog(frame, "Dostawa zosta\u0142a zako\u0144czona.", 140);                
                frame.getDataPanel(GUI.TAB_ORDERS).setChanged(true);
                frame.getDataPanel(GUI.TAB_DELIVERIES).setChanged(true);
                frame.setSelectedDataPanel(GUI.TAB_DELIVERIES);
                ((TableDeliveriesPanel)frame.getActiveDataPanel()).refreshTable();
                dispose();
                
              }
              
              else new ErrorDialog(frame, "B\u0142\u0105d SQL:" + gloss.getLastError(), true);                
              
                
            }
              
          }
      });
      
      p2.add(endButton);
      
      p2.add(new JLabel("  "));
      
      p2.add(new CloseButton("Zamknij"));
            
      p.add(p2);
      
      add(p);
      
   }
   

   @Override
   public void start(IProgress progress) {
       
      JTable parentTable = ((TablePanel) frame.getActiveDataPanel()).getTable();
      DeliveriesTableModel tableModel = (parentTable != null) ? (DeliveriesTableModel)(parentTable.getModel()) : null;     
     
      if (parentTable != null && tableModel != null)
        delivery = tableModel.getElement(parentTable.convertRowIndexToModel(parentTable.getSelectedRow())); 
      
      try {
        tableModel.itemComplete(delivery);       
        progress.hideComponent();
        if (delivery != null) super.showDialog(980, 650);
        else System.err.println("delivery == null");
      }
      catch (SQLException e) {
        progress.hideComponent();
        new ErrorDialog(frame, "B\u0142\u0105d SQL: " + e, true);  
      }        
      catch (NullPointerException e) {
        progress.hideComponent();
        new ErrorDialog(frame, "B\u0142\u0105d: brak danych dostawy", true); 
      }
        
        
   }

    
 
    
}

