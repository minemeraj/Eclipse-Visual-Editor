package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.util.URI;

public class SWTConstants {
	
	public static final URI
		SF_CONTROL_BOUNDS,
		SF_CONTROL_LOCATION,
		SF_CONTROL_SIZE,
		SF_CONTROL_LAYOUTDATA,		
		SF_COMPOSITE_LAYOUT,
		SF_COMPOSITE_CONTROLS,
		
		SF_FILL_LAYOUT_MARGIN_HEIGHT,
		SF_FILL_LAYOUT_MARGIN_WIDTH,
		SF_FILL_LAYOUT_SPACING,
		SF_FILL_LAYOUT_TYPE,
		
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
		
		SF_TABLE_COLUMNS;
		
	public static final String
		POINT_CLASS_NAME,
		RECTANGLE_CLASS_NAME;
		
	static {
		POINT_CLASS_NAME = "org.eclipse.swt.graphics.Point"; //$NON-NLS-1$
		RECTANGLE_CLASS_NAME = "org.eclipse.swt.graphics.Rectangle"; //$NON-NLS-1$
	}		
		
	static {
		SF_CONTROL_BOUNDS = URI.createURI("java:/org.eclipse.swt.widgets#Control/bounds");		 //$NON-NLS-1$
		SF_CONTROL_LOCATION = URI.createURI("java:/org.eclipse.swt.widgets#Control/location");		 //$NON-NLS-1$
		SF_CONTROL_SIZE = URI.createURI("java:/org.eclipse.swt.widgets#Control/size");		 //$NON-NLS-1$					
		SF_CONTROL_LAYOUTDATA = URI.createURI("java:/org.eclipse.swt.widgets#Control/layoutData");		 //$NON-NLS-1$		
		SF_COMPOSITE_LAYOUT = URI.createURI("java:/org.eclipse.swt.widgets#Composite/layout");		 //$NON-NLS-1$
		SF_COMPOSITE_CONTROLS = URI.createURI("java:/org.eclipse.swt.widgets#Composite/controls");		 //$NON-NLS-1$
		
		SF_FILL_LAYOUT_MARGIN_HEIGHT = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/marginHeight");    //$NON-NLS-1$
		SF_FILL_LAYOUT_MARGIN_WIDTH = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/marginWidth");    //$NON-NLS-1$
		SF_FILL_LAYOUT_SPACING = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/spacing");    //$NON-NLS-1$
		SF_FILL_LAYOUT_TYPE = URI.createURI("java:/org.eclipse.swt.layout#FillLayout/type");    //$NON-NLS-1$
		
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

	}
}	