/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui;

import datamodel.IData;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import datamodel.tablemodels.ITableModel;
  
  
/**
 *
 * Renderer do komórek tabeli zawierających pole stanu (do zmiany koloru tekstu)
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TableStateCellRenderer extends DefaultTableCellRenderer {
    
      
  @Override
  public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
          boolean hasFocus, int row, int col) {
        
    boolean noSelection = false;
    int tmpRow = row;
    try {
      row = table.getRowSorter().convertRowIndexToModel(row);
    }
    // wyjątek oznacza że na tabela ma wyłączone zaznaczanie
    catch (NullPointerException e) { noSelection = true; }
        
    JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, hasFocus, hasFocus, row, col);
        
    ITableModel<? extends IData> tabModel = (ITableModel<?>) table.getModel();
 
    if (tabModel.isStateColumn(col) && tabModel.getStateCellColor(row) != null) {
            
        label.setForeground(tabModel.getStateCellColor(row));
        if (!noSelection) {
          if (table.getSelectedRow()==tmpRow) setBackground(table.getSelectionBackground());
          else setBackground(table.getBackground());
        }
            
    }
        
               
    return label;
        
  }
    
}

  
  
