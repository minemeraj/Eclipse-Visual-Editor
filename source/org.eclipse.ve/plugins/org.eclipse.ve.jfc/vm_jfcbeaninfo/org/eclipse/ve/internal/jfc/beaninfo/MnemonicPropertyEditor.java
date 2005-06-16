package org.eclipse.ve.internal.jfc.beaninfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: MnemonicPropertyEditor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-16 17:46:03 $ 
 */
public class MnemonicPropertyEditor extends JPanel {
	
     /**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -7164390783177994848L;
	private JList constantsList = null;
     private JTextField constantField = null;
     private JTextField descriptionField = null;
     
     private static TreeMap constantsToValue = null;
     private static HashMap valueToConstants = null;
     
     private boolean initialized = false;
     private Integer value;

     public MnemonicPropertyEditor() {
     	value = new Integer( KeyEvent.VK_UNDEFINED );
     	
     	// This will ensure the constants list will get focus when the property
     	// editor window activated
     	this.addAncestorListener( new AncestorListener() {
     		public void ancestorAdded( AncestorEvent e ) {
				Container parent = e.getAncestorParent();
				if (parent instanceof JComponent) {
					Container topParent = ((JComponent) parent).getTopLevelAncestor();
					if (topParent instanceof Window) {
						((Window) topParent).addWindowListener(new WindowAdapter() {
							public void windowOpened(WindowEvent e) {
								getConstantsList().requestFocus();
							}
							public void windowActivated(WindowEvent e) {
								getConstantsList().requestFocus();
							}
						});
					}
				}
			}
     		public void ancestorMoved( AncestorEvent e ) {
     		}
     		public void ancestorRemoved( AncestorEvent e ) {
     		}
     	} );
     }
	
	private TreeMap getConstantsToValue() {
		if ( constantsToValue == null ) {
			initializeMaps();
		}
		return constantsToValue;
	}
	
	private HashMap getValueToConstants() {
		if ( valueToConstants == null ) {
			initializeMaps();
		}
		return valueToConstants;
	}

    private void initializeMaps() {
        constantsToValue = new TreeMap();
        valueToConstants = new HashMap();
        Class cClass = java.awt.event.KeyEvent.class;
		java.lang.reflect.Field fields[] = cClass.getFields();
		String name;
		Integer value;
		for ( int i = 0; i < fields.length; i++ ) {
			name = fields[i].getName();
			if ( name.startsWith( "VK_" ) ) { //$NON-NLS-1$
				name = name.substring( 3 );
				try {
					value = new Integer( fields[i].getInt( fields[i] ) );
					constantsToValue.put( name, value );
					valueToConstants.put( value, name );
				} catch ( Exception e ) { }
			}
		}
    }
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void initialize() {
		if (!initialized) {
			setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

			GridBagLayout gb = new java.awt.GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			setLayout(gb);
			c.insets = new Insets(5, 5, 5, 5);
//			c.gridx = 0;
//			c.gridy = 0;
//			c.gridwidth = 2;
//			c.fill = GridBagConstraints.NONE;
//			c.anchor = GridBagConstraints.WEST;
//			JLabel title = new JLabel(VisualBeanInfoMessages.getString("MnemonicPropertyEditor.Title")); //$NON-NLS-1$
//			title.setFont(new Font("Dialog", java.awt.Font.PLAIN, 14)); //$NON-NLS-1$
//			add(title, c);

			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			c.anchor = GridBagConstraints.WEST;
			add(new JLabel(VisualBeanInfoMessages.getString("MnemonicPropertyEditor.PressKey")),c); //$NON-NLS-1$

			c.gridx = 0;
			c.gridy = 1;
			c.gridwidth = 2;
			c.weightx = 1.0;
			c.weighty = 1.0;
			c.fill = GridBagConstraints.BOTH;
			JScrollPane listScroll = new JScrollPane(getConstantsList());
			removeKeyboardActions(listScroll);
			removeKeyboardActions(listScroll.getVerticalScrollBar());
			removeKeyboardActions(listScroll.getHorizontalScrollBar());
			add(listScroll, c);

			c.gridx = 0;
			c.gridy = 2;
			c.gridwidth = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.weightx = 0.0;
			c.weighty = 0.0;
			add(new JLabel(VisualBeanInfoMessages.getString("MnemonicPropertyEditor.Constant")),c); //$NON-NLS-1$

			c.gridx = 1;
			c.gridy = 2;
			c.gridwidth = 1;
			c.weightx = 1.0;
			add(getConstantField(), c);

			c.gridx = 0;
			c.gridy = 3;
			c.gridwidth = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;
			c.weightx = 0.0;
			add(new JLabel(VisualBeanInfoMessages.getString("MnemonicPropertyEditor.Description")),c); //$NON-NLS-1$

			c.gridx = 1;
			c.gridy = 3;
			c.gridwidth = 1;
			c.weightx = 1.0;
			add(getDescriptionField(), c);

			recursiveSetBackground(this, SystemColor.control);
			initialized = true;
		}
		updateSelection();
		getConstantsList().requestFocus();
	}

	/**
	 * This method initializes constantsList
	 * 
	 * @return javax.swing.JList
	 */
	private JList getConstantsList() {
		if(constantsList == null) {
			constantsList = new javax.swing.JList();
			constantsList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			constantsList.setListData( getConstantsToValue().keySet().toArray() );
			constantsList.addListSelectionListener( new ListSelectionListener() {
				public void valueChanged( ListSelectionEvent e ) {
					String constant = (String)getConstantsList().getSelectedValue();
					if ( constant != null ) {
						value = (Integer)getConstantsToValue().get( constant );
						updateFields();
					} else {
						getConstantField().setText( "" ); //$NON-NLS-1$
						getDescriptionField().setText( "" ); //$NON-NLS-1$
					}
				}
			});

            removeKeyboardActions( constantsList );
            
			constantsList.addKeyListener( new KeyAdapter() {
				public void keyPressed( KeyEvent e ) {
					if ( e.getKeyCode() != KeyEvent.VK_ENTER ) {
					    value = new Integer( e.getKeyCode() );
					    updateSelection();
					}
				}
			} );

		}
		return constantsList;
	}
	
