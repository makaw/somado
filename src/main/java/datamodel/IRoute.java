/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package datamodel;

import java.util.List;

/**
*
* Interfejs dla trasy
* 
* @author Maciej Kawecki
* @version 1.0
* 
*/
public interface IRoute {

	String getVehicleRegistrationNo();
	
	List<? extends IAddressPoint> getAddressPoints();
	
}
