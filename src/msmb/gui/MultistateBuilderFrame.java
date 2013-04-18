package  msmb.gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.TableColumn;
import java.util.*;

import msmb.commonUtilities.tables.ScientificFormatCellRenderer;
import msmb.model.*;
import msmb.utility.*;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class MultistateBuilderFrame extends JDialog	 {

	private static final long serialVersionUID = 1L;
	protected static int row_to_highlight;
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JTextField jTextField_species = null;
	private JPanel jPanel = null;
	private JLabel jLabel1 = null;
	private JTextField jTextField_newSite = null;
	private JSpinner spinner_lower = null;
	private JSpinner spinner_upper = null;
	private JTextField jTextField_listStates = null;
	private JButton jButton = null;
	private JScrollPane jScrollPane = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	private JLabel jLabel3b = null;
	private MultistateSpecies species; 
	private MainGui parentFrame;
	private JButton jButton1 = null;
	private JButton jButton3 = null;
	private CustomFocusTraversalPolicy newPolicy;
	private JPanel upper = null;
	private JTabbedPane jTabbedPane = null;
	private JPanel jPanelConcentrationSpecies = null;
	private JScrollPane jScrollPaneConcentrationSpecies= null;
	private CustomTableModel_MSMB tableConcentrationSpeciesmodel= null;
	private CustomJTable_MSMB jTableConcentrationSpecies;
	private JList<String> jListSite = null;
	private JPanel panel;
	private JLabel lblWarning;
	private JRadioButton jRadioBoolean;
	
	public MultistateBuilderFrame(MainGui owner) throws Exception {
		super(owner);
		initialize();
		species = new MultistateSpecies(MainGui.multiModel,new String());
		parentFrame = owner;
	}
	
	private void initialize() {
		this.setSize(407, 400);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setContentPane(getJContentPane());
		this.setTitle("Multistate Builder");
		this.setLocationRelativeTo(parentFrame);
		this.setResizable(false);
		
		Vector<Component> order = new Vector<Component>(7);
        order.add(jTextField_species);
        order.add(jTextField_newSite);
        order.add(spinner_lower.getEditor().getComponent(0));
        order.add(spinner_upper.getEditor().getComponent(0));
        order.add(jTextField_listStates);
        order.add(jButton);
        order.add(jButton3);
        newPolicy = new CustomFocusTraversalPolicy(order);
        this.setFocusTraversalPolicy(newPolicy);
                
         this.setTabbedPane_enable(false);
        
  }

	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getUpper(), BorderLayout.NORTH);
			jContentPane.add(getJTabbedPane(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	private JTabbedPane getJTabbedPane() {
	
		if (jTabbedPane == null) {
			
			jTabbedPane = new JTabbedPane();
			jTabbedPane.addTab("Site details", null, this.getJPanelSpeciesDetails(), null);
			jTabbedPane.addTab(Constants.MultistateBuilder_QUANTITIES_description, null, getPanel(), null);
		}
		return jTabbedPane;
	}
	
	
	private JPanel getUpper() {
		if (upper == null) {
			upper = new JPanel();
			jLabel = new JLabel();
			jLabel.setText("Species name:");
			jLabel.setBounds(new Rectangle(11, 11, 84, 16));
			upper.setLayout(null);
			upper.setPreferredSize(new Dimension(84, 38));
			upper.add(jLabel, null);
			upper.add(getJTextField_species(), null);
			upper.add(getJButton3(), null);
		}
		return upper;
	}
	
	private void setTabbedPane_enable(boolean b) {
		jTabbedPane.setEnabled(b);
		jPanelConcentrationSpecies.setEnabled(b);
		jTableConcentrationSpecies.setEnabled(b);
		this.jTextField_listStates.setEnabled(b);
		this.jTextField_newSite.setEnabled(b);
		this.spinner_lower.setEnabled(b);
		this.spinner_upper.setEnabled(b);
		this.jLabel1.setEnabled(b);
		this.jLabel2.setEnabled(b);
		lblWarning.setEnabled(b);
		jRadioBoolean.setEnabled(b);
		jLabel3.setEnabled(b);
		jLabel3b.setEnabled(b);
		//this.jLabel4.setEnabled(b);
		//this.jTableListSite.setEnabled(b);
		this.jListSite.setEnabled(b);
		this.jButton.setEnabled(b);
		this.jButton1.setEnabled(b);
		if(!b) jButton.setForeground(Color.lightGray);
		else jButton.setForeground(jButton1.getForeground());
	}
	
	private JTextField getJTextField_species() {
		if (jTextField_species == null) {
			jTextField_species = new JTextField();
			jTextField_species.setBounds(new Rectangle(86, 10, 125, 18));
			jTextField_species.addKeyListener(new KeyListener() {
			        public void keyTyped(KeyEvent keyEvent) {
			          if(jTextField_species.getText().length() > 0) {
			        	 setTabbedPane_enable(true);   	  
			          } else {
			        	  setTabbedPane_enable(false); 
			          }
			        }

					public void keyPressed(KeyEvent arg0) {
						return;
					}

					public void keyReleased(KeyEvent e) {
						return;
					}
			      
			      });
		}
		return jTextField_species;
	}

	private JPanel getJPanelSpeciesDetails() {
		if (jPanel == null) {
			jLabel3 = new JLabel();
			jLabel3.setEnabled(false);
			jLabel3.setBounds(new Rectangle(117, 35, 23, 50));
			jLabel3.setText(" {");
			jLabel3.setFont(new Font("Arial", Font.PLAIN, 15));
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(194, 23, 15, 20));
			jLabel2.setText(" :");
			jLabel1 = new JLabel();
			jLabel1.setText("New site:");
			jLabel1.setBounds(new Rectangle(11, 17, 70, 20));	
			jPanel = new JPanel();
			
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(11, 11, 11, 11);
			GridBagLayout gbl_jPanel = new GridBagLayout();
			gbl_jPanel.columnWeights = new double[]{1.0};
			gbl_jPanel.rowWeights = new double[]{1.0};
			jPanel.setLayout(gbl_jPanel);
			
			JPanel jPanelsites = new JPanel();
			
			jPanelsites.setLayout(null);
			jPanelsites.setPreferredSize(new Dimension(545, 60));
			jPanelsites.setMinimumSize(new Dimension(545, 60));
			jPanelsites.setBorder(BorderFactory.createTitledBorder(null, "Sites:", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, UIManager.getFont("Label.font"), new Color(51, 51, 51)));
			jLabel3b = new JLabel();
			jLabel3b.setEnabled(false);
			jLabel3b.setBounds(new Rectangle(280, 35, 15, 50));
			jLabel3b.setText("} ");
			jLabel3b.setFont(new Font("Arial", Font.PLAIN, 15));
			jPanelsites.add(jLabel3b, null);
			jPanelsites.add(jLabel1, null);
			jPanelsites.add(getJTextField_newSite(), null);
			jPanelsites.add(getSpinner_lower(), null);
			jPanelsites.add(getSpinner_upper(), null);
			jPanelsites.add(getJTextField_listStates(), null);
			jPanelsites.add(getJButton(), null);
			jPanelsites.add(getJScrollPane(), null);
			jPanelsites.add(jLabel2, null);
			jPanelsites.add(jLabel3, null);
			jPanelsites.add(getJButton1(), null);
			jPanel.add(jPanelsites, gridBagConstraints);
			
			lblWarning = new JLabel("<html><p ALIGN=\"LEFT\">WARNING: any change in the sites' definition will reset all the defined <p ALIGN=\"LEFT\">initial quantities to the default initial value of "+MainGui.species_defaultInitialValue+" </html>");
			lblWarning.setBounds(11, 110, 339, 30);
			jPanelsites.add(lblWarning);
			
			jRadioBoolean = new JRadioButton("boolean {TRUE, FALSE}c");
			jRadioBoolean.setEnabled(false);
			jRadioBoolean.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if(jRadioBoolean.isSelected()) {
						spinner_upper.setValue(0); spinner_upper.revalidate();
						spinner_lower.setValue(0); spinner_lower.revalidate();
						jTextField_listStates.setText(""); jTextField_listStates.revalidate();
					}
				}
			});
			jRadioBoolean.setBounds(129, 50, 144, 23);
			jPanelsites.add(jRadioBoolean);
			
						
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.BOTH;
			gridBagConstraints2.gridy = 1;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.gridx = 0;
			//jPanel.add(getJPanelConcentrationSpecies(), gridBagConstraints2);
			
		}
		return jPanel;
	}
	
	private JPanel getJPanelConcentrationSpecies() {
		if (jPanelConcentrationSpecies == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jPanelConcentrationSpecies = new JPanel();
			jPanelConcentrationSpecies.setLayout(new GridBagLayout());
			jPanelConcentrationSpecies.add(getJScrollPaneConcentrationSpecies(), gridBagConstraints);
		}
		return jPanelConcentrationSpecies;
	}
	
	private JScrollPane getJScrollPaneConcentrationSpecies() {
		if (jScrollPaneConcentrationSpecies == null) {
			jScrollPaneConcentrationSpecies = new JScrollPane();
			jScrollPaneConcentrationSpecies.setViewportView(getJTableConcentrationSpecies());
		}
		return jScrollPaneConcentrationSpecies;
	}
	
	private JTable getJTableConcentrationSpecies() {
		if (jTableConcentrationSpecies == null) {
			Vector<String> col = new Vector<String>();
			col.add("Expanded species");
				col.add("Initial Quantity");
			
						
			tableConcentrationSpeciesmodel = new CustomTableModel_MSMB(Constants.MultistateBuilder_QUANTITIES_description,col,new Vector(),this);
			jTableConcentrationSpecies = new CustomJTable_MSMB();
			jTableConcentrationSpecies.setModel(tableConcentrationSpeciesmodel);

			
			if(parentFrame!=null) jTableConcentrationSpecies.setCustomFont(parentFrame.getCustomFont());
	        
			jTableConcentrationSpecies.initializeCustomTable(tableConcentrationSpeciesmodel);
			
		}
		return jTableConcentrationSpecies;
	}
	
	private void updateJTableConcentrationSpecies(){
		
		Vector<String> col = new Vector<String>();
		Set<String> names= this.species.getSitesNames();

	    Iterator<String> iterator = names.iterator();  
	    while (iterator.hasNext()) {  
	    	col.add(iterator.next());
	    }  
	    
	    if(names.size() ==0) {
	    	col.add("Expanded species");
	    }
		col.add("Initial Quantity");
		
		tableConcentrationSpeciesmodel = new CustomTableModel_MSMB(Constants.MultistateBuilder_QUANTITIES_description,col,new Vector(),this,false,true);
		
		jTableConcentrationSpecies = new CustomJTable_MSMB();
		jTableConcentrationSpecies.setModel(tableConcentrationSpeciesmodel);
		
		jTableConcentrationSpecies.initializeCustomTable(tableConcentrationSpeciesmodel);
		jTableConcentrationSpecies.revalidate();
		jScrollPaneConcentrationSpecies.setViewportView(jTableConcentrationSpecies);
	
		
		TableColumn column = jTableConcentrationSpecies.getColumnModel().getColumn(col.size()-1);  
        column.setCellRenderer(new ScientificFormatCellRenderer());
        
        TableColumn column2 = jTableConcentrationSpecies.getColumnModel().getColumn(col.size()-2);  
        column2.setCellRenderer(new ScientificFormatCellRenderer());
      
        
		 HashMap<String, Integer> index_columns = new HashMap<String, Integer>();
		 for(int i = 0; i < tableConcentrationSpeciesmodel.getColumnCount(); i++ ){
			 index_columns.put(tableConcentrationSpeciesmodel.getColumnName(i), new Integer(i));
		 }
		 
		 Vector<Vector> singleConfigurations = this.species.getExpandedVectors();
		 for(int i = 0; i < singleConfigurations.size(); i++) {
			 Vector<Vector> singleConf = singleConfigurations.get(i);
			 Object[] singleConf_state = new Object[singleConf.size()/2];
			 for(int j = 0; j < singleConf.size(); j=j+2 ){
				 singleConf_state[(index_columns.get(singleConf.get(j))).intValue()-1] = singleConf.get(j+1);
			 }
			 Vector<Object> v = new Vector<Object>(Arrays.asList(singleConf_state));
			
			 
		 String quantity = species.getInitial_singleConfiguration(singleConf);
			 if(quantity!= null) v.add(quantity);
			 else v.add(MainGui.species_defaultInitialValue);
			 
			 
			 this.tableConcentrationSpeciesmodel.addRow(v);
			 for(int jj = 0; jj <tableConcentrationSpeciesmodel.getColumnCount()-1; jj++ ) {
				 tableConcentrationSpeciesmodel.disableCell(i, jj);
			 }
				
		 }
		 
		 this.jTableConcentrationSpecies.revalidate();
	    
	}

	private JTextField getJTextField_newSite() {
		if (jTextField_newSite == null) {
			jTextField_newSite = new JTextField();
			jTextField_newSite.setBounds(new Rectangle(11, 51, 106, 20));
		}
		return jTextField_newSite;
	}

	private JSpinner getSpinner_lower() {
		if (spinner_lower == null) {
			spinner_lower = new JSpinner();
			spinner_lower.setBounds(new Rectangle(133, 23, 60, 20));
			((SpinnerNumberModel)spinner_lower.getModel()).setMaximum(100);
			((SpinnerNumberModel)spinner_lower.getModel()).setMinimum(0);
			spinner_lower.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					jTextField_listStates.setText("");
					jRadioBoolean.setSelected(false);
					jTextField_listStates.revalidate();
				}
			});
			spinner_lower.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					jTextField_listStates.setText("");
					jRadioBoolean.setSelected(false);
					jTextField_listStates.revalidate();
				}
			});
		}
		return spinner_lower;
	}

	private JSpinner getSpinner_upper() {
		if (spinner_upper == null) {
			spinner_upper = new JSpinner();
			spinner_upper.setBounds(new Rectangle(206, 23, 60, 20));
			((SpinnerNumberModel)spinner_upper.getModel()).setMaximum(100);
			((SpinnerNumberModel)spinner_upper.getModel()).setMinimum(0);
			spinner_upper.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					jTextField_listStates.setText("");
					jRadioBoolean.setSelected(false);
					jTextField_listStates.revalidate();
				}
			});
			spinner_upper.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					jTextField_listStates.setText("");
					jRadioBoolean.setSelected(false);
					jTextField_listStates.revalidate();
				}
			});
		}
		return spinner_upper;
	}

	private JTextField getJTextField_listStates() {
		if (jTextField_listStates == null) {
			jTextField_listStates = new JTextField();
			jTextField_listStates.setBounds(new Rectangle(133, 79, 133, 20));
			jTextField_listStates.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent e) {
					spinner_upper.setValue(0); spinner_upper.revalidate();
					spinner_lower.setValue(0); spinner_lower.revalidate();
					jRadioBoolean.setSelected(false);
				}
			});
			
		}
		return jTextField_listStates;
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(287, 40, 60, 56));
			jButton.setMargin(new Insets(11,9,11,11));
			jButton.setHorizontalAlignment(SwingConstants.CENTER);
			jButton.setText("<html><p ALIGN=\"CENTER\">Add /<p ALIGN=\"CENTER\">Change</html>");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					addNewSite();
				}
			});
		}
		return jButton;
	}
	
	private void addNewSite() {
		String sp = this.jTextField_species.getText().trim();
		if(sp.length() <= 0) return;
	
		String name = this.jTextField_newSite.getText().trim();
		if(name.length() <= 0) return;
		
		int lower = (Integer)this.spinner_lower.getValue();
		int upper = (Integer)this.spinner_upper.getValue();
		
		if(upper < lower) {
			  JOptionPane.showMessageDialog(this,"The upper value of the range cannot be smaller than the lower!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		try {
			if(jRadioBoolean.isSelected()) {		
				this.species.addSite_string(name, Constants.BooleanType.TRUE.description + "," +Constants.BooleanType.FALSE.description);
			}
			else if(this.jTextField_listStates.getText().trim().length() > 0) {
				this.species.addSite_string(name, this.jTextField_listStates.getText().trim());
			}
			else {
				this.species.addSite_range(name, lower, upper);
			}
		} catch(Exception ex) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
			//problem in parsing species (e.g. cdhBoolWrong(p{TRUE,FALSE,somethingElse}))
			  JOptionPane.showMessageDialog(this,"Problem parsing the current site. The definition cannot be accepted.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
	
		this.updateJTableConcentrationSpecies();

		//this.refreshListSitesInTable();
		this.refreshListSites();
	
		
	}
	
	
	private void deleteSite() {
		int selected = this.jListSite.getSelectedIndex();//this.jTableListSite.getSelectedRow();
		if(selected == -1) return;
		//String site = (String) (this.jTableListSite.getModel().getValueAt(selected, 0));
		String site = this.jListSite.getSelectedValue();
		if(site.length() == 0 ) return;
		this.species.deleteSite(site.substring(0, site.indexOf("{")));
		this.refreshListSites();
		this.updateJTableConcentrationSpecies();
		
	}
	
	
	
	
	
	private void refreshListSites() {
		DefaultListModel<String> model = (DefaultListModel<String>)this.jListSite.getModel();
		model.clear();
		
		
		Set<String> names = this.species.getSitesNames();
		
	    Iterator<String> iterator = names.iterator();  
	    while (iterator.hasNext()) {  
	       String site_name = iterator.next();
	       String pr = species.printSite(site_name);//, false);  
	       //Vector row = new Vector();
	       //row.add(pr);
	      // row.add(((Integer)(species.getStartIndexList(site_name))).toString());
	       model.addElement(pr);
	    }  
	    
	   /* if(names.size()==0){
	    	 model.addRow(new Vector(Arrays.asList("","")));
	    }*/
		
		this.jTextField_newSite.setText("");
		this.jTextField_listStates.setText("");
		this.jRadioBoolean.setSelected(false);
		this.spinner_lower.setValue(0);
		this.spinner_upper.setValue(0);
		
		
		this.jListSite.revalidate();
		
		
	}



	

	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(10, 150, 354, 86));
			jScrollPane.setViewportView(getJListSite());
		}
		return jScrollPane;
	}

	private JList<String> getJListSite() {
		if (jListSite == null) {
			jListSite = new JList<String>();
			jListSite.setModel(new DefaultListModel<String>());
			jListSite.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			
			jListSite.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
				public void valueChanged(javax.swing.event.ListSelectionEvent e) {
					if(jListSite.getSelectedIndex() == -1) return;
					String site = jListSite.getSelectedValue();
					if(site.length() == 0) return;
					String name = site.substring(0,site.indexOf("{"));
					Vector<?> states = species.getSiteStates(name);
					String start = (String)states.get(0);
					String end = (String)states.get(1);
					String list = (String)states.get(2);
					boolean bool = (Boolean)states.get(3);
					
					if(bool) jRadioBoolean.setSelected(true);
					else { 
						jTextField_listStates.setText(list);
						spinner_upper.setValue(Integer.parseInt(end));
						spinner_lower.setValue(Integer.parseInt(start));
					}
					jTextField_newSite.setText(name);
				}
			});
		}
		return jListSite;
	}
	
	


	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(11, 243, 70, 30));
			jButton1.setMargin(new Insets(3,3,3,3));
			jButton1.setText("Delete site");
			jButton1.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					deleteSite();
				}
			});
		}
		return jButton1;
	}

	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(new Rectangle(291, 6, 100, 26));
			jButton3.setText("Update Model");
			jButton3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					try{
						updateModel();
						
					} catch(Throwable ex) {
						if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
					}
				}
			});
		}
		return jButton3;
	}
	
	private void updateModel() throws Throwable{
		this.species.setName(new String(this.jTextField_species.getText()));
		Vector<String> reactions = new Vector<String>();
		this.species.setType(Constants.SpeciesType.MULTISTATE.copasiType);
		
	
		
		this.parentFrame.updateModel_fromMultiBuilder(this.species, reactions);
		
		dispose();
	}
	
	public void setMultistateSpecies(MultistateSpecies sp) {
		this.species =  sp; //new MultistateSpecies(completeDef);
			
		this.jTextField_species.setText(this.species.getSpeciesName());
		if(jTextField_species.getText().length() >0) {
			this.setTabbedPane_enable(true);
		}
		
		updateJTableConcentrationSpecies();
		refreshListSites();
		
		if(sp.getCompartments().size() == 0) {
			try {
				sp.setCompartment(MainGui.multiModel, MainGui.compartment_default_for_dialog_window);
			} catch (MySyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}
	
	public void clearAll() throws Exception {
		jContentPane = null;
		jTabbedPane = null;
		upper = null;
		jLabel = null;
		jTextField_species = null;
		jPanel = null;
		jLabel1 = null;
		jTextField_newSite = null;
		spinner_lower = null;
		spinner_upper = null;
		jTextField_listStates = null;
		jButton = null;
		//jTableListSite = null;
		jListSite = null;
		jScrollPane = null;
		jLabel2 = null;
		jLabel3 = null;
		jLabel3b = null;
		jButton1 = null;
		jButton3 = null;
		species = new MultistateSpecies(MainGui.multiModel,new String());
		
		initialize();
		updateJTableConcentrationSpecies();
	}


	
	public void updateMultisiteSpeciesConcentrationFromTable(int row) throws Exception {
		int quantityColumn = tableConcentrationSpeciesmodel.getColumnCount()-1;
		String initialQuantity = (String) this.tableConcentrationSpeciesmodel.getValueAt(row, quantityColumn);
		
		String singleConfiguration = this.species.getExpandedSites().get(row);
		singleConfiguration = this.species.getSpeciesName() + "(" + singleConfiguration + ")";
		
		HashMap<String, String> singleEntry = new HashMap<String, String>();
		singleEntry.put(singleConfiguration, initialQuantity);
		this.species.setInitialQuantity(singleEntry);
		
		 this.jTableConcentrationSpecies.revalidate();
	    
	     
	 }
	
	
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(getJPanelConcentrationSpecies(), BorderLayout.CENTER);
		}
		return panel;
	}

	public void selectInitialQuantityTab() {
		jTabbedPane.setSelectedIndex(1);
	}
}  

