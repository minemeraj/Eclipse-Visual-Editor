/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JFCConstants.java,v $
 *  $Revision: 1.13 $  $Date: 2006-02-19 01:32:34 $ 
 */

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.*;


/**
 * URI's for important features in EMF within a class.
 * @version 	1.0
 * @author
 */
public class JFCConstants {
	public static final URI
		SF_COMPONENT_VISIBLE,	// java.awt.Component, visible
		SF_COMPONENT_LOCATION,	// java.awt.Component, location
		SF_COMPONENT_SIZE,		// java.awt.Component, size
		SF_COMPONENT_BOUNDS,	// java.awt.Component, bounds
		SF_COMPONENT_NAME,		// java.awt.Component, name
		
		SF_CONSTRAINT_COMPONENT,	// containerVisuals/ConstraintComponent, component
		SF_CONSTRAINT_CONSTRAINT,	// containerVisuals/ConstraintComponent, constraint
		
		SF_CONTAINER_COMPONENTS,	// java.awt.Container, components
		SF_CONTAINER_LAYOUT,	// java.awt.Container, layout
		
		SF_DIALOG_MODAL,		// java.awt.Dialog, modal
		
		SF_FLOWLAYOUT_ALIGNMENT,   // java.awt.FlowLayout, alignment
		SF_FLOWLAYOUT_HGAP,    
		SF_FLOWLAYOUT_VGAP,
		
		SF_GRIDLAYOUT_COLUMNS, // java.awt.GridLayout, columns
		SF_GRIDLAYOUT_ROWS,
		SF_GRIDLAYOUT_HGAP,
		SF_GRIDLAYOUT_VGAP,
		
		SF_GRIDBAGCONSTRAINTS_GRIDX,	// java.awt.GridBagConstraints, gridx
		SF_GRIDBAGCONSTRAINTS_GRIDY,
		SF_GRIDBAGCONSTRAINTS_GRIDWIDTH,
		SF_GRIDBAGCONSTRAINTS_GRIDHEIGHT,
		SF_GRIDBAGCONSTRAINTS_WEIGHTX,
		SF_GRIDBAGCONSTRAINTS_WEIGHTY,
		SF_GRIDBAGCONSTRAINTS_ANCHOR,
		SF_GRIDBAGCONSTRAINTS_FILL,
		SF_GRIDBAGCONSTRAINTS_INSETS,
		SF_GRIDBAGCONSTRAINTS_IPADX,
		SF_GRIDBAGCONSTRAINTS_IPADY,
		
		SF_LABEL_TEXT,
		
		SF_JPROGRESSBAR_STRING,
		
		SF_JSCROLLPANE_VIEWPORTVIEW,
		
		SF_JSPLITPANE_LEFTCOMPONENT,
		SF_JSPLITPANE_RIGHTCOMPONENT,
		SF_JSPLITPANE_TOPCOMPONENT,
		SF_JSPLITPANE_BOTTOMCOMPONENT,
		SF_JSPLITPANE_ORIENTATION,
		SF_JSPLITPANE_DIVIDERLOCATION,	
		
		SF_JSLIDER_MAJORTICKS,
		SF_JSLIDER_LABELTABLE,	
		
		SF_JTABBEDPANE_TABS,
		SF_JTABCOMPONENT_COMPONENT,	//jtabbedPaneVisuals/JTabComponent, component
		SF_JTABCOMPONENT_ICON,	//jtabbedPaneVisuals/JTabComponent, tabIcon
		SF_JTABCOMPONENT_TITLE,	//jtabbedPaneVisuals/JTabComponent, tabTitle
		SF_JTABCOMPONENT_TOOLTIP,	//jtabbedPaneVisuals/JTabComponent, tabTooltipText
		
		SF_JTABLE_COLUMNS,
		SF_JTABLE_MODEL,
		SF_JLIST_MODEL,
		SF_JTABLE_AUTOCREATECOLUMNSFROMMODEL,
		SF_TABLECOLUMN_MODELINDEX,
		SF_TABLECOLUMN_HEADERVALUE,
		SF_TABLECOLUMN_PREFERREDWIDTH,
		
		SF_JMENUBAR_MENUS,
		SF_JMENU_ITEMS,
		
		SF_JTOOLBAR_ITEMS,
		
		SF_FRAME_TITLE,
		
		SF_JFRAME_DEFAULTCLOSEPERATION,
		
		CLASS_CONTAINER_CONSTRAINTCOMPONENT,	// containerVisuals/ConstraintComponent
		CLASS_JTABBEDPANE_JTABCOMPONENT;	// jtabbedPaneVisuals/JTabComponent	
		
