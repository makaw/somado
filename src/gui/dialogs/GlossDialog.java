/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;


import datamodel.IData;
import gui.GUI;
import gui.SimpleDialog;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import somado.IConf;


/**
 *
 * Klasa abstrakcyjna konstruująca okienko dialogowe ze słownikiem
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * @param <E> Klasa elementów głównego komponentu listy słownika
 * 
 */
@SuppressWarnings("serial")
public abstract class GlossDialog<E extends IData> extends SimpleDialog implements IGlossDialog<E>  {
    
        
   /** Lista elementow slownika (komponent) */
   private JList<E> objList;
   /** Filtry listy */
   protected GlossDialogFilters<E> filters;     
   /** Czy mozna wybierac wartosc ze slownika, czy tylko przegladac */
   private final boolean chooseable;
   /** Wybrana poczatkowo wartosc */
   private int selectedId = -1;
   /** Wybrany obiekt */
   protected E selectedElement;   
   
   
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param title Tytuł słownika
    * @param chooseable true jezeli mozna wybrac, false jezeli tylko przegladanie slownika
    */     
    public GlossDialog(GUI frame, String title, boolean chooseable) {
     
      super(frame,  IConf.APP_NAME + " - " + title);
      this.chooseable = chooseable;
      super.showDialog(480, 335);
      
    }        
    
    
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param title Tytuł słownika
    * @param selectedId Indeks wybranego początkowo elementu
    */         
    public GlossDialog(GUI frame, String title, int selectedId) {
     
      super(frame,  IConf.APP_NAME + " - " + title);
      this.chooseable = true;
      this.selectedId = selectedId;
      super.showDialog(480, 335);
      
    }            
    
    
   /**
    * Konstruktor 
    * @param frame Referencja do GUI
    * @param title Tytuł słownika
    */     
    public GlossDialog(GUI frame, String title) {
        
      this(frame, title, false);  
        
    }
    
   
    /**
     * Metoda udostępnia indywidualny model listy
     * @return  Model listy danego słownika
     */
    @Override
    public abstract DefaultListModel<E> getListModel();
    
    /**
     * Metoda udostępnia indywidualny model listy
     * @param filters Ustawione filtry (mapa)
     * @return  Model listy danego słownika
     */    
    @Override
    public abstract DefaultListModel<E> getListModel(Map<String, String> filters);
    
    /**
     * Metoda konstruuje zestaw pól do filtrowania na danym panelu
     * @param p Panel do umieszczenia pól
     */
    protected abstract void setFiltersFormPanel(JPanel p);
    
    /**
     * Metoda zwraca panel z przyciskami dostępnych akcji 
     * @return Panel z przyciskami dostępnych akcji 
     */
    protected abstract JPanel getButtonsPanel();    
    
    /**
     * Metoda zwraca referencje do przycisku wyboru
     * @return Ref. do przycisku wyboru
     */
    protected abstract JButton getChooseButton();
    
    /**
     * Metoda zwraca referencje do przycisku edycji
     * @return Ref. do przycisku edycji
     */
    protected abstract JButton getEditButton();
    
    
    /**
     * Renderer dla komponentu listy (do ew. nadpisania)
     * @return Renderer komponentu listy (lub null jezeli domyslny) 
     */
    protected DefaultListCellRenderer getCustomListRenderer() {
        
      return new DefaultListCellRenderer();
        
    }
    
   
   /**
    * Metoda wyświetlająca zawartość okienka
    */    
    @Override
    protected void getContent()  {
        
       
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
      mainPanel.setBackground(new Color(0xf3, 0xf7, 0xff));
        
      DefaultListModel<E> listModel = this.getListModel();      
      objList = new JList<>(listModel);
      try {
        objList.setCellRenderer(getCustomListRenderer());
      }
      catch (NullPointerException e) {}
      
      JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
      Border grayLine = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
      p.setBorder(BorderFactory.createTitledBorder(grayLine, "Filtrowanie"));
      p.setOpaque(false);
      JPanel p0 = new JPanel(new GridLayout());
      p0.setBorder(new EmptyBorder(10, 10, 10, 10));
      p0.setOpaque(false);
      p0.add(p);
      mainPanel.add(p0);
      
      filters = new GlossDialogFilters<>(this, p);
      setFiltersFormPanel(p); 

      objList.setFont(GUI.BASE_FONT);
      objList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      objList.setVisibleRowCount(10);
      
      // wlaczenie/wylaczenie przycisku edycji i wyboru
      objList.addListSelectionListener(new ListSelectionListener() { 
          
          @Override
          public void valueChanged(ListSelectionEvent listSelectionEvent) {
              
             if (getEditButton() != null) getEditButton().setEnabled(!objList.isSelectionEmpty());
             getChooseButton().setEnabled(!objList.isSelectionEmpty() && chooseable);
              
          }
          
      });
      
      JScrollPane sp = new JScrollPane(objList);
      sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      sp.setBorder(new EmptyBorder(5, 10, 5, 10));
      sp.setOpaque(false);
      sp.setViewportBorder(grayLine);
      mainPanel.add(sp);
      
      
      JPanel p2 = getButtonsPanel();
      p2.setOpaque(false);
      p2.setBorder(new EmptyBorder(5, 10, 10, 10));
      mainPanel.add(p2);
      
      mainPanel.add(new JLabel());
      
      add(mainPanel);
      
      // zaznaczenie domyslnie wybranej pozycji slownika
      if (selectedId != -1) { 
          
         int index = getGlossary().getItemsIndex(selectedId); 
         objList.setSelectedIndex(index);
         objList.ensureIndexIsVisible(index);
         selectedElement = getGlossary().getItem(index);
         
      }
    
       
    }
    
    
    /**
     * Metoda zwracajaca liste elementow slownika (jako komponent)
     * @return Komponent listy elementow slownika
     */
    @Override
    public JList<E> getObjList() {
        
       return objList; 
        
    }
    
    
    /**
     * Metoda zwracajaca aktualne filtry
     * @return Obiekt filtrow
     */
    @Override
    public GlossDialogFilters<E> getFilters() {
        
       return filters; 
        
    }
    
   
    /**
     * Metoda zwracajaca wybrany element
     * @return Wybrany element
     */
    @Override
    public E getSelectedElement() {
        
       return selectedElement;
        
    }
    
    
    
}
