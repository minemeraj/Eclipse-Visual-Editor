/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFPrototypeCreationFactory;

public class PasteActionImmediate extends SelectionAction {

	private EditDomain editDomain;

	public PasteActionImmediate(IWorkbenchPart part) {
		super(part);
		setId(ActionFactory.PASTE.getId());
	}
	
	protected boolean calculateEnabled() {
		return getPasteCommand() != null && getPasteCommand().canExecute();
	}
	
	protected Command getPasteCommand(){
		editDomain = null;
		if(getSelectedObjects().size() == 0) return null;
		EMFPrototypeCreationFactory factory = getFactory();	
		if(factory == null) return null;
		CreateRequest request = new CreateRequest();		
		request.setFactory(factory);
		request.setLocation(getPasteLocation());
		Object obj = getSelectedObjects().get(0);
		if (obj instanceof EditPart) {
			EditPart editPartTarget = (EditPart)obj;
			editDomain = EditDomain.getEditDomain(editPartTarget);	
			factory.setEditDomain(editDomain);
			return editPartTarget.getCommand(request);
		}
		return null;
	}
	
	private EMFPrototypeCreationFactory getFactory(){
		List selection = getSelectedObjects();
		if (selection == null || selection.size() != 1)
			return null;
		Object template = getClipboardContents();
//		if (template  == null)
//			return null;
		return getFactory(template);
	}

	//TODO Hardcode for CheckBox for testing
	private EMFPrototypeCreationFactory getFactory(Object template) {
		return new EMFPrototypeCreationFactory("platform:/plugin/org.eclipse.ve.swt/CheckBox.xmi#CheckBox_1"); //$NON-NLS-1$
	}

	protected Object getClipboardContents() {
		Object result = null;
		Clipboard cb = new Clipboard(Display.getDefault());
		TransferData[] transferTypes = cb.getAvailableTypes();
		for (int i = 0; i < transferTypes.length; i++) {
			if (TemplateTransfer.getInstance().isSupportedType(transferTypes[i])) {
				result = org.eclipse.gef.ui.actions.Clipboard.getDefault().getContents();
				break;
			}
		}
		cb.dispose();
		return result;
	}
	
	protected Point getPasteLocation() {
		return new Point(10, 10);
	}
	
	public void run() {
		editDomain.getCommandStack().execute(getPasteCommand());
	}

}
