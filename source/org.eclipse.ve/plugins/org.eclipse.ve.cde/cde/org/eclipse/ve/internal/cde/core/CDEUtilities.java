package org.eclipse.ve.internal.cde.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CDEUtilities.java,v $
 *  $Revision: 1.3 $  $Date: 2004-04-01 21:25:25 $ 
 */


import java.util.*;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.util.EContentsEList;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;

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
			  throw new IndexOutOfBoundsException("index=" + index + ", size=0");
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
	 * Utility method to run the runnable ASAP. I.e. if currently on the display's display thread, execute
	 * immediately. If not, do an asyncexec. This is a combination of syncexec and asyncexec. This way it will
	 * never lock up.
	 * 
	 * @param display
	 * @param runnable
	 * 
	 * @since 1.0.0
	 */
	public static void displayExec(Display display, Runnable runnable) {
		if (Thread.currentThread() == display.getThread())
			runnable.run();
		else
			display.asyncExec(runnable);
	}
	
	/**
	 * Utility method to run the runnable ASAP. Takes a GEF editpart as a convenience to find the display. Then
	 * calls regular displayExec.
	 * @param ep
	 * @param runnable
	 * 
	 * @see CDEUtilities#displayExec(Display, Runnable)
	 * @since 1.0.0
	 */
	public static void displayExec(EditPart ep, Runnable runnable) {
		if (ep.isActive())
			displayExec(ep.getViewer().getControl().getDisplay(), runnable);
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
		IModelChangeController mc = (IModelChangeController) domain.getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
		if (mc != null) {
			int holdState = mc.getHoldState();
			switch (holdState) {
				case IModelChangeController.READY_STATE:
					return null;	// Normal state, which means do rest of normal tests.
				case IModelChangeController.BUSY_STATE:
					return SharedCursors.WAIT;	// Busy, so wait
				case IModelChangeController.NO_UPDATE_STATE:
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
	 * @return the hold state from IModelChangeController
	 * 
	 * @see CDEUtilities#calculateCursor(EditDomain)
	 * @see IModelChangeController#getHoldState()
	 * @since 1.0.0
	 */
	public static int getHoldState(EditDomain domain) {
		IModelChangeController mc = (IModelChangeController) domain.getData(IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
		if (mc != null)
			return mc.getHoldState();
		else
			return IModelChangeController.READY_STATE;
	}
}