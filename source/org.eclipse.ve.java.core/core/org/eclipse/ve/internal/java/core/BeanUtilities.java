/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: BeanUtilities.java,v $
 *  $Revision: 1.24 $  $Date: 2005-02-15 23:23:54 $ 
 */

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.BasicEMap.Entry;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.ILabelProvider;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.impl.KeyedBooleanImpl;
import org.eclipse.ve.internal.cdm.model.CDMModelConstants;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.properties.AbstractAnnotationPropertyDescriptor;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

public class BeanUtilities {
	
	/**
	 * Create the object for the qualified class name.  The resourceSet is used only for reflection to look
	 * for the class, and the object returned is not added to any resource set - the caller must do that if required
	 * The class can be qualified class, e.g. java.lang.String
	 * The java object has not been added to a resource set, however the calls of this method
	 * may want to create a BeanProxyHost adaptor for it.  This is done as part of this method
	 * because if not then it cannot occur because no adaptor factory can be found
	 */
	public static IJavaInstance createJavaObject(
			String qualifiedClassName,
			ResourceSet aResourceSet,
			String initString) {
		return createJavaObject(JavaRefFactory.eINSTANCE.reflectType(qualifiedClassName, aResourceSet), aResourceSet, initString != null ? InstantiationFactory.eINSTANCE.createInitStringAllocation(initString) : null);
	}
	
	/**
	 * Create the object for the qualified class name.  The resourceSet is used only for reflection to look
	 * for the class, and the object returned is not added to any resource set - the caller must do that if required
	 * The class can be qualified class, e.g. java.lang.String
	 * The java object has not been added to a resource set, however the calls of this method
	 * may want to create a BeanProxyHost adaptor for it.  This is done as part of this method
	 * because if not then it cannot occur because no adaptor factory can be found
	 */
	public static IJavaInstance createJavaObject(
		String qualifiedClassName,
		ResourceSet aResourceSet,
		JavaAllocation allocation) {
		return createJavaObject(JavaRefFactory.eINSTANCE.reflectType(qualifiedClassName, aResourceSet), aResourceSet, allocation);
	}

	/**
	 * Create a java object given the class. If the resourceset is null, then it won't have
	 * the beanproxyhost created. However, if a beanproxy host is needed before this gets set into
	 * any resource, then the resource set must be supplied.
	 */
	public static IJavaInstance createJavaObject(
			JavaHelpers javaHelpers,
			ResourceSet aResourceSet,
			String initString) {
		return createJavaObject(javaHelpers, aResourceSet, initString != null ? InstantiationFactory.eINSTANCE.createInitStringAllocation(initString) : null);
	}
	
	/**
	 * Create a java object given the class. If the resourceset is null, then it won't have
	 * the beanproxyhost created. However, if a beanproxy host is needed before this gets set into
	 * any resource, then the resource set must be supplied.
	 */
	public static IJavaInstance createJavaObject(
		JavaHelpers javaHelpers,
		ResourceSet aResourceSet,
		JavaAllocation allocation) {
		IJavaInstance result = null;
		result = (IJavaInstance) javaHelpers.getEPackage().getEFactoryInstance().create(javaHelpers);
		result.setAllocation(allocation);

		if (aResourceSet != null) {
			// This method call on BeanProxyUtilities creates an adaptor in the argument resource set
			BeanProxyUtilities.getBeanProxyHost(result, aResourceSet);
		}

		return result;
	}

	public static IJavaObjectInstance createString(ResourceSet aResourceSet, String unquotedInitializationString) {
		return (IJavaObjectInstance) createJavaObject("java.lang.String", aResourceSet, createStringInitString(unquotedInitializationString)); //$NON-NLS-1$
	}

	/**
	 * Take a string and turn it into a init string for the String class. I.e. quote it and escape any imbedded quotes or escape chars.
	 */
	public static String createStringInitString(String value) {
		StringBuffer sb = new StringBuffer(value.length());
		sb.append('"');
		int sl = value.length();
		for (int i = 0; i < sl; i++) {
			char c = value.charAt(i);
			if (c == '"' || c == '\\')
				sb.append('\\'); // We need to escape it.
			sb.append(c);
		}
		sb.append('"');
		return sb.toString();
	}
	
