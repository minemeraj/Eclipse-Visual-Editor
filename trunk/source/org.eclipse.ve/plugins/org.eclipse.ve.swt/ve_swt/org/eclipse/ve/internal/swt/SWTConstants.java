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
		SF_GRID_DATA_VERTICAL_SPAN;
		
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

	}
}	