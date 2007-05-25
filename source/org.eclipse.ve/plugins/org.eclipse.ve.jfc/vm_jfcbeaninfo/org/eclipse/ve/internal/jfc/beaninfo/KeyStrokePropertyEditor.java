/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.text.MessageFormat;

/*
 *  $RCSfile: KeyStrokePropertyEditor.java,v $
 *  $Revision: 1.5 $  $Date: 2007-05-25 04:19:32 $ 
 */
public class KeyStrokePropertyEditor extends JPanel {
	
     /**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -4909155071277619589L;
	private JList constantsList = null;
     private JTextField constantField = null;
     private JTextField descriptionField = null;
     private JCheckBox altCheck = null;
     private JCheckBox controlCheck = null;
     private JCheckBox metaCheck = null;
     private JCheckBox shiftCheck = null;
     private ButtonGroup actionGroup = null;
     private JRadioButton pressedRadio = null;
     private JRadioButton releasedRadio = null;
     
     private static TreeMap constantsToValue = null;
     private static HashMap valueToConstants = null;
     
     private boolean initialized = false;
    
     private Integer keyValue;
     private int modifiersValue;
     private boolean releasedValue = false;
     

     public KeyStrokePropertyEditor() {
     	keyValue = new Integer( KeyEvent.VK_UNDEFINED );
     	
     	// This will ensure the constants list will get focus when the property
     	// editor window activated
     	this.addAncestorListener( new AncestorListener() {
     		public void ancestorAdded( AncestorEvent e ) {
  		   	    Container parent = e.getAncestorParent();
   			    if ( parent instanceof JComponent ) {
   				    Container topParent = ((JComponent)parent).getTopLevelAncestor();
   				    if ( topParent instanceof Window ) {
			            ((Window)topParent).addWindowListener( new WindowAdapter() {
						    public void windowOpened( WindowEvent e ) {
						        getConstantsList().requestFocus();
						    }
						    public void windowActivated( WindowEvent e ) {
							    getConstantsList().requestFocus();
						    }
					     } );
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
            this.setLayout( new BorderLayout( 5, 5 ) );
            this.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
            
//			JLabel title = new JLabel(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Title")); //$NON-NLS-1$
//		    title.setFont(new Font("Dialog", java.awt.Font.PLAIN, 14)); //$NON-NLS-1$
//		    this.add( title, BorderLayout.NORTH );
		    
		    this.add( getKeyPanel(), BorderLayout.CENTER );

            this.add( getModifiersAndActionPanel(), BorderLayout.SOUTH );
			
			recursiveSetBackground(this, SystemColor.control);
			initialized = true;
		}
		updateSelection();
		setModifiers( modifiersValue );
		getReleasedRadio().setSelected( releasedValue );
		getPressedRadio().setSelected( ! releasedValue );
		
		getConstantsList().requestFocus();
	}
		
	private JPanel getKeyPanel() {
		JPanel keyPanel = new JPanel();
		keyPanel.setBorder(
			BorderFactory.createCompoundBorder(
			    BorderFactory.createTitledBorder(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.KeyTitle")), //$NON-NLS-1$
			    BorderFactory.createEmptyBorder(5,5,5,5))); 

		GridBagLayout gb = new java.awt.GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		keyPanel.setLayout(gb);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.WEST;
		keyPanel.add(new JLabel(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.PressKey")), c); //$NON-NLS-1$

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(5, 0, 5, 0);
		JScrollPane listScroll = new JScrollPane(getConstantsList());
		removeKeyboardActions(listScroll);
		removeKeyboardActions(listScroll.getVerticalScrollBar());
		removeKeyboardActions(listScroll.getHorizontalScrollBar());
		keyPanel.add(listScroll, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.insets = new Insets(5, 0, 5, 5);
		keyPanel.add(new JLabel(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Constant")), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.insets = new Insets(5, 5, 5, 0);
		keyPanel.add(getConstantField(), c);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.WEST;
		c.weightx = 0.0;
		c.insets = new Insets(5, 0, 5, 5);
		keyPanel.add(new JLabel(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Description")), c); //$NON-NLS-1$

		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.insets = new Insets(5, 5, 5, 0);
		keyPanel.add(getDescriptionField(), c);
		
		return keyPanel;
	}
	
	private JPanel getModifiersAndActionPanel() {
		JPanel maPanel = new JPanel();
		maPanel.setLayout( new GridLayout(1, 2, 5, 0) );
		
		JPanel modPanel = new JPanel();
		modPanel.setBorder( BorderFactory.createCompoundBorder(
			    BorderFactory.createTitledBorder(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.ModifiersTitle")), //$NON-NLS-1$
			    BorderFactory.createEmptyBorder(0,5,5,0)));
		modPanel.setLayout( new GridLayout( 2, 2, 5, 5 ) );
		modPanel.add(getAltCheck());
		modPanel.add(getControlCheck());
		modPanel.add(getMetaCheck());
		modPanel.add(getShiftCheck());
		
        maPanel.add( modPanel );
        
        JPanel actionPanel = new JPanel();
        actionPanel.setBorder( BorderFactory.createCompoundBorder(
			    BorderFactory.createTitledBorder(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.ActionTitle")), //$NON-NLS-1$
			    BorderFactory.createEmptyBorder(0,5,5,0)));
   		actionPanel.setLayout( new GridLayout( 2, 1, 5, 5 ) );
	    actionPanel.add(getPressedRadio(), null);
		actionPanel.add(getReleasedRadio(), null);
		actionGroup = new ButtonGroup();
		actionGroup.add( getPressedRadio() );
		actionGroup.add( getReleasedRadio() );
		
		maPanel.add( actionPanel );		
		
		return maPanel;
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
						keyValue = (Integer)getConstantsToValue().get( constant );
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
					if ( ! isModifierKey( e.getKeyCode() ) && e.getKeyCode() != KeyEvent.VK_ENTER ) {
					    getConstantsList().setSelectedValue(
					        getValueToConstants().get( new Integer( e.getKeyCode() ) ), 
					        true );
					    setModifiers( 0 );
					}
				}
				
				public void keyTyped( KeyEvent e ) {
					setModifiers( e.getModifiers() );				
				}
			} );

		}
		return constantsList;
	}
	
	public void updateSelection() {
		getConstantsList().setSelectedValue(
		    getValueToConstants().get( keyValue ), true );
	}
	
	public void updateFields() { 
		getConstantField().setText( "VK_" + getValueToConstants().get( keyValue ) ); //$NON-NLS-1$
		getDescriptionField().setText( KeyEvent.getKeyText( keyValue.intValue() ) );
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
	
		/**
	 * This method initializes altCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getAltCheck() {
		if(altCheck == null) {
			altCheck = new javax.swing.JCheckBox();
			altCheck.setText(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Alt_Modifier")); //$NON-NLS-1$
		}
		return altCheck;
	}
	/**
	 * This method initializes controlCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getControlCheck() {
		if(controlCheck == null) {
			controlCheck = new javax.swing.JCheckBox();
			controlCheck.setText(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Control_Modifier")); //$NON-NLS-1$
		}
		return controlCheck;
	}
	/**
	 * This method initializes metaCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getMetaCheck() {
		if(metaCheck == null) {
			metaCheck = new javax.swing.JCheckBox();
			metaCheck.setText(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Meta_Modifier")); //$NON-NLS-1$
		}
		return metaCheck;
	}
	/**
	 * This method initializes shiftCheck
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getShiftCheck() {
		if(shiftCheck == null) {
			shiftCheck = new javax.swing.JCheckBox();
			shiftCheck.setText(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Shift_Modifier")); //$NON-NLS-1$
		}
		return shiftCheck;
	}
	
		/**
	 * This method initializes pressedRadio
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getPressedRadio() {
		if(pressedRadio == null) {
			pressedRadio = new javax.swing.JRadioButton();
			pressedRadio.setText(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Pressed_Action")); //$NON-NLS-1$
			pressedRadio.setSelected( !releasedValue );
		}
		return pressedRadio;
	}
	/**
	 * This method initializes releasedRadio
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getReleasedRadio() {
		if(releasedRadio == null) {
			releasedRadio = new javax.swing.JRadioButton();
			releasedRadio.setText(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Released_Action")); //$NON-NLS-1$
			releasedRadio.setSelected( releasedValue );
		}
		return releasedRadio;
	}
	
	private void removeKeyboardActions( JComponent c ) {
	    // Get rid of the existing swing key bindings for the component.
        KeyListener existing[] = c.getListeners(KeyListener.class);
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
		if ( keyValue.intValue() == KeyEvent.VK_UNDEFINED ) {
			return ""; //$NON-NLS-1$
		} else {
			String modifiersText = KeyEvent.getKeyModifiersText(getModifiers());
			String spacer = ( modifiersText.length() > 0 ) ? VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Spacer") : ""; //$NON-NLS-1$ //$NON-NLS-2$
			String keyText = KeyEvent.getKeyText(keyValue.intValue());
			String actionText = (getReleasedValue() ? VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Released") :  //$NON-NLS-1$
		        	                             VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.Pressed") ); //$NON-NLS-1$
		        	                             
		    return MessageFormat.format(VisualBeanInfoMessages.getString("KeyStrokePropertyEditor.DisplayName(modifiers,spacer,key,action)"), //$NON-NLS-1$
		        new Object[] { modifiersText, spacer, keyText, actionText } );
		}
	}
	
	/**
	 * Method getJavaInitializationString.
	 */
	public String getJavaInitializationString() {
		String constant = "java.awt.event.KeyEvent.VK_" + getValueToConstants().get( keyValue ); //$NON-NLS-1$
		StringBuffer value =
			new StringBuffer("javax.swing.KeyStroke.getKeyStroke("); //$NON-NLS-1$
		value.append(constant);
		value.append(", "); //$NON-NLS-1$
		
		int mods = getModifiers();
		
		boolean first = true;
		if ( ( mods & java.awt.Event.ALT_MASK ) != 0 ) {
			if (!first) {
				value.append(" | "); //$NON-NLS-1$
			} else {
				first = false;
			}
			value.append("java.awt.Event.ALT_MASK"); //$NON-NLS-1$
		}
		if ( ( mods & java.awt.Event.CTRL_MASK ) != 0 ) {
			if (!first) {
				value.append(" | "); //$NON-NLS-1$
			} else {
				first = false;
			}
			value.append("java.awt.Event.CTRL_MASK"); //$NON-NLS-1$
		}
		if ( ( mods & java.awt.Event.META_MASK ) != 0 ) {
			if (!first) {
				value.append(" | "); //$NON-NLS-1$
			} else {
				first = false;
			}
			value.append("java.awt.Event.META_MASK"); //$NON-NLS-1$
		}
		if ( ( mods & java.awt.Event.SHIFT_MASK ) != 0 ) {
			if (!first) {
				value.append(" | "); //$NON-NLS-1$
			} else {
				first = false;
			}
			value.append("java.awt.Event.SHIFT_MASK"); //$NON-NLS-1$
		}
		if ( first ) {
			value.append( "0" ); //$NON-NLS-1$
		}

		value.append(", "); //$NON-NLS-1$
		value.append(getReleasedValue() ? "true" : "false"); //$NON-NLS-1$ //$NON-NLS-2$
		value.append(")"); //$NON-NLS-1$

		return value.toString();
	}

