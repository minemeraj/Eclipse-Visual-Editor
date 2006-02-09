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
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.util.URI;

public class SWTConstants {
	
	public static final URI
		SF_CONTROL_BOUNDS,
		SF_CONTROL_LOCATION,
		SF_CONTROL_SIZE,
		SF_CONTROL_VISIBLE,
		SF_CONTROL_LAYOUTDATA,	
		SF_CONTROL_MENU,
		SF_DECORATIONS_TEXT,		
		SF_DECORATIONS_MENU_BAR,
		SF_COMPOSITE_LAYOUT,
		SF_COMPOSITE_CONTROLS,
		SF_SCROLLEDCOMPOSITE_CONTENT,
		
		SF_CBANNER_LEFT,
		SF_CBANNER_RIGHT,
		SF_CBANNER_BOTTOM,
		SF_CBANNER_SIMPLE,
		
		SF_VIEWFORM_TOPLEFT,
		SF_VIEWFORM_TOPRIGHT,
		SF_VIEWFORM_TOPCENTER,
		SF_VIEWFORM_CONTENT,
		
		SF_FILL_LAYOUT_MARGIN_HEIGHT,
		SF_FILL_LAYOUT_MARGIN_WIDTH,
		SF_FILL_LAYOUT_SPACING,
		SF_FILL_LAYOUT_TYPE,
		
		SF_FORM_LAYOUT_SPACING,
		SF_FORM_LAYOUT_MARGIN_HEIGHT,
		SF_FORM_LAYOUT_MARGIN_WIDTH,
		
		SF_GRID_LAYOUT_NUM_COLUMNS,
		SF_GRID_LAYOUT_MAKE_COLUMNS_EQUAL_WIDTH,
		SF_GRID_LAYOUT_HORIZONTAL_SPACING,
		SF_GRID_LAYOUT_VERTICAL_SPACING,
		SF_GRID_LAYOUT_MARGIN_HEIGHT,
		SF_GRID_LAYOUT_MARGIN_WIDTH,
		
		SF_GRID_DATA_HORIZONTAL_ALIGN,
		SF_GRID_DATA_VERTICAL_ALIGN,
		SF_GRID_DATA_HORIZONTAL_GRAB,
		SF_GRID_DATA_VERTICAL_GRAB,
		SF_GRID_DATA_HORIZONTAL_SPAN,
		SF_GRID_DATA_VERTICAL_SPAN,
		SF_GRID_DATA_HEIGHT_HINT,
		SF_GRID_DATA_WIDTH_HINT,
		SF_GRID_DATA_HORIZONTAL_INDENT,
		
		SF_ROW_LAYOUT_FILL,
		SF_ROW_LAYOUT_JUSTIFY,
		SF_ROW_LAYOUT_MARGIN_BOTTOM,
		SF_ROW_LAYOUT_MARGIN_HEIGHT,
		SF_ROW_LAYOUT_MARGIN_LEFT,
		SF_ROW_LAYOUT_MARGIN_RIGHT,
		SF_ROW_LAYOUT_MARGIN_TOP,
		SF_ROW_LAYOUT_MARGIN_WIDTH,
		SF_ROW_LAYOUT_PACK,
		SF_ROW_LAYOUT_SPACING,
		SF_ROW_LAYOUT_TYPE,
		SF_ROW_LAYOUT_WRAP,
		
		SF_TABLE_COLUMNS,
		SF_TABLE_ITEMS,
		SF_TREE_COLUMNS,
		SF_TREE_ITEMS,
		SF_TABFOLDER_ITEMS,
		SF_TABITEM_CONTROL,
		SF_CTABFOLDER_ITEMS,
		SF_CTABITEM_CONTROL,
		SF_COOLBAR_ITEMS,
		SF_COOLITEM_CONTROL,
		SF_TOOLBAR_ITEMS,
		SF_MENU_ITEMS,
		SF_MENUITEM_MENU,
		SF_EXPANDABLECOMPOSITE_CLIENT,
		SF_ITEM_TEXT;
		
	public static final String
		POINT_CLASS_NAME,
		RECTANGLE_CLASS_NAME,
		TABLE_CLASS_NAME;
		
	static {
		POINT_CLASS_NAME = "org.eclipse.swt.graphics.Point"; //$NON-NLS-1$
		RECTANGLE_CLASS_NAME = "org.eclipse.swt.graphics.Rectangle"; //$NON-NLS-1$
		TABLE_CLASS_NAME = "org.eclipse.swt.widgets.Table"; //$NON-NLS-1$
	}		
		
