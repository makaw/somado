/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.glossaries;

import datamodel.Audit;
import datamodel.AuditDiff;
import datamodel.Delivery;
import datamodel.OrderState;
import datamodel.docs.DocAudit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import somado.Database;
import somado.User;

/**
 *
 * Szablon obiektu "słownika" dostaw - dodatkowe operacje BD na zapisanych dostawach
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossDeliveries extends GlossaryAdapter<Delivery> {
     
  
  /**
   * Konstruktor
   * @param database Ref. do DB
   */
  public GlossDeliveries(Database database) {
      
    super(database);
           
  }
      
  
  /**
   * Metoda "usuwa" element ze słownika, czyli zamyka dostawę
   * @param delivery Obiekt dostawy
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @return true jezeli OK
   */  
  @Override
  public boolean deleteItem(Delivery delivery, User user) {      
      
    if (delivery == null || !delivery.isActive()) return false;
    
    try {
           
      database.doUpdate("START TRANSACTION;");
    
      PreparedStatement ps = database.prepareQuery("UPDATE dat_deliveries SET active = 0, date_end = NOW()"
              + " WHERE id = ?;");
      ps.setInt(1, delivery.getId());
      ps.executeUpdate();
    
      ps = database.prepareQuery("SELECT id FROM dat_orders WHERE delivery_id = ? AND state_id = ?");
      ps.setInt(1, delivery.getId());
      ps.setInt(2, OrderState.DELIVERY.getId());
      ResultSet rs = ps.executeQuery();
    
      while (rs.next()) {
        
        ps = database.prepareQuery("UPDATE dat_orders SET delivery_id=0, state_id = ? WHERE id = ?;");
        ps.setInt(1, OrderState.NEW.getId());
        ps.setInt(2, rs.getInt("id"));
        ps.executeUpdate();
        
      }
      
      database.doUpdate("COMMIT;");            
      
      Audit audit = new Audit(delivery, delivery, AuditDiff.AM_ADD, "Zamkni\u0119to dostaw\u0119");
      (new DocAudit(database, delivery)).addElement(audit, user); 
        
    }
    
    catch (SQLException ex) {
        
      try {  
         database.doUpdate("ROLLBACK;");
      }
      catch (SQLException ex2) {  System.err.println(ex2); }
      
      lastError = ex.getMessage();
      return false;
        
    }
      
     
    return true;
    
      
  }
  
  
    
}
