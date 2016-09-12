/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.glossaries;


import datamodel.UserData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import somado.Database;
import somado.UserRole;


/**
 *
 * Szablon obiektu reprezentującego słownik użytkowników - kierowców
 * nieprzyporządkowanych do tabeli kierowców
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class GlossUserDataDrivers extends Glossary<UserData> {

    
  /**
   * Konstruktor
   * @param database Ref. do BD
   */
  public GlossUserDataDrivers(Database database) {
      
    super(database);
    
  }    

      
    
  /**
   * Metoda zwraca domyslny (pierwszy na liscie) element slownika
   * @return Domyslny element slownika
   */
  @Override
  public UserData getDefaultItem() {
      
      return null;
      
  }
  

  

  /**
   * Metoda pobiera model dla komponentu JComboBox
   * @return Model dla komponentu JComboBox
   */  
  @Override
  public DefaultComboBoxModel<UserData> getListModel() {
      

    DefaultComboBoxModel<UserData> listModel = new DefaultComboBoxModel<>();
      
    items.clear();

    try {
        
      PreparedStatement ps = database.prepareQuery("SELECT u.* FROM sys_users AS u  WHERE u.role = ? "
              + "AND (SELECT COUNT(*) FROM dat_drivers WHERE user_id=u.id)=0;");
      ps.setInt(1, UserRole.DRIVER.getId());
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
             
         items.add(new UserData(rs));
             
      }
         
    } catch (SQLException e) {
       
        System.err.println("B\u0142\u0105d SQL: "+e);
       
    }         
    
    Iterator<UserData> iterator = items.iterator();
    while (iterator.hasNext())  listModel.addElement(iterator.next());
      
    return listModel; 
    
      
  }  
  
  
  @Override
  public DefaultComboBoxModel<UserData> getListModel(Map<String, String> params) {

      return getListModel();
      
  }
  
 
  
    
}
