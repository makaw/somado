/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import datamodel.IData;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import datamodel.tablemodels.ITableModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellRenderer;
import somado.Settings;


/**
 *
 * Ogólny abstrakcyjny szablon obiektu centralnego panelu dla list (tabel)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public abstract class TablePanel extends EmptyPanel {
     
  /** Komponent tabeli do wyświetlenia danych */  
  protected final JTable table;  
  /** Obiekt dostępnych filtrow */
  protected final TableFilters filters;  
  /** Referencja do GUI */
  protected final GUI frame;
  /** Panel zawierający pola do filtrowania */
  private final JPanel filtersPanel;
  /** Etykieta z liczbą dostępnych rekordów */
  private JLabel allRecCountLabel;
  /** Etykieta z liczbą wczytanych rekordów */
  private JLabel readRecCountLabel;  
  
  
  /**
   * Wewnętrzna klasa do generowania paneli sterujących
   */
  @SuppressWarnings("serial")
  private class navPanel extends JPanel {
      
    navPanel(boolean opaque) {
        
      super(new FlowLayout(FlowLayout.LEFT));
      setOpaque(opaque);
      setBackground(new Color(0xe8e8e8));
      setBorder(new LineBorder(new Color(0xc6c6c6), 1));
      setMaximumSize(new Dimension(SCREEN_WIDTH, 60));      
        
    }  
      
  }

  
  /**
   * Wewnętrzna klasa - renderer do wierszy łamiący tekst + ew. wyróżnienie
   */  
  protected class WrapTableRenderer extends JTextArea implements TableCellRenderer {
    
    public WrapTableRenderer() {
      setOpaque(true);
      setMargin(new Insets(3, 2, 3, 2));
      setLineWrap(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
            boolean hasFocus, int row, int column) {

      @SuppressWarnings("unchecked")
      ITableModel<? extends IData> tabModel = (ITableModel) table.getModel();
      Color cl = (tabModel.isHighlightedCell(column, table.getRowSorter().convertRowIndexToModel(row))) ? Color.RED 
                : (isSelected ? table.getSelectionForeground() : table.getForeground());
        
      setForeground(cl);
      setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());    
      setText((value == null) ? "" : value.toString());
      
    
      
      return this;
    
    }  
  
  }
  

  /**
   * Konstruktor budujący panel tabeli
   * @param titleLabel Nagłówek 
   * @param frame Ref. do GUI
   */  
  @SuppressWarnings("serial")
  public TablePanel(String titleLabel, final GUI frame) {
             
     super();
     setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
     setBorder(new EmptyBorder(0,15,10,20));  
     
     this.frame = frame;     
 
     filtersPanel = new navPanel(false);
     
     filtersPanel.setBorder(BorderFactory.createTitledBorder(filtersPanel.getBorder(), titleLabel));
      
     add(new JLabel(" "));
     
     // tabela rozszerzona o zapamietywanie biezacej pozycji kontekstowego menu
     table = new JTable() {
         
        @Override
        public Point getPopupLocation(MouseEvent event) {
          putClientProperty("popupTriggerLocation",  event != null ? event.getPoint() : null);
          return super.getPopupLocation(event);
        }  
        
     };
     
     table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
     table.setUpdateSelectionOnSort(false);
     table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
     table.setAutoCreateRowSorter(true);
     table.setRowHeight(22);
      
     // ustanowienie pol filtrowania
     filters = new TableFilters(table, this, filtersPanel);          

     filtersPanel.setMaximumSize(new Dimension(SCREEN_WIDTH, 70));
      
     JPanel p0 = new JPanel(new GridLayout(1,1));
     p0.setBorder(new EmptyBorder(0, 0, 15, 0));
     p0.setOpaque(false);
     p0.setMaximumSize(new Dimension(SCREEN_WIDTH, 70));
      
     p0.add(filtersPanel); 
     add(p0);     

     JScrollPane scroll = new JScrollPane(table);      
     scroll.setMaximumSize(new Dimension(SCREEN_WIDTH, GUI.FRAME_HEIGHT - 250));
     add(scroll);            
      
  } 
         
  /**
   * Konieczne operacje po kazdej zmianie modelu (np. szerokosc kolumn)
   */
  protected abstract void afterModelChange();
  
  
  
  /**
   * Metoda "dozbraja" obiekt w elementy zalezne od klasy dziedziczacej, 
   * ktore nie moga byc wywolane w ramach konstruktora
   */
  final protected void completeTablePanel() {
      
    setFiltersPanel(filtersPanel);    
    table.setModel(getNewTableModel());
    table.addMouseListener(getTableListener());

    @SuppressWarnings("unchecked")
    ITableModel<? extends IData> dataModel = ((ITableModel)(table.getModel()));
    
    table.getRowSorter().toggleSortOrder(dataModel.getDefaultSortOrder());
    if (!dataModel.getDefaultSortOrderAsc())
       table.getRowSorter().toggleSortOrder(dataModel.getDefaultSortOrder()); 
               
    try {
      JPanel bPanel = new navPanel(true);  
      bPanel.add(getButtonsPanel());
      add(bPanel);
    }
    catch (NullPointerException e) {}
       
    
    // panel z liczba wczytanych rekordow 
    
    JPanel cntPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    cntPanel.setOpaque(false);
    cntPanel.setBorder(new EmptyBorder(5, 5, 5, 5));    
    readRecCountLabel = new NormLabel("Znaleziono rekord\u00f3w: " 
            + String.valueOf(dataModel.getAllElementsCount()) + ".");
    cntPanel.add(readRecCountLabel);

    allRecCountLabel = new NormLabel("", true);
    
    if (Integer.parseInt(Settings.getValue("items_per_page"))<dataModel.getAllElementsCount()) {
    
      allRecCountLabel.setText(" Widoczne rekordy: " + Settings.getValue("items_per_page") +
              ". U\u017cyj filtr\u00f3w lub zmie\u0144 ustawienia, aby wy\u015bwieli\u0107 "
              + "pozosta\u0142e.");   
      
    }
    
    cntPanel.add(allRecCountLabel);
    add(cntPanel);
    
  }
  
  
  /**
   * Metoda uaktualnia liczbe wczytanych rekordow
   */
  protected void refreshAllElementsCountPanel() {
      
    @SuppressWarnings("unchecked")  
    ITableModel<? extends IData> dataModel = ((ITableModel)(table.getModel()));  
      
    readRecCountLabel.setText("Znaleziono rekord\u00f3w: " 
            + String.valueOf(dataModel.getAllElementsCount()) + ".");

    if (Integer.parseInt(Settings.getValue("items_per_page"))<dataModel.getAllElementsCount()) {
    
      allRecCountLabel.setText(" Widoczne rekordy: " + Settings.getValue("items_per_page") +
              ". U\u017cyj filtr\u00f3w lub zmie\u0144 ustawienia, aby wy\u015bwieli\u0107 "
              + "pozosta\u0142e.");
      allRecCountLabel.setVisible(true);
      
    }      
    
    else allRecCountLabel.setVisible(false);
            
  }
  
  
  
  /**
   * Metoda ustanawia pola filtrowania
   * @param p Panel do umieszczenia komponentow pol filtrowania
   */  
  protected abstract void setFiltersPanel(JPanel p);
  
  
  /**
   * Metoda ma zwracac nowa instancje odpowiedniego modelu tabeli
   * @param params Mapa parametrow (filtrow)
   * @return Nowy obiekt modelu tabeli
   */  
  protected abstract AbstractTableModel getNewTableModel(Map<String, String> params);
  
  
  /**
   * Metoda ma zwracac nowa instancje odpowiedniego modelu tabeli (bez filtrow)
   * @return Nowy obiekt modelu tabeli
   */
  protected final AbstractTableModel getNewTableModel() {
      
     return getNewTableModel(null); 
      
  }
  
  
  /**
   * Metoda ma zwracac listener myszy dla wierszy tabeli
   * @return Listener myszy
   */
  protected abstract MouseAdapter getTableListener();

  
  /**
   * Metoda ma zwracac panel z osadzonymi przyciskami dostepnych akcji
   * @return Panel z przyciskami dostepnych akcji
   */
  protected abstract JPanel getButtonsPanel();
  
  

  /**
   * Metoda zwraca referencje do obiektu tabeli z danymi
   * @return Ref. do obiektu tabeli z danymi
   */
  public JTable getTable() {
      
    return table;  
      
  }
  
  
  /**
   * Metoda zwraca referencje do obiektu filtrow tabeli z danymi
   * @return Ref. do obiektu filtrow tabeli z danymi
   */  
  public TableFilters getTableFilters() {
      
    return filters;
    
  }
  

  /**
   * Odswiezenie calej tabeli
   */
  public void refreshTable() {
      
     int rowIndex = table.getSelectedRow();  
      
     List<? extends SortKey> sortKeys = table.getRowSorter().getSortKeys();
      
     // uaktualnienie modelu
     AbstractTableModel tModel = getNewTableModel(filters.getParams());
     table.setModel(tModel);
       
     // sortowanie i szerokosc kolumn
     afterModelChange();
     table.getRowSorter().setSortKeys(sortKeys);
       
     // proba ustawienia zaznaczenia
     if (rowIndex!=-1) try {
         
       table.setRowSelectionInterval(rowIndex, rowIndex);
       table.scrollRectToVisible(table.getCellRect(rowIndex, 0, true));
       
     }  
     // na wypadek usuniecia rekordu ...
     catch (IllegalArgumentException e) {}
        
      
  }
  
  /**
   * Metoda ustawia renderer dla wybranych kolumn (stany - kolor tekstu)
   */
  public void setStateCellRenderer() {
    
     for (int i=0;i<table.getColumnModel().getColumnCount();i++)
      if (((ITableModel)(table.getModel())).isStateColumn(i)) 
         table.getColumnModel().getColumn(i).setCellRenderer(new TableStateCellRenderer());

  }

  
  /**
   * Metoda ustawia renderer dla wierszy wymagających łamania tekstu
   */
  public void setWrapRowRenderer() {
      
    for (int i=0;i<table.getColumnModel().getColumnCount();i++) {
      if (((ITableModel)(table.getModel())).isWrapColumn(i))  {
          
        table.getColumnModel().getColumn(i).setCellRenderer(new WrapTableRenderer());
        for (int j=0; j<table.getRowCount();j++) {  

          String txt = table.getValueAt(j,i).toString();
          int height = txt.length() - txt.replace("\n", "").length();
          table.setRowHeight(j, height<=1 ? 22 : (height)*18);

        }          
          
      } 
      
    }      
      
  }   
    
  
    
}
