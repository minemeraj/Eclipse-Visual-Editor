/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.core;
/*
 *  $RCSfile: BeanProxyAdapter.java,v $
 *  $Revision: 1.24 $  $Date: 2004-10-27 20:42:03 $ 
 */

import java.util.*;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.util.ListenerList;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.java.*;

import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;

import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

/**
 * Adapter to wrap a MOF Bean and its bean proxy.
 */
public class BeanProxyAdapter extends AdapterImpl implements IBeanProxyHost {
	
	private IBeanProxy fBeanProxy;	// It should be accessed only through accessors, even subclasses.
	private boolean inInstantiation = false;	// Are we in instantiation. If so, reinstantiate has special processing.
	
	// If a reinstantiation is needed, then this exception will be thrown and caught.
	// If in instantiation, it will then try again. This is needed because if reinstantiation is required while
	// instantiating in the first place, the normal reinstantiation process will cause problems. This
	// is because we normally ask the parent to reinstantiate, but during instantiation, the parent
	// doesn't know about us and it may try to remove us when we aren't even there yet.
	// The other time it is needed is if applying a multivalue, it must terminate any more
	// multi-applies and try again.
	protected class ReinstantiationNeeded extends RuntimeException {
		public ReinstantiationNeeded() {
		}
	};
		
	protected boolean fOwnsProxy = false;

	// Need a table of original setting proxies so that if a setting is canceled it can be restored to the
	// original proxy. It will be created the first time we set anything.
	// NOTE: We use its existance as a flag also. Upon releaseProxy, if the table doesn't exist, then
	// we never set anything. This means we don't need go through releasing settings. This is a savings
	// because it could cause the first introspection of a class when are trying to release the class.
	// An introspection at that time is not necessary since we never accessed any of its settings.
	protected HashMap fOrigSettingProxies;
	
	// Whether this proxy is wrappering the "this" part for a BeanSubclassComposition. If it is, then
	// the bean that gets instantiated is the superclass (since we are in the process of developing
	// this bean, we can't actually instantiate it). Also, when querying for property values, we
	// won't submit properties that are defined as a local property (i.e. one that is defined at
	// this class level) since we don't have this class level instantiated.
	// null means not yet initialized. It is figured out by seeing if its container sf is thisPart.
	protected Boolean fIsThis;

	// Non-instantiated classes. Used to test if a feature can be set or not. This is used in conjunction with isThis.
	// It is the list of JavaClasses, starting with this class up to but not including the first non-abstract class in
	// the hierarchy. This is because we can't instantiate abstract classes.
	protected List notInstantiatedClasses = null;	
	
	// The domain must be given to us so we can create instances
	private IBeanProxyDomain domain;
	
	// Hold any exception that occurs when we try to create the BeanProxy
	protected Throwable fInstantiationError;
	
/**
 * BeanProxyAdaptor constructor comment.
 */
public BeanProxyAdapter(IBeanProxyDomain domain) {
	super();
	this.domain = domain;
}

/*
 * Is this an instantiation feature.
 */
protected boolean isInstantiationFeature(EStructuralFeature sf) {
	return JavaInstantiation.ALLOCATION.equals(sf.getName());
}
/**
 * applied: A setting has been applied to the mof object,
 * now apply it to the bean.
 */
 
public void notifyChanged(Notification msg){
	switch ( msg.getEventType()) {
		case Notification.ADD :			
		case Notification.SET : 
			if (!CDEUtilities.isUnset(msg)) {
				try {
					applied((EStructuralFeature)msg.getFeature(), msg.getNewValue(), msg.getPosition());
				} catch (ReinstantiationNeeded e) {
					// It was thrown and not handled, we need to reinstantiate ourselves.
					reinstantiateBeanProxy();
				}				
				break;			
			}	// else flow into unset.
		case Notification.UNSET : 
		case Notification.REMOVE :
			try {
				canceled((EStructuralFeature)msg.getFeature(), msg.getOldValue(), msg.getPosition());
			} catch (ReinstantiationNeeded e) {
				// It was thrown and not handled, we need to reinstantiate ourselves.
				reinstantiateBeanProxy();
			}							
			break;
		case Notification.ADD_MANY:
			try {
				appliedList((EStructuralFeature)msg.getFeature(), (List) msg.getNewValue(), msg.getPosition(), false);
			} catch (ReinstantiationNeeded e) {
				// It was thrown and not handled, we need to reinstantiate ourselves.
				reinstantiateBeanProxy();
			}
			break;
		case Notification.REMOVE_MANY:
			try {
				canceledList((EStructuralFeature)msg.getFeature(), (List) msg.getOldValue(), msg.getPosition());
			} catch (ReinstantiationNeeded e) {
				// It was thrown and not handled, we need to reinstantiate ourselves.
				reinstantiateBeanProxy();
			}
			break;
		case Notification.REMOVING_ADAPTER:
			removingAdapter();	// Let subclasses hook into the remove.
			releaseBeanProxy();	// This adapter is being removed, so get rid of the proxy.
			break;
		case Notification.MOVE:
			moved((EStructuralFeature)msg.getFeature(), msg.getNewValue(), ((Integer) msg.getOldValue()).intValue(), msg.getPosition());
			break;
	}
}

/**
 * Default implementation of move of feature has occurred. Subclasses should override if necessary.
 * @param feature
 * @param value
 * @param oldPosition
 * @param newPosition
 * 
 * @since 1.0.2
 */
protected void moved(EStructuralFeature feature, Object value, int oldPosition, int newPosition) {
	// The default is to do a remove followed by add.
	canceled(feature, value, oldPosition);
	applied(feature, value, newPosition);
}

/*
 * Adapter being removed, subclasses may override to add function.
 */
protected void removingAdapter() {
}

/*
 * Are we in the process of instantiating the bean.
 */
protected final boolean inInstantiation() {
	return inInstantiation;
}

/*
 * Default is to just call applied.
 */
protected void appliedList(EStructuralFeature sf, List newValues, int position, boolean testValidity){
	Iterator iter = newValues.iterator();	
	while (iter.hasNext()){
		Object v = iter.next();
		if (!testValidity || isValidFeature(sf, v))
			applied(sf, v, position);
		if (position != -1)
			position++;
	}
}

/*
 * Get the registered Adapter, but different in that if eo not in a resource, then
 * use registered factories from the resource that this BeanProxyAdapter is in. 
 * 
 * @param eo Object to adapt
 * @param adapterType Type to adapt too
 * @return The adapter, or <code>null</code> if not found.
 * 
 * @since 1.0.0
 */
protected Adapter getRegisteredAdapter(EObject eo, Object adapterType) {
	Adapter a = EcoreUtil.getExistingAdapter(eo, adapterType);
	if (a == null) {
		Resource res = eo.eResource();
		ResourceSet rset = res != null ? res.getResourceSet() : null;
		if (rset == null) {
			res = getEObject().eResource();
			rset = res != null ? res.getResourceSet() : null;	// Try the guy we are in, if one.
			if (rset == null)
				rset = JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain());
		}
		AdapterFactory f = EcoreUtil.getAdapterFactory(rset.getAdapterFactories(), adapterType);
		if (f != null)
			a = f.adaptNew(eo, adapterType);
	}
	return a;
}

