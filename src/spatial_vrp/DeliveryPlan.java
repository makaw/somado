/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package spatial_vrp;

import datamodel.Audit;
import datamodel.AuditDiff;
import datamodel.Delivery;
import datamodel.Order;
import datamodel.OrderState;
import datamodel.Pack;
import datamodel.docs.DocAudit;
import datamodel.tablemodels.RoutePointsTableModel;
import gui.GUI;
import gui.dialogs.ErrorDialog;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import jsprit.core.problem.job.Job;
import jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import jsprit.core.problem.solution.route.VehicleRoute;
import jsprit.core.problem.solution.route.activity.TourActivity;
import org.jxmapviewer.viewer.GeoPosition;
import somado.Database;
import somado.IConf;
import somado.Settings;
import somado.SettingsException;
import somado.User;


/**
 *
 * Zaplanowanie nowej dostawy (utworzenie macierzy kosztów, rozwiązanie problemu VRP)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class DeliveryPlan {
    
  /** Ref. do GUI */  
  private final GUI frame;  
  /** Najlepsze wyznaczone rozwiązanie problemu VRP */
  private VehicleRoutingProblemSolution solution;
  /** Maksymalny czas jazdy kierowcy w dostawie */
  private double maxDriverWorkTime = IConf.DEFAULT_MAX_DRIVER_WORK_TIME * 3600.0;
  /** Macierz kosztów i najkrótszych ścieżek */
  private CostMatrix costMatrix;
  /** Lista kierowców */
  private final List<datamodel.Driver> drivers;
  /** Lista przesyłek */
  private final List<Pack> packs;
  
  /**
   * Konstruktor do wyznaczenia nowego planu dostawy (nowego rozwiązania)
   * @param frame Ref. do GUI
   * @param packs Lista wybranych paczek (zamówień pogrupowanych wg punktów dostawy)
   * @param drivers Lista wybranych dostępnych kierowców
   */
  public DeliveryPlan(GUI frame, List<Pack> packs, List<datamodel.Driver> drivers) {
           
    this.frame = frame;  
    this.drivers = drivers;
    this.packs = packs;  
    
    try {  
              
      costMatrix = new CostMatrix(frame, packs);  
      if (costMatrix.isDone()) {
        VRPSolver vrp = new VRPSolver(frame, costMatrix, drivers);
        solution = vrp.getBestSolution();
        maxDriverWorkTime = vrp.getMaxDriverWorkTime();
      }
    
    }    

    catch (SettingsException e) {
        
      new ErrorDialog(frame, e.getMessage(), true);  
        
    }        
        
                
  }
  
  /**
   * Metoda zwraca łączny koszt rozwiązania
   * @return Łączny koszt
   */
  public double getTotalCost() {
      
    return solution != null ? solution.getCost() : 0.0;
      
  }

  
  public double getMaxDriverWorkTime() {
     return maxDriverWorkTime;
  }      
  
  
  /**
   * Metoda zwraca listę nieobsłużonych zamówień
   * @return Lista nieobsłużonych zamówień
   */
  public List<Order> getUnassignedOrders() {
      
    List<Order> orders = new ArrayList<>();
    if (solution == null) return orders;
    
    //GlossOrders glossOrders = new GlossOrders(frame.getDatabaseShared());
    
    for (Job job: solution.getUnassignedJobs()) {
        
        //glossOrders.getItem(Integer.parseInt(job.getId())));
      orders.add(getOrderById(Integer.parseInt(job.getId())));
        
    }
    
    return orders;
      
  }
  
  
  /**
   * Metoda zwraca liczbę zaangażowanych kierowców
   * @return Liczba zaangażowanych kierowców
   */
  public int getDriversNumber() {
      
    if (solution == null) return 0;
    
    return solution.getRoutes().size(); 
      
  }
  
  
  /**
   * Metoda zwraca wskazane zamówienie z wejściowej listy przesyłek
   * @param orderId ID bazodanowe zamówienia
   * @return Zamówienie
   */
  private Order getOrderById(int orderId) {
      
    
    Order order = null;
    Iterator<Pack> iterator = packs.iterator();
    while (order == null && iterator.hasNext()) {

       for (Order tmp : iterator.next().getOrdersList()) 
         if (tmp.getId() == orderId) {
           order = tmp;
           break;
         }
    }
    
    return order;      
      
  }
  
  
  /**
   * Metoda zwraca kierowcę wskazanego pojazdu z wejściowej listy
   * @param orderId ID bazodanowe pojazdu
   * @return Kierowca
   */  
  private datamodel.Driver getDriverByVehicleId(int vehicleId) {
      
    datamodel.Driver driver = null;
    Iterator<datamodel.Driver> iterator = drivers.iterator();
    while (iterator.hasNext()) {
       datamodel.Driver tmp = iterator.next();
       if (tmp.getVehicle().getId() == vehicleId) {
          driver = tmp;
          break;
       }
    }
    
    return driver;
      
  }
  
  
  /**
   * Metoda zwraca listę ułożonych tras
   * @return Lista ułożonych tras
   */
  public List<Route> getRoutes() {
      
    List<Route> routes = new ArrayList<>();
    if (solution == null) return routes;
    
    //GlossOrders glossOrders = new GlossOrders(frame.getDatabaseShared());
    //GlossDrivers glossDrivers = new GlossDrivers(frame.getDatabaseShared());
    
    for (VehicleRoute vRoute: solution.getRoutes()) {
        
      datamodel.Driver driver = getDriverByVehicleId(Integer.parseInt(vRoute.getVehicle().getId()));
      //datamodel.Driver driver = glossDrivers.getItemByVehicleOrDriverKey(Integer.parseInt(vRoute.getVehicle().getId()), true);
      
      List<RoutePoint> points = new ArrayList<>();
      // punkt startu = magazyn
      TourActivity lastAct = vRoute.getStart(); 
      
      int pointId = 0;
      
      points.add(new RoutePoint(pointId++, 0.0, 0.0, new Order(Settings.getDepot()), null, 
              costMatrix.getAdditionalGeometry(lastAct.getLocation().getIndex())));

      Iterator<TourActivity> iterator = vRoute.getTourActivities().iterator();
      while (iterator.hasNext()) {
      
        TourActivity act = iterator.next();
        int jobId = (act instanceof TourActivity.JobActivity) ?
                Integer.parseInt(((TourActivity.JobActivity) act).getJob().getId()) : 0;
   
        //glossOrders.getItem(jobId)
        Order order = (jobId>0) ? getOrderById(jobId) : new Order(Settings.getDepot());
        int index = act.getLocation().getIndex();
        int lastIndex = lastAct.getLocation().getIndex();
    
        CostMatrixElem path = costMatrix.getPath(lastIndex, index);
        points.add(new RoutePoint(pointId++, path.getDistance(), path.getTime(), order, path.getGeometry(),
                costMatrix.getAdditionalGeometry(index)));         
                
        lastAct = act;
          
      }

      // punkt końcowy - magazyn
      if (driver.isReturnToDepot()) {
        TourActivity act = vRoute.getEnd();
        int index = act.getLocation().getIndex();
        int lastIndex = lastAct.getLocation().getIndex();
        CostMatrixElem path = costMatrix.getPath(lastIndex, index);
        points.add(new RoutePoint(pointId++, path.getDistance(), path.getTime(), 
                new Order(Settings.getDepot()), path.getGeometry(), costMatrix.getAdditionalGeometry(index)));
      }
      
      routes.add(new Route(driver, points));
        
    }
    
      
    return routes;
      
  }
  
  
  /**
   * Czy plan jest gotowy
   * @return True jeżeli gotowy
   */
  public boolean isDone() {
      
    return solution != null;  
      
  }
  
  
  /**
   * Zatwierdzenie planu dostawy - zapis dostawy do BD
   * @param deliveryDate Data dostawy
   * @param user Ref. do obiektu zalogowanego użytkownika
   * @throws SQLException Błąd SQL
   */
  public void savePlan(Date deliveryDate, User user) throws SQLException {
      
    if (!isDone()) return;
    
    List<Route> routes = getRoutes();
    if (routes.isEmpty()) return;
    
    Database database = frame.getDatabaseShared();   
      
    try {      
    
      database.doUpdate("START TRANSACTION;"); 
    
      boolean addGeo = false;
      try {
        addGeo = (Settings.getIntValue("additional_geometry") == 1); 
      }
      catch (NumberFormatException e) {}
      
      PreparedStatement ps = database.prepareQuery("INSERT INTO dat_deliveries (id, delivery_date, "
            + "driver_max_work_time, shortest_path_algorithm, additional_geometry, "
            + "depot_desc, depot_longitude, depot_latitude, total_cost, active, date_add, user_add_id) "
            + "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, 1, NOW(), ?);", true);
    
      ps.setString(1, Settings.DATE_FORMAT.format(deliveryDate));
      ps.setDouble(2, getMaxDriverWorkTime());
      ps.setString(3, Settings.getValue("shortest_path_algorithm"));
      ps.setBoolean(4, addGeo);
      ps.setString(5, Settings.getDepot().toString());
      ps.setDouble(6, Settings.getDepot().getLongitude());
      ps.setDouble(7, Settings.getDepot().getLatitude());
      ps.setDouble(8, getTotalCost());
      ps.setInt(9, user.getId());
      
      ps.executeUpdate();
     
      ResultSet rs = ps.getGeneratedKeys();
      if (!rs.next())  throw new SQLException("b\u0142ad zapisu");  
      int deliveryId = rs.getInt(1);
    
        
      for (Route route: routes) {
          
         ps = database.prepareQuery("INSERT INTO dat_deliveries_drivers (id, delivery_id, driver_user_id, driver_desc, "
                 + "vehicle_registration_no, vehicle_desc, return_to_depot) VALUES (NULL, ?, ?, ?, ?, ?, ?);", true);
         ps.setInt(1, deliveryId);
         ps.setInt(2, route.getDriver().getUserData().getId());
         ps.setString(3, route.getDriver().getUserData().getSurname() + " " + route.getDriver().getUserData().getFirstname());
         ps.setString(4, route.getDriver().getVehicle().getRegistrationNo());
         ps.setString(5, "[max " + String.format("%.2f", route.getDriver().getVehicle().getVehicleModel().getMaximumLoad()) 
                 + "t] " + route.getDriver().getVehicle().toString());
         ps.setBoolean(6, route.getDriver().isReturnToDepot());
       
         ps.executeUpdate();
       
         rs = ps.getGeneratedKeys();
         if (!rs.next()) throw new SQLException("b\u0142ad zapisu");          
         int driverId = rs.getInt(1);
                
          // dodanie etykiet punktów odbioru na mapie
         RoutePointsTableModel rModel = new RoutePointsTableModel(route.getPoints());        
         Iterator<RoutePoint> iterator = route.getPoints().iterator();
         while (iterator.hasNext()) {
            RoutePoint tmp = iterator.next();
            tmp.setLabel(rModel.getLabel(tmp.getOrder().getCustomer().getId()));
         }
         
         RoadFixedGeometry fixed = new RoadFixedGeometry(route.getPoints());         
       
         for (RoutePoint point : route.getPoints()) {
           
            ps = database.prepareQuery("INSERT INTO dat_deliveries_drivers_orders (id, delivery_driver_id, customer_desc, "
                  + "customer_longitude, customer_latitude, customer_label, order_number, order_weight, "
                  + "order_orig_id, time, distance) "
                  + "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", true);
          
            ps.setInt(1, driverId);
            ps.setString(2, point.getOrder().getCustomer().toString());
            ps.setDouble(3, point.getOrder().getCustomer().getLongitude());
            ps.setDouble(4, point.getOrder().getCustomer().getLatitude());
            ps.setString(5, point.getLabel());
            ps.setString(6, point.getOrder().getNumber());
            ps.setDouble(7, point.getOrder().getTotalWeight());
            ps.setInt(8, point.getOrder().getId());
            ps.setDouble(9, point.getTime());
            ps.setDouble(10, point.getDistance());
            
            ps.executeUpdate();
           
            rs = ps.getGeneratedKeys();
            if (!rs.next()) throw new SQLException("b\u0142ad zapisu"); 
            int orderId = rs.getInt(1);
          
            if (fixed.getFixedGeometry(point.getId()) != null)
              for (GeoPosition pos : RoutePoint.getCoordsList(fixed.getFixedGeometry(point.getId()))) {
              
                ps = database.prepareQuery("INSERT INTO dat_deliveries_orders_geom (id, delivery_order_id, "
                      + "longitude, latitude, additional) VALUES (NULL, ?, ?, ?, 0);");
                ps.setInt(1, orderId);
                ps.setDouble(2, pos.getLongitude());
                ps.setDouble(3, pos.getLatitude());   
              
                ps.executeUpdate();
           
              }
       
            if (fixed.getFixedAdditionalGeometry(point.getId()) != null)
              for (GeoPosition pos : RoutePoint.getCoordsList(fixed.getFixedAdditionalGeometry(point.getId()))) {
              
                ps = database.prepareQuery("INSERT INTO dat_deliveries_orders_geom (id, delivery_order_id, "
                      + "longitude, latitude, additional) VALUES (NULL, ?, ?, ?, 1);");
                ps.setInt(1, orderId);
                ps.setDouble(2, pos.getLongitude());
                ps.setDouble(3, pos.getLatitude()); 
              
                ps.executeUpdate();
              
              }  
            
            // zmiana stanu zamówienia
            if (point.getOrder() != null && point.getOrder().getId()>0) {
                
              ps = database.prepareQuery("UPDATE dat_orders SET state_id = ?, delivery_id = ?, "
                      + "date_state_mod = NOW() WHERE id = ? ;");
              ps.setInt(1, OrderState.DELIVERY.getId());
              ps.setInt(2, deliveryId);
              ps.setInt(3, point.getOrder().getId());
              
              ps.executeUpdate();
              
            }
                     
         }
         
                   
         try {
           Thread.sleep(2);
         }
         catch (InterruptedException ex) {
           throw new SQLException("Przerwano proces zapisu dostawy.");  
         }
          
      }        
    
      database.doUpdate("COMMIT;");
      
      ps = database.prepareQuery("SELECT * FROM dat_deliveries WHERE id = ?");
      ps.setInt(1, deliveryId);
      rs = ps.executeQuery();
      rs.next();
      Delivery delivery = new Delivery(rs);
      
      Audit audit = new Audit(delivery, delivery, AuditDiff.AM_ADD, "Zatwierdzono plan nowej dostawy");
      (new DocAudit(database, delivery)).addElement(audit, user); 
      
    }
    
    catch (SQLException ex) {
       database.doUpdate("ROLLBACK;"); 
       throw ex; 
    }
      
  }
    
  
    
}
    
    