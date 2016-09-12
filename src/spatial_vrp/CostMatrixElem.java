/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package spatial_vrp;

import com.vividsolutions.jts.geom.Geometry;


/**
 *
 * Element macierzy kosztów przejazdu pomiędzy punktami adresowymi oraz geometrii najkrótszej trasy 
 * pomiędzy tymi punktami
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class CostMatrixElem {
    
   /** Odległość (w m) pomiędzy punktami */ 
   private double distance; 
   /** Szacowany czas przejazdu (w sek) pomiędzy punktami */ 
   private final double time;
   /** Geometria najkrótszej trasy pomiędzy punktami */
   private Geometry geometry;
   /** Dodatkowa geometria dla punktów odbioru */
   private Geometry additionalGeometry;
   
   /**
    * Konstruktor
    * @param distance Odległość (w m) pomiędzy punktami
    * @param time Szacowany czas przejazdu (w sek) pomiędzy punktami
    * @param geometry Geometria najkrótszej trasy pomiędzy punktami
    */
   public CostMatrixElem(double distance, double time, Geometry geometry) {
       
     this.distance = distance;  
     this.time = time;     
     this.geometry = geometry;
       
   }
   
   
   /**
    * Konstruktor kopiujący obiekt
    * @param elem Kopiowany obiekt
    */
   public CostMatrixElem(CostMatrixElem elem) {
       
     this(elem.distance, elem.time, elem.geometry);  
       
   }
   
   
   
   /**
    * Konstruktor
    * @param distance Odległość (w m) pomiędzy punktami
    * @param time Szacowany czas przejazdu (w sek) pomiędzy punktami
    */
   public CostMatrixElem(double distance, double time) {
       
     this(distance, time, null);
       
   }   

   
   public double getDistance() {
      return distance;
   }
   

   public void setDistance(double distance) {
      this.distance = distance;
   }   
   
   
   public double getTime() {
      return time;
   }
      

   public Geometry getGeometry() {
      return geometry;
   }
   

   public void setGeometry(Geometry geometry) {
      this.geometry = geometry;
   }
            

   public Geometry getAdditionalGeometry() {
      return additionalGeometry;
   }

   public void setAdditionalGeometry(Geometry additionalGeometry) {
      this.additionalGeometry = additionalGeometry;
   }         
    
    
}
