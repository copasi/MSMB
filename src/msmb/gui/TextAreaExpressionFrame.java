package msmb.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import javax.swing.*;

import org.apache.commons.lang3.tuple.MutablePair;

import msmb.model.ComplexSpecies;
import msmb.model.MultiModel;
import msmb.utility.Constants;
import msmb.utility.GraphicalProperties;
import msmb.utility.MySyntaxException;

public class TextAreaExpressionFrame extends JDialog {

	private JPanel contentPane;
	private MultiModel multiModel;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextAreaExpressionFrame frame = new TextAreaExpressionFrame(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	public void setVisible(boolean b) {
		GraphicalProperties.resetFonts(this);
		pack();
		setLocationRelativeTo(null);
		super.setVisible(b);
	};

	public String showDialog() {
		GraphicalProperties.resetFonts(this);
		setLocationRelativeTo(null);
		pack();
		super.setVisible(true);
		
		String ret = textPane.getText();
	    
	   //POST PROCESS THE TEXT, DELETE NEW LINE, ANYTHING ELSE??
	    
	    return ret;
	}
	
	
	
	/**
	 * Create the frame.
	 */
	public TextAreaExpressionFrame(MultiModel multiModel) {
		this.multiModel = multiModel;
		setTitle("Expression editor");
		setModal(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(45*MainGui.customFont.getSize(), 25*MainGui.customFont.getSize()));
		scrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		textPane = new JTextPane();
		textPane.setEditorKit(new WrapEditorKit());
		scrollPane.setViewportView(textPane);
		textPane.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK), "none");
		textPane.getInputMap().put(KeyStroke.getKeyStroke("ctrl H"), "none");
	    
		
		AutoCompletion_MSMB autoCompletion = new AutoCompletion_MSMB(Constants.provider, multiModel);
       	autoCompletion.setShowDescWindow(true);
   		autoCompletion.setAutoActivationEnabled(true);
   		autoCompletion.setAutoCompleteSingleChoices(false);
   		autoCompletion.setAutoCompleteEnabled(false);
   		autoCompletion.install(textPane);
   		textPane.addKeyListener(new AutoCompleteKeyLister(autoCompletion,multiModel,false));
   		KeyListener keyListener = new KeyListener() {
		      public void keyPressed(final KeyEvent keyEvent) { 
		    	  if(keyEvent.getKeyCode() !=KeyEvent.CTRL_DOWN_MASK) { 
		    		  SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							  final JTextComponent source = ((JTextComponent)keyEvent.getSource());
							  String current = source.getText();
								  int cursor  = source.getCaretPosition();
								  MainGui.updateAutocompletionContext(current, cursor);
						}
		    		  }); 
		    	  }
		      }
		      public void keyReleased(KeyEvent keyEvent) {  }
		      public void keyTyped(KeyEvent keyEvent) {	}
		    
		    };
		    textPane.addKeyListener(keyListener);
		    MouseListener mouseListener = new MouseListener() {
				@Override
				public void mouseReleased(MouseEvent e) {	}
				@Override
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {}
				@Override
				public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(final MouseEvent e) {	  
					SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						  final JTextComponent source = ((JTextComponent)e.getSource());
						  String current = source.getText();
							  int cursor  = source.getCaretPosition();
							  MainGui.updateAutocompletionContext(current, cursor);
					}
	    		  });
				}
			};
			
			textPane.addMouseListener(mouseListener);
		JLabel lblNoteContextAssist = new JLabel("Note: context assist is available while typing (key combination: Ctrl-H)");
		contentPane.add(lblNoteContextAssist, BorderLayout.NORTH);
	}
	
	
	public void setText(String text) {
		textPane.setText(text);
		textPane.setCaretPosition(0);
	}
	
	
	class WrapEditorKit extends StyledEditorKit {
        ViewFactory defaultFactory=new WrapColumnFactory();
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }

    }

    class WrapColumnFactory implements ViewFactory {
        public View create(Element elem) {
            String kind = elem.getName();
            if (kind != null) {
                if (kind.equals(AbstractDocument.ContentElementName)) {
                    return new WrapLabelView(elem);
                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                    return new ParagraphView(elem);
                } else if (kind.equals(AbstractDocument.SectionElementName)) {
                    return new BoxView(elem, View.Y_AXIS);
                } else if (kind.equals(StyleConstants.ComponentElementName)) {
                    return new ComponentView(elem);
                } else if (kind.equals(StyleConstants.IconElementName)) {
                    return new IconView(elem);
                }
            }

            // default to text display
            return new LabelView(elem);
        }
    }

    class WrapLabelView extends LabelView {
        public WrapLabelView(Element elem) {
            super(elem);
        }

        public float getMinimumSpan(int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }

    }
	

}