	/** 
	 * @param newJavaBean   - A newly created JavaBean
	 * @param name		 	- The beanName which will become the field name
	 */
	public static void setBeanName(IJavaInstance newJavaBean, String name){
		
		CDMFactory fact = CDMFactory.eINSTANCE;
		AnnotationEMF an = fact.createAnnotationEMF();
		if(an!=null){
			EStringToStringMapEntryImpl sentry = (EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
			sentry.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			sentry.setValue(name);
			CDEUtilities.putEMapEntry(an.getKeyedValues(), sentry);
			an.setAnnotates(newJavaBean);
		}
	}
	
	protected static BasicEMap.Entry getMapEntry(Annotation annotation, Object key) {
		int keyPos = annotation.getKeyedValues().indexOfKey(key);
		return keyPos != -1 ? (Entry) annotation.getKeyedValues().get(keyPos) : null;
	}
	
	public static Command getSetBeanNameCommand (IJavaInstance newJavaBean, String name, EditDomain domain) {
		// check first that if the "name" is already the current name... no point on driving
		// a rename if not needed
		Annotation annotation = domain.getAnnotationLinkagePolicy().getAnnotation(newJavaBean);		
		if (annotation != null) {
			Entry oldkv = getMapEntry(annotation, NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			if (oldkv!=null)
				if (name.equals(oldkv.getValue())) return null;
		}
		
		EStringToStringMapEntryImpl sentry = (EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
		sentry.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
		sentry.setValue(name);
						
		return AnnotationPolicy.applyAnnotationSetting(newJavaBean, (BasicEMap.Entry)sentry, domain);
	}
	
	/** 
	 * This method will clear the Visual Constraint annotation.  It can remove it all together,
	 * in which case the object will NOT be on the FF, or create an empty one, in which case the 
	 * object will flow to the next location on the FF.
	 * 
	 * @param newJavaBean   - A newly created JavaBean
	 * @param hideFF    	- if set to true, remove visual-constraint which mean that 
	 *                                        the bean will NOT be on the FF.
	 *                        if set to false, create an empty contraints, which mean
	 *                                        that the bean will flow on the FF.
	 */
	public static void setEmptyVisualContraints(IJavaInstance newJavaBean, EditDomain ed, boolean hideFF){
		
		CDMFactory fact = CDMFactory.eINSTANCE;
		
		// Get the current annotation.
		AnnotationEMF.ParentAdapter a = (AnnotationEMF.ParentAdapter) EcoreUtil.getExistingAdapter(newJavaBean, AnnotationEMF.ParentAdapter.PARENT_ANNOTATION_ADAPTER_KEY);
		AnnotationEMF an = a != null ? (AnnotationEMF) a.getParentAnnotation() : null;		
		
		if(an==null) {
			an = fact.createAnnotationEMF();
			an.setAnnotates(newJavaBean);
		}
			
    	// find the primary diagram
    	Diagram d = null ;
    	for (int i=0; i<ed.getDiagramData().getDiagrams().size(); i++) {
    		Diagram di = (Diagram) ed.getDiagramData().getDiagrams().get(i);
    		if (Diagram.PRIMARY_DIAGRAM_ID.equals(di.getId())) {
    			d = di ;
    			break;
    		}
    	}
    	// Set a null Visual Constraint in the Visual Info annotation.
        KeyedBooleanImpl key = (KeyedBooleanImpl) CDMFactory.eINSTANCE.create(CDMPackage.eINSTANCE.getKeyedBoolean());
        if (hideFF)
            key.setValue(Boolean.TRUE);
        else
        	key.setValue(Boolean.FALSE);	
        key.setKey(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
        
        VisualInfo vi = an.getVisualInfo(d) ;
        if (vi == null)  {
            // Create a new Visual Info
	        vi = CDMFactory.eINSTANCE.createVisualInfo() ;	        
            vi.setDiagram(d) ;
            
        }
	    if(vi.getKeyedValues().containsKey(CDMModelConstants.VISUAL_CONSTRAINT_KEY))
	        	vi.getKeyedValues().removeKey(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
	    vi.getKeyedValues().add(key) ;
        an.getVisualInfos().add(vi) ;

	}
	
	public static Command getSetEmptyVisualContraintsCommand (IJavaInstance newJavaBean, boolean hideFF, EditDomain domain) {
		
		KeyedBooleanImpl key = (KeyedBooleanImpl) CDMFactory.eINSTANCE.create(CDMPackage.eINSTANCE.getKeyedBoolean());
		key.setKey(CDMModelConstants.VISUAL_CONSTRAINT_KEY);
        if (hideFF)
            key.setValue(Boolean.TRUE);
        else
        	key.setValue(Boolean.FALSE);
    	// find the primary diagram
    	Diagram d = null ;    	
		for (int i=0; i<domain.getDiagramData().getDiagrams().size(); i++) {
    		Diagram di = (Diagram) domain.getDiagramData().getDiagrams().get(i);
    		if (Diagram.PRIMARY_DIAGRAM_ID.equals(di.getId())) {
    			d = di ;
    			break;
    		}
    	}
        
        return VisualInfoPolicy.applyVisualInfoSetting(newJavaBean, key, domain, d);        
	}

	/**
	 * Answer whether or not this is the class being composed
	 */
	public static boolean isThisPart(IJavaObjectInstance aBean) {

		EStructuralFeature containerSF = aBean.eContainmentFeature();
		return containerSF.getName().equals("thisPart"); //$NON-NLS-1$

	}

	/**
	 * @param javaObjectInstance
	 * @param featureName
	 * Return the feature value.  Either is is set in the EMF model so we get it
	 * or else we go to the target VM
	 * 
	 * @since 1.0.0
	 */
	public static IJavaInstance getFeatureValue(IJavaInstance javaObject, String featureName) {
		EStructuralFeature feature = javaObject.eClass().getEStructuralFeature(featureName);
		if(feature == null) {
			return null;
		} else {
			return getFeatureValue(javaObject,feature);
		}
	}
	/**
	 * @param javaObjectInstance
	 * @param featureName
	 * Return the feature value.  Either is is set in the EMF model so we get it
	 * or else we go to the target VM
	 * 
	 * @since 1.0.0
	 */	
	public static IJavaInstance getFeatureValue(IJavaInstance javaObject, EStructuralFeature feature){
		Object featureValue = javaObject.eGet(feature);
		if(featureValue != null){
			return (IJavaInstance)featureValue;
		} else {
			IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost(javaObject);
			if(!beanProxyHost.isBeanProxyInstantiated()) beanProxyHost.instantiateBeanProxy();
			return beanProxyHost.getBeanPropertyValue(feature);
		}
	}
	

	/**
	 * @param componentType
	 * @param editDomain
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public static String getLabel(IJavaInstance component, EditDomain editDomain) {
		// The label used for the icon is the same one used by the JavaBeans tree view
		ILabelProvider labelProvider = ClassDescriptorDecoratorPolicy.getPolicy(editDomain).getLabelProvider(component.eClass());
		if ( labelProvider != null ) {
			return labelProvider.getText(component);		
		} else { 
			// If no label provider exists use the toString of the target VM JavaBean itself
			return BeanProxyUtilities.getBeanProxy(component).toBeanString(); 
		}
	}	
}
