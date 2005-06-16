/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: ColorPropertyEditor.java,v $
 *  $Revision: 1.7 $  $Date: 2005-06-16 17:46:03 $ 
 */

import java.awt.*;
import java.text.MessageFormat;

import javax.swing.*;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeListener;

public class ColorPropertyEditor extends JPanel {
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -8120038443381231652L;

	private boolean initialized = false;

	private JColorChooser colorChooser = null;
	private NamedColorChooserPanel namedPanel = null;
	
	private boolean previewEnabled = true;
	private JPanel emptyPanel = new JPanel();

	private Color value = null;
	
	
	// index of the Named Colors tab in the chooser's JTabbedPane
	private int namedTabPosition = 0;
	
	public void addChangeListener( ChangeListener listener ) {
		getColorChooser().getSelectionModel().addChangeListener( listener );
	}
	
	public void removeChangeListener( ChangeListener listener ) {
		getColorChooser().getSelectionModel().removeChangeListener( listener );
	}

	private JColorChooser getColorChooser() {
		if (colorChooser == null) {
			Color oldSelected = (Color)UIManager.get( "TabbedPane.selected" ); //$NON-NLS-1$
			UIManager.put("TabbedPane.selected", java.awt.SystemColor.control); //$NON-NLS-1$
			
			if ( value != null ) {
				colorChooser = new JColorChooser( value );
			} else {
  			    colorChooser = new JColorChooser();
			}
			
			UIManager.put("TabbedPane.selected", oldSelected); //$NON-NLS-1$

            // a bit of juggling to get the Named Colors tab to be
            // the first tab in the chooser
			AbstractColorChooserPanel[] defaults =
				colorChooser.getChooserPanels();
			AbstractColorChooserPanel[] panels =
				new AbstractColorChooserPanel[defaults.length + 1];
			panels[namedTabPosition] = getNamedPanel();
			System.arraycopy(defaults, 0, panels, 1, defaults.length);
			colorChooser.setChooserPanels(panels);
			getNamedPanel().updateListSelection( value );
		}
		return colorChooser;
	}
	
	private NamedColorChooserPanel getNamedPanel() {
         if ( namedPanel == null ) {
         	namedPanel = new NamedColorChooserPanel();
         }
         return namedPanel;
	}				

	public void initialize() {
		if (!initialized) {
			// initialize the color chooser
			// could be in line below, but this is clearer			
			
			// Horrible hack because the Color Chooser with the GTK L&F doesn't like added chooser panels
			// See Sun bug 5027338
			LookAndFeel lnf = null;
			if (UIManager.getLookAndFeel().getClass().getName().equals("com.sun.java.swing.plaf.gtk.GTKLookAndFeel")) { //$NON-NLS-1$
				lnf = UIManager.getLookAndFeel();
				try {
					UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			getColorChooser();
			autoSelectTab();
			if (lnf != null) {
				try {
					UIManager.setLookAndFeel(lnf);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			this.setLayout(new BorderLayout());
			this.add(colorChooser, BorderLayout.CENTER);
			recursiveSetBackground(this, java.awt.SystemColor.control);
			
			initialized = true;
		}
	}
	
	public void autoSelectTab() { 
        // manipulate the ColorChooser's TabbedPane to show the tab
        // for the currently selected value
	    JTabbedPane tp = findChildTabbedPane( getColorChooser() );
		if ( value == null || 
		     NamedColorChooserPanel.isBasicColor( value ) || 
		     NamedColorChooserPanel.isSystemColor( value ) ) {
		    // set the initial tab to the Named Colors tab
			tp.setSelectedIndex( namedTabPosition );
		} else {
			// set the initial tab to the RGB tab
			tp.setSelectedIndex( tp.getTabCount() - 1 );
		}
	}		

	public String getAsText() {
		if (value == null) {
			return ""; //$NON-NLS-1$
		} else if (NamedColorChooserPanel.isBasicColor(value)) {
			return MessageFormat.format(VisualBeanInfoMessages.getString("ColorEditor.Color(String)"), //$NON-NLS-1$
			new Object[] {NamedColorChooserPanel.getColorName(value)});
		} else if (NamedColorChooserPanel.isSystemColor(value)) {
			return MessageFormat.format(VisualBeanInfoMessages.getString("ColorEditor.SystemColor(String)"), //$NON-NLS-1$
			new Object[] {NamedColorChooserPanel.getColorName(value)});
		} else {
			return MessageFormat.format(VisualBeanInfoMessages.getString("ColorEditor.Color(int,int,int)"), //$NON-NLS-1$
			new Object[] {
				new Integer(value.getRed()),
				new Integer(value.getGreen()),
				new Integer(value.getBlue())});
		}
	}

	public String getJavaInitializationString() {
		if (value == null) {
			return "null"; //$NON-NLS-1$
		} else if (NamedColorChooserPanel.isBasicColor(value)) {
			return getNamedPanel().getConstant(value);
		} else if (NamedColorChooserPanel.isSystemColor(value)) {
			return getNamedPanel().getConstant(value);
		} else {
			int r = value.getRed();
			int g = value.getGreen();
			int b = value.getBlue();
			return "new java.awt.Color(" + r + "," + g + "," + b + ")"; //$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
		}
	}

	public Object getValue() {
		if ( colorChooser != null ) {
			value = getColorChooser().getColor();
		} 
		return value;
	}

	public boolean isPaintable() {
		return true;
	}

	public void paintValue(java.awt.Graphics gfx, java.awt.Rectangle box) {
		if (getValue() != null) {
			gfx.drawRect(box.x, box.y, box.width, box.height);
			gfx.setColor((Color) getValue());
			gfx.fillRect(box.x, box.y, box.width, box.height);
		}
	}

	public void setValue(Object newValue) {
		if (newValue instanceof Color) {
			value = (Color)newValue;
			if ( colorChooser != null ) {
				getColorChooser().setColor( value );
				getNamedPanel().updateListSelection( value );
			}
		}
	}
	
	public void setPreviewEnabled( boolean doPreview ) {
		if ( doPreview ) {
			// returns preview panel to the default;
		    getColorChooser().setPreviewPanel(null);
		} else {
			getColorChooser().setPreviewPanel(emptyPanel);
		}
		previewEnabled = doPreview;
	}
	
	public boolean getPreviewEnabled() {
		return previewEnabled;
	}
			
    /**
     * Recursively set this container and every child of the container's
     * background color to the given color.
     * Note: ignores setting background of JTextComponent and JList components
     * @param parent the starting container to set colors
     * @param bgColor the background color to set to
     */
	private void recursiveSetBackground( Container parent, Color bgColor ) {
		if ((parent instanceof JList) ||
		    (parent instanceof javax.swing.text.JTextComponent)) {
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
	
	/**
	 * Recursively search through a container's children searching for the first
	 * JTabbedPane, and return it.
	 * @param parent the starting point of the search
	 * @return the child JTabbedPane if there is one, null otherwise
	 */
	private JTabbedPane findChildTabbedPane( Container parent ) {
		JTabbedPane retval = null;
		
		if ( parent instanceof JTabbedPane ) {
			return (JTabbedPane)parent;
		}
		
		Component[] children = parent.getComponents();
		for ( int i = 0; i < children.length && retval == null; i++ ) {
			if ( children[i] instanceof Container ) {
				retval = findChildTabbedPane( (Container)children[i] );
			}
		}
		return retval;
	}		 
}
