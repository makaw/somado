/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.gloss;

import datamodel.IDataEditable;
import datamodel.docs.DocAudit;
import gui.GUI;
import gui.dialogs.docpanels.AuditPanel;
import gui.formfields.FormTabbedPane;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;



/**
 *
 * Szablon zawartosci okienka do edycji/dodawania elementow slownika 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class GlossFormPanel extends JPanel {
      
     /** Kolor tla aktywnej zakladki */
     protected final Color bgColor = new Color(0xddecfa);
     /** Panel z danymi */
     protected final JPanel dataTabPane;
      
     
     /**
      * Konstruktor
      * @param frame Referencja do GUI
      * @param dataTabTitle Naglowek zakladki z edycja danych
      * @param object obiekt elementu slownika
      */
     GlossFormPanel(GUI frame, String dataTabTitle, IDataEditable object) {
         
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        JTabbedPane tabPane = new FormTabbedPane(bgColor);        
        dataTabPane =  (JPanel)tabPane.add(dataTabTitle, new JPanel());
        JPanel auditTabPane = (object.getId()!=0) ? 
               (JPanel)tabPane.add("Historia zmian", new JPanel()) : new JPanel();       
  
                
        dataTabPane.add(new JLabel(" "));                          
        
        if (frame.getUser().isAdmin() && object.getId()!=0) {          
          auditTabPane.add(new JLabel(" "));        
          auditTabPane.add(new AuditPanel(new DocAudit(frame.getDatabaseShared(), object), bgColor));
        }
        
        add(tabPane);
        
     }               
     
     
}