protected void applied(EStructuralFeature sf , Object newValue , int position){
	if (isBeanProxyInstantiated()) {
		if (!inInstantiation() && isInstantiationFeature(sf)) {
			reinstantiateBeanProxy();
			return;
		}
		// We have something to set into, so we are live
		// Get the decorator that contains the introspected BeanInfo properties
		PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator( (EModelElement) sf);
		// Only apply if this if it is not the this part, or if it is the
		// this part, then only if it is a non-local attribute.
		if (!isThisPart() || !isAttributeLocal(sf)) {
			BeanFeatureDecorator featureDecor = null; 
			if (propertyDecorator == null || propertyDecorator.getWriteMethod() == null) {
				// Note: fields are handled in kludge way, need a better way of doing this. This is being put off until next release.
				// This could be a field instead, see if it has a feature decor. 
				// When we rewrite this, need to take into consideration that the field may be final, in which case it
				// would be read only.
				featureDecor = (BeanFeatureDecorator)Utilities.getDecorator((EModelElement)sf,BeanFeatureDecorator.class);
			}
			
			if ((propertyDecorator != null && propertyDecorator.getWriteMethod() != null) || featureDecor != null) {
				IJavaInstance javaValue = (IJavaInstance)newValue;
				IBeanProxyHost settingBean = null;
				if (javaValue != null) {
					settingBean = (IBeanProxyHost) getRegisteredAdapter(javaValue, IBeanProxyHost.BEAN_PROXY_TYPE);
				}
				// Let the attribute apply the setting. We have to make sure the setting
				// is instantiated since we are applying to an instantiated object			
				synchronized (this) {
					// Synchronize with the instantiate on this bean so that other threads can't actually
					// apply anything until after the instantiation is finished. Otherwise we could
					// get race conditions of which apply get's done first.
					if (fOrigSettingProxies == null || !getOriginalSettingsTable().containsKey(sf)) {
						// This is the first time for this setting, so we save the current value for canceling.
						IBeanProxy origValue = getBeanProxyValue(sf, propertyDecorator, featureDecor);
						getOriginalSettingsTable().put(sf, origValue);
					}
					if (settingBean != null) {
						settingBean.instantiateBeanProxy();
					}					
					// Apply the value by firing a set method
					if (settingBean != null && settingBean.getErrorStatus() == ERROR_SEVERE)
						processError(sf, ((ExceptionError) settingBean.getErrors().get(0)).error);
					else
						applyBeanFeature(sf, propertyDecorator, featureDecor, settingBean != null ? settingBean.getBeanProxy() : null);
				}
			}
		} 
	}
}

protected IBeanProxyFeatureMediator getFeatureMediator(BeanFeatureDecorator featureDecor) {
	String beanProxyFeatureMediatorName = featureDecor.getBeanProxyMediatorName();	
	if ( beanProxyFeatureMediatorName != null ) {
		try {
			Class mediatorClass = CDEPlugin.getClassFromString(beanProxyFeatureMediatorName);
			return (IBeanProxyFeatureMediator)mediatorClass.newInstance();
		} catch ( Exception exc ) {
			JavaVEPlugin.log(exc, Level.WARNING);
		}
	}
	return null;		
}

