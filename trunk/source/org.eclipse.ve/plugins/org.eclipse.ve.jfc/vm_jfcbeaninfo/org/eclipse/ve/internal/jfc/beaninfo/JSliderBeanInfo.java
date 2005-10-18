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
 *  $RCSfile: JSliderBeanInfo.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-18 15:32:23 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;

public class JSliderBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JSliderMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jslider");  //$NON-NLS-1$
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
	   			DISPLAYNAME, JSliderMessages.getString("stateChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("stateChanged.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("stateChangeEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JSliderMessages.getString("stateChanged.stateChangeEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event fired when slider value is changed",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"change", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JSliderMessages.getString("changeEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JSliderMessages.getString("changeEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.ChangeListener.class,
						"addChangeListener", "removeChangeListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JSlider.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JSliderMessages.getString("JSlider.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JSliderMessages.getString("JSlider.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jslide32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jslide16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
		EventSetDescriptor aDescriptorList[] = {
			changeEventSetDescriptor()
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
	    return loadImage("jslide32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jslide16.gif"); //$NON-NLS-1$
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
	   			DISPLAYNAME, JSliderMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible text",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getExtent()
			super.createMethodDescriptor(getBeanClass(),"getExtent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getExtent().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getExtent().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getInverted()
			super.createMethodDescriptor(getBeanClass(),"getInverted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getInverted().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getInverted().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMajorTickSpacing()
			super.createMethodDescriptor(getBeanClass(),"getMajorTickSpacing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getMajorTickSpacing().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getMajorTickSpacing().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMaximum()
			super.createMethodDescriptor(getBeanClass(),"getMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getMaximum().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getMaximum().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinimum()
			super.createMethodDescriptor(getBeanClass(),"getMinimum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getMinimum().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getMinimum().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinorTickSpacing()
			super.createMethodDescriptor(getBeanClass(),"getMinorTickSpacing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getMinorTickSpacing().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getMinorTickSpacing().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getModel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the data model handling slider properties",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getOrientation()
			super.createMethodDescriptor(getBeanClass(),"getOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getOrientation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getOrientation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPaintLabels()
			super.createMethodDescriptor(getBeanClass(),"getPaintLabels",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getPaintLabels().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getPaintLabels().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPaintTicks()
			super.createMethodDescriptor(getBeanClass(),"getPaintTicks",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getPaintTicks().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getPaintTicks().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPaintTrack()
			super.createMethodDescriptor(getBeanClass(),"getPaintTrack",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getPaintTrack().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getPaintTrack().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSnapToTicks()
			super.createMethodDescriptor(getBeanClass(),"getSnapToTicks",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getSnapToTicks().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getSnapToTicks().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getValue()
			super.createMethodDescriptor(getBeanClass(),"getValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getValue().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("getValue().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getValueIsAdjusting()
			super.createMethodDescriptor(getBeanClass(),"getValueIsAdjusting",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getValueIsAdjusting().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the list value is adjusting"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setExtent(int)
			super.createMethodDescriptor(getBeanClass(),"setExtent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setExtent(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the size of the range covered by knob",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("extent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setExtent(int).extent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Size of the range covered",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setInverted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setInverted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setInverted(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether to reverse the slider values",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setInverted(boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE if slider values are reversed",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setMajorTickSpacing(int)
			super.createMethodDescriptor(getBeanClass(),"setMajorTickSpacing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setMajorTickSpacing(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setMajorTickSpacing(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("n", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setMajorTickSpacing(int).spacing.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Pixels between major ticks",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setMaximum(int)
			super.createMethodDescriptor(getBeanClass(),"setMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setMaximum(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setMaximum(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("max", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setMaximum(int).maximum.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Maximum value",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setMinimum(int)
			super.createMethodDescriptor(getBeanClass(),"setMinimum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setMinimum(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setMinimum(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("min", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setMinimum(int).minimum.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Minimum value",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setMinorTickSpacing(int)
			super.createMethodDescriptor(getBeanClass(),"setMinorTickSpacing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setMinorTickSpacing(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setMinorTickSpacing(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("n", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setMinorTickSpacing(int).spacing.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Pixels between minor ticks",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setModel(BoundedRangeModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setModel(BoundedRangeModel).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the model that handles slider properties",
	      		EXPERT, Boolean.TRUE,
	      		BOUND, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setModel(BoundedRangeModel).aModel.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, JSliderMessages.getString("setOrientation(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the orientation",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("orientation", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setOrientation(int).orientation.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "HORIZONTAL or VERTICAL",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setPaintLabels(boolean)
			super.createMethodDescriptor(getBeanClass(),"setPaintLabels",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setPaintLabels(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setPaintLabels(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setPaintLabels(boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint slider labels",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setPaintTicks(boolean)
			super.createMethodDescriptor(getBeanClass(),"setPaintTicks",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setPaintTicks(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setPaintTicks(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setPaintTicks(boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint slider ticks",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setPaintTrack(boolean)
			super.createMethodDescriptor(getBeanClass(),"setPaintTrack",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setPaintTrack(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setPaintTrack(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setPaintTrack(boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to paint slider track",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setSnapToTicks(boolean)
			super.createMethodDescriptor(getBeanClass(),"setSnapToTicks",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setSnapToTicks(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setSnapToTicks(boolean).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setSnapToTicks(boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to move only to ticks",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setValue(int)
			super.createMethodDescriptor(getBeanClass(),"setValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setValue(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setValue(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("value", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setValue(int).value.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "New value to slide to",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setValueIsAdjusting(boolean)
			super.createMethodDescriptor(getBeanClass(),"setValueIsAdjusting",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setValueIsAdjusting(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("setValueIsAdjusting(boolean).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setValueIsAdjusting.b.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "whether the value is adjusting",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("getUI().Name"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setUI(SliderUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JSliderMessages.getString("setUI(SliderUI).Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		BOUND, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("uI", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JSliderMessages.getString("setUI(SliderUI).ui.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.plaf.SliderUI.class
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
			// extent
			super.createPropertyDescriptor(getBeanClass(),"extent", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("extent.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("extent.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// inverted
			super.createPropertyDescriptor(getBeanClass(),"inverted", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("inverted.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("inverted.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// majorTickSpacing
			super.createPropertyDescriptor(getBeanClass(),"majorTickSpacing", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("majorTickSpacing.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("majorTickSpacing.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
	    	// maximum
			super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("maximum.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("maximum.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// minimum
			super.createPropertyDescriptor(getBeanClass(),"minimum", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("minimum.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("minimum.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// minorTickSpacing
			super.createPropertyDescriptor(getBeanClass(),"minorTickSpacing", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("minorTickSpacing.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("minorTickSpacing.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
	    	// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("model.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("model.Desc"), //$NON-NLS-1$
	      		//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      		BOUND, Boolean.TRUE,
				EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
					DISPLAYNAME, JSliderMessages.getString("orientation.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JSliderMessages.getString("orientation.spacing"), //$NON-NLS-1$
	      			IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      				JSliderMessages.getString("orientation.HORIZONTAL"), new Integer(javax.swing.JSlider.HORIZONTAL), //$NON-NLS-1$
	      				"javax.swing.JSlider.HORIZONTAL", //$NON-NLS-1$
	      				JSliderMessages.getString("orientation.VERTICAL"), new Integer(javax.swing.JSlider.VERTICAL), //$NON-NLS-1$
	      				"javax.swing.JSlider.VERTICAL" //$NON-NLS-1$
	    			}
	    		}
	    	),
			// paintLabels
			super.createPropertyDescriptor(getBeanClass(),"paintLabels", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("paintLabels.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("paintLabels.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// paintTicks
			super.createPropertyDescriptor(getBeanClass(),"paintTicks", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("paintTicks.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("paintTicks.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// paintTrack
			super.createPropertyDescriptor(getBeanClass(),"paintTrack", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("paintTrack.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("paintTrack.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// snapToTicks
			super.createPropertyDescriptor(getBeanClass(),"snapToTicks", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("snapToTicks.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("snapToTicks.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// value
			super.createPropertyDescriptor(getBeanClass(),"value", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("value.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("value.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		PREFERRED, Boolean.TRUE
	    		}
	    	),
			// valueIsAdjusting
			super.createPropertyDescriptor(getBeanClass(),"valueIsAdjusting", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JSliderMessages.getString("valueIsAdjusting.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JSliderMessages.getString("valueIsAdjusting.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// labelTable
			super.createPropertyDescriptor(getBeanClass(),"labelTable", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("labelTable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("labelTable.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		//DESIGNTIMEPROPERTY, Boolean.FALSE
				EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JSliderMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JSliderMessages.getString("ui.Desc"), //$NON-NLS-1$
	      		//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      		BOUND, Boolean.TRUE,
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
