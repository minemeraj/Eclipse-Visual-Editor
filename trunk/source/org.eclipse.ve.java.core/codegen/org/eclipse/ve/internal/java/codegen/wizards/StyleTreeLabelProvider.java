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

package org.eclipse.ve.internal.java.codegen.wizards;

import java.util.*;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author pmuldoon
 * 
 * Label provider for New Visual Class Wizard TreeViewer
 */

public class StyleTreeLabelProvider extends LabelProvider {
    private Map imageCache = new HashMap(11);

    public Image getImage(Object element) {
        ImageDescriptor descriptor = null;
        ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
        if (element instanceof CategoryModel) {
            descriptor = images.getImageDescriptor("IMG_OBJ_FOLDER");

            Image image = (Image) imageCache.get(descriptor);
            if (image == null) {
                image = descriptor.createImage();
                imageCache.put(descriptor, image);
            }
            return image;
        }
        return null;
    }
    
    /*
     * @see ILabelProvider#getText(Object)
     */
    public String getText(Object element) {
        if (element instanceof CategoryModel) {
            if(((CategoryModel)element).getName() == null) {
                return "Style Name Error";
            } else {
                return ((CategoryModel)element).getName();
            }
        } else if (element instanceof VisualElementModel) {
            if (((VisualElementModel)element).getName() == null) {
                return "Element Name Error";
            } else                
                return ((VisualElementModel)element).getName();
        } 
        
        return null;
    }
    
    public void dispose() {
        for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
            ((Image) i.next()).dispose();
        }
   
    }
}
