/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: MethodParser.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.logging.Level;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author gmendel
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MethodParser {
	
	String          fSeperator ;
	
	IMethod  		fMethod = null ;
	int			fSrcOffset = -1 ;
	String   		fSource = null ;
	CompilationUnit fCU = null ;
	
	static final String foo = "Foo" ; //$NON-NLS-1$
	static final String classTemplatePre = "public class "+foo+" {\n" ; //$NON-NLS-1$ //$NON-NLS-2$
	static final String classTemplatePost = "\n}" ; //$NON-NLS-1$
	

	/**
	 * Constructor for MethodParser.
	 */
	public MethodParser(IMethod method, String seperator) {
		super();
		fMethod = method ;
		fSeperator = seperator ;
	}
	
	CompilationUnit getCU() {
		if (fCU != null) return fCU ;
		try {
			StringBuffer sb = new StringBuffer (classTemplatePre) ;
			
			sb.replace(classTemplatePre.indexOf(foo),
			           classTemplatePre.indexOf(foo)+foo.length(),
			           fMethod.getDeclaringType().getElementName() 
			           ) ;		
			fSrcOffset = sb.length() ;	
			fSource = sb.toString()+fMethod.getSource()+classTemplatePost ;
		}
		catch (JavaModelException e) {}
		
		if (fSource != null) {
	   		ASTParser parser = ASTParser.newParser(AST.JLS2);
			parser.setSource(fSource.toCharArray());
			fCU = (CompilationUnit) parser.createAST(null);
		}
		return fCU ;		
	}
	
	/**
	 * Determine if methodName is called from fMethod.
	 * If not,it adds a call to it after the super() constructor
	 */
	public boolean addMethodCallIfNeeded (final String methodName) {
		
		final boolean result[] = {false} ;
		final ASTNode SuperBlock[] = { null, null };
		
		getCU() ;
		
		// Parse to find out where is the super() block is and
		// find out if methodName is there
		fCU.getRoot().accept(new ASTVisitor() {
			   public boolean visit(Block node) {
			   	  if (SuperBlock[1] == null) SuperBlock[1] = node ;
			   	  return true ;
			   }
			   public boolean visit(SuperConstructorInvocation node) {
			   	 if (node.getStartPosition()>0)
			   	    SuperBlock[0] = node ;			  
			   	 return true ;
			   }
			   public boolean visit(MethodInvocation node) {
			   	if (methodName.equals(node.getName().getIdentifier()))
			   	     result[0] = true ;
			   	return false ;
			   }
		             }) ;
		             
		if (!result[0]) {  // methodName was not found, need to add it.
			int insertOffset = -1; 
			if (SuperBlock[0] != null)   // We found a super() call
				insertOffset= SuperBlock[0].getStartPosition()+SuperBlock[0].getLength() ; // for LF
		    else if (SuperBlock[1] != null)
		        insertOffset = SuperBlock[1].getStartPosition()+SuperBlock[1].getLength() ;
		        
		    if (insertOffset>=0) {
		    	// TODO  Need to use current filler not just a \t
				try {
					fMethod.getCompilationUnit().getBuffer().replace(
					  fMethod.getSourceRange().getOffset()+insertOffset-fSrcOffset,
					  0,
					  fSeperator+"\t\t"+methodName+"();" //$NON-NLS-1$ //$NON-NLS-2$
					) ;
				}
				catch (JavaModelException e) {}
				fCU = null ;
		    	return true ;
		    }
		    else 
		      JavaVEPlugin.log("MethodParser.addMethodCallIfNedded(): Could not insert", Level.FINE) ;		       //$NON-NLS-1$
		}
		return false ;
		
	}

}