/**
 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#applyBeanPropertyProxyValue(EStructuralFeature, IBeanProxy)
 */
public void applyBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IBeanProxy aproxy) {
	PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator( (EModelElement) aBeanPropertyFeature);
	// Only apply if this if it is not the this part, or if it is the
	// this part, then only if it is a non-local attribute.
	if (!isThisPart() || !isAttributeLocal(aBeanPropertyFeature)) {
		BeanFeatureDecorator featureDecor = null; 
		if (propertyDecorator == null || propertyDecorator.getWriteMethod() == null) {
			// Note: fields are handled in kludge way, need a better way of doing this. This is being put off until next release.
			// This could be a field instead, see if it has a feature decor. 
			// When we rewrite this, need to take into consideration that the field may be final, in which case it
			// would be read only.
			featureDecor = (BeanFeatureDecorator)Utilities.getDecorator((EModelElement)aBeanPropertyFeature, BeanFeatureDecorator.class);
		}
		
		applyBeanFeature(aBeanPropertyFeature, propertyDecorator, featureDecor, aproxy);
	}
}

protected final void applyBeanFeature(EStructuralFeature sf , PropertyDecorator propDecor , BeanFeatureDecorator featureDecor, IBeanProxy settingBeanProxy) {
	try {

		if (getBeanProxy().isValid()) {
			primApplyBeanFeature(sf, propDecor, featureDecor, settingBeanProxy);
			clearError(sf);		
			revalidateBeanProxy();			
		}
	} catch (ReinstantiationNeeded e) {
		throw e;	
	} catch ( RuntimeException exc ) {
		// It is possible that the set method threw an exception
		// In this case record the error against the feature and recycle the bean
		processError(sf,exc);
	} catch (ThrowableProxy e) {
		// It is possible that the set method threw an exception
		// In this case record the error against the feature and recycle the bean
		processError(sf,e);
	}
}
		

/*
 * This can be overridden. Overrides shouldn't catch exceptions except if they can clean them up to mean a good apply was done.
 * Otherwise, applyBeanFeature will catch exceptions and mark the errors correctly.
 */
protected void primApplyBeanFeature(EStructuralFeature sf , PropertyDecorator propDecor , BeanFeatureDecorator featureDecor, IBeanProxy settingBeanProxy) throws ThrowableProxy {

	if (propDecor != null) {
		if (propDecor.needIntrospection())
			throw new ReinstantiationNeeded();	// We need to reinstantiate. That will then cause re-introspection to occur.
		 if (propDecor.getWriteMethod() != null) {
			BeanProxyUtilities.writeBeanFeature(propDecor , getBeanProxy() , settingBeanProxy);	
			return;
		 }
	}
	if (featureDecor != null) {
		IBeanProxyFeatureMediator mediator = getFeatureMediator(featureDecor);
		if (mediator != null)
			mediator.applied(sf, getBeanProxy(), settingBeanProxy);
	}
}

protected synchronized final void reinstantiateBeanProxy() {
	// IF we are in instantiation, so we can't do let the parent do the reinstantiation.
	// Only we know how we are trying to instantiate, and the catcher of this exception can handle that.
	if (inInstantiation)
		throw new ReinstantiationNeeded();
	primReinstantiateBeanProxy();
}
	
/*
 * Actual reinstantiate process. The main method (reinstantiateBeanProxy)
 * tests to make sure we are in a non instantiation state. Only then can
 * parents reliable recreate us. If it got here we know the parent has a reference
 * to recreate.
 */
protected void primReinstantiateBeanProxy() {
	EObject parent = ((EObject)getTarget()).eContainer();	
	if (parent != null){
		if ( parent instanceof IJavaInstance) {
			IBeanProxyHost parentProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance)parent);
			parentProxyHost.reinstantiateChild(this);
			return;
		}
	}
	
	// Re-create us.  This occurs when are on the free form surface or no parent.
	// This whole method is overridden in ComponentProxyAdapter but for regular beans
	// re-creation is just fine
	releaseBeanProxy();		
	instantiateBeanProxy();
}
protected Map keyedErrors = new HashMap(1);
/**
 * Errors can occur when a feature is applied.  In this case we need to deal with it by 
 * flagging the feature as being one that can't be applied to the live JavaBean
 * In addition we need to throw away and re-create the live JavaBean
 * This MUST be specialized in any kind of JavaBean that can be a child of someone
 */
protected void processError(EStructuralFeature sf, Throwable exc){

	// We need a better way of getting the error level.
	ErrorType error = new PropertyError(exc, exc instanceof InstantiationException ? ERROR_INFO : ERROR_WARNING , sf);
	// By default we release and re-create the targetVM JavaBean
	processError(sf, error);
	reinstantiateBeanProxy();
	
}

/**
 * This is for adding a keyed error type to the list of errors.
 * 
 * @param key The key to use
 * @param error The error to add to the keyed errors
 */
protected void processError(Object key, ErrorType error) {
	keyedErrors.put(key, error);
	fireErrorStatusChanged();	
}

