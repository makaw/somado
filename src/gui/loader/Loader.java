/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.loader;


import gui.GUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingWorker;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;


/**
 *
 * Wykonanie operacji z paskiem postępu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Loader extends SwingWorker<Boolean, Object> {
    
   /** Okno z paskiem postępu */ 
   private final ProgressDialog progressDialog; 
   /** Interfejs klasy wykonującej operację */
   private final IProgressInvoker invoker;
   
   
   /**
    * Konstruktor
    * @param frame Ref. do GUI
    * @param title Nagłówek okienka z paskiem postępu
    * @param invoker Interfejs klasy wykonującej operację
    * @param determinate Czy pokazywać postęp procentowo
    */
   public Loader(GUI frame, String title, final IProgressInvoker invoker, boolean determinate) {
       
     this.invoker = invoker;   

     progressDialog = new ProgressDialog(frame, title, !determinate);
     
     // zamknięcie okna "krzyżykiem"
     ((JDialog)progressDialog).addWindowListener(new WindowAdapter() {

         @Override
         public void windowClosing(WindowEvent arg0) {
            cancel(true);
         }

      });
     
     // przycisk "anuluj" z okna z paskiem postępu
     progressDialog.getCancelButton().addActionListener(new ActionListener() {

         @Override
         public void actionPerformed(ActionEvent e) {
             cancel(true);
         }
     });
        
             
   }
    

   
   /**
    * Konstruktor
    * @param frame Ref. do GUI
    * @param invoker Interfejs klasy wykonującej operację
    * @param determinate Czy pokazywać postęp procentowo
    */
   public Loader(GUI frame, final IProgressInvoker invoker, boolean determinate) {
       
     this(frame, "Trwa wykonywanie \u017c\u0105danej operacji", invoker, determinate);
     
   }
   
   
    
   /**
    * Wykonywanie operacji w tle
    * @return True jeżeli zakończone
    */
   @Override
   protected Boolean doInBackground() {
     
     try {
       invoker.start(progressDialog);
     }
     catch (Exception e) {
       cancel(true);  
       System.err.println(e);  
     }
     
     return true;

   }
         
   
   /**
    * Koniec, schowanie wskaźnika postępu
    */
   @Override
   protected void done() {

     progressDialog.hideComponent();     
     
   }      
   
   /**
    * Start
    */ 
   public void load() {
    
     execute();
     progressDialog.showComponent();
       
   }
     
    
}