	public void updateSelection() {
		getConstantsList().setSelectedValue(
		    getValueToConstants().get( value ), true );
	}
	
	public void updateFields() { 
		getConstantField().setText( "VK_" + getValueToConstants().get( value ) ); //$NON-NLS-1$
		getDescriptionField().setText( KeyEvent.getKeyText( value.intValue() ) );
	}
		
	/**
	 * This method initializes constantField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getConstantField() {
		if(constantField == null) {
			constantField = new javax.swing.JTextField();
			constantField.setText(""); //$NON-NLS-1$
			constantField.setEditable(false);
		}
		return constantField;
	}

	/**
	 * This method initializes descriptionField
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getDescriptionField() {
		if(descriptionField == null) {
			descriptionField = new javax.swing.JTextField();
			descriptionField.setText(""); //$NON-NLS-1$
			descriptionField.setEditable(false);
		}
		return descriptionField;
	}
	
	private void removeKeyboardActions( JComponent c ) {
	    // Get rid of the existing swing key bindings for the component.
        KeyListener existing[] = (KeyListener[])c.getListeners(KeyListener.class);
        for ( int i = 0; i < existing.length; i++ ) {
         	c.removeKeyListener(existing[i]);
        }
        c.resetKeyboardActions();
        c.setInputMap( JComponent.WHEN_FOCUSED, null );
        c.setInputMap( JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT, null );
	}
	
	/**
     * Recursively set this container and every child of the container's
     * background color to the given color.
     * Note: ignores setting background of JList components
     * @param parent the starting container to set colors
     * @param bgColor the background color to set to
     */
	private void recursiveSetBackground( Container parent, Color bgColor ) {
		if (parent instanceof JList) {
			return;
		}
		
		parent.setBackground( bgColor );
		
		Component[] children = parent.getComponents();
		for ( int i = 0; i < children.length; i++ ) {
			if ( children[i] instanceof Container ) {
				recursiveSetBackground( (Container)children[i], bgColor );
			} else {
				children[i].setBackground( bgColor );
			}
		}
	}
			
	public String getAsText() {
		if ( value.intValue() == KeyEvent.VK_UNDEFINED ) {
			return ""; //$NON-NLS-1$
		} else {
		    return KeyEvent.getKeyText( value.intValue() );
		}
	}
	
	public String getJavaInitializationString() {
	    return "java.awt.event.KeyEvent.VK_" + getValueToConstants().get( value ); //$NON-NLS-1$
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue( Object o ) {
		if ( o instanceof Integer ) {
			value = (Integer)o;
		}
	}

}
