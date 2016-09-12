/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

import datamodel.Audit;
import datamodel.AuditDiff;
import datamodel.Customer;
import datamodel.IData;
import datamodel.docs.DocAudit;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * Szablon obiektu przechowującego ustawienia aplikacji (singleton)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class Settings extends HashMap<String, String> implements IData {
          
  /** Format daty */
  public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
   /** Format daty z czasem */
  public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   /** Format daty z czasem (bez sek) */
  public static final SimpleDateFormat DATETIME_NS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  
  
  /** Współrzędne graniczne obsługiwanego obszaru */
  private double westBoxCoord, northBoxCoord, eastBoxCoord, southBoxCoord;
  
  /** Magazyn */
  private Customer depot;
  
  
  /** Pomocnicza klasa do historii zmian */
  public class SettingsAudit {
      
    private final String keyValue;
    public SettingsAudit(String keyValue) {
      this.keyValue = keyValue;  
    }
      
  }
  
  
  /** Instancja klasy */
  private final static Settings SETTINGS = new Settings();
    
  /** 
   * Zwrócenie instancji klasy
   * @return Instancja klasy
   */
  public static Settings getInstance() {
      
     return SETTINGS;
     
  }

  /** Blokada utworzenia nowej instancji klasy */
  private Settings() {
        
    super();     
    
  }
  
  
  /**
   * Zwraca obiekt do zapisania historii zmian
   * @param key klucz
   * @param value wartosc
   * @return Pomocniczy obiekt do zapisania historii zmian
   */
  public SettingsAudit getKeyValue(String key, String value) {
      
    return new SettingsAudit(key + ": " +value);  
      
  }
  
  
  /**
   * Załadowanie podstawowych ustawień z pliku .properties
   * @throws java.io.FileNotFoundException Brak pliku .properties
   * @throws SettingsException Błąd IO - brak dostępu do pliku
   */
  public void loadBasic() throws FileNotFoundException, SettingsException  {
        
    clear();
       
    Properties props = new Properties();
     
    try {
      props.load(new FileReader("conf.properties"));      
    } catch (IOException | NullPointerException e) {
      try {        
         props.load(Settings.class.getResourceAsStream("/resources/conf.default.properties"));  
      }  
      catch (IOException | NullPointerException ex) {
        System.err.println(ex);
        throw new SettingsException("Nie mo\u017cna odczyta\u0107 podstawowej konfiguracji (b\u0142\u0105d I/O).");       
      }
    }    
    
    this.put("db_localcache_name", props.getProperty("datasource.localcache.name"));
    this.put("db_local_name", props.getProperty("datasource.local.name"));
    this.put("db_url", props.getProperty("datasource.url"));
    this.put("db_username", props.getProperty("datasource.username"));      
    this.put("db_password", props.getProperty("datasource.password"));

    // graniczne wspolrzedne obslugiwanego obszaru
    try {
      westBoxCoord = Double.valueOf(props.getProperty("coordsbox.west"));
      northBoxCoord = Double.valueOf(props.getProperty("coordsbox.north"));
      eastBoxCoord = Double.valueOf(props.getProperty("coordsbox.east"));
      southBoxCoord = Double.valueOf(props.getProperty("coordsbox.south"));
    }
    catch (Exception e) {
      throw new SettingsException("Nieprawid\u0142owe wsp\u0142\u00f3rz\u0119dne graniczne obs\u0142ugiwanego obszaru.");  
    }
    
    this.put("tms_url", props.getProperty("tms.url"));
    
  }  
  
    
  
  /** 
   * Inicjalizacja: załadowanie ustawień z BD
   * @param database Referencja do obiektu bazy danych
   * @throws SettingsException Błąd konfiguracji
   */
  public void load(Database database) throws SettingsException {
      
    try {
          
      ResultSet rs = database.doQuery("SELECT * FROM sys_settings WHERE enabled='1';");
      while (rs.next())  this.put(rs.getString("key"), rs.getString("value"));      
      
      int depotId = Integer.parseInt(getValue("depot_customer_id")); 
      
      PreparedStatement ps = database.prepareQuery("SELECT * FROM glo_customers WHERE id = ?;");      
      ps.setInt(1, depotId);
      rs = ps.executeQuery();
      
      if (rs.next()) depot = new Customer(rs);
      else  throw new SettingsException("Nie mo\u017cna odczyta\u0107 konfiguracji globalnej: "
                + "nieprawid\u0142owe dane magazynu.");
      
      
    } catch (SQLException e2) {
       
       System.err.println("Settings: b\u0142\u0105d SQL: "+e2);
       throw new SettingsException("Nie mo\u017cna odczyta\u0107 konfiguracji "
               + (database instanceof DatabaseShared ? "globalnej" : "lokalnej") 
               + " (b\u0142\u0105d SQL).");
       
    } 
    
    
  }    
  
  
  /**
   * Zmiana globalnej wartości dla danego klucza
   * @param database Referencja do bazy danych
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @param key Klucz
   * @param value Wartość
   * @throws SQLException Blad SQL 
   */
  public static void updateKey(Database database, User user, String key, String value)  
          throws SQLException {
      
    PreparedStatement ps = database.prepareQuery("UPDATE sys_settings SET `value` = ? WHERE `key` = ? ;");   
    ps.setString(1, value);
    ps.setString(2, key);
    ps.executeUpdate();   
    
    if (database instanceof DatabaseShared) {
      Audit audit = new Audit(getInstance().getKeyValue(key, value), 
                      getInstance().getKeyValue(key, getValue(key)), getInstance(), 
                      AuditDiff.AM_MOD, "Zmieniono ustawienia");
      (new DocAudit(database, getInstance())).addElement(audit, user);     
    }
      
  }
  
  
  
  /**
   * Metoda zwraca wartość liczbowa dla danego klucza
   * @param key Klucz
   * @return wartość
   */  
  public static int getIntValue(String key) {
      
    try {  
      return Integer.parseInt(getValue(key));  
    }
    catch (NumberFormatException e) {
      System.err.println("B\u0142\u0105d ustawien NumberFormat dla klucza: "+key);  
      return -1;  
    }
      
  }        
  
  
  
  /**
   * Metoda zwraca obiekt "odbiorcy" - magazynu
   * @return Obiekt magazynu
   */
  public static Customer getDepot() {
      
    return getInstance().depot;  
      
  }
  
  
  /**
   * Metoda zwraca wartość(ustawienie) dla danego klucza
   * @param key Klucz
   * @return wartość
   */
  public static String getValue(String key) {
      
    return getInstance().get(key);
      
  }

  /**
   * Metoda zwraca zachodnią granicę obsługiwanego obszaru
   * @return Wsp. zachodniej granicy obsługiwanego obszaru
   */
  public static double getWestBoxCoord() {
      
    return getInstance().westBoxCoord;
    
  }
  
  /**
   * Metoda zwraca północną granicę obsługiwanego obszaru
   * @return Wsp. północnej granicy obsługiwanego obszaru
   */  
  public static double getNorthBoxCoord() {
      
    return getInstance().northBoxCoord;
    
  }
  
  /**
   * Metoda zwraca wschodnią granicę obsługiwanego obszaru
   * @return Wsp. wschodniej granicy obsługiwanego obszaru
   */
  public static double getEastBoxCoord() {
      
    return getInstance().eastBoxCoord;
        
  }

  /**
   * Metoda zwraca południową granicę obsługiwanego obszaru
   * @return Wsp. południowej granicy obsługiwanego obszaru
   */
  public static double getSouthBoxCoord() {
      
    return getInstance().southBoxCoord;
    
  }
  
  
  /** 
   * Statyczna metoda sprawdzająca czy dane współrzędne mieszczą się w obsługiwanym zakresie
   * @param longitude Długość geograficzna
   * @param latitude Szerokość geograficzna
   * @return True jeżeli punkt mieści się w zakresie
   */
  public static boolean checkBoxCoords(double longitude, double latitude) {
      
     return (longitude>=Settings.getWestBoxCoord() && longitude<=Settings.getEastBoxCoord() && 
             latitude>=Settings.getSouthBoxCoord() && latitude<=Settings.getNorthBoxCoord());
      
  }
  
  /**
   * Metoda zwraca informację o obsługiwanym zakresie współrzędnych
   * @return Informacja o zakresie
   */
  public static String infoBoxCoords() {
      
    return  "Zakres d\u0142ugo\u015bci: " + String.format("%.4f", Settings.getWestBoxCoord()) + " - " 
            + String.format("%.4f", Settings.getEastBoxCoord()) + "\n"
            + "Zakres szeroko\u015bci: " + String.format("%.4f", Settings.getSouthBoxCoord()) + " - " 
            + String.format("%.4f", Settings.getNorthBoxCoord());  
      
  }
  
  
  /**
   * Przekształcenie liczby sekund do formatu hh:mm:ss
   * @param seconds Liczba sekund
   * @param showSec True jeżeli pokazywać sekundy
   * @return Czas w formacie hh:mm:ss
   */
  private static String formatTime(double seconds, boolean showSec) {
          
    int hh = (int)(seconds / 3600.0); 
    int mm = ((int)(seconds / 60.0) % 60);
    int ss = (int)seconds % 60;
   
    return showSec ? String.format("%02d:%02d:%02d", hh, mm, ss) : String.format("%02d:%02d", hh, mm);
      
  }  
  
  
  /**
   * Przekształcenie liczby sekund do formatu hh:mm:ss
   * @param seconds Liczba sekund
   * @return Czas w formacie hh:mm:ss
   */
  public static String formatTime(double seconds) {  
      
    return formatTime(seconds, true);  
      
  }
  
  /**
   * Przekształcenie liczby sekund do formatu hh:mm
   * @param seconds Liczba sekund
   * @return Czas w formacie hh:mm
   */
  public static String formatTimeNS(double seconds) {  
      
    return formatTime(seconds, false);  
      
  }  
  
  
  
  @Override
  public Integer getId() {
     return 1; 
  }
  
  @Override
  public String toString() {      
    return "";       
  }
  
    
}
