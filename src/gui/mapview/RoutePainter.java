/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */

package gui.mapview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.painter.Painter;


/**
 * Rysowanie trasy na mapie
 * Na podstawie dokumentacji JXMapViewer, autor oryginału: Martin Steiger
 * @author Maciej Kawecki
 */
public class RoutePainter implements Painter<JXMapViewer> {
   
   /** Kolor trasy */ 
   private final static Color ROUTE_COLOR = Color.RED; 
   /** Kolor dodatkowych odcinków trasy (do testów) */
   private final static Color ROUTE_ADD_COLOR = ROUTE_COLOR;//new Color(0xff6633);
   /** Trasa jako lista punktów */ 	
   private final List<GeoPosition> track;
   /** Czy to dodatkowa geometria */
   private final boolean additional;
   
   
   /**
    * Konstruktor
    * @param track Trasa jako lista punktów
    * @param additional Czy to dodatkowa geometria
    */
   public RoutePainter(List<GeoPosition> track, boolean additional) {
		
     this.track = new ArrayList<>(track);
     this.additional = additional;
	
   }
   
   
   
   /**
    * Konstruktor
    * @param track Trasa jako lista punktów
    */
   public RoutePainter(List<GeoPosition> track) {
		
     this(track, false);
	
   }
   

   @Override
   public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
	
     g = (Graphics2D) g.create();

     // przekształcenie współrzędnych z widoku mapy do wsp. bitmapy
     Rectangle rect = map.getViewportBounds();
     g.translate(-rect.x, -rect.y);

     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

     g.setColor(Color.BLACK);
     g.setStroke(new BasicStroke(4));
     drawRoute(g, map);
     g.setColor(additional ? ROUTE_ADD_COLOR : ROUTE_COLOR);
     g.setStroke(new BasicStroke(2));
     drawRoute(g, map);          

     g.dispose();

   }
   
   /**
    * Rysowanie trasy
    * @param g Obiekt grafiki Java2D
    * @param map Obiekt mapy
    */
   private void drawRoute(Graphics2D g, JXMapViewer map) {
	
      int lastX = 0;
      int lastY = 0;
      boolean first = true;
		
      for (GeoPosition pos : track) {
			
        // przekształcenie współrzędnych geograficznych do wsp. bitmapy
	Point2D point = map.getTileFactory().geoToPixel(pos, map.getZoom());

	if (first) first = false;
        else g.drawLine(lastX, lastY, (int) point.getX(), (int) point.getY());
	
        lastX = (int) point.getX();
	lastY = (int) point.getY();
		
      }
      
   }
   
}
