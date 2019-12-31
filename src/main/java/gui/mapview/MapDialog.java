/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.mapview;


import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import org.jxmapviewer.viewer.GeoPosition;

import datamodel.Customer;
import datamodel.IAddressPoint;
import datamodel.IRoute;
import gui.GUI;
import gui.SimpleDialog;
import gui.dialogs.ErrorDialog;
import somado.IConf;
import somado.Lang;
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
   /** Trasa do wyświetlenia */
   private List<? extends IAddressPoint> route;
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
         new ErrorDialog(frame, Lang.get("Error.MissingTms") + "\n\n" + e, true);  
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
        
      this(frame, Lang.get("Dialogs.MapPreview"), longitude, latitude);  
        
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
    * Konstruktor, mapa z trasą 
    * @param frame Referencja do GUI
    * @param route Kierowca i jego zamówienia
    */     
    public MapDialog(GUI frame, IRoute route) {    
        
      this(frame, Lang.get("Dialogs.RoutePreview") + " " + route.getVehicleRegistrationNo(), 
            route.getAddressPoints().get(0).getCustomerLongitude(), route.getAddressPoints().get(0).getCustomerLatitude(), false);
      this.route = route.getAddressPoints();
      if (!tmsErr) super.showDialog(800, 600);  
        
    }
                  
    
   /**
    * Metoda wyświetlająca zawartość okienka
    */    
    @Override
    protected void getContent()  {
        
       setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
       
       mapPanel = new MapPanel(Settings.getValue("tms_url")); 
       if (route != null) {  
    	   mapPanel.setRoute(route); 
       }       
       else { 
    	   mapPanel.setAddressPoint(centerPosition);  
       }
       
       add(mapPanel); 
       
       add(new JSeparator(JSeparator.HORIZONTAL));
       
       JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER));
       p.setPreferredSize(new Dimension(500, 40));
       p.setBorder(new EmptyBorder(5, 0, 5, 0));
       p.add(new CloseButton(Lang.get("Close")));
       add(p);      
      
    }
    

    public MapPanel getMapPanel() {
        return mapPanel;
    }
    
    
    
 
    
}
