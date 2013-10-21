package msmb.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;

import msmb.model.ComplexSpecies;
import msmb.utility.Constants;
import msmb.utility.GraphicalProperties;
import msmb.utility.MySyntaxException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

import org.COPASI.CCopasiDataModel;
import org.COPASI.CCopasiRootContainer;
import org.COPASI.CMetab;
import org.COPASI.CModel;
import org.apache.commons.lang3.tuple.MutablePair;

public class ImportAnnotationsFrame extends JFrame {

	private JPanel contentPane;
	private DefaultListModel listImported_unmatched_model;
	private DefaultListModel list_matched_model;
	private JList listMSMB_unmatched;
	private JList list_matched;
	private DefaultListModel listMSMB_unmatched_model;
	private JList listImported_unmatched;
	private JTextField textFieldFileName;
	private JTextArea txtrXmlAnnotationCode;


	public static void main(String[] args) {
		 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui.addLibraryPath("..\\libs");
					 MainGui.addLibraryPath("..\\..\\libs");
					 MainGui.addLibraryPath(".\\libs");
					ImportAnnotationsFrame frame = new ImportAnnotationsFrame();
					
					frame.showDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void addMSMBelements(Vector<MutablePair<String, String>> element_type) {
		listMSMB_unmatched.removeAll();
		listMSMB_unmatched_model.clear();
		Vector<MSMBelement> items = new Vector<MSMBelement>();
		for(MutablePair<String, String> pair : element_type) {
			items.add(new MSMBelement(pair.left, pair.right));
		}
		Collections.sort(items);
		 for(MSMBelement i : items){
			 listMSMB_unmatched_model.addElement(i);
	    }  
	}
	
	void showDialog() {
		GraphicalProperties.resetFonts(this);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	  
		
	    return;
	}
	

	
	private void automaticMatching() {
		HashMap<String, SBMLelementWithAnnotation> imported = new HashMap<String, SBMLelementWithAnnotation>();
		for(int i = 0; i < listImported_unmatched_model.size(); i++) {
			SBMLelementWithAnnotation element = (SBMLelementWithAnnotation) listImported_unmatched_model.get(i);
			imported.put(element.getName(), element);
		}
		
		HashMap<String, MSMBelement> MSMB = new HashMap<String, MSMBelement>();
		for(int i = 0; i < listMSMB_unmatched_model.size(); i++) {
			MSMBelement element = (MSMBelement) listMSMB_unmatched_model.get(i);
			MSMB.put(element.getName(), element);
		}
		
		Iterator<String> it = MSMB.keySet().iterator();
		Vector<AnnotationAssociation> items = new Vector<AnnotationAssociation>();
		list_matched_model.clear();
		
		while(it.hasNext()) {
			String name = it.next();
			String unquoted = name;
			if(unquoted.startsWith("\"")&&unquoted.endsWith("\"")) unquoted = unquoted.substring(1, unquoted.length()-1);
				if(imported.containsKey(unquoted) ) {
					items.add(new AnnotationAssociation(MSMB.get(name),imported.get(unquoted)));
					listImported_unmatched_model.removeElement(imported.get(unquoted));
					listMSMB_unmatched_model.removeElement(MSMB.get(name));
			}
		}
		
		Collections.sort(items);
		 for(AnnotationAssociation i : items){
			 list_matched_model.addElement(i);
	    }  
		
		
		
	}

	

	public ImportAnnotationsFrame() {
		// to display xml nicely in tree: http://www.javalobby.org/java/forums/t19666.html
		setTitle("Import annotations");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(4, 4));
		setContentPane(contentPane);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.8);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setResizeWeight(0.5);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_1);
		
		JPanel panel_matched = new JPanel();
		splitPane_1.setLeftComponent(panel_matched);
		panel_matched.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel_matched.add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Delete selected associations");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<AnnotationAssociation> selected = list_matched.getSelectedValuesList();
				for(AnnotationAssociation element : selected) {
					listMSMB_unmatched_model.addElement(element.getSpeciesMSMB());
					listImported_unmatched_model.addElement(element.getSpeciesImport());
					list_matched_model.removeElement(element);
				}
				Vector<MSMBelement> items = new Vector<MSMBelement>();
				Enumeration<MSMBelement> e = listMSMB_unmatched_model.elements();
				while(e.hasMoreElements()) {
					items.add(e.nextElement());
				}
				Collections.sort(items);
			
				listMSMB_unmatched_model.clear();
				for(Object i : items){
					 listMSMB_unmatched_model.addElement(i);
			    }  
				 
				Vector<SBMLelementWithAnnotation> items2 = new Vector<SBMLelementWithAnnotation>();
				Enumeration<SBMLelementWithAnnotation> e2 = listImported_unmatched_model.elements();
					while(e2.hasMoreElements()) {
						items2.add(e2.nextElement());
					}
					listImported_unmatched_model.clear();
								Collections.sort(items2);
					 for(Object i : items2){
						 listImported_unmatched_model.addElement(i);
				    }  
				 
				 
				
			}
		});
		panel.add(btnNewButton);
		
		list_matched_model = new DefaultListModel();
	
		JScrollPane scrollPane_jlistMatched = new JScrollPane();
		panel_matched.add(scrollPane_jlistMatched, BorderLayout.CENTER);
	
		list_matched = new JList(list_matched_model);
		scrollPane_jlistMatched.setViewportView(list_matched);
			
		list_matched.addListSelectionListener(new ListSelectionListener() {
			@Override
			 public void valueChanged(ListSelectionEvent e) {
				int firstIndex = list_matched.getSelectedIndex();
				if(firstIndex != -1) {
					txtrXmlAnnotationCode.setText(
							((AnnotationAssociation)list_matched_model.getElementAt(firstIndex)).getAnnotation());
					txtrXmlAnnotationCode.revalidate();
			    }
			}
		});
		
		panel_matched.add(scrollPane_jlistMatched, BorderLayout.CENTER);
		
		JPanel panel_unmatched = new JPanel();
		panel_unmatched.setLayout(new BorderLayout(0, 0));
		splitPane_1.setRightComponent(panel_unmatched);
		
		JPanel panel_1 = new JPanel();
		panel_unmatched.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnAddAssociation = new JButton("Add association");
		btnAddAssociation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int selMSMB = listMSMB_unmatched.getSelectedIndex();
				int selImport =  listImported_unmatched.getSelectedIndex();
				if(selMSMB == -1 || selImport == -1) {
					JOptionPane.showMessageDialog(new JButton(),"No element is selected in the list of elements \nfrom the model and/or from the imported file.", "Missing selection", JOptionPane.ERROR_MESSAGE);
					return;
				}
				AnnotationAssociation aa = new AnnotationAssociation(
						(MSMBelement) listMSMB_unmatched.getSelectedValue(), (SBMLelementWithAnnotation) listImported_unmatched.getSelectedValue());
				list_matched_model.addElement(aa);
				listMSMB_unmatched_model.removeElementAt(selMSMB);
				listImported_unmatched_model.removeElementAt(selImport);
				
				
			}
		});
		panel_1.add(btnAddAssociation);
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setResizeWeight(0.5);
		panel_unmatched.add(splitPane_2, BorderLayout.CENTER);
		
		listMSMB_unmatched_model = new DefaultListModel();
		listMSMB_unmatched = new JList(listMSMB_unmatched_model);
		JScrollPane scrollPane_jlistMSMB_unmatched = new JScrollPane();
		scrollPane_jlistMSMB_unmatched.setViewportView(listMSMB_unmatched);
		
		listMSMB_unmatched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		splitPane_2.setLeftComponent(scrollPane_jlistMSMB_unmatched);
		
		listImported_unmatched_model = new DefaultListModel();
		 listImported_unmatched = new JList(listImported_unmatched_model);
		
		listImported_unmatched.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane_listImported_unmatched = new JScrollPane();
		scrollPane_listImported_unmatched.setViewportView(listImported_unmatched);
			
		listImported_unmatched.addListSelectionListener(new ListSelectionListener() {
			@Override
			 public void valueChanged(ListSelectionEvent e) {
				int firstIndex = listImported_unmatched.getSelectedIndex();
				if(firstIndex!=-1) {
					txtrXmlAnnotationCode.setText(
							((SBMLelementWithAnnotation)listImported_unmatched_model.getElementAt(firstIndex)).getAnnotation());
					txtrXmlAnnotationCode.revalidate();
				    }
				}	
		});
		
		splitPane_2.setRightComponent(scrollPane_listImported_unmatched);
		
		JScrollPane scrollPane_xmlAnnotation = new JScrollPane();
		splitPane.setRightComponent(scrollPane_xmlAnnotation);
		
		txtrXmlAnnotationCode = new JTextArea();
		txtrXmlAnnotationCode.setEditable(false);
		scrollPane_xmlAnnotation.setViewportView(txtrXmlAnnotationCode);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.NORTH);
		panel_2.setLayout(new BorderLayout(4, 4));
		
		JLabel lblNewLabel = new JLabel("Import annotations from file: ");
		panel_2.add(lblNewLabel, BorderLayout.WEST);
		
		textFieldFileName = new JTextField();
		panel_2.add(textFieldFileName, BorderLayout.CENTER);
		textFieldFileName.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("Browse...");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser("C:\\Users\\alida\\Dropbox\\__VT\\MENDES_mRNA_model\\0-Initial material");
				int returnVal = fileChooser.showSaveDialog(null);
		        if (returnVal == JFileChooser.APPROVE_OPTION) {
		            File file = fileChooser.getSelectedFile();
		            assert CCopasiRootContainer.getRoot() != null;
		            CCopasiDataModel dataModel = CCopasiRootContainer.addDatamodel();
		            assert CCopasiRootContainer.getDatamodelList().size() == 1;
		            textFieldFileName.setText(file.getAbsolutePath());
		            try {
						dataModel.importSBML(file.getAbsolutePath());
					} catch (Exception e) {
						e.printStackTrace();
					}
		            CModel model = dataModel.getModel();
		            
		            int iMax = (int)model.getMetabolites().size();
		            
		            for (int i = 0;i < iMax;i++)
		            {
		                CMetab metab = model.getMetabolite(i);
		                
		                SBMLelementWithAnnotation element = new SBMLelementWithAnnotation(metab.getObjectName(), 
		                																Constants.TitlesTabs.SPECIES.description,
		                																metab.getMiriamAnnotation());
		                listImported_unmatched_model.addElement(element);
		            }
		            
		            automaticMatching();
		            
		        } 
			}
		});
		panel_2.add(btnNewButton_1, BorderLayout.EAST);
		
		pack();
	}

}

