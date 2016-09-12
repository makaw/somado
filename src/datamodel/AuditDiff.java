/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import somado.Settings;


/**
 *
 * Szablon obiektu reprezentującego poj.zmianę atrybutów obiektu
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class AuditDiff implements Serializable {
    
  /** Tryb modyfikacji obiektu */
  public static int AM_MOD = 2;
  /** Tryb bledu serializacji (nie mozna odczytac obiektu) */
  public static int AM_ERR = 0;
  /** Tryb usuwania obiektu */
  public static int AM_DEL = 3;
  /** Tryb dodawania nowego obiektu */
  public static int AM_ADD = 1;
    
  /** Mapa wartosci atrybutow obiektu */
  private final Map<String, String> diff;
  /** Komentarz */
  private final String comment;
  
  
  /** Nr wersji klasy (do serializowania obiektow) */
  private static final long serialVersionUID = 1L;
  
  /**
   * Konstruktor
   * @param objectOld Poprzedni obiekt
   * @param objectNew Obiekt po wykonanych zmianach
   * @param mode Tryb zmiany
   * @param comment Komentarz
   */
  public AuditDiff(Object objectOld, Object objectNew, int mode, String comment) {
      
     diff = new HashMap<>();    
     this.comment = comment;
     
     if (objectNew == null || objectNew.equals(objectOld)) return;
     
     Class<?> c = objectNew.getClass(); 
     
     Field[] fields = c.getDeclaredFields(); 
     for (Field field : fields) { 

        field.setAccessible(true);
        
        try { 
                        
             try {                                  
                 
               if (!fieldFormat(field.get(objectOld)).equals(fieldFormat(field.get(objectNew)))) {
                 diff.put(field.getName(), 
                   diffFormat(fieldFormat(field.get(objectOld)), fieldFormat(field.get(objectNew))));
               }
               
             }
             catch (NullPointerException e) {             
        
               diff.put(field.getName(), fieldFormat(field.get(objectNew)));              
               
             } 
           
        }
        catch (IllegalArgumentException | IllegalAccessException e2) {           
          System.err.println(e2.getMessage());            
        }
       
           
    }
    

  }

  
  /**
   * Konstruktor przy wpisie dot. dodania nowego obiektu
   * @param obj Nowy obiekt
   * @param mode Tryb zmiany
   * @param comment Komentarz
   */
  public AuditDiff(Object obj, int mode, String comment) {
      
     this(null, obj, mode, comment);
      
  }
  
  
  /** 
   * Pusty konstruktor (w razie bledu odczytu zserializowanego obiektu)
   */
  public AuditDiff() {
      
     this(null, null, AM_ERR, "(nie mo\u017cna odczyta\u0107 obiektu)");
                 
  }
  
  
  /**
   * Konwersja obiektu na napis
   * @param obj Obiekt wejsciowy
   * @return Napis
   */
  private String fieldFormat(Object obj) {
      
     String tmp = ""; 
      
     try { 
       if (obj instanceof Date) tmp = Settings.DATE_FORMAT.format((Date) obj);
       else if (obj instanceof HashMap) tmp = "";
       else tmp = obj.toString();
     }
     catch (NullPointerException e) {}
 
     return tmp;
     
  }
  
  
  /**
   * Formatowanie napisu z roznica miedzy atrybutami obiektu
   * @param valOld  Poprzednia wartosc atrybutu
   * @param valNew  Nowa wartosc atrybutu
   * @return Napis
   */
  private String diffFormat(String valOld, String valNew) {
      
     return "[" + valOld + "] \u2192 [" + valNew + "]";  

  }  

  
   
  public Map<String, String> getDiff() {
      
      return diff;
      
  }

   
  public String getComment() {
  
      return comment;
      
  }

  
  @Override
  public String toString() {
      
    String s = "";
      
    Iterator<Map.Entry<String, String>> it = diff.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<String, String> pair = it.next();
        s += "[" + pair.getKey() + "] = " +  "[" + pair.getValue() + "], ";
        it.remove(); 
    } 
    
    try {
      return s.substring(0, s.length()-2);
    }
    catch (Exception e) {
      return s;          
    }
      
  }  
  
  
    
}