	private boolean isModifierKey( int keyCode ) {
		return ( keyCode == KeyEvent.VK_ALT ||
		          keyCode == KeyEvent.VK_CONTROL ||
		          keyCode == KeyEvent.VK_META ||
		          keyCode == KeyEvent.VK_SHIFT );
	}
	
	private void setModifiers( int mod ) {
		getAltCheck().setSelected(      ( mod & java.awt.Event.ALT_MASK ) != 0 );
		getControlCheck().setSelected(  ( mod & java.awt.Event.CTRL_MASK ) != 0 );
		getMetaCheck().setSelected(     ( mod & java.awt.Event.META_MASK ) != 0 );
		getShiftCheck().setSelected(    ( mod & java.awt.Event.SHIFT_MASK ) != 0 );
	}
	
	private int getModifiers() {
		if ( initialized ) {
		    modifiersValue = 0;
  		    if ( getAltCheck().isSelected() )     { modifiersValue |= java.awt.Event.ALT_MASK; }
		    if ( getControlCheck().isSelected() ) { modifiersValue |= java.awt.Event.CTRL_MASK; }
		    if ( getMetaCheck().isSelected() )    { modifiersValue |= java.awt.Event.META_MASK; }
		    if ( getShiftCheck().isSelected() )   { modifiersValue |= java.awt.Event.SHIFT_MASK; }
		}		
		return modifiersValue;
	}
	
	private boolean getReleasedValue() {
		if ( initialized ) {
			releasedValue = getReleasedRadio().isSelected();
		}
		return releasedValue;
	}
	
	public Object getValue() {
		return KeyStroke.getKeyStroke( keyValue.intValue(), getModifiers(), getReleasedValue() );
	}
	
	public void setValue( Object o ) {
		if ( o != null && o instanceof KeyStroke ) {
			KeyStroke ks = (KeyStroke)o;
			keyValue = new Integer( ks.getKeyCode() );
			modifiersValue = ks.getModifiers();
			releasedValue = ks.isOnKeyRelease();
		}
	}
}
