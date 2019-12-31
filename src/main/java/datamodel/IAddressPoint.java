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

import org.jxmapviewer.viewer.GeoPosition;

/**
*
* Interfejs dla punktu adresowego
* 
* @author Maciej Kawecki
* @version 1.0
* 
*/
public interface IAddressPoint extends IData {

	List<GeoPosition> getAdditionalGeometryCoords();
	
	List<GeoPosition> getGeometryCoords();
	
	double getCustomerLatitude();
	
	double getCustomerLongitude();
	
	String getCustomerLabel();
	
	boolean isOnRoute();
	
}
