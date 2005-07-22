/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementationf
 *******************************************************************************/
package org.eclipse.ve.internal.swt.targetvm;
import java.lang.reflect.Field;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

/**
 * Get the widths and heights of the columns and rows so that the IDE can show these in its GEF feedback
 * This class works on Eclipse 3.0 because it has access to fields in the GridLayout
 * In 3.1 these were removed so this won't work, however the IDE GridLayoutPolicyHelper instead uses
 * the newer peer named GridLayoutHelper
 */

public class GridLayoutHelper_30 {

	private Composite fComposite;
	public int[] widths;
	public int[] heights;
	private Field pixelColumnWidthsFieldProxy;
	private Field pixelRowHeightsFieldProxy;

	public void setComposite(Composite aComposite) {
		fComposite = aComposite;
		computeValues();
	}

	private void computeValues() {
		GridLayout gridLayout = (GridLayout) fComposite.getLayout();
		try {
			if(pixelColumnWidthsFieldProxy == null){
				pixelColumnWidthsFieldProxy = gridLayout.getClass().getDeclaredField("pixelColumnWidths");
				pixelColumnWidthsFieldProxy.setAccessible(true);
			}
			if(pixelRowHeightsFieldProxy == null){
				pixelRowHeightsFieldProxy = gridLayout.getClass().getDeclaredField("pixelRowHeights");
				pixelRowHeightsFieldProxy.setAccessible(true);				
			}			
			widths = (int[])pixelColumnWidthsFieldProxy.get(gridLayout);
			heights = (int[])pixelRowHeightsFieldProxy.get(gridLayout);			
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

	}

}