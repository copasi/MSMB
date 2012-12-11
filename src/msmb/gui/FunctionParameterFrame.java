package  msmb.gui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import javax.swing.JComboBox;


import msmb.model.Function;
import msmb.utility.Constants;
import msmb.utility.GridLayout2;

import java.util.HashSet;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class FunctionParameterFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JTextField jLabelFunName = null;
	private JLabel jLabel1 = null;
	private JLabel jLabelEquation = null;
	private JScrollPane jScrollPane = null;
	private GridLayout gridLayout;  
	private JPanel jPanel = null;
	
	private Function function = null;
	private JButton jButton = null;
	
	private int modifiedRow = -1;
	private MainGui parentFrame = null;
	private String oldName;
	
	public FunctionParameterFrame(MainGui owner, Function f, int row) {
		super();
		initialize();
		modifiedRow = row;
		parentFrame  = owner;
		if(f!= null)
			try {
				function = f;
				fillFrameFields(f);
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
	}
	
	public void setFunction(int row, Function f) {
		if(f!= null)
			try {
				function = f;
				modifiedRow = row;
				oldName = f.getName();
				fillFrameFields(f);
			} catch (Exception e) {
				if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e.printStackTrace();
			}
	}
	
	public void clearAll() {
		this.function = null;
		jLabelFunName.setText("");
		this.jLabelEquation.setText("");
		jPanel.removeAll();
	}
	
	
	
	private void initialize() {
		this.setSize(272, 286);
		this.setContentPane(getJContentPane());
		this.setTitle("Function properties...");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelEquation = new JLabel();
			jLabelEquation.setBounds(new Rectangle(101, 34, 153, 20));
			jLabelEquation.setText("");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(13, 34, 90, 20));
			jLabel1.setText("Equation:");
			jLabelFunName = new JTextField();
			jLabelFunName.setBounds(new Rectangle(101, 9, 153, 20));
			jLabelFunName.setText("");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(13, 9, 90, 20));
			jLabel.setText("Function name:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabelFunName, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabelEquation, null);
			jContentPane.add(getJScrollPane(), null);
			jContentPane.add(getJButton(), null);
			
		}
		return jContentPane;
	}

	
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(13, 62, 242, 160));
			jScrollPane.setViewportView(getJPanel());
		}
		return jScrollPane;
	}

	

	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			EmptyBorder border = new EmptyBorder(6,6,5, 5);
			
			jPanel.setBorder(border);
		}
		return jPanel;
	}
	
	private void fillFrameFields(Function f) throws Exception{
		this.jLabelFunName.setText(f.getName());
		this.jLabelEquation.setText(f.getExpandedEquation(new Vector<String>()));
		
		Vector<String> paramNames = f.getParametersNames();
		Vector<String> paramTypes = f.getParametersTypes();
		gridLayout = new GridLayout2();
		int nparam = paramNames.size();
		if(nparam < 5) gridLayout.setRows(5);
		else gridLayout.setRows(nparam);
		
		gridLayout.setHgap(2);
		gridLayout.setVgap(5);
		
		
		jPanel.setLayout(gridLayout);
		
		Vector<String> pTypes = new Vector<String>();
		pTypes.addAll(Constants.paramTypes);
		
		for(int i = 0; i < nparam; i++) {
			Dimension dim = new Dimension(15,20);
			JLabel nameLabel = new JLabel();
			if(nameLabel.getPreferredSize().width < dim.width) nameLabel.setPreferredSize(dim);
			if(nameLabel.getMinimumSize().width < dim.width) nameLabel.setMinimumSize(dim);
			
			nameLabel.setText(paramNames.get(i));
			JComboBox<String> types = new JComboBox<String>(pTypes);
			String t = paramTypes.get(i);
			types.setPreferredSize(new Dimension(50,20));
			types.setMinimumSize(new Dimension(50,20));
			for(int j = 0; j <pTypes.size(); j++) {
				if(pTypes.get(j).toLowerCase().contains(t.toLowerCase())) {
					types.setSelectedIndex(j);
					break;
				}
			}
			JTextField order = new JTextField(new Integer(i+1).toString());
			order.setMinimumSize(dim);
			order.setMaximumSize(dim);
			order.setPreferredSize(dim);
			jPanel.add(order);
			jPanel.add(nameLabel);
			jPanel.add(types);
			
		}
		
		if(nparam < 5) {//fill the grid with empty labels
			
			for(int i = 0; i < 15-nparam*3; i++) {
				JLabel nameLabel = new JLabel(" ");
				jPanel.add(nameLabel);
			}
		}
		
		revalidate();
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton("Update Model");
			//jButton.setBounds(new Rectangle(this.size().width-100-17, 228, 100, 26));
			jButton.setBounds(new Rectangle(this.getSize().width-120-17, 228, 120, 26));
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					Function alreadyExist = null;
					try {
						alreadyExist = parentFrame.multiModel.funDB.getFunctionByName(jLabelFunName.getText());
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					int alreadyExIndex = parentFrame.multiModel.funDB.getFunctionIndex(alreadyExist);
					if(alreadyExIndex!=-1 && alreadyExIndex+1!=modifiedRow) {
						  JOptionPane.showMessageDialog(new JButton(),"The new name you chose already exists!", "Invalid name!", JOptionPane.ERROR_MESSAGE);
						  return;
					}
				
					
					try {
						function.setName(jLabelFunName.getText());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					if(modifiedRow != -1) {
						try {
							updateFunctionParameter();
						} catch (Exception e1) {
							if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) e1.printStackTrace();
							  JOptionPane.showMessageDialog(new JButton(),e1.getMessage(), "Invalid change!", JOptionPane.ERROR_MESSAGE);
							  return;
						}
						parentFrame.renameFunction_fromCellOrfromFunctionParameterFrame(function, modifiedRow,oldName);
					}
					setVisible(false);
				}
			
			});
			
		}
		return jButton;
	}
	
	private void updateFunctionParameter() throws Exception {
		Component[] comp = jPanel.getComponents();
		String paramName = null;
		String paramType = null;
		Integer cparamType = null;
		Integer paramOrder = null;
		HashSet<Integer> indexes = new HashSet<Integer>();
		for(int i = 0; i < comp.length; i++) {
			Component current = comp[i];
			if(current instanceof JTextField) {
				try{
					paramOrder =Integer.parseInt(((JTextField)current).getText());
					
				} catch(Exception ex) {
					if(MainGui.DEBUG_SHOW_PRINTSTACKTRACES) ex.printStackTrace();
					throw new Exception("\""+((JTextField)current).getText()+"\" is not a valid integer index.");
				}
				if(indexes.contains(paramOrder)) {
					throw new Exception(paramOrder+": duplicate index.");
				}
				indexes.add(paramOrder);
			}
			if(current instanceof JLabel) {
				paramName = ((JLabel)current).getText();
			}
			if(current instanceof JComboBox) {
				paramType = ((String)((JComboBox<?>)current).getSelectedItem());
			}
			if(paramName!= null && paramType != null && paramOrder != null) {
				
				if(function.getNumParam() < paramOrder){
					throw new Exception("The index "+paramOrder+" is out of bound. The function contains "+function.getNumParam()+ " parameters.");
				}
				
				cparamType = Constants.FunctionParamType.getCopasiTypeFromDescription(paramType);
				function.setParameterRole(paramName, cparamType);
				function.setParameterIndex(paramName, paramOrder);
				paramName = null;
				paramType = null;
				paramOrder = null;
			}
		}
	}

	public String getSignature() {
		if(this.function!= null) return this.function.printCompleteSignature();
		else return null;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
