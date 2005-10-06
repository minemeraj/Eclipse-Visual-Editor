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
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: JFileChooserBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JFileChooserBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle JFileChooserMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jfilechooser");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JFileChooser.class;
}

public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.Desc") //$NON-NLS-1$
				}
			);
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/dialog32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/dialog16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}

/**
 * Gets the action event set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.actionPerformed.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.actionPerformed.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("ActionEvent", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.actionPerformed.ActionEvent.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.actionPerformed.ActionEvent.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.event.ActionEvent.class }
			),
	};
	aDescriptor = super.createEventSetDescriptor(getBeanClass(),
		"action", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.EventSetDesc.action.name"), //$NON-NLS-1$
			SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.EventSetDesc.action.Desc"), //$NON-NLS-1$
			INDEFAULTEVENTSET, Boolean.TRUE,
		},
		aDescriptorList, java.awt.event.ActionListener.class,
		"addActionListener", "removeActionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}

/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		EventSetDescriptor aDescriptorList[] = {
			actionEventSetDescriptor(),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

/**
 * @return an icon of the specified kind for JButton
 */
public java.awt.Image getIcon(int kind) {
	// TODO: Set to valid icon files
	if (kind == ICON_COLOR_32x32)
		return loadImage("dialog32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16)
		return loadImage("dialog16.gif"); //$NON-NLS-1$
	return super.getIcon(kind);
}

/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		// TODO: Set OBSCURE, EXPERT to appropriate values
		MethodDescriptor aDescriptorList[] = {
			// accept
			super.createMethodDescriptor(getBeanClass(),
				"accept",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.accept.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.accept.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.accept.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.accept.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// addActionListener
			super.createMethodDescriptor(getBeanClass(),
				"addActionListener",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.addActionListener.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.addActionListener.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("ActionListener", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.addActionListener.ActionListener.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.addActionListener.ActionListener.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.event.ActionListener.class }
			),
			// addChoosableFileFilter
			super.createMethodDescriptor(getBeanClass(),
				"addChoosableFileFilter",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.addChoosableFileFilter.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.addChoosableFileFilter.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("FileFilter", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.addChoosableFileFilter.FileFilter.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.addChoosableFileFilter.FileFilter.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { javax.swing.filechooser.FileFilter.class }
			),
			// approveSelection
			super.createMethodDescriptor(getBeanClass(),
				"approveSelection",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.approveSelection.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.approveSelection.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// cancelSelection
			super.createMethodDescriptor(getBeanClass(),
				"cancelSelection",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.cancelSelection.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.cancelSelection.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// changeToParentDirectory
			super.createMethodDescriptor(getBeanClass(),
				"changeToParentDirectory",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.changeToParentDirectory.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.changeToParentDirectory.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// ensureFileIsVisible
			super.createMethodDescriptor(getBeanClass(),
				"ensureFileIsVisible",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.ensureFileIsVisible.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.ensureFileIsVisible.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.ensureFileIsVisible.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.ensureFileIsVisible.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// getAcceptAllFileFilter
			super.createMethodDescriptor(getBeanClass(),
				"getAcceptAllFileFilter",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getAcceptAllFileFilter.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getAcceptAllFileFilter.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getAccessibleContext
			super.createMethodDescriptor(getBeanClass(),
				"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getAccessibleContext.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getAccessibleContext.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getAccessory
			super.createMethodDescriptor(getBeanClass(),
				"getAccessory",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getAccessory.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getAccessory.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getApproveButtonMnemonic
			super.createMethodDescriptor(getBeanClass(),
				"getApproveButtonMnemonic",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getApproveButtonMnemonic.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getApproveButtonMnemonic.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getApproveButtonText
			super.createMethodDescriptor(getBeanClass(),
				"getApproveButtonText",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getApproveButtonText.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getApproveButtonText.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getApproveButtonToolTipText
			super.createMethodDescriptor(getBeanClass(),
				"getApproveButtonToolTipText",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getApproveButtonToolTipText.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getApproveButtonToolTipText.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getChoosableFileFilters
			super.createMethodDescriptor(getBeanClass(),
				"getChoosableFileFilters",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getChoosableFileFilters.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getChoosableFileFilters.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getControlButtonsAreShown
			super.createMethodDescriptor(getBeanClass(),
				"getControlButtonsAreShown",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getControlButtonsAreShown.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getControlButtonsAreShown.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getCurrentDirectory
			super.createMethodDescriptor(getBeanClass(),
				"getCurrentDirectory",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getCurrentDirectory.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getCurrentDirectory.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getDescription
			super.createMethodDescriptor(getBeanClass(),
				"getDescription",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getDescription.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getDescription.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.getDescription.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.getDescription.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// getDialogTitle
			super.createMethodDescriptor(getBeanClass(),
				"getDialogTitle",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getDialogTitle.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getDialogTitle.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getDialogType
			super.createMethodDescriptor(getBeanClass(),
				"getDialogType",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getDialogType.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getDialogType.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getFileFilter
			super.createMethodDescriptor(getBeanClass(),
				"getFileFilter",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileFilter.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileFilter.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getFileSelectionMode
			super.createMethodDescriptor(getBeanClass(),
				"getFileSelectionMode",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileSelectionMode.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileSelectionMode.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getFileSystemView
			super.createMethodDescriptor(getBeanClass(),
				"getFileSystemView",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileSystemView.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileSystemView.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getFileView
			super.createMethodDescriptor(getBeanClass(),
				"getFileView",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileView.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getFileView.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getIcon
			super.createMethodDescriptor(getBeanClass(),
				"getIcon",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getIcon.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getIcon.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.getIcon.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.getIcon.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// getName
			super.createMethodDescriptor(getBeanClass(),
				"getName",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getName.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getName.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.getName.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.getName.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// getSelectedFile
			super.createMethodDescriptor(getBeanClass(),
				"getSelectedFile",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getSelectedFile.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getSelectedFile.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getSelectedFiles
			super.createMethodDescriptor(getBeanClass(),
				"getSelectedFiles",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getSelectedFiles.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getSelectedFiles.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getTypeDescription
			super.createMethodDescriptor(getBeanClass(),
				"getTypeDescription",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getTypeDescription.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getTypeDescription.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.getTypeDescription.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.getTypeDescription.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// getUI
			super.createMethodDescriptor(getBeanClass(),
				"getUI",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getUI.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getUI.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getUIClassID
			super.createMethodDescriptor(getBeanClass(),
				"getUIClassID",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.getUIClassID.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.getUIClassID.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// isAcceptAllFileFilterUsed
			super.createMethodDescriptor(getBeanClass(),
				"isAcceptAllFileFilterUsed",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.isAcceptAllFileFilterUsed.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.isAcceptAllFileFilterUsed.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// isDirectorySelectionEnabled
			super.createMethodDescriptor(getBeanClass(),
				"isDirectorySelectionEnabled",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.isDirectorySelectionEnabled.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.isDirectorySelectionEnabled.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// isFileHidingEnabled
			super.createMethodDescriptor(getBeanClass(),
				"isFileHidingEnabled",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.isFileHidingEnabled.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.isFileHidingEnabled.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// isFileSelectionEnabled
			super.createMethodDescriptor(getBeanClass(),
				"isFileSelectionEnabled",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.isFileSelectionEnabled.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.isFileSelectionEnabled.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// isMultiSelectionEnabled
			super.createMethodDescriptor(getBeanClass(),
				"isMultiSelectionEnabled",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.isMultiSelectionEnabled.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.isMultiSelectionEnabled.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// isTraversable
			super.createMethodDescriptor(getBeanClass(),
				"isTraversable",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.isTraversable.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.isTraversable.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.isTraversable.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.isTraversable.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// removeActionListener
			super.createMethodDescriptor(getBeanClass(),
				"removeActionListener",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.removeActionListener.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.removeActionListener.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("ActionListener", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.removeActionListener.ActionListener.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.removeActionListener.ActionListener.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.event.ActionListener.class }
			),
			// removeChoosableFileFilter
			super.createMethodDescriptor(getBeanClass(),
				"removeChoosableFileFilter",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.removeChoosableFileFilter.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.removeChoosableFileFilter.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("FileFilter", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.removeChoosableFileFilter.FileFilter.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.removeChoosableFileFilter.FileFilter.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { javax.swing.filechooser.FileFilter.class }
			),
			// rescanCurrentDirectory
			super.createMethodDescriptor(getBeanClass(),
				"rescanCurrentDirectory",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.rescanCurrentDirectory.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.rescanCurrentDirectory.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// resetChoosableFileFilters
			super.createMethodDescriptor(getBeanClass(),
				"resetChoosableFileFilters",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.resetChoosableFileFilters.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.resetChoosableFileFilters.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// setAcceptAllFileFilterUsed
			super.createMethodDescriptor(getBeanClass(),
				"setAcceptAllFileFilterUsed",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setAcceptAllFileFilterUsed.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setAcceptAllFileFilterUsed.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("boolean", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setAcceptAllFileFilterUsed.boolean.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setAcceptAllFileFilterUsed.boolean.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { boolean.class }
			),
			// setAccessory
			super.createMethodDescriptor(getBeanClass(),
				"setAccessory",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setAccessory.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setAccessory.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("JComponent", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setAccessory.JComponent.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setAccessory.JComponent.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { javax.swing.JComponent.class }
			),
			// setApproveButtonMnemonic
			super.createMethodDescriptor(getBeanClass(),
				"setApproveButtonMnemonic",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setApproveButtonMnemonic.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setApproveButtonMnemonic.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setApproveButtonMnemonic.int.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setApproveButtonMnemonic.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setApproveButtonText
			super.createMethodDescriptor(getBeanClass(),
				"setApproveButtonText",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setApproveButtonText.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setApproveButtonText.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setApproveButtonText.String.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setApproveButtonText.String.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.lang.String.class }
			),
			// setApproveButtonToolTipText
			super.createMethodDescriptor(getBeanClass(),
				"setApproveButtonToolTipText",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setApproveButtonToolTipText.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setApproveButtonToolTipText.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setApproveButtonToolTipText.String.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setApproveButtonToolTipText.String.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.lang.String.class }
			),
			// setControlButtonsAreShown
			super.createMethodDescriptor(getBeanClass(),
				"setControlButtonsAreShown",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setControlButtonsAreShown.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setControlButtonsAreShown.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("boolean", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setControlButtonsAreShown.boolean.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setControlButtonsAreShown.boolean.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { boolean.class }
			),
			// setCurrentDirectory
			super.createMethodDescriptor(getBeanClass(),
				"setCurrentDirectory",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setCurrentDirectory.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setCurrentDirectory.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setCurrentDirectory.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setCurrentDirectory.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// setDialogTitle
			super.createMethodDescriptor(getBeanClass(),
				"setDialogTitle",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setDialogTitle.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setDialogTitle.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setDialogTitle.String.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setDialogTitle.String.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.lang.String.class }
			),
			// setDialogType
			super.createMethodDescriptor(getBeanClass(),
				"setDialogType",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setDialogType.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setDialogType.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setDialogType.int.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setDialogType.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setFileFilter
			super.createMethodDescriptor(getBeanClass(),
				"setFileFilter",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileFilter.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileFilter.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("FileFilter", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileFilter.FileFilter.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileFilter.FileFilter.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { javax.swing.filechooser.FileFilter.class }
			),
			// setFileHidingEnabled
			super.createMethodDescriptor(getBeanClass(),
				"setFileHidingEnabled",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileHidingEnabled.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileHidingEnabled.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("boolean", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileHidingEnabled.boolean.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileHidingEnabled.boolean.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { boolean.class }
			),
			// setFileSelectionMode
			super.createMethodDescriptor(getBeanClass(),
				"setFileSelectionMode",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileSelectionMode.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileSelectionMode.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileSelectionMode.int.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileSelectionMode.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setFileSystemView
			super.createMethodDescriptor(getBeanClass(),
				"setFileSystemView",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileSystemView.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileSystemView.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("FileSystemView", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileSystemView.FileSystemView.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileSystemView.FileSystemView.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { javax.swing.filechooser.FileSystemView.class }
			),
			// setFileView
			super.createMethodDescriptor(getBeanClass(),
				"setFileView",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileView.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setFileView.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("FileView", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileView.FileView.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setFileView.FileView.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { javax.swing.filechooser.FileView.class }
			),
			// setMultiSelectionEnabled
			super.createMethodDescriptor(getBeanClass(),
				"setMultiSelectionEnabled",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setMultiSelectionEnabled.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setMultiSelectionEnabled.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("boolean", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setMultiSelectionEnabled.boolean.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setMultiSelectionEnabled.boolean.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { boolean.class }
			),
			// setSelectedFile
			super.createMethodDescriptor(getBeanClass(),
				"setSelectedFile",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setSelectedFile.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setSelectedFile.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setSelectedFile.File.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setSelectedFile.File.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File.class }
			),
			// setSelectedFiles
			super.createMethodDescriptor(getBeanClass(),
				"setSelectedFiles",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.setSelectedFiles.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.setSelectedFiles.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("File[]", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.setSelectedFiles.File[].Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.setSelectedFiles.File[].Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.io.File[].class }
			),
			// showDialog
			super.createMethodDescriptor(getBeanClass(),
				"showDialog",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.showDialog.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.showDialog.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.showDialog.Component.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.showDialog.Component.Desc"), //$NON-NLS-1$
					}),
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.showDialog.String.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.showDialog.String.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class, java.lang.String.class }
			),
			// showOpenDialog
			super.createMethodDescriptor(getBeanClass(),
				"showOpenDialog",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.showOpenDialog.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.showOpenDialog.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.showOpenDialog.Component.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.showOpenDialog.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class }
			),
			// showSaveDialog
			super.createMethodDescriptor(getBeanClass(),
				"showSaveDialog",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.showSaveDialog.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.showSaveDialog.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.ParamDesc.showSaveDialog.Component.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.ParamDesc.showSaveDialog.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class }
			),
			// updateUI
			super.createMethodDescriptor(getBeanClass(),
				"updateUI",  //$NON-NLS-1$
				new Object[] {
					DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.MthdDesc.updateUI.Name"), //$NON-NLS-1$
					SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.MthdDesc.updateUI.Desc"), //$NON-NLS-1$
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		PropertyDescriptor aDescriptorList[] = {
			// UI
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.UI.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.UI.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
			// UIClassID
			super.createPropertyDescriptor(getBeanClass(),"UIClassID", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.UIClassID.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.UIClassID.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
			// acceptAllFileFilter
			super.createPropertyDescriptor(getBeanClass(),"acceptAllFileFilter", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.acceptAllFileFilter.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.acceptAllFileFilter.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// acceptAllFileFilterUsed
			super.createPropertyDescriptor(getBeanClass(),"acceptAllFileFilterUsed", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.acceptAllFileFilterUsed.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.acceptAllFileFilterUsed.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
			// accessibleContext
			super.createPropertyDescriptor(getBeanClass(),"accessibleContext", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.accessibleContext.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.accessibleContext.Desc"), //$NON-NLS-1$
			}
			),
			//TODO: potentially useful when visible, but leads to confusing UI
			// accessory
			super.createPropertyDescriptor(getBeanClass(),"accessory", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.accessory.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.accessory.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
			// approveButtonMnemonic
			super.createPropertyDescriptor(getBeanClass(),"approveButtonMnemonic", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.approveButtonMnemonic.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.approveButtonMnemonic.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE,
			}
			),
			// approveButtonText
			super.createPropertyDescriptor(getBeanClass(),"approveButtonText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.approveButtonText.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.approveButtonText.Desc"), //$NON-NLS-1$
			}
			),
			// approveButtonToolTipText
			super.createPropertyDescriptor(getBeanClass(),"approveButtonToolTipText", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.approveButtonToolTipText.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.approveButtonToolTipText.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// background - hide it
			super.createPropertyDescriptor(getBeanClass(),"background", new Object[] { //$NON-NLS-1$
			HIDDEN, Boolean.TRUE
				}
			),
			// choosableFileFilters
			super.createPropertyDescriptor(getBeanClass(),"choosableFileFilters", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.choosableFileFilters.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.choosableFileFilters.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
			// controlButtonsAreShown
			super.createPropertyDescriptor(getBeanClass(),"controlButtonsAreShown", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.controlButtonsAreShown.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.controlButtonsAreShown.Desc"), //$NON-NLS-1$
			}
			),
			// currentDirectory
			super.createPropertyDescriptor(getBeanClass(),"currentDirectory", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.currentDirectory.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.currentDirectory.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
			// dialogTitle
			super.createPropertyDescriptor(getBeanClass(),"dialogTitle", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.dialogTitle.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.dialogTitle.Desc"), //$NON-NLS-1$
			}
			),
			// dialogType
			super.createPropertyDescriptor(getBeanClass(),"dialogType", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.dialogType.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.dialogType.Desc"), //$NON-NLS-1$
				BOUND, Boolean.TRUE,
				IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
					JFileChooserMessages.getString("dialogType.OPEN_DIALOG"), new Integer(javax.swing.JFileChooser.OPEN_DIALOG), //$NON-NLS-1$
							"javax.swing.JFileChooser.OPEN_DIALOG", //$NON-NLS-1$
					JFileChooserMessages.getString("dialogType.SAVE_DIALOG"), new Integer(javax.swing.JFileChooser.SAVE_DIALOG), //$NON-NLS-1$
							"javax.swing.JFileChooser.SAVE_DIALOG", //$NON-NLS-1$
					JFileChooserMessages.getString("dialogType.CUSTOM_DIALOG"), new Integer(javax.swing.JFileChooser.CUSTOM_DIALOG), //$NON-NLS-1$
							"javax.swing.JFileChooser.CUSTOM_DIALOG", //$NON-NLS-1$
				}
			}
			),
			// directorySelectionEnabled
			super.createPropertyDescriptor(getBeanClass(),"directorySelectionEnabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.directorySelectionEnabled.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.directorySelectionEnabled.Desc"), //$NON-NLS-1$
			}
			),
			// fileFilter
			super.createPropertyDescriptor(getBeanClass(),"fileFilter", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.fileFilter.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.fileFilter.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// fileHidingEnabled
			super.createPropertyDescriptor(getBeanClass(),"fileHidingEnabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.fileHidingEnabled.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.fileHidingEnabled.Desc"), //$NON-NLS-1$
				BOUND, Boolean.TRUE,
			}
			),
			// fileSelectionEnabled
			super.createPropertyDescriptor(getBeanClass(),"fileSelectionEnabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.fileSelectionEnabled.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.fileSelectionEnabled.Desc"), //$NON-NLS-1$
			}
			),
			// fileSelectionMode
			super.createPropertyDescriptor(getBeanClass(),"fileSelectionMode", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.fileSelectionMode.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.fileSelectionMode.Desc"), //$NON-NLS-1$
				BOUND, Boolean.TRUE,
				IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
					JFileChooserMessages.getString("fileSelectionMode.FILES_ONLY"), new Integer(javax.swing.JFileChooser.OPEN_DIALOG), //$NON-NLS-1$
							"javax.swing.JFileChooser.OPEN_DIALOG", //$NON-NLS-1$
					JFileChooserMessages.getString("fileSelectionMode.DIRECTORIES_ONLY"), new Integer(javax.swing.JFileChooser.SAVE_DIALOG), //$NON-NLS-1$
							"javax.swing.JFileChooser.DIRECTORIES_ONLY", //$NON-NLS-1$
					JFileChooserMessages.getString("fileSelectionMode.FILES_AND_DIRECTORIES"), new Integer(javax.swing.JFileChooser.FILES_AND_DIRECTORIES), //$NON-NLS-1$
							"javax.swing.JFileChooser.FILES_AND_DIRECTORIES", //$NON-NLS-1$
				}
			}
			),
			// fileSystemView
			super.createPropertyDescriptor(getBeanClass(),"fileSystemView", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.fileSystemView.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.fileSystemView.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// fileView
			super.createPropertyDescriptor(getBeanClass(),"fileView", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.fileView.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.fileView.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
			}
			),
			// font - hide it
			super.createPropertyDescriptor(getBeanClass(),"font", new Object[] { //$NON-NLS-1$
			HIDDEN, Boolean.TRUE
				}
			),
			// foreground - hide it
			super.createPropertyDescriptor(getBeanClass(),"foreground", new Object[] { //$NON-NLS-1$
			HIDDEN, Boolean.TRUE
				}
			),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
			HIDDEN, Boolean.TRUE
				}
			),
			// multiSelectionEnabled
			super.createPropertyDescriptor(getBeanClass(),"multiSelectionEnabled", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.multiSelectionEnabled.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.multiSelectionEnabled.Desc"), //$NON-NLS-1$
			}
			),
			// selectedFile
			super.createPropertyDescriptor(getBeanClass(),"selectedFile", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.selectedFile.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.selectedFile.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
			// selectedFiles
			super.createPropertyDescriptor(getBeanClass(),"selectedFiles", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, JFileChooserMessages.getString("JFileChooser.PropDesc.selectedFiles.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, JFileChooserMessages.getString("JFileChooser.PropDesc.selectedFiles.Desc"), //$NON-NLS-1$
				HIDDEN, Boolean.TRUE,
			}
			),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}

}