/**
 * Errors can occur when a feature is applied.  In this case we need to deal with it by 
 * flagging the feature as being one for this object that can't be applied to the live JavaBean
 * In addition we need to throw away and re-create the live JavaBean
 * This MUST be specialized in any kind of JavaBean that can be a child of someone
 * 
 * This is for multi-valued settings. It will throw a Reinstation
 */
protected void processError(EStructuralFeature sf, Throwable exc, Object object) throws ReinstantiationNeeded {
	Object errors = keyedErrors.get(sf);
	if (errors == null || !(errors instanceof List)) {
		// Either this is the first for the feature or it somehow switched from single to multi.
		errors = new ArrayList(1);
		keyedErrors.put(sf, errors);
	}

	//TODO We need a better way of deciding the error severity than looking up the type - JRW
	ErrorType error = new MultiPropertyError(object, exc, exc instanceof InstantiationException ? ERROR_INFO : ERROR_WARNING , sf);
	((List) errors).add(error);
	
	fireErrorStatusChanged();

	// Log it so that we can figure out later why.
	JavaVEPlugin.log(exc, Level.WARNING);		
	
	// Now need to throw reinstation needed so that multi-values do not continue trying to apply follow on values. There
	// could be a problem of duplicate adds in that case.
	throw new ReinstantiationNeeded();
}
/**
 * If a JavaBean fails to be created record the error and notify any interested listeners
 */
protected void processInstantiationError(Throwable exc){
	fInstantiationError = exc;
	fireErrorStatusChanged();
}

protected void clearError(Object key){
	Object v = keyedErrors.remove(key);
	if(v != null && keyedErrors.isEmpty() && fInstantiationError == null){
		// We had an error of this key, and it is now no errors and no instantiation error, so let all know it went clear.
		fireErrorStatusChanged();
	}
	
	if (v instanceof ErrorType) 
		fireClearedError((ErrorType) v);
	else if (v instanceof List) {
		List errors = (List) v;
		for (int i = 0; i < errors.size(); i++) {
			fireClearedError((ErrorType) errors.get(i));
		}
	}
}

protected void clearError(EStructuralFeature sf, Object object) {
	Object errors = keyedErrors.get(sf);
	if (errors != null)
		if (errors instanceof List) {
			Iterator itr = ((List) errors).iterator();
			while (itr.hasNext()) {
				MultiPropertyError merr = (MultiPropertyError) itr.next();
				if (object == merr.getErrorObject()) {
					itr.remove();
					fireClearedError(merr);
					break;
				}
			}
			
			if (((List) errors).isEmpty())
				clearError(sf);	// There are no more, so clear completely
		} else
			clearError(sf);	// It somehow switched types, so clear it completely.
}
		
protected boolean isValidFeature(EStructuralFeature aSF){
	return !keyedErrors.containsKey(aSF);
}

protected boolean isValidFeature(EStructuralFeature sf, Object object) {
	Object errors = keyedErrors.get(sf);
	if (errors != null)
		if (errors instanceof List) {
			Iterator itr = ((List) errors).iterator();
			while (itr.hasNext()) {
				MultiPropertyError e = (MultiPropertyError) itr.next();
				if (object == e.getErrorObject())
					return false;
			}
		} else
			clearError(sf);	// It somehow switched types, so clear it completely.
	return true;
}

protected void fireErrorStatusChanged(){
	if ( fErrorListeners != null ) {
		Object[] listeners = fErrorListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			((IBeanProxyHost.ErrorListener)listeners[i]).errorStatusChanged();
		}
	}
}

protected void fireAddedError(ErrorType e){
	if ( fErrorListeners != null ) {
		Object[] listeners = fErrorListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			((IBeanProxyHost.ErrorListener)listeners[i]).errorAdded(e);
		}
	}
}

protected void fireClearedError(ErrorType e){
	if ( fErrorListeners != null ) {
		Object[] listeners = fErrorListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			((IBeanProxyHost.ErrorListener)listeners[i]).errorCleared(e);
		}
	}
}

/**
 * Apply all of the settings to the proxy.
 */
protected void applyAllSettings() {
	
	// Now apply all of the feature settings.
	// For the moment, apply in same order as in MOF object. This could cause some problems
	// later, but we attack that then.
	EObject eTarget = getEObject();
	Iterator features = eTarget.eClass().getEAllStructuralFeatures().iterator();
	while (features.hasNext()) {
		EStructuralFeature sf = (EStructuralFeature) features.next();
		if (eTarget.eIsSet(sf)) {
			if (sf.isMany()) {
				appliedList(sf,(List)eTarget.eGet(sf), Notification.NO_INDEX, true);	// Test validity because we are applying all.
			} else {
				// Do not apply the value if it is in error
				if (isValidFeature(sf)){
					applied(sf,eTarget.eGet(sf), Notification.NO_INDEX);			
				} 	
			}
		}
	}
	// Validate all errors
//	getBeanValidator().reset();	
//	getBeanValidator().validateAll();
}

/*
 * Default is to just call canceled, however we do it from the end so that position always make sense.
 */
protected void canceledList(EStructuralFeature sf, List oldValues, int position){
	ListIterator iter = oldValues.listIterator(oldValues.size());
	if (position != -1)
		position += (oldValues.size()-1)	;
	while (iter.hasNext()){
		canceled(sf, iter.next(), position);
		if (position != -1)
			position--;		
	}
}

