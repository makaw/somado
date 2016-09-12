/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Program jest częścią pracy dyplomowej inżynierskiej zrealizowanej
 *  na Wydziale Elektrycznym Politechniki Warszawskiej.
 *  Autor: Maciej Kawecki 2016
 * 
 */
package gui.dialogs;


import datamodel.ShortestPathAlgorithm;
import datamodel.docs.DocAudit;
import gui.formfields.FormRowPad;
import gui.GUI;
import gui.IconButton;
import gui.ImageRes;
import gui.formfields.SliderField;
import gui.dialogs.docpanels.DialogDocPanel;
import gui.dialogs.gloss.GlossCustomerEditModDialog;
import gui.formfields.FormRow;
import gui.formfields.FormTabbedPane;
import gui.mapview.MapDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.SQLException;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import somado.Database;
import somado.IConf;
import somado.Settings;
import somado.SettingsException;
import somado.User;


/**
 *
 * Szablon obiektu wywołującego okienka z formularzem zmiany ustawień 
 * 
 * @author Maciej Kawecki
 * @version 1.0
 * 
 */
@SuppressWarnings("serial")
public class SettingsDialog extends EditLockableDataDialog {

    /** Ref. do globalnej bazy danych */
    private final Database database;
    /** Ref. do obiektu zalogowanego użytkownika */
    private final User user;
    
    /**
     * Konstruktor, wypełnienie wewn. pól  i wyświetlenie okienka
     * @param frame Referencja do GUI
     */          
    public SettingsDialog(GUI frame) {
     
      super(frame,  IConf.APP_NAME + " - Ustawienia");
      this.database = frame.getDatabaseShared();
      this.user = frame.getUser();
      if (!user.isAdmin()) return;
      checkLock(Settings.getInstance());
      super.showDialog(530, 490);
        
    }    
    
   
    
