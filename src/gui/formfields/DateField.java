/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.formfields;

import gui.ImageRes;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import somado.Settings;

/**
 *
 * Szablon obiektu panelu z polem wyboru daty
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class DateField extends JPanel implements IFormField {
    
  /** Okno z wyborem daty */  
  private final JDatePickerImpl datePicker;
  /** Przycisk zmiany daty */
  private final JButton changeButton;
    
  
  /**
   * Wewnetrzna klasa formatujaca date
   */  
  @SuppressWarnings("serial")
  protected class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    @Override
    public Object stringToValue(String text) throws ParseException {
        return Settings.DATE_FORMAT.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return Settings.DATE_FORMAT.format(cal.getTime());
        }

        return "";
    }

  }      
  
  
  /**
   * Konstruktor
   * @param date Domyslna (poczatkowa) data
   */
  public DateField(Date date) {
      
     super(new FlowLayout(FlowLayout.LEFT)); 
     setOpaque(false);
     
     final UtilDateModel model = new UtilDateModel();
     
     Properties pr = new Properties();
     pr.put("text.today", "Dzi\u015b");
     pr.put("text.month", "Miesi\u0105c");
     pr.put("text.year", "Rok");    
 
     JDatePanelImpl datePanel = new JDatePanelImpl(model, pr);
     datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
     
     if (date != null) {
       model.setDate(Integer.parseInt((new SimpleDateFormat("yyyy")).format(date)),
                     Integer.parseInt((new SimpleDateFormat("MM")).format(date))-1,
                     Integer.parseInt((new SimpleDateFormat("dd")).format(date)));
     }
     
     
     model.setSelected(true); 
     
     datePicker.setShowYearButtons(false);
     datePicker.setTextEditable(false);
     datePicker.setButtonFocusable(false);
     
     changeButton = (JButton) datePicker.getComponent(1);
     changeButton.setToolTipText("Ustaw dat\u0119");
     changeButton.setText("");
     changeButton.setIcon(ImageRes.getIcon("icons/calendar.png"));
     changeButton.setPreferredSize(new Dimension(30, 30));
     datePicker.getJFormattedTextField().setPreferredSize(new Dimension(120, 30));
     datePicker.getJFormattedTextField().setBackground(new Color(0xeaeaea));
     
     add(datePicker);              
           
     
  }
    
  
  
  /**
   * Metoda zwraca wybrany ze slownika obiekt
   * @return Wybrany ze slownika obiekt
   */
  @Override
  public Object getSelectedElement() {      
      
      try {
          
        return Settings.DATE_FORMAT.parse(datePicker.getJFormattedTextField().getText());
        
      } catch (ParseException e) {
         
        System.err.println(e.getMessage());  
        return new Date(0);    
          
      }
      
      
  }
  
  
  /**
   * (Niezaimplementowana) Metoda ustawia dana wartosc
   * @param element Wartosc do ustawienia
   */  
  @Override
  public void setSelectedElement(Object element) {}
  
  
  
  public JDatePickerImpl getDatePicker() {
      
     return datePicker; 
      
  }

  
  @Override
  public void setEnabled(boolean enabled) {

    changeButton.setEnabled(enabled);
      
  }
  
  
  @Override
  public boolean isEnabled() {
      
    return changeButton.isEnabled();
      
  }

  
}
