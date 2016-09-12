/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package somado;

import datamodel.glossaries.GlossLocks;
import gui.GUI;
import gui.dialogs.ErrorDialog;
import gui.dialogs.PasswordDialog;
import gui.dialogs.WarningDialog;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
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
  /** Obiekt współdzielonej bazy danych */
  private DatabaseShared databaseShared;
  /** Obiekt użytkownika */
  private User user;
  /** Obiekt obserwatora */
  private AppObserver appObserver;
  /** Login (pobierany od użytkownika) */
  private String appLogin = "";
  /** Hasło aplikacyjne (pobierane od użytkownika) */
  private String appPass = "";
  

  /** PID @ host */
  public static final String APP_PID =  ManagementFactory.getRuntimeMXBean().getName();
  
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
   * @param appPass Hasło aplikacyjne
   * @param reconnect True jeżeli to próba ponownego połączenia
   * @return false błąd uwierzytelnienia DB (złe hasło), lub true jeżeli OK
   */
  private boolean connectDb(String appPass, boolean reconnect) {     
      
     try {

       // załadowanie ustawień i otwarcie globalnej bazy danych
       Settings.getInstance().loadBasic();
       databaseShared = new DatabaseShared();  
       databaseShared.doUpdate("SET NAMES 'utf8' COLLATE 'utf8_polish_ci';");  
       Settings.getInstance().load(databaseShared);
       
       // otwarcie lokalnej bazy danych SQLite tylko dla sprawdzenia, każdorazowo będzie tworzone nowe połączenie
       DatabaseLocal.getInstance().close();
          
     } catch (FileNotFoundException e) {
        
         String error = "Nie mo\u017cna odczyta\u0107 podstawowej konfiguracji "
                 + " (brak pliku .conf).\n"
                  + "Program teraz zako\u0144czy dzia\u0142anie.\n\n"
                  + "Skontaktuj si\u0119 z administratorem.";
          
          new ErrorDialog(gui, error, true);             
          quitApp();         
          
     } catch (SettingsException e) {
       
          String error = e.getMessage() + "\n"
                  + "Program teraz zako\u0144czy dzia\u0142anie.\n\n"
                  + "Skontaktuj si\u0119 z administratorem.";
          
          new ErrorDialog(gui, error, true);             
          quitApp();
           
     }  catch (SQLException e) {
       
        // podano nieprawidłowe hasło do bazy danych 
        if (e.getErrorCode()==1045) {
            
          String error = new UserException(UserException.AUTH).getMessage();
          new ErrorDialog(gui, error); 

          return false;
            
        }
         
        // inny problem z połączeniem
        else {
         
          System.err.println("Problem z po\u0142\u0105czeniem z baz\u0105 danych: "+e);
          String error = "Problem z po\u0142\u0105czeniem z baz\u0105 danych. "
                  + (!reconnect ? "Program teraz zako\u0144czy dzia\u0142anie.\n\n"
                  + "Skontaktuj si\u0119 z administratorem." : "");
          new ErrorDialog(gui, error, true);
          if (!reconnect) quitApp();
          
        }
         
     } catch (ClassNotFoundException | NullPointerException e) {
          
          System.err.println("Problem ze sterownikiem bazy danych: "+e);
          String error = "Problem ze sterownikiem bazy danych, "
                  + "program teraz zako\u0144czy dzia\u0142anie.\n\n"
                  + "Skontaktuj si\u0119 z administratorem.";
          
          new ErrorDialog(gui, error, true);
          quitApp();
          
     }
     
     
     return true;
 
      
  }
  
  
  /**
   * Metoda próbuje ustalić połączenie z globalna bazą danych przy pomocy podanego hasła aplikacji 
   * i laduje ustawienia
   * @param appPass Hasło aplikacyjne
   * @return false błąd uwierzytelnienia DB (złe hasło), lub true jeżeli OK
   */  
  private boolean connectDb(String appPass) {
      
     return connectDb(appPass, false);  
      
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
         
         // użytkownik podał hasło aplikacyjne
         case "app_pass":

            try { 
              Object[] obj = (Object[]) obs.getObject();
              appLogin = (String) obj[0];
              appPass = (String) obj[1];
            }
            catch (NullPointerException e) {
              System.err.println(e.getMessage());  
            }
            break;   
             
         // ponowne połączenie ze współdzieloną bazą danych
         case "db_connect":
             
            if (connectDb(appPass, true)) {
                
              // przekazanie do GUI referencji do obiektu globalnej bazy danych i nowego pingowania
              appObserver.sendObject("db_shared", databaseShared); 
              if (Settings.getIntValue("ping_db") == 1) {    
                appObserver.sendObject("ping", new PingDb(gui, databaseShared));
              }                  
                
            }
            
            break;
            
     }
            
        
  }
  
  
  
  /**
   * Start aplikacji (połączenie, autentykacja, przesłanie obiektów do GUI 
   */
  private void startApp() {
      
     appObserver.addObserver(this);   
      
     // zapytanie o login i haslo/BD, autoryzacja, proba polaczenia z BD
     do {                  
         
        new PasswordDialog(gui);  
        if (!connectDb(appPass)) continue;
        try {
          user = new User(appLogin, appPass, databaseShared);
        } catch (UserException ex) {
          new ErrorDialog(gui, ex.getMessage());
        }
        
     } while (user == null || !user.isAuthorized());              

     
     // przekazanie do GUI referencji do obiektu zdalnej bazy danych
     appObserver.sendObject("db_shared", databaseShared); 
     
     
     // przekazanie do GUI referencji do obiektu użytkownika
     appObserver.sendObject("user", user);  
     
    // pingowanie zdalnej bazy danych
     if (Settings.getIntValue("ping_db") == 1) {    
          appObserver.sendObject("ping", new PingDb(gui, databaseShared));
     }
     
     // zdjęcie ewentualnych blokad globalnej BD
     GlossLocks.clearItems(databaseShared, user);
     
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
   * @param ping Timer wysyłający pingi do serwera BD
   */
  public static void quitApp(PingDb ping) {
      
     if (ping != null) ping.stop(); 
     System.exit(0); 
      
  }
  
  
  /**
   * Statyczna metoda kończąca działanie programu
   */
  public static void quitApp() {
      
     quitApp(null);
      
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
  @SuppressWarnings("CallToPrintStackTrace")
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


