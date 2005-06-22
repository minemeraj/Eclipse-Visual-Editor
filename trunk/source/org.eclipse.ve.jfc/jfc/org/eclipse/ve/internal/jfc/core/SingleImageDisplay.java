package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: SingleImageDisplay.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-22 14:53:04 $ 
 */

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class SingleImageDisplay extends Canvas{

    private Image img;
    private String warningMsg = null;
    private int horizontalIncrement = 10;
    private int verticalIncrement = 10;
    private int horizontalPageIncrement, verticalPageIncrement;
    private int xOrigin = 0;
    private int yOrigin = 0;
    private final ScrollBar vBar, hBar;    


    public SingleImageDisplay( Composite shell, int style ){
        super( shell, style );
        vBar = getVerticalBar();
        hBar = getHorizontalBar();
        addListeners();
    }


    public SingleImageDisplay( Composite shell, int style, int hi, int vi ){
        this( shell, style );
        horizontalIncrement = hi;
        verticalIncrement = vi;
    }


    public Point computeSize( int wHint, int hHint, boolean changed ){
        int trim = 0;
        int width = trim;
        int height = trim;
        if( img != null ){
            width += img.getBounds().width+4;	// Allow room for focus border
            height += img.getBounds().height+4;
        }
        else{
            width += 200;
            height += 200;
        }
        if( wHint != SWT.DEFAULT ){
            width = wHint;
        }
        if( hHint != SWT.DEFAULT ){
            height = hHint;
        }
        return new Point( width, height );
    }

    private void addListeners(){
        // Add horizontal scroll listener
        if( hBar != null ){
            hBar.setEnabled( false );
            hBar.setVisible( false );
            hBar.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
                    horizontalScroll();
                }
            });
        }

        // Add vertical scroll listener
        if( vBar != null ){
            vBar.setEnabled( false );
            vBar.setVisible( false );
            vBar.addSelectionListener(new SelectionAdapter(){
				public void widgetSelected(SelectionEvent e) {
                    verticalScroll();
                }
            });
        }
        		
		// Add the focus listener
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				redraw();	// So we have the focus indicator
			}
			
			public void focusLost(FocusEvent e) {
				redraw();	// So we get rid of the focus indicator
			}
		});
        

        // Add paint listener
        addPaintListener( new PaintListener(){
            public void paintControl( PaintEvent pe ){
                paintImage( pe.gc );
            }
        });

        // Add a widget listener that would be notified when the widget is
        // is disposed and would then dispose the currently displayed image
        addDisposeListener(new DisposeListener() {
            public void widgetDisposed(DisposeEvent e) {
                if( img != null && img != IconController.warnIcon && img != IconController.errorIcon && img != IconController.noMemoryIcon ){
                    img.dispose();
                    img = null;
                }
            }
        });

        // Add a resize listener so that the scroll bars can be changed accordingly
        // when a resize occurs
        addControlListener(new ControlAdapter() {
        	private boolean inResize;
			public void controlResized(ControlEvent e) {
                if (!inResize) {
                	inResize = true;
					try {
						resetHorizontalBar();
						resetVerticalBar();
					} finally {
						inResize = false;
					}
					xOrigin = 0;
					yOrigin = 0;
					redraw();
				}
            }
        });

        // Add a key listener to allow scrolling the currently displayed image
        // through the keyboard
        addKeyListener( new KeyScrollListener() );
        
	    // Add this to tell it that we want escape and return, tab, and backtab to continue being sent up
	    // the heirarchy so that the shell can handle them.  
		addTraverseListener(new TraverseListener() {
				public void keyTraversed(TraverseEvent e) {
					if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN ||
						e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
						e.doit = true;
					}
				}
		});         

    }

    
    // Paints the image on the canvas.  Centers the image if it is smaller
    // than the visible area of the canvas
    private void paintImage( GC gc ){
        int cw = getClientArea().width;
        int ch = getClientArea().height;            
    	
        if( img != null ){
            int w = img.getBounds().width;
            int h = img.getBounds().height;
            int width = cw > w ? w : cw-4;
            int height = ch > h ? h : ch-4;
            int cx = cw > w ? 2+(cw - w -4)/2 : 2; 
            int cy = ch > h ? 2+(ch - h -4)/2 : 2;

            gc.drawImage( img, xOrigin, yOrigin, width, height,
                          cx, cy, width, height );
        }
        if (isFocusControl()) {
			Color oldForeground = gc.getForeground();
			//set the border color
			gc.setForeground(ColorConstants.gray);
			//draw the focus rectangle
			int oldLS = gc.getLineStyle();
			gc.setLineStyle(SWT.LINE_DOT);
			gc.drawRectangle(0+2, 0+2, cw-4, ch-4);
			//reset gc to old foreground color
			gc.setForeground(oldForeground);
			gc.setLineStyle(oldLS);
        }        
    }


	/**
	 * Return the message for the selected image.
	 * If it is null, then the image was good.
	 * Otherwise it is a warning.
	 */
	public String getImageMessage() {
		return warningMsg;
	}
	
	/**
	 * Set the image that was selected.
	 * NOTE: This should be called in the UI Thread.
	 */
    public void setImage(final IPath path, final IWorkspaceRoot wsroot){

		warningMsg = null;
    	if (img != null && img != IconController.noMemoryIcon && img != IconController.warnIcon && img != IconController.errorIcon) {
    		img.dispose();
    		img = null;
    	}
    	
    	if (path == null) {
    		redraw();
    		return;
    	}
    		
    	String filename = null;
    	if (wsroot != null) {
    		IResource res = wsroot.findMember(path);
    		if (res != null)
    			filename = res.getLocation().toOSString();
    	} else
    		filename = path.toOSString();
    		
    	if (filename != null) {
    		try {
		        img = new Image(getDisplay(), filename);
    		} catch (SWTException e) {
    			warningMsg = e.getLocalizedMessage();
    			if (warningMsg == null) {
    				// SWT didn't return a msg, so we just put one in
    				warningMsg = JFCMessages.SingleImageDisplay_ImageNotLoaded_WARN_; 
    				JavaVEPlugin.log(e);	// Log it so we can go back and look
    			}
    			img = IconController.warnIcon;
    		} catch (OutOfMemoryError e) {
    			warningMsg = JFCMessages.SingleImageDisplay_OutOfMemory_WARN_; 
    			img = IconController.noMemoryIcon;
    		}
    	}
	        
        resetHorizontalBar();
        resetVerticalBar();
        xOrigin = 0;
        yOrigin = 0;
        redraw();
    }


    private void resetHorizontalBar(){
        if( hBar == null ){
        }
        else if( img == null ){
            hBar.setEnabled( false );
            hBar.setVisible( false );
        }
        else{
            hBar.setSelection( 0 );
            int windowWidth = getClientArea().width;
            int imageWidth = img.getBounds().width;
            if( imageWidth > windowWidth ){
                hBar.setEnabled( true );
                hBar.setVisible( true );
                hBar.setMaximum( imageWidth );
                horizontalPageIncrement = windowWidth - horizontalIncrement;
                hBar.setPageIncrement( horizontalPageIncrement);
                hBar.setIncrement( horizontalIncrement );
                hBar.setThumb( windowWidth );
            }
            else{
                hBar.setEnabled( false );
                hBar.setVisible( false );
            }
        }
    }


    private void resetVerticalBar(){
        if( vBar == null ){
        }
        else if( img == null ){
            vBar.setEnabled( false );
            vBar.setVisible( false );
        }
        else{
            vBar.setSelection( 0 );
            int windowHeight = getClientArea().height;
            int imageHeight = img.getBounds().height;
            if( imageHeight > windowHeight ){
                vBar.setEnabled( true );
                vBar.setVisible( true );
                vBar.setMaximum( imageHeight );
                verticalPageIncrement = windowHeight - verticalIncrement;
                vBar.setPageIncrement( verticalPageIncrement); 
                vBar.setIncrement( verticalIncrement );
                vBar.setThumb( windowHeight );
            }
            else{
                vBar.setEnabled( false );
                vBar.setVisible( false );
            }
        }
    }


    private void horizontalScroll(){
        int temp = hBar.getSelection();
        if( xOrigin != temp && hBar.isEnabled() ){
            xOrigin = temp;
            redraw();
        }
    }

    
    private void verticalScroll(){
        int temp = vBar.getSelection();
        if( temp != yOrigin && vBar.isEnabled() ){
            yOrigin = temp;
            redraw();
        }
    }

    private class KeyScrollListener implements KeyListener{

        public void keyPressed( KeyEvent kee ){
            switch( kee.keyCode ){
                case SWT.ARROW_DOWN: 
                    changeBarSelection( vBar, verticalIncrement );                   
                    break;
                case SWT.ARROW_UP:
                    changeBarSelection( vBar, -verticalIncrement );                   
                    break;
                case SWT.ARROW_RIGHT:
                    changeBarSelection( hBar, horizontalIncrement);                   
                    break;
                case SWT.ARROW_LEFT:
                    changeBarSelection( hBar, -horizontalIncrement);                   
                    break;
				case SWT.PAGE_UP:
					changeBarSelection( vBar, -verticalPageIncrement);
					break;
				case SWT.PAGE_DOWN:
					changeBarSelection( vBar, verticalPageIncrement );
					break;
				case SWT.HOME:
					if( kee.stateMask == SWT.CTRL ){
						scrollTo(hBar, 0);
						scrollTo(vBar, 0);
					} else
						scrollTo(hBar, 0);
					break;
				case SWT.END:
					if( kee.stateMask == SWT.CTRL ){
						scrollTo(hBar, hBar.getMaximum());
						scrollTo(vBar, vBar.getMaximum());						
					}
					else {
						scrollTo(hBar, hBar.getMaximum());
					}
					break;                    
            }
        }

        public void keyReleased( KeyEvent kee ){
        }

        private void changeBarSelection( ScrollBar bar, int increment ){
            if( bar != null && bar.isEnabled() ){
                int selection = bar.getSelection() + increment;
				scrollTo(bar, selection);
            }
        }

		private void scrollTo(ScrollBar bar, int selection) {
			if (selection < 0)
				selection = 0;
			if (selection > bar.getMaximum())
				selection = bar.getMaximum();
			bar.setSelection( selection );				
			if( bar == vBar ){				
			    verticalScroll();
			}
			else if( bar == hBar ){
			    horizontalScroll();
			}
		}
     
    }

}