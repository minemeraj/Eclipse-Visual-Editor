package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.util.URI;

public class SWTConstants {
	
	public static final URI
		SF_CONTROL_BOUNDS,
		SF_CONTROL_LOCATION,
		SF_CONTROL_SIZE,
		SF_COMPOSITE_LAYOUT,
		SF_COMPOSITE_CONTROLS;
		
	public static final String
		POINT_CLASS_NAME,
		RECTANGLE_CLASS_NAME;
		
	static {
		POINT_CLASS_NAME = "org.eclipse.swt.graphics.Point";
		RECTANGLE_CLASS_NAME = "org.eclipse.swt.graphics.Rectangle";
	}		
		
	static {
		SF_CONTROL_BOUNDS = URI.createURI("java:/org.eclipse.swt.widgets#Control/bounds");		 //$NON-NLS-1$
		SF_CONTROL_LOCATION = URI.createURI("java:/org.eclipse.swt.widgets#Control/location");		 //$NON-NLS-1$
		SF_CONTROL_SIZE = URI.createURI("java:/org.eclipse.swt.widgets#Control/size");		 //$NON-NLS-1$					
		SF_COMPOSITE_LAYOUT = URI.createURI("java:/org.eclipse.swt.widgets#Composite/layout");		 //$NON-NLS-1$
		SF_COMPOSITE_CONTROLS = URI.createURI("java:/org.eclipse.swt.widgets#Composite/controls");		 //$NON-NLS-1$
	}
}	