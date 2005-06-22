/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BasicAllocationProcesser.java,v $
 *  $Revision: 1.17 $  $Date: 2005-06-22 21:05:23 $ 
 */
package org.eclipse.ve.internal.java.core;
 
import java.text.MessageFormat;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.ParseTreeAllocationInstantiationVisitor;
import org.eclipse.jem.internal.instantiation.base.ParseTreeAllocationInstantiationVisitor.ProcessingException;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;

/**
 * The basic allocation processer. It handles the basic allocations.
 * <ul>
 *   <li> InitStringAllocation
 *   <li> ImplicitAllocation
 *   <li> ParseTreeAllocation
 * </ul>
 * 
 * <p>It needs to be registered with the AllocationAdapterFactory in use. The method registerWithFactory can be used
 * to simplify things.
 * 
 * @see org.eclipse.ve.internal.java.core.IAllocationProcesser
 * @see org.eclipse.jem.internal.instantiation.InitStringAllocation
 * @see org.eclipse.jem.internal.instantiation.ImplicitAllocation
 * @see org.eclipse.jem.internal.instantiation.ParseTreeAllocation
 * 
 * @since 1.0.0
 */
public class BasicAllocationProcesser implements IAllocationProcesser {
	
	/*
	 * This is an instantation visitor which can handle emf references. 
	 * 
	 * @since 1.0.0
	 */
	private static class ParseAllocation extends ParseTreeAllocationInstantiationVisitor {
		
		private IBeanTypeProxy thisType;
		
		public ParseAllocation(IBeanTypeProxy thisType) {
			this.thisType = thisType;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.instantiation.ParseVisitor#visit(org.eclipse.jem.internal.instantiation.PTInstanceReference)
		 */
		public boolean visit(PTInstanceReference node) {
			// It might be possible that the reference has not yet been instantiated. This could happen because we are refering to an
			// object in the constructor that is not on the freeform and is not a property or a child of bean that is on the freeform.
			// In that case it won't be instantiated on its own. So we will need to instantiate here.
			IInternalBeanProxyHost proxyHost = (IInternalBeanProxyHost) BeanProxyUtilities.getBeanProxyHost(node.getObject());
			IExpression exp = getExpression();
			if (!proxyHost.isBeanProxyInstantiated() && !proxyHost.inInstantiation()) {			
				// Instantiate it.
				exp.createSubexpression();
				try {
					proxyHost.instantiateBeanProxy(exp);
				} finally {
					if (exp.isValid())
						exp.createSubexpressionEnd();
				}
			}
			exp.createProxyExpression(getNextExpression(), proxyHost.getProxy());
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.instantiation.ParseVisitor#visit(org.eclipse.jem.internal.instantiation.PTMethodInvocation)
		 */
		public boolean visit(PTMethodInvocation node) {
			// We are handling this.getClass() and getClass() special because we can possibly get the this.class. Normally "this...." anything 
			// cannot be handled because we don't have a "this" object to work with. So if getClass() or this.getClass() then we know we
			// can simply return the thisType we have already stored within us.
			if ("getClass".equals(node.getName()) && (node.getReceiver() == null || node.getReceiver() instanceof PTThisLiteral)) { //$NON-NLS-1$
				if (thisType == null)
					throw new IllegalArgumentException(JavaMessages.BasicAllocationProcesser_ThisTypeNotFoundOrInvalid_EXC_); 
				try {
					getExpression().createProxyExpression(getNextExpression(), thisType);
				} catch (IllegalStateException e) {
					throw new ProcessingException(e);
				}
				return false;
			} else 
				return super.visit(node);
		}
}
	
	protected IBeanProxyDomain domain; 

	public BasicAllocationProcesser() {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IAllocationProcesser#allocate(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	public IBeanProxy allocate(JavaAllocation allocation) throws AllocationException {
		EClass allocClass = allocation.eClass();
		// We are using explicit tests here for type of allocation so that these can be overridden by
		// others. If we used instanceof, then the overrides that are subclasses may be grabbed here
		// instead of where intended.
		if (allocClass == InstantiationPackage.eINSTANCE.getParseTreeAllocation())
			return allocate((ParseTreeAllocation) allocation);
		else if (allocClass == InstantiationPackage.eINSTANCE.getInitStringAllocation())
			return allocate((InitStringAllocation) allocation);
		else if (allocClass == InstantiationPackage.eINSTANCE.getImplicitAllocation())
			return allocate((ImplicitAllocation) allocation);
		else
			throw new AllocationException(new IllegalArgumentException(MessageFormat.format(JavaMessages.BasicAllocationProcesser_InvalidAllocationClass_EXC_, new Object[]{allocClass.toString()}))); 
	}
	
	/**
	 * Allocate from a parse tree allocation.
	 * @param allocation
	 * @return the bean proxy from allocation execution.
	 * @throws AllocationException
	 * 
	 * @since 1.0.0
	 */
	protected IBeanProxy allocate(ParseTreeAllocation allocation) throws AllocationException {
		return BasicAllocationProcesser.instantiateWithExpression(allocation.getExpression(), domain);
	}
	
	/**
	 * Allocate from a parse tree allocation and an expression.
	 * @param allocation
	 * @param expression
	 * @return
	 * @throws AllocationException
	 * 
	 * @since 1.1.0
	 */
	protected ExpressionProxy allocate(ParseTreeAllocation allocation, IExpression expression) throws AllocationException {
		ParseAllocation allocator = new ParseAllocation(domain.getThisType());
		try {
			return allocator.getProxy(allocation.getExpression(), expression);
		} catch (ProcessingException e) {
			throw new AllocationException(e.getCause());
		} catch (RuntimeException e) {
			throw new AllocationException(e);
		}
	}
	
	/**
	 * Allocate with an initstring and an expression.
	 * @param initString
	 * @param expression
	 * @return
	 * @throws AllocationException
	 * 
	 * @since 1.1.0
	 */
	protected ExpressionProxy allocate(InitStringAllocation initString, IExpression expression) throws AllocationException {
		// The container of the allocation is the IJavaInstance being instantiated.
		String qualifiedClassName = ((IJavaInstance) initString.eContainer()).getJavaType().getQualifiedNameForReflection();
		String initializationString = initString.getInitString();
		IProxyBeanType targetClass = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, qualifiedClassName);
		if (targetClass == null || (targetClass.isBeanProxy() && ((IBeanTypeProxy) targetClass).getInitializationError() != null)) {
			// The target class is invalid.
			Throwable exc = new ExceptionInInitializerError(targetClass != null ? ((IBeanTypeProxy) targetClass).getInitializationError() : MessageFormat.format(JavaMessages.Proxy_Class_has_Errors_ERROR_, new Object[] {JavaMessages.BasicAllocationProcesser_unknown_ERROR_})); 
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
				JavaVEPlugin.log("Could not instantiate " + (targetClass != null ? targetClass.getTypeName() : "unknown") + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				JavaVEPlugin.log(exc, Level.WARNING);
			}
			throw new AllocationException(exc);			
		}
		
		return instantiateWithString(initializationString, targetClass, expression);
	}
	
