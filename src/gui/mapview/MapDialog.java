/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.mapview;


import datamodel.Customer;
import datamodel.DeliveryDriver;
import datamodel.DeliveryDriverOrder;
import spatial_vrp.Route;
import spatial_vrp.RoutePoint;
import gui.GUI;
import gui.SimpleDialog;
import gui.dialogs.ErrorDialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;
import org.jxmapviewer.viewer.GeoPosition;
import somado.IConf;
import somado.Settings;
import somado.Somado;


/**
 *
 * Okienko dialogowe z mapą
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MapDialog extends SimpleDialog {
    
   /** Wskazany centralny punkt na mapie */ 
   private final GeoPosition centerPosition;    
   /** Panel mapy */
   private MapPanel mapPanel;   
   /** Trasa do wyświetlenia dla planu dostawy */
   private List<RoutePoint> planRoute = null;
   /** Trasa do wyświetlenia dla zatwierdzonej dostawy */
   private List<DeliveryDriverOrder> orders = null;
   /** Czy wystąpił błąd połączenia z TMS */
   private boolean tmsErr = false;
   
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param title Nagłówek okna
    * @param longitude Długość geograficzna centralnego punktu mapy
    * @param latitude Szerokość geofraficzna centralnego punktu mapy
    * @param show True jeżeli wyświetlić okienko
    */     
    public MapDialog(GUI frame, String title, double longitude, double latitude, boolean show) {
     
      super(frame, IConf.APP_NAME + " - " + title); 
      this.centerPosition = new GeoPosition(latitude, longitude);
                   
      // sprawdzenie dostepnosci uslugi TMS
      try{
         Somado.testTMS();   
       } catch (Exception e){
           
         System.err.println(e + "\nURL: " + Settings.getValue("tms_url"));
         new ErrorDialog(frame, "Nie mo\u017cna po\u0142\u0105czy\u0107 si\u0119 z us\u0142ug\u0105 TMS. "
               + "Mapy s\u0105 niedost\u0119pne.\n\n" + e, true);  
         tmsErr = true;
         return;
         
       } 
      
       if (show) super.showDialog(640, 480);                
        
    }    
    
    
   /**
    * Konstruktor, mapa z punktem
    * @param frame Referencja do GUI
    * @param title Nagłówek okna
    * @param longitude Długość geograficzna centralnego punktu mapy
    * @param latitude Szerokość geofraficzna centralnego punktu mapy
    */     
    public MapDialog(GUI frame, String title, double longitude, double latitude) {
        
      this(frame, title, longitude, latitude, true);  
        
    }    
   
    
   
   /**
    * Konstruktor, mapa z punktem
    * @param frame Referencja do GUI
    * @param longitude Długość geograficzna centralnego punktu mapy
    * @param latitude Szerokość geofraficzna centralnego punktu mapy
    */     
    public MapDialog(GUI frame, double longitude, double latitude) {
        
      this(frame, "podgl\u0105d mapy", longitude, latitude);  
        
    }
    
    
   /**
    * Konstruktor, mapa z punktem (lokalizacja odbiorcy towaru)
    * @param frame Referencja do GUI
    * @param customer Odbiorca towaru
    */     
    public MapDialog(GUI frame, Customer customer) {
        
      this(frame, customer.toString(), customer.getLongitude(), customer.getLatitude());
        
    }    
    
    
   /**
    * Konstruktor, mapa z trasą (dla planu dostawy)
    * @param frame Referencja do GUI
    * @param route Trasa (lista punktów trasy)
    */     
    public MapDialog(GUI frame, Route route) {    
        
      this(frame, "podgl\u0105d trasy " + route.getDriver().getVehicle().getRegistrationNo(),
            route.getPoints().get(0).getOrder().getCustomer().getLongitude(),
            route.getPoints().get(0).getOrder().getCustomer().getLatitude(), false);
      this.planRoute = route.getPoints();
      if (!tmsErr) super.showDialog(800, 600);  
        
    }
    
    
   /**
    * Konstruktor, mapa z trasą (dla zatwierdzonej dostawy)
    * @param frame Referencja do GUI
    * @param deliveryDriver Kierowca i jego zamówienia
    */     
    public MapDialog(GUI frame, DeliveryDriver deliveryDriver) {    
        
      this(frame, "podgl\u0105d trasy " + deliveryDriver.getVehicleRegistrationNo(), 
            deliveryDriver.getOrders().get(0).getCustomerLongitude(), 
            deliveryDriver.getOrders().get(0).getCustomerLatitude(), false);
      this.orders = deliveryDriver.getOrders();
      if (!tmsErr) super.showDialog(800, 600);  
        
    }
              
    
    
   /**
    * Metoda wyświetlająca zawartość okienka
    */    
    @Override
    protected void getContent()  {
        
       setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
       
       mapPanel = new MapPanel(Settings.getValue("tms_url"), centerPosition);          
       if (planRoute != null) mapPanel.setRoute(planRoute);  
       else if (orders != null) mapPanel.setFinalRoute(orders);
       else mapPanel.setAddressPoint(centerPosition);  
       
       add(mapPanel); 
       
       add(new JSeparator(JSeparator.HORIZONTAL));
       
       JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
       p.setPreferredSize(new Dimension(500, 40));
       p.setBorder(new EmptyBorder(5, 0, 5, 0));
       p.add(new CloseButton("Zamknij"));
       add(p);      
      
    }
    

    public MapPanel getMapPanel() {
        return mapPanel;
    }
    
    
    
 
    
}
