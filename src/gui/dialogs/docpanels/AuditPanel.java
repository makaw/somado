/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.docpanels;

import datamodel.Audit;
import datamodel.docs.DocAuditListElem;
import datamodel.docs.IEditableDoc;
import java.awt.Color;
import javax.swing.JComponent;


/**
 *
 * Szablon obiektu panelu ze stronicowana lista wpisow audytowych 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class AuditPanel extends DialogDocPanel<DocAuditListElem, Audit> {

    public AuditPanel(IEditableDoc<DocAuditListElem, Audit> doc, Color bgColor, JComponent closeButton) {
       super(doc, bgColor, closeButton);
    }
    
     public AuditPanel(IEditableDoc<DocAuditListElem, Audit> doc, Color bgColor) {
       super(doc, bgColor);  
     }
    
}
