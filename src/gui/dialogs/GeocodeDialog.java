/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import datamodel.GeoAddress;
import gui.SimpleDialog;
import gui.GUI;
import gui.formfields.FormRowPad;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import gui.mapview.MapDialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import somado.Database;
import somado.DatabaseLocal;
import somado.IConf;
import spatial_vrp.Geocoding;



/**
 *
 * Szablon obiektu wywołującego geokodujące okienko dialogowe z wyborem wyniku geokodowania
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class GeocodeDialog extends SimpleDialog implements IProgressInvoker {
    
   /** Pole wyboru rekordu */
   private JList<GeoAddress> addrList;   
   /** Czy dokonano wyboru */
   private boolean selected;   
   /** Zapytanie geokodowania */
   private final String request;   
   /** Uproszczone zapytanie geokodowania (tylko miasto) */
   private final String simpleRequest;   
   /** Lista odpowiedzi usługi geokodowania */ 
   List<GeoAddress> addresses = null;
   /** Czy geokodowanie przebiegło poprawnie */
   boolean isDone = true;
   
   
   /**
    * Konstruktor, wywołanie konstruktora klasy nadrzędnej i wyświetlenie okienka
    * @param frame Referencja do interfejsu GUI
    * @param request Zapytanie geokodowania
    * @param simpleRequest Uproszczone zapytanie geokodowania (tylko miasto)
    */ 
   public GeocodeDialog(GUI frame, String request, String simpleRequest) {
        
     super(frame, IConf.APP_NAME + "- wyniki geokodowania");
     selected = false;
     this.request = request;    
      
     this.simpleRequest = simpleRequest;
     
     new Loader(frame, this, false).load();       
     if (isDone)  super.showDialog(650, 250);
            
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
        
      JLabel txt = new JLabel(request);
      txt.setFont(GUI.BASE_FONT);
      p.add(new FormRowPad("Zapytanie: ", txt));      
           
      DefaultListModel<GeoAddress> addrModel = new DefaultListModel<>();
      
      Iterator<GeoAddress> iterator = addresses.iterator();
      while (iterator.hasNext()) {
         addrModel.addElement(iterator.next());
      }
      
      
      addrList = new JList<>(addrModel);
      addrList.setFont(GUI.BASE_FONT);
      addrList.setVisibleRowCount(3);
    
      JScrollPane scroll = new JScrollPane(addrList);
      scroll.setPreferredSize(new Dimension(450, 120));
      scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
      scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
     
      p.add(new FormRowPad("Wyniki:", scroll));  
      
      final JButton mapButton = new JButton("Zobacz na mapie");
      mapButton.setEnabled(false);
      mapButton.setFocusPainted(false);
      mapButton.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
             
            new MapDialog(frame, addrList.getSelectedValue().getLongitude(), 
                    addrList.getSelectedValue().getLatitude());
              
          }
      });
      
      
      final JButton chooseButton = new JButton("Wybierz lokalizacj\u0119");
      chooseButton.setEnabled(false);
      chooseButton.setFocusPainted(false);
      chooseButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(final ActionEvent e) {   
           
            selected = true;
            dispose();
             
         }
      });
      
      addrList.addListSelectionListener(new ListSelectionListener() {
          @Override
          public void valueChanged(ListSelectionEvent e) {
            chooseButton.setEnabled(!addrList.isSelectionEmpty());
            mapButton.setEnabled(!addrList.isSelectionEmpty());
          }
      });
            
      JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(10, 0, 5, 0)); 
      p2.add(mapButton);
      p2.add(new JLabel(" "));
      p2.add(chooseButton);
      p2.add(new JLabel(" "));
      p2.add(new CloseButton("Anuluj"));
            
      p.add(p2);
      
      add(p);      
      
   }


   /**
    * Czy dokonano wyboru
    * @return True jeżeli tak
    */
   public boolean isSelected() {
       
      return selected;
        
   }
    

   /**
    * Metoda zwraca wybrany obiekt
    * @return Wybrany adres
    */
   public GeoAddress getSelectedAddress() {
       
     return addrList.getSelectedValue();
       
   }

   @Override
   public void start(IProgress progress) {
       
     Database database = null;
       
     try {
         
       Thread.sleep(10);   
       database = DatabaseLocal.getInstance();   
       addresses = new Geocoding(database, request).getResponse();
       if (addresses.isEmpty()) {
         Thread.sleep(500);
         addresses = new Geocoding(database, simpleRequest).getResponse();
       }
             
       if (addresses.isEmpty()) throw new Exception("brak danych");       
       progress.hideComponent();   
                  
     }      
       
     catch (Exception ex) {
              
        progress.hideComponent(); 
        if (!(ex instanceof InterruptedException)) {
           System.err.println(ex);
           new ErrorDialog(frame, "Wyst\u0105pi\u0142 b\u0142\u0105d: " + ex.getMessage(), true);        
        }
        dispose();
        isDone = false;
        
     }        
     
     try {
       database.close();
     }
     catch (Exception e) {}
             
        
   }

   
   
}

