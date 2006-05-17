/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created May 27, 2005 by Gili Mendel
 * 
 *  $RCSfile: PropertySheetSetting.java,v $
 *  $Revision: 1.5 $  $Date: 2006-05-17 20:14:53 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import com.ibm.icu.util.StringTokenizer;
 

public class PropertySheetSetting implements EditorSettings.ISetting {
	
	public final static String id = "Settings.PropertySheet"; //$NON-NLS-1$
	
	
	JavaVisualEditorPart editor;
	IResource resource;
	QualifiedName name;
	
	ControlListener listener = null;
	Tree tree;
	
	
	int width[];
	
	
	
	protected void updateWidths() {
		width = new int [tree.getColumnCount()];
		TreeColumn[] col = tree.getColumns();
		for (int i = 0; i < col.length; i++) {
			width[i] = col[i].getWidth();
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < width.length; i++) {
			if (i>0)
				sb.append(EditorSettings.SEPERATOR);
			sb.append(width[i]);
		}
		try {
			resource.setPersistentProperty(name,sb.toString());
		} catch (CoreException e) {
			JavaVEPlugin.log(e);
		}
	}
	
	public PropertySheetSetting(Tree t) {
		tree = t;		
	}
	
	protected void addListener() {
		if (listener==null && !tree.isDisposed()) {
			listener = new ControlListener() {	
				public void controlResized(ControlEvent e) {
					updateWidths();
				}	
				public void controlMoved(ControlEvent e) {
					updateWidths();	
				}	
			};
			TreeColumn[] ctls = tree.getColumns();
			for (int i = 0; i < ctls.length; i++) 
				ctls[i].addControlListener(listener);			
	    }				
	}
	
	protected void removeListener() {
		if (listener!=null && !tree.isDisposed()) {
			TreeColumn[] ctls = tree.getColumns();
			for (int i = 0; i < ctls.length; i++) 
				ctls[i].removeControlListener(listener);				
			listener=null;			
		}
	}
	
	public void apply() {
		try {
			String val = resource.getPersistentProperty(name);
			TreeColumn[] ctls = tree.getColumns();
			if (val!=null) {
				StringTokenizer st = new StringTokenizer(val,EditorSettings.SEPERATOR);
				width = new int[st.countTokens()];
				int index = 0;
				while(st.hasMoreTokens()) {
					String s = st.nextToken();
					width[index]=Integer.parseInt(s);
					if (index<ctls.length)
						ctls[index].setWidth(width[index]);
					index++;
				}				
			}
		} catch (CoreException e1) {
			JavaVEPlugin.log(e1);
		}
		addListener();
	}


	public void dispose() {		
		removeListener();
	}

	public void setQualifier(String q) {
		name = new QualifiedName(q,id);		
	}

	public void setEditor(JavaVisualEditorPart e) {
		editor = e;		
	}

	public void setResource(IResource r) {
		resource = r;		
	}
}
