package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTextPane;
import javax.swing.JButton;

import org.apache.commons.lang3.tuple.Pair;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.MutableTreeTableNode;


import debugTab.FoundElement;
import debugTab.FoundElementToDelete;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;
import javax.swing.JScrollPane;

import utility.*;

public class DeleteFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private String speciesCompleteRow = new String();
	private static JXTreeTableExtended treeTable;
	private static DefaultTreeTableModel foundElementTableModel;
	static FoundElementToDelete rootElementToDelete;
	private static DefaultMutableTreeTableNode completeMutableTree;
	private JComponent panel_jXtable;
	private DefaultMutableTreeTableNode rootNode;
	public String getSpeciesCompleteRow() {return speciesCompleteRow;}
	public void setSpeciesCompleteRow(String speciesCompleteRow) {this.speciesCompleteRow = speciesCompleteRow;}

	
	public DeleteFrame(Pair<FoundElementToDelete, DefaultMutableTreeTableNode> pair) {
		setTitle("Delete");
		setModal(true);
		completeMutableTree = pair.getRight();
		rootElementToDelete = pair.getLeft();
		setSpeciesCompleteRow(MainGui.printCompleteRowContent(rootElementToDelete));

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JTextPane txtpnThistextifadifferentif = new JTextPane();
		txtpnThistextifadifferentif.setBackground(super.getBackground());
		txtpnThistextifadifferentif.setEditable(false);
		txtpnThistextifadifferentif.setText("You are about to delete the following component: \r"+speciesCompleteRow+".\rThis action will have the following impact on the model:");
		panel.add(txtpnThistextifadifferentif, BorderLayout.CENTER);
		
		JPanel panel_selectWhatToDelete = new JPanel();
		contentPane.add(panel_selectWhatToDelete, BorderLayout.CENTER);
		panel_selectWhatToDelete.setLayout(new BorderLayout(0, 0));
		
		panel_jXtable = new JPanel();
		panel_selectWhatToDelete.add(panel_jXtable, BorderLayout.CENTER);
		
		
		rootNode = getPartialViewOfCompleteMutableTree();
		treeTable = createXTreeTable(rootNode);
		treeTable.expandRow(0);
		treeTable.getSelectionModel().setSelectionMode (TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeTable.setAutoStartEditOnKeyStroke(true);
		
		
        panel_jXtable.setLayout(new BorderLayout(0, 0));
               
        CustomColumnFactory factory2 = new CustomColumnFactory();
        DeleteTreeRendering.configureColumnFactory(factory2, getClass());
        treeTable.setColumnFactory(factory2);
      	treeTable.setColumnSequence(new Object[] { Constants.DeleteColumns.TREE_ELEMENT.description, Constants.DeleteColumns.ELEMENT.description, Constants.DeleteColumns.ACTION_TO_TAKE.description, Constants.DeleteColumns.NEW_VALUE.description});
	  	treeTable.getTableHeader().setReorderingAllowed(false);
	  	
	  	
	
	    panel_jXtable.add(new JScrollPane(treeTable));	
		
	  	
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Cancel");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JButton btnDeleteSelected = new JButton("Apply chosen actions");
		btnDeleteSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				MainGui.applyDeleteActions(foundElementTableModel.getRoot());
				MainGui.indexToDelete.clear();
				dispose();
			}
		});
		panel_3.add(btnDeleteSelected);
		panel_3.add(btnNewButton);
		
		
		treeTable.expandAll();
        treeTable.resetComboBoxValues();
	}
	
	

	static boolean resetPartialView = true;
		
	static void resetPartialViewOfCompleteMutableTree(DefaultMutableTreeTableNode node) {
		if(!resetPartialView) { // because the expandPath call the setValueAt which calls again this one... so I want to avoid infinite loops
			resetPartialView = true;
			return;
		} else  {
			resetPartialView = false;
		}
		
		DefaultMutableTreeTableNode checkFromHere = (DefaultMutableTreeTableNode) completeMutableTree;
		Vector<DefaultMutableTreeTableNode> children = new Vector<DefaultMutableTreeTableNode>();
		for(int i = 0; i < checkFromHere.getChildCount(); i++) {
			DefaultMutableTreeTableNode n = (DefaultMutableTreeTableNode) checkFromHere.getChildAt(i);
			if(n.getUserObject() instanceof FoundElementToDelete && node.getUserObject() instanceof FoundElementToDelete) {
					if(n.getUserObject().equals(node.getUserObject())) { children.addAll(getAllChildren(n)); }
					else {
						children.addAll(lookForChildren(n,node));
					}
			}
		}
		
		if(node.getUserObject() instanceof FoundElementToDelete) {
			FoundElementToDelete elem = (FoundElementToDelete) node.getUserObject();
			if(elem.getActionToTake().compareTo(Constants.DeleteActions.DELETE.description)==0) {
				if(node.getChildCount()==0) {
					for(DefaultMutableTreeTableNode child : children) {
						foundElementTableModel.insertNodeInto(child, node, node.getChildCount());
					}
				}
			} else {
				int children1 = node.getChildCount();
				for(int i = 0; i < children1 ; i++) {
					foundElementTableModel.removeNodeFromParent((MutableTreeTableNode) node.getChildAt(0));
				}
			}
		}
		
		
		treeTable.revalidate();
		if( node.getChildCount() > 0) {
			treeTable.expandPath(new TreePath(foundElementTableModel.getPathToRoot(node)));
			
		} else {
			resetPartialView = true;
		
		}
		treeTable.getTreeSelectionModel().setSelectionPath(	new TreePath(foundElementTableModel.getPathToRoot(node)));
		treeTable.resetComboBoxValues();	
	
	}
	
	private static Vector<DefaultMutableTreeTableNode> lookForChildren(DefaultMutableTreeTableNode newRoot,	DefaultMutableTreeTableNode node) {
		Vector<DefaultMutableTreeTableNode> children = new Vector<DefaultMutableTreeTableNode>();
		DefaultMutableTreeTableNode checkFromHere = newRoot;
		for(int i = 0; i < checkFromHere.getChildCount(); i++) {
			DefaultMutableTreeTableNode n = (DefaultMutableTreeTableNode) checkFromHere.getChildAt(i);
			if(n.getUserObject().equals(node.getUserObject())) { children.addAll(getAllChildren(n)); }
			else {
				children.addAll(lookForChildren(n,node));
			}
		}
		return children;
	}

	private static Vector<DefaultMutableTreeTableNode> getAllChildren(DefaultMutableTreeTableNode parent) {
		Vector<DefaultMutableTreeTableNode> ret = new Vector<DefaultMutableTreeTableNode>();
		for(int i = 0; i < parent.getChildCount(); i++) {
			ret.add(new DefaultMutableTreeTableNode(parent.getChildAt(i).getUserObject()));
		}
		return ret;
	}

	private static DefaultMutableTreeTableNode getPartialViewOfCompleteMutableTree_recCall(DefaultMutableTreeTableNode partial_completeMutableTree) {
		
		DefaultMutableTreeTableNode partialTree = new DefaultMutableTreeTableNode(partial_completeMutableTree.getUserObject());

		if(partial_completeMutableTree.getChildCount() == 0) return partialTree;
		
		DefaultMutableTreeTableNode currentRoot = (DefaultMutableTreeTableNode) partial_completeMutableTree;
//		DefaultMutableTreeTableNode nodeOnTheCompleteTree = (DefaultMutableTreeTableNode) partial_completeMutableTree.getChildAt(0);

		for(int i = 0; i < currentRoot.getChildCount(); i++) {
			DefaultMutableTreeTableNode currentChild = (DefaultMutableTreeTableNode) currentRoot.getChildAt(i);
			if(currentRoot.getUserObject() instanceof FoundElementToDelete) {
				FoundElementToDelete elementParent = (FoundElementToDelete) currentRoot.getUserObject();
				//FoundElementToDelete elementChild = (FoundElementToDelete) currentChild.getUserObject();

				if(elementParent.getActionToTake().compareTo(Constants.DeleteActions.DELETE.description)==0) {
					partialTree.add(getPartialViewOfCompleteMutableTree_recCall(currentChild));
				}

			}

		}
		return partialTree;
		
	}
	private static DefaultMutableTreeTableNode getPartialViewOfCompleteMutableTree() {
		//return completeMutableTree;
		DefaultMutableTreeTableNode partialTree = new DefaultMutableTreeTableNode(completeMutableTree.getChildAt(0).getUserObject());
	
		DefaultMutableTreeTableNode currentRoot = null;
		
		currentRoot = (DefaultMutableTreeTableNode) completeMutableTree.getChildAt(0);
		
		DefaultMutableTreeTableNode nodeOnTheCompleteTree = (DefaultMutableTreeTableNode) completeMutableTree.getChildAt(0);
		
		for(int i = 0; i < currentRoot.getChildCount(); i++) {
			
			DefaultMutableTreeTableNode currentChild = (DefaultMutableTreeTableNode) nodeOnTheCompleteTree.getChildAt(i);
			if(currentRoot.getUserObject() instanceof FoundElementToDelete) {
				FoundElementToDelete elementParent = (FoundElementToDelete) currentRoot.getUserObject();
				//FoundElementToDelete elementChild = (FoundElementToDelete) currentChild.getUserObject();
	
				if(elementParent.getActionToTake().compareTo(Constants.DeleteActions.DELETE.description)==0) {
					//partialTree.add(new DefaultMutableTreeTableNode(elementChild));
					partialTree.add(getPartialViewOfCompleteMutableTree_recCall(currentChild));
				}
				
			}

		}
		DefaultMutableTreeTableNode invisibleRoot =  new DefaultMutableTreeTableNode("");
		invisibleRoot.add(partialTree);
		return invisibleRoot;
	}





		/*public TreeTableModel generateTreeFoundElements(Vector <> ) {
			DefaultMutableTreeTableNode currentNameNode = null;
			
			DefaultMutableTreeTableNode aRoot = new DefaultMutableTreeTableNode(afsdasf);
			for (FoundElement testPerson : list) {
				/*currentLast = testPerson.getTableDescription();
				if (currentLast.equals(prevLast)) {
					currentNameNode.add(new DefaultMutableTreeTableNode(testPerson));
				} else {
					if (currentNameNode != null) {
						aRoot.add(currentNameNode);
					}
					currentNameNode = new DefaultMutableTreeTableNode(new FoundElement(Constants.TitlesTabs.REACTIONS.description,0,0));
					currentNameNode.add(new DefaultMutableTreeTableNode(testPerson));
					prevLast = currentLast;
				}*
				currentNameNode = new DefaultMutableTreeTableNode(new FoundElement(Constants.TitlesTabs.REACTIONS.description,0,0));
				currentNameNode.add(new DefaultMutableTreeTableNode(testPerson));
				aRoot.add(currentNameNode);
			}
			return new TreeTableModel(aRoot);
		}*/

	    
	    private static JXTreeTableExtended createXTreeTable(DefaultMutableTreeTableNode defaultMutableTreeNode) {
	    	
	    	treeTable = new JXTreeTableExtended();
			foundElementTableModel = new TreeTableModel(defaultMutableTreeNode);
	    	treeTable.setTreeTableModel(foundElementTableModel);
			
		
			
			
			treeTable.setEditable(true);
			treeTable.setTreeCellRenderer(new DefaultTreeCellRenderer() {
				private static final long serialVersionUID = 1L;

				public java.awt.Component getTreeCellRendererComponent(
						javax.swing.JTree tree, Object value, boolean sel,
						boolean expanded, boolean leaf, int row,
						boolean hasFocus) {
					
										 
					if (value instanceof DefaultMutableTreeTableNode) {
						DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode) value;
						if(node.getUserObject() instanceof FoundElement) {
							FoundElement person = (FoundElement) node.getUserObject();
							setText(person.toString());
						}
							if (node.isLeaf() && node.getParent() == tree.getModel().getRoot()) {
								//foundElementTableModel.insertNodeInto(new DefaultMutableTreeTableNode(""), node, 0);
								setIcon(getDefaultClosedIcon());
							}
						
							else super.getTreeCellRendererComponent(tree, value, sel,expanded, leaf, row, hasFocus); 
						
						
					}
					
					

					return this;
				}
			});
			
			
			//add a highlighter, pretty.
			treeTable.addHighlighter(HighlighterFactory.createAlternateStriping(Color.white,MainGui.color_shading_table));
		
			treeTable.initializeRowEditorModel();
				
	        return treeTable;
	    }
	    
	   
	  
}
