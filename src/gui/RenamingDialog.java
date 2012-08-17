package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import model.MultiModel;
import model.MultistateSpecies;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.LayoutStyle.ComponentPlacement;

import utility.Constants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RenamingDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JLabel btnNewButton_1;
	private JLabel lblASpeciesWith;
	private JTextPane txtpnIfADifferent;
	private JLabel lblOr;
	private JPanel panel;
	private JPanel panel_2;
	String clashingName = new String();
	String speciesRow = new String();
	String textIfADifferent = new String();
	String completeClashingMultistate = new String();
	
	String returnString = new String();
	//private String speciesOldName;
	static MultiModel multiModel = null;
	public String getReturnString() {
		return returnString;
	}

	/**
	 * @wbp.parser.constructor
	 */
	public RenamingDialog(MultiModel m, String clashingName, String speciesRow, String speciesOldName, int actionsType) {
		multiModel=m;
		this.clashingName = clashingName;
		this.speciesRow = speciesRow;
	//	this.speciesOldName = speciesOldName;
		this.textIfADifferent = "If a different name is NOT provided, the current species \r"+speciesRow+"\r\nwill be DELETED and " +
				"all the references to "+speciesOldName+" will be redirected to "+clashingName+".";
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Existing name");
		setBounds(100, 100, 454, 221);
		Icon icon = UIManager.getIcon("OptionPane.warningIcon");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		{
			lblOr = new JLabel((String) null);
		}
		contentPanel.setLayout(new BorderLayout(10, 0));
		contentPanel.add(lblOr);
		{
			panel = new JPanel();
			contentPanel.add(panel);
			btnNewButton_1 = new JLabel(icon);
			{
				lblASpeciesWith = new JLabel("A Species with that name already exists. Provide a New Name for the Species.");
				lblASpeciesWith.setHorizontalAlignment(SwingConstants.LEFT);
			}
			GroupLayout gl_panel = new GroupLayout(panel);
			gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnNewButton_1)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(lblASpeciesWith, GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
						.addGap(11))
			);
			gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
							.addComponent(btnNewButton_1)
							.addComponent(lblASpeciesWith, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
						.addGap(0, 0, Short.MAX_VALUE))
			);
			panel.setLayout(gl_panel);
		}
	
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			
			if(actionsType == Constants.DELETE_SPECIES_AND_REDIRECT)
			{
				
				{
					JButton btnNewButton = new JButton("Merge species\n");
					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							deleteSpeciesAndRedirect();
						}
					});
					buttonPane.add(btnNewButton);
				}
			}
			else if(actionsType == Constants.MERGE_SPECIES)
			{
				
				JButton btnNewButton = new JButton("Merge species\n");
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						mergeSpecies();
					}
				});
				buttonPane.add(btnNewButton);
			}
			{
				String cancelButtonText = new String();
				if(actionsType == Constants.MERGE_SPECIES || actionsType == Constants.DELETE_SPECIES_AND_REDIRECT) {
					cancelButtonText = "Go back and provide New Name";
				}
				else cancelButtonText = "OK";
				JButton cancelButton = new JButton(cancelButtonText);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelOption();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.CENTER);
		{
			txtpnIfADifferent = new JTextPane();
			txtpnIfADifferent.setBackground(SystemColor.menu);
			txtpnIfADifferent.setEditable(false);
				txtpnIfADifferent.setText(textIfADifferent);
		}
		if(actionsType == Constants.MERGE_SPECIES || actionsType == Constants.DELETE_SPECIES_AND_REDIRECT)
		{
			GroupLayout gl_panel_2 = new GroupLayout(panel_2);
			gl_panel_2.setHorizontalGroup(
				gl_panel_2.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel_2.createSequentialGroup()
						.addContainerGap()
						.addComponent(txtpnIfADifferent, GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
						.addContainerGap())
			);
			gl_panel_2.setVerticalGroup(
				gl_panel_2.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel_2.createSequentialGroup()
						.addComponent(txtpnIfADifferent, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(18, Short.MAX_VALUE))
			);
			panel_2.setLayout(gl_panel_2);
		}
		this.setLocationRelativeTo(null);
		pack();
	}
	
	public RenamingDialog(String name) {
		//reduced renaming dialog used when I try to create a species with the same name as an existing one (i.e. I don't need to delete o redirect the references)
		this(multiModel,name, new String(),"", Constants.DUPLICATE_SPECIES_NAME);
		this.textIfADifferent = new String();
		txtpnIfADifferent.setText(textIfADifferent);
		pack();
	}

	public RenamingDialog(String name, String completeClashingMultistate, String existingSpecies) {
		//reduced renaming dialog used when I try to create a multistatespecies with the same name as an existing one (i.e. I need to ask if the user wants to use a fresh name or merge the states)
		this(multiModel,name, new String() ,"",Constants.MERGE_SPECIES);
		this.completeClashingMultistate = completeClashingMultistate;
		this.textIfADifferent =  "If a different name is NOT provided, the new species \r"+completeClashingMultistate+"\r\nwill be MERGED with "+existingSpecies;
		txtpnIfADifferent.setText(textIfADifferent);
		pack();
	}
	
	protected void cancelOption() {
		returnString = null;
		setVisible(false);
	}
	protected void deleteSpeciesAndRedirect() {
		returnString = "TO_BE_DELETED";
		setVisible(false);
	}
	
	protected void mergeSpecies() {
		returnString = "MERGED_SPECIES";
		setVisible(false);
	}
	
	protected void freshNameMultistateOption() {
		MultistateSpecies ms = null;
		try {
			ms = new MultistateSpecies(multiModel,completeClashingMultistate);
		} catch (Exception e) {
			if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
		}
		ms.setName(textField.getText().trim());
		returnString = ms.printCompleteDefinition();
		if(completeClashingMultistate.compareTo(returnString)==0) {
			JOptionPane.showMessageDialog(this, "The new name should be different from the original one!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		setVisible(false);
		
	}
	
	protected void freshNameOption() {
		
		returnString = textField.getText().trim();
		if(clashingName.compareTo(returnString)==0) {
			JOptionPane.showMessageDialog(this, "The new name should be different from the original one!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		setVisible(false);
		
	}

}
