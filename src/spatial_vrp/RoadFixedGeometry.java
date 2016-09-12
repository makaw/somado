/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package spatial_vrp;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 *
 * Przygotowanie poprawionej geometrii drogi (z uwzględnieniem dodatkowych odcinków zawierających tylko jeden węzeł)
 * do wyświetlenia na mapie
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class RoadFixedGeometry {
    
  /** Geometria głównych odcinków dróg (połączenia pomiędzy węzłami) dla ID punktów trasy */
  private final Map<Integer, Geometry> geometry;
  /** Lista dodatkowych geometrii (pojedyncze fragmenty odcinków pomiędzy węzłami) dla ID punktów trasy */
  private final Map<Integer, Geometry> additionalGeometry;
  /** Poprawiona długość odcinków */
  private final Map<Integer, Double> fixedLength;
  /** Czy to obiekt dla macierzy kosztów */
  private boolean forMatrix = false;
  
  
 /**
   * Konstruktor dla macierzy kosztów
   * @param elem Element macierzy kosztów
   * @param additionalGeometry1 Dodatkowa geometria odcinka drogi dla punktu 1 (map. indeks kolumny macierzy)
   * @param additionalGeometry2 Dodatkowa geometria odcinka drogi dla punktu 2 (map. indeks wiersza macierzy)
   */
  public RoadFixedGeometry(CostMatrixElem elem, Geometry additionalGeometry1, Geometry additionalGeometry2) {
      
     this(new RoutePoint(1, 0.0, 0.0, null, elem.getGeometry(), additionalGeometry1), 
          new RoutePoint(2, elem.getDistance(), 0.0, null, elem.getGeometry(), additionalGeometry2));
     forMatrix = true;
      
  }  
  
  
 /**
   * Konstruktor dla dwóch punktów (poj. odcinek)
   * @param p1 Pierwszy punkt
   * @param p2 Drugi punkt
   */
  public RoadFixedGeometry(RoutePoint p1, RoutePoint p2) {
      
     this(new ArrayList<>(Arrays.asList(new RoutePoint[]{p1, p2})));
      
  }
  
  
  /**
   * Konstruktor dla całej trasy (do wyświetlania na mapie)
   * @param routePoints Lista punktów trasy
   */
  public RoadFixedGeometry(List<RoutePoint> routePoints) {
    
    geometry = new HashMap<>();
    additionalGeometry = new HashMap<>();
    fixedLength = new HashMap<>();
    
    List<RoutePoint> route = new ArrayList<>(routePoints);
    
 
    Iterator<RoutePoint> iterator = route.iterator();  
    Geometry prevAdditionalGeometry = null;
    
    while (iterator.hasNext()) {
        
      RoutePoint point = iterator.next();  
      
      if (point.getGeometry() == null) continue;
      
      double addLength = 0.0;
      
      // najpierw poprzednia dodatkowa  geometria (pierwszy punkt)
      if (prevAdditionalGeometry != null) {
          
        Coordinate c[] = prevAdditionalGeometry.getCoordinates();                 
          
        if (testCoords(point.getGeometry(), c, false)) {
           
           point.setGeometry(point.getGeometry().difference(prevAdditionalGeometry));
           addLength -= getRoadLen(prevAdditionalGeometry);  
            
        }  
      
      }
      
      
      // teraz aktualna dodatkowa  geometria (drugi punkt)
      if (point.getAdditionalGeometry() != null) {
          
        boolean found = false;  
          
        Coordinate c[] = point.getAdditionalGeometry().getCoordinates();                 
                    
        // jeżeli nie ostatni punkt, to czy następny także pokrywa dodatkową geometrię ? 
        boolean containsNext = false;
        if (iterator.hasNext()) {
            
          RoutePoint tmp = null;          
          Iterator<RoutePoint> iterator2 = new ArrayList<>(route).iterator();  
          while (iterator2.hasNext()) {
             tmp = iterator2.next();
             if (tmp.getId().equals(point.getId())) break;
          }
          
          if (tmp != null && iterator2.hasNext() && testCoords(iterator2.next().getGeometry(), c, true)) {
           
            containsNext = true;  
          
          }
        
        }
        
        // jeżeli ostatni (bez powrotu) lub powrót tą samą drogą
        if ((!iterator.hasNext() || containsNext) && testCoords(point.getGeometry(), c, false)) {
            
          point.setGeometry(point.getGeometry().difference(point.getAdditionalGeometry()));
          addLength -= getRoadLen(point.getAdditionalGeometry());
          found = true;   
            
        }       
        
        
         if (!found && !testCoords(point.getGeometry(), c, false)) {
             
            additionalGeometry.put(point.getId(), point.getAdditionalGeometry());
            addLength += getRoadLen(point.getAdditionalGeometry());

         }
                   
      
      }      
         
      
      geometry.put(point.getId(), point.getGeometry());       
      fixedLength.put(point.getId(), addLength);
      
      prevAdditionalGeometry = point.getAdditionalGeometry();
        
      
    }        
    
    
  }

  
  /**
   * Sprawdzenie czy ciąg punktów zawiera się w danej geometrii
   * @param geom Geometria drogi
   * @param c Ciąg punktów (tablica)
   * @param reverse Czy kolejność ma być odwrócona (powrót)
   * @return True jeżeli ciąg punktów zawiera się w geometrii
   */
  private boolean testCoords(Geometry geom, Coordinate[] c, boolean reverse) {
      
    int dk = reverse ? -1 : 1;
    
    for (int k=(reverse ? c.length-1 : 0); (reverse ? k>=0 : k<c.length); k+=dk) {

      try {                             
      
        String s = c[k].x + " " + c[k].y + "," + c[k+1].x + " " + c[k+1].y  ;            
        Geometry g = new WKTReader().read("LINESTRING(" + s + ")");            
              
        if (geom.contains(g)) return true;
            
      }
      catch (ParseException e) {             
        System.out.println(e);                
      }
      catch (ArrayIndexOutOfBoundsException | NullPointerException e) {}
                         
    }    
      
    return false;
      
  }
  
       
  /**
   * Metoda zwraca korektę długości drogi (w metrach)
   * @param pointId ID punktu trasy
   * @return Korekta długości drogi (w metrach)
   */
  public double getFixedLength(int pointId) {
       
    return fixedLength.get(pointId);
      
  }
  
  
  /**
   * Metoda zwraca korektę długości drogi (w metrach) - wersja dla macierzy kosztów
   * @return Korekta długości drogi (w metrach)
   */
  public double getFixedLength() {
      
     return forMatrix ? fixedLength.get(2) : 0.0;  
      
  }
  
   
  /**
   * Metoda zwraca poprawioną geometrię podstawową (odcinka łączącego węzły)
   * @param pointId ID punktu trasy
   * @return Poprawiona geometria podstawowa
   */
  public Geometry getFixedGeometry(int pointId) {
      
    return geometry.get(pointId);
    
  }
  
  
  /**
   * Metoda zwraca skorygowaną podstawową geometrię odcinka trasy - wersja dla macierzy kosztów
   * @return Skorygowana podstawowa geometria
   */  
  public Geometry getFixedGeometry() {
      
    return forMatrix ? getFixedGeometry(2) : null;   
      
  }
  

  /**
   * Metoda zwraca skorygowaną podstawową geometrię odcinka trasy
   * @param pointId ID punktu trasy
   * @return Skorygowana podstawowa geometria
   */  
  public Geometry getFixedAdditionalGeometry(int pointId) {
      
    return additionalGeometry.get(pointId);
     
  }
  
  
  /**
   * Metoda zwraca poprawioną geometrię dodatkową - wersja dla macierzy kosztów
   * @return Poprawiona geometria dodatkowa
   */    
  public Geometry getFixedAdditionalGeometry() {
      
    return forMatrix ? getFixedAdditionalGeometry(2) : null;   
      
  }
  
  
  
  /**
   * Metoda zwraca długość geometrii (w WGS-84) przekonwertowaną na metry (w przybliżeniu, dla małych obiektów)
   * @param geometry Geometria
   * @return Przybliżona długość geometrii w metrach
   */
  private static double getRoadLen(Geometry geometry) {
      
    // 6378137.0 - oszacowanie promienia Ziemi w m (przy przybliżaniu bryły Ziemi jako elipsoidy)  
    try {
      return Math.PI/180.0 * 6378137.0 * geometry.getLength();
    }
    catch (NullPointerException e) { return 0.0; }
      
  }
  

}
