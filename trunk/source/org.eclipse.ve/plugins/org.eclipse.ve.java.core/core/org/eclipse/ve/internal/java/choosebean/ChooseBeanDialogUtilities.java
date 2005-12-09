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
 *  $RCSfile: ChooseBeanDialogUtilities.java,v $
 *  $Revision: 1.13 $  $Date: 2005-12-09 21:55:57 $ 
 */
package org.eclipse.ve.internal.java.choosebean;

import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.core.search.IJavaSearchScope;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.proxy.core.ContributorExtensionPointInfo;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin;
import org.eclipse.jem.internal.proxy.core.IConfigurationContributionInfo.ContainerPaths;
import org.eclipse.jem.internal.proxy.core.ProxyPlugin.FoundIDs;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * 
 * 
 * @since 1.1
 */
public class ChooseBeanDialogUtilities {

	/**
	 * Comment for <code>JAVA_VE_SYMBOLICNAME</code>
	 * 
	 * @since 1.2.0
	 */
	private static final String JAVA_VE_SYMBOLICNAME = JavaVEPlugin.getPlugin().getBundle().getSymbolicName();
	/**
	 * Comment for <code>CONTRIBUTOR_EXTENSIONPOINT</code>
	 * 
	 * @since 1.2.0
	 */
	private static final String CONTRIBUTOR_EXTENSIONPOINT = JAVA_VE_SYMBOLICNAME+".choosebean"; //$NON-NLS-1$
	public static final String PI_CONTAINER = "container"; //$NON-NLS-1$	
	public static final String PI_PLUGIN = "plugin"; //$NON-NLS-1$
	public static final IChooseBeanContributor[] NO_CONTRIBS = new IChooseBeanContributor[0];
	private static ContributorExtensionPointInfo contributorInfo;
	
	private static void processContributorExtensionPoint() {
		contributorInfo = ProxyPlugin.processContributionExtensionPoint(CONTRIBUTOR_EXTENSIONPOINT);
	}
	
	public static IChooseBeanContributor[] determineContributors(IJavaProject project){
		if (contributorInfo == null)
			processContributorExtensionPoint();
		
		try {
			if (!contributorInfo.containerPathContributions.containerIdToContributions.isEmpty() || !contributorInfo.pluginToContributions.isEmpty()) {
				FoundIDs foundIds = ProxyPlugin.getPlugin().getIDsFound(project);
				List contributorList = new ArrayList();
				List veContributorList = new ArrayList();	// JavaVe contributors, so they always show first.
				for (Iterator containers = foundIds.containerIds.entrySet().iterator(); containers.hasNext();) {
					Entry entry = (Entry) containers.next();
					ContainerPaths paths = (ContainerPaths) entry.getValue();
					IConfigurationElement[] configs = (IConfigurationElement[]) contributorInfo.containerPathContributions.getContributors((String) entry.getKey(), paths.getVisibleContainerPaths());
					addContributor(contributorList, veContributorList, configs);
				}
				
				for (Iterator plugins = foundIds.pluginIds.entrySet().iterator(); plugins.hasNext();) {
					Entry entry = (Entry) plugins.next();
					if (entry.getValue() == Boolean.TRUE) {
						IConfigurationElement[] configs = (IConfigurationElement[]) contributorInfo.pluginToContributions.get(entry.getKey());
						addContributor(contributorList, veContributorList, configs);
					}
				}
				if (!veContributorList.isEmpty() || !contributorList.isEmpty() ) {
					veContributorList.addAll(contributorList);
					return (IChooseBeanContributor[]) veContributorList.toArray(new IChooseBeanContributor[veContributorList.size()]);
				} else
					return NO_CONTRIBS;
			}
		} catch (JavaModelException e) {
			JavaVEPlugin.log(e, Level.FINE);
		}
		return NO_CONTRIBS;
	}

	/**
	 * @param contributorList
	 * @param veContributorList
	 * @param configs
	 * @throws InvalidRegistryObjectException
	 * @throws CoreException 
	 * 
	 * @since 1.2.0
	 */
	private static void addContributor(List contributorList, List veContributorList, IConfigurationElement[] configs) {
		for (int i = 0; configs!=null && i < configs.length; i++) {
			try {
				IChooseBeanContributor contributor = (IChooseBeanContributor) configs[i].createExecutableExtension("class"); //$NON-NLS-1$
				if (configs[i].getNamespace().equals(JAVA_VE_SYMBOLICNAME))
					veContributorList.add(contributor);
				else
					contributorList.add(contributor);

			} catch (CoreException e) {
				JavaVEPlugin.log(e, Level.FINE);
			} catch (ClassCastException e) {
				JavaVEPlugin.log(e, Level.FINE);
			}
		}
	}
	
	/**
	 * Returns whether the passed in selected object is acceptable or not for the purpose of
	 * instantiation. 
	 * 
	 * @param selected  Selected object in the list
	 * @param packageName  The package name where the bean will be instantiated. This is needed to to filter out package protected beans
	 * @param resourceSet  Required to get the EClass of the selected object to test for non-default constructors acceptable to VE (SWT constructors)
	 * @param javaSearchScope  Required to resolve the strings to JDT ITypes
	 * @param name
	 * @return
	 * 
	 * @since 1.2.0
	 */
	public static IStatus getClassStatus(IType selected, String packageName, ResourceSet resourceSet, IJavaSearchScope javaSearchScope, String name, EditDomain domain){
		IStatus status = getClassStatus(selected, packageName, resourceSet, javaSearchScope, domain);
		if(status.isOK()){
			IStatus nameStatus = JavaConventions.validateFieldName(name);
			if(!nameStatus.isOK()){
				status = nameStatus;
			}
		}
		return status;
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
	private static IStatus getClassStatus(IType selected, String packageName, ResourceSet resourceSet, IJavaSearchScope javaSearchScope, EditDomain editDomain){
		Throwable t = null;
		String message = new String(); 
		boolean isInstantiable = true;
		if(selected==null){
			isInstantiable=false;
			//TODO setClassName(null,false);
			message = ChooseBeanMessages.SelectionAreaHelper_SecondaryMsg_NoSelectionMade; 
		}else{
			try{
				boolean isDefaultConstructorSearchRequired = true;
				// The base set of rules is that classes can only be instantiated if they have default constructors
				// Some classes however (such as SWT controls) don't conform to this, however can still be created
				// If there is a containment handler then the selection is assumed to be valid
				if(resourceSet!=null){
					IModelAdapterFactory modelAdapterFactory = CDEUtilities.getModelAdapterFactory(editDomain);
					if (modelAdapterFactory != null) {
						EClass selectedEMFClass = Utilities.getJavaClass(selected.getFullyQualifiedName('.'), resourceSet);
						if (modelAdapterFactory.typeHasAdapter(selectedEMFClass, IContainmentHandler.class)) {
							isDefaultConstructorSearchRequired = false;
						}
					}
				}
				
				IType type = selected;
				
				boolean isTypePublic = Flags.isPublic(type.getFlags());
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
				isInstantiable = !constructorError && (isTypePublic || isInPresentPackage) && ((!isTypeInner) || (isTypeInner && isTypeStatic));
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
							JAVA_VE_SYMBOLICNAME, 
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
}
