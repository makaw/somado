/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 *
 * Szablon obiektu reprezentującego użytkownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class User {
            
    
   /** Login systemowy użytkownika */ 
   private final String login;
   /** Podane hasło aplikacyjne */
   private String password;
   /** ID użytkownika (baza danych) */
   private int id = 0;

   /** Imię */
   private String firstname;
   /** Nazwisko */
   private String surname;
   
   /** Rola użytkownika */
   private UserRole role;
   

   
   
   /**
    * Konstruktor autoryzujący w systemie
    * @param login Login użytkownika
    * @param password Hasło aplikacyjne użytkownika
    * @param database Ref. do bazy danych
    * @throws UserException Wyjątek rzucany przy błędnej autoryzacji
    */
   public User(String login, String password, Database database) throws UserException {
       
     this.login = login;
     this.password = password;
     
     try {
           
         PreparedStatement ps = database.prepareQuery("SELECT id, role, firstname, surname FROM sys_users "
                 + "WHERE login=? AND passwd=PASSWORD(?) AND blocked<>'1' AND role IN(?, ?);"); 
         ps.setString(1, login);
         ps.setString(2, password);
         ps.setInt(3, UserRole.ADMIN.getId());
         ps.setInt(4, UserRole.DISPATCHER.getId());
         ResultSet rs = ps.executeQuery();
           
         if (rs.first()) {
           
           id = rs.getInt("id");
           role = UserRole.get(rs.getInt("role"));
           firstname = rs.getString("firstname");
           surname = rs.getString("surname");
             
         }  
         
         else throw new UserException(UserException.AUTH);         
         
         
       } catch (SQLException e) {
       
          System.err.println("B\u0142\u105d SQL: "+e);
          
       }
        
       
   }
   
   
   /**
    * Konstruktor dla klas dziedziczących (do zarządzania)
    * @param id ID użytkownika w BD
    * @param login Login
    * @param firstname Imię
    * @param surname Nazwisko
    * @param role Rola
    */
   protected User(int id, String login, String firstname, String surname, UserRole role) {
       
     this.id = id;
     this.login = login;
     this.firstname = firstname;
     this.surname = surname;
     this.role = role;       
       
   }
   
   /**
    * Pusty konstruktor (dla nowego elementu) 
    */
   protected User() {
       
     this(0, "", "", "", UserRole.DISPATCHER);         
       
   }
   
   
   /**
    * Zmiana hasła aplikacyjnego
    * @param password Nowe hasło
    * @param database Ref. do bazy danych
    * @throws SQLException Błąd SQL
    */
   public void changePassword(String password, Database database) throws SQLException {
       
     PreparedStatement ps = database.prepareQuery("UPDATE sys_users SET passwd = PASSWORD(?) WHERE id = ? LIMIT 1;");
     ps.setString(1, password);
     ps.setInt(2, this.id);
     ps.executeUpdate();
     
     this.password = password;       
       
   }
      
   
   
   public Integer getId() {
       return id;
   }

   /**
    * Czy użytkownik zautoryzowany
    * @return True jeżeli tak
    */
   public boolean isAuthorized() {
       return id>0;
   }


   public String getLogin() {
       return login;
   }

   public String getPassword() {
       return password;
   }

   public String getFirstname() {
       return firstname;
   }

   public void setFirstname(String firstname) {
       this.firstname = firstname;
   }

   public String getSurname() {
       return surname;
   }

   public void setSurname(String surname) {
       this.surname = surname;
   }

   public UserRole getRole() {
       return role;
   }

   public void setRole(UserRole role) {
       this.role = role;
   }
   

   public void setId(int id) {
       this.id = id;
   }
      
   
   public void setPassword(String password) {
      this.password = password;
   }   
   
   
   
   /**
    * Czy użytkownik jest administratorem
    * @return True jeżeli administrator
    */
   public boolean isAdmin() {
       
     return role == UserRole.ADMIN;  
       
   }

   
   

   
   @Override
   public String toString() {
      
     if (surname.isEmpty()) return login;  
       
     return (!firstname.isEmpty() ? firstname.charAt(0)+"." : "") + surname + " (" + login + ")";
       
   }
    
    
}
