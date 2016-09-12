/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.mapview;

import com.vividsolutions.jts.geom.Geometry;
import datamodel.DeliveryDriverOrder;
import spatial_vrp.RoutePoint;
import java.awt.GridBagConstraints;
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
import org.jxmapviewer.viewer.WaypointPainter;
import somado.Settings;
import spatial_vrp.RoadFixedGeometry;


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
    
  /** Współrzędne centralnego punktu mapy */
  private final GeoPosition centerPosition;  
 
    
  /**
   * Konstruktor
   * @param host URL usługi TMS
   * @param centerPosition Współrzędne centralnego punktu mapy
   */
  public MapPanel(String host, GeoPosition centerPosition) {
      
    super();                  
            
    this.centerPosition = centerPosition;
    
    // niezaimplementowane w JXMapKit...
    setDataProviderCreditShown(false);
    
    
    TileFactoryInfo tileFactoryInfo = new TileFactoryInfo(2, 16, 18, 256, true, true, host, "x", "y", "z") {
       @Override
       public String getTileUrl(int x, int y, int zoom) {                   
         //return this.baseURL +"?x="+x+"&y="+y+"&z="+(this.getTotalMapZoom()-zoom);
         return this.baseURL + "/" + (this.getTotalMapZoom()-zoom) + "/" + x + "/" + y + ".png";
       }                      
    }; 
           
    setTileFactory(new DefaultTileFactory(tileFactoryInfo));  
     
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
    HashSet<GeoPosition> pos = new HashSet<>();
    pos.add(point);
    getMainMap().zoomToBestFit(pos, 0.9);
                        
  }
  
  
  
     
  /**
   * Wyświetlenie trasy na mapie dla planu dostawy
   * @param routePoints Trasa (lista punktów odbioru z geometrią)
   */  
  public void setRoute(List<RoutePoint> routePoints) {
            
    List<Painter<JXMapViewer>> painters = new ArrayList<>();  
    List<WaypointPainter<Waypoint>> wpPainters = new ArrayList<>(); 
    
    RoadFixedGeometry fixed = new RoadFixedGeometry(routePoints);
    
    Iterator<RoutePoint> iterator = routePoints.iterator();    
 
    while (iterator.hasNext()) {
        
      RoutePoint point = iterator.next();      
      
      Geometry additionalGeometry = fixed.getFixedAdditionalGeometry(point.getId());
      if (additionalGeometry != null) painters.add(new RoutePainter(RoutePoint.getCoordsList(additionalGeometry))); 
      
      Geometry geometry = fixed.getFixedGeometry(point.getId());
      if (geometry != null) painters.add(new RoutePainter(RoutePoint.getCoordsList(geometry)));       
      
      if (point.getOrder() != null) 
        wpPainters.add(new WaypointLabelPainter<Waypoint>(new DefaultWaypoint(new GeoPosition(point.getOrder().getCustomer().getLatitude(),
                  point.getOrder().getCustomer().getLongitude())), point.getLabel()));      
      
    }    
    
    // dla całego zestawu punktów nie działa ...
    Set<GeoPosition> pos = new HashSet<>();
    pos.add(new GeoPosition(Settings.getDepot().getLatitude(), Settings.getDepot().getLongitude()));
    getMainMap().zoomToBestFit(pos, 0.3);
   
    for (WaypointPainter<Waypoint> p : wpPainters) painters.add(p);
		
    CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
    getMainMap().setOverlayPainter(painter); 
    
  
  }  
  
  

  /**
   * Wyświetlenie trasy na mapie dla zatwierdzonej dostawy
   * @param routePoints Trasa (lista punktów odbioru z geometrią)
   */  
  public void setFinalRoute(List<DeliveryDriverOrder> routePoints) {
            
    List<Painter<JXMapViewer>> painters = new ArrayList<>();  
    List<WaypointPainter<Waypoint>> wpPainters = new ArrayList<>(); 
    
    Iterator<DeliveryDriverOrder> iterator = routePoints.iterator();    
 
    while (iterator.hasNext()) {
        
      DeliveryDriverOrder point = iterator.next();      
      
      if (point.getAdditionalGeometryCoords() != null)
        painters.add(new RoutePainter(point.getAdditionalGeometryCoords())); 
      
      if (point.getGeometryCoords() != null)
        painters.add(new RoutePainter(point.getGeometryCoords()));       
      
      wpPainters.add(new WaypointLabelPainter<Waypoint>(new DefaultWaypoint(new GeoPosition(point.getCustomerLatitude(),
                point.getCustomerLongitude())), point.getCustomerLabel()));      
      
    }    
    
    // dla całego zestawu punktów nie działa ...
    Set<GeoPosition> pos = new HashSet<>();
    pos.add(new GeoPosition(Settings.getDepot().getLatitude(), Settings.getDepot().getLongitude()));
    getMainMap().zoomToBestFit(pos, 0.3);
   
    for (WaypointPainter<Waypoint> p : wpPainters) painters.add(p);
		
    CompoundPainter<JXMapViewer> painter = new CompoundPainter<>(painters);
    getMainMap().setOverlayPainter(painter); 
    
  
  }  
  
  
           
  
  
    
  
}


