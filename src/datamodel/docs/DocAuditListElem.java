/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
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
