/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs.docpanels;


import datamodel.docs.IDocRow;
import datamodel.docs.IEditableDoc;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Iterator;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;


/**
 *
 * Szablon obiektu panelu ze stronicowana lista wpisow (dokumentem)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <K> Klasa wiersza dokumentu
 * @param <V> Klasa kompletnego obiektu przedstawianego przez wiersz dokumentu 
 * 
 */
@SuppressWarnings("serial")
public class DialogDocPanel<K extends IDocRow, V> extends JPanel {
    
  /** Glowne pole tekstowe */  
  private final JTextPane tx;  
  /** Dokument dla pola tekstowego */
  private final StyledDocument styledDocument;  
  /** Styl tekstu */
  private final Style style;
  /** Obiekt dostarczajacy tresc */
  private final IEditableDoc<K, V> doc;
  
  
  /**
   * Konstruktor
   * @param doc Obiekt dostarczajacy tresc 
   * @param bgColor Kolor tla okna tekstowego
   * @param closeButton Komponenent przycisku zamykajacego okno
   */
  public DialogDocPanel(final IEditableDoc<K, V> doc, Color bgColor, JComponent closeButton) {
            
     
     this.doc = doc;   
     setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));      
     final JPanel pAudit = new JPanel(new GridLayout(1,1));
     pAudit.setOpaque(false); 
     pAudit.setPreferredSize(new Dimension(800, 800));
     tx = new JTextPane();
     tx.setEditable(false);
       
     // pozycja kursora na poczatku pola tekstowego
     ((DefaultCaret)tx.getCaret()).setUpdatePolicy(DefaultCaret.NEVER_UPDATE);

     tx.setBackground(bgColor);
     tx.setBorder(null);

     styledDocument =  tx.getStyledDocument();  
       
     style = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
     StyleConstants.setFontSize(style, 12);
     StyleConstants.setForeground(style, Color.BLACK);
     
     StyleConstants.setBackground(style, tx.getBackground());
      
     addText();

     pAudit.add(tx);
      
     // dodanie paska przewijania
     JScrollPane sc = new JScrollPane(tx, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
     sc.setBorder(null);
     pAudit.add(sc);      
     
     add(pAudit);
     
     int pages = (int) Math.round(Math.ceil((doc.getRowsCount()-1)/doc.getDbLimit())) + 1;
     PaginationPanel p = new PaginationPanel(pages, closeButton) {

          @Override
          protected void refresh(int direction) {
            try {       
              styledDocument.remove(0, styledDocument.getLength());
            } catch (BadLocationException e) {
              System.err.println(e.getMessage());
            }
            if (direction>0) doc.nextPage();
            else doc.prevPage();
            addText();
          }
                              
      };
      
      p.addListeners();
      p.setRowsCountLabelText("Wpis\u00f3w: " + String.valueOf(doc.getRowsCount())); 
      
      add(new JSeparator(JSeparator.HORIZONTAL));
      add(p);     
      
      
  }
  
  
  /**
   * Konstruktor bez przycisku zamykania okna
   * @param doc Obiekt dostarczajacy tresc 
   * @param bgColor Kolor tla okna tekstowego
   */  
  public DialogDocPanel(final IEditableDoc<K, V> doc, Color bgColor) {
      
    this(doc, bgColor, null);  
      
  }
  
  
  /**
   * Metoda wyswietla tekst na panelu
   */
  private void addText() {
      
     String br =  System.getProperty("line.separator");
     
     try {         
         styledDocument.insertString(styledDocument.getLength(), br, style);                       
     } catch(BadLocationException e) {
         System.err.println(e.getMessage());
     }          
      
     // umieszczenie tekstu
     Iterator<K> docIterator = doc.getDocumentText().iterator();
     while (docIterator.hasNext()) {
        
       try {

         K tmp = docIterator.next();
         StyleConstants.setBold(style, true);
         styledDocument.insertString(styledDocument.getLength(), tmp.getHeader() + br, style);
         StyleConstants.setBold(style, false);
         styledDocument.insertString(styledDocument.getLength(), tmp.getText() + br + br, style);            
    
       }
       catch(BadLocationException e) {
         System.err.println(e.getMessage());
       }     
    
     }      
      
      
  }
  

}