/**
 * canceled: A setting has been remove from the mof object,
 * now remove it from the bean.
 */
protected void canceled(EStructuralFeature sf , Object oldValue, int position) {
	if (isBeanProxyInstantiated()) {
		if (!inInstantiation() && isInstantiationFeature(sf)) {
			reinstantiateBeanProxy();
			return;
		}		
		// We have something to remove from, so we are live
		PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator( (EModelElement) sf);
		// Only cancel if it is not the this part, or if it is the
		// this part, then only if it is a non-local attribute.
		if (!isThisPart() || !isAttributeLocal(sf)) {
			BeanFeatureDecorator featureDecor = null; 
			if (propertyDecorator == null || propertyDecorator.getWriteMethod() == null) {
				// Note: fields are handled in kludge way, need a better way of doing this. This is being put off until next release.
				// This could be a field instead, see if it has a feature decor. 
				// When we rewrite this, need to take into consideration that the field may be final, in which case it
				// would be read only.
				featureDecor = (BeanFeatureDecorator)Utilities.getDecorator((EModelElement)sf,BeanFeatureDecorator.class);
			}			
			
			if ((propertyDecorator != null && propertyDecorator.getWriteMethod() != null) || featureDecor != null) {			
				IBeanProxyHost value = BeanProxyUtilities.getBeanProxyHost((IJavaInstance)oldValue);
				// Let the attribute cancel the setting. It is assumed that the value has
				// been instantiated since it was previously applied. After canceling the
				// setting, we will dispose of the bean proxy since it is not needed. It
				// would then be recreated on the re-apply if an undo was done.
				// This setting can be adapted to be a proxy			
				synchronized (this) {
					// Synchronize with the instantiate on this bean so that other threads can't actually
					// cancel anything until after the instanciation is finished. Otherwise we could
					// get race conditions of which cancel get's done first.
					// Only apply the value if we actually do have one to apply
					if ( fOrigSettingProxies != null && fOrigSettingProxies.containsKey(sf)){
						applyBeanFeature(sf, propertyDecorator, featureDecor, (IBeanProxy)fOrigSettingProxies.get(sf));
					}
					
					clearError(sf);	// Clear the error for this feature, if one was created. This could of happened
									// while canceling and we reinstantiated and this would of marked this feature in
									// error. But since this feature is actually not set anymore, then there is no error.
									// Or could of been bad already and our canceling it would clear it.
					
				}
				// Always release the bean proxy, unless the feature is not composite, if not composite, somebody else owns it. They will take care of it.
				if (value != null && (sf instanceof EAttribute || ((EReference) sf).isContainment()))
					value.releaseBeanProxy();
			}
		}
	}
}
/**
 * releaseBeanProxy: Get rid of the bean proxy being held.
 * This allows for recreation if needed.
 */
public void releaseBeanProxy() {
	if (isBeanProxyInstantiated() || getErrorStatus() == ERROR_SEVERE) {
		// Either we have a proxy or we failed to instantiate, but in this case
		// our settings may still have been instantiated, so we need to get rid of them too.
		
		// Default is to just throw the bean proxy away.
		// Other subclasses may actual do something first.
		// It will go through the settings and dispose
		// of them too since they were instantiated by this object.
		// Ignore settings that are shared. This is
		// because they are controlled by some other object.
		EObject eTarget = getEObject();
		Iterator settings = (new BeanProxyUtilities.JavaSettingsEList(eTarget, false)).basicIterator();	// Using basic iterator so that don't bother resolving any that have become proxies.
		while (settings.hasNext()) {
			// Using getExistingAdapter so that we don't fluff up a bean proxy host just to release it.
			releaseSetting((IBeanProxyHost) EcoreUtil.getExistingAdapter((Notifier) settings.next(), IBeanProxyHost.BEAN_PROXY_TYPE));					
		}
		if (fOwnsProxy && isBeanProxyInstantiated()) {
			ProxyFactoryRegistry registry = getBeanProxy().getProxyFactoryRegistry();
			// Check if valid, this could of ocurred due to finalizer after registry has been stopped.
			if (registry.isValid())
				registry.releaseProxy(getBeanProxy());	// Give it a chance to clean up
		}
		fBeanProxy = null;	// Now throw it away
		fOwnsProxy = false;
		fOrigSettingProxies = null;	// So next time we get new values.
		notInstantiatedClasses = null;
	}
}
private void releaseSetting(IBeanProxyHost proxyHost) {
	if (proxyHost != null)
		proxyHost.releaseBeanProxy();
}/**
 * About to be thrown away. Run releaseBeanProxy first to
 * make sure that any bean cleanup required is done.
 */
protected void finalize() throws Throwable {
	releaseBeanProxy();
}
/**
 * Get the attribute value from the bean.
 */
public IJavaInstance getBeanPropertyValue(EStructuralFeature aBeanPropertyAttribute) {
	// Only ask if we have a live proxy.
	// Don't query if this is a local attribute and we are the this part.	
	if (isBeanProxyInstantiated() && (!isThisPart() || !isAttributeLocal(aBeanPropertyAttribute))) {
		IBeanProxy valueProxy = getInternalBeanPropertyProxyValue(aBeanPropertyAttribute);
		IJavaInstance bean = BeanProxyUtilities.wrapperBeanProxy( valueProxy , JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain()), false , InstantiationFactory.eINSTANCE.createImplicitAllocation(getEObject(), aBeanPropertyAttribute));
		return bean;
	}
	return null;
}

public IBeanProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute){
	// Only ask if we have a live proxy.
	// Don't query if this is a local attribute and we are the this part.	
	if (isBeanProxyInstantiated() && (!isThisPart() || !isAttributeLocal(aBeanPropertyAttribute))) {
		return getInternalBeanPropertyProxyValue(aBeanPropertyAttribute);
	}
	
	return null;
}

protected IBeanProxy getInternalBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute){
	PropertyDecorator propertyDecorator = Utilities.getPropertyDecorator((EModelElement)aBeanPropertyAttribute);
	// If we have a properyt decorator then it has a get method so just call it
	if ( propertyDecorator != null && propertyDecorator.getReadMethod() != null ) {
		return getBeanProxyValue(aBeanPropertyAttribute, propertyDecorator, null);
	}
	
	// It may be that this is a public field - this has a decorator to get the value
	// ( which also makes it generic for other types of as yet unthought of features )
	BeanFeatureDecorator featureDecor = (BeanFeatureDecorator)Utilities.getDecorator((EModelElement)aBeanPropertyAttribute,BeanFeatureDecorator.class);
	if (featureDecor != null)
		return getBeanProxyValue(aBeanPropertyAttribute, propertyDecorator, featureDecor);
	return null;
}

protected IBeanProxy primReadBeanFeature(PropertyDecorator propDecor, IBeanProxy aSource) throws ThrowableProxy{
	
	return BeanProxyUtilities.readBeanFeature(propDecor, aSource);
		
}

protected IBeanProxy getBeanProxyValue(EStructuralFeature aBeanPropertyAttribute, PropertyDecorator propertyDecorator, BeanFeatureDecorator featureDecor) {
	if ( propertyDecorator != null && propertyDecorator.getReadMethod() != null) {
		try {
			return primReadBeanFeature(propertyDecorator,getBeanProxy()) ;
		} catch (ThrowableProxy e) {
			JavaVEPlugin.log(e, Level.FINE);	// An exception on query is just FINE, not a warning.
		}
	} 
	// It may be that this is a public field - this has a decorator to get the value
	// ( which also makes it generic for other types of as yet unthought of features )
	if ( featureDecor != null ) {
		IBeanProxyFeatureMediator mediator = getFeatureMediator(featureDecor);
		if ( mediator != null ) {
			try {
				return mediator.getValue(aBeanPropertyAttribute, this.getBeanProxy());					
			} catch ( Exception exc ) {
				JavaVEPlugin.log(exc, Level.FINE);	// An exception on query is just FINE, not a warning.
			}
		}
	}
	return null;	
}
/**
 * Answer the bean proxy if we have one.
 */
public final IBeanProxy getBeanProxy() {
	return fBeanProxy;
}
/**
 * Return the bean Original Settings Hashtable.
 *
 * Warning: This should not normally be called.
 * It is here so that bean customizer support can
 * access it.
 */
public Map getOriginalSettingsTable() {
	if (fOrigSettingProxies == null)
		fOrigSettingProxies = new HashMap(20);
	return fOrigSettingProxies;
}
/**
 * Create the bean proxy through the current factory
 * Decide which method to use based on how much information we have.
 * If we have an initialization string use it
 */
public final /* TODO synchronized */ IBeanProxy instantiateBeanProxy() {
	// Synchronize the creation with apply/cancel so that no changes are done during
	// creation, and no creation is tried while a creation is in progress.
	if (!isBeanProxyInstantiated()){
		inInstantiation = true;
		try {
			while (true) {
				try {
					primInstantiateBeanProxy();
					if (getErrorStatus() != ERROR_SEVERE)
						applyAllSettings();
					break;
				} catch (ReinstantiationNeeded e) {
					releaseBeanProxy();	// Release the bean proxy.
					continue;	// We need reinstantiation, so keep trying
				}
			}
		} finally {
			inInstantiation = false;
		}
	}
	return fBeanProxy;
}


/**
 * A child has asked to be re-created.  By default just created it
 * This should be specialized for containment relationships
 */
public void reinstantiateChild(IBeanProxyHost aChildProxyHost) {
	aChildProxyHost.releaseBeanProxy();
	// Now just reapply it.
	EObject child = (EObject) aChildProxyHost.getTarget(); 
	EStructuralFeature sfs[] = InverseMaintenanceAdapter.getReferencesFrom(getEObject(), child);
	for (int i = 0; i < sfs.length; i++) {
		int index = Notification.NO_INDEX;
		EStructuralFeature sf = sfs[i];		
		if (sf.isMany()) {
			List values = (List) getEObject().eGet(sf);
			index = values.indexOf(child);
		}
		applied(sf, child, index);
	}
}
/**
 *  Instantiate the bean proxy given an existing one, used for implicit settings
 */
