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
 *  $RCSfile: JScrollBarBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class JScrollBarBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JScrollBarMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jscrollbar");  //$NON-NLS-1$

/**
 * Gets the adjustmentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor adjustmentEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.AdjustmentEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.AdjustmentListener.class,
				"adjustmentValueChanged",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("adjustmentValueChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("adjustmentValueChanged.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("adjustmentEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JScrollBarMessages.getString("adjustmentValueChanged.adjustmentEvent.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JScrollBarMessages.getString("adjustmentValueChanged.adjustmentEvent.Desc"), //$NON-NLS-1$
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"adjustment", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JScrollBarMessages.getString("adjustmentEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JScrollBarMessages.getString("adjustmentEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, java.awt.event.AdjustmentListener.class,
						"addAdjustmentListener", "removeAdjustmentListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JScrollBar.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JScrollBarMessages.getString("JScrollBar.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JScrollBarMessages.getString("JScrollBar.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/scrb32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/scrb16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		java.beans.EventSetDescriptor aDescriptorList[] = {
			adjustmentEventSetDescriptor()
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
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("scrb32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("scrb16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible text",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getBlockIncrement()
			super.createMethodDescriptor(getBeanClass(),"getBlockIncrement",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getBlockIncrement().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("getBlockIncrement().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMaximum()
			super.createMethodDescriptor(getBeanClass(),"getMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getMaximum().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the maximum value of the scroll bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinimum()
			super.createMethodDescriptor(getBeanClass(),"getMinimum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getMinimum().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum value of the scroll bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getModel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the data model",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUnitIncrement()
			super.createMethodDescriptor(getBeanClass(),"getUnitIncrement",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getUnitIncrement().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("getUnitIncrement().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getValue()
			super.createMethodDescriptor(getBeanClass(),"getValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getValue().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the scroll bar's current value",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getValueIsAdjusting()
			super.createMethodDescriptor(getBeanClass(),"getValueIsAdjusting",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getValueIsAdjusting().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the list value is adjusting"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setBlockIncrement(int)
			super.createMethodDescriptor(getBeanClass(),"setBlockIncrement",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setBlockIncrement(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("setBlockIncrement(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("blockIncrement", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setBlockIncrement(int).blockIncrement.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Block increment",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setEnabled(boolean)
			super.createMethodDescriptor(getBeanClass(),"setEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setEnabled(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Enable or disable the scroll bar",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setEnabled(boolean).aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to enable",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setMaximum(int)
			super.createMethodDescriptor(getBeanClass(),"setMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setMaximum(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the maximum scrolled value",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("max", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setMaximum(int).maximum.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Maximum scrolled value",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setMinimum(int)
			super.createMethodDescriptor(getBeanClass(),"setMinimum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setMinimum(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the minimum scrolled value",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("min", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setMinimum(int).minimum.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Minimum scrolled value",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setModel(BoundedRangeModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setModel(BoundedRangeModel).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the model that handles scrollbar properties",
	      		EXPERT, Boolean.TRUE,
	      		BOUND, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setModel(BoundedRangeModel).aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "BoundedRangeModel instance",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.BoundedRangeModel.class
	      		}		    		
		  	),
			// setOrientation(int)
			super.createMethodDescriptor(getBeanClass(),"setOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setOrientation(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the orientation",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("orientation", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setOrientation(int).orientation.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "HORIZONTAL or VERTICAL",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setUnitIncrement(int)
			super.createMethodDescriptor(getBeanClass(),"setUnitIncrement",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setUnitIncrement(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("setUnitIncrement(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("unitIncrement", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setUnitIncrement(int).unitIncrement.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Unit increment",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setValue(int)
			super.createMethodDescriptor(getBeanClass(),"setValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setValue(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("setValue(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("value", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setValue(int).value.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "New value to scroll to",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setValueIsAdjusting(boolean)
			super.createMethodDescriptor(getBeanClass(),"setValueIsAdjusting",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setValueIsAdjusting(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("setValueIsAdjusting(boolean).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setValueIsAdjusting.b.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "whether the value is adjusting",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setVisibleAmount(int)
			super.createMethodDescriptor(getBeanClass(),"setVisibleAmount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setVisibleAmount(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("setVisibleAmount(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("extent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setVisibleAmount(int).extent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Amount of view visible",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setUI(ScrollBarUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JScrollBarMessages.getString("setUI(ScrollBarUI).Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		BOUND, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("scrollBarUI", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JScrollBarMessages.getString("setUI(scrollBarUI).scrollBarUI.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.plaf.ScrollBarUI.class
	      		}		    		
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
			// blockIncrement
			super.createPropertyDescriptor(getBeanClass(),"blockIncrement", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("blockIncrement.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("blockIncrement.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
			// enabled
			super.createPropertyDescriptor(getBeanClass(),"enabled", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("enabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("enabled.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// maximum
			super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("maximum.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("maximum.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// minimum
			super.createPropertyDescriptor(getBeanClass(),"minimum", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("minimum.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("minimum.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("model.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("model.Desc"), //$NON-NLS-1$
	      		//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	    		}
	    	),
	    	// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
	      			DISPLAYNAME, JScrollBarMessages.getString("orientation.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JScrollBarMessages.getString("orientation.Desc"), //$NON-NLS-1$
	      			PREFERRED, Boolean.TRUE,
	      			IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      				JScrollBarMessages.getString("orientation.HORIZONTAL"), new Integer(javax.swing.JScrollBar.HORIZONTAL), //$NON-NLS-1$
	      				"javax.swing.JScrollBar.HORIZONTAL", //$NON-NLS-1$
	      				JScrollBarMessages.getString("orientation.VERTICAL"), new Integer(javax.swing.JScrollBar.VERTICAL), //$NON-NLS-1$
	      				"javax.swing.JScrollBar.VERTICAL" //$NON-NLS-1$
	    			}
	    		}
	    	),
			// unitIncrement
			super.createPropertyDescriptor(getBeanClass(),"unitIncrement", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("unitIncrement.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("unitIncrement.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
	    	// value
			super.createPropertyDescriptor(getBeanClass(),"value", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("value.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("value.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
			// valueIsAdjusting
			super.createPropertyDescriptor(getBeanClass(),"valueIsAdjusting", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JScrollBarMessages.getString("valueIsAdjusting.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JScrollBarMessages.getString("valueIsAdjusting.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// visibleAmount
			super.createPropertyDescriptor(getBeanClass(),"visibleAmount", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("visibleAmount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("visibleamount.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JScrollBarMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JScrollBarMessages.getString("ui.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	    		}
	    	)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}
