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
 *  $RCSfile: CompositionProxyAdapter.java,v $
 *  $Revision: 1.12 $  $Date: 2005-02-15 23:23:54 $ 
 */
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.NotifierImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.remote.REMConnection;
import org.eclipse.jem.util.TimerTests;

import org.eclipse.ve.internal.cde.core.CDEUtilities;

import org.eclipse.ve.internal.jcm.*;


/**
 * This is a special adaptor for BeanCompositions.
 * There really isn't a bean proxy, but we need to handle
 * the disposition of beanproxies. This will be where this is done.
 *
 * It is also a notifier. What will happen is it will renotify all
 * events before processing the event. This is useful because
 * other adapters that need to do something before this one cleans
 * up the proxies.
 *
 */
public class CompositionProxyAdapter extends NotifierImpl implements Adapter {
	public static final Class BEAN_COMPOSITION_PROXY = CompositionProxyAdapter.class;
	
	public static final int RELEASE_PROXIES = 3000000;
	// A BIG KLUDGE AT THE MOMENT TO NOTIFY THE COMPOSITION ADAPTERS TO RELEASE ALL PROXIES.
	public static final int INSTANTIATE_PROXIES = 3000001;
	// A BIG KLUDGE AT THE MOMENT TO NOTIFY THE COMPOSITION ADAPTERS TO INSTANTIATE ALL PROXIES.	
	
	Notifier target;
	
public Notifier getTarget() {
	return target;
}	

public void setTarget(Notifier target) {
	this.target = target;
}

public boolean isAdapterForType(Object type) {
	return BEAN_COMPOSITION_PROXY.equals(type);
}

public void notifyChanged(Notification msg) {
	if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
		eAdapters().clear();
	} else
		eNotify(msg);	// Notify listeners of the change before we do anything with it.
	if (msg.getEventType() == RELEASE_PROXIES) {
		releaseBeanProxy();
	} else if (msg.getEventType() == INSTANTIATE_PROXIES) {
		initBeanProxy();
	} else {
		int fid = msg.getFeatureID(MemberContainer.class); 
		if (fid != JCMPackage.MEMBER_CONTAINER__MEMBERS && fid != JCMPackage.MEMBER_CONTAINER__PROPERTIES) {
			// Not members or properties. Those will be handled by their parent instead. 
			switch (msg.getEventType()) {
				case Notification.REMOVING_ADAPTER:
					releaseBeanProxy();
					break;
				case Notification.ADD:
				case Notification.SET:
					if (!CDEUtilities.isUnset(msg)) {
						releaseSetting(msg.getOldValue());
						initSetting(msg.getNewValue());
						break;
					}	// else flow into unset.
				case Notification.REMOVE:
				case Notification.UNSET:
					releaseSetting(msg.getOldValue());
					break;
				case Notification.ADD_MANY:
					Iterator itr = ((List) msg.getNewValue()).iterator();
					while (itr.hasNext())
						initSetting(itr.next());
					break;
				case Notification.REMOVE_MANY:
					itr = ((List) msg.getOldValue()).iterator();
					while (itr.hasNext())
						releaseSetting(itr.next());
					break;
			}
		}
	}
}

public void initBeanProxy() {
	// TODO Should Only instantiate this and components, but we may not be a BeanSubclassComposition. Need a better
	// way of handling this.
	if (getTarget() instanceof BeanSubclassComposition) {
		TimerTests.basicTest.startStep("Init this");
		TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_STEP);
		TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_METHOD_STEP);
		initSetting(((BeanSubclassComposition) getTarget()).getThisPart());
		TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_METHOD_STEP);
		TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_STEP);
		TimerTests.basicTest.stopStep("Init this");
	}

	// Next run the components.
	List components = ((BeanComposition) getTarget()).getComponents();
	for (int i = 0; i < components.size(); i++) {
		String step = "init#"+i;
		TimerTests.basicTest.startStep(step);
		TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_STEP);
		TimerTests.basicTest.startAccumulating(REMConnection.INVOKE_METHOD_STEP);
		initSetting(components.get(i));
		TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_METHOD_STEP);
		TimerTests.basicTest.stopAccumulating(REMConnection.INVOKE_STEP);
		TimerTests.basicTest.stopStep(step);
	}
}

/**
 * When we are explicitly disposed dispose all bean proxies on all values
 * of our composition.
 */
public void releaseBeanProxy() {

	// It will go through the attribute settings and dispose
	// of them too since they were instantiated by this object. This does all of the containment because
	// some properties may of been initialized and so aren't known as components.
	Iterator settings = ((EObject)target).eContents().iterator();	// Get only the attrs and composite refs.
	while (settings.hasNext()) {
		releaseSetting(settings.next());
	}
}

protected void initSetting(Object v) {
	if (v instanceof IJavaInstance) {	
		IBeanProxyHost value = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) v);
		if (value != null && value.getBeanProxyDomain().getProxyFactoryRegistry().isValid()) {
			value.instantiateBeanProxy();
		}
	}
}

protected void releaseSetting(Object v) {
	if (v instanceof IJavaInstance) {	
		// Get existing adapter, if it doesn't have one, don't create it.
		final IBeanProxyHost value = (IBeanProxyHost) EcoreUtil.getExistingAdapter((Notifier) v, IBeanProxyHost.BEAN_PROXY_TYPE);
		if (value != null) {
			Platform.run(new ISafeRunnable() {
				public void handleException(Throwable exception) {
					JavaVEPlugin.getPlugin().getLogger().log(exception, Level.WARNING);
				}

				public void run() throws Exception {
					value.releaseBeanProxy();	// Dispose of a bean proxy automatically takes care of dispose any of the children of the proxy.
				}
			});
		}
	}
}

/**
 * isAdaptorForType method comment.
 */
public boolean isAdaptorForType(Object type) {
	return BEAN_COMPOSITION_PROXY.equals(type);
}
}