public final synchronized IBeanProxy instantiateBeanProxy(IBeanProxy aBeanProxy){
		inInstantiation = true;
		fOwnsProxy = false;		
		try {
			while (true) {
				try {
					setupBeanProxy(aBeanProxy);
					applyAllSettings();
					break;
				} catch (ReinstantiationNeeded e) {
					continue;	// We need reinstantiation, so keep trying. However for this case, it is not to recreate the bean, since the bean was already given to us as is. It is to keep trying the applies until it works.
				}
			}
		} finally {
			inInstantiation = false;
		}	
	return fBeanProxy;
}

/* Perform primitive instantiation of the bean without applying any settings
 */
protected void primInstantiateBeanProxy() {
	// First create the bean
	fIsThis = null; // So we re-query to find out.
	fInstantiationError = null; // Clear the field that holds any instantiation errors before we create it
	if (domain.getProxyFactoryRegistry().isValid()) {
		if (!isThisPart()) {
			if (getJavaObject().isSetAllocation()) {
				JavaAllocation allocation = getJavaObject().getAllocation();
				fOwnsProxy = true;
				try {
					setupBeanProxy(beanProxyAllocation(allocation));
				} catch (IAllocationProcesser.AllocationException e) {
					processInstantiationError(e.getCause());
				} catch (IllegalArgumentException e) {
					processInstantiationError(e);
				}
				return;
			}
			
			// otherwise just create it using the default ctor.
			IBeanTypeProxy targetClass = getTargetTypeProxy();
			try {
				fOwnsProxy = true; // Since we created it, obviously we own it.
				setupBeanProxy(basicInitializationStringAllocation(null,targetClass));
			} catch (IAllocationProcesser.AllocationException exc) {
				processInstantiationError(exc.getCause());
			}			
		} else {
			// We are a "this" part, so instantiate the super class (go up until we reach one that is not abstract) instead.
			// Also, since the initialization string applies only to the target class, we
			// can't have one when instantiating the superclass.
			notInstantiatedClasses = new ArrayList(2);
			JavaClass thisClass = (JavaClass) ((IJavaInstance) target).getJavaType();
			notInstantiatedClasses.add(thisClass);
			JavaClass superclass = thisClass.getSupertype();
			while (superclass != null && superclass.isAbstract()) {
				notInstantiatedClasses.add(superclass);
				superclass = superclass.getSupertype();
			}
			IBeanTypeProxy targetClass = null;
			if (superclass != null)
				targetClass =
					domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(superclass.getQualifiedNameForReflection());

			try {
				fOwnsProxy = true; // Since we created it, obviously we own it				
				setupBeanProxy(basicInitializationStringAllocation(null,targetClass));
			} catch (IAllocationProcesser.AllocationException exc) {
				processInstantiationError(exc.getCause());
			}
		}
	}
}
/**
 * @param domain2
 * @param allocation
 * Temporary method here just to allow SWT proxies to refactor the basic initialization to run it on the display thread 
 * 
 * @since 1.0.0
 */
protected IBeanProxy beanProxyAllocation(JavaAllocation allocation) throws AllocationException {
	return getBeanProxyDomain().getAllocationProcesser().allocate(allocation); 
}
/**
 * @param aString
 * @param targetClass 
 * 
 * @since 1.0.0
 */
protected IBeanProxy basicInitializationStringAllocation(String aString, IBeanTypeProxy targetClass) throws AllocationException{
	return BasicAllocationProcesser.instantiateWithString(aString, targetClass);
}
protected IBeanTypeProxy getTargetTypeProxy() {
	String qualifiedClassName = getJavaObject().getJavaType().getQualifiedNameForReflection();
	IBeanTypeProxy targetClass = domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(qualifiedClassName);					
	return targetClass;
}

/**
 * isAdaptorForType.
 */
public boolean isAdapterForType(Object type) {
	return BEAN_PROXY_TYPE == type || ERROR_NOTIFIER_TYPE == type || ERROR_HOLDER_TYPE == type;
}
/**
 * See if this attribute is at this level, or is
 * it inherited. Answer true if local.
 */
protected boolean isAttributeLocal(EStructuralFeature attr) {
	return notInstantiatedClasses != null ? notInstantiatedClasses.contains(attr.eContainer()) : false;
}
/**
 * When the bean proxy is instantiated it is stored in a field, so testing the field tells us
 * whether or not the bean proxy has been instantiated or not
 */
public final boolean isBeanProxyInstantiated() {
	return fBeanProxy != null && fBeanProxy.isValid();
}
/**
 * isThisPart method comment.
 */
protected boolean isThisPart() {
	if (fIsThis == null) {
		// TODO Got to get rid of "thisPart" stuff. It is a bad hack.
		EStructuralFeature containersf = getEObject().eContainmentFeature();
		fIsThis = (containersf != null && "thisPart".equals(containersf.getName())) ? Boolean.TRUE : Boolean.FALSE; //$NON-NLS-1$
	}
	return fIsThis.booleanValue();
}
/**
 * Default behavior is to invalidate, then validate.
 */
public void revalidateBeanProxy(){
	invalidateBeanProxy();
	validateBeanProxy();
}

/**
 * Set a bean proxy into this adaptor, if not already set.
 *
 * Warning: This is a dangerous method because it could
 * set a proxy that is not consistent with the target MOF object.
 * Creation date: (1/10/00 12:41:53 PM)
 * @param beanProxy com.ibm.mom.beaninfo.IBeanProxy
 */