class AnnotationAssociation implements Comparable<AnnotationAssociation>{
	
	MSMBelement speciesMSMB = new MSMBelement("", "");
	SBMLelementWithAnnotation speciesImport = new SBMLelementWithAnnotation("","", "");
	
	public MSMBelement getSpeciesMSMB() {		return speciesMSMB;	}
	public SBMLelementWithAnnotation getSpeciesImport() {		return speciesImport;	}

	public AnnotationAssociation(MSMBelement msmb_speciesName, SBMLelementWithAnnotation import_SBMLelement) {
		speciesMSMB = new MSMBelement(msmb_speciesName);
		speciesImport = new SBMLelementWithAnnotation(import_SBMLelement);
	}
	
	@Override
	public String toString() {
		return speciesMSMB.getName() +" <-> " + speciesImport;
	}
	
	public String getAnnotation() {return speciesImport.getAnnotation();}
	
	@Override
	public int compareTo(AnnotationAssociation o) {
		return speciesMSMB.compareTo(o.speciesMSMB);
	}
	
}

class SBMLelementWithAnnotation implements Comparable<SBMLelementWithAnnotation>{
	
	String elementName = new String();
	String elementType = new String();
	String elementAnnotation = new String();
	
	public String getAnnotation() {return elementAnnotation;}
	public String getName() {return elementName;}
	
	public SBMLelementWithAnnotation(String name, String type, String annot) {
		elementName = new String(name);
		elementType = new String(type);
		elementAnnotation = new String(annot);
	}
	
	public SBMLelementWithAnnotation(SBMLelementWithAnnotation from) {
		elementName = new String(from.elementName);
		elementType = new String(from.elementType);
		elementAnnotation = new String(from.elementAnnotation);
	}
	
	@Override
	public String toString() {
		return elementName +" (" + elementType+")";
	}
	@Override
	public int compareTo(SBMLelementWithAnnotation o) {
		return elementName.compareTo(o.elementName);
	}
}

class MSMBelement implements Comparable<MSMBelement> {
	String elementName = new String();
	String elementType = new String();
	public String getName() {return elementName;}
	
	
	public MSMBelement(String name, String type) {
		elementName = new String(name);
		elementType = new String(type);
	}
	
	public MSMBelement(MSMBelement o) {
		elementName = new String(o.elementName);
		elementType = new String(o.elementType);
	}
	
	@Override
	public String toString() {
		return elementName +" (" + elementType+")";
	}


	@Override
	public int compareTo(MSMBelement o) {
		return elementName.compareTo(o.elementName);
	}
	
}