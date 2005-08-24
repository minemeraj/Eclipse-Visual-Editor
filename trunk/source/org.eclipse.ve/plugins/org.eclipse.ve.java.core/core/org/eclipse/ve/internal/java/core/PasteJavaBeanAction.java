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
/*
 *  $RCSfile: PasteJavaBeanAction.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFPrototypeCreationFactory;

public class PasteJavaBeanAction extends SelectionAction {

	private CreationTool creationTool;
	private EditDomain editDomain;
	public boolean executeImmediately = false;

	public PasteJavaBeanAction(IWorkbenchPart part,EditDomain anEditDomain) {
		super(part);
		setId(ActionFactory.PASTE.getId());
		editDomain = anEditDomain;
	}
	
	protected boolean calculateEnabled() {
		
		// If we are going to be executed immediately we are run we should only enabled if the drop
		// can occur, otherwise we just enable ourself if we have good clipboard contents as the target
		// could change between now and execution
		if(executeImmediately) {
			// We can only drop onto a single target
			if(getSelectedObjects().size() != 1){
				return false;
			}
			CreationFactory factory = getFactory();		
			if(factory != null){
				CreateRequest request = new CreateRequest();		
				request.setFactory(factory);
				request.setLocation(getPasteLocation());			
				// If we should execute immediately drop the source onto the target right away
				EditPart editPart = (EditPart)getSelectedObjects().get(0);
				Command command = editPart.getCommand(request);
				return command != null && command.canExecute();
			}
			return false;			
		} else {
			return getClipboardContents() != null;
		}
	}
	
	private CreationFactory getFactory(){
		List selection = getSelectedObjects();
		if (selection == null || selection.size() != 1)
			return null;
		Object template = getClipboardContents();
		if (template  == null)
			return null;
		return getFactory(template);
	}

	private CreationFactory getFactory(Object template) {
		// Load the contents of the clipboard and assume they are well formed XMI
		EObject root = editDomain.getDiagramData();
		ResourceSet targetResourceSet = root.eResource().getResourceSet();
		// Use a dummy URI for the resource that is unique
		Resource clipboardResource = targetResourceSet.createResource(root.eResource().getURI().appendSegment(":" + System.currentTimeMillis())); //$NON-NLS-1$
		ByteArrayInputStream is = new ByteArrayInputStream(getClipboardContents().getBytes());	
		try {
			clipboardResource.load(is, null);
			return new EMFPrototypeCreationFactory("/0", clipboardResource); //$NON-NLS-1$
		} catch (Exception e) {
			// If the XMI is for objects and features not in the current project's resource set (i.e. build path)
			// an exception will be thrown trying to parse the XMI.  In this case paste is not allowed			
			return null;
		} finally {
			targetResourceSet.getResources().remove(clipboardResource);
		}
	}

	protected String getClipboardContents() {
		Object result = null;
		Clipboard cb = new Clipboard(Display.getDefault());
		TransferData[] transferTypes = cb.getAvailableTypes();
		for (int i = 0; i < transferTypes.length; i++) {
			if (TextTransfer.getInstance().isSupportedType(transferTypes[i])) {			
				result = cb.getContents(TextTransfer.getInstance());
				break;
			}
		}
		cb.dispose();
		// If the clipboard is a String then make sure it begins with a VE header to distinguish it from 
		// other strings that might be there but aren't actually valid for the VE
		String clipboardContents = (String)result;
		if (clipboardContents != null && clipboardContents.startsWith(JavaVEPlugin.TRANSFER_HEADER)){
			clipboardContents = clipboardContents.substring(JavaVEPlugin.TRANSFER_HEADER.length());
			return clipboardContents;
		} else {
			return null;
		}
	}
	
	protected Point getPasteLocation() {
		return new Point(10, 10);
	}
	
	public void run() {
		creationTool = null;
		CreationFactory factory = getFactory();		
		if(factory != null){
			CreateRequest request = new CreateRequest();		
			request.setFactory(factory);
			request.setLocation(getPasteLocation());			
			if(executeImmediately) {
				// If we should execute immediately drop the source onto the target right away
				EditPart editPart = (EditPart)getSelectedObjects().get(0);
				Command command = editPart.getCommand(request);
				editDomain.getCommandStack().execute(command);
			} else {
				creationTool = new CreationTool(factory);				
				editDomain.setActiveTool(creationTool);
			}
		}
	}
}
