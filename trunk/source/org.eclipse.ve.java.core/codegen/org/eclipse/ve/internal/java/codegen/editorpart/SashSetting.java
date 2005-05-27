/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  Created May 27, 2005 by Gili Mendel
 * 
 *  $RCSfile: SashSetting.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-27 21:58:27 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.StringTokenizer;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.cde.core.CustomSashForm;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * This setting will monitor the sash' weights
 * This setting is not global and will be persisted on a resource.
 * 
 * @since 1.1.0
 */
public class SashSetting implements EditorSettings.ISetting {
	
	public final static String id = "Settings.Sash";
	
	
	JavaVisualEditorPart editor;
	IResource resource;
	QualifiedName name;
	CustomSashForm sash;
	
	ControlListener listener=null;
	Control listenedControl=null;
	
	int weights[];
	
	
	protected void updateWeights() {
		weights = sash.getWeights();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < weights.length; i++) {
			if (i>0)
				sb.append(EditorSettings.SEPERATOR);
			sb.append(weights[i]);
		}
		try {
			resource.setPersistentProperty(name,sb.toString());
		} catch (CoreException e) {
			JavaVEPlugin.log(e);
		}
	}
	
	public SashSetting(CustomSashForm s) {
		sash = s;		
	}
	
	protected void addListener() {
		if (listener==null && !sash.isDisposed()) {
			listener = new ControlListener() {	
				public void controlResized(ControlEvent e) {
					updateWeights();
				}	
				public void controlMoved(ControlEvent e) {
					updateWeights();	
				}	
			};
			listenedControl = sash.getChildren()[0];
			listenedControl.addControlListener(listener);			
	    }				
	}
	
	protected void removeListener() {
		if (listenedControl!=null && !listenedControl.isDisposed()) {
			listenedControl.removeControlListener(listener);
			listener=null;
			listenedControl=null;
		}
	}
	
	public void apply() {
		try {
			String val = resource.getPersistentProperty(name);
			if (val!=null) {
				StringTokenizer st = new StringTokenizer(val,EditorSettings.SEPERATOR);
				weights = new int[st.countTokens()];
				int index = 0;
				while(st.hasMoreTokens()) {
					String s = st.nextToken();
					weights[index++]=Integer.parseInt(s);
				}
				sash.setWeights(weights);
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
