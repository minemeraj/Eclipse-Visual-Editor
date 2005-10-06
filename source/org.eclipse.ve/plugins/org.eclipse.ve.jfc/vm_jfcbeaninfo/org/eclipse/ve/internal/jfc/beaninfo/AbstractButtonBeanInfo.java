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
 *  $RCSfile: AbstractButtonBeanInfo.java,v $
 *  $Revision: 1.9 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class AbstractButtonBeanInfo extends IvjBeanInfo {


private static java.util.ResourceBundle resAbstractButton = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.abstractbutton");  //$NON-NLS-1$

/**
 * Gets the actionevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor actionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ActionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ActionListener.class,
				"actionPerformed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.ActionPerformed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.ActionPerformed.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("actionEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resAbstractButton.getString("ParamDesc.ActionPerformed.actionEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on clicking the button",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"action", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resAbstractButton.getString("EventSetDesc.ActionPerformed.Action.name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resAbstractButton.getString("EventSetDesc.ActionPerformed.Action.Desc"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
						PREFERRED, Boolean.TRUE	      				
	      				}, 
						aDescriptorList, java.awt.event.ActionListener.class,
						"addActionListener", "removeActionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor changeEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.ChangeEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.ChangeListener.class,
				"stateChanged",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.StateChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.StateChanged.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("stateChangeEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resAbstractButton.getString("ParamDesc.StateChanged.StateChangedEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event fired on state change",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"change", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resAbstractButton.getString("EventSetDesc.Change.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resAbstractButton.getString("EventSetDesc.Change.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.ChangeListener.class,
						"addChangeListener", "removeChangeListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the itemevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor itemEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.ItemEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.ItemListener.class,
				"itemStateChanged",  //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, resAbstractButton.getString("MthdDesc.ItemStateChanged.Name"), //$NON-NLS-1$
				SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.ItemStateChanged.Desc"), //$NON-NLS-1$
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("itemEvent", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, resAbstractButton.getString("ParamDesc.ItemStateChanged.itemEvent.Name"), //$NON-NLS-1$
					// SHORTDESCRIPTION, "Item state changed event",
					}
				)
			},
			paramTypes
			)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"item", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resAbstractButton.getString("EventSetDesc.ItemStateChanged.item.Name"), //$NON-NLS-1$
						SHORTDESCRIPTION, resAbstractButton.getString("EventSetDesc.ItemStateChanged.item.Desc"), //$NON-NLS-1$
						}, 
						aDescriptorList, java.awt.event.ItemListener.class,
						"addItemListener", "removeItemListener"); //$NON-NLS-1$ //$NON-NLS-2$

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
			changeEventSetDescriptor(),
			itemEventSetDescriptor()
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}


/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public  Class getBeanClass() {
	return javax.swing.AbstractButton.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, resAbstractButton.getString("BeanDesc.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, resAbstractButton.getString("BeanDesc.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/butt32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/butt16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("butt32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("butt16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// doClick(int)
			super.createMethodDescriptor(getBeanClass(),"doClick",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.DoClick.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.DoClick.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("time", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.DoClick.Time.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Time in milliseconds",
	      				}
	      			)},
	      		new Class[] { int.class }		    		
		  	),
		  	// getActionCommand()
			super.createMethodDescriptor(getBeanClass(),"getActionCommand",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetActionCommand.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.GetActionCommand.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDisabledIcon()
			super.createMethodDescriptor(getBeanClass(),"getDisabledIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetDisabledIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the disabled icon",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDisabledSelectedIcon()
			super.createMethodDescriptor(getBeanClass(),"getDisabledSelectedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetDisabledSelectedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the disabled selected icon",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHorizontalAlignment()
			super.createMethodDescriptor(getBeanClass(),"getHorizontalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetHorizontalAlignment.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.GetHorizontalAlignment.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHorizontalTextPosition()
			super.createMethodDescriptor(getBeanClass(),"getHorizontalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetHorizontalTextPosition.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.GetHorizontalTextPosition.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getIcon()
			super.createMethodDescriptor(getBeanClass(),"getIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the default icon",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMargin()
			super.createMethodDescriptor(getBeanClass(),"getMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetMargin.Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the margin between border and text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the model",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMnemonic()
			super.createMethodDescriptor(getBeanClass(),"getMnemonic",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetMnemonic.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.GetMnemonic.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPressedIcon
			super.createMethodDescriptor(getBeanClass(),"getPressedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetPressedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the pressed icon"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRolloverIcon
			super.createMethodDescriptor(getBeanClass(),"getRolloverIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetRolloverIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the rollover icon"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRolloverSelectedIcon
			super.createMethodDescriptor(getBeanClass(),"getRolloverSelectedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetRolloverSelectedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the rollover selected icon"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedIcon()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetSelectedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the selected icon",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getText()
			super.createMethodDescriptor(getBeanClass(),"getText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetText.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetUI.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the ButtonUI object",
	      		//EXPERT, Boolean.TRUE,
	      		//OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVerticalAlignment()
			super.createMethodDescriptor(getBeanClass(),"getVerticalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetVerticalAlignment.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.GetVerticalAlignment.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVerticalTextPosition()
			super.createMethodDescriptor(getBeanClass(),"getVerticalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.GetVerticalTextPosition.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.GetVerticalTextPosition.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isBorderPainted
			super.createMethodDescriptor(getBeanClass(),"isBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.IsBorderPainted.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.IsBorderPainted.Desc")	    		},  //$NON-NLS-1$
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isContentAreaFilled
			super.createMethodDescriptor(getBeanClass(),"isContentAreaFilled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.isContentAreaFilled.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Sets whether the button should paint the content area or leave it transparent"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isFocusPainted
			super.createMethodDescriptor(getBeanClass(),"isFocusPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.IsFocusPainted.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Whether focus should be painted"	    	
	      		},
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isRolloverEnabled
			super.createMethodDescriptor(getBeanClass(),"isRolloverEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.IsRolloverEnabled.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Whether the rollover effects are enabled",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isSelected
			super.createMethodDescriptor(getBeanClass(),"isSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.IsSelected.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.IsSelected.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setActionCommand(String)
			super.createMethodDescriptor(getBeanClass(),"setActionCommand",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetActionCommand.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.SetActionCommand.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aCommand", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetActionCommand.aCommand.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Command string",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setBorderPainted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetBorderPainted.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.SetBorderPainted.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetBorderPainted.b.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint border",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setContentAreaFilled(boolean)
			super.createMethodDescriptor(getBeanClass(),"setContentAreaFilled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetContentAreaFilled.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Sets whether the button should paint the content area or leave it transparent",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetContentAreaFilled.b.name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint the content area",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setDisabledIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setDisabledIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetDiabledIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon for the disabled state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetDisabledIcon.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Disabled icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setDisabledSelectedIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setDisabledSelectedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetDisabledSelectedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon for the disabled selected state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetDisabledSelectedIcon.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Disabled selected icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setFocusPainted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setFocusPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetFocusPainted.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether the focus should be painted",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetFocusPainted.b.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint focus",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setHorizontalAlignment(int)
			super.createMethodDescriptor(getBeanClass(),"setHorizontalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetHorizontalAlignment.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the horizontal alignment of icon and text",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("alignment", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetHorizontalAlignment.alignment.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setHorizontalTextPosition(int)
			super.createMethodDescriptor(getBeanClass(),"setHorizontalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetHorizontalTextPosition.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the horizontal text position relative to the icon",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("position", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetHorizontalTextPosition.position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the default icon",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetIcon.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Default icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setMargin(Insets)
			super.createMethodDescriptor(getBeanClass(),"setMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetMargin.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the margin between border and label",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("insets", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetMargin.insets.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Default icon",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Insets.class
	      		}		    		
		  	),
		  	// setMnemonic(char)
			super.createMethodDescriptor(getBeanClass(),"setMnemonic",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetMnemonic.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.SetMnemonic.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mnemonic", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetMnemonic.mnemonic.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Mnemonic key",
	      				})
	      		},
	      		new Class[] {
	      			char.class
	      		}		    		
		  	),
		  	// setPressedIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setPressedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetPressedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon for the pressed state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetPressedIcon.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Pressed icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setRolloverEnabled(boolean)
			super.createMethodDescriptor(getBeanClass(),"setRolloverEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetRolloverEnabled.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Disable or enable rollover effects",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetRolloverEnabled.b.name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to enable rollover effects",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setRolloverIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetRolloverIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon for the rollover state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetRolloverIcon.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Rollover icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setRolloverSelectedIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setRolloverSelectedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetRolloverSelectedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon for the rollover selected state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetRolloverSelectedIcon.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Rollover selected icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setSelected(boolean)
			super.createMethodDescriptor(getBeanClass(),"setSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetSelected.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.SetSelected.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetSelected.b.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to select",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setModel(ButtonModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the button model for data",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetModel.model.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Button model",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.ButtonModel.class
	      		}		    		
		  	),
		  	// setSelectedIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setSelectedIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetSelectedIcon.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon for the selected state",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetSelectedIcon.icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Selected icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setText(String)
			super.createMethodDescriptor(getBeanClass(),"setText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetText.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the text",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("text", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetText.text.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setUI(ButtonUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetUI.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the Button UI",
	      		//EXPERT, Boolean.TRUE,
	      		//OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("uI", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetUI.ui.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Button UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.ButtonUI.class
	      		}		    		
		  	),
		  	// setVerticalAlignment(int)
			super.createMethodDescriptor(getBeanClass(),"setVerticalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetVerticalAlignment.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.SetVerticalAlignment.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("alignment", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetVerticalAlignment.alignment.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setVerticalTextPosition(int)
			super.createMethodDescriptor(getBeanClass(),"setVerticalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resAbstractButton.getString("MthdDesc.SetVerticalTextPosition.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resAbstractButton.getString("MthdDesc.SetVerticalTextPosition.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("position", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resAbstractButton.getString("ParamDesc.SetVerticalTextPosition.position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	)
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
			// actionCommand
			super.createPropertyDescriptor(getBeanClass(),"actionCommand", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.actionCommand.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.ActionCommand.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// action
			super.createPropertyDescriptor(getBeanClass(),"action", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.action.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.action.Desc"), //$NON-NLS-1$
	    		}
	    	),			
	    	// borderPainted
			super.createPropertyDescriptor(getBeanClass(),"borderPainted", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.borderPainted.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.borderPainted.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// contentAreaFilled
			super.createPropertyDescriptor(getBeanClass(),"contentAreaFilled", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.contentAreaFilled.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.ContentAreaFilled.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE    	
	    		}
	    	),
	    	// disabledIcon
			super.createPropertyDescriptor(getBeanClass(),"disabledIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.disabledIcon.Name"),  //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.DisabledIcon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// disabledSelectedIcon
			super.createPropertyDescriptor(getBeanClass(),"disabledSelectedIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.disabledSelectedIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.disabledSelectedIcon.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// focusPainted
			super.createPropertyDescriptor(getBeanClass(),"focusPainted", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.focusPainted.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.focusPainted.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// horizontalAlignment
			super.createPropertyDescriptor(getBeanClass(),"horizontalAlignment", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.horizontalAlignment.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.HorizontalAlignment.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			resAbstractButton.getString("Alignment.LEFT"), new Integer(javax.swing.SwingConstants.LEFT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEFT", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.RIGHT"), new Integer(javax.swing.SwingConstants.RIGHT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.RIGHT", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.LEADING"), new Integer(javax.swing.SwingConstants.LEADING), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEADING", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.TRAILING") , new Integer(javax.swing.SwingConstants.TRAILING), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TRAILING"      			 //$NON-NLS-1$
	    		}
	    	}
	    	),
	    	// horizontalTextPosition
			super.createPropertyDescriptor(getBeanClass(),"horizontalTextPosition", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.horizontalTextPosition.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.HorizontalTextPosition.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			resAbstractButton.getString("Alignment.LEFT"), new Integer(javax.swing.SwingConstants.LEFT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEFT", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.RIGHT"), new Integer(javax.swing.SwingConstants.RIGHT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.RIGHT", //$NON-NLS-1$
					resAbstractButton.getString("Alignment.LEADING"), new Integer(javax.swing.SwingConstants.LEADING), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEADING", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.TRAILING") , new Integer(javax.swing.SwingConstants.TRAILING), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TRAILING"      		      						 //$NON-NLS-1$
	    		}
	    	}
	    	),
	    	// icon
			super.createPropertyDescriptor(getBeanClass(),"icon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.icon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.Icon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// margin
			super.createPropertyDescriptor(getBeanClass(),"margin", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resAbstractButton.getString("PropDesc.margin.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.margin.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// mnemonic
			super.createPropertyDescriptor(getBeanClass(),"mnemonic", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resAbstractButton.getString("PropDesc.mnemonic.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.Mnemonic.Desc") //$NON-NLS-1$
				}
			),
	    	// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.model.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.Model.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//PREFERRED, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// pressedIcon
			super.createPropertyDescriptor(getBeanClass(),"pressedIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.pressedIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.PressedIcon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// rolloverEnabled
			super.createPropertyDescriptor(getBeanClass(),"rolloverEnabled", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.rolloverEnabled.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.RolloverEnabled.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE	      	
	    		}
	    	),
	    	// rolloverIcon
			super.createPropertyDescriptor(getBeanClass(),"rolloverIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.rolloverIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.RolloverIcon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE	      	
	    		}
	    	),
	    	// rolloverSelectedIcon
			super.createPropertyDescriptor(getBeanClass(),"rolloverSelectedIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.rolloverSelectedIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.rolloverSelectedIcon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selected
			super.createPropertyDescriptor(getBeanClass(),"selected", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.selected.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.Selected.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// selectedIcon
			super.createPropertyDescriptor(getBeanClass(),"selectedIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.selectedIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.selectedIcon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectedObjects
			super.createPropertyDescriptor(getBeanClass(),"selectedObjects", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.selectedObjects.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.selectedObjects.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
	    	// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.text.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.Text.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// verticalAlignment
			super.createPropertyDescriptor(getBeanClass(),"verticalAlignment", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.verticalAlignment.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.VerticalAlignment.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			resAbstractButton.getString("Alignment.TOP"), new Integer(javax.swing.SwingConstants.TOP), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TOP", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.BOTTOM"), new Integer(javax.swing.SwingConstants.BOTTOM), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.BOTTOM"	      			 //$NON-NLS-1$
	    		}
	    		}
	    	),
	    	// verticalTextPosition
			super.createPropertyDescriptor(getBeanClass(),"verticalTextPosition", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.verticalTextPosition.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.VerticalTextPosition.Desc"), //$NON-NLS-1$
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			resAbstractButton.getString("Alignment.TOP"), new Integer(javax.swing.SwingConstants.TOP), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TOP", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			resAbstractButton.getString("Alignment.BOTTOM"), new Integer(javax.swing.SwingConstants.BOTTOM), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.BOTTOM"	      			 //$NON-NLS-1$
	    		}
	    		}
	    	),		
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resAbstractButton.getString("PropDesc.ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resAbstractButton.getString("PropDesc.ui.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//PREFERRED, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
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
