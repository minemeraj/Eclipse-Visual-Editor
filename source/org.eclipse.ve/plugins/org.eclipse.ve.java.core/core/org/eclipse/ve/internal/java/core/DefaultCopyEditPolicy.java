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
 *  $Revision: 1.18 $  $Date: 2005-09-14 18:20:07 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.FeatureValueProvider;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.java.impl.JavaPackageImpl;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;

public class DefaultCopyEditPolicy extends AbstractEditPolicy {
	
	public static int nextID = 0;
	protected List objectsToCopy = new ArrayList(20);
	protected List visitedObjects = new ArrayList(20);
	protected EcoreUtil.Copier copier;
	private AnnotationLinkagePolicy annotationLinkagePolicy;
	
	public DefaultCopyEditPolicy(EditDomain anEditDomain){
		annotationLinkagePolicy = anEditDomain.getAnnotationLinkagePolicy();
	}
		
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
				nextID = 0;
				Object template = null;
				// Get the host, copy it and serialize to a stream
				IJavaInstance javaBeanToCopy = (IJavaInstance) getHost().getModel();
				
				// Create a new EMF Resource for the copied object		
				Resource newEMFResource = javaBeanToCopy.eResource().getResourceSet().createResource(javaBeanToCopy.eResource().getURI().appendSegment("" + javaBeanToCopy.hashCode()));				 //$NON-NLS-1$

				// Use the ECore.Copier to put everything from the copy set into the resource set
				copier = new EcoreUtil.Copier(){
					private static final long serialVersionUID = 1L;
					protected void copyContainment(EReference reference,EObject subject,EObject target) {
						if(reference.getName().equals("events")){
							// Do not copy events
						} else {
							super.copyContainment(reference,subject,target);
						}						
					}
				};
				objectsToCopy.clear();
				visitedObjects.clear();				
				copyProperty(null, javaBeanToCopy);
				
				objectsToCopy.add(javaBeanToCopy.eClass().getEStructuralFeature("text"));				
		
				// Expand the references
				copier.copyReferences();
				
				cleanup(javaBeanToCopy);
				
				// Add copies of all the objects we want to copy to the new resource set
				Iterator iter = objectsToCopy.iterator();
				XMIResource xmiResource = (XMIResource)newEMFResource;
				while(iter.hasNext()){
					Object objectToClone = copier.get(iter.next());
					if(objectToClone != null){
						if(xmiResource.getID((EObject)objectToClone) == null){
							xmiResource.setID((EObject)objectToClone,"ID_" + nextID++); //$NON-NLS-1$
						}
						newEMFResource.getContents().add(objectToClone);
					}
				}
		
				// Save to a stream
				HashMap XML_TEXT_OPTIONS = new HashMap(2);
				XML_TEXT_OPTIONS.put(XMLResource.OPTION_PROCESS_DANGLING_HREF,XMLResource.OPTION_PROCESS_DANGLING_HREF_RECORD);
				XML_TEXT_OPTIONS.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
				XML_TEXT_OPTIONS.put(XMLResource.OPTION_LINE_WIDTH, new Integer(100));		
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				try {
					newEMFResource.save(os, XML_TEXT_OPTIONS);
				} catch (IOException e) {
					JavaVEPlugin.log(e);
				}
				// Append the string "{*** VE TRANSFER ***} to the front to make sure that paste only takes object that are valid VE objects
				// We don't use a custom transfer type as it is nice to actually work with the clipboard contents as a String 
				template = JavaVEPlugin.TRANSFER_HEADER + os.toString();
				// Paste the string of the object to copy to the clipboard
				Clipboard cb = new Clipboard(Display.getCurrent());
				cb.setContents(new Object[] {template}, new Transfer[] {TextTransfer.getInstance()});				
				
