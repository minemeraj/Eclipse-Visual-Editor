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
 *  $Revision: 1.3 $  $Date: 2005-10-21 15:10:58 $ 
 */
package org.eclipse.ve.internal.cde.core;

import java.util.*;

import org.eclipse.draw2d.*;
 
/**
 * 
 * This class is the GEF ToolTip images that in the future will work more like code assist.
 * 
 * This class originated from an innerclass inside a helper class which was in org.eclipse.ve.java.core.
 * It was moved up to CDE to be used in a general purpose manner.
 * 
 * @since 1.2.0
 */
public class ToolTipContentHelper extends Panel {
	ArrayList	fToolTipProcessors = new ArrayList() ;
	HashMap		fProcessorFigures;
			
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
	 * Activate the tooltip helper.
	 * 
	 * @since 1.2.0
	 */
	public void activate() {
		if (fProcessorFigures != null) {
			Iterator iter = fToolTipProcessors.iterator();
			while(iter.hasNext()){
				((ToolTipProcessor)iter.next()).activate();
			}
		}
	}
	
	/*
	 * Deactivate the tooltip helper.
	 * 
	 * @since 1.2.0
	 */
	public void deactivate() {
		if (fProcessorFigures != null) {
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
		if (fProcessorFigures == null) {
			fProcessorFigures = new HashMap(fToolTipProcessors.size());
			// Get the figure for each tool tip processor
			Iterator iter = fToolTipProcessors.iterator();
			while (iter.hasNext()) {
				ToolTipProcessor processor = (ToolTipProcessor) iter.next();
				IFigure figure = processor.createFigure();
				if (figure != null) {
					add(figure);
					fProcessorFigures.put(processor, figure);
				}
			}
		}
	}
	public void addToolTipProcessor(ToolTipProcessor p) {
		fToolTipProcessors.add(p);
	}
	/*
	 * Deactivate the tooltip processor and remove the processor figure from this helper
	 */
	public void removeToolTipProcessor(ToolTipProcessor p) {
		p.deactivate();
		if (fProcessorFigures != null) {
			IFigure processorFigure = (IFigure) fProcessorFigures.get(p);
			if (processorFigure != null) {
				remove(processorFigure);
				fProcessorFigures.remove(p);
			}
		}
		fToolTipProcessors.remove(p);
	}

	/*
	 * Remove the processor figures from this helper and set the map to null
	 * so the next time the helper is shown (see addNotify), it will recreate the processor figures.
	 */
	public void refresh() {
		if (fProcessorFigures != null) {
			Iterator iter = fProcessorFigures.values().iterator();
			while (iter.hasNext())
				remove((IFigure) iter.next());
			fProcessorFigures = null;
		}
	}
}
