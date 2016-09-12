/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.formfields;


import gui.GUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * "Suwak" do ustawiania wartości parametrów z zadanego przedziału
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class SliderField extends JPanel {
    
  /** Komponent "suwaka" */
  private final JSlider slider;  
  /** Etykieta z opisem */
  private final JLabel titleLabel;
  /** Pole tekstowe do wyświetlania bieżącej wartości */
  private final JTextField valueField;
  /** Etykieta do wyświetlania miary */
  private final JLabel symbolLabel;
  /** Format liczby w polu tekstowym */
  private final Format valueFieldFormat;
  /** Jednostka "suwaka" */
  private final double unit;
  /** Minimalna wartość */
  private final double min;  
  /** Maksymalna wartość */
  private final double max;
  /** Pomocniczy obiekt do nasłuchiwania zmian */
  private final PropertyChangeSupport changes = new PropertyChangeSupport(this); 
  
  /**
   * Konstruktor
   * @param title Tytuł
   * @param digits Ilość cyfr po przecinku
   * @param min Minimalna wartość
   * @param max Maksymalna wartość
   * @param font Czcionka
   * @param symbol Symbol miary jednostki
   */
  public SliderField(String title, int digits, double min, double max, Font font, String symbol) {
      
    super(new GridLayout(1,1));
    setOpaque(false);
    
    if (digits<0) digits = 0;
    
    valueField = new JTextField(4);
    valueField.setEditable(false);
    valueField.setFocusable(false);
    valueField.setFont(font);
    
    unit = digits > 0 ? Math.pow(10, -digits) : 1;
    this.max = max;
    this.min = min;
    
    int scaledMin = (int)Math.round(min * Math.pow(10, digits));
    int scaledMax = (int)Math.round(max * Math.pow(10, digits));
    slider = new JSlider(JSlider.HORIZONTAL, 0, scaledMax, 1);
    int tickSpace = (scaledMax-scaledMin)/3;
    slider.setMajorTickSpacing(tickSpace);
    slider.setMinimum(scaledMin);
    slider.setMaximum(scaledMax);
    slider.setOpaque(false);
    slider.setPaintTicks(true);
    slider.setPaintLabels(true);

    
    Format f = new DecimalFormat(digits > 0 ? (digits>=2 ? "0.00" : "0.0") : "#");
    Properties labels = new Properties();
    for (int i=0;i<4;i++) {
       double val = (i == 0 ? min : i == 3 ? max : (min + i*tickSpace*unit));
       JLabel label = new JLabel(f.format(val));
       label.setFont(font);
       labels.put(scaledMin + i * tickSpace,label);
    }
    
    slider.setLabelTable(labels);    
    
    valueFieldFormat = new DecimalFormat(digits > 0 ? String.valueOf(unit).substring(0, digits+1) + "0" : "#");
    
    final boolean zeroAllowed = min <= 0;
    
    slider.addChangeListener(new ChangeListener() {

       @Override
       public void stateChanged(ChangeEvent e) {
         int val = ((JSlider)e.getSource()).getValue();
         if (val == 0 && !zeroAllowed) {
             ((JSlider)e.getSource()).setValue(1); 
             val = 1;
         }
         
         changes.firePropertyChange("value", Double.valueOf(valueField.getText().replace(',','.')), val*unit);
         valueField.setText(String.valueOf(valueFieldFormat.format(val*unit)));
        
       }
    });
    
      
    valueField.setText(String.valueOf(valueFieldFormat.format(slider.getValue()*unit)));
    
    titleLabel = new JLabel(title);
    titleLabel.setFont(font);
    
    JLayeredPane p = new JLayeredPane();
    p.setLayout(new FlowLayout(FlowLayout.CENTER));
    p.setLayer(p, 0);
    p.setOpaque(false);
    p.add(titleLabel);    
    p.add(valueField);
    
    symbolLabel = new JLabel();
    
    if (symbol != null) {
      symbolLabel.setText(symbol);
      symbolLabel.setFont(font);
      p.add(symbolLabel);
    }
   
    p.add(slider);
    add(p);
    p.setPreferredSize(new Dimension(250, 80));
    
  }  
  
  
  /**
   * Konstruktor (bez czcionki)
   * @param title Tytuł
   * @param digits Ilość cyfr po przecinku
   * @param min Minimalna wartość
   * @param max Maksymalna wartość
   * @param symbol Symbol miary jednostki
   */  
  public SliderField(String title, int digits, double min, double max, String symbol) {
      
    this(title, digits, min, max, GUI.BASE_FONT, symbol);
      
  }  
  
  
  /**
   * Konstruktor (bez symbolu miary jednostki)
   * @param title Tytuł
   * @param digits Ilość cyfr po przecinku
   * @param min Minimalna wartość
   * @param max Maksymalna wartość
   * @param font Czcionka
   */  
  public SliderField(String title, int digits, double min, double max, Font font) {
      
    this(title, digits, min, max, font, null);  
      
  }
    
  
  /**
   * Metoda zwraca liczbową wartość "suwaka"
   * @return Wartość komponentu
   */
  public double getValue() {
      
    return Double.valueOf(valueField.getText().replace(",", "."));
      
  }
  
  
  
  /**
   * Ustawienie wartości "suwaka"
   * @param value Nowa wartość
   * @param free True jeżeli dowolna wartość
   */
  public void setValue(double value, boolean free) {
      
     if (!free && value > max) value = max; 
     valueField.setText(valueFieldFormat.format(value));
     slider.setValue((int)Math.round(value/unit));      
      
  }
  
  
  public void setValue(double value) {
    setValue(value, false);  
  }
  
  
  /**
   * Maksymalna wartość suwaka
   * @return Maksymalna wartość suwaka
   */
  public double getMaxValue() {
      
    return slider.getMaximum();
      
  }
  
  
  /**
   * Ustawienie edytowalności komponentu
   * @param editable True jeżeli edycja jest możliwa
   */
  public void setEditable(boolean editable) {
      
    slider.setEnabled(editable);
    valueField.setEnabled(editable);
    titleLabel.setForeground(editable ? new Color(0x303030) : new Color(0xa0a0a0));
    symbolLabel.setForeground(editable ? new Color(0x303030) : new Color(0xa0a0a0));
      
  }

  /**
   * Zmiana rozmiarów komponentu
   * @param width Nowa szerokość
   * @param height Nowa wysokość
   */
  public void changeSize(int width, int height) {
    
     slider.setPreferredSize(new Dimension(width-10, height-25));
     setPreferredSize(new Dimension(width, height));
     
  }
    
  
  @Override
  public void addPropertyChangeListener(PropertyChangeListener l) {
    changes.addPropertyChangeListener(l);
  }

  
  @Override
  public void removePropertyChangeListener(PropertyChangeListener l) {
    changes.removePropertyChangeListener(l);
  }  
  
      
  
    
}


