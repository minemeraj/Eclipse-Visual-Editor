package org.eclipse.swt.widgets.beaninfo;

import java.beans.*;

import org.eclipse.swt.SWT;


public class CTabFolderBeanInfo extends IvjBeanInfo {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.beaninfo.IvjBeanInfo#getBeanClass()
	 */
	public Class getBeanClass() {
		return org.eclipse.swt.custom.CTabFolder.class;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.BeanInfo#getEventSetDescriptors()
	 */
	public EventSetDescriptor[] getEventSetDescriptors() {
		return new EventSetDescriptor[] {
				CTabFolder2ListenerEventSet.getEventSetDescriptor(getBeanClass()),
				SelectionListenerEventSet.getEventSetDescriptor(getBeanClass())
		};
	}
	
	public BeanDescriptor getBeanDescriptor() {
		BeanDescriptor descriptor = new BeanDescriptor(getBeanClass());
		descriptor.setValue(
			SweetHelper.STYLE_BITS_ID,
		    new Object[] [] {
				{ "close" , CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.Close.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.Close.Value.CLOSE") , "org.eclipse.swt.SWT.CLOSE" , new Integer(SWT.CLOSE) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "tabPosition" , CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.TabPosition.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.TabPosition.Value.TOP") , "org.eclipse.swt.SWT.TOP" , new Integer(SWT.TOP) , //$NON-NLS-1$ //$NON-NLS-2$
					CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.TabPosition.Value.BOTTOM") , "org.eclipse.swt.SWT.BOTTOM" , new Integer(SWT.BOTTOM) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "border" , CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.Border.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.Border.Value.BORDER") , "org.eclipse.swt.SWT.BORDER" , new Integer(SWT.BORDER) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "flat" , CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.Flat.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.Flat.Value.FLAT") , "org.eclipse.swt.SWT.FLAT" , new Integer(SWT.FLAT) , //$NON-NLS-1$ //$NON-NLS-2$
				} },
				{ "tabDisplay" , CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.TabDisplay.Name") , Boolean.FALSE ,  new Object[] { //$NON-NLS-1$ //$NON-NLS-2$
				    CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.TabDisplay.Value.MULTI") , "org.eclipse.swt.SWT.MULTI" , new Integer(SWT.MULTI) , //$NON-NLS-1$ //$NON-NLS-2$
					CTabFolderMessages.getString("CTabFolderBeanInfo.StyleBits.TabDisplay.Value.SINGLE") , "org.eclipse.swt.SWT.SINGLE" , new Integer(SWT.SINGLE) , //$NON-NLS-1$ //$NON-NLS-2$
				} }
			}
		);
		return descriptor;
	}	

	/**
	 * Return the property descriptors for this bean.
	 * 
	 * @return java.beans.PropertyDescriptor[]
	 */
	public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
		try {
			if(SWT.getVersion() >= 3100){
				PropertyDescriptor aDescriptorList[] = {
					// maximized
					super.createPropertyDescriptor(getBeanClass(),"maximized", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("maximizedDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("maximizedSD"), //$NON-NLS-1$
					}
					),
					// maximizeVisible
					super.createPropertyDescriptor(getBeanClass(),"maximizeVisible", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("maximizeVisibleDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("maximizeVisibleSD"), //$NON-NLS-1$
					}
					),
					// minimized
					super.createPropertyDescriptor(getBeanClass(),"minimized", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("minimizedDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("minimizedSD"), //$NON-NLS-1$
					}
					),
					// minimizeVisible
					super.createPropertyDescriptor(getBeanClass(),"minimizeVisible", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("minimizeVisibleDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("minimizeVisibleSD"), //$NON-NLS-1$
					}
					),
					// MRUVisible
					super.createPropertyDescriptor(getBeanClass(),"MRUVisible", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("MRUVisibleDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("MRUVisibleSD"), //$NON-NLS-1$
					}
					),
					// selectionBackground
					super.createPropertyDescriptor(getBeanClass(),"selectionBackground", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("selectionBackgroundDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("selectionBackgroundSD"), //$NON-NLS-1$
					}
					),
					// selectionForeground
					super.createPropertyDescriptor(getBeanClass(),"selectionForeground", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("selectionForegroundDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("selectionForegroundSD"), //$NON-NLS-1$
					}
					),
					// simple
					super.createPropertyDescriptor(getBeanClass(),"simple", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("simpleDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("simpleSD"), //$NON-NLS-1$
					}
					),
					// tabHeight
					super.createPropertyDescriptor(getBeanClass(),"tabHeight", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("tabHeightDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("tabHeightSD"), //$NON-NLS-1$
					}
					),
					// unselectedCloseVisible
					super.createPropertyDescriptor(getBeanClass(),"unselectedCloseVisible", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("unselectedCloseVisibleDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("unselectedCloseVisibleSD"), //$NON-NLS-1$
					}
					),
					// unselectedImageVisible
					super.createPropertyDescriptor(getBeanClass(),"unselectedImageVisible", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, CTabFolderMessages.getString("unselectedImageVisibleDN"), //$NON-NLS-1$
						SHORTDESCRIPTION, CTabFolderMessages.getString("unselectedImageVisibleSD"), //$NON-NLS-1$
					}
					),
				};
				return aDescriptorList;
			} else {
				PropertyDescriptor aDescriptorList[] = {
						// maximized
						super.createPropertyDescriptor(getBeanClass(),"maximized", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("maximizedDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("maximizedSD"), //$NON-NLS-1$
						}
						),
						// maximizeVisible
						super.createPropertyDescriptor(getBeanClass(),"maximizeVisible", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("maximizeVisibleDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("maximizeVisibleSD"), //$NON-NLS-1$
						}
						),
						// minimized
						super.createPropertyDescriptor(getBeanClass(),"minimized", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("minimizedDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("minimizedSD"), //$NON-NLS-1$
						}
						),
						// minimizeVisible
						super.createPropertyDescriptor(getBeanClass(),"minimizeVisible", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("minimizeVisibleDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("minimizeVisibleSD"), //$NON-NLS-1$
						}
						),
						// selectionBackground
						super.createPropertyDescriptor(getBeanClass(),"selectionBackground", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("selectionBackgroundDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("selectionBackgroundSD"), //$NON-NLS-1$
						}
						),
						// selectionForeground
						super.createPropertyDescriptor(getBeanClass(),"selectionForeground", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("selectionForegroundDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("selectionForegroundSD"), //$NON-NLS-1$
						}
						),
						// simple
						super.createPropertyDescriptor(getBeanClass(),"simple", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("simpleDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("simpleSD"), //$NON-NLS-1$
						}
						),
						// tabHeight
						super.createPropertyDescriptor(getBeanClass(),"tabHeight", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("tabHeightDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("tabHeightSD"), //$NON-NLS-1$
						}
						),
						// unselectedCloseVisible
						super.createPropertyDescriptor(getBeanClass(),"unselectedCloseVisible", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("unselectedCloseVisibleDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("unselectedCloseVisibleSD"), //$NON-NLS-1$
						}
						),
						// unselectedImageVisible
						super.createPropertyDescriptor(getBeanClass(),"unselectedImageVisible", new Object[] { //$NON-NLS-1$
							DISPLAYNAME, CTabFolderMessages.getString("unselectedImageVisibleDN"), //$NON-NLS-1$
							SHORTDESCRIPTION, CTabFolderMessages.getString("unselectedImageVisibleSD"), //$NON-NLS-1$
						}
						),
					};
					return aDescriptorList;
			}
		} catch (Throwable exception) {
			handleException(exception);
		};
		return null;
	}
	
	protected PropertyDescriptor[] overridePropertyDescriptors(PropertyDescriptor[] pds) {
		PropertyDescriptor[] newPDs = (PropertyDescriptor[]) pds.clone();

		replacePropertyDescriptor(newPDs, "layout", null, new Object[] { //$NON-NLS-1$
				DESIGNTIMEPROPERTY, Boolean.FALSE,});

		return newPDs;
	}
}
