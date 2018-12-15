/*
 * 
 *  Somado (System Optymalizacji Małych Dostaw)
 *  Optymalizacja dostaw towarów, dane OSM, problem VRP
 * 
 *  Autor: Maciej Kawecki 2016 (praca inż. EE PW)
 * 
 */
package gui.dialogs;


import datamodel.ShortestPathAlgorithm;
import gui.formfields.FormRowPad;
import gui.GUI;
import gui.IconButton;
import gui.ImageRes;
import gui.SimpleDialog;
import gui.formfields.SliderField;
import gui.dialogs.gloss.GlossCustomerEditModDialog;
import gui.formfields.FormRow;
import gui.formfields.FormTabbedPane;
import gui.mapview.MapDialog;
import java.awt.Color;
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
import somado.Database;
import somado.IConf;
import somado.Lang;
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
public class SettingsDialog extends SimpleDialog {

    /** Ref. do globalnej bazy danych */
    private final Database database;
    /** Ref. do obiektu zalogowanego użytkownika */
    private final User user;
    
    /**
     * Konstruktor, wypełnienie wewn. pól  i wyświetlenie okienka
     * @param frame Referencja do GUI
     */          
    public SettingsDialog(GUI frame) {
     
      super(frame,  IConf.APP_NAME + " - " + Lang.get("Dialogs.Settings"));
      this.database = frame.getDatabase();
      this.user = frame.getUser();
      if (!user.isAdmin()) return;
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
       JPanel tabPane1 =  (JPanel)tabPane.add(Lang.get("Dialogs.Settings"), new JPanel());
   
       tabPane1.setPreferredSize(new Dimension(500, 460));
       
       
       final JPanel buttonsPanel = new JPanel(new FlowLayout());     
       
       
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
       
       final IconButton mapButton = new IconButton(ImageRes.getIcon("icons/map.png"), Lang.get("Dialogs.Settings.MapPreview"));              
       mapButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
            
              new MapDialog(frame,  Lang.get("Dialogs.Settings.MapDepot"), Settings.getDepot().getLongitude(), 
                      Settings.getDepot().getLatitude());
               
           }
       });
       
                      
       depotPanel.add(new JLabel(" "));
       depotPanel.add(mapButton);

       
       final IconButton changeButton = new IconButton(ImageRes.getIcon("icons/form_edit.png"), Lang.get("ChangeData"));            
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
        
       tabPane1.add(new FormRow("<html>" + Lang.get("Dialogs.Settings.Depot") + ":<br /><br /><br /><br /></html>", depotPanel));                    
       
       JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
       p.setMaximumSize(new Dimension(600, 40));
       p.setOpaque(false);
       p.setBorder(new EmptyBorder(5, 0, 10, 0));
       final JComboBox<ShortestPathAlgorithm> algorithmField  = new JComboBox<>();    
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
       
       p.add(new FormRow("<html>" + Lang.get("Dialogs.Settings.ShortestPathAlgorithm") + ":</html>", p0));
       tabPane1.add(p);
       
       algorithmField.addItemListener(new ItemListener() {
         @Override
         public void itemStateChanged(ItemEvent event) {
           if (event.getStateChange() == ItemEvent.SELECTED) 
             algDescField.setText(((ShortestPathAlgorithm)event.getItem()).getDescription());
         }       
       });
       
       final SliderField workTimeField = new SliderField("max", 1, 1.0, 10.0, "h");
       double maxWorkTime = 1.0;
       try {
         maxWorkTime = Double.valueOf(Settings.getValue("driver_max_work_time"));
       }
       catch (NumberFormatException e) {}
       workTimeField.setValue(maxWorkTime);
       workTimeField.setBorder(new EmptyBorder(10, 0, 0, 0));
       tabPane1.add(new FormRowPad("<html>" + Lang.get("Dialogs.Settings.MaxDriverWorkTime") + ":</html>",
               workTimeField));
       
       p0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
       p0.setOpaque(false);
       p0.setPreferredSize(new Dimension(600, 70));
       final JCheckBox addGeoField = new JCheckBox("<html> &nbsp; " + Lang.get("Dialogs.Settings.AdditionalGeometry.Desc") + "</html>");
       try {
         addGeoField.setSelected(Settings.getIntValue("additional_geometry")==1);
       }
       catch (NumberFormatException e) { addGeoField.setSelected(false); }
       addGeoField.setOpaque(false);
       addGeoField.setFocusPainted(false);
       addGeoField.setFont(GUI.BASE_FONT);
       p0.add(addGeoField);
       tabPane1.add(new FormRowPad("<html>" + Lang.get("Dialogs.Settings.AdditionalGeometry") + "<br />&nbsp; </html>", p0));       
       
       p = new JPanel(new FlowLayout(FlowLayout.LEFT));
       p.add(new JLabel(Lang.get("Dialogs.Settings.MaxRowCount") + ":                      ", JLabel.LEFT));
       p.setOpaque(false);
       tabPane1.add(p);
       
       p0 = new JPanel(new GridLayout(1, 2, 30, 0));
       p0.setOpaque(false);
       
       final JTextField itemsPerPageField = new JTextField(7);  
       itemsPerPageField.setText(Settings.getValue("items_per_page")); 
       p0.add(new FormRowPad("<html>" + Lang.get("Dialogs.Settings.MaxRowCount.DataTable") + ":<br /> &nbsp; </html>", itemsPerPageField));
       itemsPerPageField.setMaximumSize(new Dimension(140, 25));
       
       final JTextField itemsPerPageGlossField = new JTextField(7);
       itemsPerPageGlossField.setText(Settings.getValue("items_per_page_gloss")); 
       p0.add(new FormRowPad("<html>" + Lang.get("Dialogs.Settings.MaxRowCount.GlossList") + ":<br /> &nbsp; </html>:",
               itemsPerPageGlossField));
       itemsPerPageGlossField.setMaximumSize(new Dimension(140, 25));                  
       p.add(p0);
       
       buttonChange = new JButton(Lang.get("Apply"));
       buttonChange.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(final ActionEvent e) { 
              
             try {
                 
               int itemsPage = Integer.parseInt(itemsPerPageField.getText());
               int itemsPageGloss = Integer.parseInt(itemsPerPageGlossField.getText());          
               
               if (itemsPage<=0 || itemsPageGloss<=0) 
                   throw new NumberFormatException();
            

               Settings.updateKey(database, user, "items_per_page", itemsPerPageField.getText());
               Settings.updateKey(database, user, "items_per_page_gloss", itemsPerPageGlossField.getText());
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
                               
               new ErrorDialog(frame, Lang.get("Error.WrongData"));  
                 
             }             
             catch (SQLException e2) {
                 
               String error = Lang.get("Error.Sql", e2.getMessage()) + "\n" ;
               new ErrorDialog(frame, error, true); 
                 
             }
             catch (SettingsException e3) {}
              
              
             
          }
       });
       
       JButton buttonCancel = new CloseButton(Lang.get("Cancel"));
             
       
       buttonsPanel.setBorder(new EmptyBorder(5, 0, 5, 0)); 
      
       buttonsPanel.add(buttonChange);
       buttonsPanel.add(buttonCancel);
       
       add(tabPane);
       add(buttonsPanel);              

    }

  
    
    
}