    /**
     * Metoda wyświetlająca zawartość okienka
     */    
    @Override
    protected void getContent()  {
        
       final Color bgColor = new Color(0xddecfa);

       setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
       
       final JTabbedPane tabPane = new FormTabbedPane(bgColor);        
       JPanel tabPane1 =  (JPanel)tabPane.add("Ustawienia", new JPanel());
       final JPanel auditPane =  (JPanel)tabPane.add("Historia zmian", new JPanel());
   
       tabPane1.setPreferredSize(new Dimension(500, 460));
       
       auditPane.add(getAuditPanel(bgColor));
       
       final JPanel buttonsPanel = new JPanel(new FlowLayout());
     
       tabPane.addChangeListener(new ChangeListener() { 
            @Override
            public void stateChanged(ChangeEvent evt) { 
                JTabbedPane pane = (JTabbedPane) evt.getSource(); 
                Component sel = pane.getSelectedComponent();
                try {
                  buttonsPanel.setVisible(!sel.equals(auditPane));
                }
                catch (NullPointerException e) {}
            } 
        });       
       
       
       final JButton buttonChange;
   
       JPanel depotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
       depotPanel.setMaximumSize(new Dimension(600, 85));  
       depotPanel.setBorder(new EmptyBorder(0, 8, 4, 0));
       depotPanel.setOpaque(false);
        
       final JTextArea depotField = new JTextArea();
       depotField.setText(Settings.getDepot().toString());
        
       depotField.setEditable(false);
       depotField.setLineWrap(true);
       depotField.setWrapStyleWord(true);
       depotField.setRows(3);
       depotField.setBackground(new Color(0xeaeaea));
        
       JScrollPane sc = new JScrollPane(depotField);
       sc.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
       sc.setPreferredSize(new Dimension(280, 50));
       depotPanel.add(sc);        
       
       final IconButton mapButton = new IconButton(ImageRes.getIcon("icons/map.png"), "Zobacz na mapie");              
       mapButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
            
              new MapDialog(frame, "mapa: magazyn", Settings.getDepot().getLongitude(), 
                      Settings.getDepot().getLatitude());
               
           }
       });
       
                      
       depotPanel.add(new JLabel(" "));
       depotPanel.add(mapButton);

       
       final IconButton changeButton = new IconButton(ImageRes.getIcon("icons/form_edit.png"), "Zmie\u0144 dane");        
       changeButton.setEnabled(!locked);
       changeButton.addActionListener(new ActionListener() {

           @Override
           public void actionPerformed(ActionEvent e) {
            
              new GlossCustomerEditModDialog(frame);
              
              try {
                Settings.getInstance().load(database);
              }
              catch (SettingsException e1) {}
                            
              depotField.setText(Settings.getDepot().toString());

               
           }

       });
      
       depotPanel.add(changeButton);        
        
       tabPane1.add(new FormRow("<html>Magazyn:<br /><br /><br /><br /></html>", depotPanel));                    
       
       JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
       p.setMaximumSize(new Dimension(600, 40));
       p.setOpaque(false);
       p.setBorder(new EmptyBorder(5, 0, 10, 0));
       final JComboBox<ShortestPathAlgorithm> algorithmField  = new JComboBox<>();      
       algorithmField.setEditable(!locked);
       algorithmField.setModel(new DefaultComboBoxModel<>(ShortestPathAlgorithm.values()));
       algorithmField.setFont(GUI.BASE_FONT);
       algorithmField.setPreferredSize(new Dimension(120, 32));
       
       ShortestPathAlgorithm algorithm = ShortestPathAlgorithm.get(Settings.getValue("shortest_path_algorithm"));
       algorithmField.setSelectedItem(algorithm);
       
       JPanel p0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
       p0.setOpaque(false);
       final JTextArea algDescField = new JTextArea(algorithm.getDescription());
       algDescField.setEditable(false);
       algDescField.setOpaque(false);
       algDescField.setBorder(new EmptyBorder(5, 5, 5, 5));
       algDescField.setPreferredSize(new Dimension(230, 40));
       algDescField.setLineWrap(true);
       algDescField.setWrapStyleWord(true);
       p0.add(algorithmField);
       p0.add(algDescField);
       
       p.add(new FormRow("<html>Algorytm najkr\u00f3t-<br />szej \u015bcie\u017cki:</html>", p0));
       tabPane1.add(p);
       
       algorithmField.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent event) {
           if (event.getStateChange() == ItemEvent.SELECTED) 
             algDescField.setText(((ShortestPathAlgorithm)event.getItem()).getDescription());
         }       
       });
       
       final SliderField workTimeField = new SliderField("max", 1, 1.0, 10.0, "h");
       workTimeField.setEnabled(!locked);
       double maxWorkTime = 1.0;
       try {
         maxWorkTime = Double.valueOf(Settings.getValue("driver_max_work_time"));
       }
       catch (NumberFormatException e) {}
       workTimeField.setValue(maxWorkTime);
       workTimeField.setBorder(new EmptyBorder(10, 0, 0, 0));
       tabPane1.add(new FormRowPad("<html>Maksymalny czas<br />jazdy kierowcy<br />w 1 dostawie:</html>",
               workTimeField));
       
       p0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
       p0.setOpaque(false);
       p0.setPreferredSize(new Dimension(600, 70));
       final JCheckBox addGeoField = new JCheckBox("<html> &nbsp; dodatkowa geometria dr\u00f3g, u\u015bci\u015bla "
               + "geometri\u0119<br /> &nbsp; niekt\u00f3rych odcink\u00f3w oraz odleg\u0142o\u015bci, mo\u017ce "
               + "poprawia\u0107<br />&nbsp;  jako\u015b\u0107 rozwi\u0105za\u0144, "
               + "ale wyd\u0142u\u017ca czas oblicze\u0144</html>");
       try {
         addGeoField.setSelected(Settings.getIntValue("additional_geometry")==1);
       }
       catch (NumberFormatException e) { addGeoField.setSelected(false); }
       addGeoField.setOpaque(false);
       addGeoField.setFocusPainted(false);
       addGeoField.setEnabled(!locked);
       addGeoField.setFont(GUI.BASE_FONT);
       p0.add(addGeoField);
       tabPane1.add(new FormRowPad("<html>Dodatkowa<br />geometria: <br />&nbsp; </html>", p0));       
       
       p = new JPanel(new FlowLayout(FlowLayout.LEFT));
       p.add(new JLabel("Maksymalna wy\u015bwietlana ilo\u015b\u0107 wierszy:                      ", JLabel.LEFT));
       p.setOpaque(false);
       tabPane1.add(p);
       
       p0 = new JPanel(new GridLayout(1, 2, 30, 0));
       p0.setOpaque(false);
       
       final JTextField itemsPerPageField = new JTextField(7);  
       itemsPerPageField.setEditable(!locked);
       itemsPerPageField.setText(Settings.getValue("items_per_page")); 
       p0.add(new FormRowPad("<html>Tabela danych:<br /> &nbsp; </html>", itemsPerPageField));
       itemsPerPageField.setMaximumSize(new Dimension(140, 25));
       
       final JTextField itemsPerPageDocField = new JTextField(7);
       itemsPerPageDocField.setEditable(!locked);
       itemsPerPageDocField.setText(Settings.getValue("items_per_page_doc")); 
       p0.add(new FormRowPad("<html>Okno tekstowe (np. historia):</html>:",
               itemsPerPageDocField));
       itemsPerPageDocField.setMaximumSize(new Dimension(140, 25));                  
       p.add(p0);
       
       // przygotowanie przycisków Zastosuj i Anuluj
       buttonChange = new JButton("Zastosuj");
       buttonChange.setEnabled(!locked);
       buttonChange.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) { 
              
             try {
                 
               int itemsPage = Integer.parseInt(itemsPerPageField.getText());
               int itemsPageDoc = Integer.parseInt(itemsPerPageDocField.getText());          
               
               if (itemsPage<=0 || itemsPageDoc<=0) 
                   throw new NumberFormatException();
            

               Settings.updateKey(database, user, "items_per_page", itemsPerPageField.getText());
               Settings.updateKey(database, user, "items_per_page_doc", itemsPerPageDocField.getText());
               Settings.updateKey(database, user, "shortest_path_algorithm", 
                       ShortestPathAlgorithm.get(algorithmField.getSelectedItem().toString()).toString());
               Settings.updateKey(database, user, "driver_max_work_time",
                       String.valueOf(workTimeField.getValue()));
               Settings.updateKey(database, user, "additional_geometry", addGeoField.isSelected() ? "1" : "0");
             
               int tmp = -1;
               try {
                 tmp = Integer.parseInt(Settings.getValue("items_per_page"));
               }
               catch (NumberFormatException ex) {}
               
               Settings.getInstance().load(database);
               
               // przeladowanie aktywnej zakladki
               if (itemsPage != tmp) frame.refreshTabPanels();                                
               
               dispose();
               
             }
             
             catch (NumberFormatException e1) {
                               
               new ErrorDialog(frame, "Podano niepoprawne dane.");  
                 
             }             
             catch (SQLException e2) {
                 
               String error = "Wyst\u0105pi\u0142 b\u0142\u0105d SQL: " + e2.getMessage() + "\n"                       
                  + "Spr\u00f3buj ponownie, lub skontaktuj si\u0119 z administratorem.";
               new ErrorDialog(frame, error, true); 
                 
             }
             catch (SettingsException e3) {}
              
              
             
          }
       });
       
       JButton buttonCancel = new CloseButton("Anuluj");
             
       
       buttonsPanel.setBorder(new EmptyBorder(5, 0, 5, 0)); 
      
       buttonsPanel.add(buttonChange);
       buttonsPanel.add(buttonCancel);
       
       add(tabPane);
       add(buttonsPanel);              

    }

    
   /**
    * Metoda zwraca panel z lista historii zmian
    * @param bgColor Kolor tla
    * @return Panel z lista historii zmian
    */    
    protected JPanel getAuditPanel(Color bgColor) {
        
      DocAudit docAudit = new DocAudit(frame.getDatabaseShared(), Settings.getInstance());

      return new DialogDocPanel<>(docAudit, bgColor);
        
        
    }   
    
    
}



