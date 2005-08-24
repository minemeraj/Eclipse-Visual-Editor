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
 *  $RCSfile: BorderLayoutBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class BorderLayoutBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle BorderLayoutMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.borderlayout");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.BorderLayout.class;
}

public java.beans.BeanDescriptor getBeanDescriptor() {
	return new java.beans.BeanDescriptor(java.awt.BorderLayout.class);
}

/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return( new java.beans.EventSetDescriptor[0]);
}

/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		// TODO: Re-enable the display names, short descriptions when we can drop to NLS
		MethodDescriptor aDescriptorList[] = {
			// addLayoutComponent
			super.createMethodDescriptor(getBeanClass(),
				"addLayoutComponent",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.addLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.addLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.String.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.String.Desc"), //$NON-NLS-1$
					}),
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.lang.String.class, java.awt.Component.class }
			),
			// addLayoutComponent
			super.createMethodDescriptor(getBeanClass(),
				"addLayoutComponent",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.addLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.addLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
					createParameterDescriptor("Object", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.Object.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.addLayoutComponent.Object.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class, java.lang.Object.class }
			),
			// getHgap
			super.createMethodDescriptor(getBeanClass(),
				"getHgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getHgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getHgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getLayoutAlignmentX
			super.createMethodDescriptor(getBeanClass(),
				"getLayoutAlignmentX",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getLayoutAlignmentX.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getLayoutAlignmentX.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.getLayoutAlignmentX.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.getLayoutAlignmentX.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// getLayoutAlignmentY
			super.createMethodDescriptor(getBeanClass(),
				"getLayoutAlignmentY",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getLayoutAlignmentY.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getLayoutAlignmentY.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.getLayoutAlignmentY.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.getLayoutAlignmentY.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// getVgap
			super.createMethodDescriptor(getBeanClass(),
				"getVgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getVgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.getVgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// invalidateLayout
			super.createMethodDescriptor(getBeanClass(),
				"invalidateLayout",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.invalidateLayout.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.invalidateLayout.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.invalidateLayout.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.invalidateLayout.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// layoutContainer
			super.createMethodDescriptor(getBeanClass(),
				"layoutContainer",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.layoutContainer.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.layoutContainer.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.layoutContainer.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.layoutContainer.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// maximumLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"maximumLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.maximumLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.maximumLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.maximumLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.maximumLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// minimumLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"minimumLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.minimumLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.minimumLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.minimumLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.minimumLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// preferredLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"preferredLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.preferredLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.preferredLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.preferredLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.preferredLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// removeLayoutComponent
			super.createMethodDescriptor(getBeanClass(),
				"removeLayoutComponent",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.removeLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.removeLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.removeLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.removeLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class }
			),
			// setHgap
			super.createMethodDescriptor(getBeanClass(),
				"setHgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.setHgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.setHgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.setHgap.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.setHgap.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setVgap
			super.createMethodDescriptor(getBeanClass(),
				"setVgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.MthdDesc.setVgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.MthdDesc.setVgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.ParamDesc.setVgap.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.ParamDesc.setVgap.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
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
			// hgap
			super.createPropertyDescriptor(getBeanClass(),"hgap", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.PropDesc.hgap.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.PropDesc.hgap.Desc"), //$NON-NLS-1$
				BOUND, Boolean.FALSE,
				EXPERT, Boolean.FALSE,
				HIDDEN, Boolean.FALSE,
			}
			),
			// vgap
			super.createPropertyDescriptor(getBeanClass(),"vgap", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, BorderLayoutMessages.getString("BorderLayout.PropDesc.vgap.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, BorderLayoutMessages.getString("BorderLayout.PropDesc.vgap.Desc"), //$NON-NLS-1$
				BOUND, Boolean.FALSE,
				EXPERT, Boolean.FALSE,
				HIDDEN, Boolean.FALSE,
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