	/**
	 * Allocate for an implicit with an expression.
	 * @param implicit
	 * @param expression
	 * @return The allocation
	 * 
	 * @since 1.0.0
	 */
	protected IProxy allocate(ImplicitAllocation implicit, IExpression expression) {
		EObject source = implicit.getParent();
		IBeanProxyHost proxyhost = (IBeanProxyHost) EcoreUtil.getExistingAdapter(source, IBeanProxyHost.BEAN_PROXY_TYPE);
		return ((IInternalBeanProxyHost) proxyhost).getBeanPropertyProxyValue(implicit.getFeature(), expression, ForExpression.ROOTEXPRESSION);
	}
	
	/**
	 * Allocate for an init string.
	 * @param initString
	 * @return The allocation
	 * 
	 * @since 1.0.0
	 */
	protected IBeanProxy allocate(InitStringAllocation initString) throws AllocationException {
		// The container of the allocation is the IJavaInstance being instantiated.
		String qualifiedClassName = ((IJavaInstance) initString.eContainer()).getJavaType().getQualifiedNameForReflection(); 
		return BasicAllocationProcesser.instantiateWithString(initString.getInitString(), domain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(qualifiedClassName));
	}
	
	/**
	 * Allocate for an implicit
	 * @param implicit
	 * @return The allocation
	 * 
	 * @since 1.0.0
	 */
	protected IBeanProxy allocate(ImplicitAllocation implicit) {
		EObject source = implicit.getParent();
		IBeanProxyHost proxyhost = (IBeanProxyHost) EcoreUtil.getExistingAdapter(source, IBeanProxyHost.BEAN_PROXY_TYPE);
		return proxyhost.getBeanPropertyProxyValue(implicit.getFeature());		
	}
	
