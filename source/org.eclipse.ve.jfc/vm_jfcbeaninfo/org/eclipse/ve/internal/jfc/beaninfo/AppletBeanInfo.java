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
 *  $RCSfile: AppletBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.ParameterDescriptor;

public class AppletBeanInfo extends IvjBeanInfo {
	
private static java.util.ResourceBundle resApplet = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.applet");  //$NON-NLS-1$
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return java.applet.Applet.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, resApplet.getString("BeanInfo.Applet.BeanDesc.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.BeanDesc.Desc") //$NON-NLS-1$
						}			    
				  	  );
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return( new java.beans.EventSetDescriptor[0]);
}
	/**
	 * @return an icon of the specified kind for JButton
	 */
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("applet32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("applet16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// destroy()
			super.createMethodDescriptor(getBeanClass(),"destroy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Destroy.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.Destroy.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getAudioClip(URL,String)
			super.createMethodDescriptor(getBeanClass(),"getAudioClip",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetAudioClip1.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the audio clip at URL",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.GetAudioClip1.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Base location of audio clip",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.GetAudioClip1.arg2.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "name of audio clip",
	      			}
	      		)},
	      	new Class[] {
	      		java.net.URL.class,
	      		String.class
	      		}		    		
		  	),
			// getAudioClip(URL)
			super.createMethodDescriptor(getBeanClass(),"getAudioClip",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetAudioClip2.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the audio clip at URL",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.GetAudioClip2.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Location of audio clip",
	      			}
	      		)},
	      	new Class[] {
	      		java.net.URL.class
	      		}		    		
		  	),
		  	// getAppletContext()
			super.createMethodDescriptor(getBeanClass(),"getAppletContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetAppletContext.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get browser/viewer running applet",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// getAppletInfo()
			super.createMethodDescriptor(getBeanClass(),"getAppletInfo",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetAppletInfo.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get text to display in About dialog",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getCodeBase()
			super.createMethodDescriptor(getBeanClass(),"getCodeBase",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetCodeBase.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.GetCodeBase.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getDocumentBase()
			super.createMethodDescriptor(getBeanClass(),"getDocumentBase",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetDocumentBase.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.GetDocumentBase.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getImage(URL,String)
			super.createMethodDescriptor(getBeanClass(),"getImage",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetImage1.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the image at URL",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.GetImage1.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Base location of image",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.GetImage1.arg2.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "name of image",
	      			}
	      		)},
	      	new Class[] {
	      		java.net.URL.class,
	      		String.class
	      		}		    		
		  	),
		   // getImage(URL)
		   super.createMethodDescriptor(getBeanClass(),"getImage",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetImage2.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the image at URL",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.GetImage2.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Location of image",
	      			}
	      		)},
	      	new Class[] {
	      		java.net.URL.class
	      		}		    		
		  	),
		  	// getLocale()
			super.createMethodDescriptor(getBeanClass(),"getLocale",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetLocale.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the applet locale",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getParameter(String)
			super.createMethodDescriptor(getBeanClass(),"getParameter",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetParameter.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.GetParameter.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.GetParameter.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Parameter name",
	      			}
	      		)},
	      	new Class[] {
	      		String.class
	      		}		    		
		  	),
			// getParameterInfo()
			super.createMethodDescriptor(getBeanClass(),"getParameterInfo",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.GetParameterInfo.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get string array describing parameters",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
		  	// init()
		  	super.createMethodDescriptor(getBeanClass(),"init",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Init.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.Init.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// isActive()
			super.createMethodDescriptor(getBeanClass(),"isActive",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.IsActive.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Check if the applet is active",
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// play(URL,String)
			super.createMethodDescriptor(getBeanClass(),"play",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Play1.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "play the audio at URL",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.Play1.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Base location of audio",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.Play1.arg2.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "name of audio",
	      			}
	      		)},
	      	new Class[] {
	      		java.net.URL.class,
	      		String.class
	      		}		    		
		  	),
			// play(URL)
			super.createMethodDescriptor(getBeanClass(),"play",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Play2.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.Play2.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.Play2.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Base location of audio",
	      			}
	      		)},
	      	new Class[] {
	      		java.net.URL.class
	      		}		    		
		  	),
			// resize(int,int)
			super.createMethodDescriptor(getBeanClass(),"resize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Resize1.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "resize the applet",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.Resize1.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "new width of applet",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.Resize1.arg2.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "new height of applet",
	      			}
	      		)},
	      	new Class[] {
	      		int.class,
	      		int.class
	      		}		    		
		  	),
			// resize(Dimension)
			super.createMethodDescriptor(getBeanClass(),"resize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Resize2.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "resize the applet",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.Resize2.arg2.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "new dimension of applet",
	      			}
	      		)},
	      	new Class[] {
	      		java.awt.Dimension.class
	      		}		    		
		  	),
			// setStub(AppletStub)
			super.createMethodDescriptor(getBeanClass(),"setStub",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.SetStub.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Sets this applet's stub.",
	      		HIDDEN, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("appletStub", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.SetStub.AppletStub.Name"), //$NON-NLS-1$
	      			}
	      		)},
	      		new Class[] {
	      			java.applet.AppletStub.class
	      		}		    		
		  	),
			// showStatus(String)
			super.createMethodDescriptor(getBeanClass(),"showStatus",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.ShowStatus.Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Display string in status window",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, resApplet.getString("BeanInfo.Applet.ParamDesc.ShowStatus.arg1.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "String to display",
	      			}
	      		)},
	      	new Class[] {
	      		String.class
	      		}		    		
		  	),
			// start()
			super.createMethodDescriptor(getBeanClass(),"start",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Start.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.Start.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// stop()
			super.createMethodDescriptor(getBeanClass(),"stop",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resApplet.getString("BeanInfo.Applet.MthdDesc.Stop.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.MthdDesc.Stop.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
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
			// active
			super.createPropertyDescriptor(getBeanClass(),"active", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resApplet.getString("PropDesc.active.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resApplet.getString("PropDesc.active.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// appletContext
			super.createPropertyDescriptor(getBeanClass(),"appletContext", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resApplet.getString("PropDesc.appletContext.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.PropDesc.AplletContext.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE,
			DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// appletInfo
			super.createPropertyDescriptor(getBeanClass(),"appletInfo", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resApplet.getString("PropDesc.appletInfo.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.PropDesc.AppletInfo.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// codeBase
			super.createPropertyDescriptor(getBeanClass(),"codeBase", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resApplet.getString("PropDesc.codeBase.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.PropDesc.CodeBase.Desc"), //$NON-NLS-1$
			DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// documentBase
			super.createPropertyDescriptor(getBeanClass(),"documentBase", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resApplet.getString("PropDesc.documentBase.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.PropDesc.DocumentBase.Desc"), //$NON-NLS-1$
			DESIGNTIMEPROPERTY, Boolean.FALSE
	    		}
	    	),
			// parameterInfo
			super.createPropertyDescriptor(getBeanClass(),"parameterInfo", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resApplet.getString("PropDesc.parameterInfo.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resApplet.getString("BeanInfo.Applet.PropDesc.ParameterInfo.Desc"), //$NON-NLS-1$
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
