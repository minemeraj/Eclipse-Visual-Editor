package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ImageProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-06 20:19:47 $ 
 */

import org.eclipse.core.resources.*;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.BeanProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.jem.internal.proxy.core.*;

public class ImageProxyAdapter extends BeanProxyAdapter {
	EditDomain fEditDomain;
	/**
	 * Constructor for ImageProxyAdapter.
	 * @param domain
	 */
	public ImageProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		fEditDomain = domain.getEditDomain();
	}

	/**
	 * The initialization string used to construct an ImageIcon can be two possibilities:
	 *  - within the scope of the project... e.g. getClass().getResource("/someFileName")
	 *  - or an image file within the current file system... which is a hard-code string. e.g. "C:\myImages\somegraphic.gif"
	 * The first one must be resolved here because the remote vm doesn't know how to resolve it.
	 * We do that by creating an initialization string with the full path name.
	 */
	protected IBeanProxy instantiateWithString(IBeanTypeProxy targetClass, String initString)
		throws ThrowableProxy, InstantiationException {
		int index = initString.lastIndexOf("getClass().getResource"); //$NON-NLS-1$
		if (index != -1) {
			int firstQuoteIndex = initString.indexOf('"', index); //$NON-NLS-1$
			if (firstQuoteIndex != -1) {
				int lastQuoteIndex = initString.indexOf('"', firstQuoteIndex + 1);
				if (lastQuoteIndex != -1) {
					String pathString = initString.substring(firstQuoteIndex + 1, lastQuoteIndex).trim();
					IFile file = null;
					try {
						IProject project = ((FileEditorInput) (fEditDomain).getEditorPart().getEditorInput()).getFile().getProject();
						IJavaProject jproject = JavaCore.create(project);
						IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
						// Need to look in the roots to find first one.
						IPackageFragmentRoot[] roots = jproject.getAllPackageFragmentRoots();
						for (int i = 0; i < roots.length; i++) {
							IResource res = workspaceRoot.findMember(roots[i].getPath().append(pathString));
							if (res instanceof IFile) {
								file = (IFile) res;
								break;
							}
						}
					} catch (JavaModelException e) {
					}

					if (file != null) {
						String fullPath = file.getLocation().toString();
						// Prepend the beginning part (typically java.awt.Toolkit.getDefaultToolkit().getImage(" to the new
						// absolute path, then add in what followed the getClass().getResource(...)  That's why start with lastQuoteIndex+2, to 
						// skip over the final quote and closing parenthesis of the getResource().
						initString = initString.substring(0, index) + BeanUtilities.createStringInitString(fullPath) + initString.substring(lastQuoteIndex+2);
					}
				}
			}
		}
		// TODO Need to fix this compile error... for now just return null
		return null;
//		return super.instantiateWithString(targetClass, initString);
	}
}