class CustomFocusTraversalPolicy extends FocusTraversalPolicy {
Vector<Component> order;

	public CustomFocusTraversalPolicy(Vector<Component> order) {
		this.order = new Vector<Component>(order.size());
		this.order.addAll(order);
	}
	
	public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
	{
		int idx = (order.indexOf(aComponent) + 1) % order.size();
		return order.get(idx);
	}
	
	public Component getComponentBefore(Container focusCycleRoot,
	                          Component aComponent)
	{
		int idx = order.indexOf(aComponent) - 1;
		if (idx < 0) {
		idx = order.size() - 1;
		}
		return order.get(idx);
	}
	
	public Component getDefaultComponent(Container focusCycleRoot) {
		return order.get(0);
	}
	
	public Component getLastComponent(Container focusCycleRoot) {
		return order.lastElement();
	}
	
	public Component getFirstComponent(Container focusCycleRoot) {
		return order.get(0);
	}
	
}


/*class FilterHistoryJList extends JList {

    private FilterField filterField;
    private int DEFAULT_FIELD_WIDTH = 20;

    public FilterHistoryJList() {
        super();
        setModel (new FilterModel());
        filterField = new FilterField (DEFAULT_FIELD_WIDTH);
        filterField.textField.requestFocus();
    }

    public void setModel (ListModel m) {
        if (! (m instanceof FilterModel))
            throw new IllegalArgumentException();
        super.setModel (m);
    }

    public void addItem (Object o) {
        ((FilterModel)getModel()).addElement (o);
    }

    public FilterField getFilterField() {
        return filterField;
    }

    // test filter list
    public static void main (String[] args) {
        String[] listItems = {
            "Chris", "Joshua", "Daniel", "Michael",
            "Don", "Kimi", "Kelly", "Keagan"
        };
        JFrame frame = new JFrame ("FilterHistoryJList");
        frame.getContentPane().setLayout (new BorderLayout());
        // populate list
        FilterHistoryJList list = new FilterHistoryJList();
        for (int i=0; i<listItems.length; i++)
            list.addItem (listItems[i]);
        // add to gui
        JScrollPane pane =
            new JScrollPane (list,
                             ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                             ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        frame.getContentPane().add (pane, BorderLayout.CENTER);
        FilterField filterField = list.getFilterField();
        frame.getContentPane().add (filterField, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
        filterField.textField.requestFocus();
    }

    // inner class to provide filtered model
    class FilterModel extends AbstractListModel {
        ArrayList items;
        ArrayList filterItems;
        public FilterModel() {
            super();
            items = new ArrayList();
            filterItems = new ArrayList();
        }
        public Object getElementAt (int index) {
            if (index < filterItems.size())
                return filterItems.get (index);
            else
                return null;
        }
        public int getSize() {
            return filterItems.size();
        }
        
        public void clear() {
        	items.clear();
        	filterItems.clear();
        	refilter();
        }
        public void addElement (Object o) {
            items.add (o);
            refilter();
        }
        private void refilter() {
            filterItems.clear();
            String term = getFilterField().textField.getText();
            for (int i=0; i<items.size(); i++)
                if (items.get(i).toString().indexOf(term, 0) != -1)
                    filterItems.add (items.get(i));
            fireContentsChanged (this, 0, getSize());
        }
    }

    // inner class provides filter-by-keystroke field
    class FilterField extends JComponent
        implements DocumentListener, ActionListener {
        LinkedList prevSearches;
        JTextField textField;
        JButton prevSearchButton;
        JPopupMenu prevSearchMenu;
        
        public void setText(String s) {
        	this.textField.setText(s);
        }
        
        public FilterField (int width) {
            super();
            setLayout(new BorderLayout());
            textField = new JTextField (width);
            textField.getDocument().addDocumentListener (this);
            textField.addActionListener (this);
            prevSearchButton = new JButton (new ImageIcon ("mag-glass.png"));
            prevSearchButton.setBorder(null);
            prevSearchButton.addMouseListener (new MouseAdapter() {
                    public void mousePressed (MouseEvent me) {
                        popMenu (me.getX(), me.getY());
                    }
                });
            add (prevSearchButton, BorderLayout.WEST);
            add (textField, BorderLayout.CENTER);
            prevSearches = new LinkedList ();
        }
        public void popMenu (int x, int y) {
            prevSearchMenu = new JPopupMenu();
            Iterator it = prevSearches.iterator();
            while (it.hasNext())
                prevSearchMenu.add (new PrevSearchAction(it.next().toString()));
            prevSearchMenu.show (prevSearchButton, x, y);
        }
        public void actionPerformed (ActionEvent e) {
            // called on return/enter, adds term to prevSearches
            if (e.getSource() == textField) {
                prevSearches.addFirst (textField.getText());
                if (prevSearches.size() > 10)
                    prevSearches.removeLast();
            }
        }
        public void changedUpdate (DocumentEvent e) {((FilterModel)getModel()).refilter(); }
        public void insertUpdate (DocumentEvent e) {((FilterModel)getModel()).refilter(); }
        public void removeUpdate (DocumentEvent e) {((FilterModel)getModel()).refilter(); }
    }

    class PrevSearchAction extends AbstractAction {
        String term;
        public PrevSearchAction (String s) {
            term = s;
            putValue (Action.NAME, term);
        }
        public String toString() { return term; }
        public void actionPerformed (ActionEvent e) {
            getFilterField().textField.setText (term);
          }
    }
}*/