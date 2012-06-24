package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;


import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import utility.Constants;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.JSlider;
import java.awt.FlowLayout;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class PreferencesFrame extends JDialog {

	
	private static final long serialVersionUID = 1L;
	private JCheckBox jCheckBoxHighlightCellOpenIssues = null;
	private JCheckBox jCheckBoxAutocomplete = null;
	private JCheckBox jCheckBoxDialogWindow = null;
	private JCheckBox jCheckBoxShowAllAvailableFunctions = null;


	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.ButtonGroup buttonGroup2;
	private javax.swing.JCheckBox jCheckBoxPopUpWarnings;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JRadioButton jRadioButton1;
	private javax.swing.JRadioButton jRadioButton2;
	private javax.swing.JRadioButton jRadioButton3;
	private javax.swing.JPanel panelBehavior;
	private JTextField defaultCompName;
	private JTextField defaultGlobalQValue;
	private JTextField defaultSpeciesInitialValue;
	private JTextField defaultCompartmentInitialValue;
	private JButton jButtonOK;
	private JButton jButtonRestoreDefaults;
	private JPanel panelColors;
	private JColorChooser colorchooser;
	private JPanel panelButtons;
	private JPanel contentPanel;
	private JScrollPane scrollPaneColorPalette;
	private JScrollPane scrollPaneBehavior;
	private JPanel colorchooserPreview;
	private JPanel panel_1;
	private JLabel labelMajourIssues;
	private JLabel labelHightlight;
	private JLabel labelDefaults;
	private JLabel currentLabel;
	private JRadioButton radioButton_labelDefaults;
	private JRadioButton radioButton_labelMajorIssues;
	private JRadioButton radioButton_labelHighlight;
	private JPanel panelMain;
	private JPanel panelButtonLeft;
	private JPanel panelRight;
	private JPanel panelDefaults;
	private JLabel lblDefault;
	private JPanel panel_6;
	//private JTextField txtCell;
//	private JTextField textField;
//	private JTextField textField_1;
	private JLabel lblNewLabel_1;
	private JScrollPane scrollPaneButtonLeft;
	private JPanel panel_5;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JPanel panel_7;
	private JButton btnNewButton_2;
	private JButton btnNewButton_3;
	private JScrollPane scrollPaneDefaults;
	private JPanel panel_2;
	private JPanel panelFontSize;
	private JSlider slider;
	private JLabel lblNewLabel_2;
	
	private MainGui gui;
	private JScrollPane scrollPaneDirectories;
	private JPanel panelDirectories;
	private JPanel panelAutosave;
	private JPanel panel_4;
	private JLabel lblDirectoryPath;
	private JTextField textFieldDirectoryAutosave;
	private JButton btnButtonDirectoryAutosave;
	private JPanel panel_8;
	private JPanel panel_11;
	private JLabel lblIWantTo;
	private JSpinner spinner_1;
	private JLabel lblMinutes;
	private JPanel panel_12;
	private JLabel lblDirectoryForBug;
	private JTextField textField_3;
	private JButton btnBrowse;
	private JPanel panel_13;
	private JLabel lblBaseWorkingDirectory;
	private JTextField textField_4;
	private JButton btnBrowse_1;
	private JFileChooser fileChooser;
	private JPanel panel_14;
	private JPanel panel_15;
	private JCheckBox chckbxNewCheckBox;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PreferencesFrame frame = new PreferencesFrame(null);
					//frame.setExpressionAndShow("a+b");
					
					frame.revalidate();
					frame.pack();
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public PreferencesFrame(MainGui gui) {

		this.setTitle("Preferences...");
		this.setSize(new Dimension(453, 341));

		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setSize(new Dimension(456, 358));
		this.setContentPane(contentPanel);

		this.gui = gui;
		initComponents();

		
		//tabbedPane.setSelectedIndex(0);
	}

	private Component getColorChooser() {
		UIManager.put("ColorChooser.swatchesNameText", "Sample colors"); 
		UIManager.put("ColorChooser.hsvNameText", "Hue, Saturation and Value"); 


		colorchooser = new JColorChooser();
		colorchooser.setPreviewPanel(new JPanel());

		// Retrieve the current set of panels
		AbstractColorChooserPanel[] oldPanels = colorchooser.getChooserPanels();
		for (int i=2; i<oldPanels.length; i++) {

			colorchooser.removeChooserPanel(oldPanels[i]);
		}

		colorchooser.getSelectionModel().addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				Color newColor = colorchooser.getColor();
				MainGui.color_border_defaults = newColor;
				//colorchooserPreview.setBackground(newColor);
				if(currentLabel!= null) {
					if(currentLabel.getText().contains("Border")) {
						currentLabel.setBorder(new LineBorder(newColor, 3));
					} else {
						currentLabel.setBackground(newColor);
					}
				}
			}
		});

		currentLabel = null;
		colorchooserPreview = new JPanel();

		MouseListener pickLabelToChange_listener = new MouseListener() {
			public void mouseClicked(MouseEvent e) { 
				if(e.getSource().equals(labelDefaults)) radioButton_labelDefaults.setSelected(true);
				if(e.getSource().equals(labelMajourIssues)) radioButton_labelMajorIssues.setSelected(true);
				if(e.getSource().equals(labelHightlight)) radioButton_labelHighlight.setSelected(true);
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		};

		labelDefaults = new JLabel(" Border cell with Default value ");
		labelDefaults.setBorder(new LineBorder(MainGui.color_border_defaults, 3));
		labelDefaults.addMouseListener(pickLabelToChange_listener);
		colorchooserPreview.setLayout(new GridLayout(0, 3, 0, 0));

		radioButton_labelDefaults = new JRadioButton("");
		radioButton_labelDefaults.setHorizontalAlignment(SwingConstants.CENTER);

		radioButton_labelMajorIssues = new JRadioButton("");
		radioButton_labelMajorIssues.setHorizontalAlignment(SwingConstants.CENTER);

		radioButton_labelHighlight = new JRadioButton("");
		radioButton_labelHighlight.setHorizontalAlignment(SwingConstants.CENTER);
		colorchooserPreview.add(labelDefaults);

		labelMajourIssues = new JLabel(" Background cell with Major Issue ");
		labelMajourIssues.setBackground(MainGui.color_cell_with_errors);


		labelMajourIssues.setOpaque(true);
		labelMajourIssues.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		labelMajourIssues.addMouseListener(pickLabelToChange_listener);
		colorchooserPreview.add(labelMajourIssues);

		labelHightlight = new JLabel(" Background Highlighted cell ");
		labelHightlight.setBackground(MainGui.color_cell_to_highlight);
		labelHightlight.setOpaque(true);
		labelHightlight.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		labelHightlight.addMouseListener(pickLabelToChange_listener);
		colorchooserPreview.add(labelHightlight);
		colorchooser.setPreviewPanel(colorchooserPreview);

		buttonGroup2 = new ButtonGroup();
		buttonGroup2.add(radioButton_labelDefaults);
		buttonGroup2.add(radioButton_labelMajorIssues);
		buttonGroup2.add(radioButton_labelHighlight);

		colorchooserPreview.add(radioButton_labelDefaults);
		colorchooserPreview.add(radioButton_labelMajorIssues);
		colorchooserPreview.add(radioButton_labelHighlight);


		radioButton_labelDefaults.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {		if(radioButton_labelDefaults.isSelected()) currentLabel = labelDefaults;	}
		});
		radioButton_labelHighlight.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {		if(radioButton_labelHighlight.isSelected()) currentLabel = labelHightlight;	}
		});
		radioButton_labelMajorIssues.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {		if(radioButton_labelMajorIssues.isSelected()) currentLabel = labelMajourIssues;	}
		});
		radioButton_labelDefaults.setSelected(false);
		radioButton_labelHighlight.setSelected(false);
		radioButton_labelMajorIssues.setSelected(false);

		return colorchooser;
	}

	private void initComponents() {

		//tabbedPane = new JTabbedPane();
		//tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		//contentPanel.add(tabbedPane, BorderLayout.NORTH);

		panelColors = new JPanel();
		panelColors.setLayout(new BorderLayout(0, 0));
		panelColors.add(getColorChooser());

		//tabbedPane.addTab("Colors", null, panelColors, null);

		panelButtons = new JPanel();
		contentPanel.add(panelButtons, BorderLayout.SOUTH);



		buttonGroup1 = new javax.swing.ButtonGroup();

		/*  // An AutoCompletion acts as a "middle-man" between a text component
	      // and a CompletionProvider. It manages any options associated with
	      // the auto-completion (the popup trigger key, whether to display a
	      // documentation window along with completion choices, etc.). Unlike
	      // CompletionProviders, instances of AutoCompletion cannot be shared
	      // among multiple text components.
	      AutoCompletion ac = new AutoCompletion(Constants.provider);
	      ac.install(defaultCompName);*/

		scrollPaneColorPalette = new JScrollPane();
		scrollPaneColorPalette.setViewportView(panelColors);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "VT palette", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelColors.add(panel_1, BorderLayout.SOUTH);
		
		panelFontSize = new JPanel();
		panelFontSize.setBorder(new TitledBorder(null, "Fonts", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		FlowLayout flowLayout = (FlowLayout) panelFontSize.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		panelColors.add(panelFontSize, BorderLayout.NORTH);
		
		lblNewLabel_2 = new JLabel("Size");
		panelFontSize.add(lblNewLabel_2);
		
		slider = new JSlider();
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(5);
		slider.setMinimum(5);
		slider.setMaximum(40);
		if(gui!= null) slider.setValue(gui.getCustomFont().getSize());
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if(gui!= null) gui.setCustomFont(slider.getValue());
			}
		});
		Hashtable labelTable = new Hashtable();
		for(int i = slider.getMinimum(); i <= slider.getMaximum(); i = i + slider.getMinorTickSpacing() ) {
			labelTable.put( new Integer(i), new JLabel(new Integer(i).toString()) );
		}
		slider.setLabelTable( labelTable );
		
		panelFontSize.add(slider);
		MouseListener pickColorBackground_listener = new MouseListener() {
			public void changeColor(MouseEvent e) {
				colorchooser. getSelectionModel().setSelectedColor(((JComponent)(e.getSource())).getBackground());
			}

			@Override
			public void mouseClicked(MouseEvent e) { changeColor(e);	}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}
		};


		Vector<Color> colors = new Vector<Color>();
		colors.add(Constants.vt_orange);
		colors.add(Constants.vt_maroon);
		colors.add(Constants.vt_red_1);
		colors.add(Constants.vt_red_2);
		colors.add(Constants.vt_red_3);
		colors.add(Constants.vt_red_4);
		colors.add(Constants.vt_gold_1);
		colors.add(Constants.vt_gold_2);
		colors.add(Constants.vt_gold_3);
		colors.add(Constants.vt_gold_4);
		colors.add(Constants.vt_green_1);
		colors.add(Constants.vt_green_2);
		colors.add(Constants.vt_green_3);
		colors.add(Constants.vt_green_4);
		colors.add(Constants.vt_blues_1);
		colors.add(Constants.vt_blues_2);
		colors.add(Constants.vt_blues_3);
		colors.add(Constants.vt_blues_4);
		colors.add(Constants.vt_gray_1);
		colors.add(Constants.vt_gray_2);
		colors.add(Constants.vt_gray_3);
		colors.add(Constants.vt_gray_4);
		colors.add(Constants.vt_cream_1);
		colors.add(Constants.vt_cream_2);
		colors.add(Constants.vt_cream_3);
		colors.add(Constants.vt_cream_4);


		for(int i = 0; i < colors.size(); i++) {
			JLabel lblNewLabel = new JLabel("     ");
			lblNewLabel.setOpaque(true);
			lblNewLabel.setBackground(colors.get(i));
			lblNewLabel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
			lblNewLabel.addMouseListener(pickColorBackground_listener);
			panel_1.add(lblNewLabel);
		}






		jButtonRestoreDefaults = new JButton("Restore original Defaults");
		panelButtons.add(jButtonRestoreDefaults);
		jButtonOK = new JButton("OK");
		panelButtons.add(jButtonOK);
		jButtonOK.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					MainGui.updateFunctionsView();
					Vector usage = MainGui.search(defaultCompName.getText());
					MainGui.revalidateExpressions(usage);
					usage = MainGui.search(defaultGlobalQValue.getText());
					MainGui.revalidateExpressions(usage);
					usage = MainGui.search(defaultSpeciesInitialValue.getText());
					MainGui.revalidateExpressions(usage);
					usage = MainGui.search(defaultCompartmentInitialValue.getText());
					MainGui.revalidateExpressions(usage);
					MainGui.color_border_defaults = ((LineBorder)(labelDefaults.getBorder())).getLineColor();
					MainGui.color_cell_to_highlight = labelHightlight.getBackground();
					MainGui.color_cell_with_errors = labelMajourIssues.getBackground();
				} catch (Exception e1) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
				}
				savePreferencesToFile();

				setVisible(false);
			}


		});

		jButtonRestoreDefaults.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defaultGlobalQValue.setText(Constants.DEFAULT_GLOBALQ_INITIAL_VALUE);
				defaultCompName.setText(Constants.DEFAULT_COMPARTMENT_NAME);
				defaultSpeciesInitialValue.setText(Constants.DEFAULT_SPECIES_INITIAL_VALUE);
				defaultCompartmentInitialValue.setText(Constants.DEFAULT_COMPARTMENT_INITIAL_VALUE);
				labelHightlight.setBackground(Constants.DEFAULT_COLOR_HIGHLIGHT);
				labelDefaults.setBorder(new LineBorder(Constants.DEFAULT_COLOR_DEFAULTS, 3));
				labelMajourIssues.setBackground(Constants.DEFAULT_COLOR_ERRORS);
				panelBehavior.revalidate();
				colorchooserPreview.revalidate();
			}
		});
		contentPanel.add(panelButtons, BorderLayout.SOUTH);
		
		scrollPaneDefaults = new JScrollPane();
		panelDefaults = new JPanel();
		scrollPaneDefaults.setViewportView(panelDefaults);
		panelDefaults.setLayout(new BorderLayout(0, 0));

		panel_2 = new JPanel();
		panelDefaults.add(panel_2, BorderLayout.NORTH);
		BoxLayout b = new BoxLayout(panel_2, BoxLayout.X_AXIS);
		panel_2.setLayout(b);
		
		lblDefault = new JLabel("Default");
		panel_2.add(Box.createHorizontalStrut(10));
		panel_2.add(lblDefault);
		panel_2.add(Box.createHorizontalStrut(10));
		//panelButtons.add(scrollPaneDefaults);
		defaultCompName = new JTextField(MainGui.compartment_default_for_dialog_window);

		defaultCompName.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void removeUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void insertUpdate(DocumentEvent e) {
				updateDefault();
			}

			public void updateDefault() {
				String s = defaultCompName.getText();
				MainGui.compartment_default_for_dialog_window = s;
		
				//MOOOOOOOOOOOOOOOOOOOOOOOOVE THOSE COMMANDS FOR WHEN WHEN THE USER CLICK OK!!!
				MainGui.updateDefaultValue(Constants.TitlesTabs.SPECIES.description, Constants.SpeciesColumns.COMPARTMENT.index, MainGui.compartment_default_for_dialog_window);
				MainGui.updateDefaultValue(Constants.TitlesTabs.COMPARTMENTS.description, Constants.CompartmentsColumns.NAME.index, MainGui.compartment_default_for_dialog_window);
			}
		});

		defaultGlobalQValue = new JTextField(MainGui.globalQ_defaultValue_for_dialog_window);
		defaultGlobalQValue.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void removeUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void insertUpdate(DocumentEvent e) {
				updateDefault();
			}

			public void updateDefault() {
				MainGui.globalQ_defaultValue_for_dialog_window = defaultGlobalQValue.getText();
				MainGui.updateDefaultValue(Constants.TitlesTabs.GLOBALQ.description, Constants.GlobalQColumns.VALUE.index, MainGui.globalQ_defaultValue_for_dialog_window);
			}
		});

		defaultSpeciesInitialValue = new JTextField(MainGui.species_defaultInitialValue);
		defaultSpeciesInitialValue.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void removeUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void insertUpdate(DocumentEvent e) {
				updateDefault();
			}

			public void updateDefault() {
				new String(defaultSpeciesInitialValue.getText());
				MainGui.species_defaultInitialValue = defaultSpeciesInitialValue.getText

						();
				MainGui.updateDefaultValue(Constants.TitlesTabs.SPECIES.description, 

						Constants.SpeciesColumns.INITIAL_QUANTITY.index, MainGui.species_defaultInitialValue);
			}
		});


		defaultCompartmentInitialValue = new JTextField(MainGui.compartment_defaultInitialValue);
		defaultCompartmentInitialValue.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void removeUpdate(DocumentEvent e) {
				updateDefault();
			}
			public void insertUpdate(DocumentEvent e) {
				updateDefault();
			}

			public void updateDefault() {
				MainGui.compartment_defaultInitialValue = 

						defaultCompartmentInitialValue.getText();
				MainGui.updateDefaultValue(Constants.TitlesTabs.COMPARTMENTS.description, 

						Constants.CompartmentsColumns.INITIAL_SIZE.index, MainGui.compartment_defaultInitialValue);
			}
		});
		
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		panel_6 = new JPanel();
		panel_2.add(panel_6);
		GridLayout gr = new GridLayout(0, 2, 0, 0);
		gr.setVgap(10);
		gr.setHgap(2);
		panel_6.setLayout(gr);

		JLabel jLabel3 = new JLabel("Species initial value:");
		panel_6.add(jLabel3);

		//defaultSpeciesInitialValue = new JTextField();
		panel_6.add(defaultSpeciesInitialValue);
		defaultSpeciesInitialValue.setColumns(10);

		JLabel jLabel2 = new JLabel("Global Quantity initial value:");
		panel_6.add(jLabel2);

		//textField = new JTextField();
		panel_6.add(defaultGlobalQValue);
		defaultGlobalQValue.setColumns(10);

		JLabel lblCompartmentName = new JLabel("Compartment name:");
		panel_6.add(lblCompartmentName);

		//textField_1 = new JTextField();
		panel_6.add(defaultCompName);
		defaultCompName.setColumns(10);

		lblNewLabel_1 = new JLabel("Compartment size:");
		panel_6.add(lblNewLabel_1);

		//	textField_2 = new JTextField();
		panel_6.add(defaultCompartmentInitialValue);
		defaultCompartmentInitialValue.setColumns(10);
		//tabbedPane.addTab("Colors", null, scrollPaneColorPalette, null);

		panelMain = new JPanel();
		contentPanel.add(panelMain, BorderLayout.CENTER);
		panelMain.setBackground(Color.RED);
		panelMain.setLayout(new BorderLayout(0, 0));

		panelButtonLeft = new JPanel();
		panelButtonLeft.setBackground(Color.MAGENTA);
		panelMain.add(panelButtonLeft, BorderLayout.WEST);
		panelButtonLeft.setLayout(new BorderLayout(0, 0));

		scrollPaneButtonLeft = new JScrollPane();
		panelButtonLeft.add(scrollPaneButtonLeft);

		panel_5 = new JPanel();
		scrollPaneButtonLeft.setViewportView(panel_5);
		panel_5.setLayout(new GridLayout(0, 1, 0, 0));

		btnNewButton = new JButton("General behavior");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRight.removeAll();
				panelRight.add(scrollPaneBehavior, BorderLayout.CENTER);
				panelRight.revalidate();
				Component c = panelRight.getParent();
				while(true) {
					if(c instanceof JDialog) break;
					c = c.getParent();
				}
				((JDialog)c).pack();
				panelRight.repaint();
			
			}
		});
		panel_5.add(btnNewButton);

		btnNewButton_1 = new JButton("Default values");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRight.removeAll();
				panelRight.add(scrollPaneDefaults, BorderLayout.CENTER);
				panelRight.revalidate();
				Component c = panelRight.getParent();
				while(true) {
					if(c instanceof JDialog) break;
					c = c.getParent();
				}
				((JDialog)c).pack();
			}
		});
		panel_5.add(btnNewButton_1);
		
		btnNewButton_3 = new JButton("Autosave");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRight.removeAll();
				panelRight.add(scrollPaneDirectories, BorderLayout.CENTER);
				panelRight.revalidate();
				Component c = panelRight.getParent();
				while(true) {
					if(c instanceof JDialog) break;
					c = c.getParent();
				}
				((JDialog)c).pack();
			}
		});
		panel_5.add(btnNewButton_3);
		
		btnNewButton_2 = new JButton("Fonts and Colors");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelRight.removeAll();
				panelRight.add(scrollPaneColorPalette, BorderLayout.CENTER);
				panelRight.revalidate();
				Component c = panelRight.getParent();
				while(true) {
					if(c instanceof JDialog) break;
					c = c.getParent();
				}
				((JDialog)c).pack();

			}
		});
		panel_5.add(btnNewButton_2);
		
		scrollPaneDirectories = new JScrollPane();
		
		panelDirectories = new JPanel();
		scrollPaneDirectories.setViewportView(panelDirectories);
		panelDirectories.setLayout(new BorderLayout(0, 0));
		
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Workspace options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		//panelDirectories.add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		panel_13 = new JPanel();
		FlowLayout flowLayout_4 = (FlowLayout) panel_13.getLayout();
		flowLayout_4.setAlignment(FlowLayout.LEFT);
		panel_4.add(panel_13);
		
		lblBaseWorkingDirectory = new JLabel("Base working directory");
		panel_13.add(lblBaseWorkingDirectory);
		
		textField_4 = new JTextField();
		textField_4.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateAutosavePath();
			}
			public void removeUpdate(DocumentEvent e) {
				updateAutosavePath();
			}
			public void insertUpdate(DocumentEvent e) {
				updateAutosavePath();
			
			}
			private void updateAutosavePath() {
				gui.setAutosaveDirectory(textField_4.getText());
			}

			
		});
		panel_13.add(textField_4);
		textField_4.setColumns(10);
		
	
		
		btnBrowse_1 = new JButton("Browse...");
		btnBrowse_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 int returnVal = fileChooser.showOpenDialog(null);
                 if (returnVal == JFileChooser.APPROVE_OPTION) {
                     File file = fileChooser.getSelectedFile();
                     textField_4.setText(file.getAbsolutePath());
                 }
				
			}
		});
		panel_13.add(btnBrowse_1);
		
		panel_12 = new JPanel();
		FlowLayout flowLayout_5 = (FlowLayout) panel_12.getLayout();
		flowLayout_5.setAlignment(FlowLayout.LEFT);
		panel_4.add(panel_12);
		
		lblDirectoryForBug = new JLabel("Directory for Bug report");
		panel_12.add(lblDirectoryForBug);
		
		textField_3 = new JTextField();
		panel_12.add(textField_3);
		textField_3.setColumns(10);
		
		btnBrowse = new JButton("Browse...");
		panel_12.add(btnBrowse);
		
		panel_7 = new JPanel();
		panelDirectories.add(panel_7, BorderLayout.CENTER);
		
		panel_14 = new JPanel();
		panel_7.add(panel_14);
		panel_14.setLayout(new BorderLayout(0, 0));
		
		panelAutosave = new JPanel();
		panel_14.add(panelAutosave);
		panelAutosave.setBorder(new TitledBorder(null, "Autosave", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelAutosave.setLayout(new GridLayout(0, 1, 0, 5));
		
		panel_8 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_8.getLayout();
		flowLayout_2.setAlignment(FlowLayout.LEFT);
		panelAutosave.add(panel_8);
		
		lblDirectoryPath = new JLabel("Directory path:");
		panel_8.add(lblDirectoryPath);
		
		textFieldDirectoryAutosave = new JTextField();
		textFieldDirectoryAutosave.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateAutosavePath();
			}
			public void removeUpdate(DocumentEvent e) {
				updateAutosavePath();
			}
			public void insertUpdate(DocumentEvent e) {
				updateAutosavePath();
			
			}
			private void updateAutosavePath() {
				gui.setAutosaveDirectory(textFieldDirectoryAutosave.getText());
			}

			
		});
		panel_8.add(textFieldDirectoryAutosave);
		textFieldDirectoryAutosave.setColumns(20);
		
		btnButtonDirectoryAutosave = new JButton("Browse...");
		btnButtonDirectoryAutosave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 int returnVal = fileChooser.showOpenDialog(null);
                 if (returnVal == JFileChooser.APPROVE_OPTION) {
                     File file = fileChooser.getSelectedFile();
                     textFieldDirectoryAutosave.setText(file.getAbsolutePath());
                 }
			}
		});
		panel_8.add(btnButtonDirectoryAutosave);
		
		panel_11 = new JPanel();
		panelAutosave.add(panel_11);
		
		lblIWantTo = new JLabel("I want to save every");
		panel_11.add(lblIWantTo);
		
		spinner_1 = new JSpinner();
		spinner_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				gui.setAutosaveTime((Integer)spinner_1.getValue());
			}
		});
		spinner_1.setModel(new SpinnerNumberModel(5, 1, 30, 1));
		panel_11.add(spinner_1);
		
		lblMinutes = new JLabel("minutes.");
		panel_11.add(lblMinutes);
		
		panel_15 = new JPanel();
		FlowLayout flowLayout_6 = (FlowLayout) panel_15.getLayout();
		flowLayout_6.setAlignment(FlowLayout.LEFT);
		panel_14.add(panel_15, BorderLayout.NORTH);
		
		chckbxNewCheckBox = new JCheckBox("Activate Autosave");
		chckbxNewCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean sel = chckbxNewCheckBox.isSelected();
				panelAutosave.setEnabled(sel);
				textFieldDirectoryAutosave.setEnabled(sel);
				btnButtonDirectoryAutosave.setEnabled(sel);
				lblDirectoryPath.setEnabled(sel);
				lblMinutes.setEnabled(sel);
				lblIWantTo.setEnabled(sel);
				spinner_1.setEnabled(sel);
				
				panelAutosave.revalidate();
				panelAutosave.repaint();
			}
		});
		chckbxNewCheckBox.setSelected(true);
		panel_15.add(chckbxNewCheckBox);

		panelRight = new JPanel();
		panelRight.setBackground(Color.YELLOW);
		panelMain.add(panelRight, BorderLayout.CENTER);

		panelBehavior = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jCheckBoxAutocomplete = new javax.swing.JCheckBox();
		jCheckBoxDialogWindow = new javax.swing.JCheckBox();
		jCheckBoxHighlightCellOpenIssues = new javax.swing.JCheckBox();
		jCheckBoxPopUpWarnings = new javax.swing.JCheckBox();
		jCheckBoxShowAllAvailableFunctions = new javax.swing.JCheckBox();
		jPanel2 = new javax.swing.JPanel();
		jRadioButton1 = new javax.swing.JRadioButton();
		jRadioButton2 = new javax.swing.JRadioButton();
		jRadioButton3 = new javax.swing.JRadioButton();

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Customize software behavior", 

				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N


		jCheckBoxAutocomplete.setText("Autocomplete related spreadsheets and cells");
		jCheckBoxAutocomplete.setName("jCheckBoxAutocomplete");

		jCheckBoxDialogWindow.setText("Show pop-up when Species are defined by the tool"); 
		jCheckBoxDialogWindow.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				jCheckBox2StateChanged(evt);
			}
		});

		jCheckBoxShowAllAvailableFunctions.setText("Show all available functions"); 
		// jCheckBoxShowAllAvailableFunctions.setEnabled(false);
		jCheckBoxShowAllAvailableFunctions.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					updateStatusAllAvailableFunctions();
				} catch (Exception e1) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
				}

			}
		});
		jCheckBoxAutocomplete.setSelected(MainGui.autocompleteWithDefaults);
		jCheckBoxAutocomplete.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				updateStatusAutocomplete();
			}
		});

		jCheckBoxDialogWindow.setSelected(MainGui.show_defaults_dialog_window);
		jCheckBoxDialogWindow.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent e) {
				updateStatusDialogWindow();
			}
		});
		jCheckBoxShowAllAvailableFunctions.setSelected(true);

		jCheckBoxHighlightCellOpenIssues.setText("Highlight all cells with open issues");
		jCheckBoxHighlightCellOpenIssues.setEnabled(false);
		jCheckBoxPopUpWarnings.setText("Show pop-up windows for warning messages");
		jCheckBoxPopUpWarnings.setEnabled(false);
		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jCheckBoxAutocomplete)
								.addComponent(jCheckBoxDialogWindow)
								.addGroup(jPanel1Layout.createSequentialGroup()
												)
																																.addComponent(jCheckBoxPopUpWarnings)
																						.addComponent(jCheckBoxHighlightCellOpenIssues)
																						.addComponent(jCheckBoxShowAllAvailableFunctions))
																						.addContainerGap(6, Short.MAX_VALUE))
				);
		jPanel1Layout.setVerticalGroup(
				jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup()
						.addComponent(jCheckBoxAutocomplete)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jCheckBoxDialogWindow)
						.addGap(7, 7, 7)
						
																				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																				.addComponent(jCheckBoxPopUpWarnings)
																				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																				.addComponent(jCheckBoxHighlightCellOpenIssues)
																				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																				.addComponent(jCheckBoxShowAllAvailableFunctions)
																				.addContainerGap(6, Short.MAX_VALUE))
				);

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Renamig options", 

				javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION)); // NOI18N
		jPanel2.setName("jPanel2"); // NOI18N

		buttonGroup1.add(jRadioButton1);


		jRadioButton1.setText(Constants.RENAMING_OPTION_ALL_STRING); 

		buttonGroup1.add(jRadioButton2);
		jRadioButton2.setText(Constants.RENAMING_OPTION_CUSTOM_STRING); 
		buttonGroup1.add(jRadioButton3);
		jRadioButton3.setText(Constants.RENAMING_OPTION_NONE_STRING); 


		jRadioButton1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				renamingOptionChanged(Constants.RENAMING_OPTION_ALL);
			}
		});

		jRadioButton2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				renamingOptionChanged(Constants.RENAMING_OPTION_CUSTOM);
			}
		});

		jRadioButton3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				renamingOptionChanged(Constants.RENAMING_OPTION_NONE);
			}
		});
		panelRight.setLayout(new BorderLayout(0, 0));




		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jRadioButton1)
								.addComponent(jRadioButton2)
								.addComponent(jRadioButton3)
								)
								.addContainerGap())
				);
		jPanel2Layout.setVerticalGroup(
				jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addComponent(jRadioButton1)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jRadioButton2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jRadioButton3)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addContainerGap(25, Short.MAX_VALUE))
				);
		//tabbedPane.addTab("General", mainPanel);
		scrollPaneBehavior = new JScrollPane();
		panelRight.add(scrollPaneDirectories, BorderLayout.CENTER);
		scrollPaneBehavior.setViewportView(panelBehavior);

		panelBehavior.setLayout(new BorderLayout(0, 0));
		panelBehavior.add(jPanel1, BorderLayout.CENTER);
		panelBehavior.add(jPanel2, BorderLayout.SOUTH);
		switch (MainGui.renamingOption) {
		case Constants.RENAMING_OPTION_ALL:
			jRadioButton1.setSelected(true);
			break;
		case Constants.RENAMING_OPTION_CUSTOM:
			jRadioButton2.setSelected(true);
			break;
		case Constants.RENAMING_OPTION_NONE:
			jRadioButton3.setSelected(true);
			break;
		default:
			break;
		}
		this.setModal(true);
		this.pack();
		this.setLocationRelativeTo(null);
	}
	

	protected void renamingOptionChanged(int i) {
		MainGui.renamingOption = i;

	}

	private void jCheckBox2StateChanged(javax.swing.event.ChangeEvent evt) {                                        
		// TODO add your handling code here:
	}                                       



	public void updateStatusAutocomplete() {
		if(jCheckBoxAutocomplete.isSelected()) MainGui.autocompleteWithDefaults = true;
		else {
			MainGui.autocompleteWithDefaults = false;
			this.jCheckBoxDialogWindow.setSelected(false);
		}
	}


	public void updateStatusAllAvailableFunctions() throws Exception {
		if(jCheckBoxShowAllAvailableFunctions.isSelected()) MainGui.showAllAvailableFunctions = true;
		else {
			MainGui.showAllAvailableFunctions = false;
		}
		MainGui.updateFunctionsView();

	}

	public void updateStatusDialogWindow() {
		if(jCheckBoxDialogWindow.isSelected()) {
			this.jCheckBoxAutocomplete.setSelected(true);
			MainGui.show_defaults_dialog_window = true;
		}
		else MainGui.show_defaults_dialog_window = false;
	}

	public void setCheckboxDialogWindowForDefaults(boolean b) {
		jCheckBoxDialogWindow.setSelected(b);
	}


	public void extractPreferences(Vector<String> pref) {
		for(int i = 0; i < pref.size(); i++) {
			String element = pref.get(i);
			StringTokenizer st = new StringTokenizer(element, Constants.Preferences.SEPARATOR.description);
			String name = st.nextToken();
			String value = st.nextToken();

			if(name.compareTo(Constants.Preferences.AUTOCOMPLETE.description)==0) {
				if(value.compareTo(Constants.Preferences.CHECKED.description)==0) jCheckBoxAutocomplete.setSelected(true);
				else jCheckBoxAutocomplete.setSelected(false);
				continue;
			} 


			if(name.compareTo(Constants.Preferences.SHOW_ALL_FUNCTIONS.description)==0) {
				if(value.compareTo(Constants.Preferences.CHECKED.description)==0) jCheckBoxShowAllAvailableFunctions.setSelected(true);
				else jCheckBoxShowAllAvailableFunctions.setSelected(false);
				continue;
			} 


			if(name.compareTo(Constants.Preferences.COMP_NAME.description)==0) {
				defaultCompName.setText(value);
				continue;
			} 
			if(name.compareTo(Constants.Preferences.INITIAL_COMP_SIZE.description)==0) {
				defaultCompartmentInitialValue.setText(value);
				continue;
			}
			if(name.compareTo(Constants.Preferences.INITIAL_GLOBALQ_VALUE.description)==0) {
				defaultGlobalQValue.setText(value);
				continue;
			}
			if(name.compareTo(Constants.Preferences.INITIAL_SPECIES_VALUE.description)==0) {
				defaultSpeciesInitialValue.setText(value);
				continue;
			}
			if(name.compareTo(Constants.Preferences.POPUP_AUTOCOMPLETE.description)==0) {
				if(value.compareTo(Constants.Preferences.CHECKED.description)==0) jCheckBoxDialogWindow.setSelected(true);
				else jCheckBoxDialogWindow.setSelected(false);
				continue;
			} 
			if(name.compareTo(Constants.Preferences.RENAMING.description)==0) {
				if(value.compareTo(Constants.Preferences.RENAME_AUTO.description)==0) jRadioButton1.setSelected(true);
				else if(value.compareTo(Constants.Preferences.RENAME_CUSTOM.description)==0) jRadioButton2.setSelected(true);
				else if(value.compareTo(Constants.Preferences.RENAME_NONE.description)==0) jRadioButton3.setSelected(true);
				continue;
			} 

			if(name.compareTo(Constants.Preferences.COLOR_DEFAULTS.description)==0) {
				labelDefaults.setBorder(new LineBorder(new Color(Integer.parseInt(value),true), 3));
				MainGui.color_border_defaults = new Color(Integer.parseInt(value),true);
				continue;
			} 

			if(name.compareTo(Constants.Preferences.COLOR_HIGHLIGHT.description)==0) {
				labelHightlight.setBackground(new Color(Integer.parseInt(value),true));
				MainGui.color_cell_to_highlight = new Color(Integer.parseInt(value),true);
				continue;
			} 

			if(name.compareTo(Constants.Preferences.COLOR_MAJOR.description)==0) {
				labelMajourIssues.setBackground(new Color(Integer.parseInt(value),true));
				MainGui.color_cell_with_errors = new Color(Integer.parseInt(value),true);
				continue;
			} 
		}
	}

	private void savePreferencesToFile() {
		BufferedWriter out; 
		try {
			out = new BufferedWriter(new FileWriter(MainGui.file_preferences));

			out.write(Constants.Preferences.AUTOCOMPLETE.description+Constants.Preferences.SEPARATOR.description);
			if(jCheckBoxAutocomplete.isSelected()) out.write(Constants.Preferences.CHECKED.description);
			else out.write(Constants.Preferences.UNCHECKED.description);
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.COMP_NAME.description+Constants.Preferences.SEPARATOR.description);
			out.write(defaultCompName.getText());
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.INITIAL_COMP_SIZE.description+Constants.Preferences.SEPARATOR.description);
			out.write(defaultCompartmentInitialValue.getText());
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.INITIAL_GLOBALQ_VALUE.description+Constants.Preferences.SEPARATOR.description);
			out.write(defaultGlobalQValue.getText());
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.INITIAL_SPECIES_VALUE.description+Constants.Preferences.SEPARATOR.description);
			out.write(defaultSpeciesInitialValue.getText());
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.POPUP_AUTOCOMPLETE.description+Constants.Preferences.SEPARATOR.description);
			if(jCheckBoxDialogWindow.isSelected()) out.write(Constants.Preferences.CHECKED.description);
			else out.write(Constants.Preferences.UNCHECKED.description);
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.SHOW_ALL_FUNCTIONS.description+Constants.Preferences.SEPARATOR.description);
			if(jCheckBoxShowAllAvailableFunctions.isSelected()) out.write(Constants.Preferences.CHECKED.description);
			else out.write(Constants.Preferences.UNCHECKED.description);
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.RENAMING.description+Constants.Preferences.SEPARATOR.description);
			if(jRadioButton1.isSelected())	out.write(Constants.Preferences.RENAME_AUTO.description);
			else if(jRadioButton2.isSelected())	out.write(Constants.Preferences.RENAME_CUSTOM.description);
			else if(jRadioButton3.isSelected())	out.write(Constants.Preferences.RENAME_NONE.description);
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.COLOR_DEFAULTS.description+Constants.Preferences.SEPARATOR.description);
			out.write(new Integer(MainGui.color_border_defaults.getRGB()).toString());
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.COLOR_MAJOR.description+Constants.Preferences.SEPARATOR.description);
			out.write(new Integer(MainGui.color_cell_with_errors.getRGB()).toString());
			out.write(System.getProperty("line.separator"));

			out.write(Constants.Preferences.COLOR_HIGHLIGHT.description+Constants.Preferences.SEPARATOR.description);
			out.write(new Integer(MainGui.color_cell_to_highlight.getRGB()).toString());
			out.write(System.getProperty("line.separator"));

			out.flush();
			out.close();
		}
		catch (Exception e) {
			System.err.println("Trouble writing Preferences File: "+ e);
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
	}
}

