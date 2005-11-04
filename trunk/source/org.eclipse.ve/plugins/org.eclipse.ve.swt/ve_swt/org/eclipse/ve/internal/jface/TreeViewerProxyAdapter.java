package org.eclipse.ve.internal.jface;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.JavaAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

import org.eclipse.ve.internal.swt.BeanSWTUtilities;
import org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter;


public class TreeViewerProxyAdapter extends UIThreadOnlyProxyAdapter {

	private EStructuralFeature sf_tree;
	
	// Was the tree created by the TreeViewer?
	// This is determined by the "tree" setting and seeing if it is implicit allocation of the tree property.
	// If it is , or there is no setting, then the tree is owned by the viewer.
	// If is not an implicit allocation of the tree property, then it is not owned by the viewer.
	// This field will be set on instantiation and on each apply so that we know what the state was.
	// We can't look at the setting at release time because by then the setting may of been canceled but
	// still what it was (because it actually can't change once instantiated).
	//
	// So we rely on the convention that the "tree" feature will always be set at least once so that we can determine the type.
	boolean ownsTree;	 

	public TreeViewerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_tree = ((EObject)newTarget).eClass().getEStructuralFeature("tree");		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#primInstantiateDroppedPart(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected IProxy primInstantiateDroppedPart(IExpression expression) throws AllocationException {
		// We are instantiating. We need to determine if we own the tree or not. After instantiation we will follow the 
		// applied settings to determine this.
		IJavaInstance tree = (IJavaInstance) getEObject().eGet(sf_tree);
		setOwnsTree(tree);
		return super.primInstantiateDroppedPart(expression);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter#primApplied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void primApplied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if (feature == sf_tree)
			setOwnsTree((IJavaInstance) value);
		super.primApplied(feature, value, index, isTouch, expression, testValidity);
	}

	/*
	 * Set if we own the tree or not.
	 * @param tree
	 * 
	 * @since 1.2.0
	 */
	private void setOwnsTree(IJavaInstance tree) {
		if (tree != null) {
			JavaAllocation alloc = tree.getAllocation();
			if (alloc != null && alloc.isImplicit()) {
				ImplicitAllocation impAlloc = (ImplicitAllocation) alloc;
				ownsTree = impAlloc.getParent() == getTarget() && impAlloc.getFeature() == sf_tree;
			} else
				ownsTree = false;	// If no alloc or not implicit, then we don't own it.
		} else
			ownsTree = true;	// Assume we own it.
	}
	
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, final IExpression expression, final ForExpression forExpression) {
		if(true){
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);			
		}
		if (aBeanPropertyFeature == sf_tree) {			
			if (!onUIThread()) {
				return (IProxy) invokeSyncExecCatchThrowable(new DisplayManager.ExpressionDisplayRunnable(expression) {		
					protected Object doRun(IBeanProxy displayProxy) throws ThrowableProxy {
						return getTreeProxy(expression,forExpression);
					}
				});
			} else {
				return getTreeProxy(expression,forExpression);
			}
		} else {
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
		}
	}
	
	protected IProxy getTreeProxy(IExpression expression, ForExpression forExpression){
		ExpressionProxy result = expression.createProxyAssignmentExpression(forExpression);
		expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT, "getTree", true, 0);
		expression.createProxyExpression(ForExpression.METHOD_RECEIVER, getBeanProxy());
		return result;								
	}
	
	protected void primPrimReleaseBeanProxy(IExpression expression) {
		// We need to physically release the tree if we created the tree. Otherwise it will never go away.
		if (isOwnsProxy() && isBeanProxyInstantiated() && ownsTree) {
			// Since the tree proxy can't change once instantiated, we can use the getBeanPropertyProxyValue call
			// to get the value.
			BeanSWTUtilities.invoke_WidgetDispose(getBeanPropertyProxyValue(sf_tree, expression, ForExpression.ROOTEXPRESSION), expression, getModelChangeController());
		}
	}

}
