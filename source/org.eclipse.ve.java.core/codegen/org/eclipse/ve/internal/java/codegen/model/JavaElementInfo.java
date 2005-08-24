/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JavaElementInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:48 $ 
 */
package org.eclipse.ve.internal.java.codegen.model;

import org.eclipse.jdt.core.*;
 
/**
 * 
 * @since 1.0.0
 */
public class JavaElementInfo {
	String  handle;
	int	    offset;
	int     length;
	String  name;
	String  content;
	
	public JavaElementInfo(IJavaElement element) {
		handle = element.getHandleIdentifier();
		if (element instanceof ISourceReference) {
			ISourceReference sr = (ISourceReference) element;
			try {
				offset=sr.getSourceRange().getOffset();
			    length=sr.getSourceRange().getLength();
			    content = sr.getSource();
			}
		    catch (JavaModelException e) {
		    	offset=-1;
				length=-1;
				content=null;		    	
		    }			
		}
		else {
			offset=-1;
			length=-1;
			content=null;
		}
		name = element.getElementName();
	}
	
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return Returns the handle.
	 */
	public String getHandle() {
		return handle;
	}
	/**
	 * @return Returns the length.
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}
	
	public ISourceRange getSourceRange() {
		return new ISourceRange() {
			public int getLength() {
				return length;
			}
			public int getOffset() {
				return offset;
			}
		};
	}
}
