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
 *  $Revision: 1.31 $  $Date: 2005-07-22 14:05:46 $ 
 */

import java.util.regex.Pattern;

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
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.jcm.*;

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

	private static final String lineSep = System.getProperties().getProperty("line.separator");
	
	/**
	 * Take a string and turn it into a quoted string with escape if necessary. This string can already
	 * have escapes in it. This is useful for the property editors. They show the characters as escaped
	 * already. But the string could of come directly from an actual live string and so it needs
	 * the escapes to be added. This can handle both.
	 * <p>
	 * <b>Note:</b> This incoming string may be a nice looking string that can be converted to
	 * a quoted string. But if there were any escapes in the original string, then that string
	 * is not suitable for being considered the evaluated string. In that case the
	 * string returned from here must be evaluated to get the true evaulated value to 
	 * apply to the live objects. This can be done by just seeing if there are any '\' in
	 * the init string, and if so, then the init string needs to be evaluated to get true 
	 * apply value.
	 * @param value
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static String createStringInitString(String value) {
		return createQuotedInitString(value, true);
	}
	
	public static String createCharacterInitString(char value) {
		return createQuotedInitString(String.valueOf(value), false);
	}

	/**
	 * Local util to quote the 
	 * @param value the value to be quoted
	 * @param isString quote as a string <code>true</code>, or quote as a char.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected static String createQuotedInitString(String value, boolean isString) {
		StringBuffer sb = new StringBuffer(value.length()+2);
		sb.append(isString ? '"' : '\'');
		int sl = value.length();
		for (int i = 0; i < sl; i++) {
			char c = value.charAt(i);
			if (c == lineSep.charAt(0)) {
				if (lineSep.length()>1) {
					if (i+1 < sl) {
						if (lineSep.charAt(1) == value.charAt(i+1)) {
							i++;
							sb.append("\\n");	// Change to linesep.
							continue;
						}
					}
				} else {
					sb.append("\\n");	// Change to linesep.
					continue;					
				}
			}
			switch (c) {
				case '\b':
					sb.append("\\b");
					break;
				case '\t':
					sb.append("\\t");
					break;					
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '"':
					if (isString)
						sb.append("\\\"");	// Put out an escaped quote. But only for string. Not needed for char.
					else
						sb.append('"');
					break;
				case '\'':
					if (!isString)
						sb.append("\\\'");	// Put out escaped quote. But we only do this on chars. Strings can handle it unquoted.
					else
						sb.append('\'');
					break;
				default:	
					if (c == '\\') {
						if (i+1 < sl) {
							// See if we need to escape or not.
							char nextChar = value.charAt(i + 1);
							switch (nextChar) {
								case 'b':
								case 't':
								case 'n':
								case 'r':
								case '"':
								case '\'':
								case '\\':
								case 'u':
									sb.append('\\'); // Don't escape it, just put it out as normal text because the next char will make it a valid escape.
									sb.append(nextChar); // Actually put it out now.
									i++;
									continue;
								default:
									sb.append("\\\\"); // Put it out as escaped. Wanted the backslash to actually be in the string.
									continue;
							}
						} else {
							// We are ending with a '\'. This needs to be escaped to be valid.
							sb.append('\\');
						}
					}
					sb.append(c);
					break;
			}
		}
		sb.append(isString ? '"' : '\'');
		return sb.toString();
	}

	private static final Pattern NEEDS_ESCAPES = Pattern.compile("(.*\b|\t|\r|\n|\"|\'|\\\\)*.*", java.util.regex.Pattern.DOTALL);
	/**
	 * This takes a string that has no escapes and will add escapes in. 
	 * @param value string to escape. <code>null</code> will return as null.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static String getEscapedString(String value) {
		if (value != null && NEEDS_ESCAPES.matcher(value).matches()) {
			StringBuffer sb = new StringBuffer(value.length());
			int sl = value.length();
			boolean multiCharSep = lineSep.length() > 1;
			for (int i = 0; i < sl; i++) {
				char c = value.charAt(i);
				// If multichar line sep, see if next char is part of line sep, if so replace with "\\n".
				// If not multichar line sep, then treat it normally.
				if (multiCharSep && c == lineSep.charAt(0)) {
					if (i+1 < sl) {
						if (lineSep.charAt(1) == value.charAt(i+1)) {
							i++;
							sb.append("\\n");	// Change to linesep.
							continue;
						}
					}
					// Not part of multisep, so treat it normal.
				}
				switch (c) {
					case '\b':
						sb.append("\\b");
						break;
					case '\t':
						sb.append("\\t");
						break;					
					case '\n':
						sb.append("\\n");
						break;
					case '\r':
						sb.append("\\r");
						break;
					case '\\':
						sb.append("\\\\");
						break;
					default:
						sb.append(c);
						break;
				}
			}
			return sb.toString();
		} else
			return value;
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
	
	public static String getBeanName(IJavaInstance javaBean, EditDomain anEditDomain){
		Annotation annotation = anEditDomain.getAnnotationLinkagePolicy().getAnnotation(javaBean);
		if (annotation != null) {
			Entry keyValue = BeanUtilities.getMapEntry(annotation, NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			if(keyValue != null){
				 return(String) keyValue.getValue();
			}
		}
		return null;	
	}
	
	public static BasicEMap.Entry getMapEntry(Annotation annotation, Object key) {
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
						
		return AnnotationPolicy.applyAnnotationSetting(newJavaBean, sentry, domain);
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

	/*
	 * Check to ensure the childComponent we are adding is a Global/Global which means: - it has a global variable (i.e. is a member) - has it's own
	 * initialization method (i.e. getJButton() for a jButton variable)
	 */
	public static boolean isValidBeanLocation(EditDomain domain, EObject childComponent) {
		Object bcm = domain.getDiagramData();
		if (bcm instanceof BeanSubclassComposition) {
			BeanSubclassComposition beanComposition = (BeanSubclassComposition) bcm;
			// Check that it's a global variable
			if (beanComposition.getMembers().indexOf(childComponent) != -1) {
				// Check for Global/Global by ensuring the return from the initialize method is the same
				// as the child component.
				EObject cRef = InverseMaintenanceAdapter.getFirstReferencedBy(childComponent, JCMPackage.eINSTANCE
						.getJCMMethod_Initializes());
				if (cRef != null && cRef instanceof JCMMethod) {
					if (((JCMMethod) cRef).getReturn() == childComponent)
						return true;
				}
			}
		}
		return false;
	}	
}
