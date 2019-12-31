/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package spatial_vrp;

import datamodel.Driver;
import datamodel.IAddressPoint;
import datamodel.IRoute;

import java.util.List;


/**
 *
 * Szablon obiektu reprezentującego wyznaczoną trasę dla planu dostawy
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Route implements IRoute {
    
   /** Kierowca dla którego wyznaczono trasę */
   private Driver driver;
   /** Lista punktów odbioru towaru */
   List<RoutePoint> points;
   
   /**
    * Konstruktor
    * @param driver Kierowca dla którego wyznaczono trasę
    * @param points Lista punktów odbioru towaru
    */
   public Route(Driver driver, List<RoutePoint> points) {
       
     this.driver = driver;
     this.points = points;
              
   }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<RoutePoint> getPoints() {
        return points;
    }

    public void setPoints(List<RoutePoint> points) {
        this.points = points;
    }


	@Override
	public String getVehicleRegistrationNo() {
	  return driver != null && driver.getVehicle() != null ? driver.getVehicle().getRegistrationNo() : "";
	}

	@Override
	public List<? extends IAddressPoint> getAddressPoints() {
	  return points;
	}

    
}
