/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 */
package somado;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import gui.ImageRes;


/**
*
* T�umaczenia (singleton)
* 
* @author Maciej Kawecki
* 
*/
public class Lang {

   /** Instancja klasy */
   private final static Lang INSTANCE = new Lang();
   
   /** Lokalizacja - indeks */
   private int localeIndex = IConf.DEFAULT_LOCALE_INDEX;
   /** Lokalizacja */  
   private Locale locale = IConf.LOCALES[localeIndex];
   /** Lista t�umacze� */
   private ResourceBundle bundle;   
   
   
   private Lang() {
	   
	 Locale.setDefault(locale);
	 try {
	   bundle = ResourceBundle.getBundle("AppMessages");
	 }
	 catch (MissingResourceException e) {
	   bundle = ResourceBundle.getBundle("resources/AppMessages");
	 }
	   
   }
   
   

   /**
    * Ustawia wskazaną lokalizację
    * @param index Indeks lokalizacji
    * @return True jeżeli zmieniono
    */
   public static boolean setLocale(int index) {
 	  
 	 if (index != INSTANCE.localeIndex)  
 	 try { 
 	   INSTANCE.localeIndex = index;
 	   INSTANCE.locale = IConf.LOCALES[index];
 	   Locale.setDefault(INSTANCE.locale);
 	   try {
 	     INSTANCE.bundle = ResourceBundle.getBundle("AppMessages");
 	   }
 	   catch (MissingResourceException e) {
 		 INSTANCE.bundle = ResourceBundle.getBundle("resources/AppMessages");
 	   }
 	 
 	   return true;
 	 }
 	 catch (IndexOutOfBoundsException e) {
 	   System.err.println(e);
 	 }
 	 
 	 return false;
 	 
   }
   
   
   public static int getLocaleIndex() {
 	 return INSTANCE.localeIndex;
   }
   

   
   /**
    * Zwraca tłumaczenie z zawartymi parametrami
    * @param key Klucz frazy
    * @param params Parametry
    * @return Tłumaczenie
    */
   public static String get(String key, Object... params) {
 	  
	 try {    
 	   return MessageFormat.format(INSTANCE.bundle.getString(key), params);
	 }
	 catch (MissingResourceException | NullPointerException e) {
	   System.err.println(e);
	   return key;
	 }
 	  	 
   }
   
   
   
   public static ImageIcon getIcon(Locale locale) {
	   
	   return ImageRes.getIcon("icons/" + locale.toString() + ".png");
	   
   }
	
	
}
