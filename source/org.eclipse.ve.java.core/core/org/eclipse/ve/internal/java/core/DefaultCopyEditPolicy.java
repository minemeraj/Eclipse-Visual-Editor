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
 *  $Revision: 1.2 $  $Date: 2005-05-11 19:01:20 $ 
 */
package org.eclipse.ve.internal.java.core;

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

				// A copy set is built up with everything to be copied
				List objectsToCopy = new ArrayList();
				
				copyProperty(javaBeanToCopy,objectsToCopy);				
				
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
				XML_TEXT_OPTIONS.put(XMLResource.OPTION_ENCODING, "UTF-8");
				XML_TEXT_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));		
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					newEMFResource.save(os, XML_TEXT_OPTIONS);
				} catch (IOException e) {
					JavaVEPlugin.log(e);
				}
				// Append the string "{*** VE TRANSFER ***} to the front to make sure that paste only takes object that are valid VE objects
				// We don't use a custom transfer type as it is nice to actually work with the clipboard contents as a String 
				template = PasteActionTool.TRANSFER_HEADER + os.toString();
				// Paste the string of the object to copy to the clipboard
				Clipboard cb = new Clipboard(Display.getCurrent());
				cb.setContents(new Object[] {template}, new Transfer[] {TextTransfer.getInstance()});
				cb.dispose();				
			}

		};
	}
	
	private void expandCopySet(final EObject eObject, final List objectsToCopy) {
		
		// All properties should be collapsed
		if(eObject instanceof FeatureValueProvider){
			FeatureValueProvider obj = (FeatureValueProvider) eObject;
			obj.visitSetFeatures(new FeatureValueProvider.Visitor(){
				public Object isSet(EStructuralFeature feature, Object value) {
					if(shouldCopyFeature(feature)){
						Object propertyValue = eObject.eGet(feature);						
						if(propertyValue instanceof EObject){
							copyProperty((EObject)propertyValue,objectsToCopy);
						} else if (propertyValue instanceof List){
							copyList((List)propertyValue,objectsToCopy);
						}
					}
					return null;
				}
			});
		}
		
	}
	
	private boolean shouldCopyFeature(EStructuralFeature feature){
		// By default copy references that are not containment and are not on the free form
		return 
			feature instanceof EReference && 
			!((EReference)feature).isContainment();
	}
	
	private void copyList(List list, List objectsToCopy){
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			Object propertyValue = iter.next();
			if(propertyValue instanceof EObject){
				copyProperty((EObject)propertyValue,objectsToCopy);
			} else if (propertyValue instanceof List){
				copyList((List)propertyValue,objectsToCopy);
			}
		}		
	}
	
	private void copyProperty(EObject ePropertyValue, List objectsToCopy){
		
		if(ePropertyValue instanceof IJavaInstance){
			// This method allows additional work to be done to clean up a particular JavaBean
			normalize((IJavaInstance)ePropertyValue);
		}
		// Add the property value to the set of objects being copied
		if(objectsToCopy.indexOf(ePropertyValue) == -1){
			objectsToCopy.add(ePropertyValue);
			expandCopySet(ePropertyValue,objectsToCopy);
		}
	}
	
	
	protected void normalize(final IJavaInstance javaBean){

	}
}
