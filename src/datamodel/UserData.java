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
import somado.IConf;
import somado.User;
import somado.UserRole;


/**
 *
 * Szablon obiektu reprezentującego użytkownika (wersja do zarządzania)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class UserData extends User implements IDataEditable {
    
    /** Blokada konta */
    private boolean blocked;
    
    /** Przypisany punkt odbioru towaru (dla konta klienta) */
    private Customer customer;
    
    /** Wartości atrybutów superklasy (dla audytu zmian) */
    private String userAttr = "";
    
    /**
     * Konstruktor (z BD)
     * @param rs Zestaw wyników zapytania do BD
     * @param prefix Prefix pol dot. kraju w zapytaniu
     * @throws SQLException Błąd SQL
     */    
    public UserData(ResultSet rs, String prefix) throws SQLException {
      
      super(rs.getInt(prefix+"id"), rs.getString(prefix+"login"), rs.getString(prefix+"firstname"), 
            rs.getString(prefix+"surname"), UserRole.get(rs.getInt(prefix+"role"))); 
      blocked = rs.getBoolean(prefix+"blocked");
        
    }            
    
    /**
     * Konstruktor (z BD)
     * @param rs Zestaw wyników zapytania do BD
     * @throws SQLException Błąd SQL
     */        
    public UserData(ResultSet rs) throws SQLException {
        
      this(rs, "");  
        
    }
    
    
    /**
     * Konstruktor
     * @param id Id rekordu w bazie danych
     * @param login Login użytkownika
     * @param firstname Imię
     * @param surname Nazwisko
     * @param role Rola w systemie
     * @param blocked Blokada konta
     */
    public UserData(int id, String login, String firstname, String surname, UserRole role, boolean blocked) {
        
       super(id, login, firstname, surname, role);          
       this.blocked = blocked;
        
    }
    
    
    /**
     * Konstruktor kopiujący dane
     * @param user Kopiowany obiekt
     */
    public UserData(User user) {
        
      this(user.getId(), user.getLogin(), user.getFirstname(), user.getSurname(), user.getRole(), false);  
      if (user.getPassword() != null) super.setPassword(user.getPassword());
      if (user instanceof UserData) this.blocked = ((UserData)user).blocked;
      
    }
    
    
    /**
    * Pusty konstruktor (dla nowego elementu) 
    */
    public UserData() {  
        
      super();  
      blocked = false;
              
    }  

    

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
    
    
    

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }        
    
    /** 
     * Metoda ustawia napis z aktualnych wartości atrybutów superklasy (żeby były widoczne dla audytu)
     * Metodę trzeba wywołać przed utworzeniem nowego wpisu audytowego
     */
    public void setUserAttr() {
       userAttr = getLogin() + ", " + getRole().toString() + ", " + getSurname() + " " + getFirstname();              
    }
    
    
    @Override
    public void verify() throws Exception {
       
       if (getLogin().isEmpty())  throw new Exception("Nie podano loginu.");       
        
    }
    
    
    /**
     * Weryfikacja do zmiany hasła
     * @param secondPass Potwierdzenie nowego hasła
     * @throws Exception Wprowadzono nieprawidłowe dane
     */
    public void verifyPass(String secondPass) throws Exception {
        
      if (getPassword().isEmpty()) throw new Exception("Nie podano has\u0142a.");
      if (getPassword().length()<IConf.MIN_PASS_LEN) throw new Exception("Has\u0142o jest za kr\u00f3tkie"
              + " (wymagane min."+String.valueOf(IConf.MIN_PASS_LEN)+ "znak\u00f3w).");
      if (!getPassword().equals(secondPass)) throw new Exception("Has\u0142a nie pasuj\u0105 do siebie.");                
        
    }
    
    
    @Override
    public String toString() {
        
      return getSurname() + " " + getFirstname() + " (" + getLogin() + ", " + getRole().toString() + ")";
        
    }
    
}
