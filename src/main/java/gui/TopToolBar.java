/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import gui.dialogs.AboutDialog;
import gui.dialogs.ConfirmDialog;
import gui.dialogs.DeliveryOpenDialog;
import gui.dialogs.SettingsDialog;
import gui.dialogs.tableforms.OrderEditNewDialog;
import somado.IConf;
import somado.Lang;
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
    
  /** Lista przycisków tylko dla admina (do zablokowania) */
  private final List<JButton> adminButtons;
  /** Przyciski */
  private final ToolBarButton orderBtn, delivBtn, settBtn, aboutBtn, quitBtn;
  
  
  /**
   * Wewnętrzna klasa budująca pojedynczy przycisk na pasku narzędziowym
   */
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
    
    orderBtn = new ToolBarButton(ImageRes.getIcon("icons/form_add.png"), Lang.get("Menu.NewOrder"));
    toolBarElem.add(orderBtn);
    toolBarElem.add(new JLabel(" "));
    orderBtn.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        
           frame.setSelectedDataPanel(GUI.TAB_ORDERS);
           new OrderEditNewDialog(frame); 
            
        }  
    
    });        
    
    
    delivBtn = new ToolBarButton(ImageRes.getIcon("icons/delivery.png"), Lang.get("Menu.NewDelivery"));
    delivBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
       
        new DeliveryOpenDialog(frame);  
          
      }
    });      
    toolBarElem.add(delivBtn);    
    toolBarElem.add(new JLabel(" "));             
    
    
    settBtn = new ToolBarButton(ImageRes.getIcon("icons/tools.png"), Lang.get("Menu.Settings"));
    adminButtons.add(settBtn);
    settBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
          
        new SettingsDialog(frame);
        
      }
    });      
    toolBarElem.add(settBtn);    
    toolBarElem.add(new JLabel(" ")); 
    
    aboutBtn = new ToolBarButton(ImageRes.getIcon("icons/help.png"), Lang.get("Menu.About"));
    aboutBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
       
        new AboutDialog(frame);
        
      }
    });            
    toolBarElem.add(aboutBtn);
    toolBarElem.add(new JLabel(" "));
    
    quitBtn = new ToolBarButton(ImageRes.getIcon("icons/exit.png"), Lang.get("Menu.Quit"));
    quitBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(final ActionEvent e) {
          
        boolean res = new ConfirmDialog(frame, Lang.get("Menu.Quit.AreYouSure")).isConfirmed();        
        if (res) frame.quitApp();
        
      }
    });      
    toolBarElem.add(quitBtn);
    
    add(toolBarElem);
    add(Box.createHorizontalGlue());
     
    
    JComboBox<Locale> langList = new JComboBox<>(IConf.LOCALES);    
    langList.setSelectedIndex(IConf.DEFAULT_LOCALE_INDEX);
    langList.setFont(GUI.BASE_FONT);
    langList.setBorder(new EmptyBorder(0, 10, 0, 15));
    langList.setEditable(false);
    langList.setRenderer(new ListCellRenderer<Locale>() {
		@Override
		public Component getListCellRendererComponent(JList<? extends Locale> list, Locale value, int index,
			boolean isSelected, boolean cellHasFocus) {
			DefaultListCellRenderer listRenderer = new DefaultListCellRenderer();
			JLabel c = (JLabel) listRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus); 
			c.setText(value.getDisplayLanguage(value));	
			c.setIcon(Lang.getIcon(value));
			return c;
		}
	});
    langList.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Lang.setLocale(langList.getSelectedIndex());
			frame.refreshAfterLangChange();
			frame.repaint();			
		}
	});

    langList.setMaximumSize(new Dimension(150, 24));
    add(langList);        
    
    setMaximumSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width,35));
      
  }
  

  /**
   * Metoda w razie potrzeby blokuje przyciski tylko dla administratora
   * @param user Obiekt zalogowanego użytkownika
   */
  public void lockButtons(User user) {
      
     for (JButton b: adminButtons) b.setEnabled(user.isAdmin());
      
  }
  
  
  
  public void setBtnCaptions() {
	  	    
	 orderBtn.setToolTipText(Lang.get("Menu.NewOrder"));
	 delivBtn.setToolTipText(Lang.get("Menu.NewDelivery"));
	 settBtn.setToolTipText(Lang.get("Menu.Settings"));
	 aboutBtn.setToolTipText(Lang.get("Menu.About"));
	 quitBtn.setToolTipText(Lang.get("Menu.Quit"));
	 
  }
  
    
}
