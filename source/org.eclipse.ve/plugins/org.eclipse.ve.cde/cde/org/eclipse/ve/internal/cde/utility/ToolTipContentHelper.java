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
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:06 $ 
 */
package org.eclipse.ve.internal.cde.utility;

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
        ITextViewer fviewer ;
		ArrayList	fContentAssistProcessors = new ArrayList() ;
		HashMap		fEntries = new HashMap() ;
		
		public AssistedToolTipFigure(ITextViewer v) {
			super() ;
			fviewer = v ;
			FlowLayout layout = new FlowLayout(false) ;
			layout.setMajorSpacing(0) ;
			layout.setMinorSpacing(0) ;
			setLayoutManager(layout) ;
		}		
		public AssistedToolTipFigure(ITextViewer v, IContentAssistProcessor[] p) {
			this(v) ;
			for (int i = 0; i < p.length; i++) {
				addContentProcessor(p[i]) ;
			}
		}
		public void addContentProcessor(IContentAssistProcessor p) {
			fContentAssistProcessors.add(p) ;
			// TODO right now the assumption is a single proposal per processor
			Label l = new Label() ;
			add(l) ;
			fEntries.put(p,l) ;
		}
		public void removeContentProcessor(IContentAssistProcessor p){
			Label l = (Label) fEntries.remove(p);
			remove(l) ;			
			fContentAssistProcessors.remove(p) ;			
		}
		
	    // Overide this so that we know when we are set on the ToolTip's shell
	    // This will give us time to refresh the text
	    //
	    // Do not want to take too long here so use pre-defined labels
		public void addNotify() {		
			super.addNotify();
			for (int i = 0; i < fContentAssistProcessors.size(); i++) {
				IContentAssistProcessor p = (IContentAssistProcessor) fContentAssistProcessors.get(i) ;
				Label l = (Label) fEntries.get(p) ;
				// Assume a single proposal per processor
				int offset = fviewer != null ? fviewer.getSelectedRange().x : -1 ;
				ICompletionProposal[] props = p.computeCompletionProposals(fviewer,offset) ;
				if (props != null && props.length>0) {
					// TODO need to process multi proposals per processor
					ICompletionProposal prop = props[0] ;
					l.setText(prop.getDisplayString()) ;
					l.setIcon(prop.getImage()) ;
				}
				else {
					l.setText("") ; //$NON-NLS-1$
					l.setIcon(null) ;
				}
			}
			
		}

	}
	
	public static IFigure createToolTip(ITextViewer v) {		
		return new AssistedToolTipFigure(v) ;		
	}
	public static IFigure createToolTip(ITextViewer v, IContentAssistProcessor[] p) {		
		return new AssistedToolTipFigure(v,p) ;		
	}



}
