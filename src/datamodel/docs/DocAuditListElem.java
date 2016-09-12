/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.docs;



/**
 *
 * Klasa definiuje poszczegolne wiersze dokumentu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class DocAuditListElem implements IDocRow {
      
   /** Nagłówek */ 
   public String header;
   /** Treść */
   public String text;
     
   /**
    * Konstruktor
    * @param header Nagłówek
    * @param text Treść
    */
   public DocAuditListElem(String header, String text) {
         
      this.header = header;
      this.text = text;
         
   }

    @Override
   public String getHeader() {
   
       return header;
       
   }
   

    @Override
   public String getText() {
       
      return text;
      
   }
   

}
