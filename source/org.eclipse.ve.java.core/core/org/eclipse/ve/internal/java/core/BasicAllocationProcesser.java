package org.eclipse.ve.internal.java.core;
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
 *  $Revision: 1.2 $  $Date: 2004-02-06 20:46:46 $ 
 */
 
import java.text.MessageFormat;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.IExpressionConstants.NoExpressionValueException;
import org.eclipse.jem.workbench.utility.ParseTreeAllocationInstantiationVisitor;
import org.eclipse.jem.workbench.utility.ParseTreeAllocationInstantiationVisitor.ProcessingException;

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

}
	
	protected IBeanProxyDomain domain; 

	public BasicAllocationProcesser() {
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.IAllocationProcesser#allocate(org.eclipse.jem.internal.instantiation.JavaAllocation)
	 */
	public IBeanProxy allocate(JavaAllocation allocation) throws AllocationException {
		EClass allocClass = allocation.eClass();
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
	 * @return The allocation
	 * 
	 * @since 1.0.0
	 */
	protected IBeanProxy allocate(ParseTreeAllocation parseTree) throws AllocationException {
		ParseAllocation allocator = new ParseAllocation();
		try {
			return allocator.getBeanProxy(parseTree.getExpression(), domain.getProxyFactoryRegistry());
		} catch (ProcessingException e) {
			throw new AllocationException(e.getCause());
		} catch (ThrowableProxy e) {
			throw new AllocationException(e);
		} catch (NoExpressionValueException e) {
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
			JavaVEPlugin.log("Could not instantiate " + (targetClass != null ? targetClass.getTypeName() : "unknown") + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			throw new AllocationException(exc);			
		}
		
		try { 					
			return initializationString != null ? targetClass.newInstance(initializationString) : targetClass.newInstance();
		} catch ( ThrowableProxy exc ) {
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			throw new AllocationException(exc);
		} catch (InstantiationException exc) {
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
			throw new AllocationException(exc);
		} catch (ClassCastException exc){				
			JavaVEPlugin.log("Could not instantiate " + targetClass.getTypeName() + " with initialization string=" + initializationString, MsgLogger.LOG_WARNING); //$NON-NLS-1$ //$NON-NLS-2$				
			JavaVEPlugin.log(exc, MsgLogger.LOG_WARNING);
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
