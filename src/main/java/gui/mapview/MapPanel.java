/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.mapview;

import java.awt.GridBagConstraints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;

import datamodel.IAddressPoint;
import somado.IConf;


/**
 *
 * Panel z mapą
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class MapPanel extends JXMapKit {  
    

  /**
   * Konstruktor
   * @param host URL usługi TMS
   */
  public MapPanel(String host) {
      
    super();                  
            
    setDataProviderCreditShown(false);
        
    TileFactoryInfo tileFactoryInfo = new TileFactoryInfo(2, 16, 18, 256, true, true, host, "x", "y", "z") {
       @Override
       public String getTileUrl(int x, int y, int zoom) {                           
         return this.baseURL + "/" + (this.getTotalMapZoom()-zoom) + "/" + x + "/" + y + ".png";
       }                      
    }; 
    
    DefaultTileFactory tileFactory = new DefaultTileFactory(tileFactoryInfo);
    tileFactory.setUserAgent(IConf.TILE_USER_AGENT);       
    setTileFactory(tileFactory);  
     
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gbc.anchor = GridBagConstraints.NORTHEAST;                
      
    getMainMap().add(new OSMCreditPanel(), gbc);    
        
  }    
  
  
  
  
  /**
   * Ustawienie punktu adresowego i odpowiednie przybliżenie
   * @param point Współrzędne punktu
   */
  public void setAddressPoint(GeoPosition point) {
      
    setAddressLocation(point);
    getMainMap().setZoom(5);
                        
  }        
  
  
  /**
   * Wyświetlenie trasy na mapie 
   * @param routePoints Trasa (lista punktów odbioru z geometrią)
   * @param <T> Klasa punktu adresowego
   */  
  public <T extends IAddressPoint> void setRoute(List<T> routePoints) {
            
    List<Painter<JXMapViewer>> painters = new ArrayList<>();  
    Set<GeoPosition> pos = new HashSet<>();
    
    Iterator<T> iterator = routePoints.iterator();    
 
    while (iterator.hasNext()) {
        
      T point = iterator.next();      
      
      if (point.getAdditionalGeometryCoords() != null && !point.getAdditionalGeometryCoords().isEmpty()) {
        painters.add(new RoutePainter(point.getAdditionalGeometryCoords()));
      }
      
      if (point.getGeometryCoords() != null && !point.getGeometryCoords().isEmpty()) {
        painters.add(new RoutePainter(point.getGeometryCoords()));       
      }
      
      if (point.isOnRoute()) {
        GeoPosition pointPos = new GeoPosition(point.getCustomerLatitude(), point.getCustomerLongitude());
        pos.add(pointPos);
        painters.add(new WaypointLabelPainter<Waypoint>(new DefaultWaypoint(pointPos), point.getCustomerLabel()));
      }
      
    }    
        
    pos.add(new GeoPosition(routePoints.get(0).getCustomerLatitude(), routePoints.get(0).getCustomerLongitude()));
    
    finishRoute(painters, pos);
		
  }  
  
  

  
  private void finishRoute(List<Painter<JXMapViewer>> painters, Set<GeoPosition> pos) {
	  
	CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
	getMainMap().setOverlayPainter(painter);    		
	  
	ComponentListener listener = new ComponentListener() {
			
		@Override
		public void componentShown(ComponentEvent arg0) {}
			
		@Override
		public void componentResized(ComponentEvent arg0) {
		  getMainMap().zoomToBestFit(pos, 0.7);			
		  getMainMap().removeComponentListener(this);			
		}

		@Override
		public void componentHidden(ComponentEvent e) {}

		@Override
		public void componentMoved(ComponentEvent e) {}
	}; 
	
	getMainMap().addComponentListener(listener);
	  	  
  }
  
  
}


