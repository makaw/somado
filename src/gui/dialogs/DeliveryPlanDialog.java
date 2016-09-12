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
import spatial_vrp.Route;
import spatial_vrp.RoutePoint;
import datamodel.tablemodels.OrdersDeliveryPlanTableModel;
import datamodel.tablemodels.RoutePointsTableModel;
import gui.GUI;
import gui.ImageRes;
import gui.SimpleDialog;
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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
public class DeliveryPlanDialog extends EditLockableDataDialog implements IData, IProgressInvoker {
    
   /** Plan dostawy */ 
   private final DeliveryPlan deliveryPlan;
   /** Data dostawy */
   private final Date deliveryDate;
    
   /**
    * Konstruktor,wyświetlenie okienka
    * @param frame Referencja do GUI
    * @param deliveryPlan Obiekt otwieranej dostawy
    * @param deliveryDate Planowana data dostawy
    */     
    public DeliveryPlanDialog(GUI frame, DeliveryPlan deliveryPlan, Date deliveryDate) {
     
      super(frame, IConf.APP_NAME + " - plan nowej dostawy");      
      this.deliveryPlan = deliveryPlan;
      this.deliveryDate = deliveryDate;
      showDialog(980, 600);
        
  }    
    
    
  
  @Override
  protected final void showDialog(int width, int height) {
  
     checkLock(this);
     if (getLock() != null) {
         
       String error = "Nowa dostawa jest obecnie planowana "
              + (getLock().getUserLogin().equals(frame.getUser().getLogin()) ? 
                "w innym otwartym oknie aplikacji\n"
              : "przez innego u\u017cytkownika:\n" + getLock().getUserLogin() + " ")
              + "(start: " + Settings.DATETIME_NS_FORMAT.format(getLock().getDateAdd()) + ").\n\n"
              + "Zatwierdzenie dostawy nie b\u0119dzie mo\u017cliwe.";
        new WarningDialog(frame, error, 220);
        locked = true;
        
      }
      
      super.showDialogNoCheck(width, height);
    
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
        
      JPanel p0 = new JPanel(new GridLayout(1, 2, 50, 0));
      p0.setOpaque(false);
      JLabel txt = new JLabel(Settings.DATE_FORMAT.format(deliveryDate));
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Data dostawy:            ", txt, 160));  
      txt = new JLabel(Settings.formatTime(deliveryPlan.getMaxDriverWorkTime()));      
      txt.setFont(GUI.BASE_FONT);
      FormRowPad formRow = new FormRowPad("Maks. czas jazdy kierowcy:", txt, 240);
      formRow.setMinimumSize(new Dimension(400, 40));
      p0.add(formRow);
         
      p0.setMinimumSize(new Dimension(800, 45));
      p0.setSize(new Dimension(800, 45));
      p0.setAlignmentX(JLabel.LEFT);
      p.add(p0);
      
      p0 = new JPanel(new GridLayout(1, 2, 50, 0));
      p0.setOpaque(false);
      
      int nio = deliveryPlan.getUnassignedOrders().size();
      JLabel orderNumberLabel = new JLabel(" / "+String.valueOf(nio));
      orderNumberLabel.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Zam\u00f3wienia obs\u0142u\u017cone/nieobs\u0142.:", orderNumberLabel, 250));
      
      txt = new JLabel(String.valueOf(deliveryPlan.getDriversNumber()));
      txt.setFont(GUI.BASE_FONT);
      p0.add(new FormRowPad("Liczba zaanga\u017cowanych kierowc\u00f3w:", txt, 280)); 
      
      p0.setMinimumSize(new Dimension(800, 45));
      p0.setSize(new Dimension(800, 45));
      p0.setAlignmentX(JLabel.LEFT);
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
      
      txt = new JLabel(String.format("%.2f", deliveryPlan.getTotalCost()) + " l");
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
      
      if (nio>0) {
          
         JPanel tPanel = new JPanel();
         tPanel.setOpaque(false);
         tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.Y_AXIS));         
          
