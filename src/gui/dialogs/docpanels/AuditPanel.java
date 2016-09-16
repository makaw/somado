/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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
