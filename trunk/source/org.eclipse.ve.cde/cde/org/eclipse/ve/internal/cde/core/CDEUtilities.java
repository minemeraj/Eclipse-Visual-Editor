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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: CDEUtilities.java,v $
 *  $Revision: 1.13 $  $Date: 2005-08-11 21:00:29 $ 
 */


import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.gef.*;
import org.eclipse.swt.graphics.Cursor;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

/**
 * Utilities for CDE functions that are not found in a more logical place.
 */
public class CDEUtilities {

	/**
	 * Used to walk through the specified features of the EObject. 
	 * For list settings, each entry in the list will be individually returned.
	 * 
	 * Use basicList and basicIterator to not have the proxies resolve.
	 * 
	 * @parameter shared true will go through the shared references too.
	 */
	public static class ESettingsEList extends EContentsEList {

		protected boolean shared;
		
		public ESettingsEList(EObject eObject, boolean shared) {
			this(eObject, (EStructuralFeature[]) ((BasicEList) eObject.eClass().getEAllStructuralFeatures()).data(), shared);
		}
		
		public ESettingsEList(EObject eObject, EStructuralFeature[] eStructuralFeatures, boolean shared) {
			super(eObject, eStructuralFeatures);
			this.shared = shared;
		}

		public class FeatureIteratorImpl extends EContentsEList.FeatureIteratorImpl {
			protected boolean shared;
			public FeatureIteratorImpl(EObject eObject, EStructuralFeature[] eStructuralFeatures, boolean shared) {
				super(eObject, eStructuralFeatures);
				this.shared = shared;
			}

			protected boolean isIncluded(EStructuralFeature eStructuralFeature) {
				return ESettingsEList.this.isIncluded(eStructuralFeature);
			}
		}

		public class ResolvingFeatureIteratorImpl extends FeatureIteratorImpl {
			public ResolvingFeatureIteratorImpl(EObject eObject, EStructuralFeature[] eStructuralFeatures, boolean shared) {
				super(eObject, eStructuralFeatures, shared);
			}

			protected boolean resolve() {
				return true;
			}
		}

		protected boolean isIncluded(EStructuralFeature eStructuralFeature) {
			return !shared ? ((eStructuralFeature instanceof EAttribute) || ((EReference) eStructuralFeature).isContainment()) : true;
		}

		protected ListIterator newListIterator() {
			return this.resolve()
				? new ResolvingFeatureIteratorImpl(eObject, eStructuralFeatures, shared)
				: new FeatureIteratorImpl(eObject, eStructuralFeatures, shared);
		}

		public List basicList() {
			return new ESettingsEList(eObject, eStructuralFeatures, shared) {
				protected boolean resolve() {
					return false;
				}
			};
		}

		public ListIterator basicListIterator() {
			if (eStructuralFeatures == null)
			{
			  return FeatureIteratorImpl.EMPTY_ITERATOR;
			}			
			return new FeatureIteratorImpl(eObject, eStructuralFeatures, shared);
		}
		
		public Iterator basicIterator() {
			return basicListIterator();		
		}
		
