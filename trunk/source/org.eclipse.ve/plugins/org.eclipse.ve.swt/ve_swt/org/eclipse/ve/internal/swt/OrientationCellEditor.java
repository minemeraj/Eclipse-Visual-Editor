/*
 * Created on Mar 4, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author JoeWin
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class OrientationCellEditor extends EnumeratedIntValueCellEditor {
	
	public OrientationCellEditor(Composite composite){
		super(composite,
			new String[] { "HORIZONTAL" , "VERTICAL" } ,
	        new Integer[] { new Integer(SWT.HORIZONTAL) , new Integer(SWT.VERTICAL) } ,			
			new String[] { "org.eclipse.swt.SWT.HORIZONTAL" , "org.eclipse.swt.SWT.VERTICAL" } 
		);
	}	
}
