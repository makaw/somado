/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import somado.Settings;



/**
 *
 * Szablon obiektu reprezentującego historie zmian obiektu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Audit {
        
  /** Zmienione atrybuty obiektu */
  private AuditDiff diff;
  /** Nazwisko i login uzytkownika */
  private String userName;
  /** Nazwa klasy nadrzednego obiektu */
  private String parentType;  
  /** Identyfikator nadrzednego obiektu */
  private int parentId;
  /** Data zmiany */
  private Date date;
  
  
  
  /**
   * Konstruktor
   * @param objectOld Poprzedni obiekt
   * @param objectNew Obiekt po wykonanych zmianach
   * @param parent Obiekt nadrzedny
   * @param mode Tryb zmiany
   * @param comment Komentarz
   */
  public Audit (Object objectOld, Object objectNew, IData parent, int mode, String comment) {
              
    this.parentId = parent.getId();
    this.parentType = parent.getClass().getSimpleName();
     
    if (mode==AuditDiff.AM_MOD) comment += ": " + parent.toString();
    
    this.date = new Date();
    
    if (objectOld == null) 
      this.diff = new AuditDiff(objectNew, mode, comment);
    else
      this.diff = new AuditDiff(objectOld, objectNew, mode, comment);
      
  }
    
  
  /**
   * Konstruktor przy wpisie dot. dodania nowego obiektu
   * @param object Nowy obiekt
   * @param parent Obiekt nadrzedny
   * @param mode Tryb zmiany
   * @param comment Komentarz
   */  
  public Audit (Object object, IData parent, int mode, String comment) {
      
    this(null, object, parent, mode, comment);
      
  }
  
  
  /**
   * Konstruktor
   * @param rs Uchwyt do wynikow zapytania BD
   * @throws SQLException Blad SQL
   */
  public Audit (ResultSet rs) throws SQLException {
       
    try {
        
      byte[] buf = rs.getBytes("diff");
      ObjectInputStream objectIn = null;
      if (buf != null) objectIn = new ObjectInputStream(new ByteArrayInputStream(buf));
      if (objectIn != null) this.diff = (AuditDiff) (objectIn.readObject());
      
    }
    
    // niekompatybilnosc obiektu (wrong serialVersionUID)
    catch (IOException | ClassNotFoundException e) {
        
      this.diff = new AuditDiff();
        
    }
  
      
    this.userName = rs.getString("user_login");
    this.parentId = rs.getInt("parent_id");
    this.parentType = rs.getString("parent_type");
    
    try {
       this.date = Settings.DATETIME_FORMAT.parse(rs.getString("date_add"));
    } catch (ParseException e) {
       this.date = new Date();      
    }    
      
  }
  
  

  public AuditDiff getDiff() {
     return diff;
  }


  public String getUserLogin() {
     return userName;
  }

    
  public Date getDate() {
      return date;
  }

  public int getParentId() {
      return parentId;
  }

   
  public String getParentType() {
      return parentType;
  }

  

    
}
