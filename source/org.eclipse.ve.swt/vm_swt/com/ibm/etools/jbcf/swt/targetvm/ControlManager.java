package com.ibm.etools.jbcf.swt.targetvm;

import java.io.DataOutputStream;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.jem.internal.proxy.common.*;

public class ControlManager implements ICallback , ControlListener {
	
	public static final int 
		CO_RESIZED = 1,
		CO_MOVED = 2,
		CO_REFRESHED = 3,
		IMAGE_INITIAL_LENGTH = 4,
		IMAGE_FINISHED = 5;
	
	protected Control fControl;
	protected IVMServer fServer;
	protected int fCallbackID;	

	public void initializeCallback(IVMServer vmServer, int callbackID) {
		fServer = vmServer;
		fCallbackID = callbackID;
	}
	
	public void setControl(Control aControl) {
		if (fControl != null) {
			fControl.removeControlListener(this);
		}
		fControl = aControl;
		if (fControl != null) {
			fControl.addControlListener(this);
			// Queue up a refresh to get the bounds, even if not yet showing, get something at least.
			queueRefresh();	
		}
	}	
	
	/**
	 * Call back the IDE VM
	 * Do not  queue up onto the SWT display thread because this then calls back the display
	 * and the echo creates a deadlock, so instead just wait for the display thread to become empty
	 * and then callback the IDE
	 */
	private void queueRefresh() {

		Environment.display.syncExec(new Runnable() {
			public void run() {
				// Wait for the display thread to be empty of all sync runnables				
			}
		});

		// This is different behavior to Swing that queues on the event queue
		// and we cannot use Dispaly.sync or async here because of the deadlock caused by the echo
		if (fServer != null) {
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, CO_REFRESHED, null);						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}
	
	public void controlMoved(final ControlEvent e) {
		final Point overallLocation = getLocation();		
		if (fServer != null) {
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						return handler.callbackWithParms(fCallbackID, CO_MOVED, new Object[] {new Integer(overallLocation.x) , new Integer(overallLocation.y)});						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}

	public void controlResized(final ControlEvent e) {
		if (fServer != null) {
			try {
				fServer.doCallback(new ICallbackRunnable() {
					public Object run(ICallbackHandler handler) throws CommandException {
						Control c = (Control)e.widget;
						return handler.callbackWithParms(fCallbackID, CO_RESIZED, new Object[] {new Integer(c.getBounds().width), new Integer(c.getBounds().height)});						
					}
				});
			} catch (CommandException exp) {
			}
		}
	}
	/**
	 * The bounds of the control expressed relative to the parent bounds origin rather than the
	 * origin of the client area  
	 */
	public Rectangle getBounds(){
		final Rectangle[] bounds = new Rectangle[1];
		Environment.display.syncExec(new Runnable(){
			public void run(){
				bounds[0] = fControl.getBounds();
				if(fControl.getParent() != null){
					Point parentClientOrigin = getClientOrigin(fControl.getParent());
					bounds[0].x += parentClientOrigin.x;
					bounds[0].y += parentClientOrigin.y;
				}
			}
		});
		return bounds[0];
	}
	/**
	 * The size of the client area expressed as a box whose top left corner is relative to its bounds
	 */
	public Rectangle getClientBox(){
		final Rectangle[] clientBox = new Rectangle[1];
		Environment.display.syncExec(new Runnable(){
			public void run(){
				clientBox[0] = ((Composite)fControl).getClientArea();
				// The width and height are good, but the location of the client area is always 0,0 and not expressed
				// as relative to the bounds of the composite itself
				Point clientOrigin = getClientOrigin(fControl);
				clientBox[0].x = clientOrigin.x;
				clientBox[0].y = clientOrigin.y;
			}
		});
		return clientBox[0];
	}	
	private Point getClientOrigin(Control aControl){
		Point displayClientOrigin = aControl.toDisplay(0,0);
		Point locationRelativeToDisplay = aControl.getParent() == null ?
			aControl.getLocation() : aControl.getParent().toDisplay(aControl.getLocation()); 
		return new Point(displayClientOrigin.x - locationRelativeToDisplay.x,displayClientOrigin.y - locationRelativeToDisplay.y);
	}
	/**
	 * The location of the control returned is adjusted so it is relative to the bounds
	 */
	public Point getLocation(){
		return getOverallLocation();			
	}
	/**
	 * Return the location of the control relative to the parent's bounds corner
	 */
	private Point getOverallLocation(){
		
 		final Point[] location = new Point[1];
		Environment.display.syncExec(new Runnable(){
			public void run(){
				// If the control has no parent then just return its location
				if(fControl.getParent() == null){
					location[0] = fControl.getLocation();
				} else {
					Control parentControl = fControl.getParent();
					Point parentClientOrigin = getClientOrigin(parentControl);
					Point controlLocation = fControl.getLocation();
					location[0] = new Point(parentClientOrigin.x + controlLocation.x,parentClientOrigin.y + controlLocation.y);					
				}
			}
		});
		return location[0];	
	}
	public void captureImage(){
		Environment.display.syncExec(new Runnable(){
			public void run(){
				try{
					final DataOutputStream outputStream = new DataOutputStream(fServer.requestStream(fCallbackID,0));
					//----					
					ImageCapture grabber = new ImageCapture();
					//----
					Image image = grabber.getImage(fControl,true);
					final ImageData imageData = image.getImageData();
					byte[] data = imageData.data;					
					final int[] pixels = new int[data.length];
					for(int i = 0; i < data.length; i++){
						pixels[i] = data[i];
					}					
					fServer.doCallback(new ICallbackRunnable(){
						public Object run(ICallbackHandler handler) throws CommandException{
							return handler.callbackWithParms(fCallbackID,IMAGE_INITIAL_LENGTH,
								new Object[]{new Integer(pixels.length), new Integer(imageData.width), new Integer(imageData.height)});
						}
					});
					for (int i = 0; i < pixels.length; i++) {
						outputStream.write( pixels[i]);		
					}
//					outputStream.write(pixels);
					outputStream.close();
					fServer.doCallback(new ICallbackRunnable(){
						public Object run(ICallbackHandler handler) throws CommandException{
							return handler.callbackWithParms(fCallbackID,IMAGE_FINISHED,null);
						}
					});
				} catch (Exception exc){
					exc.printStackTrace();	
				}
			}
		});
	}
}
