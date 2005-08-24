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
 *  $RCSfile: CardLayoutBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:10 $ 
 */

import java.beans.*;

public class CardLayoutBeanInfo extends IvjBeanInfo {

private static java.util.ResourceBundle CardLayoutMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.cardlayout");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.CardLayout.class;
}

public java.beans.BeanDescriptor getBeanDescriptor() {
	return new java.beans.BeanDescriptor(java.awt.CardLayout.class);
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
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.addLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.addLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
					createParameterDescriptor("Object", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.Object.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.Object.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class, java.lang.Object.class }
			),
			// addLayoutComponent
			super.createMethodDescriptor(getBeanClass(),
				"addLayoutComponent",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.addLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.addLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.String.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.String.Desc"), //$NON-NLS-1$
					}),
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.addLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.lang.String.class, java.awt.Component.class }
			),
			// first
			super.createMethodDescriptor(getBeanClass(),
				"first",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.first.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.first.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.first.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.first.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// getHgap
			super.createMethodDescriptor(getBeanClass(),
				"getHgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.getHgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.getHgap.Desc"), //$NON-NLS-1$
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
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.getLayoutAlignmentX.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.getLayoutAlignmentX.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.getLayoutAlignmentX.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.getLayoutAlignmentX.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// getLayoutAlignmentY
			super.createMethodDescriptor(getBeanClass(),
				"getLayoutAlignmentY",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.getLayoutAlignmentY.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.getLayoutAlignmentY.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.getLayoutAlignmentY.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.getLayoutAlignmentY.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// getVgap
			super.createMethodDescriptor(getBeanClass(),
				"getVgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.getVgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.getVgap.Desc"), //$NON-NLS-1$
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
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.invalidateLayout.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.invalidateLayout.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.invalidateLayout.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.invalidateLayout.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// last
			super.createMethodDescriptor(getBeanClass(),
				"last",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.last.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.last.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.last.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.last.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// layoutContainer
			super.createMethodDescriptor(getBeanClass(),
				"layoutContainer",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.layoutContainer.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.layoutContainer.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.layoutContainer.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.layoutContainer.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// maximumLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"maximumLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.maximumLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.maximumLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.maximumLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.maximumLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// minimumLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"minimumLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.minimumLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.minimumLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.minimumLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.minimumLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// next
			super.createMethodDescriptor(getBeanClass(),
				"next",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.next.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.next.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.next.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.next.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// preferredLayoutSize
			super.createMethodDescriptor(getBeanClass(),
				"preferredLayoutSize",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.preferredLayoutSize.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.preferredLayoutSize.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.preferredLayoutSize.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.preferredLayoutSize.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// previous
			super.createMethodDescriptor(getBeanClass(),
				"previous",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.previous.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.previous.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.previous.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.previous.Container.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class }
			),
			// removeLayoutComponent
			super.createMethodDescriptor(getBeanClass(),
				"removeLayoutComponent",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.removeLayoutComponent.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.removeLayoutComponent.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Component", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.removeLayoutComponent.Component.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.removeLayoutComponent.Component.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Component.class }
			),
			// setHgap
			super.createMethodDescriptor(getBeanClass(),
				"setHgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.setHgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.setHgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.setHgap.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.setHgap.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// setVgap
			super.createMethodDescriptor(getBeanClass(),
				"setVgap",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.setVgap.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.setVgap.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.setVgap.int.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.setVgap.int.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { int.class }
			),
			// show
			super.createMethodDescriptor(getBeanClass(),
				"show",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.show.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.show.Desc"), //$NON-NLS-1$
					PREFERRED, Boolean.TRUE,
					EXPERT, Boolean.FALSE,
					OBSCURE, Boolean.FALSE,
				},
				new ParameterDescriptor[] {
					createParameterDescriptor("Container", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.show.Container.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.show.Container.Desc"), //$NON-NLS-1$
					}),
					createParameterDescriptor("String", new Object[] { //$NON-NLS-1$
						//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.ParamDesc.show.String.Name"), //$NON-NLS-1$
						//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.ParamDesc.show.String.Desc"), //$NON-NLS-1$
					}),
				},
				new Class[] { java.awt.Container.class, java.lang.String.class }
			),
			// toString
			super.createMethodDescriptor(getBeanClass(),
				"toString",  //$NON-NLS-1$
				new Object[] {
					//DISPLAYNAME, CardLayoutMessages.getString("CardLayout.MthdDesc.toString.Name"), //$NON-NLS-1$
					//SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.MthdDesc.toString.Desc"), //$NON-NLS-1$
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
			// hgap
			super.createPropertyDescriptor(getBeanClass(),"hgap", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CardLayoutMessages.getString("CardLayout.PropDesc.hgap.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.PropDesc.hgap.Desc"), //$NON-NLS-1$
				BOUND, Boolean.FALSE,
				EXPERT, Boolean.FALSE,
				HIDDEN, Boolean.FALSE,
			}
			),
			// vgap
			super.createPropertyDescriptor(getBeanClass(),"vgap", new Object[] { //$NON-NLS-1$
				DISPLAYNAME, CardLayoutMessages.getString("CardLayout.PropDesc.vgap.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, CardLayoutMessages.getString("CardLayout.PropDesc.vgap.Desc"), //$NON-NLS-1$
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
