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
 *  $RCSfile: ScrollPaneBeanInfo.java,v $
 *  $Revision: 1.4 $  $Date: 2005-10-06 15:18:44 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.vm.IBaseBeanInfoConstants;

public class ScrollPaneBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle resscrollpane = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.scrollpane");  //$NON-NLS-1$
	
	
public Class getBeanClass() {
	return java.awt.ScrollPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the ScrollPaneBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.ScrollPane.class);
		aDescriptor.setDisplayName(resscrollpane.getString("ScrollPaneDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(resscrollpane.getString("ScrollPaneSD")); //$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/scrpne32.gif");//$NON-NLS-2$//$NON-NLS-1$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/scrpne16.gif");//$NON-NLS-2$//$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return ( new EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("scrpne32.gif");//$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("scrpne16.gif");//$NON-NLS-1$
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
		  	// doLayout()
			super.createMethodDescriptor(getBeanClass(),"doLayout", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "doLayout()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Lay out the scroll pane",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getHAdjustable()
			super.createMethodDescriptor(getBeanClass(),"getHAdjustable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getHAdjustable()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the Adjustable object for horizontal scrollbar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getHScrollbarHeight()
			super.createMethodDescriptor(getBeanClass(),"getHScrollbarHeight", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getHScrollbarHeight()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollpane.getString("getHScrollbarHieght()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getScrollbarDisplayPolicy()
			super.createMethodDescriptor(getBeanClass(),"getScrollbarDisplayPolicy", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getScrollbarDisplayPolicy()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollpane.getString("getScrollbarDisplayPolicy()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getScrollPosition()
			super.createMethodDescriptor(getBeanClass(),"getScrollPosition", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getScrollPosition()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the child position at viewport origin",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getVAdjustable()
			super.createMethodDescriptor(getBeanClass(),"getVAdjustable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getVAdjustable()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the Adjustable object for vertical scrollbar",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getViewportSize()
			super.createMethodDescriptor(getBeanClass(),"getViewportSize", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getViewportSize()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollpane.getString("getViewportSize()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getVScrollbarWidth()
			super.createMethodDescriptor(getBeanClass(),"getVScrollbarWidth", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getVScrollbarWidth()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollpane.getString("getVScrollbarWidth()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// paramString()
			super.createMethodDescriptor(getBeanClass(),"paramString", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "paramString()",//$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Return string representing scrollpane state",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// printComponents(Graphics)
			super.createMethodDescriptor(getBeanClass(), "printComponents", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "printComponents(Graphics)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Print the component in the scroll pane",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollpane.getString("graphicsParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "the graphics context to use",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.awt.Graphics.class
	      		}   		
		  	),
			// setScrollPosition(Point)
			super.createMethodDescriptor(getBeanClass(), "setScrollPosition", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setScrollPosition(Point)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Scroll to position in child component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollpane.getString("pointParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Point to scroll to",
	      			})	      		
	      		},
	      		new Class[] { 
	      			java.awt.Point.class
	      		}   		
		  	),
			// setScrollPosition(int,int)
			super.createMethodDescriptor(getBeanClass(), "setScrollPosition", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setScrollPosition(int,int)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Scroll to position in child component",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollpane.getString("xParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "x position",
	      			}),
	      			createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, resscrollpane.getString("yParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "y position",
	      			})	      		
	      		},
	      		new Class[] { 
	      			int.class, int.class
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
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// hAdjustable
			super.createPropertyDescriptor(getBeanClass(),"hAdjustable", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollpane.getString("hAdjustableDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollpane.getString("hAdjustableSD"), //$NON-NLS-1$
	    		}
	    	),
			// hScrollbarHeight
			super.createPropertyDescriptor(getBeanClass(),"hScrollbarHeight", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollpane.getString("hScrollbarHeightDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollpane.getString("hScrollbarHeightSD"), //$NON-NLS-1$
	    		}
	    	),
			// layout - discard from the property sheet
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      		DISPLAYNAME, resscrollpane.getString("layout.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resscrollpane.getString("layout.Desc"), //$NON-NLS-1$
	      		DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// scrollbarDisplayPolicy
			super.createPropertyDescriptor(getBeanClass(),"scrollbarDisplayPolicy", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollpane.getString("scrollbarDisplayPolicyDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollpane.getString("scrollbarDisplayPolicySD"), //$NON-NLS-1$
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			resscrollpane.getString("asNeededEnumDN"), new Integer(java.awt.ScrollPane.SCROLLBARS_AS_NEEDED), //$NON-NLS-1$
	      				"java.awt.ScrollPane.SCROLLBARS_AS_NEEDED",//$NON-NLS-1$
	      			resscrollpane.getString("alwaysEnumDN"), new Integer(java.awt.ScrollPane.SCROLLBARS_ALWAYS), //$NON-NLS-1$
	      				"java.awt.ScrollPane.SCROLLBARS_ALWAYS",//$NON-NLS-1$
	      			resscrollpane.getString("neverEnumDN"), new Integer(java.awt.ScrollPane.SCROLLBARS_NEVER), //$NON-NLS-1$
	      				"java.awt.ScrollPane.SCROLLBARS_NEVER",//$NON-NLS-1$

	    			}
	    		}
	    	),
			// scrollPosition
			super.createPropertyDescriptor(getBeanClass(),"scrollPosition", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollpane.getString("scrollPositionDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollpane.getString("scrollPositionSD"), //$NON-NLS-1$
      		DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// vAdjustable
			super.createPropertyDescriptor(getBeanClass(),"vAdjustable", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollpane.getString("vAdjustableDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollpane.getString("vAdjustableSD"), //$NON-NLS-1$
	    		}
	    	),
			// viewportSize
			super.createPropertyDescriptor(getBeanClass(),resscrollpane.getString("viewportSize"), new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resscrollpane.getString("viewportSizeDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollpane.getString("viewportSizeSD"), //$NON-NLS-1$
	    		}
	    	),
			// vScrollbarWidth
			super.createPropertyDescriptor(getBeanClass(),"vScrollbarWidth", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, resscrollpane.getString("vScrollbarWidthDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, resscrollpane.getString("vScrollbarWidthSD"), //$NON-NLS-1$
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
