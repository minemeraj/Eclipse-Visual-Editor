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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: ComponentProxyAdapter.java,v $
 *  $Revision: 1.19 $  $Date: 2005-04-05 21:53:36 $ 
 */
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.awt.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.*;

import org.eclipse.ve.internal.jfc.common.ImageDataConstants;
/**
 * Live Bean Adapter for java.awt.Component.
 * Implements IComponentNotifier so that edit parts can
 * add listeners to this component.
 * Creation date: (1/11/00 2:39:30 PM)
 * @author: Richard Kulp
 */
public class ComponentProxyAdapter extends BeanProxyAdapter implements IVisualComponent, IComponentProxyHost {

	// Visibility, locationcould be applied, but we don't want those showing up as settings.
	// These are used with the freeform to position, and make visible the component, but those are
	// edittime settings are not reflected in the runtime code generation.

	private IJavaInstance fDefaultVisibility = null; // Default value of visibility if it has been overrideen, queried from live object.
	private IJavaInstance fVisibilityToUse = null; // Visibility to use when live object created, it is the override value 
	
	private IJavaInstance fJLocationToUse = null; // Location to use when live object created, this is the override value
												 // For example for live windows this is set to an off-screen value.  null if not overriden
	private Point fPLocationToUse = null;		 // As above but stored as a Point rather than an IJavaInstance

	protected ImageDataCollector fImageDataCollector = null;
	protected final Object imageAccessorSemaphore = new Object();	// [73930] Semaphore for access to image stuff, can't use (this) because that is also used for instantiation and deadlock can occur.
	// Image collector, if one set for this component, may not have one if the image is collected by a parent.

	protected List fComponentListeners = null; // Listeners for IComponentNotification.
	protected ComponentManager fComponentManager; // The listener on the IDE
	protected ImageNotifierSupport imSupport;
	protected IComponentProxyHost fParentComponent = null; // Parent's ComponentProxyAdaptor.

	// Image validity flag it must be checked/changed only under synchronization on this.
	protected int fImageValid = INVALID;
		protected static final int INVALID = 1, // Image is invalid, even if currently collecting, that will be aborted and thrown away.
		INVALID_COLLECTING = 2, // Image is invalid, but currently collecting to make it valid.
	VALID = 3; // Image is valid.

	// Need these features often, but they depend upon the class we are in,
	// can't get them as statics because they would be different for each Eclipse project.
	protected EStructuralFeature sfComponentVisible, sfComponentLocation, sfComponentSize, sfComponentBounds;
    private IPointBeanProxy fInitialLocation;

