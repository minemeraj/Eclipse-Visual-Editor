/*******************************************************************************
 * Copyright (c) 2004 Red Hat, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Red Hat, Inc. - 1.0 implementation
 *******************************************************************************/

package org.eclipse.ve.internal.java.codegen.wizards;

import java.util.*;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import org.eclipse.ve.internal.cde.core.CDEPlugin;

/**
 * @author pmuldoon
 * 
 * Label provider for New Visual Class Wizard TreeViewer
 */

public class StyleTreeLabelProvider extends LabelProvider {

	private Map imageCache = new HashMap(11);

	public Image getImage(Object element) {
		if (element instanceof CategoryModel) {
			ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
			return images.getImage(ISharedImages.IMG_OBJ_FOLDER);
		} else {
			VisualElementModel visualElementModel = ((VisualElementModel) element);
			String imagename = visualElementModel.getIconFile();
			String imageId = null;
			String bundlename = null;
			if (imagename == null) {
				org.eclipse.jdt.ui.ISharedImages uiImages = JavaUI.getSharedImages();
				return uiImages.getImage(org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CLASS);
			} else {
				bundlename = visualElementModel.getContributorBundleName();
				imageId = bundlename + "/" + imagename;
			}
			Image image = (Image) imageCache.get(imageId);
			if (image == null) {
				image = CDEPlugin.getImageFromBundle(Platform.getBundle(bundlename), imagename);
				imageCache.put(imageId, image);
			}
			return image;
		}
	}

	/*
	 * @see ILabelProvider#getText(Object)
	 */
	public String getText(Object element) {
		if (element instanceof CategoryModel) {
			if (((CategoryModel) element).getName() == null) {
				return "Style Name Error";
			} else {
				return ((CategoryModel) element).getName();
			}
		} else if (element instanceof VisualElementModel) {
			if (((VisualElementModel) element).getName() == null) {
				return "Element Name Error";
			} else
				return ((VisualElementModel) element).getName();
		}

		return null;
	}

	public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}

	}
}