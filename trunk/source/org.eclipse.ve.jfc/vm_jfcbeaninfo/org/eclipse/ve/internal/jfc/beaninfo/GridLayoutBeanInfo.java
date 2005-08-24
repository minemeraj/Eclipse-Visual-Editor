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
 *  $RCSfile: GridLayoutBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class GridLayoutBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle GridLayoutMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.gridlayout");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.GridLayout.class;
}

public java.beans.BeanDescriptor getBeanDescriptor() {
	return new java.beans.BeanDescriptor(java.awt.GridLayout.class);
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
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.addLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.addLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.addLayoutComponent.String.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.addLayoutComponent.String.Desc"), //$NON-NLS-1$
					}),
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.addLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.addLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.lang.String.class, java.awt.Component.class }
			),
			// getColumns
			super.createMethodDescriptor(getBeanClass(),
				"getColumns",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.getColumns.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.getColumns.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getHgap
			super.createMethodDescriptor(getBeanClass(),
				"getHgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.getHgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.getHgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getRows
			super.createMethodDescriptor(getBeanClass(),
				"getRows",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.getRows.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.getRows.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// getVgap
			super.createMethodDescriptor(getBeanClass(),
				"getVgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.getVgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.getVgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
				},
				new Class[] {  }
			),
			// layoutContainer
			super.createMethodDescriptor(getBeanClass(),
				"layoutContainer",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.layoutContainer.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.layoutContainer.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.layoutContainer.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.layoutContainer.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// minimumLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"minimumLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.minimumLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.minimumLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.minimumLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.minimumLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// preferredLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"preferredLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.preferredLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.preferredLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.preferredLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.preferredLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// removeLayoutComponent
			super.createMethodDescriptor(getBeanClass(),
				"removeLayoutComponent",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.removeLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.removeLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.removeLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.removeLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class }
			),
			// setColumns
			super.createMethodDescriptor(getBeanClass(),
				"setColumns",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.setColumns.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.setColumns.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.setColumns.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.setColumns.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setHgap
			super.createMethodDescriptor(getBeanClass(),
				"setHgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.setHgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.setHgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.setHgap.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.setHgap.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setRows
			super.createMethodDescriptor(getBeanClass(),
				"setRows",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.setRows.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.setRows.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.setRows.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.setRows.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setVgap
			super.createMethodDescriptor(getBeanClass(),
				"setVgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.setVgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.setVgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.ParamDesc.setVgap.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.ParamDesc.setVgap.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// toString
			super.createMethodDescriptor(getBeanClass(),
				"toString",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, GridLayoutMessages.getString("GridLayout.MthdDesc.toString.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.MthdDesc.toString.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
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
			// columns
			super.createPropertyDescriptor(getBeanClass(),"columns", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, GridLayoutMessages.getString("GridLayout.PropDesc.columns.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.PropDesc.columns.Desc"), //$NON-NLS-1$
				BOUND, Boolean.FALSE,
				EXPERT, Boolean.FALSE,
				HIDDEN, Boolean.FALSE,
			}
			),
			// hgap
			super.createPropertyDescriptor(getBeanClass(),"hgap", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, GridLayoutMessages.getString("GridLayout.PropDesc.hgap.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.PropDesc.hgap.Desc"), //$NON-NLS-1$
				BOUND, Boolean.FALSE,
				EXPERT, Boolean.FALSE,
				HIDDEN, Boolean.FALSE,
			}
			),
			// rows
			super.createPropertyDescriptor(getBeanClass(),"rows", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, GridLayoutMessages.getString("GridLayout.PropDesc.rows.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.PropDesc.rows.Desc"), //$NON-NLS-1$
				BOUND, Boolean.FALSE,
				EXPERT, Boolean.FALSE,
				HIDDEN, Boolean.FALSE,
			}
			),
			// vgap
			super.createPropertyDescriptor(getBeanClass(),"vgap", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, GridLayoutMessages.getString("GridLayout.PropDesc.vgap.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, GridLayoutMessages.getString("GridLayout.PropDesc.vgap.Desc"), //$NON-NLS-1$
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
