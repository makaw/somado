/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.docs;


import datamodel.Audit;
import datamodel.IData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import somado.Database;
import somado.Settings;
import somado.User;


/**
 *
 * Szablon obiektu reprezentującego dokument historii zmian obiektu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class DocAudit implements IEditableDoc<DocAuditListElem, Audit> {
    
    
  /** Referencja do obiektu bazy danych */
  private final Database database;
  /** Klucz audytowanego obiektu */
  private int parentId;
  /** Typ (nazwa klasy) audytowanego obiektu */
  private String parentType;
  /** Lista wpisow audytowych (wierszy dokumentu) */
  protected final List<Audit> audits;
  /** komunikat ostatniego bledu (operacje DB) */
  private String lastError;  
  private final int dbLimit;
  private int rowsCount = 0;
  private int startPage = 0;
  
  
  /**
   * Konstruktor
   * @param database Ref. do globalnej bazy danych
   * @param parent Audytowany obiekt
   */
  public DocAudit(Database database, IData parent) {
      
    this.database = database;      
    this.dbLimit = Settings.getIntValue("items_per_page_doc");   
    try {
      this.parentId = parent.getId();
      this.parentType = parent.getClass().getSimpleName();
    }
    catch (NullPointerException e) {
      this.parentId = 0;
      this.parentType = null;  
    }
    
    audits = new ArrayList<>();
           
  }    

  /**
   * Konstruktor
   * @param database Referencja do globalnej bazy danych
   */
  public DocAudit(Database database) {
            
    this(database, null);
      
  }
  


  /**
   * Metoda pobiera zawartosc dokumentu (kolejne linie)
   * @return Lista wierszy dokumentu
   */  
  @Override
  public List<DocAuditListElem> getDocumentText() {
      
    List<DocAuditListElem> doc = new ArrayList<>();
    PreparedStatement ps;
    ResultSet rs;
    
    audits.clear();

    try {

      if (parentId>0) {
          
        ps = database.prepareQuery("SELECT COUNT(*) AS cnt FROM dat_audit WHERE parent_id = ? AND parent_type = ?");
        ps.setInt(1, parentId);
        ps.setString(2, parentType);  
        rs = ps.executeQuery();
                  
        ps = database.prepareQuery("SELECT * FROM dat_audit WHERE parent_id = ? "
                + " AND parent_type = ? ORDER BY date_add DESC LIMIT ?, ?");
        ps.setInt(1, parentId);
        ps.setString(2, parentType);
        ps.setInt(3, startPage*dbLimit);
        ps.setInt(4, dbLimit);
        
      }
      
      else {
          
        rs = database.doQuery("SELECT COUNT(*) AS cnt FROM dat_audit;");        
          
        ps = database.prepareQuery("SELECT * FROM dat_audit ORDER BY date_add DESC LIMIT ?, ?");
        ps.setInt(1, startPage*dbLimit);
        ps.setInt(2, dbLimit); 
          
      }
      
      if (rs.next()) rowsCount = rs.getInt("cnt");
      rs = ps.executeQuery();

      while (rs.next()) {
             
         audits.add(new Audit(rs));
             
      }
         
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
        
    } 
    
    // umieszczenie tekstu
    Iterator<Audit> docIterator = audits.iterator();
    while (docIterator.hasNext()) {
    
       Audit tmp = docIterator.next();
       doc.add(new DocAuditListElem(Settings.DATETIME_NS_FORMAT.format(tmp.getDate()) + " ["
                  + tmp.getUserLogin() + "] " + tmp.getDiff().getComment(),  tmp.getDiff().toString()));
    
    }

      
    return doc;
    
      
  }  
  
  
  
  /**
   * Metoda zwraca wskazany wiersz dokumentu
   * @param elementId indeks wiersza
   * @return Wskazany wiersz
   */      
    @Override
  public Audit getElement(int elementId) {
      
     return audits.get(elementId); 
      
  }
    
  

  
 /**
  * Metoda dodaje nowy wiersz do dokumentu
  * @param audit Nowy element
  * @param user ref. do obiektu zalogowanego uzytkownika
  * @return true jezeli OK
  */
  @Override
  public boolean addElement(Audit audit, User user) {
      
   if (audit.getDiff().getDiff().isEmpty()) return false;
    
    
    try {

       PreparedStatement ps = database.prepareQuery("INSERT INTO dat_audit (id, parent_id, " 
                 + "parent_type, diff, user_login, user_add_id, date_add) VALUES " 
                 + "(NULL, ?, ?, ?, ?, ?, NOW());", true);
       ps.setInt(1, audit.getParentId());
       ps.setString(2, audit.getParentType());
       ps.setObject(3, audit.getDiff());
       ps.setString(4, user.getLogin());
       ps.setInt(5, user.getId());
           
       ps.executeUpdate();

       
      } catch (SQLException e) {
      
        System.err.println("B\u0142\u0105d SQL: "+e);
        lastError = "B\u0142\u0105d SQL: "+e.getMessage();
        return false;
       
      }    
    
      return true;
    
  }


  /**
   * Metoda zwraca ostatni blad (logika lub BD)
   * @return Komunikat ostatniego bledu
   */    
    @Override
  public String getLastError() {
        
     return lastError;
       
  }
  
  /**
   * Metoda zwraca laczna ilosc wierszy
   * @return Laczna ilosc wierszy
   */
  @Override
  public int getRowsCount() {
      
     return rowsCount;  
      
  }
  
  /**
   * Metoda zwraca aktualny nr strony
   * @return Aktualny nr strony
   */
  @Override
  public int getStartPage() {
      
     return startPage;  
      
  }
  
  
  /**
   * Metoda zwraca limit wierszy na strone
   * @return Limit wierszy na strone
   */
  @Override
  public int getDbLimit() {
      
    return dbLimit;  
      
  }
  
  
  /**
   * Przejscie do nastepnej strony
   */
  @Override
  public void nextPage() {
     
     if (startPage*dbLimit < rowsCount) startPage++; 
      
  }
  
  
  /**
   * Przejscie do poprzedniej strony
   */
  @Override
  public void prevPage() {
     
     if (startPage>0) startPage--; 
      
  }  
 
  
    
}