         OrdersDeliveryPlanTableModel nioModel = new OrdersDeliveryPlanTableModel(deliveryPlan.getUnassignedOrders());
         JTable nioTable = new JTable(nioModel);
         nioTable.setRowSorter(null);
         nioTable.setFocusable(false);
         nioTable.setRowSelectionAllowed(false);
         nioTable.setAutoCreateRowSorter(false);
         nioTable.setRowHeight(22); 
         // zmniejszenie kolumny "data" [0]  i wyśrodkowanie
         nioTable.getColumnModel().getColumn(0).setMinWidth(120);
         nioTable.getColumnModel().getColumn(0).setMaxWidth(130);     
         nioTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
         // zmniejszenie kolumny "numer" [1] i wyśrodkowanie
         nioTable.getColumnModel().getColumn(1).setWidth(90);
         nioTable.getColumnModel().getColumn(1).setMaxWidth(90);
         nioTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
         // zmniejszenie kolumny "waga" [3] i wyr. do prawej
         nioTable.getColumnModel().getColumn(3).setWidth(100);
         nioTable.getColumnModel().getColumn(3).setMaxWidth(120);
         nioTable.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
           
         scroll = new JScrollPane(nioTable, 
                 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      
         scroll.setMaximumSize(new Dimension(980, 320));     
         scroll.setPreferredSize(new Dimension(680, 320));             
         tPanel.add(scroll);
         
         tabPane.add("Nieobs\u0142u\u017cone ("+String.valueOf(nio)+")", tPanel);
         
 
      }
      
      List<Route> routes = deliveryPlan.getRoutes();
      int routesNumber = routes.size();
      double totalTime = 0.0;
      double totalDistance = 0.0;
            