public void setBeanProxy(IBeanProxy beanProxy) {
	if (fBeanProxy == null) {
		fOwnsProxy = false;
		setupBeanProxy(beanProxy);
	}
}

/**
 * Set a bean proxy into this adapter and if necessary
 * change the type of the java object instance, This is
 * so that if what was created was a subclass, then we
 * should be that subclass. This is so that if the type
 * of the property was say an interface LayoutManager,
 * this doesn't help us to put up the correct property
 * editor. The PropertyEditor for LayoutManager doesn't
 * exist. You want to know it is a FlowLayoutManager.
 * So when it comes back as a FlowLayoutManager.
 * 
 * Subclasses may override setupBeanProxy, but they should always
 * call super.setupBeanProxy().
 */
protected void setupBeanProxy(IBeanProxy beanProxy) {
	fBeanProxy = beanProxy;
	if (beanProxy == null)
		processInstantiationError(new IllegalStateException(JavaMessages.getString("BeanProxyAdapter.NoBeanInstantiatedForSomeReason_EXC_"))); //$NON-NLS-1$
	else if (!beanProxy.getTypeProxy().isPrimitive()) {
		// We are trying to set a non-primitive and non-null proxy. Primitives aren't valid here because
		// this proxy adapter is only valid for non-primitives.
		String qualifiedClassName = beanProxy.getTypeProxy().getTypeName();
		JavaClass currentType = (JavaClass) ((IJavaInstance) target).getJavaType();
		if (!currentType.getQualifiedNameForReflection().equals(qualifiedClassName)) {
			JavaClass javaClass = (JavaClass) JavaRefFactory.eINSTANCE.reflectType(qualifiedClassName, currentType.eResource().getResourceSet());
			// Only perform the change if the new type is assignable to the old type, otherwise leave the old type alone.
			if (currentType.isAssignableFrom(javaClass)) {
				if (javaClass.getKind() == TypeKind.UNDEFINED_LITERAL)
					return;
				((InternalEObject) target).eSetClass(javaClass);
			}
		}
	}
}
public int getErrorStatus(){
	
	if (fInstantiationError != null){
		return ERROR_SEVERE;
	} 
	ErrorType mostSeverePropertyError = getMostSeverPropertyError();
	if(mostSeverePropertyError != null){
		return mostSeverePropertyError.severity;
	} else {
		return IErrorHolder.ERROR_NONE;
	}	
}
public ErrorType getMostSeverPropertyError(){
	// See whether any of the featuer errors are warning or information
	Iterator errors = keyedErrors.values().iterator();
	ErrorType informationError = null;		
	while( errors.hasNext() ) {
		Object error = errors.next();
		if (error instanceof List) {
		// See whether the error is warning or informational
		// Warning errors are more severe than information, so when we find one we return
			List eList = (List) error;
			for (int i = 0; i < eList.size(); i++) {
				ErrorType eType = (ErrorType) eList.get(i);
				if ( eType.getSeverity() == ERROR_WARNING ) {
					return eType;
				} else if ( eType.getSeverity() == ERROR_INFO ) {
					informationError = eType;
				}
			}
		} else {
			// See whether the error is warning or informational
			// Warning errors are more severe than information, so when we find one we return
			ErrorType eType = (ErrorType) error;
			if ( eType.getSeverity() == ERROR_WARNING ) {
				return eType;
			} else if ( eType.getSeverity() == ERROR_INFO ) {
				informationError = eType;
			}
		}
	}
	return informationError;
}
public List getErrors(){
	
	ArrayList result = new ArrayList(2);
	// If there is an error where the Bean can't be instantiated this is as severe as it can get - return it
	if ( fInstantiationError != null){
		result.add(new IErrorNotifier.ExceptionError(fInstantiationError,ERROR_SEVERE));
		return result;
	}
		
	// If the Bean was able to have been applied then it could have property errors, return these
	if( fInstantiationError == null ) {
		Iterator iter = keyedErrors.values().iterator();
		while(iter.hasNext()){
			Object v = iter.next();
			if (v instanceof List)
				result.addAll((List) v);
			else
				result.add(v);
		}
	}

	return result;	
}
/**
 * Set ownership of the proxy.
 * @see IBeanProxyHost
 */
public void setOwnsProxy(boolean ownsProxy) {
	fOwnsProxy = ownsProxy;
}
protected final EObject getEObject(){
	return (EObject)target;
}

protected final IJavaObjectInstance getJavaObject() {
	return (IJavaObjectInstance) target;
} 

public final IBeanProxyDomain getBeanProxyDomain() {
	return domain;
}

protected ListenerList fErrorListeners;

public void addErrorListener(ErrorListener aListener) {
	if(fErrorListeners == null) fErrorListeners = new ListenerList(2);
	fErrorListeners.add(aListener);
}

public void removeErrorListener(ErrorListener aListener) {
	if (fErrorListeners != null)
		fErrorListeners.remove(aListener);
}

/**
 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#invalidateBeanProxy()
 */
public void invalidateBeanProxy() {
}

/**
 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#validateBeanProxy()
 */
public void validateBeanProxy() {
}

}
