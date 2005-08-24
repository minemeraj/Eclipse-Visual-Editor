/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: XMLTextViewer.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:46 $ 
 */



import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
/**
 * Viewer for showing XML text for OCM.
 * Creation date: (9/30/99 11:42:11 AM)
 * @author: 
 */
public class XMLTextViewer extends TextViewer {
	protected Color fQuotedTextColor = ColorConstants.red;
	protected Color fNormalTextColor = ColorConstants.black;
	protected Color fClassIDTextColor = ColorConstants.blue;
	protected Cursor fWaitCursor;
	protected Cursor fNormalCursor;
	protected Font fTextFont;
	
/**
 * MOFTextViewer constructor comment.
 */
public XMLTextViewer(Composite aParent , int style ) {
	super(aParent , style);
	setDocument(new Document());
}

protected void createControl(Composite parent, int style) {
	super.createControl(parent, style);
	Display d = parent.getDisplay();
	fTextFont = new Font(d,"Arial", 8 , SWT.NATIVE); //$NON-NLS-1$
	getTextWidget().setFont(fTextFont);
}

protected Color getClassIDTextColor(){
	return fClassIDTextColor;
}
protected Color getNormalTextColor(){
	return fNormalTextColor;
}
protected Color getQuotedTextColor(){
	return fQuotedTextColor;
}
/**
 * The viewer's UI component is going to be disposed. So deallocate all 
 * allocated SWT resources. Subclasses should reimplement this method 
 * to release their own allocated SWT resources.
 * This default implementation disposes the popup menu.
 */
protected void handleDispose() {
	if (fNormalCursor != null) {
		fNormalCursor.dispose();
		fNormalCursor = null;
	}
	if (fWaitCursor != null) {
		fWaitCursor.dispose();
		fWaitCursor = null;
	}
	if ( fTextFont != null )
		fTextFont.dispose();		
	super.handleDispose();
}
public void highlightText() {
	// Only do this if our widget has been relized
//	if ( ((RichTextPart)fText).getTextEditor() == null ) return;
	// We have a widget.  Crawl the document doing a basic highlight algorithm to create
	// an array of ranges			
	String text = getDocument().get();
	int positionOfQuote = -1;
	int positionOfClassIDStart = -1;
	boolean classIDStartOn = false;
	boolean quoteOn = false;
	int[] ranges = new int[300];
	int iRange = 0;
	boolean quotedIsClass = false;
	
	for (int i = 0; i < text.length(); i++) {
		char nextChar = text.charAt(i);
		if (nextChar == '"') {
			
			// If we are not inside a quote then toggle the boolean to say we are
			// and set the position of the last quote + 1 to here - 1 to be normal
			if (!quoteOn) {
				quoteOn = true;
				classIDStartOn = false;
				positionOfQuote = i;
				
				if (i > 6){
					if ( text.charAt(i-1) == '=' && text.charAt(i-2) =='e' && text.charAt(i-3) == 'p'
					&& text.charAt(i-4)=='y' && text.charAt(i-5) == 't'){
						quotedIsClass = true;
					}
				}
				
			}
			else {
				// If we are not inside a quote then toggle the boolean to say we now are not
				// and set the position of the last quote to here to be the quoted color
				quoteOn = false;
				classIDStartOn = false;
				if (iRange >= ranges.length) {
					// Need to increase the array
					int[] temp = ranges;
					ranges = new int[ranges.length + 300];
					System.arraycopy(temp, 0, ranges, 0, temp.length);
				}
				if (quotedIsClass) {
					ranges[iRange++] = 1; 
					quotedIsClass = false;  //  ClassId Text Color index             
				}  
				else ranges[iRange++] = 0; // QuotedText Color index
				
				ranges[iRange++] = positionOfQuote; // Start of text offset
				ranges[iRange++] = i + 1;
				positionOfQuote = i;
			}
		}
		else if (nextChar == ':' && !quoteOn) {
				if (!classIDStartOn) {
					classIDStartOn = true;
					positionOfClassIDStart = i;
				}
		}
		else if (nextChar == ' ' || nextChar == '>') {
					if (classIDStartOn) {
						classIDStartOn = false;
						if (iRange >= ranges.length) {
							// Need to increase the array
							int[] temp = ranges;
							ranges = new int[ranges.length + 300];
							System.arraycopy(temp, 0, ranges, 0, temp.length);
						}
						ranges[iRange++] = 1; // ClassId Text Color index
						ranges[iRange++] = positionOfClassIDStart; // Start of text offset
						ranges[iRange++] = i;
					}
		}
	}//end of for
	if (iRange > 0) {
		int[] temp = ranges;
		ranges = new int[iRange];
		System.arraycopy(temp, 0, ranges, 0, iRange);
		Color[] colors = new Color[] {getQuotedTextColor(), getClassIDTextColor()};
		
		StyleRange[] styles= new StyleRange[ranges.length / 3];
		for (int i= 0, j= 0; i < ranges.length; i += 3, j++) {
			StyleRange style= new StyleRange();
			style.start= ranges[i + 1];
			style.length= ranges[i + 2] - style.start;
			style.foreground= colors[ranges[i]];
			styles[j]= style;
		}
		getTextWidget().setStyleRanges(styles);
	}
}

/**
 * Set the text into the document
 * Try and preserve the vertical scroll position to stop us always having to scoll down
 */
public void setText(String text) {

	if(getDocument() == null) return;  // NPE can occur under some circumstances closing down on a slow PC
	int oldTopIndex = getTopIndex();
	getDocument().set(text);
	highlightText();
	setTopIndex(oldTopIndex);
}

}
