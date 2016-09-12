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
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import datamodel.Customer;
import datamodel.Order;
import datamodel.Pack;
import datamodel.ShortestPathAlgorithm;
import gui.GUI;
import gui.dialogs.ErrorDialog;
import gui.loader.IProgress;
import gui.loader.IProgressInvoker;
import gui.loader.Loader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import jsprit.core.problem.Location;
import jsprit.core.problem.cost.AbstractForwardVehicleRoutingTransportCosts;
import jsprit.core.problem.driver.Driver;
import jsprit.core.problem.job.Job;
import jsprit.core.problem.vehicle.Vehicle;
import somado.AppObserver;
import somado.Database;
import somado.DatabaseLocal;
import somado.IConf;
import somado.Settings;
import somado.SettingsException;
import spatial_vrp.VRPListener.VehicleCurrentJobs;



/**
 *
 * Macierz kosztów przejazdu pomiędzy punktami adresowymi oraz geometrii najkrótszej trasy 
 * pomiędzy tymi punktami
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class CostMatrix extends AbstractForwardVehicleRoutingTransportCosts implements IProgressInvoker, Observer {
    
  /** Ref. do GUI */
  private final GUI frame;
  /** Baza danych przestrzennych */
  private final Database database;
  /** Wejściowa lista przesyłek */
  private final List<Pack> packs;
  /** Tablica odbiorców towaru (do zmapowania macierzy) */
  private final Customer[] customersArray;  
  /** Tablica przesyłek dla danego odbiorcy, indeksowana przez współrzędne macierzy */
  private final Pack[] packArray;
  /** Indeks magazynu w tablicy odbiorców */
  private int depotIndex;
  /** Ilość odbiorców towaru */
  private final int size;
  /** Tablica - macierz kosztów i geometrii najkrótszych tras */
  private final CostMatrixElem[][] paths;
  /** Tablica dodatkowej geometrii */
  private final Geometry[] additionalGeometry;
  /** Czy macierz wyznaczona */
  private boolean done = false;
  /** Algorytm wyznaczania najkrótszej ścieżki */
  private final ShortestPathAlgorithm algorithm;
  /** Bieżący ładunek */
  private final Map<Integer, List<Job>> currentJobs;

    
  /**
   * Konstruktor
   * @param frame Ref. do GUI
   * @param packs  Wejściowa lista odbiorców towaru
   * @throws SettingsException Błąd ustawień (brak położenia magazynu)
   */
  public CostMatrix(GUI frame, List<Pack> packs) throws SettingsException {
          
    this.frame = frame;
    try {
      database = DatabaseLocal.getInstance();
    }
    catch (SQLException | ClassNotFoundException | NullPointerException e) {              
      throw new SettingsException("B\u0142\u0105d bazy danych przestrzennych: " + e);            
    }  
    
    currentJobs = new HashMap<>();
    
    this.packs = packs;
    this.size = packs.size()+1;
    paths = new CostMatrixElem[size][size];   
    additionalGeometry = new Geometry[size];
    customersArray = new Customer[size];
    packArray = new Pack[size];
    algorithm = ShortestPathAlgorithm.get(Settings.getValue("shortest_path_algorithm"));
    
    new Loader(frame, "Trwa wyznaczanie najkr\u00f3tszych tras", this, true).load();
    
  }  
  
  
  /**
   * Wyliczenie kosztów przejazdu i geometrii połączeń między wszystkimi punktami
   * @param progress Interfejs okna z paskiem postępu
   * @throws SQLException Błąd SQL
   * @throws ParseException Błąd parsowania geometrii
   */
  private void calculateCosts(IProgress progress) throws SQLException, ParseException {

      
    // wyznaczenie najbliższych węzłów sieci drogowej dla każdego z odbiorców towaru   
    int i = 0; 
    packs.add(new Pack(new Customer(Settings.getDepot())));
    Iterator<Pack> iterator = packs.iterator();
    while (iterator.hasNext()) {
        
       Pack pack = iterator.next(); 
       Customer c = pack.getCustomer();
       int node = getRoadNodeId(c.getLongitude(), c.getLatitude());
       if (node == 0) throw new SQLException("Nie mo\u017cna odnale\u017b\u0107 w\u0119z\u0142a "
               + "sieci drogowej dla lokalizacji:\n" + c);
       c.setRoadNodeId(node);
       c.setIndex(i);
       if (c.getId().equals(Settings.getDepot().getId())) depotIndex = i;       
       packArray[i] = pack;       
       customersArray[i] = c;
       i++;
       
    }          
    
    int totalSize = size*size;
    
    // ustawienie algorytmu wyznaczania najkrótszej ścieżki
    PreparedStatement ps = database.prepareQuery("UPDATE roads_net SET algorithm = ? ;");
    ps.setString(1, algorithm.toString());
    ps.executeUpdate();
    
    boolean addGeo = false;
    try {
      addGeo = (Settings.getIntValue("additional_geometry") == 1); 
    }
    catch (NumberFormatException e) {}
    
    // budowa symetrycznej macierzy  
    for (i=0;i<size;i++) {
        
      // w razie naciśnięcia "anuluj" na pasku postępu  
      try {
        Thread.sleep(10);
      } catch (InterruptedException ex) {
        try {  
          database.close();
        }
        catch (Exception e) {}
        return;
      }
        
      if (addGeo) { 
        additionalGeometry[i] = findAdditionalGeometry(i);        
        if (progress != null) 
          progress.setProgress(progress.getProgress()
                            + (int) ((Double.valueOf(i*size + 0.5)/totalSize) * 100.0));   
      }
        
      for (int j=0;j<size;j++) {                
        
        if (i==j)  paths[i][j] = new CostMatrixElem(0.0, 0.0);
        else if (i>j)  paths[i][j] = new CostMatrixElem(paths[j][i]);
        else if (customersArray[i].getRoadNodeId() == customersArray[j].getRoadNodeId()) {
            
          paths[i][j] = new CostMatrixElem(0.0, 0.0, null);  
            
        }
        else {

          // wyznaczenie najkrótszej ścieżki
          ps = database.prepareQuery("SELECT Cost, AsText(Geometry) AS geom, ArcRowId "
                  + "FROM roads_net where NodeFrom=? AND NodeTo=? ");
          ps.setInt(1, customersArray[i].getRoadNodeId());
          ps.setInt(2, customersArray[j].getRoadNodeId());
          ResultSet rs = ps.executeQuery();
    
          if (rs.next())
          try {
             
            WKTReader wktReader = new WKTReader();
            String geom = rs.getString("geom");
            // pusta geometria (nie można wyznaczyć trasy)
            if (geom.equals("LINESTRING()")) {
               throw new ParseException("Nie mo\u017cna wyznaczy\u0107 "
               + "trasy pomi\u0119dzy lokalizacjami:\n"
               + customersArray[i] + "\n" + customersArray[j]);
            }
            
            Geometry geometry = wktReader.read(geom);
            paths[i][j] = new CostMatrixElem(0.0, rs.getDouble("Cost"),  geometry);            
          
          }
          // brak połączenia pomiędzy węzłami
          catch (NullPointerException ex) {
                        
            throw new SQLException("Brak po\u0142\u0105czenia pomi\u0119dzy w\u0119z\u0142ami sieci drogowej:\n" 
                                  + customersArray[i].toString() + "\n" + customersArray[j].toString());
           
          }
         
          double distance = 0.0; 
         
          while (rs.next()) {
             
             ps = database.prepareQuery("SELECT length FROM roads WHERE id = ?");
             ps.setInt(1, rs.getInt("ArcRowId"));
             ResultSet rs2 = ps.executeQuery();
             if (rs2.next()) distance += rs2.getDouble("length");            
             
           }
         
           paths[i][j].setDistance(distance);     
        
           CostMatrixElem tmp = new CostMatrixElem(paths[i][j]);
           RoadFixedGeometry fixed = new RoadFixedGeometry(tmp, additionalGeometry[i], additionalGeometry[j]);
           paths[i][j].setDistance(paths[i][j].getDistance() + fixed.getFixedLength());
           
         }        
        
        
         if (progress != null) 
         progress.setProgress(progress.getProgress()
                            + (int) ((Double.valueOf(i*size+j)/totalSize) * 100.0));   
       
       }
                       
       
     }
     
  } 
  
  
  /**
   * Odnalezienie dodatkowej geometrii dla każdego z punktów 
   * (dodatkowy fragment drogi od punktu odbioru do najbliższego węzła drogi)     
   * @param i Indeks punktu (kolumna w macierzy kosztów)
   * @return Dodatkowa geometria
   * @throws SQLException Błąd SQL
   * @throws ParseException Błąd parsowania geometrii
   */
  private Geometry findAdditionalGeometry(int i) throws SQLException, ParseException {
      
      
    // Uzywany system odniesienia WGS-84 (EPSG:4326)  
    PreparedStatement ps = database.prepareQuery("SELECT rpath, node_from FROM (SELECT "
              + "AsText(Line_Substring(r.geometry, 0.0, Line_Locate_Point(r.geometry, MakePoint(?, ?, 4326)))) "
              + "AS rpath, r.node_from FROM roads AS r "
              + "WHERE r.node_from = ?  AND oneway_fromto = 1 "
              + "UNION SELECT "
              + "AsText(Line_Substring(r.geometry, Line_Locate_Point(r.geometry, MakePoint(?, ?, 4326)), 1.0)) "
              + "AS rpath, r.node_from FROM roads AS r "
              + "WHERE r.node_to = ?  AND oneway_tofrom = 1) "
              + "WHERE rpath IS NOT NULL ORDER BY Distance(rpath, MakePoint(?, ?, 4326)) LIMIT 1");
      
    ps.setDouble(1, customersArray[i].getLongitude());
    ps.setDouble(2, customersArray[i].getLatitude());
    ps.setInt(3, customersArray[i].getRoadNodeId());
    ps.setDouble(4, customersArray[i].getLongitude());
    ps.setDouble(5, customersArray[i].getLatitude());
    ps.setInt(6, customersArray[i].getRoadNodeId());    
    ps.setDouble(7, customersArray[i].getLongitude());
    ps.setDouble(8, customersArray[i].getLatitude()); 
    
    ResultSet rs = ps.executeQuery();
    if (!rs.next()) return null;
    
    return new WKTReader().read(rs.getString("rpath"));
      
  }
  
     
  
  
  /**
   * Metoda zwraca wskazany element macierzy
   * @param i Indeks kolumny (lokalizacja 1)
   * @param j Indeks wiersza (lokalizacja 2)
   * @return Element macierzy
   */
  public CostMatrixElem getPath(int i, int j) {
    
    try {  
       return paths[i][j]; 
    }
    catch (IndexOutOfBoundsException e) {
       return null;  
    }
      
  }

  /**
   * Zwrócenie dodatkowej geometrii dla punktu odbioru towaru
   * @param i Indeks elementu macierzy (punkt odbioru)
   * @return Dodatkowa geometria (odcinek drogi od najbliższego węzła)
   */
  public Geometry getAdditionalGeometry(int i) {
   
    return additionalGeometry[i];
        
  }
  
  
  /**
   * Zwrócenie aktualnej listy przesyłek
   * @return Aktualna (uzupełniona) lista przesyłek
   */
  public List<Pack> getPacksList() {
      
     return new ArrayList<>(packs); 
      
  }
  

  /**
   * Zwrócenie indeksu magazynu dla macierzy kosztów
   * @return Indeks magazynu dla macierzy kosztów
   */
  public int getDepotIndex() {
      
     return depotIndex;
     
  }  
  
  
  @Override
  public String toString() {
             
    String tmp = super.toString() + "\n\n"; 
    
    tmp += "--------------------------------------------------\n";
    
    tmp += "Algorithm: " + algorithm.toString() + "\n";
      
    for (int i=0;i<size;i++) 
      tmp += String.valueOf(i) + ". " + customersArray[i].toString() + " | roadNode \u0023"
              + String.valueOf(customersArray[i].getRoadNodeId()) + "\n";
      
    tmp += "--------------------------------------------------\n";
    
    for (int i=0;i<size;i++) {
        
      tmp += String.valueOf(i) + "   ";  
        
      for (int j=0;j<size;j++) {
        
        String t = String.format("%.4f", paths[i][j].getTime());
        if (t.length()<10) for (int k=t.length();k<10;k++) t+=" ";
        tmp+=t+"  ";
        
      }
      
      tmp += "\n";
              
    } 
    
    tmp += "--------------------------------------------------\n\n";
    
    return tmp;
      
  }
     
       
     
  /**
   * Metoda zwracająca najbliższy węzeł sieci drogowej
   * @param longitude Długość geograficzna
   * @param latitude Szerokość geograficzna
   * @return Identyfikator węzła w sieci drogowej
   * @throws SQLException Błąd SQL
   */
  private int getRoadNodeId(double longitude, double latitude) throws SQLException {
      
    int nodeId = 0;
    
    // Uzywany system odniesienia WGS-84 (EPSG:4326)
    PreparedStatement ps = database.prepareQuery("SELECT node_id,"
            + " Distance(geometry, MakePoint(?, ?, 4326)) AS dist  "
            + "FROM roads_nodes WHERE ROWID IN "
              + "(SELECT ROWID FROM SpatialIndex WHERE f_table_name = 'roads_nodes' AND "
              + "search_frame = BuildCircleMbr(?, ?, 0.1, 4326)) "
              + "ORDER BY dist LIMIT 1;");
     
    ps.setDouble(1, longitude);
    ps.setDouble(2, latitude);
    ps.setDouble(3, longitude);
    ps.setDouble(4, latitude);
        
    ResultSet rs = ps.executeQuery();

    if (rs.next()) nodeId = rs.getInt("node_id");
     
    return nodeId;
      
  }   
  

  
  
  @Override
  public double getTransportTime(Location lctn, Location lctn1, double d, Driver driver, Vehicle vhcl) {
         
    return paths[lctn.getIndex()][lctn1.getIndex()].getTime();
    
  }
    

  @Override
  public double getTransportCost(Location lctn, Location lctn1, double d, Driver driver, Vehicle vhcl) {
      
    // Odległość między lokalizacjami w km
    double distance = paths[lctn.getIndex()][lctn1.getIndex()].getDistance()/1000.0;  
    
    if (distance == 0.0) return 0.0;
    
    // Waga aktualnie przewożonego ładunku w setkach kg
    double currentLoad = 0.0;
    
    // Średnie zużycie paliwa na 100 km 
    double fuelCons = IConf.MIN_VEHICLE_FUEL_CONSUMPTION; 
    
    try {
        
      fuelCons = vhcl.getType().getVehicleCostParams().perDistanceUnit;   
      int vehicleId = Integer.parseInt(vhcl.getId());      
      
      for (Job job : currentJobs.get(vehicleId)) currentLoad += job.getSize().get(0);
      
      boolean jobServed = false;
        
      for (Job job : currentJobs.get(vehicleId)) {
           
        // szukanie na liście zadania przydzielonego do bieżącej lokalizacji  
        for (Order order : packArray[lctn1.getIndex()].getOrdersList()) {
            
          if (order.getId() == Integer.parseInt(job.getId())) {              
             jobServed = true;
             break;              
          }
            
        }
        
        if (jobServed) break;
        currentLoad -= job.getSize().get(0);        
          
      }                  
          
      // wartość w setkach kg
      currentLoad /= 100.0;
            
    }
    catch (NullPointerException e) {  }     
    
    // Dodane oszacowane dodatkowe zużycie paliwa na każde 100 kg ładunku 
    fuelCons += fuelCons * IConf.ADDITIONAL_FUEL_CONSUMPTION_PER_100KG * currentLoad;
    
    // Koszt jako przewidywana ilość zużytego paliwa na dystansie pomiędzy lokalizacjami
    return (distance/100.0) * fuelCons;
    
  }

  
  public boolean isDone() {
      return done;
  }
 
  
  
  @Override
  public void start(IProgress progress) {
    
    try { 
        
       calculateCosts(progress);   
       done = true;
       
    }
    catch (SQLException | ParseException e) {
        
      new ErrorDialog(frame, "B\u0142\u0105d bazy danych przestrzennych: " + e, true);  
     
    }
    
    try {  
      database.close();
    }
    catch (Exception e) {}
     
    progress.hideComponent();
      
  }

  
  /**
   * Metoda ustawia referencje przekazane przez obserwatora
   * @param o Obserwowany obiekt 
   * @param object Przekazany obiekt
   */
  @Override
  public void update(Observable o, Object object) {  
       
      AppObserver obs = (AppObserver)object;
     
      switch (obs.getKey()) {
         
        case "vehicle_current_load":
            {
              
              VehicleCurrentJobs vl = (VehicleCurrentJobs)obs.getObject(); 
              currentJobs.put(vl.vehicleId, vl.jobs);
              
              break;
                
            }
            
        case "vehicle_current_load_remove":
            {
              
              VehicleCurrentJobs vl = (VehicleCurrentJobs)obs.getObject(); 
              currentJobs.remove(vl.vehicleId, vl.jobs);
              
              break;
                
            }
            
        case "vehicle_current_clear":
            {
               
              currentJobs.clear();
              break;
                
            }
        
    }
  
  }
  
  
      
}
