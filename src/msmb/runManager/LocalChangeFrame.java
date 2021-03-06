package msmb.runManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JRadioButton;

import java.awt.GridLayout;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JComboBox;

import msmb.runManager.RunManager.NotesLabels;
import msmb.utility.GraphicalProperties;

import org.apache.commons.lang3.tuple.MutablePair;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class LocalChangeFrame extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox comboBoxParents;
	private JRadioButton radioButtonPickValueFromParent;
	private JRadioButton radioButtonNewExpression;
	private JTextField expressionTextPane;
	private JComboBox expressionComboBox;
	private String newValue ;
	private ExitOption exitOption;
	private JRadioButton radioButtonBaseSet;
	private JPanel panel;
	private boolean withComboBox = false;
	private JDialog parentDialog;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LocalChangeFrame dialog = new LocalChangeFrame(null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MutablePair<Integer, String> initializeAndShowWithComboBox(Vector<String> parents, String currentValue, String changesFrom_noteLabel, Vector<String> elementsForCombo) {
			panel.add(expressionComboBox, BorderLayout.CENTER);
			expressionComboBox.removeAllItems();
			for(int i = 0; i < elementsForCombo.size(); ++i) {
				expressionComboBox.addItem(elementsForCombo.get(i));
			}
			
			withComboBox  = true;
			return initializeAndShow(parents, currentValue, changesFrom_noteLabel);
	}
	
	
	public MutablePair<Integer, String> initializeAndShow(Vector<String> parents, String currentValue, String changesFrom_noteLabel) {
		comboBoxParents.removeAllItems();
		int selected = 0;
		Collections.sort(parents);
		for (int i = 0; i < parents.size();++i) {
			String p = parents.get(i);
			if(p.startsWith(changesFrom_noteLabel.substring(changesFrom_noteLabel.indexOf("@")+1)+" (")) {
				selected = i;
			}
			comboBoxParents.addItem(p);
		}

		if(parents.size() == 0) {
			radioButtonPickValueFromParent.setEnabled(false);
			comboBoxParents.setEnabled(false);
		} else {
			radioButtonPickValueFromParent.setEnabled(true);
			comboBoxParents.setEnabled(true);
		}
		
		NotesLabels which = RunManager.NotesLabels.getTypeFromDescription(changesFrom_noteLabel);
		
		switch (which) {
		case FROM_ANCESTOR: 
			radioButtonPickValueFromParent.setSelected(true);
			comboBoxParents.setSelectedIndex(selected);
			break;
		case FROM_BASESET:
			radioButtonBaseSet.setSelected(true);
			break;
		case LOCAL:
			radioButtonNewExpression.setSelected(true);
			expressionTextPane.setText(currentValue);
			if(withComboBox) {expressionComboBox.setSelectedItem(currentValue);}
			break;
		case CONFLICT: 
			expressionTextPane.setText("");
			radioButtonNewExpression.setSelected(true);
			break;
		default:
			break;
		}
		
		return showDialog();
	}
	
	
	private MutablePair<Integer, String> showDialog() {
		newValue  = new String();
		GraphicalProperties.resetFonts(this);
		pack();
		Rectangle screen = parentDialog.getGraphicsConfiguration().getBounds();
	    setLocation(
	        screen.x + (screen.width - getWidth()) / 2,
	        screen.y + (screen.height - getHeight()) / 2 ); 
		
		setVisible(true);
		
	    MutablePair<Integer, String> ret = new MutablePair<Integer, String>();
		if(exitOption == ExitOption.CANCEL) return null;
		if(exitOption == ExitOption.FROM_ANCESTOR) {
			ret.left = ExitOption.FROM_ANCESTOR.code;
			ret.right = comboBoxParents.getSelectedItem().toString();;
		} else if(exitOption == ExitOption.LOCAL) {
			ret.left = ExitOption.LOCAL.code;
			if(withComboBox) {
				ret.right = expressionComboBox.getSelectedItem().toString().trim();
			} else {
				ret.right = expressionTextPane.getText().trim();
			}
		} else {
			ret.left = ExitOption.FROM_BASESET.code;
			ret.right = "";
		}
	    return ret;
	}
	
	/**
	 * Create the dialog.
	 */
	public LocalChangeFrame(JDialog parent) {
		super(parent);
		parentDialog = parent;
		setTitle("Changing: ");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(3, 1));
		expressionTextPane = new JTextField();
		expressionComboBox = new JComboBox();
		
	
		{
			JPanel valueFromParentPanel = new JPanel();
			contentPanel.add(valueFromParentPanel);
			valueFromParentPanel.setLayout(new GridLayout(0, 2, 0, 0));
			{
				radioButtonPickValueFromParent = new JRadioButton("Pick value from ancestor:");
				valueFromParentPanel.add(radioButtonPickValueFromParent);
			}
			{
				comboBoxParents = new JComboBox();
				comboBoxParents.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						radioButtonPickValueFromParent.setSelected(true);
						expressionTextPane.setText("");
					}
				});
				valueFromParentPanel.add(comboBoxParents);
			}
		}
		{
			panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(new BorderLayout(0, 0));
			{
				radioButtonNewExpression = new JRadioButton("New expression");
				panel.add(radioButtonNewExpression, BorderLayout.WEST);
			}
			{
				expressionTextPane.getDocument().addDocumentListener(new DocumentListener() {
					  public void changedUpdate(DocumentEvent e) {
					    event();
					  }
					  public void removeUpdate(DocumentEvent e) {
						  event();
					  }
					  public void insertUpdate(DocumentEvent e) {
						  event();
					  }

					  public void event() {
						  radioButtonNewExpression.setSelected(true);
					   }
					});
				expressionTextPane.setPreferredSize(new Dimension(100, 20));
				panel.add(expressionTextPane, BorderLayout.CENTER);
			}
			{
				JPanel valueFromBaseSetPanel = new JPanel();
				contentPanel.add(valueFromBaseSetPanel);
				valueFromBaseSetPanel.setLayout(new BorderLayout());
				{
					radioButtonBaseSet = new JRadioButton("Pick value from base set");
					valueFromBaseSetPanel.add(radioButtonBaseSet);
					expressionTextPane.setText("");
				}
			}
			{
				expressionComboBox.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						 radioButtonNewExpression.setSelected(true);
					}
				});
								
				
			}
			
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Assign new value");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(radioButtonNewExpression.isSelected()) {
							exitOption = ExitOption.LOCAL;
							dispose();
						} else if(radioButtonPickValueFromParent.isSelected()) {
							exitOption = ExitOption.FROM_ANCESTOR;
							dispose();
						}else if(radioButtonBaseSet.isSelected()) {
							exitOption = ExitOption.FROM_BASESET;
							dispose();
						} else {
							//nothing selected
							JOptionPane.showMessageDialog(null, "No option selected!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}	
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						exitOption = ExitOption.CANCEL;
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		//Group the radio buttons.
	    ButtonGroup group = new ButtonGroup();
	    group.add(radioButtonPickValueFromParent);
	    group.add(radioButtonNewExpression);
	    group.add(radioButtonBaseSet);
	}


	

}

enum ExitOption {
		OK(0),
	   CANCEL(-1), 
	   FROM_ANCESTOR(RunManager.NotesLabels.FROM_ANCESTOR.getOption()), 
	   LOCAL(RunManager.NotesLabels.LOCAL.getOption()), 
	   FROM_BASESET(RunManager.NotesLabels.FROM_BASESET.getOption());
	   			          
	   public final int code;
	   
	   ExitOption(int index) {
	              this.code = index;
	    }
	   
	  }
