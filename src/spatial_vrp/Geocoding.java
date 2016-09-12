/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package spatial_vrp;


import datamodel.GeoAddress;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.request.NominatimSearchRequest;
import fr.dudie.nominatim.model.Address;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import somado.Database;
import somado.IConf;
import somado.Settings;


/**
 *
 * Szablon obiektu reprezentującego zapytanie geokodujące do usługi Nominatim 
 * (z uwzględnieniem cache'owania zapytań)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class Geocoding {          
            
  /** Lista rekordów odpowiedzi usługi na zapytanie */  
  private List<GeoAddress> response = null;
  /** Referencja do bazy danych z cache geokodowania */
  private final Database database;
  /** Zapytanie */
  private final String request;
  
    
  /**
   * Konstruktor
   * @param database Ref. do bazy danych z cache geokodowania
   * @param request Zapytanie
   * @throws IOException Błąd I/O (problem z połączeniem)
   * @throws java.sql.SQLException Błąd SQL (cache SQLite)
   */
  public Geocoding(Database database, String request) throws IOException, SQLException {
      
     this.database = database;  
     this.request = request;
      
     response = new ArrayList<>();
     
     if (!testCache()) {
       Iterator<Address> iterator = nominatimRequest().iterator();
       while (iterator.hasNext()) {
          
         Address tmp = iterator.next();
         GeoAddress addr = new GeoAddress(tmp.getDisplayName(), tmp.getLongitude(), tmp.getLatitude());
         response.add(addr);  
         cacheAddr(addr);
         
       }      
     }
     
  }
      
  /**
   * Wysłanie zapytania do usługi OSM Nominatim
   * @return Lista odpowiedzi
   * @throws IOException Błąd I/O (połączenie)
   */
  private List<Address> nominatimRequest() throws IOException {
      
     CloseableHttpClient httpclient = HttpClients.createDefault();
        
     JsonNominatimClient nominatimClient = new JsonNominatimClient(httpclient, IConf.EMAIL_NOMINATIM);
     NominatimSearchRequest nominatimRequest = new NominatimSearchRequest();
     nominatimRequest.addCountryCode("PL");
     
     nominatimRequest.setViewBox(Settings.getWestBoxCoord(), Settings.getNorthBoxCoord(), 
             Settings.getEastBoxCoord(), Settings.getSouthBoxCoord());    
     nominatimRequest.setBounded(true);
     
     nominatimRequest.setQuery(request);
     return nominatimClient.search(nominatimRequest);     
    
  }


  public List<GeoAddress> getResponse() {
 
     return response;
     
  }
  
  /**
   * Sprawdzenie czy zapytanie istnieje w cache
   * @return True jeżeli jest w cache
   * @throws SQLException Błąd SQL
   */
  private boolean testCache() throws SQLException {
    
    response.clear();
    
    PreparedStatement ps = database.prepareQuery("SELECT id, display_name, longitude, latitude "
            + " FROM ncache.nominatim_cache WHERE request = ? ");
    ps.setString(1, request);
    
    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
        
      response.add(new GeoAddress(rs));
        
    } 
    
    return !response.isEmpty();
            
  }
  
  
  /**
   * Dodanie pojedynczego wyniku dla zapytania do cache
   * @param addr Pojedynczy wynik geokodowania
   * @throws SQLException Błąd SQL
   */
  private void cacheAddr(GeoAddress addr) throws SQLException {
   
     PreparedStatement ps = database.prepareQuery("INSERT INTO ncache.nominatim_cache (request, display_name, "
             + "longitude, latitude, date_add) VALUES (?, ?, ?, ?, DATE('now'));");
     ps.setString(1, request);
     ps.setString(2, addr.getDisplayName());   
     ps.setDouble(3, addr.getLongitude());
     ps.setDouble(4, addr.getLatitude());
     
     ps.executeUpdate();
       
  }  
  
  
 
}
