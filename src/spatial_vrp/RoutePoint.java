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
import datamodel.IData;
import datamodel.Order;
import java.util.ArrayList;
import java.util.List;
import org.jxmapviewer.viewer.GeoPosition;


/**
 *
 * Szablon obiektu reprezentującego pojedynczy punkt odbioru towaru na wyznaczonej trasie 
 * dla planu dostawy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class RoutePoint implements IData {
    
    /** Identyfikator punktu trasy */
    private Integer id;
    /** Odległość do tego punktu */
    private double distance;
    /** Szacowany czas przejazdu [w sek] części trasy do tego punktu */
    private double time;
    /** Zrealizowane w tym punkcie zamówienie */
    private Order order;
    /** Geometria odcinka od poprzedniego punktu */
    private Geometry geometry;
    /** Dodatkowa geometria (odcinek drogi od lub do najbliższego węzła) */
    private Geometry additionalGeometry;
    /** Etykieta punktu na mapie */
    private String label;
    
    
    /**
     * Konstruktor
     * @param id Identyfikator punktu trasy
     * @param distance Odległość do tego punktu
     * @param time Szacowany czas przejazdu [w sek] części trasy do tego punktu
     * @param order Zrealizowane w tym punkcie zamówienie
     * @param geometry Geometria odcinka od poprzedniego punktu
     * @param additionalGeometry Dodatkowa geometria (odcinek drogi od najbliższego węzła)
     */
    public RoutePoint(Integer id, double distance, double time, Order order, Geometry geometry,
            Geometry additionalGeometry) {
        
      this.id = id;
      this.distance = distance;
      this.time = time;
      this.order = order;
      this.geometry = geometry;       
      this.additionalGeometry = additionalGeometry;
        
    }
    
    /**
     * Konstruktor kopiujący obiekt
     * @param point Kopiowany obiekt
     */
    public RoutePoint(RoutePoint point) {
        
      this.id = point.id;
      this.distance = point.distance;
      this.time = point.time;
      this.order = point.order;
      this.geometry = point.geometry;        
      this.additionalGeometry = point.additionalGeometry;
      this.label = point.label;
        
    }
        

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Geometry getAdditionalGeometry() {
        return additionalGeometry;
    }

    public void setAdditionalGeometry(Geometry additionalGeometry) {
        this.additionalGeometry = additionalGeometry;
    }
    

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    
    
      
  /**
   * Konwersja trasy z geometrii na listę koordynat
   * @param geometry Geometria trasy
   * @return Lista koordynat
   */
  public static List<GeoPosition> getCoordsList(Geometry geometry) {
      
    if (geometry == null) return null;  
    List<GeoPosition> route = new ArrayList<>();
 
    for (int i=0; i < geometry.getNumGeometries(); i++){
        
      Geometry lineGeometry = geometry.getGeometryN(i);
      for (Coordinate coordinate : lineGeometry.getCoordinates()) {
         
         route.add(new GeoPosition(coordinate.y, coordinate.x));
        
      }
    }          
     
    return route;  
      
  }
    
    
    
}