	/**
	 * Allocate for a parse tree with immediate evaluation.
	 * @param parseTree
	 * @param domain
	 * @return The allocation.
	 * 
	 * @since 1.0.0
	 */
	public static IBeanProxy instantiateWithExpression(PTExpression expression, IBeanProxyDomain domain) throws AllocationException {
		ParseAllocation allocator = new ParseAllocation(domain.getThisType());
		try {
			return allocator.getBeanProxy(expression, domain.getProxyFactoryRegistry());
		} catch (ProcessingException e) {
			throw new AllocationException(e.getCause());
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (NoExpressionValueException e) {
			throw new AllocationException(e);
		} catch (RuntimeException e) {
			throw new AllocationException(e);
		}
	}
	
	public IProxy allocate(PTExpression parseTreeExpression, IExpression expression) throws AllocationException {
		ParseAllocation allocator = new ParseAllocation(domain.getThisType());
		try {
			return allocator.getProxy(parseTreeExpression, expression);
		} catch (ProcessingException e) {
			throw new AllocationException(e.getCause());
		} catch (RuntimeException e) {
			throw new AllocationException(e);
		}

	}	

	/**
	 * A helper method. There are just times where you need to instantiate using an init string and it is not a IJavaObject.
	 * Passing in null for initializationString will result in default ctor being used.
	 * 
	 * @param initializationString - <code>null</code> means use default ctor, otherwise use initialization string.
	 * @param targetClass
	 * @return created proxy
	 * @throws AllocationException
	 */
	public static IBeanProxy instantiateWithString(
		String initializationString,
		IBeanTypeProxy targetClass)
		throws AllocationException {
		if (targetClass == null || targetClass.getInitializationError() != null) {
			// The target class is invalid.
			Throwable exc = new ExceptionInInitializerError(targetClass != null ? targetClass.getInitializationError() : MessageFormat.format(JavaMessages.Proxy_Class_has_Errors_ERROR_, new Object[] {JavaMessages.BasicAllocationProcesser_unknown_ERROR_})); 
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
				JavaVEPlugin.log("Could not instantiate " + (targetClass != null ? targetClass.getTypeName() : "unknown") + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				JavaVEPlugin.log(exc, Level.WARNING);
			}
			throw new AllocationException(exc);			
		}
		
		try { 					
			return initializationString != null ? targetClass.newInstance(initializationString) : targetClass.newInstance();
		} catch ( ThrowableProxy exc ) {
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
				JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
				JavaVEPlugin.log(exc, Level.WARNING);
			}
			throw new AllocationException(exc);
		} catch (InstantiationException exc) {
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
				JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
				JavaVEPlugin.log(exc, Level.WARNING);
			}
			throw new AllocationException(exc);
		} catch (ClassCastException exc){
			if (JavaVEPlugin.isLoggingLevel(Level.WARNING)) {
				JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$				
				JavaVEPlugin.log(exc, Level.WARNING);
			}
			throw new AllocationException(exc);			
		}
	}
	
	/**
	 * Instantiate with an initstring using the expression. 
	 * 
	 * @param initString the initstring or <code>null</code> if should use default ctor.
	 * @param beanType the type to create. It must not be a primitive type. Those are handled differently.
	 * @param expression the expression to use. The expression will be valid after this call if it was valid upon entry.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static ExpressionProxy instantiateWithString(String initString, IProxyBeanType beanType, IExpression expression) {
		if (initString == null) {
			// Use default ctor.
			ExpressionProxy result = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, beanType, 0);
			return result;
		} else {
			// This more tricky. Need to use the init string parser through the expression.
			ExpressionProxy result = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			((Expression) expression).createNewInstance(ForExpression.ASSIGNMENT_RIGHT, initString, beanType);
			return result;
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IAllocationProcesser#allocate(org.eclipse.jem.internal.instantiation.JavaAllocation, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	public IProxy allocate(JavaAllocation allocation, IExpression expression) throws AllocationException {
		int mark = expression.mark();
		try {
			EClass allocClass = allocation.eClass();
			// We are using explicit tests here for type of allocation so that these can be overridden by
			// others. If we used instanceof, then the overrides that are subclasses may be grabbed here
			// instead of where intended.
			if (allocClass == InstantiationPackage.eINSTANCE.getParseTreeAllocation())
				return allocate((ParseTreeAllocation) allocation, expression);
			else if (allocClass == InstantiationPackage.eINSTANCE.getInitStringAllocation())
				return allocate((InitStringAllocation) allocation, expression);
			else if (allocClass == InstantiationPackage.eINSTANCE.getImplicitAllocation())
				return allocate((ImplicitAllocation) allocation, expression);
			else
				throw new AllocationException(new IllegalArgumentException(MessageFormat.format(JavaMessages.BasicAllocationProcesser_InvalidAllocationClass_EXC_, new Object[] { allocClass.toString()}))); //$NON-NLS-1$
		} finally {
			expression.endMark(mark);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IAllocationProcesser#setBeanProxyDomain(org.eclipse.ve.internal.java.core.IBeanProxyDomain)
	 */
	public void setBeanProxyDomain(IBeanProxyDomain domain) {
		this.domain = domain;
	}

}
