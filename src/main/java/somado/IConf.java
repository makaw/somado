/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package somado;

import java.util.Locale;

/**
 *
 * Interfejs konfiguracyjny
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
public interface IConf {
    
  /** Nazwa aplikacji */
  String APP_NAME = "Somado";
  /** Wersja aplikacji */
  String APP_VERSION = "1.03 lite [12/2018]";        
  
  /** Dost�pne ustawienia lokalne */
  Locale[] LOCALES = { new Locale("pl", "PL"), new Locale("en", "EN")};
  /** Domy�lne ustawienie lokalne (indeks) */
  int DEFAULT_LOCALE_INDEX = 1;
    
  /** Minimalna wymagana długość hasła */
  int MIN_PASS_LEN = 5;  
        
  /** Minimalna możliwa ładowność pojazdów [t] */  
  double MIN_VEHICLE_MAXLOAD = 0.1; 
  /** Maksymalna możliwa ładowność pojazdów [t] */  
  double MAX_VEHICLE_MAXLOAD = 1.90;
  /** Minimalne możliwe średnie zużycie paliwa [l/100km] */  
  double MIN_VEHICLE_FUEL_CONSUMPTION = 4.0; 
  /** Maksymalne możliwe średnie zużycie paliwa [l/100km] */  
  double MAX_VEHICLE_FUEL_CONSUMPTION = 20.0; 
  /** Dodatkowe zużycie paliwa na każde 100kg ładunku */
  double ADDITIONAL_FUEL_CONSUMPTION_PER_100KG = 0.025;
  /** Minimalny rok produkcji pojazdu */
  int MIN_VEHICLE_YEAR = 1960;
  
        
  /** Minimalna możliwa waga produktu [kg] */  
  double MIN_PRODUCT_WEIGHT = 0.001; 
  /** Maksymalna możliwa waga produktu [kg] */  
  double MAX_PRODUCT_WEIGHT = 500.0;  
  
  /** Domyślny maksymalny czas przejazdu kierowcy w jednej dostawie */
  double DEFAULT_MAX_DRIVER_WORK_TIME = 8.0;
           
}
