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


/**
 *
 * Szablon obiektu reprezentującego produkt
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Product implements IDataEditable {
    
   /** Klucz w tabeli BD */ 
   private Integer id;
   /** Nazwa produktu */
   private String name;
   /** Waga jednostkowa */
   private double weight;
   /** Dostępność */
   private boolean available;
   
   /**
    * Konstruktor obiektu produktu
    * @param id Id rekordu w bazie danych
    * @param name Nazwa
    * @param weight Waga jednostkowa
    * @param available Dostępność
    */
   public Product(int id, String name, double weight, boolean available) {
       
     this.id = id;
     this.name = name;
     this.weight = weight;
     this.available = available;
     
   }
   
   /**
    * Konstruktor obiektu produktu (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @param prefix Prefix pól dot. produktu w zapytaniu
    * @throws SQLException Błąd SQL
    */
   public Product(ResultSet rs, String prefix) throws SQLException {
       
     this.id = rs.getInt(prefix + "id");
     this.name = rs.getString(prefix + "name");
     this.weight = rs.getDouble(prefix + "weight");
     this.available = rs.getBoolean(prefix + "available");
       
   }
   
   
   /**
    * Konstruktor obiektu produktu (z BD)
    * @param rs Zestaw wyników zapytania do BD
    * @throws SQLException Błąd SQL
    */   
   public Product(ResultSet rs) throws SQLException {
        
    this(rs, "");  
        
   }
    
    
   
    /**
     * Konstruktor pustego obiektu (nowego)
     */
    public Product() {
        
      this(0, "", 0.0, false);  
        
    }       
   

   @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    
    
    

   
   @Override
   public String toString() {
       
     return name;
     
   }

    @Override
    public void verify() throws Exception {
        
      if (name.isEmpty())  throw new Exception("Nie podano nazwy produktu.");    
      if (weight<IConf.MIN_PRODUCT_WEIGHT || weight>IConf.MAX_PRODUCT_WEIGHT) 
          throw new Exception("Nieprawid\u0142owa waga produktu ("
                  + String.format("%.2f", IConf.MIN_PRODUCT_WEIGHT) + " - "
                  + String.format("%.2f", IConf.MAX_PRODUCT_WEIGHT)+")");
        
    }
    
}