		public ListIterator basicListIterator(int index)
		{
		  if (eStructuralFeatures == null)
		  {
			if (index < 0 || index > 1)
			{
			  throw new IndexOutOfBoundsException("index=" + index + ", size=0"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			return FeatureIteratorImpl.EMPTY_ITERATOR;
		  }

		  ListIterator result = basicListIterator();
		  for (int i = 0; i < index; ++i)
		  {
			result.next();
		  }
		  return result;
		}		
	}
	/**
	 * Answer whether this SET notification is really an UNSET.
	 * The definition of a SET that is really an UNSET is:
	 *   A SET notification, where the structural feature is not unsettable AND the notification is a RESET (i.e. set back to default value).
	 * For many purposes this can be considered to be an unset.
	 * 
	 * If this is called for an ADD notification, this will always return false. This is OK because an ADD is always a set.
	 * 
	 * If you know the feature is not unsettable (because you've tested for a specific feature first back in your code), then you don't need to call
	 * this method and can just do !msg.isReset() instead.
	 */
	public static boolean isUnset(Notification msg) {
		if (msg.getFeature() instanceof EStructuralFeature) {
			EStructuralFeature sf = (EStructuralFeature) msg.getFeature();
			return !sf.isUnsettable() && msg.isReset();
		} else
			return false;
	} 

	/**
	 * Add/Replace a Map entry into an EMap. This will replace one by the same key if already out there or
	 * it will add it if not.
	 * 
	 * Typically an replacement in CDE is desired because listeners are listening for changes to the EMap, not
	 * individual value changes. The contract is usually replace map entry not change value.
	 */
	public static Map.Entry putEMapEntry(EMap map, Map.Entry entry) {
		int oldEntryIndex = map.indexOfKey(entry.getKey());
		if (oldEntryIndex != -1)
			return (Map.Entry) map.set(oldEntryIndex, entry);
		else
			map.add(entry);
		return null;
	}
	
	/**
	 * Return the ModelAdapterFactory for the given domain.
	 */
	public static IModelAdapterFactory getModelAdapterFactory(EditDomain domain) {
		return (IModelAdapterFactory) domain.getData(IModelAdapterFactory.MODEL_FACTORY_KEY);
	}

	
	/**
	 * Return the ModelAdapterFactory for the given editpart (only used to find the domain).
	 */
	public static IModelAdapterFactory getModelAdapterFactory(EditPart editpart) {
		return getModelAdapterFactory(EditDomain.getEditDomain(editpart));
	}
	
	/**
	 * Set the ModelAdapterFactory for the given domain.
	 */
	public static void setModelAdapterFactory(EditDomain domain, IModelAdapterFactory factory) {
		domain.setData(IModelAdapterFactory.MODEL_FACTORY_KEY, factory);
	}
	
	public static String lowCaseFirstCharacter(String name) {
		if (name == null || name.length() == 0)
			return null;
		// Don't lowercase if second letter and third letter also uppercase, i.e. URL will stay URL and not become uRL.
		// But we want JPanel to become jPanel because that looks better.
		if (Character.isUpperCase(name.charAt(0)) && (name.length() < 3 || !(Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(2))))) {
			char[] a = name.toCharArray();
			a[0] = Character.toLowerCase(a[0]);
			return new String(a);
		} else
			return name;
	}
	
	
	/**
	 * Utility method to run the runnable at end of current transaction. Takes a GEF editpart as a convenience to find the model change controller.
	 * It will then queue it up to the end of the transaction.
	 * @param ep the editpart. If the editpart is not active then nothing added.
	 * @param Object once key to run once, it will be merged with the editpart, so that the combination of the two will determine the once. 
	 * @param runnable the runnable. It is best to use an {@link EditPartRunnable} for this so that it will make sure the editpart is still active at the time of execution.
	 * 
	 * @see ModelChangeController#execAtEndOfTransaction(Runnable, Object)
	 * @see ModelChangeController#createHashKey(Object, Object)
	 * @since 1.0.0
	 */
	public static void displayExec(EditPart ep, Object once, Runnable runnable) {
		if (ep.isActive()) {
			((ModelChangeController) EditDomain.getEditDomain(ep).getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).execAtEndOfTransaction(runnable, ModelChangeController.createHashKey(ep, once));
		}
	}
	
	/**
	 * Utility method to run the runnable at end of current transaction. Takes a GEF editpart as a convenience to find the model change controller.
	 * It will then queue it up to the end of the transaction.
	 * @param ep the editpart. If the editpart is not active then nothing added.
	 * @param runnable the runnable. It is best to use an {@link EditPartRunnable} for this so that it will make sure the editpart is still active at the time of execution.
	 * 
	 * @see ModelChangeController#execAtEndOfTransaction(Runnable)
	 * @since 1.0.0
	 */
	public static void displayExec(EditPart ep, Runnable runnable) {
		if (ep.isActive()) {
			((ModelChangeController) EditDomain.getEditDomain(ep).getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY)).execAtEndOfTransaction(runnable);
		}
	}	
	
