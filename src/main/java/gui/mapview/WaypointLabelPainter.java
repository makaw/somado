/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.mapview;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

/**
 * Rysowanie trasy na mapie - rozszerzenie zaznaczania punktów o możliwość wypisywania etykiet
 * 
 * @author Maciej Kawecki
 * @param <E> Klasa obiektu punktu trasy
 */
public class WaypointLabelPainter<E extends Waypoint> extends WaypointPainter<E> {
    
  /** Czcionka dla etykiet */
  private static final Font LAB_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 10);  
  /** Kolor etykiet */
  private static final Color LAB_COLOR = Color.BLACK;
  /** Treść etykiety */  
  private final  String label;
  /** Położenie geograficzne punktu */
  private final GeoPosition position;

  /**
   * Konstruktor
   * @param waypoint Punkt trasy
   * @param label Treść etykiety punktu
   */
  public WaypointLabelPainter(E waypoint, String label) {
      
    super();

    Set<E> wp = new HashSet<>();
    wp.add(waypoint);
    super.setWaypoints(wp);    
    super.setAntialiasing(true);
    
    this.position = waypoint.getPosition();
    this.label = label;
    
  }  
  
  
  @Override
  public void doPaint(Graphics2D g, JXMapViewer map, int w, int h) {
       
     super.doPaint(g, map, w, h);
       
     g = (Graphics2D) g.create();

     // przekształcenie współrzędnych z widoku mapy do wsp. bitmapy
     Rectangle rect = map.getViewportBounds();
     g.translate(-rect.x, -rect.y);
     
     // przekształcenie współrzędnych geograficznych do wsp. bitmapy
     Point2D point = map.getTileFactory().geoToPixel(position, map.getZoom());

     // wypisanie etykiety
     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);     
     g.setColor(LAB_COLOR);
     g.setFont(LAB_FONT);    
     int xo = (label.length()>1 ? 8 : (!label.equals("M") ? 4 : 5));
     g.drawString(label, (int) point.getX() - xo, (int) point.getY() - 20);
     
     g.dispose();
       
   }
    
    
}
