/*
 * Created on May 22, 2003
 * by gmendel
 *
*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ToolTipContentHelper.java,v $
 *  $Revision: 1.1 $  $Date: 2004-06-29 18:20:23 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.draw2d.*;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;


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
					add(figure);
				}
			}
		}
		public void addContentProcessor(ToolTipAssistFactory.TooltipDetails p) {
			fContentAssistProcessors.add(p) ;
		}	
		public void paint(Graphics graphics) {
			// TODO Auto-generated method stub
			super.paint(graphics);
		}
	}
	
	public static AssistedToolTipFigure createToolTip(ToolTipAssistFactory.TooltipDetails[] toolTipDetails) {		
		return new AssistedToolTipFigure(toolTipDetails);		
	}



}
