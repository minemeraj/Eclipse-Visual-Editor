package org.eclipse.ve.internal.jface;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.swt.UIThreadOnlyProxyAdapter;


public class TreeViewerProxyAdapter extends UIThreadOnlyProxyAdapter {

	private EStructuralFeature sf_tree;

	public TreeViewerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_tree = ((EObject)newTarget).eClass().getEStructuralFeature("tree");		
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
	}

}