	/**
	 * ComponentProxyAdaptor constructor comment.
	 */
	public ComponentProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * @see Adapter#setTarget(Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sfComponentVisible = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_VISIBLE);
			sfComponentLocation = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_LOCATION);
			sfComponentSize = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_SIZE);
			sfComponentBounds = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_BOUNDS);
		}
	}
	/**
	 * addComponentManager method comment.
	 */
	public synchronized void addComponentListener(IVisualComponentListener aListener) {
		// Add to listener list, then if we have a componentNotifierProxy, add
		// this to its list. If not, then see if we have a beanProxy. If so, then
		// create a componentNotifierProxy and add this listener to it.
		// We keep a copy of the listener list here so that if the component
		// is not yet created, or is recreated, we can add back in all of the
		// listeners.

		if (fComponentListeners == null)
			fComponentListeners = new ArrayList(1);

		fComponentListeners.add(aListener);
		if (fComponentManager != null) {
			fComponentManager.addComponentListener(aListener);
		} else {
			if (getVisualComponentBeanProxy() != null && getVisualComponentBeanProxy().isValid()) {
				createComponentManager(); // Create the component listener on the bean and add all
			}
		}
	}
	/**
	 * addImageListener method comment.
	 */
	public synchronized void addImageListener(IImageListener aListener) {
		if (imSupport == null)
			imSupport = new ImageNotifierSupport();
		imSupport.addImageListener(aListener);
		if (getVisualComponentBeanProxy() != null && getVisualComponentBeanProxy().isValid())
			createImageCollector();

	}

	/*
	 * applyNullLayoutConstraints - This is called by ContainerProxyAdapter in the
	 * case of a null layout manager to have the bounds handling the
	 * defaults correctly now that we have a container to go against.
	 */
	public void applyNullLayoutConstraints() {				
		EObject ref = (EObject) target;
		// Reapply the appropriate setting in case it is marked with defaults.
		if (ref.eIsSet(sfComponentBounds) && isValidFeature(sfComponentBounds))
			appliedBounds(sfComponentBounds, ref.eGet(sfComponentBounds), -1);
		else {
			if (ref.eIsSet(sfComponentSize) && isValidFeature(sfComponentSize))
			   appliedSize(sfComponentSize, ref.eGet(sfComponentSize), -1);
	        if (ref.eIsSet(sfComponentLocation) && isValidFeature(sfComponentLocation))
	           appliedLocation(sfComponentLocation,ref.eGet(sfComponentLocation),-1) ;
		}
	}
	

	/**
	 * applied: A setting has been applied to the mof object,
	 * We are testing for visibility and location. We are testing to
	 * see if we are allowing them to go through.
	 */
	protected void applied(EStructuralFeature as, Object newValue, int position) {
		// constraints is special as are visible/location.  For the latter 
		// we only want to apply it if the apply visibility/location flag is true.
		if (!isBeanProxyInstantiated())
			return; // Nothing to apply to yet or could not construct.
		if (as == sfComponentBounds)
			appliedBounds(as, newValue, position); // Handle bounds
		else if (as == sfComponentSize)
			appliedSize(as, newValue, position); // Handle size
	    else if (as == sfComponentLocation)
	        appliedLocation(as, newValue, position);
		else if (as != sfComponentVisible || fVisibilityToUse == null)
			super.applied(as, newValue, position); // We letting the settings go through
	}

	protected void appliedSize(final EStructuralFeature as, Object newValue, int position) {
		IDimensionBeanProxy dim = (IDimensionBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) newValue);
		if (dim != null && (dim.getWidth() == -1 || dim.getHeight() == -1)) {
			IBeanProxy containerBeanProxy = BeanAwtUtilities.invoke_getParent(getBeanProxy());
			if (containerBeanProxy == null)
				return; // Not yet within a container, can't do this function yet, and we shouldn't apply the values since they mean nothing.

			// If the either width or height is -1, then we need to adjust to preferred size.
			// This will do an actual set of the setting in the EMF object. This is OK because
			// -1 is not a valid setting anyway, and doing an undo followed by redo will come back
			// with -1, and then at this point in time it would reconstruct the setting.
			Dimension size = new Dimension(dim.getWidth(), dim.getHeight());
			if (NullLayoutEditPolicy.adjustForPreferredSize(getBeanProxy(), size)) {
				String initString = DimensionJavaClassCellEditor.getJavaInitializationString(size.width, size.height, JFCConstants.DIMENSION_CLASS_NAME);
				final IJavaInstance dimensionBean = BeanUtilities.createJavaObject(JFCConstants.DIMENSION_CLASS_NAME, ((EObject) target).eResource().getResourceSet(), initString);
				Display.getDefault().asyncExec(new Runnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						// We may not be within the context of a change control, so we need to get a controller to handle the change.
						getModelChangeController().doModelChanges(new Runnable() {
							public void run() {
								// Set the constraints on the component bean.  This will change the size of the component
								// Because we will be called back with notify and apply the constraints rectangle to the live bean
								//
								// Note: Need to use RuledCommandBuilder.
								RuledCommandBuilder cbld = new RuledCommandBuilder(getBeanProxyDomain().getEditDomain());
								cbld.applyAttributeSetting((EObject) target, as, dimensionBean);
								cbld.getCommand().execute();
							}
						}, true);
					}
				});				
				return; // Let the notify back from the set here do the actual apply.
			}
		}

		super.applied(as, newValue, position);
	}

	protected void appliedLocation(final EStructuralFeature as, Object newValue, int position) {
		IPointBeanProxy pnt = (IPointBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) newValue);
		if (pnt != null && (pnt.getX() == -1 || pnt.getY() == -1)) {
			IBeanProxy containerBeanProxy = BeanAwtUtilities.invoke_getParent(getBeanProxy());
			if (containerBeanProxy == null)
				return; // Not yet within a container, can't do this function yet, and we shouldn't apply the values since they mean nothing.

			// If the either x or y is -1, then we need to adjust 
			Point location = new Point(pnt.getX(), pnt.getY());
			if (NullLayoutEditPolicy.adjustForPreferredLocation(getBeanProxy(), location,5,5)) {
				String initString = PointJavaClassCellEditor.getJavaInitializationString(location.x, location.y,JFCConstants.POINT_CLASS_NAME);
				final IJavaInstance pointBean = BeanUtilities.createJavaObject(JFCConstants.POINT_CLASS_NAME, ((EObject) target).eResource().getResourceSet(), initString);//$NON-NLS-1$
				Display.getDefault().asyncExec(new Runnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						// We may not be within the context of a change control, so we need to get a controller to handle the change.
						getModelChangeController().doModelChanges(new Runnable() {
							public void run() {
								// Set the constraints on the component bean.  This will change the size of the component
								// Because we will be called back with notify and apply the constraints rectangle to the live bean
								//
								// Note: Need to use RuledCommandBuilder.
								RuledCommandBuilder cbld = new RuledCommandBuilder(getBeanProxyDomain().getEditDomain());
								cbld.applyAttributeSetting((EObject) target, as, pointBean);
								cbld.getCommand().execute();

							}
						}, true);
					}
				});				
				return; // Let the notify back from the set here do the actual apply.
			}
		}

		if (fJLocationToUse == null)
			super.applied(as, newValue, position);	// We want location to be applied.
	}



	protected void appliedBounds(final EStructuralFeature as, Object newValue, int position) {
		IRectangleBeanProxy rect = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) newValue);
		if (rect != null
			&& (rect.getWidth() == -1 || rect.getHeight() == -1 || (rect.getX() == Integer.MIN_VALUE && rect.getY() == Integer.MIN_VALUE))) {
			IBeanProxy containerBeanProxy = BeanAwtUtilities.invoke_getParent(getBeanProxy());
			if (containerBeanProxy == null)
				return; // Not yet within a container, can't do this function yet, and we shouldn't apply the values since they mean nothing.
			// If the either width or height is -1, or x/y are Integer.MIN_VALUE, then we need to adjust to preferred size/location.
			// This will do an actual set of the setting in the EMF object. This is OK because
			// -1 and Integer.MIN_VALUE is not a valid setting anyway, and doing an undo followed by redo will come back
			// with the same value, and then at this point in time it would reconstruct the setting.

			Rectangle bounds = new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			if (NullLayoutEditPolicy.adjustForPreferredSizeAndPosition(getBeanProxy(), bounds, 5, 5)) {
				String initString = RectangleJavaClassCellEditor.getJavaInitializationString(bounds,JFCConstants.RECTANGLE_CLASS_NAME);
				final IJavaInstance rectBean = BeanUtilities.createJavaObject(JFCConstants.RECTANGLE_CLASS_NAME, ((EObject) target).eResource().getResourceSet(), initString);//$NON-NLS-1$
				Display.getDefault().asyncExec(new Runnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						// We may not be within the context of a change control, so we need to get a controller to handle the change.
						getModelChangeController().doModelChanges(new Runnable() {
							public void run() {
								// Set the constraints on the component bean.  This will change the size of the component
								// Because we will be called back with notify and apply the constraints rectangle to the live bean
								//
								// Note: Need to use RuledCommandBuilder.
								RuledCommandBuilder cbld = new RuledCommandBuilder(getBeanProxyDomain().getEditDomain());
								cbld.applyAttributeSetting((EObject) target, as, rectBean);
								cbld.getCommand().execute();
							}
						}, true);
					}
				});
				return; // Let the notify back from the set here do the actual apply. 
			}

		}

		if (fJLocationToUse == null)
			super.applied(as, newValue, position);
		else {
			// Don't want location to be changed, so we will just do a set size instead
			String initString = PointJavaClassCellEditor.getJavaInitializationString(rect.getX(), rect.getY(),JFCConstants.POINT_CLASS_NAME);
			IPointBeanProxy pointProxy = (IPointBeanProxy) BeanProxyUtilities.getBeanProxy(fJLocationToUse);
			initString =
				RectangleJavaClassCellEditor.getJavaInitializationString(pointProxy.getX(), pointProxy.getY(), rect.getWidth(), rect.getHeight(),JFCConstants.RECTANGLE_CLASS_NAME);
			super.applied(
				sfComponentBounds,
				BeanUtilities.createJavaObject(JFCConstants.RECTANGLE_CLASS_NAME, ((EObject) target).eResource().getResourceSet(), initString),//$NON-NLS-1$
				position);
			
		}
	}
	
	protected Point getDefaultLocation(){
		if(fPLocationToUse == null){
		    return fComponentManager.getLocation();		    
		} else {
			return new Point(fInitialLocation.getX(),fInitialLocation.getY());		    
		}
	}

	/**
	 * applyVisibility - Set the apply visibility flag.
	 * If the flag is true, then the actual visibility setting will be used untouched and applied immediately.
	 *
	 * If the flag is false, then the current value will queried and saved and then it will be set to the setToVisibility value (if not null).
	 *
	 * The purpose of this is so that when the attributes visible and location are set from the property sheet
	 * they don't actually move the live bean.  This is used for things like Frame, Dialog, etc...
	 * where we want to make them always visible ( so we can get an image of them ) and make them
	 * always off screen so the user doesn't have to see them ( unless they explicitly ask to do so)
	 */
	public void applyVisibility(boolean apply, Boolean setToVisibility) {
		if (apply || setToVisibility == null) {
			if (getEObject().eIsSet(sfComponentVisible)) {
				super.applied(sfComponentVisible, (getEObject()).eGet(sfComponentVisible), 0);
			}
		} else {
			// We are about to apply a non-default visibility so capture the current one
			if(fDefaultVisibility == null){
				recordDefaultVisibility();
			}
			fVisibilityToUse =
				BeanProxyUtilities.wrapperBeanProxy(
					getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(setToVisibility),
					getEObject().eResource().getResourceSet(),
					null,
					true);

			// If we have a proxy then set the visible to the specified setting.
			super.applied(sfComponentVisible, fVisibilityToUse, -1); // Now apply the setTo value, use super to avoid checks on flag			
		}
	}

	/**
	 * applyLocation - Set the apply location flag.
	 * If the flag is true, then the actual location setting will be used untouched and applied immediately.
	 *
	 * If the flag is false, then the current value will queried and saved and then it will be set to the setToLocation value (if not null).
	 *
	 * The purpose of this is so that when the attribute location is set from the property sheet
	 * it doesn't actually move the live bean.  This is used for things like Frame, Dialog, etc...
	 * where we want to make them always visible ( so we can get an image of them ) and make them
	 * always off screen so the user doesn't have to see them ( unless they explicitly ask to do so)
	 */
	public void applyLocation(boolean apply, Point setToLocation) {
		if (apply || setToLocation == null) {
			if (((EObject) target).eIsSet(sfComponentLocation)) {
				super.applied(sfComponentLocation, ((EObject) target).eGet(sfComponentLocation), 0);
			}
		} else {
		    // Get the existing location and store it so this can be shown on the property sheet
		    fInitialLocation = (IPointBeanProxy)super.getBeanPropertyProxyValue(sfComponentLocation);	    
			// Create a new point for the location we wish to set the component to
			// and apply this as an attribute settings
			fJLocationToUse = BeanUtilities.createJavaObject(
				"java.awt.Point", //$NON-NLS-1$
				((EObject) target).eResource().getResourceSet(), 
				PointJavaClassCellEditor.getJavaInitializationString(setToLocation.x, setToLocation.y,JFCConstants.POINT_CLASS_NAME));
			// If we have a proxy then set the location to the specified setting.
			fPLocationToUse = setToLocation;
			super.applied(sfComponentLocation, fJLocationToUse, 0); // Now apply the setTo value, use super to avoid checks on flag
		}
	}

	/**
	 * canceled: A setting has been canceled to the mof object,
	 * We are testing for visibility and location. We are testing to
	 * see if we are allowing them to go through.
	 */
	protected void canceled(EStructuralFeature as, Object oldValue, int position) {
		if (!((as == sfComponentVisible && fVisibilityToUse != null) || (as == sfComponentLocation && fJLocationToUse != null)))
			super.canceled(as, oldValue, position); // We letting the settings go through
		if (as == sfComponentBounds && fJLocationToUse != null) {
			// We are canceling bounds and we have a location to use, so
			// we need to restore default location to the original default since
			// bounds had overridden it.
			IBeanProxy loc = (IBeanProxy) getOriginalSettingsTable().get(sfComponentLocation);
		}
	}
	/**
	 * Create the component listener proxy,
	 * add in all of the listeners and then add it to the beanProxy.
	 * Creation date: (3/15/00 11:13:03 AM)
	 */
	protected void createComponentManager() {
		// Create an instance of org.eclipse.ve.internal.jfc.vm.ComponentManager on the target VM
		if (fComponentManager == null) {
			fComponentManager = new ComponentManager();
			// Having created the ComponentManager in the IDE transfer all existing people listening to us
			// to the component listener
			Iterator listeners = fComponentListeners.iterator();
			while (listeners.hasNext()) {
				fComponentManager.addComponentListener((IVisualComponentListener) listeners.next());
			}
		}
		
		if (fParentComponent != null) {
			// Set both components togheter to reduce VM traffic latency
			fComponentManager.setComponentAndParent(getVisualComponentBeanProxy(),fParentComponent.getVisualComponentBeanProxy());
		} else {
			fComponentManager.setComponentBeanProxy(getVisualComponentBeanProxy());			
		}
	}
	/**
	 * Create the image collector.,
	 */
	protected void createImageCollector() {
		synchronized (imageAccessorSemaphore) {
			if (fImageDataCollector == null) {
				ProxyFactoryRegistry registry = getVisualComponentBeanProxy().getProxyFactoryRegistry();
				fImageDataCollector = new ImageDataCollector(registry);
			}
		}
	}

	/**
	 * releaseBeanProxy: Get rid of the bean proxy being held.
	 * This allows for recreation if needed.
	 * We need to dispose of the Image notifier.
	 */
	public void releaseBeanProxy() {
		if (fImageDataCollector != null) {
			// Be on the safe so no spurious last minute notifications are sent out.
			fImageDataCollector.release();
			fImageDataCollector = null;
			clearError(IMAGE_DATA_COLLECTION_ERROR_KEY);	// In case one is hanging around.
		}
		if (fComponentManager != null) {
			// Be on the safe so no spurious last minute notifications are sent out.
			fComponentManager.dispose();
			fComponentManager = null;
		}

		if (fDefaultVisibility != null) {
			IBeanProxyHost d = BeanProxyUtilities.getBeanProxyHost(fDefaultVisibility);
			if (d != null)
				d.releaseBeanProxy();
			fDefaultVisibility = null;
		}

		if (fVisibilityToUse != null) {
			IBeanProxyHost d = BeanProxyUtilities.getBeanProxyHost(fVisibilityToUse);
			if (d != null)
				d.releaseBeanProxy();
			fVisibilityToUse = null;
		}

		if (fJLocationToUse != null) {
			IBeanProxyHost d = BeanProxyUtilities.getBeanProxyHost(fJLocationToUse);
			if (d != null)
				d.releaseBeanProxy();
			fJLocationToUse = null;
		}
		
		// TODO we want to release the fparentcomponent, but not everyone is setting it correctly in their proxy adapters, so until then we can't release it.
//		fParentComponent = null;
		super.releaseBeanProxy();
	}

	/* By default remove us from the dialog
	 */
	public void disposeOnFreeForm(IBeanProxy aFreeFormDialogHost) {

		if (isBeanProxyInstantiated()) {
			if (getErrorStatus() != ERROR_SEVERE) {
				IMethodProxy removeFreeFormComponentMethodProxy =
					getBeanProxyDomain().getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aFreeFormDialogHost.getTypeProxy().getTypeName(), 
					"remove",	//$NON-NLS-1$
					new String[] { "java.awt.Component" }); //$NON-NLS-1$				
				removeFreeFormComponentMethodProxy.invokeCatchThrowableExceptions(aFreeFormDialogHost, getBeanProxy());
			}
		}
	}
	/**
	 * getBeanPropertyValue method comment.
	 */
	public IJavaInstance getBeanPropertyValue(EStructuralFeature aBeanPropertyAttribute) {
		if (!isBeanProxyInstantiated())
			return null; // We don't have a proxy yet.

		// If the property is visible/location and we didn't apply the visibility/location, then return the default value.
		if (aBeanPropertyAttribute == sfComponentVisible && fVisibilityToUse != null) {
			// Visible attribute and we didn't apply it, we used an override, so get default setting
			return fDefaultVisibility;
		} else {
			if (aBeanPropertyAttribute == sfComponentLocation && fJLocationToUse != null) {
				// Location attribute and we didn't apply it, we used an override, so get default setting
				// It is possible we have some bounds explicitly set on us in which case we need to use it's x and y
				Point userApparentLocation = null;
				if (getEObject().eIsSet(sfComponentBounds)){
					IJavaInstance explicitBounds = (IJavaInstance)getEObject().eGet(sfComponentBounds);
					IRectangleBeanProxy rect = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy(explicitBounds);
					userApparentLocation = new Point(rect.getX(),rect.getY());
				} else {
					userApparentLocation = getDefaultLocation();
				}
				return BeanUtilities.createJavaObject(
					"java.awt.Point", //$NON-NLS-1$
					getEObject().eResource().getResourceSet(), 
					PointJavaClassCellEditor.getJavaInitializationString(userApparentLocation.x, userApparentLocation.y,JFCConstants.POINT_CLASS_NAME));
			} else if (aBeanPropertyAttribute == sfComponentBounds && fJLocationToUse != null) {
				// Bounds attribute and we didn't apply it, we used an override, so get the current bounds and
				// then change the location to current location. The current location is either the set value, or the default location.
				IJavaInstance currentBounds = super.getBeanPropertyValue(aBeanPropertyAttribute);
				EObject eObject = (EObject) target;
				IJavaInstance currentLoc = null;
				if (eObject.eIsSet(sfComponentLocation)) {
					currentLoc = (IJavaInstance) eObject.eGet(sfComponentLocation);
					IPointBeanProxy currentLocProxy = (IPointBeanProxy) BeanProxyUtilities.getBeanProxy(currentLoc);
					IRectangleBeanProxy currentBoundsProxy = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy(currentBounds);
					currentBoundsProxy.setLocation(currentLocProxy);
					return currentBounds;					
				} else {
					IRectangleBeanProxy currentBoundsProxy = (IRectangleBeanProxy) BeanProxyUtilities.getBeanProxy(currentBounds);
					currentBoundsProxy.setLocation(getDefaultLocation().x,getDefaultLocation().y);
					return currentBounds;
				}
			} else {
				// Normal attribute or visibility/location applied, so get the real value.
				return super.getBeanPropertyValue(aBeanPropertyAttribute);
			}
		}
	}
	/**
	 * Return a Rectangle that contains the size and location of the live bean running on the target VM
	 */
	public Rectangle getBounds() {
		if (fComponentManager != null) {
			return fComponentManager.getBounds();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Rectangle(0, 0, 0, 0);
		}
	}

	/**
	 * Return a Point that contains the location of the live bean running on the target VM
	 */
	public Point getLocation() {
		if (fComponentManager != null) {
			return fComponentManager.getLocation();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Point(0, 0);
		}
	}
	/**
	 * getParentComponent - get the component that is the parent of this component.
	 */
	public IComponentProxyHost getParentComponentProxyHost() {
		return fParentComponent;
	}
	/**
	 *Return a dimension that contains the size of the live bean running on the target VM
	 */
	public Dimension getSize() {
		if (fComponentManager != null) {
			return fComponentManager.getSize();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Dimension(20, 20);
		}
	}
	/**
	 Return the bean proxy to the java.awt.Component
	 For components it is the same as the bean proxy.  Subclasses can override if they provide the component
	 from another source.  This is the case for Druid where the component bean is not the live bean but is
	 wrappered by it
	 */
	public IBeanProxy getVisualComponentBeanProxy() {

		return getBeanProxy();

	}
	public boolean hasImageListeners() {
		return (imSupport != null && imSupport.hasImageListeners());
	}
	/**
	 * Setup the bean proxy.
	 */
	protected void setupBeanProxy(IBeanProxy beanProxy) {
		ProxyFactoryRegistry registry = beanProxy.getProxyFactoryRegistry();
		if (fOwnsProxy && beanProxy != null && !beanProxy.getTypeProxy().isKindOf(registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component"))) { //$NON-NLS-1$
			// Something special for component. Since Component is Abstract, some one may of tried create an abstract subclass. In that case
			// the bean proxy that comes in would be for Object. So what we will do is since we are not a component, we will release the
			// beanproxy that came in and change it a Canvas (that way something will display).
			// Don't need to worry about any other kind of class because all of the subclasses of Component that we are implementing
			// are non-abstract.
			IBeanProxy newProxy = null;
			try {
				newProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Canvas").newInstance(); //$NON-NLS-1$
				// For now, just so that it is visible, we will set its background to grey. We should put a border around, but the
				// philosophy we used is actually backwards. By default no border.
				try {
					IBeanProxy grayProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Color").newInstance("java.awt.Color.lightGray"); //$NON-NLS-1$ //$NON-NLS-2$
					newProxy.getTypeProxy().getMethodProxy("setBackground", new IBeanTypeProxy[] {grayProxy.getTypeProxy()}).invokeCatchThrowableExceptions(newProxy, grayProxy); //$NON-NLS-1$
					// Also give it an initial small size so it shows up. If there is a real size set later then it will go to that.
					IBeanProxy sizeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Dimension").newInstance("new java.awt.Dimension(10,10)"); //$NON-NLS-1$ //$NON-NLS-2$
					newProxy.getTypeProxy().getMethodProxy("setSize", new IBeanTypeProxy[] {sizeProxy.getTypeProxy()}).invokeCatchThrowableExceptions(newProxy, sizeProxy); //$NON-NLS-1$
				} catch (InstantiationException e) {
					// Shouldn't occur.
				}
			} catch (ThrowableProxy e) {
				// This shouldn't of happened. Should always be able to instantiate canvas.
				JavaVEPlugin.log(e, Level.FINE);
			}
			beanProxy.getProxyFactoryRegistry().releaseProxy(beanProxy);
			beanProxy = newProxy;
			notInstantiatedClasses.remove(notInstantiatedClasses.size()-1);	// The last one WILL be for Component. We want to be able to apply component settings.
		}
		super.setupBeanProxy(beanProxy);
		if (fComponentListeners != null && !fComponentListeners.isEmpty())
			createComponentManager();
		// Now add in the image listener if one is required.
		if (hasImageListeners())
			createImageCollector();
	}

	protected void applyAllSettings() {
		if (isBeanProxyInstantiated()) {
			if (fJLocationToUse != null) {
				// We have a location setting that bypasses the setting in the mof object, apply it now so that it would off screen when made visible.
				super.applied(sfComponentLocation, fJLocationToUse, -1);
			}				

			if(getEObject().eIsSet(sfComponentVisible)){
				// Query the current value and put into fDefaultVisibility so that we know what it was at the beginning.
				recordDefaultVisibility();
			}
		}
		super.applyAllSettings();
		if (isBeanProxyInstantiated() && getErrorStatus() != ERROR_SEVERE) {

			if (fVisibilityToUse != null) {
				// We have a visibility setting that bypasses the setting in the mof object, apply it now
				super.applied(sfComponentVisible, fVisibilityToUse, -1);
			}
		}		
	}
	
	private void recordDefaultVisibility(){
		fDefaultVisibility =
			BeanProxyUtilities.wrapperBeanProxy(super.getBeanPropertyProxyValue(sfComponentVisible), ((EObject) target).eResource().getResourceSet(), null, false);		
	}
	
	protected void reapplyVisibility() {
		// TODO This whole thing is a real kludge due to switching from card layout leaves
		// the non-showing cards not visible.
		if (fVisibilityToUse != null) {
			// We have a visibility setting that bypasses the setting in the mof object, apply it now
			super.applied(sfComponentVisible, fVisibilityToUse, -1);
		} else {
			Object initial = ((IJavaObjectInstance) getTarget()).eGet(sfComponentVisible);
			if (initial == null) {
				// Not set, so make it true.
				initial = BeanProxyUtilities.wrapperBeanProxy(
					getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(true),
					((EObject) target).eResource().getResourceSet(),
					null,
					true);				
			}
			super.applied(sfComponentVisible, initial, -1);
		}
	}

	/* By default new us up constructor and add us to the dialog
	 */
	public IBeanProxy instantiateOnFreeForm(IBeanProxy aFreeFormDialogHost) {
		
		if (!isBeanProxyInstantiated() && getErrorStatus() != ERROR_SEVERE)
			instantiateBeanProxy();	// If not already instantiated, and not errors, try again. If already instantiated w/severe, don't waste time
		if (isBeanProxyInstantiated() && getErrorStatus() != ERROR_SEVERE) {
			IMethodProxy addFreeFormComponentMethodProxy =
				getBeanProxyDomain().getProxyFactoryRegistry().getMethodProxyFactory().getMethodProxy(aFreeFormDialogHost.getTypeProxy().getTypeName(), "add",//$NON-NLS-1$
				new String[] { "java.awt.Component" } //$NON-NLS-1$
				);			
			addFreeFormComponentMethodProxy.invokeCatchThrowableExceptions(aFreeFormDialogHost, instantiateBeanProxy());
			applyVisibility(false, Boolean.TRUE); // Make sure it is visible.
			revalidateBeanProxy(); // Make sure that we have a new image and size because of now being on the freeform. 
			// This is because this goes outside of the normal settings apply, which would
			// cause the revalidate then, normally.
		}
		return getBeanProxy();

	}
	/**
	 * Invalidate the image so that the next refreshImage request will
	 * kick off a new capture. If an image is currently being collected,
	 * do an abort so that it will terminate.
	 */
	public void invalidateImage() {
		synchronized (imageAccessorSemaphore) {
			if (fImageDataCollector != null) {
				if (fImageValid == INVALID_COLLECTING && fImageDataCollector.isCollectingData())
					fImageDataCollector.abort(); // We're currently do a valid collection, so abort it.
				fImageValid = INVALID; // Mark it as invalid.	
			}
		}
	}
	
	private static final Object IMAGE_DATA_COLLECTION_ERROR_KEY = new Object();
	/**
	 * Refresh the image if it is currently invalid.
	 */
	public void refreshImage() {
		if (!isBeanProxyInstantiated())
			return;
		// Need to capture validity before going in and start because there would be a deadlock while
		// waiting for completion because it would tie up "this" while DataCollectedRunnable would also
		// try to synchronize on "this" and it couldn't because we had and are waiting in waitForCompletion.
		boolean doRefresh = false;
		synchronized (imageAccessorSemaphore) {
			doRefresh = fImageValid == INVALID && fImageDataCollector != null && hasImageListeners();
			if (doRefresh)
				fImageValid = INVALID_COLLECTING;
		}
			if (doRefresh) {
				try {
					fImageDataCollector.waitForCompletion();
					// Wait if running so that we don't start it while running. Must be outside of sync block because completion requires syncing on a separate thread. Would have a deadlock then.
					// Sync back so that no one else can come in until the collection has been started
					synchronized (imageAccessorSemaphore) {
						clearError(IMAGE_DATA_COLLECTION_ERROR_KEY);
						fImageDataCollector.startComponent(getVisualComponentBeanProxy(), new ImageDataCollector.DataCollectedRunnable() {
							private int startedStatus = ImageDataConstants.IMAGE_NOT_STARTED;							
							public void imageStarted(int startStatus) {
								startedStatus = startStatus;
							}

							public void imageData(ImageData data) {
								synchronized (imageAccessorSemaphore) {
									fImageValid = VALID;
								}

								imSupport.fireImageChanged(data);
								if (startedStatus == ImageDataConstants.COMPONENT_IMAGE_CLIPPED) {
									Display.getDefault().asyncExec(new Runnable() {
										public void run() {
											// Bit of kludge, but if image clipped, create an info message for it.
											// This needs to run in ui thread.
											ErrorType err =
												new ErrorType(
													VisualMessages.getString("ComponentProxyAdapter.Picture_too_large_WARN_"), //$NON-NLS-1$
													IErrorHolder.ERROR_INFO);
											processError(IMAGE_DATA_COLLECTION_ERROR_KEY, err);
										}
									}); 
								}								
							}

							public void imageNotCollected(int status) {
								synchronized (imageAccessorSemaphore) {
									fImageValid = INVALID; // Invalid, but no longer collecting.
								}
							}
							
							public void imageException(final ThrowableProxy exception) {
								Display.getDefault().asyncExec(new Runnable() {
									public void run() {
										String eMsg = exception.getProxyLocalizedMessage();
										if (eMsg == null) {
											// No localized msg. Get the exception type.
											IBeanTypeProxy eType = exception.getTypeProxy();
											eMsg = MessageFormat.format(VisualMessages.getString("ComponentProxyAdapter.Image_collection_exception_EXC_"), new Object[] { eType.getTypeName()}); //$NON-NLS-1$
										}
										ErrorType err = new ErrorType(MessageFormat.format(VisualMessages.getString("ComponentProxyAdapter.Image_collection_failed_ERROR_"), new Object[] { eMsg }), IErrorHolder.ERROR_INFO); //$NON-NLS-1$
										processError(IMAGE_DATA_COLLECTION_ERROR_KEY, err);
									}
								});
							}
						});
					}
				} catch (ThrowableProxy e) {
					JavaVEPlugin.log(e, Level.WARNING);
					String eMsg = e.getProxyLocalizedMessage();
					if (eMsg == null) {
						// No localized msg. Get the exception type. If it is OutOfMemory, handle special.
						IBeanTypeProxy eType = e.getTypeProxy();
						if ("java.lang.OutOfMemoryError".equals(eType.getTypeName())) { //$NON-NLS-1$
							eMsg = VisualMessages.getString("ComponentProxyAdapter.Out_of_memory_WARN_"); //$NON-NLS-1$
						} else {
							eMsg = MessageFormat.format(VisualMessages.getString("ComponentProxyAdapter.Image_collection_exception_EXC_"), new Object[] {eType.getTypeName()}); //$NON-NLS-1$
						}
					}
					ErrorType err = new ErrorType(MessageFormat.format(VisualMessages.getString("ComponentProxyAdapter.Image_collection_failed_ERROR_"), new Object[] {eMsg}), IErrorHolder.ERROR_INFO); //$NON-NLS-1$
					processError(IMAGE_DATA_COLLECTION_ERROR_KEY, err);
					synchronized (imageAccessorSemaphore) {
						fImageValid = INVALID;
					}
				}
			}

	}
	/**
	 * removeComponentListener method comment.
	 */
	public synchronized void removeComponentListener(IVisualComponentListener aListener) {
		// Remove from the local list and the proxy list.
		fComponentListeners.remove(aListener);
		if (fComponentManager != null) {
			fComponentManager.removeComponentListener(aListener);
		}
	}

	/**
	 * removeImageListener method comment.
	 */
	public synchronized void removeImageListener(IImageListener aListener) {
		if (imSupport != null)
			imSupport.removeImageListener(aListener);
	}
	
	
	/**
	 * child invalidated. Default is to just pass on up to parent.
	 * Components typically don't have children, but just in case.
	 */
	public void childInvalidated(IComponentProxyHost childProxy) {
		IComponentProxyHost parent = getParentComponentProxyHost();
		if (parent != null)
			parent.childInvalidated(childProxy);
	}
	
	/**
	 * child validated. Default is to just pass on up to parent.
	 * Components typically don't have children, but just in case.
	 */
	public void childValidated(IComponentProxyHost childProxy) {
		IComponentProxyHost parent = getParentComponentProxyHost();
		if (parent != null)
			parent.childValidated(childProxy);
	}	
	
	/**
	 * invalidate
	 */
	public void invalidateBeanProxy() {
		if (getVisualComponentBeanProxy() != null) {
		    getModelChangeController().execAtEndOfTransaction(new Runnable(){
		        public void run(){				    
					if (isBeanProxyInstantiated()) {
						// Invalidate the component bean
						IBeanProxy componentBean = getVisualComponentBeanProxy();
						BeanAwtUtilities.invoke_invalidate(componentBean);
						// Go up the chain and invalidate all of the parents that have image listeners.
						IComponentProxyHost parentBean = ComponentProxyAdapter.this;
						while (parentBean != null) {
							if (parentBean.hasImageListeners())
								parentBean.invalidateImage();
							parentBean = parentBean.getParentComponentProxyHost();
						}

						// Now let the parent know
						childInvalidated(ComponentProxyAdapter.this); 
					}		            
		        }
		    },
		    ModelChangeController.createHashKey(this,"INVALIDATE"), //$NON-NLS-1$
		    new Object[] {ModelChangeController.SETUP_PHASE,ModelChangeController.INIT_VIEWERS_PHASE});
		}
	}
	
	/**
	 * now validate - Queue up a refresh image request.
	 */
	public void validateBeanProxy() {
		if (getVisualComponentBeanProxy() != null) {
			// Queue up the refresh so that if several changes are going on in this thread
			// that all of the refresh requests are queued up and only the first one
			// will actually run. The rest will see a valid refresh is in progress and
			// not do anything.
		    getModelChangeController().execAtEndOfTransaction(new Runnable() {
				public void run() {
					if (isBeanProxyInstantiated()) {
						// Still live at when invoked later.
						// Go up the chain and find all image listeners.
						List allImageListeners = new ArrayList(5);
						IComponentProxyHost nextParentBean = getParentComponentProxyHost();
						IComponentProxyHost parentBean = ComponentProxyAdapter.this;
						if (parentBean.hasImageListeners())
							allImageListeners.add(parentBean);
						while (nextParentBean != null) {
							parentBean = nextParentBean;
							if (parentBean.hasImageListeners())
								allImageListeners.add(parentBean);
							nextParentBean = parentBean.getParentComponentProxyHost();
						}

						// Now refresh all of the components that notify.
						Iterator listeners = allImageListeners.iterator();
						while (listeners.hasNext()) {
							((IComponentProxyHost) listeners.next()).refreshImage();
						}
					}
				}
			});
			childValidated(this);
		}
	}
	
	/**
	 * setParentComponent: Set who the parent component is.
	 * NOTE: This is only to be called by the apply... method
	 * in the association. It is so that we can find the
	 * parent component for invalidating the image.
	 */
	public void setParentComponentProxyHost(IComponentProxyHost parent) {
		fParentComponent = parent;
		if (fComponentManager != null)
			fComponentManager.setRelativeParentComponentBeanProxy(fParentComponent != null ? fParentComponent.getVisualComponentBeanProxy() : null);
	}
	/**
	 * When a component must be reintantiated this can only be done by its parent
	 */
	protected void primReinstantiateBeanProxy() {
		if (getParentComponentProxyHost() != null) {
			getParentComponentProxyHost().reinstantiateChild(this);
			return;
		}

		super.primReinstantiateBeanProxy();
	}

	/**
	 * Here only so that other instances of proxy adapters in this package can cross-access
	 * this method. Otherwise it would be hidden to them.
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#clearError(EStructuralFeature, Object)
	 */
	protected void clearError(EStructuralFeature sf, Object object) {
		super.clearError(sf, object);
	}

	/**
 	 * Here only so that other instances of proxy adapters in this package can cross-access
	 * this method. Otherwise it would be hidden to them.
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#processError(EStructuralFeature, Exception, Object)
	 */
	protected void processError(EStructuralFeature sf, Throwable exc, Object object) throws ReinstantiationNeeded {
		super.processError(sf, exc, object);
	}

	public boolean isTrulyValidFeature(EStructuralFeature sf, Object object) {
		return super.isValidFeature(sf, object);		
	}

}
