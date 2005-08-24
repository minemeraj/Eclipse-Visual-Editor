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
/*
 *  $RCSfile: ToolTipContentHelper.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:46 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;

import org.eclipse.draw2d.*;


/**
 * @author gmendel
 * 
 * This class is a temprorary stepping stone into getting a ToolTip like that performs more
 * like a code assist hybreed on EditParts.  At this time it provides a simple ToolTip images.
 */
public class ToolTipContentHelper {
    /*
     * This class is the GEF ToolTip images that in the future will work more like
     * code asist
     */	
	public static class AssistedToolTipFigure extends Panel {
		ArrayList	fContentAssistProcessors = new ArrayList() ;
		HashMap		fEntries;
				
		public AssistedToolTipFigure(ToolTipAssistFactory.TooltipDetails[] toolTipDetails) {
			FlowLayout layout = new FlowLayout(false) ;
			layout.setMajorSpacing(0);
			layout.setMinorSpacing(0);
			setLayoutManager(layout);			
			for (int i = 0; i < toolTipDetails.length; i++) {
				addContentProcessor(toolTipDetails[i]) ;
			}
		}
		
		/**
		 * Activate the tooltip.
		 * 
		 * 
		 * @since 1.1.0
		 */
		public void activate() {
			if (fEntries != null) {
				Iterator iter = fContentAssistProcessors.iterator();
				while(iter.hasNext()){
					((ToolTipAssistFactory.TooltipDetails)iter.next()).activate();
				}
			}
		}
		
		/**
		 * Deactivate the tooltip.
		 * 
		 * 
		 * @since 1.1.0
		 */
		public void deactivate() {
			if (fEntries != null) {
				Iterator iter = fContentAssistProcessors.iterator();
				while(iter.hasNext()){
					((ToolTipAssistFactory.TooltipDetails)iter.next()).deactivate();
				}
			}			
		}
		
		/* 
		 * The tooltip has been displayed - calculate its contents 
		 */
		public void addNotify() {
			super.addNotify();
			if (fEntries == null){
				fEntries = new HashMap(fContentAssistProcessors.size());
				// Get the label for each tool tip details
				Iterator iter = fContentAssistProcessors.iterator();
				while(iter.hasNext()){
					IFigure figure = ((ToolTipAssistFactory.TooltipDetails)iter.next()).createFigure();
					if(figure != null){
						add(figure);
					}
				}
			}
		}
		public void addContentProcessor(ToolTipAssistFactory.TooltipDetails p) {
			fContentAssistProcessors.add(p) ;
		}
	}
	
	public static AssistedToolTipFigure createToolTip(ToolTipAssistFactory.TooltipDetails[] toolTipDetails) {		
		return new AssistedToolTipFigure(toolTipDetails);		
	}



}