	public static final String
		POINT_CLASS_NAME,
		DIMENSION_CLASS_NAME,
		RECTANGLE_CLASS_NAME;
		
	static {
		POINT_CLASS_NAME = "java.awt.Point"; //$NON-NLS-1$
		DIMENSION_CLASS_NAME = "java.awt.Dimension"; //$NON-NLS-1$
		RECTANGLE_CLASS_NAME = "java.awt.Rectangle"; //$NON-NLS-1$
	}
		
	static {
		SF_COMPONENT_VISIBLE = URI.createURI("java:/java.awt#Component/visible");		 //$NON-NLS-1$
		SF_COMPONENT_LOCATION = URI.createURI("java:/java.awt#Component/location");		 //$NON-NLS-1$
		SF_COMPONENT_SIZE = URI.createURI("java:/java.awt#Component/size");				 //$NON-NLS-1$
		SF_COMPONENT_BOUNDS = URI.createURI("java:/java.awt#Component/bounds");						 //$NON-NLS-1$
		SF_COMPONENT_NAME = URI.createURI("java:/java.awt#Component/name");						 //$NON-NLS-1$
		SF_CONTAINER_LAYOUT = URI.createURI("java:/java.awt#Container/layout"); //$NON-NLS-1$
		SF_CONTAINER_COMPONENTS = URI.createURI("java:/java.awt#Container/components");		 //$NON-NLS-1$
		SF_CONSTRAINT_COMPONENT = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/java/awt/containerVisuals.ecore#//ConstraintComponent/component"); //$NON-NLS-1$
		SF_CONSTRAINT_CONSTRAINT = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/java/awt/containerVisuals.ecore#//ConstraintComponent/constraint"); //$NON-NLS-1$
		
		SF_DIALOG_MODAL = URI.createURI("java:/java.awt#Dialog/modal");		 //$NON-NLS-1$		
		
		SF_FLOWLAYOUT_ALIGNMENT = URI.createURI("java:/java.awt#FlowLayout/alignment"); //$NON-NLS-1$
		SF_FLOWLAYOUT_HGAP = URI.createURI("java:/java.awt#FlowLayout/Hgap"); //$NON-NLS-1$
		SF_FLOWLAYOUT_VGAP = URI.createURI("java:/java.awt#FlowLayout/Vgap"); //$NON-NLS-1$
				
		SF_GRIDBAGCONSTRAINTS_GRIDX = URI.createURI("java:/java.awt#GridBagConstraints/gridx"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_GRIDY = URI.createURI("java:/java.awt#GridBagConstraints/gridy"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_GRIDWIDTH = URI.createURI("java:/java.awt#GridBagConstraints/gridwidth"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_GRIDHEIGHT = URI.createURI("java:/java.awt#GridBagConstraints/gridheight"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_WEIGHTX = URI.createURI("java:/java.awt#GridBagConstraints/weightx"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_WEIGHTY = URI.createURI("java:/java.awt#GridBagConstraints/weighty"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_FILL = URI.createURI("java:/java.awt#GridBagConstraints/fill"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_INSETS = URI.createURI("java:/java.awt#GridBagConstraints/insets"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_IPADX = URI.createURI("java:/java.awt#GridBagConstraints/ipadx"); //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_IPADY = URI.createURI("java:/java.awt#GridBagConstraints/ipady");																				 //$NON-NLS-1$
		SF_GRIDBAGCONSTRAINTS_ANCHOR = URI.createURI("java:/java.awt#GridBagConstraints/anchor");																				 //$NON-NLS-1$
		
		SF_GRIDLAYOUT_COLUMNS = URI.createURI("java:/java.awt#GridLayout/columns"); //$NON-NLS-1$
		SF_GRIDLAYOUT_ROWS = URI.createURI("java:/java.awt#GridLayout/rows"); //$NON-NLS-1$
		SF_GRIDLAYOUT_HGAP = URI.createURI("java:/java.awt#GridLayout/hgap"); //$NON-NLS-1$
		SF_GRIDLAYOUT_VGAP = URI.createURI("java:/java.awt#GridLayout/vgap"); //$NON-NLS-1$
		
		SF_LABEL_TEXT = URI.createURI("java:/java.awt#Label/text");																				 //$NON-NLS-1$		
		
		SF_JPROGRESSBAR_STRING = URI.createURI("java:/javax.swing#JProgressBar/string");		 //$NON-NLS-1$
		
		SF_JSCROLLPANE_VIEWPORTVIEW = URI.createURI("java:/javax.swing#JScrollPane/viewportView"); //$NON-NLS-1$
		
		SF_JSPLITPANE_LEFTCOMPONENT = URI.createURI("java:/javax.swing#JSplitPane/leftComponent");		 //$NON-NLS-1$
		SF_JSPLITPANE_RIGHTCOMPONENT = URI.createURI("java:/javax.swing#JSplitPane/rightComponent"); //$NON-NLS-1$
		SF_JSPLITPANE_TOPCOMPONENT = URI.createURI("java:/javax.swing#JSplitPane/topComponent"); //$NON-NLS-1$
		SF_JSPLITPANE_BOTTOMCOMPONENT = URI.createURI("java:/javax.swing#JSplitPane/bottomComponent"); //$NON-NLS-1$
		SF_JSPLITPANE_ORIENTATION = URI.createURI("java:/javax.swing#JSplitPane/orientation");		 //$NON-NLS-1$
		SF_JSPLITPANE_DIVIDERLOCATION = URI.createURI("java:/javax.swing#JSplitPane/dividerLocation");		 //$NON-NLS-1$
		
		SF_JSLIDER_MAJORTICKS = URI.createURI("java:/javax.swing#JSlider/majorTickSpacing");		 //$NON-NLS-1$
		SF_JSLIDER_LABELTABLE= URI.createURI("java:/javax.swing#JSlider/labelTable");		 //$NON-NLS-1$
				
		SF_JTABBEDPANE_TABS = URI.createURI("java:/javax.swing#JTabbedPane/tabs"); //$NON-NLS-1$
		
		SF_JTABCOMPONENT_COMPONENT = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/javax/swing/jtabbedPaneVisuals.ecore#//JTabComponent/component");		 //$NON-NLS-1$
		SF_JTABCOMPONENT_ICON = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/javax/swing/jtabbedPaneVisuals.ecore#//JTabComponent/tabIcon"); //$NON-NLS-1$
		SF_JTABCOMPONENT_TITLE = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/javax/swing/jtabbedPaneVisuals.ecore#//JTabComponent/tabTitle");		 //$NON-NLS-1$
		SF_JTABCOMPONENT_TOOLTIP = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/javax/swing/jtabbedPaneVisuals.ecore#//JTabComponent/tabTooltipText");		 //$NON-NLS-1$
		
		SF_JLIST_MODEL = URI.createURI("java:/javax.swing#JList/model"); //$NON-NLS-1$

		SF_JTABLE_MODEL = URI.createURI("java:/javax.swing#JTable/model"); //$NON-NLS-1$
		SF_JTABLE_COLUMNS = URI.createURI("java:/javax.swing#JTable/columns"); //$NON-NLS-1$
		SF_JTABLE_AUTOCREATECOLUMNSFROMMODEL = URI.createURI("java:/javax.swing#JTable/autoCreateColumnsFromModel");	//$NON-NLS-1$
		
		SF_TABLECOLUMN_MODELINDEX = URI.createURI("java:/javax.swing.table#TableColumn/modelIndex");	//$NON-NLS-1$		
		SF_TABLECOLUMN_HEADERVALUE = URI.createURI("java:/javax.swing.table#TableColumn/headerValue");	//$NON-NLS-1$
		SF_TABLECOLUMN_PREFERREDWIDTH = URI.createURI("java:/javax.swing.table#TableColumn/preferredWidth");	//$NON-NLS-1$
		SF_FRAME_TITLE = URI.createURI("java:/java.awt#Frame/title"); //$NON-NLS-1$

		SF_JMENUBAR_MENUS = URI.createURI("java:/javax.swing#JMenuBar/menus"); //$NON-NLS-1$
		SF_JMENU_ITEMS = URI.createURI("java:/javax.swing#JMenu/items"); //$NON-NLS-1$
		
		SF_JTOOLBAR_ITEMS = URI.createURI("java:/javax.swing#JToolBar/items"); //$NON-NLS-1$
		
		SF_JFRAME_DEFAULTCLOSEPERATION = URI.createURI("java:/javax.swing#JFrame/defaultCloseOperation"); //$NON-NLS-1$
		
		CLASS_CONTAINER_CONSTRAINTCOMPONENT = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/java/awt/containerVisuals.ecore#//ConstraintComponent");				 //$NON-NLS-1$
		CLASS_JTABBEDPANE_JTABCOMPONENT = URI.createURI("platform:/plugin/org.eclipse.ve.jfc/overrides/javax/swing/jtabbedPaneVisuals.ecore#//JTabComponent");						 //$NON-NLS-1$
	}
	
	public static EFactory getFactory(EClass aClass) {
		return aClass.getEPackage().getEFactoryInstance();
	}
}
