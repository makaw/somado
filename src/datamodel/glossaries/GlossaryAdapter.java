/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package datamodel.glossaries;

import datamodel.IData;
import java.util.Map;
import javax.swing.ListModel;
import somado.Database;
import somado.User;


/**
 *
 * Adapter dla czesciowych implementacji interfejsu edytowalnego slownika
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów słównika, implementująca interfejs IData
 * 
 */
public class GlossaryAdapter<E extends IData>  extends Glossary<E> implements IGlossaryEditable<E> {

    /** komunikat ostatniego bledu (operacje DB) */
    protected String lastError = "";

    
    public GlossaryAdapter(Database database) {
        
      super(database);  
        
    }
    
    
    @Override
    public boolean addItem(E element, User user) {
    
      return false;
        
    }

    @Override
    public String getLastError() {
        
      return lastError;
      
    }

    @Override
    public boolean updateItem(E element, User user) {
       
      return false;
      
    }
    

    @Override
    public boolean deleteItem(E element, User user) {
       
       return false;
       
    }
    

    @Override
    public ListModel<E> getListModel() {
        
       return null;
       
    }
    

   
    

    @Override
    public int getItemsIndex(int elementId) {
      
       return -1;
       
    }
    

    @Override
    public E getItem(int elementId) {
        
      return null;
       
    }

    @Override
    public E getDefaultItem() {
        
      return null;
       
    }

    @Override
    public ListModel<E> getListModel(Map<String, String> params) {
      return null;
    }

    
    
}
