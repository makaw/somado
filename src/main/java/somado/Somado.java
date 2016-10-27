/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package somado;


import gui.GUI;
import gui.dialogs.AboutDialog;
import gui.dialogs.ErrorDialog;
import gui.dialogs.WarningDialog;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import javax.swing.SwingUtilities;


/**
 *
 * Klasa główna zawierająca metodę main() - wywołanie interfejsu graficznego, ustalenie polaczen, 
 * autentykacja
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public final class Somado implements Observer {
  
  /** Obiekt reprezentujący graficzny interfejs */
  private GUI gui;  
  /** Obiekt bazy danych */
  private Database database;
  /** Obiekt obserwatora */
  private AppObserver appObserver;
  
  
  /**
   * Konstruktor klasy głównej, bezpieczne wywołanie interfejsu graficznego
   * @throws InterruptedException Błąd przy wywołaniu GUI
   * @throws InvocationTargetException Błąd przy wywołaniu GUI
   */
  private Somado() throws InterruptedException, InvocationTargetException {

    appObserver = new AppObserver();  
      
    // bezpieczne wywołanie interfejsu graficznego  
    SwingUtilities.invokeAndWait(new Runnable() {
      @Override
      public void run() {
        gui = new GUI(appObserver); 
        gui.build();
      }
    });
    
    appObserver.addObserver(gui);
    
  }
  
  
  /**
   * Metoda próbuje ustalić połączenie z globalna bazą danych przy pomocy podanego hasła aplikacji 
   * i laduje ustawienia
   * @return false błąd uwierzytelnienia DB, lub true jeżeli OK
   */
  private boolean connectDb() {     
      
     try {

       // załadowanie ustawień i otwarcie globalnej bazy danych
       Settings.getInstance().loadBasic();
       database = new Database();        
       Settings.getInstance().load(database);
       
          
     } catch (FileNotFoundException e) {
        
         String error = "Nie mo\u017cna odczyta\u0107 podstawowej konfiguracji "
                 + " (brak pliku .conf).\n"
                  + "Program teraz zako\u0144czy dzia\u0142anie.\n\n";
          
          new ErrorDialog(gui, error, true);             
          quitApp();         
          
     } catch (SettingsException e) {
       
          String error = e.getMessage() + "\n"
                  + "Program teraz zako\u0144czy dzia\u0142anie.\n\n";
          
          new ErrorDialog(gui, error, true);             
          quitApp();
           
     }  catch (SQLException e) {
               
          System.err.println("Problem z po\u0142\u0105czeniem z baz\u0105 danych: "+e);
          String error = "Problem z po\u0142\u0105czeniem z baz\u0105 danych. "
                  + "Program teraz zako\u0144czy dzia\u0142anie.\n\n";
          new ErrorDialog(gui, error, true);
          quitApp();
                  
         
     } catch (ClassNotFoundException | NullPointerException e) {
          
          System.err.println("Problem ze sterownikiem bazy danych: "+e);
          String error = "Problem ze sterownikiem bazy danych, "
                  + "program teraz zako\u0144czy dzia\u0142anie.\n\n";
          
          new ErrorDialog(gui, error, true);
          quitApp();
          
     }
     
     
     return true;
 
      
  }
    
    
  
  /**
   * Metoda odbiera obiekt przekazany przez obserwatora
   * @param o obiekt obserwowany
   * @param object przekazany obiekt
   */
  @Override
  public void update(Observable o, Object object) {
       
     AppObserver obs = (AppObserver)object;
     switch (obs.getKey()) {
             
        
     }
            
        
  }
  
  
  
  /**
   * Start aplikacji (połączenie, autentykacja, przesłanie obiektów do GUI 
   */
  private void startApp() {
      
     appObserver.addObserver(this);   
     
     connectDb();
      
     // przekazanie do GUI referencji do obiektu b.d.
     appObserver.sendObject("db_shared", database); 
     
     
     // przekazanie do GUI referencji do obiektu użytkownika
	 User user = new User();
     appObserver.sendObject("user", user);   
     

     new AboutDialog(gui);           

     
     
     // sprawdzenie dostepnosci uslugi TMS
     try{
        testTMS();   
     } catch(Exception e){
        System.err.println(e); 
        new WarningDialog(gui, "Nie mo\u017cna po\u0142\u0105czy\u0107 si\u0119 z us\u0142ug\u0105 TMS. "
                + "Mapy nie b\u0119d\u0105 wy\u015bwietlane nieprawid\u0142owo.\n\n" + e, 200);        
     }
      
  }
  
  
  /**
   * Statyczna metoda sprawdzająca dostępność usługi TMS
   * @throws java.net.MalformedURLException Źle sformowany URL usługi TMS
   * @throws Exception Brak dostępności usługi TMS
   */
  public static void testTMS() throws MalformedURLException, Exception {
      
    URL url = new URL(Settings.getValue("tms_url"));
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    connection.setRequestMethod("GET");
    connection.connect();
    int code = connection.getResponseCode();
    if (code != HttpURLConnection.HTTP_OK) {
      throw new Exception("HTTP "+String.valueOf(code));
    }               
      
  }
  
  
  
  
  /**
   * Statyczna metoda kończąca działanie programu
   */
  public static void quitApp() {
      
	  System.exit(0); 
      
  }  
  
  /**
   * Statyczna metoda zapisująca logi z błędami
   * @param user Obiekt zalogowanego użytkownika
   * @param error Komunikat błędu
   */
  public static void addLog(User user, String error) {
      
      
    Calendar cal = Calendar.getInstance();
    try {
         
      FileWriter fw = new FileWriter("somado.log", true); 
      fw.write(Settings.DATETIME_FORMAT.format(cal.getTime()) + ": " + error 
              + System.getProperty("line.separator")  + System.getProperty("line.separator"));
      fw.close();
        
    }
    catch (IOException ex) { 
      System.err.println(ex);  
    }                  
      
  }
  
  

  /** 
   * Metoda main wołana przez system w trakcie uruchamiania aplikacji:
   * Ustawia temat LookAndFeel GUI, wywołuje konstruktor, który
   * uruchamia interfejs graficzny, przesyla do GUI odpowiednie referencje.
   * @param args Argumenty przekazane do aplikacji.
   */
  //@SuppressWarnings("CallToPrintStackTrace")
  public static void main(final String[] args) {
    
     GUI.setLookAndFeel();

     try {
         
        new Somado().startApp();
        
     } catch (InterruptedException | InvocationTargetException e) {
         
        System.err.println("Problem podczas wywo\u0142ania interfejsu graficznego: "+e);
        e.printStackTrace();
        
     }
        
  }
  
  

}


