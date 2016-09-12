/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;

import datamodel.docs.DocAudit;
import gui.GUI;
import gui.SimpleDialog;
import gui.dialogs.docpanels.AuditPanel;
import java.awt.Color;
import somado.IConf;


/**
 *
 * Okno z pelna historia zmian
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class AuditDialog extends SimpleDialog {

    
   public AuditDialog(GUI frame) {
       
      super(frame,  IConf.APP_NAME + " - Historia zmian");
      super.showDialog(800, 600);       
       
   }
   

  /**
   * Metoda wyświetlająca zawartość okienka
   */    
   @Override
   protected void getContent()  {
       
      add(new AuditPanel(new DocAudit(frame.getDatabaseShared()), new Color(0xddecfa), 
              new SimpleDialog.CloseButton()));
            
   }   
   
    
    
}
