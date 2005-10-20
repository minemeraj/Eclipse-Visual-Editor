/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ToolTipContentHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2005-10-20 19:34:44 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.draw2d.*;
 
/**
 * 
 * This class is the GEF ToolTip images that in the future will work more like code assist.
 * 
 * This class originated from an innerclass inside TooTipContentHelper which was in org.eclipse.ve.java.core.
 * It was moved up to CDE to be used in a general purpose manner.
 * 
 * @since 1.2.0
 */
public class ToolTipContentHelper extends Panel {
	ArrayList	fToolTipProcessors = new ArrayList() ;
	HashMap		fEntries;
			
	public ToolTipContentHelper(ToolTipProcessor[] toolTipProcessors) {
		FlowLayout layout = new FlowLayout(false) ;
		layout.setMajorSpacing(0);
		layout.setMinorSpacing(0);
		setLayoutManager(layout);			
		for (int i = 0; i < toolTipProcessors.length; i++) {
			addToolTipProcessor(toolTipProcessors[i]) ;
		}
	}
	
	/*
	 * Activate the tooltip.
	 * 
	 * @since 1.2.0
	 */
	public void activate() {
		if (fEntries != null) {
			Iterator iter = fToolTipProcessors.iterator();
			while(iter.hasNext()){
				((ToolTipProcessor)iter.next()).activate();
			}
		}
	}
	
	/*
	 * Deactivate the tooltip.
	 * 
	 * @since 1.2.0
	 */
	public void deactivate() {
		if (fEntries != null) {
			Iterator iter = fToolTipProcessors.iterator();
			while(iter.hasNext()){
				((ToolTipProcessor)iter.next()).deactivate();
			}
		}			
	}
	
	/* 
	 * The tooltip has been displayed - create the figures for each of the tool tip processors 
	 */
	public void addNotify() {
		super.addNotify();
		if (fEntries == null){
			fEntries = new HashMap(fToolTipProcessors.size());
			// Get the label for each tool tip details
			Iterator iter = fToolTipProcessors.iterator();
			while(iter.hasNext()){
				IFigure figure = ((ToolTipProcessor)iter.next()).createFigure();
				if(figure != null){
					add(figure);
				}
			}
		}
	}
	public void addToolTipProcessor(ToolTipProcessor p) {
		fToolTipProcessors.add(p) ;
	}

}