	static {
		SF_CONTROL_BOUNDS = URI.createURI("java:/org.eclipse.swt.widgets#Control/bounds");		 //$NON-NLS-1$
		SF_CONTROL_LOCATION = URI.createURI("java:/org.eclipse.swt.widgets#Control/location");		 //$NON-NLS-1$
		SF_CONTROL_SIZE = URI.createURI("java:/org.eclipse.swt.widgets#Control/size");		 //$NON-NLS-1$
		SF_CONTROL_VISIBLE = URI.createURI("java:/org.eclipse.swt.widgets#Control/visible");		 //$NON-NLS-1$		
		SF_CONTROL_LAYOUTDATA = URI.createURI("java:/org.eclipse.swt.widgets#Control/layoutData");		 //$NON-NLS-1$
		SF_CONTROL_MENU = URI.createURI("java:/org.eclipse.swt.widgets#Control/menu");	 //$NON-NLS-1$
		SF_COMPOSITE_LAYOUT = URI.createURI("java:/org.eclipse.swt.widgets#Composite/layout");		 //$NON-NLS-1$
		SF_COMPOSITE_CONTROLS = URI.createURI("java:/org.eclipse.swt.widgets#Composite/controls");		 //$NON-NLS-1$
		SF_SCROLLEDCOMPOSITE_CONTENT = URI.createURI("java:/org.eclipse.swt.custom#ScrolledComposite/content"); //$NON-NLS-1$
		
		SF_CBANNER_LEFT = URI.createURI("java:/org.eclipse.swt.custom#CBanner/left"); //$NON-NLS-1$
		SF_CBANNER_RIGHT = URI.createURI("java:/org.eclipse.swt.custom#CBanner/right"); //$NON-NLS-1$
		SF_CBANNER_BOTTOM = URI.createURI("java:/org.eclipse.swt.custom#CBanner/bottom"); //$NON-NLS-1$
		SF_CBANNER_SIMPLE = URI.createURI("java:/org.eclipse.swt.custom#CBanner/simple"); //$NON-NLS-1$
		
		SF_VIEWFORM_TOPLEFT = URI.createURI("java:/org.eclipse.swt.custom#ViewForm/topLeft"); //$NON-NLS-1$
		SF_VIEWFORM_TOPRIGHT = URI.createURI("java:/org.eclipse.swt.custom#ViewForm/topRight"); //$NON-NLS-1$
		SF_VIEWFORM_TOPCENTER = URI.createURI("java:/org.eclipse.swt.custom#ViewForm/topCenter"); //$NON-NLS-1$
		SF_VIEWFORM_CONTENT = URI.createURI("java:/org.eclipse.swt.custom#ViewForm/content"); //$NON-NLS-1$
		
		SF_DECORATIONS_TEXT = URI.createURI("java:/org.eclipse.swt.widgets#Decorations/text");		 //$NON-NLS-1$
		
		SF_FILL_LAYOUT_MARGIN_HEIGHT = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/marginHeight");    //$NON-NLS-1$
		SF_FILL_LAYOUT_MARGIN_WIDTH = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/marginWidth");    //$NON-NLS-1$
		SF_FILL_LAYOUT_SPACING = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/spacing");    //$NON-NLS-1$
		SF_FILL_LAYOUT_TYPE = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/type");    //$NON-NLS-1$
		
		SF_FORM_LAYOUT_SPACING = URI.createURI("java:/org.eclipse.swt.layout#FormLayout/spacing");    //$NON-NLS-1$
		SF_FORM_LAYOUT_MARGIN_HEIGHT = URI.createURI("java:/org.eclipse.swt.layout#FormLayout/marginHeight");    //$NON-NLS-1$
		SF_FORM_LAYOUT_MARGIN_WIDTH = URI.createURI("java:/org.eclipse.swt.layout#FormLayout/marginWidth");    //$NON-NLS-1$
		
		SF_GRID_LAYOUT_NUM_COLUMNS = URI.createURI("java:/org.eclipse.swt.layout#GridLayout/numColumns");    //$NON-NLS-1$
		SF_GRID_LAYOUT_MAKE_COLUMNS_EQUAL_WIDTH = URI.createURI("java:/org.eclipse.swt.layout#GridLayout/makeColumnsEqualWidth");    //$NON-NLS-1$
		SF_GRID_LAYOUT_HORIZONTAL_SPACING = URI.createURI("java:/org.eclipse.swt.layout#GridLayout/horizontalSpacing");    //$NON-NLS-1$
		SF_GRID_LAYOUT_VERTICAL_SPACING = URI.createURI("java:/org.eclipse.swt.layout#GridLayout/verticalSpacing");    //$NON-NLS-1$
		SF_GRID_LAYOUT_MARGIN_HEIGHT = URI.createURI("java:/org.eclipse.swt.layout#GridLayout/marginHeight");    //$NON-NLS-1$
		SF_GRID_LAYOUT_MARGIN_WIDTH = URI.createURI("java:/org.eclipse.swt.layout#GridLayout/marginWidth");    //$NON-NLS-1$
		
		SF_GRID_DATA_HORIZONTAL_ALIGN = URI.createURI("java:/org.eclipse.swt.layout#GridData/horizontalAlignment");    //$NON-NLS-1$
		SF_GRID_DATA_VERTICAL_ALIGN = URI.createURI("java:/org.eclipse.swt.layout#GridData/verticalAlignment");    //$NON-NLS-1$
		SF_GRID_DATA_HORIZONTAL_GRAB = URI.createURI("java:/org.eclipse.swt.layout#GridData/grabExcessHorizontalSpace");    //$NON-NLS-1$
		SF_GRID_DATA_VERTICAL_GRAB = URI.createURI("java:/org.eclipse.swt.layout#GridData/grabExcessVerticalSpace");    //$NON-NLS-1$
		SF_GRID_DATA_HORIZONTAL_SPAN = URI.createURI("java:/org.eclipse.swt.layout#GridData/horizontalSpan");    //$NON-NLS-1$
		SF_GRID_DATA_VERTICAL_SPAN = URI.createURI("java:/org.eclipse.swt.layout#GridData/verticalSpan");    //$NON-NLS-1$
		SF_GRID_DATA_HEIGHT_HINT = URI.createURI("java:/org.eclipse.swt.layout#GridData/heightHint");    //$NON-NLS-1$
		SF_GRID_DATA_WIDTH_HINT = URI.createURI("java:/org.eclipse.swt.layout#GridData/widthHint");    //$NON-NLS-1$
		SF_GRID_DATA_HORIZONTAL_INDENT = URI.createURI("java:/org.eclipse.swt.layout#GridData/horizontalIndent");    //$NON-NLS-1$
		
		SF_ROW_LAYOUT_FILL = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/fill");    //$NON-NLS-1$
		SF_ROW_LAYOUT_JUSTIFY = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/justify");    //$NON-NLS-1$
		SF_ROW_LAYOUT_MARGIN_BOTTOM = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/marginBottom");    //$NON-NLS-1$
		SF_ROW_LAYOUT_MARGIN_HEIGHT = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/marginHeight");    //$NON-NLS-1$
		SF_ROW_LAYOUT_MARGIN_LEFT = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/marginLeft");    //$NON-NLS-1$
		SF_ROW_LAYOUT_MARGIN_RIGHT = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/marginRight");    //$NON-NLS-1$
		SF_ROW_LAYOUT_MARGIN_TOP = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/marginTop");    //$NON-NLS-1$
		SF_ROW_LAYOUT_MARGIN_WIDTH = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/marginWidth");    //$NON-NLS-1$
		SF_ROW_LAYOUT_PACK = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/pack");    //$NON-NLS-1$
		SF_ROW_LAYOUT_SPACING = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/spacing");    //$NON-NLS-1$
		SF_ROW_LAYOUT_TYPE = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/type");    //$NON-NLS-1$
		SF_ROW_LAYOUT_WRAP = URI.createURI("java:/org.eclipse.swt.layout#RowLayout/wrap");    //$NON-NLS-1$
		
		SF_TABLE_COLUMNS = URI.createURI("java:/org.eclipse.swt.widgets#Table/tableColumns");    //$NON-NLS-1$
		SF_TABLE_ITEMS = URI.createURI("java:/org.eclipse.swt.widgets#Table/tableItems");    //$NON-NLS-1$
		SF_TREE_COLUMNS = URI.createURI("java:/org.eclipse.swt.widgets#Tree/treeColumns");    //$NON-NLS-1$
		SF_TREE_ITEMS = URI.createURI("java:/org.eclipse.swt.widgets#Tree/treeItems");    //$NON-NLS-1$		
		SF_TABFOLDER_ITEMS = URI.createURI("java:/org.eclipse.swt.widgets#TabFolder/items");	 //$NON-NLS-1$
		SF_TABITEM_CONTROL = URI.createURI("java:/org.eclipse.swt.widgets#TabItem/control");	 //$NON-NLS-1$
		SF_CTABFOLDER_ITEMS = URI.createURI("java:/org.eclipse.swt.custom#CTabFolder/items");	 //$NON-NLS-1$
		SF_CTABITEM_CONTROL = URI.createURI("java:/org.eclipse.swt.custom#CTabItem/control");	 //$NON-NLS-1$
		SF_COOLBAR_ITEMS = URI.createURI("java:/org.eclipse.swt.widgets#CoolBar/items");	 //$NON-NLS-1$
		SF_COOLITEM_CONTROL = URI.createURI("java:/org.eclipse.swt.widgets#CoolItem/control");	 //$NON-NLS-1$
		SF_TOOLBAR_ITEMS = URI.createURI("java:/org.eclipse.swt.widgets#ToolBar/items");	 //$NON-NLS-1$
		SF_DECORATIONS_MENU_BAR = URI.createURI("java:/org.eclipse.swt.widgets#Decorations/menuBar");	 //$NON-NLS-1$
		SF_MENU_ITEMS = URI.createURI("java:/org.eclipse.swt.widgets#Menu/items");	 //$NON-NLS-1$
		SF_MENUITEM_MENU = URI.createURI("java:/org.eclipse.swt.widgets#MenuItem/menu");	 //$NON-NLS-1$
		
		SF_ITEM_TEXT = URI.createURI("java:/org.eclipse.swt.widgets#Item/text");	 //$NON-NLS-1$
		
		SF_EXPANDABLECOMPOSITE_CLIENT = URI.createURI("java:/org.eclipse.ui.forms.widgets#ExpandableComposite/client");	 //$NON-NLS-1$		
	}
}	
