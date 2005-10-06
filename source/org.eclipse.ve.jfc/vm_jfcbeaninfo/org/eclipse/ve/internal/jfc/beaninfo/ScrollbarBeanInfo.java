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
 *  $RCSfile: ScrollbarBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class ScrollbarBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resscrollbar = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.scrollbar");  //$NON-NLS-1$
	
/**
 * Gets the adjustmentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor adjustmentEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.AdjustmentEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.AdjustmentListener.class,
				"adjustmentValueChanged", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resscrollbar.getString("adjustmentValueChangedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("adjustmentValueChangedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("adjustmentEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("adjustmentEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on scroll bar interaction",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"adjustment", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, resscrollbar.getString("adjustmentEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, resscrollbar.getString("adjustmentEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      			}, 
						aDescriptorList, java.awt.event.AdjustmentListener.class,
						"addAdjustmentListener", "removeAdjustmentListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.awt.Scrollbar.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the ScrollbarBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.Scrollbar.class);
		aDescriptor.setDisplayName(resscrollbar.getString("ScrollbarDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resscrollbar.getString("ScrollbarSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/scrb32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/scrb16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
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
		    return loadImage("scrb32.gif");//$NON-NLS-1$
		if (kind == ICON_COLOR_16x16) 
		    return loadImage("scrb16.gif");//$NON-NLS-1$
	   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
		  	// addNotify()
			super.createMethodDescriptor(getBeanClass(),"addNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "addNotify()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Create the peer",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getBlockIncrement()
			super.createMethodDescriptor(getBeanClass(),"getBlockIncrement", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getBlockIncrement()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the value scrolled on selecting track",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMaximum()
			super.createMethodDescriptor(getBeanClass(),"getMaximum", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMaximum()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the maximum value",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getMinimum()
			super.createMethodDescriptor(getBeanClass(),"getMinimum", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getMinimum()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the minimum value",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getOrientation()
			super.createMethodDescriptor(getBeanClass(),"getOrientation", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getOrientation()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		SHORTDESCRIPTION, resscrollbar.getString("getOrientation()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getUnitIncrement()
			super.createMethodDescriptor(getBeanClass(),"getUnitIncrement", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getUnitIncrement()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the value scrolled on selecting arrows",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getValue()
			super.createMethodDescriptor(getBeanClass(),"getValue", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getValue()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the current value",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getVisibleAmount()
			super.createMethodDescriptor(getBeanClass(),"getVisibleAmount", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getVisibleAmount()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the range represented by scroll bar bubble",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setBlockIncrement(int)
			super.createMethodDescriptor(getBeanClass(),"setBlockIncrement", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setBlockIncrement(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setBlockIncrement(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("blockValueParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Scrolled value on selecting track",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// setMaximum(int)
			super.createMethodDescriptor(getBeanClass(),"setMaximum", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setMaximum(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setMaximum(init)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("scrolledValueParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Maximum scrolled value",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// setMinimum(int)
			super.createMethodDescriptor(getBeanClass(),"setMinimum", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setMinimum(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setMinimum(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("scrolledValueParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Minimum scrolled value",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// setOrientation(int)
			super.createMethodDescriptor(getBeanClass(),"setOrientation", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setOrientation(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setOrientation(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("orientationParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "HORIZONTAL or VERTICAL",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// setUnitIncrement(int)
			super.createMethodDescriptor(getBeanClass(),"setUnitIncrement", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setUnitIncrement(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setUnitIncrement(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("unitValueParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Scrolled value on selecting arrow",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// setValue(int)
			super.createMethodDescriptor(getBeanClass(),"setValue", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setValue(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setValue(nit)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("valueParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Value to scroll to",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
	      		}	    		
		  	),
			// setValues(int,int,int,int)
			super.createMethodDescriptor(getBeanClass(),"setValues", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setValues(int,int,int,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setValues(int,init,int,int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("valueParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Current scroll position",
	      			}),
	      			createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("visibleParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Visible amount per page",
	      			}),
	      			createParameterDescriptor("arg3", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("minimumParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Minimum scroll value",	      			
	      			}),
	      			createParameterDescriptor("arg4", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("maximumParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Maximum scroll value",	      			
	      			})
	      		},
	      		new Class[] { 
	      			int.class, int.class, int.class, int.class 
	      		}	    		
		  	),
			// setVisibleAmount(int)
			super.createMethodDescriptor(getBeanClass(),"setVisibleAmount", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setVisibleAmount(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollbar.getString("setVisibleAmount(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollbar.getString("valueParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Visible amount per page",
	      			})
	      		},
	      		new Class[] { 
	      			int.class 
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
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// blockIncrement
			super.createPropertyDescriptor(getBeanClass(),"blockIncrement", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollbar.getString("blockIncrementDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollbar.getString("blockIncrementSD"), //$NON-NLS-1$
	    		}
	    	),
			// maximum
			super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollbar.getString("maximumDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollbar.getString("maximumSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// minimum
			super.createPropertyDescriptor(getBeanClass(),"minimum", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollbar.getString("minimumDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollbar.getString("minimumSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollbar.getString("orientationDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollbar.getString("orientationSD"), //$NON-NLS-1$
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      				resscrollbar.getString("HORIZONTALEnumDN"), new Integer(java.awt.Scrollbar.HORIZONTAL), //$NON-NLS-1$
	      				"java.awt.Scrollbar.HORIZONTAL",//$NON-NLS-1$
	      				resscrollbar.getString("VERTICALEnumDN"), new Integer(java.awt.Scrollbar.VERTICAL), //$NON-NLS-1$
	      				"java.awt.Scrollbar.VERTICAL"//$NON-NLS-1$
	    			}
	    		}
	    	),
			// unitIncrement
			super.createPropertyDescriptor(getBeanClass(),"unitIncrement", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollbar.getString("unitIncrementDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollbar.getString("unitIncrementSD"), //$NON-NLS-1$
	    		}
	    	),
			// value
			super.createPropertyDescriptor(getBeanClass(),"value", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollbar.getString("valueDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollbar.getString("valueSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// visibleAmount
			super.createPropertyDescriptor(getBeanClass(),"visibleAmount", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollbar.getString("visibleAmountDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollbar.getString("visibleAmountSD"), //$NON-NLS-1$
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
