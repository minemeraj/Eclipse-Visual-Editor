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
 *  $Revision: 1.2 $  $Date: 2005-06-21 19:53:10 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.*;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.palette.PaletteViewer;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * Save's the palette state per resource. There is no way to listen to palette edit parts expending/closing, so we will persist the palette when he
 * editor is closed
 * 
 * @since 1.1.0
 */
public class PaletteDrawersSetting implements EditorSettings.ISetting {

	public final static String id = "Settings.Paletted.Drawers";

	JavaVisualEditorPart editor;

	IResource resource;

	QualifiedName name;

	EditDomain domain;

	EditPart paletteRootEditPart = null;

	PaletteRoot paletteRootModel = null;

	public PaletteDrawersSetting(EditDomain d, PaletteRoot mr) {
		domain = d;
		paletteRootModel = mr;
	}

	/*
	 * find the root for the palette itself
	 */
	protected EditPart getEditPartRoot() {
		if (paletteRootEditPart != null)
			return paletteRootEditPart;

		PaletteViewer v = domain.getPaletteViewer();
		if (v != null) {
			paletteRootEditPart = (EditPart) v.getEditPartRegistry().get(getModelRoot());
		}
		return paletteRootEditPart;
	}

	/*
	 * find the root of the (GEF) palette model
	 */
	protected PaletteRoot getModelRoot() {
		if (paletteRootModel != null)
			return paletteRootModel;
		paletteRootModel = domain.getPaletteRoot();
		return paletteRootModel;
	}

	/**
	 * Get drawer. This is so that we can set its initial state.
	 * 
	 * @param idOrLabel
	 *            either the id or label. ID is checked first.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected PaletteDrawer getDrawer(String idOrLabel) {
		for (Iterator iter = getModelRoot().getChildren().iterator(); iter.hasNext();) {
			Object elm = iter.next();
			if (elm instanceof PaletteDrawer)
				if (((PaletteDrawer) elm).getLabel().equals(idOrLabel))
					return (PaletteDrawer) elm;
		}
		return null;
	}

	public void apply() {
		try {
			String val = resource.getPersistentProperty(name);
			boolean openedOne = false;
			if (val != null) {
				StringTokenizer st = new StringTokenizer(val, EditorSettings.SEPERATOR);
				while (st.hasMoreElements()) {
					String idOrlabel = st.nextToken();
					int state = Integer.parseInt(st.nextToken());
					PaletteDrawer d = getDrawer(idOrlabel);
					if (d != null) {
						d.setInitialState(state);
						if (state != PaletteDrawer.INITIAL_STATE_CLOSED)
							openedOne = true;
					}
				}

			}
			if (!openedOne) {
				// We didn't explicitly open one. Now check all of the drawers to see if any are open. If none are open, then open first one.
				PaletteRoot root = getModelRoot();
				PaletteDrawer firstDrawer = null;
				boolean oneOpen = false;
				for (Iterator iter = root.getChildren().iterator(); iter.hasNext();) {
					PaletteEntry entry = (PaletteEntry) iter.next();
					if (entry instanceof PaletteDrawer) {
						PaletteDrawer drawer = (PaletteDrawer) entry;
						if (firstDrawer == null)
							firstDrawer = drawer;
						if (drawer.getInitialState() != PaletteDrawer.INITIAL_STATE_CLOSED) {
							oneOpen = true;
							break;
						}
					}
				}
				if (!oneOpen && firstDrawer != null)
					firstDrawer.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
			}
		} catch (CoreException e) {
			JavaVEPlugin.log(e);
		}

	}

	public void dispose() {
		// Editor is closing, persist the palette
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		if (getEditPartRoot() != null) {
			PaletteViewer paletteViewer = (PaletteViewer) getEditPartRoot().getViewer();
			List drawers = getEditPartRoot().getChildren();
			if (drawers != null) {
				Iterator elements = drawers.iterator();
				while (elements.hasNext()) {
					EditPart ep = (EditPart) elements.next();
					Object model = ep.getModel();
					if (model instanceof PaletteDrawer) {
						PaletteDrawer drawer = (PaletteDrawer) model;
						if (!first)
							sb.append(EditorSettings.SEPERATOR);
						first = false;
						if (drawer.getId().length() != 0)
							sb.append(drawer.getId());
						else
							sb.append(drawer.getLabel());
						sb.append(EditorSettings.SEPERATOR);
						int state = PaletteDrawer.INITIAL_STATE_CLOSED;
						if (paletteViewer.isExpanded(drawer))
							state = PaletteDrawer.INITIAL_STATE_OPEN;
						if (paletteViewer.isPinned(drawer))
							state = PaletteDrawer.INITIAL_STATE_PINNED_OPEN;
						sb.append(state);
					}
				}
			}
			try {
				resource.setPersistentProperty(name, sb.toString());
			} catch (CoreException e) {
				JavaVEPlugin.log(e);
			}
		}
	}

	public void setQualifier(String q) {
		name = new QualifiedName(q, id);
	}

	public void setEditor(JavaVisualEditorPart e) {
		editor = e;
	}

	public void setResource(IResource r) {
		resource = r;
	}

}
