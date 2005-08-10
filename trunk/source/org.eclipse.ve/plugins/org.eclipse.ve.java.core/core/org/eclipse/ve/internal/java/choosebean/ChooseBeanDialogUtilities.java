/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ChooseBeanDialogUtilities.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-10 23:12:42 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.util.*;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.corext.util.TypeInfo;
import org.eclipse.swt.graphics.Image;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.decorators.ClassDescriptorDecorator;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.core.PrototypeFactory;
import org.eclipse.ve.internal.java.rules.IBeanNameProposalRule;
 
/**
 * 
 * 
 * @since 1.1
 */
public class ChooseBeanDialogUtilities {

	public static final String PI_CONTAINER = "container"; //$NON-NLS-1$	
	public static final String PI_PLUGIN = "plugin"; //$NON-NLS-1$
	public static final IChooseBeanContributor[] NO_CONTRIBS = new IChooseBeanContributor[0];
	private static HashMap contributorNameImageMap = new HashMap();
	
	public static IChooseBeanContributor[] determineContributors(IJavaProject project){
		Map containerIDs = new HashMap();
		Map pluginsIDs = new HashMap();
		try {
			ProxyPlugin.getPlugin().getIDsFound(project, containerIDs, new HashMap(), pluginsIDs, new HashMap());
			List contributorList = new ArrayList();
			IExtensionPoint exp = Platform.getExtensionRegistry().getExtensionPoint(JavaVEPlugin.getPlugin().getBundle().getSymbolicName(),
					"choosebean"); //$NON-NLS-1$
			IExtension[] extensions = exp.getExtensions();

			if (extensions.length > 0) {

				// Ensure that the org.eclipse.ve.java plugins are the first in the list
				IExtension[] orderedExtensions = new IExtension[extensions.length];
				int index = 0;
				String veBaseBundleName = JavaVEPlugin.getPlugin().getBundle().getSymbolicName();
				for (int i = 0; i < extensions.length; i++) {
					if (extensions[i].getNamespace().equals(veBaseBundleName)) {
						orderedExtensions[index++] = extensions[i];
					}
				}
				// Any remaining extensions go to the end
				for (int i = 0; i < extensions.length; i++) {
					if (!extensions[i].getNamespace().equals(veBaseBundleName)) {
						orderedExtensions[index++] = extensions[i];
					}
				}

				for (int ec = 0; ec < orderedExtensions.length; ec++) {
					IConfigurationElement[] configElms = orderedExtensions[ec].getConfigurationElements();
					for (int cc = 0; cc < configElms.length; cc++) {
						IConfigurationElement celm = configElms[cc];
						if (containerIDs.get(celm.getAttributeAsIs(PI_CONTAINER)) == Boolean.TRUE
								|| pluginsIDs.get(celm.getAttributeAsIs(PI_PLUGIN)) == Boolean.TRUE) {
							try {
								IChooseBeanContributor contributor = (IChooseBeanContributor) celm.createExecutableExtension("class"); //$NON-NLS-1$
								contributorList.add(contributor);
							} catch (CoreException e) {
								JavaVEPlugin.log(e, Level.FINE);
							} catch (ClassCastException e) {
								JavaVEPlugin.log(e, Level.FINE);
							}
						}
					}
				}
			}
			return !contributorList.isEmpty() ? (IChooseBeanContributor[]) contributorList.toArray(new IChooseBeanContributor[contributorList.size()]) : NO_CONTRIBS;
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.FINE);
			return NO_CONTRIBS;
		}
	}
	
	/**
	 * Stores the images of contributors based on contributor names
	 * 
	 * @param contrib
	 * @return
	 */
	public static Image getContributorImage(IChooseBeanContributor contrib){
		Image image = null;
		if(contrib!=null){
			String contribName = contrib.getName();
			if(contribName==null)
				contribName = contrib.getClass().getName();
			if(contributorNameImageMap.containsKey(contribName))
				image = (Image) contributorNameImageMap.get(contribName);
			else{
				image = contrib.getImage();
				contributorNameImageMap.put(contribName, image);
			}
		}
		return image;
	}

	/**
	 * Returns whether the passed in selected object is acceptable or not for the purpose of
	 * instantiation. 
	 * 
	 * @param selected  Selected object in the list
	 * @param packageName  The package name where the bean will be instantiated. This is needed to to filter out package protected beans
	 * @param resourceSet  Required to get the EClass of the selected object to test for non-default constructors acceptable to VE (SWT constructors)
	 * @param javaSearchScope  Required to resolve the strings to JDT ITypes
	 * @return
	 * 
	 * @since 1.1
	 */
	public static IStatus getClassStatus(Object selected, String packageName, ResourceSet resourceSet, IJavaSearchScope javaSearchScope){
		Throwable t = null;
		String message = new String(); 
		boolean isInstantiable = true;
		if(selected==null){
			isInstantiable=false;
			//TODO setClassName(null,false);
			message = ChooseBeanMessages.SelectionAreaHelper_SecondaryMsg_NoSelectionMade; 
		}else{
			try{
				TypeInfo ti = (TypeInfo) selected;
				
				boolean isDefaultConstructorSearchRequired = true;
				// The base set of rules is that classes can only be instantiated if they have default constructors
				// Some classes however (such as SWT controls) don't conform to this, however can still be created
				// The base ClassDescriptorDecorator has a key of "org.eclipse.ve.internal.PrototypeFactory" that returns a class name
				// implementing "org.eclipse.ve.internal.PrototypeFactory" that can create the EMF model for this
				// If such a decorator exists then the selection is assumed to be valid
				if(resourceSet!=null){
					EClass selectedEMFClass = Utilities.getJavaClass(ti.getFullyQualifiedName(), resourceSet);
					ClassDescriptorDecorator decorator =
						(ClassDescriptorDecorator) ClassDecoratorFeatureAccess.getDecoratorWithKeyedFeature(
							selectedEMFClass,
							ClassDescriptorDecorator.class,
							PrototypeFactory.PROTOTYPE_FACTORY_KEY);
					if(decorator != null) {
						isDefaultConstructorSearchRequired = false;
					}
				}
				
				IType type = ti.resolveType(javaSearchScope);
				
				boolean isTypePublic = Flags.isPublic(type.getFlags());
				boolean isTypeAbstract = Flags.isAbstract(type.getFlags());
				boolean isInPresentPackage = type.getPackageFragment().getElementName().equals(packageName);
				boolean isTypeStatic = Flags.isStatic(type.getFlags());
				boolean isTypeInner = type.getDeclaringType()!=null;
				boolean isPublicNullConstructorPresent = false;
				boolean isAnyConstructorPresent = false;
				
				// If we don't need a default constructor skip the searching of the methods for one
				if(isDefaultConstructorSearchRequired){
					IMethod[] methods = type.getMethods();
					for(int m=0;m<methods.length;m++){
						if(methods[m].isConstructor() &&
								methods[m].getParameterTypes().length<1 &&
								Flags.isPublic(methods[m].getFlags())){
							isPublicNullConstructorPresent = true;
							//break;
						}
						if(methods[m].isConstructor())
							isAnyConstructorPresent=true;
					}
				}
 				boolean constructorError = isDefaultConstructorSearchRequired && !isPublicNullConstructorPresent && isAnyConstructorPresent;
				
				if(constructorError){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.SelectionAreaHelper_SecondaryMsg_NoPublicNullConstructor); 
				}
				if(!isTypePublic){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.SelectionAreaHelper_SecondaryMsg_TypeNonPublic); 
				}
				if(isTypeAbstract){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.ChooseBeanDialog_Message_AbstractType);  
				}
				if(isTypeInner && (!isTypeStatic)){
					if(message.length()>0)
						message = message.concat(" : "); //$NON-NLS-1$
					message = message.concat(ChooseBeanMessages.ChooseBeanDialog_Message_NonStaticType);  
				}
				// Instantiable if all are true:
				//		+ No Constructor is present OR public null constructor present
				//		+ Type is public OR type is in present package
				//		+ Type is not abstract
				//		+ Enclosing type is not IType OR Enclosing type is IType and this type is static
				isInstantiable = !constructorError && (isTypePublic || isInPresentPackage) && (!isTypeAbstract) && ((!isTypeInner) || (isTypeInner && isTypeStatic));
				if(isInstantiable)
					message = new String();
				//TODO setClassName(getFullSelectionName(ti), isInstantiable) ;
			}catch(JavaModelException e){
				t = e;
				isInstantiable = false;
				if(message.length()>0)
					message = message.concat(" : "); //$NON-NLS-1$
				message = ChooseBeanMessages.SelectionAreaHelper_SecondaryMsg_Unknown_ERROR_; 
			}
		}
		Status status = new Status(
							isInstantiable?IStatus.OK:IStatus.ERROR, 
							JavaVEPlugin.getPlugin().getBundle().getSymbolicName(), 
							isInstantiable?IStatus.OK:IStatus.ERROR, 
							message, 
							t);
		return status;
	}

	public static void setBeanName(EObject obj, String name, EditDomain ed){
		if(obj!=null) {
			AnnotationLinkagePolicy policy = ed.getAnnotationLinkagePolicy();
			Annotation annotation = policy.getAnnotation(obj);
			if(annotation==null)
				annotation = AnnotationPolicy.createAnnotation(obj);
			
			EStringToStringMapEntryImpl sentry = (EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
			sentry.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			sentry.setValue(name);
			annotation.getKeyedValues().add(sentry);
			
			policy.setModelOnAnnotation(obj, annotation);
		}
	}
	
	/**
	 * 
	 * This is a temporary solution, we need to have a name proposal provider - not
	 * hard coded access to CodeGen.
	 */
	public static String getFieldProposal (String className, EditDomain ed, ResourceSet rs) {
		String proposal = new String();
		if (ed != null && rs != null) {
			EObject o = ed.getDiagramData();
			IType javaElement = null;
			// CodeGen will have an adapter on the diagram to resolve the IType
			for (Iterator iter = o.eAdapters().iterator(); iter.hasNext();) {
				Object a = iter.next();
				if (a instanceof IAdaptable) {
					Object je = ((IAdaptable) a).getAdapter(IJavaElement.class);
					// The rule needs the IType for a JavaBased editor to ensure
					// no instance variable duplication
					if (je instanceof IType) {
						javaElement = (IType) je;
						break;
					}
				}
			}
			if (javaElement != null) {
				IBeanNameProposalRule pp = (IBeanNameProposalRule) ed.getRuleRegistry().getRule(IBeanNameProposalRule.RULE_ID); 
				String result = pp.getProspectInstanceVariableName(className, new Object[] { javaElement }, rs);
				if(result!=null)
					proposal = result;
			}
		}
		return proposal;
	}

}
