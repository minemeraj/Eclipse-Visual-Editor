/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DefaultCopyEditPolicy.java,v $
 *  $Revision: 1.2 $  $Date: 2005-05-10 00:16:30 $ 
 */
package org.eclipse.ve.internal.java.codegen.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.jem.internal.instantiation.base.FeatureValueProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class DefaultCopyEditPolicy extends AbstractEditPolicy {
	
	public Command getCommand(Request request) {
		if(CopyAction.REQ_COPY.equals(request.getType())){
			return getCopyCommand(request);
		} else {
			return null;
		}
	}

	private Command getCopyCommand(Request request) {
		
		return new Command(){
			public void execute(){
				Object template = null;
				// Get the host, copy it and serialize to a stream
				IJavaInstance javaBeanToCopy = (IJavaInstance) getHost().getModel();
				// Create a new EMF Resource for the copied object		
				Resource newEMFResource = javaBeanToCopy.eResource().getResourceSet().createResource(javaBeanToCopy.eResource().getURI().appendSegment("" + javaBeanToCopy.hashCode()));				

				// Work needs to be done to deal with additional references
				List objectsToCopy = new ArrayList();
				expandCopySet(javaBeanToCopy,objectsToCopy);
				
				// This method allows additional work to be done to clean up a particular JavaBean
				normalize((IJavaInstance) javaBeanToCopy);
				
				// Use the ECore.Copier to put everything from the copy set into the resource set
				EcoreUtil.Copier copier = new EcoreUtil.Copier();
				copier.copyAll(objectsToCopy);
				copier.copyReferences();
				
				// Add copies of all the objects we want to copy to the new resource set
				Iterator iter = objectsToCopy.iterator();
				while(iter.hasNext()){
					newEMFResource.getContents().add(copier.get(iter.next()));
				}
		
				// Save to a stream
				HashMap XML_TEXT_OPTIONS = new HashMap(2);
				XML_TEXT_OPTIONS.put(
						XMLResource.OPTION_PROCESS_DANGLING_HREF, 
						XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
				XML_TEXT_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));		
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					newEMFResource.save(os, XML_TEXT_OPTIONS);
				} catch (IOException e) {
					JavaVEPlugin.log(e);
				}
				template = os.toString();
				// Append the string "{*** VE TRANSFER ***} to the front to make sure that paste only takes object that are valid VE objects
				// We don't use a custom transfer type as it is nice to actually work with the clipboard contents as a String 
				template = PasteActionTool.TRANSFER_HEADER + template;
				// Paste the string of the object to copy to the clipboard
				Clipboard cb = new Clipboard(Display.getDefault());
				cb.setContents(new Object[] {template}, new Transfer[] {TextTransfer.getInstance()});
				cb.dispose();				
			}

		};
	}
	
	private void expandCopySet(final EObject eObject, final List objectsToCopy) {

		// All objects to be copied are added to the list of objects to copy
		objectsToCopy.add(eObject);
		
		// All properties should be collapsed
		if(eObject instanceof FeatureValueProvider){
			FeatureValueProvider obj = (FeatureValueProvider) eObject;
			obj.visitSetFeatures(new FeatureValueProvider.Visitor(){
				public void isSet(EStructuralFeature feature, Object value) {
					if(feature instanceof EReference){
						Object propertyValue = eObject.eGet(feature);						
						if(propertyValue instanceof EObject){
							// Add the property value to the set of objects being copied
							EObject ePropertyValue = (EObject)propertyValue;
							if(objectsToCopy.indexOf(ePropertyValue) == -1){
								objectsToCopy.add(ePropertyValue);
								expandCopySet(ePropertyValue,objectsToCopy);
							}
						}
					}
				}
			});
		}
		
	}
	
	
	protected void normalize(final IJavaInstance javaBean){

	}
}
