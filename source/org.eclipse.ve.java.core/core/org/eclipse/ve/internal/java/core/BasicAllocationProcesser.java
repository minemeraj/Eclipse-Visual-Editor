/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: BasicAllocationProcesser.java,v $
 *  $Revision: 1.7 $  $Date: 2004-05-19 23:04:07 $ 
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
import org.eclipse.jem.internal.proxy.initParser.tree.IExpressionConstants.NoExpressionValueException;

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
			try {
				IBeanProxy reference = BeanProxyUtilities.getBeanProxy(node.getObject());
				getExpression().createProxyExpression(getNextExpression(), reference);
			} catch (ThrowableProxy e) {
				throw new ProcessingException(e);
			} catch (NoExpressionValueException e) {
				throw new ProcessingException(e);
			}				
			return false;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jem.internal.instantiation.ParseVisitor#visit(org.eclipse.jem.internal.instantiation.PTMethodInvocation)
		 */
		public boolean visit(PTMethodInvocation node) {
			// We are handling this.getClass() and getClass() special because we can possibly get the this.class. Normally "this...." anything 
			// cannot be handled because we don't have a "this" object to work with. So if getClass() or this.getClass() then we know we
			// can simply return the thisType we have already stored within us.
			if ("getClass".equals(node.getName()) && (node.getReceiver() == null || node.getReceiver() instanceof PTThisLiteral)) {
				if (thisType == null)
					throw new IllegalArgumentException("Type of \"this\" was not found, or it was invalid.");
				try {
					getExpression().createProxyExpression(getNextExpression(), thisType);
				} catch (IllegalStateException e) {
					throw new ProcessingException(e);
				} catch (ThrowableProxy e) {
					throw new ProcessingException(e);
				} catch (NoExpressionValueException e) {
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
			throw new IllegalArgumentException("Invalid allocation class: \""+allocClass.toString()+"\"");
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
	 * Allocate for a parse tree
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
			Throwable exc = new ExceptionInInitializerError(targetClass != null ? targetClass.getInitializationError() : MessageFormat.format(JavaMessages.getString("Proxy_Class_has_Errors_ERROR_"), new Object[] {"unknown"})); //$NON-NLS-1$
			JavaVEPlugin.log("Could not instantiate " + (targetClass != null ? targetClass.getTypeName() : "unknown") + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, Level.WARNING);
			throw new AllocationException(exc);			
		}
		
		try { 					
			return initializationString != null ? targetClass.newInstance(initializationString) : targetClass.newInstance();
		} catch ( ThrowableProxy exc ) {
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, Level.WARNING);
			throw new AllocationException(exc);
		} catch (InstantiationException exc) {
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, Level.WARNING);
			throw new AllocationException(exc);
		} catch (ClassCastException exc){				
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, Level.WARNING); //$NON-NLS-1$ //$NON-NLS-2$				
			JavaVEPlugin.log(exc, Level.WARNING);
			throw new AllocationException(exc);			
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IAllocationProcesser#setBeanProxyDomain(org.eclipse.ve.internal.java.core.IBeanProxyDomain)
	 */
	public void setBeanProxyDomain(IBeanProxyDomain domain) {
		this.domain = domain;
	}

}