				cb.dispose();		
				objectsToCopy.clear();
				visitedObjects.clear();
				copier = null;
			}

		};
	}
	
	protected void expandCopySet(final EObject eObject) {
		
		visitedObjects.add(eObject);
		FeatureValueProvider.FeatureValueProviderHelper.visitSetFeatures(eObject, new FeatureValueProvider.Visitor(){
			public Object isSet(EStructuralFeature feature, Object value) {
				copyFeature(feature,value,objectsToCopy);
				return null;
			}
		});		
	}
	
	protected void copyFeature(EStructuralFeature feature, Object object,List objectsToCopy){

		if(object instanceof EObject){
			copyProperty(feature,(EObject)object);
		} else if (object instanceof List){
			copyList(feature,(List)object);
		}
	}
	
	
	protected boolean shouldCopyFeature(EStructuralFeature feature, Object eObject){
		if(feature == null)
			return true;
		// Do not copy things like JavaPackage or JavaMethod
		if(eObject.getClass().getPackage() ==  JavaPackageImpl.class.getPackage())
			return false;
		// Do not copy events
		if(feature.getName().equals("events")) return false;
		// By default copy references that are not containment and are not on the free form
		return  
			feature instanceof EReference
		&& !((EReference)feature).isContainment();
	}
	
	protected boolean shouldExpandFeature(EStructuralFeature feature, Object eObject){
		if(feature != null && feature.getName().equals("events")){
			return false;
		}
		return true;
	}
	
	private void copyList(EStructuralFeature feature, List list){
		
		Iterator iter = list.iterator();
		while(iter.hasNext()){
			Object propertyValue = iter.next();
			if(propertyValue instanceof EObject){
				copyProperty(feature, (EObject)propertyValue);
			} else if (propertyValue instanceof List){
				copyList(feature, (List)propertyValue);
			}
		}		
	}
	
	private void copyProperty(EStructuralFeature feature, EObject ePropertyValue){
	
		// Add the property value to the set of objects being copied		
		if(objectsToCopy.indexOf(ePropertyValue) == -1 && shouldCopyFeature(feature, ePropertyValue)){
			objectsToCopy.add(ePropertyValue);
			copier.copy(ePropertyValue);
			preExpand((IJavaInstance)ePropertyValue);	
			copyAnnotation(ePropertyValue);
		}
		// Keep walking because the object being copied might not have been added to the copy set
		// but it might have children that are
		if(visitedObjects.indexOf(ePropertyValue) == -1 && shouldExpandFeature(feature,ePropertyValue)){
			expandCopySet(ePropertyValue);			
		}
	}
	
	private void copyAnnotation(EObject anObject){
		
		Annotation annotation = annotationLinkagePolicy.getAnnotation(anObject);
		if(annotation != null){
			objectsToCopy.add(annotation);
			copier.copy(annotation);
		}
	}
	
	protected void removeReferenceTo(EObject javaBeanToCopy, String featureName, EObject originalJavaBean) {
	
		EStructuralFeature structuralFeature = javaBeanToCopy.eClass().getEStructuralFeature(featureName);
		javaBeanToCopy.eUnset(structuralFeature);
		removeReferenceTo(javaBeanToCopy,structuralFeature,originalJavaBean);

	}

	protected void removeReferenceTo(EObject javaBeanToCopy, EStructuralFeature feature, EObject originalJavaBean){
		
		Object propertyValue = originalJavaBean.eGet(feature);
		removeReferenceBetween(javaBeanToCopy,propertyValue,originalJavaBean);
		
	}
	
	protected void removeReferenceBetween(EObject javaBeanToCopy, final Object propertyValue, EObject originalJavaBean){
	
		if(propertyValue != null){
			copier.remove(propertyValue);
			// Remove all properties from the copier that are contained by the object just removed
			if(propertyValue instanceof EObject){
				FeatureValueProvider.FeatureValueProviderHelper.visitSetFeatures((EObject) propertyValue, new FeatureValueProvider.Visitor(){
					public Object isSet(EStructuralFeature feature, Object value) {
						if(feature instanceof EReference && ((EReference)feature).isContainment()){
							EObject copiedPropertyValue = (EObject) copier.get(propertyValue);
							removeReferenceBetween(copiedPropertyValue,value,(EObject)propertyValue);
						}
						return null;
					}
				});
			}
		} 
	}
		
	protected void cleanup(IJavaInstance javaBeanToCopy){
		
		// Get the annotation
		Annotation annotation = annotationLinkagePolicy.getAnnotation(javaBeanToCopy);
		if(annotation != null){
			Annotation copiedAnnotation = (Annotation)copier.get(annotation);
			copiedAnnotation.getVisualInfos().clear();
		}
	}
			
    protected void preExpand(final IJavaInstance javaBean){
	 }

}