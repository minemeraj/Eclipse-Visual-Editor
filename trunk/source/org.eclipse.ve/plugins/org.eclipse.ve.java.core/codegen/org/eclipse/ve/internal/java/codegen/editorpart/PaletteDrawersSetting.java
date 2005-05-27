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
 *  $RCSfile: PaletteDrawersSetting.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-27 21:58:27 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;


import java.util.*;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.palette.PaletteViewer;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * Save's the palette state per resource.
 * There is no way to listen to palette edit parts expending/closing,
 * so we will persist the palette when he editor is closed
 * 
 * @since 1.1.0
 */
public class PaletteDrawersSetting implements EditorSettings.ISetting  {
	public final static String id = "Settings.Paletted.Drawers";
	
	
	JavaVisualEditorPart editor;
	IResource resource;
	QualifiedName name;	
	EditDomain  domain;
	EditPart    paletteRootEditPart = null;
	PaletteRoot paletteRootModel = null;
		
	
	public PaletteDrawersSetting(EditDomain d, PaletteRoot mr) {
		domain = d;	
		paletteRootModel = mr;
	}
	
	/*
	 * find the root for the palette itself
	 */
	protected EditPart getEditPartRoot() {
		if (paletteRootEditPart!=null)
			return paletteRootEditPart;
		
		paletteRootModel = domain.getPaletteRoot();
		if (domain.getPaletteViewer()!=null) {
			PaletteViewer v = domain.getPaletteViewer();
			Iterator elements = v.getRootEditPart().getChildren().iterator();
			while (elements.hasNext()) {
				EditPart ep = (EditPart)elements.next();
				if (ep.getModel() instanceof PaletteRoot) {
					paletteRootEditPart = ep;
					break;
				}
			}		
		}		
		return paletteRootEditPart;
	}
	
	/*
	 * find the root of the (GEF) palette model
	 */
	protected PaletteRoot getModelRoot() {
		if (paletteRootModel!=null)
			return paletteRootModel;
		paletteRootModel = domain.getPaletteRoot();
		return paletteRootModel;
	}
	
	/*
	 * At this time not drawer ids, so use their labels
	 */
	protected PaletteDrawer getDrawer(String label) {		
		for (Iterator iter = getModelRoot().getChildren().iterator(); iter.hasNext();) {
			Object elm = iter.next();
			if (elm instanceof PaletteDrawer)
				if (((PaletteDrawer)elm).getLabel().equals(label))
					return (PaletteDrawer)elm;
		}
		return null;
	}
	
	public void apply() {
		try {	
			String val = resource.getPersistentProperty(name);
			if (val!=null) {
				StringTokenizer st = new StringTokenizer(val,EditorSettings.SEPERATOR);
				while (st.hasMoreElements()) {
					String label = st.nextToken();
					int state = Integer.parseInt(st.nextToken());
					PaletteDrawer d = getDrawer(label);
					if (d!=null)
						d.setInitialState(state);					
				}
				
			}
		} catch (CoreException e) {
			JavaVEPlugin.log(e);
		}

	}

	public void dispose() {
		// Editor is closing, persist the palette
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		if (getEditPartRoot()!=null) {				
			List drawers = getEditPartRoot().getChildren();
			if (drawers!=null) {
				Iterator elements = drawers.iterator();
				while (elements.hasNext()) {
					EditPart ep = (EditPart)elements.next();
					if (ep instanceof DrawerEditPart) {
						if (!first)
							sb.append(EditorSettings.SEPERATOR);
						first=false;
						DrawerEditPart dr = (DrawerEditPart)ep;
						PaletteDrawer model = (PaletteDrawer)ep.getModel();
						sb.append(model.getLabel());
						sb.append(EditorSettings.SEPERATOR);
						int state = PaletteDrawer.INITIAL_STATE_CLOSED;
						if (dr.isExpanded())
							state = PaletteDrawer.INITIAL_STATE_OPEN;
						if (dr.isPinnedOpen())
							state = PaletteDrawer.INITIAL_STATE_PINNED_OPEN;
						sb.append(state);
					}
				}
			}
			try {
				resource.setPersistentProperty(name,sb.toString());
			} catch (CoreException e) {
				JavaVEPlugin.log(e);
			}
		}
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