      if (routesNumber>0) {                          
        
        int totalOrdersNumber = 0;  
        
        for (final Route route: routes) {
          
          JPanel tPanel = new JPanel();
          tPanel.setOpaque(false);
          tPanel.setLayout(new BoxLayout(tPanel, BoxLayout.Y_AXIS));    
            
          RoutePointsTableModel rModel = new RoutePointsTableModel(route.getPoints());
                   
          int ordersNumber = rModel.getAllElementsCount() - (route.getDriver().isReturnToDepot() ? 2 : 1);
          totalOrdersNumber += ordersNumber;
          
          totalTime += rModel.getTotalTime();
          totalDistance += rModel.getTotalDistance();
          
          JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
          p1.setPreferredSize(new Dimension(950, 40));
          p1.setOpaque(false);
          p1.add(new JLabel("Kierowca:"));
          txt = new JLabel(route.getDriver().getUserData().getSurname() + " "
                  + route.getDriver().getUserData().getFirstname());
          txt.setMaximumSize(new Dimension(140, 40));
          txt.setPreferredSize(new Dimension(140, 40));
          txt.setFont(GUI.BASE_FONT);
          txt.setBorder(new EmptyBorder(0, 0, 0, 8));
          p1.add(txt);
          p1.add(new JLabel("Pojazd:"));
          txt = new JLabel("[max " + String.format("%.2f", route.getDriver().getVehicle().getVehicleModel().getMaximumLoad()) + "t] "
                  + route.getDriver().getVehicle().toString());
          txt.setFont(GUI.BASE_FONT);
          txt.setMaximumSize(new Dimension(355, 40));
          txt.setPreferredSize(new Dimension(355, 40));
          txt.setBorder(new EmptyBorder(0, 0, 0, 8));
          p1.add(txt);
          
          p1.add(new JLabel("Powr\u00f3t: "));
          txt = new JLabel(route.getDriver().isReturnToDepot() ? "tak" : "nie");
          txt.setFont(GUI.BASE_FONT);
          if (!route.getDriver().isReturnToDepot()) txt.setForeground(Color.RED);
          txt.setMaximumSize(new Dimension(55, 40));
          txt.setPreferredSize(new Dimension(55, 40));
          txt.setBorder(new EmptyBorder(0, 0, 0, 15));
          p1.add(txt);
          
          // dodanie etykiet punktów odbioru na mapie
          Iterator<RoutePoint> iterator = route.getPoints().iterator();
          while (iterator.hasNext()) {
            RoutePoint tmp = iterator.next();
            tmp.setLabel(rModel.getLabel(tmp.getOrder().getCustomer().getId()));
          }
          
          // przycisk z podglądem trasy
          if (ordersNumber > 0) {
            JButton mapButton = new JButton("Podgl\u0105d trasy", ImageRes.getIcon("icons/map.png"));
            mapButton.setFocusPainted(false);            
            mapButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
            
                  new MapDialog(frame, route);
                    
                }
            });            
            p1.add(mapButton);          
          }
          
          tPanel.add(p1);
          
          p1 = new JPanel(new FlowLayout());
          p1.setOpaque(false);
          p1.setPreferredSize(new Dimension(800, 10));
          tPanel.add(p1);
          
          JTable rTable = new JTable(rModel);
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
          // wyśrodkowanie kolumn z numerem zam. [2] i z czasem [5][5]
          rTable.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
          rTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
          rTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
          // zmniejszenie i wyrównanie do prawej kolumn z odległością [7][8]
          rTable.getColumnModel().getColumn(7).setWidth(80);
          rTable.getColumnModel().getColumn(7).setMaxWidth(100);    
          rTable.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
          rTable.getColumnModel().getColumn(8).setWidth(80);
          rTable.getColumnModel().getColumn(8).setMaxWidth(100);    
          rTable.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);          
           
          scroll = new JScrollPane(rTable, 
                 JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      
          scroll.setMaximumSize(new Dimension(980, 320));     
          scroll.setPreferredSize(new Dimension(980, 320));             
          tPanel.add(scroll);
          
          tabPane.add(route.getDriver().getVehicle().getRegistrationNo() 
                  + " ("+ String.valueOf(ordersNumber)+")", tPanel);
          
        }
        
        
        // zaktualizowanie etykiet          
        orderNumberLabel.setText(String.valueOf(totalOrdersNumber) + orderNumberLabel.getText());  
        totalTimeLabel.setText(Settings.formatTime(totalTime));
        totalDistanceLabel.setText(String.format("%.1f", totalDistance/1000.0) + " km");      
      
 
      }
      
      else  orderNumberLabel.setText("0" + orderNumberLabel.getText());  
             

      p.add(tabPane);
        
           
      JButton saveButton = new JButton("Zatwierd\u017a dostaw\u0119");
      saveButton.setEnabled(!locked && routesNumber > 0);
      saveButton.setFocusPainted(false);
      saveButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
       
           boolean res =
              new ConfirmDialog(frame, "Czy na pewno zatwierdzi\u0107 now\u0105 dostaw\u0119 ?", 140).isConfirmed();
           
           if (res) new Loader(frame, DeliveryPlanDialog.this, false).load();
              
         }
      });
      
      
      JButton againButton = new JButton("Planuj ponownie");
      againButton.setFocusPainted(false);
      againButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
       
           dispose();
           new DeliveryOpenDialog(frame);             
             
         }
      });

      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(10, 0, 5, 0)); 

      p2.add(saveButton);
      p2.add(new JLabel(" "));
      p2.add(againButton);
      p2.add(new JLabel(" "));
      p2.add(new CloseButton("Anuluj"));
            
      p.add(p2);
      
      add(p);
      
   }

    @Override
    public void start(IProgress progress) {
      
      try {
        deliveryPlan.savePlan(deliveryDate, frame.getUser());
        progress.hideComponent();
        new InfoDialog(frame, "Nowa dostawa zosta\u0142a zatwierdzona.", 140);
        frame.getDataPanel(GUI.TAB_ORDERS).setChanged(true);
        frame.getDataPanel(GUI.TAB_DELIVERIES).setChanged(true);
        frame.setSelectedDataPanel(GUI.TAB_DELIVERIES);
        ((TableDeliveriesPanel)frame.getActiveDataPanel()).refreshTable();
        dispose();
      }  
      catch (SQLException ex) {
        progress.hideComponent();
        System.err.println("B\u0142\u0105d SQL: " + ex);
        new ErrorDialog(frame, "B\u0142\u0105d SQL: " + ex, true);
      } 
        
    }

    
    @Override
    public Integer getId() {  return 1; }  
    
}

