package org.eclipse.ve.internal.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.IControlProxyHost;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
import org.eclipse.ve.internal.java.visual.VisualUtilities;
 
public class CompositeProxyAdapter extends ControlProxyAdapter implements IHoldProcessing {
	//TODO AWT ContainerProxyAdapter has IHoldProcessing - does this need to be part of JBCF ?
	protected EReference sf_containerControls;
	private IMethodProxy layoutMethodProxy;  // Field for method proxy to layout();
	private IMethodProxy moveAboveMethodProxy, moveBelowMethodProxy;	// method proxy for move above and below
	private IBeanTypeProxy compositeManager;
	// TODO these method proxies should be off in the factory constants so we don't need to get it for each and every composite.

	
	public CompositeProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sf_containerControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);

	}
	
	/*
	 * Return the first instantiated bean proxy at or after the given index.
	 * It is assumed that tests for the container being instantiated has already been done.
	 * Return null if not found.
	 */
	protected IBeanProxy getBeanProxyAt(int position) {
		if (position < 0)
			return null;
		List controls = (List) ((EObject) getTarget()).eGet(sf_containerControls);
		for (int i=position; i<controls.size(); i++) {
			EObject control = (EObject) controls.get(i);
			IBeanProxyHost controlProxyHost =
				BeanProxyUtilities.getBeanProxyHost((IJavaInstance) control);
			if (controlProxyHost.isBeanProxyInstantiated())
				return controlProxyHost.getBeanProxy();
		}
		
		return null;
	}	
	
	protected void appliedList(EStructuralFeature sf, List newValues, int position, boolean testValidity){
		// The default inherited behavior is to iterate everything in the list and apply it one by one
		// This is not good for SWT composites because we get a new image on each apply. So in case
		// of list, we will do a primitive add, and then validate once at the end.
		if(sf == sf_containerControls){
			Iterator iter = newValues.iterator();
			while(iter.hasNext()){
				primAddControl((IJavaObjectInstance)iter.next(), position++);
			}
			childValidated(this);
		} else 
			super.appliedList(sf, newValues, position, testValidity);
	}

	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (as == sf_containerControls) {
			addControl((IJavaObjectInstance) newValue, position);
		} else if ("layout".equals(as.getName())) {
			// If switching layouts we must ensure that the children's layoutData matches that of our new layout manager			
			ensureChildLayoutDataCorrect(newValue);
			super.applied(as, newValue, position);			
		}else {
			super.applied(as, newValue, position);
		}
	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (sf == sf_containerControls) {
			removeControl((IJavaObjectInstance)oldValue);
		} else {
			super.canceled(sf, oldValue, position);
		}

	}
	
	private void ensureChildLayoutDataCorrect(Object newLayout){
		// Do not process if the layout manager is bad, possibly following a bad parse error
		IBeanProxyHost layoutProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance)newLayout);
		if(layoutProxyHost != null){
			layoutProxyHost.instantiateBeanProxy();
			if(layoutProxyHost.getErrorStatus() == IBeanProxyHost.ERROR_SEVERE) return;
		}
		// Some layouts physically set the layoutData of their child controls, for example
		// GridLayout sets its composite's children to have a layoutData of GridData
		// This is done on the target VM without the VE having a chance to model it
		// If in code a user changes the layout from GridData to another then the layoutData
		// needs fixing up as it will be incorrect and cause class cast exceptions
		IBeanProxy constraintBeanProxy = null;
		if(newLayout != null){
			ILayoutPolicyFactory layoutPolicyFactory = VisualUtilities.getLayoutPolicyFactory(((EObject)newLayout).eClass(),null);
			JavaClass constraintClass = layoutPolicyFactory == null ?
			    null :
		        layoutPolicyFactory.getConstraintClass(((EObject)getTarget()).eResource().getResourceSet());
			if(constraintClass != null){
				 constraintBeanProxy = getBeanProxy().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(constraintClass.getQualifiedName());
			}
		}
    	// There is a target VM helper that does all of the checking and correcting to avoid VM traffic
		final IMethodProxy methodProxy = getCompositeManager().getMethodProxy("ensureChildLayoutDataCorrect",new String[] {"org.eclipse.swt.widgets.Composite","java.lang.String"});
		final IBeanProxy finalConstraintBeanProxy = constraintBeanProxy;
		// Create a BeanProxy for the name of the constraint class
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable(){
			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				methodProxy.invokeCatchThrowableExceptions(getCompositeManager(),new IBeanProxy[] {getBeanProxy(),finalConstraintBeanProxy});
				return null;
			}
		});	
	}
	
	private IBeanTypeProxy getCompositeManager(){
		if(compositeManager == null){
			compositeManager = getBeanProxy().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.CompositeManager");
		}
		return compositeManager;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#moved(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, int)
	 */
	protected void moved(EStructuralFeature feature, Object value, int oldPosition, int newPosition) {
		if (feature == sf_containerControls) {
			final IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) value);
			if (controlProxyHost == null || !controlProxyHost.isBeanProxyInstantiated())
				return;	// Not instantiated, don't try to move on the jvm.
			
			// A move of "components" can be done simply here by using moveAbove instead of remove and add.
			IBeanProxy above = getBeanProxyAt(newPosition+1);
			if (above != null)
				moveComponentBefore(controlProxyHost, above);
			else
				moveComponentToEnd(controlProxyHost);
			childValidated(this);
		} else
			super.moved(feature, value, oldPosition, newPosition);
	}

	protected IMethodProxy layoutMethodProxy(){
		if(layoutMethodProxy == null){
			layoutMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("layout"); //$NON-NLS-1$
		}
		return layoutMethodProxy;
	}
	
	protected IMethodProxy moveAboveMethodProxy(){
		if(moveAboveMethodProxy == null){
			moveAboveMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("moveAbove", "org.eclipse.swt.widgets.Control"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return moveAboveMethodProxy;
	}	
	
	protected IMethodProxy moveBelowMethodProxy(){
		if(moveBelowMethodProxy == null){
			moveBelowMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("moveBelow", "org.eclipse.swt.widgets.Control"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return moveBelowMethodProxy;
	}
	
	public void reinstantiateChild(IBeanProxyHost aChildProxyHost) {
		// Find the index of the child - remove it and re-insert it at this position
		IJavaObjectInstance child = (IJavaObjectInstance)aChildProxyHost.getTarget();
		List controls = (List) ((IJavaObjectInstance)getTarget()).eGet(sf_containerControls);
		int indexOfChild = controls.indexOf(child);
		removeControl(child);
		addControl(child, indexOfChild);
	}
	
	
	/**
	 * @param child
	 * @param indexOfChild
	 * 
	 * @since 1.0.2
	 */
	protected void addControl(IJavaObjectInstance child, int indexOfChild) {
		primAddControl(child,indexOfChild);
		childValidated(this);
	}
	
	protected void setupBeanProxy(IBeanProxy beanProxy) {		
		super.setupBeanProxy(beanProxy);
		// Hack for now till I can talk this through with Rich properly - JRW
		// If we don't declare the fact that implicit allocation is not owned then we dispose them
		// and we shouldn't as it is the responsibility of the owner to dispose them
		JavaAllocation allocation = getJavaObject().getAllocation();
		if(allocation instanceof ImplicitAllocation){
			fOwnsProxy = false;
		}
		
	}

	public void releaseBeanProxy() {
				
		// Need to release all of the controls.  This is because they will be implicitly disposed anyway when super
		// gets called because the target VM will dispose them as children
		// If they have been implicitly disposed on the target VM but the IBeanProxyHost doesn't know about this then i
		// still thinks they are there and will try to re-dispose them and also it'll remain listening for changes 
		// and this causes stack errors - bugzilla 60017
		List controls = (List) ((IJavaObjectInstance)getTarget()).eGet(sf_containerControls);
		Iterator iter = controls.iterator();
		while(iter.hasNext()){
			IBeanProxyHost value = (IBeanProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaInstance)iter.next());
			if (value != null)
				value.releaseBeanProxy();
		}
		super.releaseBeanProxy();
	}
	
	protected void primAddControl(IJavaObjectInstance aControl, int position) {
		final IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost(aControl);
		((ControlProxyAdapter) controlProxyHost).setParentProxyHost(null);	// So that it doesn't notify us of changes until after we have it added.
		controlProxyHost.releaseBeanProxy();
		controlProxyHost.instantiateBeanProxy();

		// Now we need to move it above the correct guy.
		IBeanProxy before = getBeanProxyAt(position + 1);
		if (before != null) {
			moveComponentBefore(controlProxyHost, before);
		}
		
		((ControlProxyAdapter) controlProxyHost).setParentProxyHost(this);	// Now we can be notified of changes.		
	}
	
	/**
	 * @param controlProxyHost
	 * @param before
	 * 
	 * @since 1.0.2
	 */
	protected void moveComponentBefore(final IBeanProxyHost controlProxyHost, final IBeanProxy before) {
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {

			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				moveAboveMethodProxy().invokeCatchThrowableExceptions(controlProxyHost.getBeanProxy(), before);
				return null;
			}
		});
	}
	
	/**
	 * @param controlProxyHost
	 * 
	 * @since 1.0.2
	 */
	protected void moveComponentToEnd(final IBeanProxyHost controlProxyHost) {
		invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {

			public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
				moveBelowMethodProxy().invokeCatchThrowableExceptions(controlProxyHost.getBeanProxy(), (IBeanProxy) null);
				return null;
			}
		});
	}

	protected void removeControl(IJavaObjectInstance aControl) throws ReinstantiationNeeded {
		// Dispose the control
		IBeanProxyHost controlProxyHost = BeanProxyUtilities.getBeanProxyHost(aControl);
		controlProxyHost.releaseBeanProxy();
		revalidateBeanProxy();
	}
	
	public void childValidated(IControlProxyHost childProxy) {
		// Hold up layout processing if we are executing a HoldProcessingCommand
	    if (!holding()) {
	        // We are the top with no parents, do a layout() on us
	        invokeSyncExecCatchThrowableExceptions(new DisplayManager.DisplayRunnable() {
	
	            public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
	            	// Not sure why the bean proxy is null here but need to check for it
	            	if (getBeanProxy() != null)
	            		// Call the layout() method
	            		return layoutMethodProxy().invoke(getBeanProxy());
	            	return null;
	            }
	        });
	        if (imSupport != null) refreshImage();
	        if (parentProxyAdapter != null) super.childValidated(childProxy);
	    }
	}	
	private int holdCount = 0;
	
	protected final boolean holding() {
		return holdCount > 0;
	}
	
	/**
	 * ContainerProxyAdapter holds the "components" relationship listening to
	 * allow major changes to occur. When resumed, it will completely refresh
	 * the "components" relationship.
	 * @see org.eclipse.ve.internal.jfc.core.IHoldProcessing#holdProcessing()
	 */
	public final void holdProcessing() {
		holdCount++;
	}
	
	/**
	 * @see org.eclipse.ve.internal.jfc.core.IHoldProcessing#resumeProcessing()
	 */
	public final void resumeProcessing() {
		if (--holdCount == 0) {
			holdEnded();
		} else if (holdCount < 0)
			holdCount = 0;
	}
	
	protected void holdEnded() {
		// Force the Composite to re-layout its controls
		revalidateBeanProxy();
	}

}