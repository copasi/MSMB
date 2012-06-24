package gui;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.GridLayout;
import utility.GridLayout2;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import debugTab.FoundElement;

public class RenamingFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabel = null;
	private JLabel jLabelOriginalName = null;
	private JLabel jLabel1 = null;
	private JTextField jLabelNewName = null;
	private JScrollPane jScrollPane = null;
	private GridLayout gridLayout;  
	private JPanel jPanel = null;
	private JButton jButtonAll;
	private JButton jButtonNone;
	private JLabel jLabelS;
	private JLabel jLabelComment;
	
	private JButton jButtonRename = null;
	
	
	private String from = new String();
	private String to = new String();
	private Vector<FoundElement> foundElements = new Vector<FoundElement>();

	
	public RenamingFrame(MainGui owner) {
		super();
		initialize();
	}
	
	public void setRenamingString(String from, String to) {
		this.from = from;
		this.to = to;
	}
	
	public void clearAll() {
		jLabelOriginalName.setText("");
		jLabelNewName.setText("");
		jPanel.removeAll();
	}
	
	
	
	private void initialize() {
		this.setSize(272, 350);
		this.setContentPane(getJContentPane());
		this.setTitle("Renaming");
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}

	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelNewName = new JTextField();
			jLabelNewName.setBounds(new Rectangle(101, 34, 153, 20));
			jLabelNewName.setText("");
			jLabelNewName.setEditable(false);
			jLabelNewName.getDocument().addDocumentListener(new DocumentListener() {
				  public void changedUpdate(DocumentEvent e) {
				    updateTo();
				  }
				  public void removeUpdate(DocumentEvent e) {
					  updateTo();
				  }
				  public void insertUpdate(DocumentEvent e) {
					  updateTo();
				  }

				  public void updateTo() {
					  Component[] comps = jPanel.getComponents();
					  if(jLabelNewName.getText().trim().length() == 0) {
						  for(int i = 0; i < comps.length; i++ ){
							  Component c = comps[i];
							  if(c instanceof JCheckBox) {
								  ((JCheckBox)c).setSelected(false);
							  }
						  }
						  return;
					  } 
					  Vector<Object> selected = new Vector<>();
					  for(int i = 0; i < comps.length; i++ ){
						  Component c = comps[i];
						  if(c instanceof JCheckBox) {
							  if(((JCheckBox) c).isSelected()) {
								  selected.add(i);
								  ((JCheckBox)c).setSelected(false);
							  }
						  }
					  }
					  to = jLabelNewName.getText().trim();

					  for(int i = 0; i < comps.length; i++ ){
						  Component c = comps[i];
						  if(c instanceof JCheckBox) {
							  if(selected.contains(i)) {
								   ((JCheckBox)c).setSelected(true);
							  }
						  }
					  }
				  }
				});
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(13, 34, 90, 20));
			jLabel1.setText("New name:");
			jLabelOriginalName = new JLabel();
			jLabelOriginalName.setBounds(new Rectangle(101, 9, 153, 20));
			jLabelOriginalName.setText("");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(13, 9, 90, 20));
			jLabel.setText("Current name:");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabel, null);
			jContentPane.add(jLabelOriginalName, null);
			jContentPane.add(jLabel1, null);
			jContentPane.add(jLabelNewName, null);
			jContentPane.add(getJScrollPane(), null);

			jLabelS = new JLabel();
			jLabelS.setBounds(new Rectangle(13, 228, 45, 20));
			jLabelS.setText("Select");
			jContentPane.add(jLabelS, null);
			jButtonAll = new JButton("All");
			jButtonAll.setBounds(new Rectangle(jLabelS.getWidth()+6, 228, 60, 26));
			jButtonAll.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Component[] comps = jPanel.getComponents();
					for(Component c: comps ){
						if(c instanceof JCheckBox)((JCheckBox)c).setSelected(true);
					}
				}

			});
			jContentPane.add(jButtonAll, null);
			
			jButtonNone = new JButton("None");
			jButtonNone.setBounds(new Rectangle(jLabelS.getWidth()+jButtonAll.getWidth()+12, 228, 60, 26));
			jButtonNone.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Component[] comps = jPanel.getComponents();
					for(Component c: comps ){
						if(c instanceof JCheckBox) ((JCheckBox)c).setSelected(false);
					}
				}

			});
			jContentPane.add(jButtonNone, null);
			
			jLabelComment = new JLabel();
			jLabelComment.setBackground(jPanel.getBackground());
			
			jLabelComment.setFont(jLabelS.getFont());
		
			jLabelComment.setBounds(new Rectangle(13, 228+20+6, jScrollPane.getWidth()-6, 40));
			jLabelComment.setText("<html>(general renaming strategies can be customized <p> through File-Preferences...)<html>");
			jContentPane.add(jLabelComment, null);
			
			jButtonRename = new JButton("Rename");
			//jButton.setBounds(new Rectangle(this.size().width-100-17, 228, 100, 26));
			jButtonRename.setBounds(new Rectangle(this.getSize().width-80-17, 228+20+6+20+6+6, 80, 26));
			jButtonRename.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainGui.autocompleteWithDefaults = false;
					MainGui.applyRenaming(collectRenamingElements());
					boolean oldAutocompleteWithDefaults = MainGui.autocompleteWithDefaults;
					MainGui.revalidateExpressions(collectElements_noRenaming());
					MainGui.autocompleteWithDefaults = oldAutocompleteWithDefaults;
					setVisible(false);
				}

			

			});
			jContentPane.add(jButtonRename, null);
			
		}
		return jContentPane;
	}

	private Vector<Vector<Comparable>> collectRenamingElements() {
		Vector<Vector<Comparable>> ret = new Vector<Vector<Comparable>>();
		for(int i = 0, jj = 0; i < jPanel.getComponentCount();i++) {
			Component c = jPanel.getComponent(i);
			Vector<Comparable> element = new Vector<Comparable>();
			if(c instanceof JCheckBox) {
				JCheckBox j = (JCheckBox)c;
				if(j.isSelected()) {
					element.add(j.getText());
					element.add(foundElements.get(jj));
					ret.add(element);
				}
				jj++;
			}
		}
		return ret;
	}
	
	private Vector<FoundElement> collectElements_noRenaming() {
		Vector<FoundElement> ret = new Vector<FoundElement>();
		for(int i = 0, jj = 0; i < jPanel.getComponentCount();i++) {
			Component c = jPanel.getComponent(i);
			if(c instanceof JCheckBox) {
				JCheckBox j = (JCheckBox)c;
				if(!j.isSelected()) {
					ret.add(foundElements.get(jj));
				}
				jj++;
			}
		}
		return ret;
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
	
	public void fillFrameFields(Vector<FoundElement> found) throws Exception{
		jPanel.removeAll();
		this.foundElements.clear();
		this.foundElements.addAll(found);	
		this.jLabelOriginalName.setText(this.from);
		this.jLabelNewName.setText(this.to);
		
		gridLayout = new GridLayout2();
		int nparam = found.size();
		if(nparam < 5) gridLayout.setRows(5);
		else gridLayout.setRows(nparam+2);
		
		gridLayout.setHgap(2);
		gridLayout.setVgap(2);
		
		jPanel.setLayout(gridLayout);
		jPanel.setBackground(Color.white);
		
		jPanel.add(new JLabel("Declared in"));
		for(int i = 0; i < nparam; i++) {
		
			Dimension dim = new Dimension(15,20);
			JCheckBox foundCB = new JCheckBox();
			foundCB.setBackground(jPanel.getBackground());
			if(foundCB.getPreferredSize().width < dim.width) foundCB.setPreferredSize(dim);
			if(foundCB.getMinimumSize().width < dim.width) foundCB.setMinimumSize(dim);
			foundCB.addItemListener(new ItemListener() {
			    public void itemStateChanged(ItemEvent e) {
			    	updateCurrentText(((JCheckBox)(e.getSource())));
			      }
			});

			foundCB.setText(MainGui.getCellContent(found.get(i)));
			
			foundCB.setToolTipText(MainGui.getRowContent(found.get(i)));

			
			jPanel.add(foundCB);
			if(i==0) jPanel.add(new JLabel("Used in"));	
		}
		
		if(nparam < 5) {//fill the grid with empty labels
			
			for(int i = 0; i < 5-nparam-2; i++) {
				JLabel nameLabel = new JLabel(" ");
				jPanel.add(nameLabel);
			}
		}
		
		revalidate();
	}

	static String replaceAllWords(String original, String find, String replacement) {
	    StringBuilder result = new StringBuilder(original.length());
	    String delimiters = "+-*/(), ;{}=";
	    StringTokenizer st = new StringTokenizer(original, delimiters, true);
	    while (st.hasMoreTokens()) {
	        String w = st.nextToken();
	        if (w.equals(find)) {
	            result.append(replacement);
	        } else {
	            result.append(w);
	        }
	    }
	    return result.toString();
	}
	
	

		
	
	private void updateCurrentText(JCheckBox jCheckBox) {
		if(jCheckBox.isSelected()) {
			String old = jCheckBox.getText();
			String newS =replaceAllWords(old,from,to);
			jCheckBox.setText(newS);
		} else {
			String old = jCheckBox.getText();
			String newS = replaceAllWords(old, to, from);
			jCheckBox.setText(newS);
		}
		jPanel.revalidate();
		
	}
	
	public void setSelected(FoundElement foundElement) {
		int ind = -1;
		for(int i = 0; i < foundElements.size(); i++) {
			FoundElement el = foundElements.get(i);
			if(el.compareTo(foundElement)==0) ind = i;
		}
		if(ind >1) { ind = ind + 2;}
		if(ind == 0) ind = 1;
		
		JCheckBox j = (JCheckBox)jPanel.getComponent(ind);
		j.setSelected(true);
		jPanel.revalidate();
	}

	public void renameAll() {
		Component[] comps = jPanel.getComponents();
		for(Component c: comps){
			if(c instanceof JCheckBox) ((JCheckBox)c).setSelected(true);
		}
		MainGui.applyRenaming(collectRenamingElements());
	}
	




} 