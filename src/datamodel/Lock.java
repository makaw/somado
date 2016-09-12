/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import somado.Somado;
import somado.Settings;
import somado.User;


/**
 *
 * Szablon obiektu reprezentującego blokade rekordu w BD
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Lock implements IData {
    
  /** ID rekordu w BD blokowanego obiektu */  
  private int parentId;
  /** Nazwa  klasy blokowanego obiektu */
  private String parentType;
  /** Login użytkownika */
  private String userLogin;
  /** Identyfikator procesu systemowego i hosta */
  private String pid;
  /** Data i godzina założenia blokady */
  private Date dateAdd;
  
  /**
   * Konstruktor
   * @param object Edytowany obiekt (rekord do zablokowania)
   * @param user Obiekt zalogowanego użytkownika
   */
  public Lock(IData object, User user) {
      
    parentId = object.getId();
    parentType = object.getClass().getSimpleName();
    userLogin = user.getLogin();
    pid = Somado.APP_PID;
    dateAdd = new Date();
      
  }
  
  
  /**
   * Konstruktor obiektu blokady (z BD)
   * @param rs Zestaw wyników zapytania do BD
   * @throws SQLException Błąd SQL
   */
  public Lock(ResultSet rs) throws SQLException {
      
    parentId = rs.getInt("parent_id");
    parentType = rs.getString("parent_type");
    userLogin = rs.getString("user_login");
    pid = rs.getString("pid");
    try {
       dateAdd = Settings.DATETIME_FORMAT.parse(rs.getString("date_add"));      
    } catch (ParseException e) {
       dateAdd = new Date();
    }
      
  }
  
  

    public int getParentId() {
        return parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getPid() {
        return pid;
    }

    public Date getDateAdd() {
        return dateAdd;
    }
    
  
    @Override
    public Integer getId() {
        
      return -1;  
        
    }
  
    
}
