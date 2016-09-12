/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package spatial_vrp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jsprit.core.algorithm.recreate.InsertionData;
import jsprit.core.algorithm.recreate.listener.BeforeJobInsertionListener;
import jsprit.core.algorithm.recreate.listener.InsertionEndsListener;
import jsprit.core.algorithm.recreate.listener.InsertionStartsListener;
import jsprit.core.algorithm.recreate.listener.JobInsertedListener;
import jsprit.core.algorithm.ruin.listener.RuinListener;
import jsprit.core.problem.job.Job;
import jsprit.core.problem.solution.route.VehicleRoute;
import somado.AppObserver;



/**
 *
 * Listener dla poszczególnych etapów każdej z iteracji algorytmu VRP (m.in. przed i po rujnowaniu, po odtwarzaniu).
 * Przesyła bieżące informacje do obiektu macierzy kosztów, przy wykorzystaniu obserwatora.
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public class VRPListener implements RuinListener, InsertionEndsListener, InsertionStartsListener,
        BeforeJobInsertionListener, JobInsertedListener {

    
  /**
   * Struktura informacji o aktualnych zadaniach danego pojazdu, przesyłanej do macierzy kosztów 
   */
  static class VehicleCurrentJobs {
      
    /** Identyfikator pojazdu */
    public int vehicleId;
    /** Lista zadań */
    public List<Job> jobs;
    
    /**
     * Konstruktor 
     * @param vehicleId Identyfikator pojazdu 
     * @param jobs Lista zadań
     */
    public VehicleCurrentJobs(int vehicleId, List<Job> jobs) {
        
       this.vehicleId = vehicleId;
       this.jobs = jobs;
        
    }
            
  }
  
    
  /** Referencja do obiektu obserwatora z macierzy kosztów */  
  private final AppObserver appObserver;
  
  /**
   * Konstruktor
   * @param appObserver Referencja do obiektu obserwatora z macierzy kosztów
   */
  public VRPListener(AppObserver appObserver) {
      
    this.appObserver = appObserver;
      
  }
    
    
  @Override
  public void ruinStarts(Collection<VehicleRoute> clctn) {
            
    sendClearSignal();
    for (VehicleRoute vr : clctn) sendDataList(vr);            
            
  }

  
  @Override
  public void ruinEnds(Collection<VehicleRoute> clctn, Collection<Job> clctn1) {
               
    sendClearSignal();      
    for (VehicleRoute vr : clctn) sendDataList(vr);
            
  }
  
  
  @Override
  public void informInsertionStarts(Collection<VehicleRoute> clctn, Collection<Job> clctn1) {
  
    sendClearSignal();  
    for (VehicleRoute vr : clctn) sendDataList(vr);  
       
  }

    

  @Override
  public void informInsertionEnds(Collection<VehicleRoute> clctn) {
      
    sendClearSignal();  
    for (VehicleRoute vr : clctn) sendDataList(vr);  
      
  }  
  
  
  @Override
  public void informBeforeJobInsertion(Job job, InsertionData id, VehicleRoute vr) {}
  

  @Override
  public void informJobInserted(Job job, VehicleRoute vr, double d, double d1) {
      
     try {
                            
       int vehicleId = Integer.parseInt(vr.getVehicle().getId());  
       List<Job> tmp = new ArrayList<>();
       tmp.add(job);
       sendData(new VehicleCurrentJobs(vehicleId, tmp));
              
     }
            
     catch (Exception e) { }      
        
  }  
  

  @Override
  public void removed(Job job, VehicleRoute vr) {
  
     try {
                            
       int vehicleId = Integer.parseInt(vr.getVehicle().getId());  
       List<Job> tmp = new ArrayList<>();
       tmp.add(job);
       sendDataRemove(new VehicleCurrentJobs(vehicleId, tmp));
              
     }
            
     catch (Exception e) { }     
  
  }
    
  
  /**
   * Wysłanie listy danych do obiektu macierzy kosztów
   * @param vr Bieżąca trasa pojazdu
   */
  private void sendDataList(VehicleRoute vr) {
 
     try {
                            
       int vehicleId = Integer.parseInt(vr.getVehicle().getId());
       List<Job> jobs = new ArrayList<>(vr.getTourActivities().getJobs());            
       sendData(new VehicleCurrentJobs(vehicleId, jobs));
              
     }
            
     catch (Exception e) { }


   }
  
   /**
    * Wysłąnie pojedynczego dodanego zadania do macierzy kosztów
    * @param jobs Dodane zadanie
    */
   private void sendData(VehicleCurrentJobs jobs) {
       
      appObserver.sendObject("vehicle_current_load",  jobs);       
       
   }
  
   /**
    * Wysłanie sygnału dla usunięcia przydzielonych zadań dla macierzy kosztów
    */
   private void sendClearSignal() {
       
      appObserver.sendObject("vehicle_current_clear",  true);   
          
   }
   
   /**
    * Wysłanie pojedynczego usuniętego zadania do macierzy kosztów
    * @param job Usunięte zadanie
    */
   private void sendDataRemove(VehicleCurrentJobs job) {
       
      appObserver.sendObject("vehicle_current_load_remove",  job);        
       
   }
    
    
}
