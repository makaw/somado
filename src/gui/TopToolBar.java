/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui;

import gui.dialogs.AboutDialog;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.DeliveryOpenDialog;
import gui.dialogs.SettingsDialog;
import gui.dialogs.tableforms.OrderEditNewDialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import somado.User;


/**
 *
 * Szablon obiektu budującego górny pasek narzędziowy w oknie aplikacji
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class TopToolBar extends JToolBar {
    
  /** Etykieta z nazwą zalogowanego użytkownika */  
  private final JLabel userLabel;
  /** Etykieta z nazwą wybranej bazy danych */
  private final JLabel dbLabel;
  /** Lista przycisków tylko dla admina (do zablokowania) */
  private final List<JButton> adminButtons;
  
  /**
   * Wewnętrzna klasa budująca pojedynczy przycisk na pasku narzędziowym
   */
  @SuppressWarnings("serial")
  private class ToolBarButton extends JButton {
      
     private ToolBarButton(ImageIcon image, String toolTipText) {
         
         super("", image);
         setPreferredSize(new Dimension(28, 24));
         setFocusPainted(false);
         setToolTipText(toolTipText); 
         
     }  
      
  }    
    
  /**
   * Konstruktor szablonu obiektu paska narzędziowego 
   * @param frame Referencja do GUI
   */
  protected  TopToolBar(final GUI frame) {
  
    setFloatable(false);
    setOpaque(false);

    adminButtons = new ArrayList<>();
    
    JPanel toolBarElem = new JPanel(new FlowLayout(FlowLayout.LEFT));
    toolBarElem.setOpaque(false);
    
    ToolBarButton tbButton = new ToolBarButton(ImageRes.getIcon("icons/form_add.png"), "Nowe zam\u00f3wienie");
    toolBarElem.add(tbButton);
    toolBarElem.add(new JLabel(" "));
    tbButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        
           frame.setSelectedDataPanel(GUI.TAB_ORDERS);
           new OrderEditNewDialog(frame); 
            
        }  
    
    });
        
    
    
    tbButton = new ToolBarButton(ImageRes.getIcon("icons/delivery.png"), "Nowa dostawa");
    tbButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
       
        new DeliveryOpenDialog(frame);  
          
      }
    });      
    toolBarElem.add(tbButton);    
    toolBarElem.add(new JLabel(" "));             
    
    
    tbButton = new ToolBarButton(ImageRes.getIcon("icons/tools.png"), "Ustawienia");
    adminButtons.add(tbButton);
    tbButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
          
        new SettingsDialog(frame);
        
      }
    });      
    toolBarElem.add(tbButton);    
    toolBarElem.add(new JLabel(" ")); 
    
    tbButton = new ToolBarButton(ImageRes.getIcon("icons/help.png"), "O programie");
    tbButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
       
        new AboutDialog(frame);
        
      }
    });            
    toolBarElem.add(tbButton);
    toolBarElem.add(new JLabel(" "));
    
    tbButton = new ToolBarButton(ImageRes.getIcon("icons/exit.png"), "Koniec");
    tbButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
          
        boolean res = new ConfirmDialog(frame, "Czy na pewno zako\u0144czy\u0107 ?").isConfirmed();        
        if (res) frame.quitApp();
        
      }
    });      
    toolBarElem.add(tbButton);
    
    add(toolBarElem);
    add(Box.createHorizontalGlue());

    userLabel = new JLabel(ImageRes.getIcon("icons/user.png"));
    userLabel.setFont(GUI.BASE_FONT);
    userLabel.setBorder(new EmptyBorder(0, 0, 0, 15));
    userLabel.setVisible(false);
    userLabel.setToolTipText("Zalogowany u\u017cytkownik");
    add(userLabel);   
    
    dbLabel = new JLabel(ImageRes.getIcon("icons/database.png"));
    dbLabel.setFont(GUI.BASE_FONT);
    dbLabel.setBorder(new EmptyBorder(0, 10, 0, 15));
    dbLabel.setVisible(false);
    dbLabel.setToolTipText("Stan po\u0142\u0105czenia z baz\u0105 danych");
    add(dbLabel);  
            
    setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width,35));
      
  }
  
  /**
   * Metoda zwraca referencję do etykiety z nazwą zalogowanego użytkownika
   * @return Referencja do etykiety z nazwą zalogowanego użytkownika
   */
  protected JLabel getUserLabel() {
   
    return userLabel;  
      
  }
  
  /**
   * Metoda zwraca referencję do etykiety ze stanem połączenia z bazą danych
   * @return Referencja do etykiety ze stanem połączenia z bazą danych
   */  
  protected JLabel getDbLabel() {
      
    return dbLabel;  
      
  }
  
  /**
   * Metoda w razie potrzeby blokuje przyciski tylko dla administratora
   * @param user Obiekt zalogowanego użytkownika
   */
  public void lockButtons(User user) {
      
     for (JButton b: adminButtons) b.setEnabled(user.isAdmin());
      
  }
  
    
}
