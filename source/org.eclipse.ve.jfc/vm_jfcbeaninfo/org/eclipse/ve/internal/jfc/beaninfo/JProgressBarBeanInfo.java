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
 *  $RCSfile: JProgressBarBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

import javax.swing.BoundedRangeModel;
import javax.swing.plaf.ProgressBarUI;

public class JProgressBarBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JProgressBarMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jprogressbar");  //$NON-NLS-1$

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
	   			DISPLAYNAME, JProgressBarMessages.getString("stateChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("stateChanged.Desc"), //$NON-NLS-1$
				PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("stateChangeEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JProgressBarMessages.getString("stateChanged.stateChangeEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event fired when progress bar value has changed",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"change", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JProgressBarMessages.getString("changeEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JProgressBarMessages.getString("changeEvents.Desc"), //$NON-NLS-1$
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
	return javax.swing.JProgressBar.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JProgressBarMessages.getString("JProgressBar.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JProgressBarMessages.getString("JProgressBar.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jpbbar32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jpbbar16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	    return loadImage("jpbar32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jpbar16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getMaximum()
			super.createMethodDescriptor(getBeanClass(),"getMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("getMaximum().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the maximum value of the progress bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getMinimum()
			super.createMethodDescriptor(getBeanClass(),"getMinimum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("getMinimum().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the minimum value of the progress bar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getOrientation()
			super.createMethodDescriptor(getBeanClass(),"getOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("getOrientation().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("getOrientation().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isBorderPainted()
			super.createMethodDescriptor(getBeanClass(),"isBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("isBorderPainted().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Whether the progress bar has a border",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// isStringPainted()
			super.createMethodDescriptor(getBeanClass(),"isStringPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("isStringPainted().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "true if the progress bar will render a string",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getValue()
			super.createMethodDescriptor(getBeanClass(),"getValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("getValue().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("getValue().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// setBorderPainted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setBorderPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setBorderPainted(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set whether to border must be painted",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setBorderPainted(boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE if border is required",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
			// setStringPainted(boolean)
			super.createMethodDescriptor(getBeanClass(),"setStringPainted",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setStringPainted(boolean).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "set true so the progress bar will render a string",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("bool", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setStringPainted(boolean).aBoolean.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE if the progress bar will render a string",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setMaximum(int)
			super.createMethodDescriptor(getBeanClass(),"setMaximum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setMaximum(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("setMaximum(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("max", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setMaximum(int).maximum.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JProgressBarMessages.getString("setMaximum(int).maximum.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setMinimum(int)
			super.createMethodDescriptor(getBeanClass(),"setMinimum",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setMinimum(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("setMinimum(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("min", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setMinimum(int).minimum.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JProgressBarMessages.getString("setMinimum(int).minimum.Desc"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// setOrientation(int)
			super.createMethodDescriptor(getBeanClass(),"setOrientation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setOrientation(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the orientation",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("orientation", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setOrientation(int).orientation.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "HORIZONTAL or VERTICAL",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setValue(int)
			super.createMethodDescriptor(getBeanClass(),"setValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setValue(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("setValue(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("value", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setValue(int).value.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "New value",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("getModel().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("getModel().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setModel(BoundedRangeModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setModel(BoundedRangeModel).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("setModel(BoundedRangeModel).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setModel(BoundedRangeModel).model.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			BoundedRangeModel.class
	      		}		    		
		  	),
			// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("getUI().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setUI(ProgressBarUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JProgressBarMessages.getString("setUI(ProgressBarUI).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("setUI(ProgressBarUI).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("progressBarUI", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JProgressBarMessages.getString("setUI(ProgressBarUI).progressBarUI.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
	      			ProgressBarUI.class
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
			// borderPainted
			super.createPropertyDescriptor(getBeanClass(),"borderPainted", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JProgressBarMessages.getString("borderPainted.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("borderPainted.Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// maximum
			super.createPropertyDescriptor(getBeanClass(),"maximum", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JProgressBarMessages.getString("maximum.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("maximum.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// minimum
			super.createPropertyDescriptor(getBeanClass(),"minimum", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JProgressBarMessages.getString("mimimum.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("minimum.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// orientation
			super.createPropertyDescriptor(getBeanClass(),"orientation", new Object[] { //$NON-NLS-1$
	      			DISPLAYNAME, JProgressBarMessages.getString("orientation.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JProgressBarMessages.getString("orientation.Desc"), //$NON-NLS-1$
	      			PREFERRED, Boolean.TRUE,
	      			ENUMERATIONVALUES, new Object[] {
	      				JProgressBarMessages.getString("orientation.HORIZONTAL"), new Integer(javax.swing.JProgressBar.HORIZONTAL), //$NON-NLS-1$
	      				"javax.swing.JProgressBar.HORIZONTAL", //$NON-NLS-1$
	      				JProgressBarMessages.getString("orientation.VERTICAL"), new Integer(javax.swing.JProgressBar.VERTICAL), //$NON-NLS-1$
	      				"javax.swing.JProgressBar.VERTICAL" //$NON-NLS-1$
	    			}
	    		}
	    	),
	    	// value
			super.createPropertyDescriptor(getBeanClass(),"value", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JProgressBarMessages.getString("value.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("value.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE,
	      		PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// string
			super.createPropertyDescriptor(getBeanClass(),"string", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, JProgressBarMessages.getString("string.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("string.Desc"), //$NON-NLS-1$
	      		BOUND, Boolean.TRUE
	    		}
	    	),
	    	// stringPainted - if true the string is shown
	    	super.createPropertyDescriptor(getBeanClass(),"stringPainted", new Object[]{ //$NON-NLS-1$
	    		DISPLAYNAME, JProgressBarMessages.getString("stringPainted.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("stringPainted.Desc") //$NON-NLS-1$
	    		}
	    	),
	    	// model
	    	super.createPropertyDescriptor(getBeanClass(),"model", new Object[]{ //$NON-NLS-1$
	    		DISPLAYNAME, JProgressBarMessages.getString("model.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("model.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
	    		}
	    	),
	    	// ui
	    	super.createPropertyDescriptor(getBeanClass(),"UI", new Object[]{ //$NON-NLS-1$
	    		DISPLAYNAME, JProgressBarMessages.getString("ui.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JProgressBarMessages.getString("ui.Desc"), //$NON-NLS-1$
				EXPERT, Boolean.TRUE,
				BOUND, Boolean.TRUE
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