	/**
	 * This is to be used in GEF Tools in CDE so that the cursor can be set to busy or not allowed as necessary depending on 
	 * the state of editor. The default calculateCursor in GEF AbstractTool only checks the state of the command, i.e. can
	 * the command execute or not. In CDE this isn't sufficient. The domain can also be in a state to prevent the
	 * execution of the command. We want the appropriate cursor to come up while dragging.
	 * <p>
	 * The way to use this is in any Tool that you write for the CDE to overwrite the calculateCursor() command with:
	 * <pre>
	 * 	protected Cursor calculateCursor() {
	 * 		Cursor result = CDEUtilities.calculateCursor((org.eclipse.ve.internal.cde.core.EditDomain) getDomain());
	 * 		if (result != null)
	 * 			return result;
	 * 		super.calculateCursor();
	 * 	}
	 * </pre>
	 * <p>
	 * CDE provides some standard Tools which already use this and these can be used as superclasses or as they are.
	 * 
	 * @param domain CDE edit domain
	 * @return cursor to use or <code>null</code> if should call super.calculateCursor().
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#calculateCursor()
	 * @see CDECreationTool
	 * @since 1.0.0
	 */
	public static Cursor calculateCursor(EditDomain domain) {
		ModelChangeController mc = (ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
		if (mc != null) {
			int holdState = mc.getHoldState();
			switch (holdState) {
				case ModelChangeController.READY_STATE:
					return null;	// Normal state, which means do rest of normal tests.
				case ModelChangeController.BUSY_STATE:
					return SharedCursors.WAIT;	// Busy, so wait
				case ModelChangeController.NO_UPDATE_STATE:
				default:
					// For now default goes in here too. May want to extend capability to have more cursors depending on state.
					return SharedCursors.NO;	// Update not allowed.
			}
		}
		return null;
	}
	
	/**
	 * This is used to simply see if the domain is in a holding state. In those states commands should not be executed.
	 * This can be used in Tools in conjunction with calculateCursor to quickly see if calculateCursor needs to be done
	 * instead of other code. For example in CDESelectionTool this is used in the drag tracker redirects so that when in
	 * hold state it doesn't send requests to the tracker and causes a refreshCursor to occur so correct cursor shows up.
	 * 
	 * @param domain
	 * @return the hold state from ModelChangeController
	 * 
	 * @see CDEUtilities#calculateCursor(EditDomain)
	 * @see ModelChangeController#getHoldState()
	 * @since 1.0.0
	 */
	public static int getHoldState(EditDomain domain) {
		ModelChangeController mc = (ModelChangeController) domain.getData(ModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
		if (mc != null)
			return mc.getHoldState();
		else
			return ModelChangeController.READY_STATE;
	}
	
	/**
	 * Editpart name path. This is used to reach a specific editpart via the model names of the editparts from top to 
	 * bottom to reach the desired editpart. This is used to allow query of current selected edit part, getting it
	 * into name format, and then after a reload using the name path to try to find the specified edit part again.
	 * Can't just use the editpart itself because after reload it will be a physically different editpart.
	 * <p>
	 * This would be used only in models where the name in compositions may not be unique. If they were unique, then
	 * the editpart can be found directly. Though it can be used in unique models too, it would just add overhead.
	 * <p>
	 * There is a possibility that some editpart models may not have a name. In that case we will use index into the
	 * parent. Hopefully that will work well.
	 *  
	 * @since 1.0.0
	 */
	public static class EditPartNamePath {
		/**
		 * Path of names to reach specified editpart from top editpart. First entry will be child of top editpart.
		 * Top editpart is the contents of the root edit part.
		 */
		public String[] namePath;
	}
	
	/**
	 * Return the path to the editpart from the top editpart.
	 * 
	 * @param ep
	 * @param domain
	 * @return the path (through names) to the editpart, or <code>null</code> if this is the top editpart (defined as root editpart contents).
	 * @throws IllegalArgumentException if editpart is not active.
	 * 
	 * @since 1.0.0
	 */
	public static EditPartNamePath getEditPartNamePath(EditPart ep, EditDomain domain) {
		if (!ep.isActive())
			throw new IllegalArgumentException("editpart must be active."); //$NON-NLS-1$
		
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		List path = new ArrayList();
		EditPart top = ep.getRoot().getContents();
		for (; ep != top; ep = ep.getParent()) {
			Annotation a = policy.getAnnotation(ep.getModel());
			if (a != null) {
				String name = (String) a.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
				if (name != null && name.length() > 0) {
					path.add(name);
					continue;
				}
			}
			// No name set, or no annotation, use index into parent.			
			EditPart parent = ep.getParent();			
			path.add('{'+String.valueOf(parent.getChildren().indexOf(ep))+'}');
		}
		
		if (path.isEmpty())
			return null;	// It is the top editpart.
		
		EditPartNamePath result = new EditPartNamePath();
		result.namePath = new String[path.size()];
		// Since it was built bottom up, but we want it top down, we go in reverse.
		int pi = 0;
		for (int i = path.size()-1; i >= 0 ; i--) {
			result.namePath[pi++] = (String) path.get(i);
		}
		
		return result;
	}
	
	/**
	 * Find the edit part given by the path from the given root editpart.
	 * 
	 * @param path the path, or <code>null</code> if root contents.
	 * @param viewer the viewer to search in
	 * @param domain
	 * @return the editpart or <code>null</code> if editpart can't be found.
	 * 
	 * @since 1.0.0
	 */
	public static EditPart findEditpartFromNamePath(EditPartNamePath path, EditPartViewer viewer, EditDomain domain) {
		EditPart ep = viewer.getContents();		
		if (path == null)
			return ep;
		AnnotationLinkagePolicy policy = domain.getAnnotationLinkagePolicy();
		String[] namePath = path.namePath;
nextName:	
		for (int i = 0; i < namePath.length; i++) {
			List children = ep.getChildren();	
			String name = namePath[i];
			if (name.charAt(0) != '{' || name.charAt(name.length()-1) != '}') {
				// Standard name.
				for (int j = 0; j < children.size(); j++) {
					EditPart child = (EditPart) children.get(j);
					Annotation a = policy.getAnnotation(child.getModel());
					if (a != null) {
						String childName = (String) a.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
						if (name.equals(childName)) {
							ep = child;	// Found it
							continue nextName;
						}
					}
				}
			} else {
				// Using index format.
				String indexString = name.substring(1, name.length()-1);
				try {
					int index = Integer.parseInt(indexString);
					if (index < children.size()) {
						ep = (EditPart) children.get(index); // Found it.
						continue nextName;
					}
				} catch (NumberFormatException e) {
				}
			}
			
			return null;	// If it got here, then it didn't find it at this level, so it is no longer available.
		}
		
		return ep;
	}
	
	/**
	 * Helper to find an annotation of the given class type on the given model.
	 * @param model
	 * @param annotationType
	 * @return
	 * 
	 * @since 1.0.0
	 */
	public static EAnnotation findDecorator(EModelElement model, Class annotationType) {
		List as = model.getEAnnotations();
		int s = as.size();
		for (int i=0; i<s; i++) {
			Object next = as.get(i);
			if (annotationType.isInstance(next))
				return (EAnnotation) next;
		}
		return null;
	}
	
	/**
	 * Strip newlines/tabs (either Win or Unix format) from the string, replacing with the given character.
	 * @param in
	 * @param replaceNewline char to replace new lines with. If <code>0x00</code>, then don't replace newlines.
	 * @param replaceTab char to replace tabs with. If <code>0x00</code>, then don't replace tabs.
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public static String stripNewLineTabs(String in, char replaceNewline, char replaceTab) {
		boolean doNL = replaceNewline != 0x00;
		boolean doTab = replaceTab != 0x00;
		int nl = doNL ? in.indexOf('\n') : -1;
		int tab = doTab ? in.indexOf('\t') : -1;
		if (nl != -1 || tab != -1) {
			char[] inchar = in.toCharArray();
			int length = inchar.length;
			int ndx;
			if (nl != -1)
				if (tab != -1)
					ndx = Math.min(nl, tab);
				else
					ndx = nl;
			else
				ndx = tab;
			for (; ndx < inchar.length; ndx++) {
				if (doNL && inchar[ndx] == '\n') {
					if (ndx > 0 && inchar[ndx-1] == '\r') {
						// Got \r\n type newline.
						inchar[ndx-1] = replaceNewline;
						System.arraycopy(inchar, ndx+1, inchar, ndx, inchar.length-ndx-1);	// Move rest after the \n back over \n
						length--;	// Reduce final length by one.
					} else {
						// Got \n type newline
						inchar[ndx] = replaceNewline;
					}
				} if (doTab && inchar[ndx] == '\t') {
					// Got \t.
					inchar[ndx] = replaceTab;
				}
			}
			return new String(inchar, 0, length);
		} else
			return in;
	}
}
