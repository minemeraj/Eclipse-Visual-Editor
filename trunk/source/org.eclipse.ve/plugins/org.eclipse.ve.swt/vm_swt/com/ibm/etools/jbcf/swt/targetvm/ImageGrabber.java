package com.ibm.etools.jbcf.swt.targetvm;

import java.lang.reflect.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.internal.win32.RECT;
import org.eclipse.swt.widgets.*;

public class ImageGrabber {
	
	static Field eventTableField;
	static Method sendEventMethod;
	
	static{
		try{
			System.loadLibrary("swtprint");
		} catch (UnsatisfiedLinkError error){
			System.out.println("Unable to load swtprint.dll");
			error.printStackTrace();
		}
	}
	// Get the raw printed picture of the Control with the trim and border already filled in with the background color
	public native int[] createPrintedPixels(int hWind, int controlWidth, int controlHeight, int colorHandle);
	
	// This returns the image of the argument which is a combination of its heavyweight trim 
	// combined with any paint event it may have
	public int[] getPixels(Control control) {
		Point size = control.getSize();
		int backgroundHandle = control.getBackground() == null ? 0 : control.getBackground().handle;
		int width = size.x;
		int height = size.y;
		int handle = control.handle;
		return createPrintedPixels(handle,width,height,backgroundHandle);
		
	}
	
	public Image getImage(Control control, boolean includeChildren){
		
		Image myImage = getImage(control);
		// Get the images of all of the children
		if(control instanceof Composite && includeChildren){
			Point clientOrigin = getClientOrigin(control);
			Composite composite = (Composite)control;
			Control[] children = composite.getChildren();
			GC myImageGC = new GC(myImage);
			for (int i = children.length; i > 0; i--) {
				// If the child is not visible then don't try and get its image
				// An example of where this would cause a problem is TabFolder where all the controls
				// for each page are children of the TabFolder, but only the visible one is being shown on the active page
				if(!children[i-1].isVisible()) continue;
				Image childImage = getImage(children[i-1],true);
				Rectangle bounds = childImage.getBounds();
				bounds.toString();
				// Paint the child image on top of our one
				// Its location is within our client area origin, so if it is at 10,10 and our client origin
				// is at 5,5 then we draw at 15,15 on our image
				Point childCorner = children[i-1].getLocation();
				childCorner.x = childCorner.x + clientOrigin.x;
				childCorner.y = childCorner.y + clientOrigin.y;
				myImageGC.drawImage(childImage,childCorner.x,childCorner.y);
			}
			// For Table we need to get the image of the TableColumns
			if(control instanceof Table){
				drawTableHeadings(control, myImageGC);
			}			
			myImageGC.dispose();
		}
		return myImage;
	}
	
	public Image getImage(Control control){
		
		Point size = control.getSize();
		int[] data = getPixels(control);
		ImageData imageData = new ImageData(size.x,size.y,32,new PaletteData(0x00FF0000,0x00FF00,0x00FF));
		imageData.setPixels(0,0,data.length,data,0);		
		// The GC now contains the client area painted into it
		Image image = new Image(control.getDisplay(),imageData);

		// If the control is a composite then we should paint its client area into it
		if(control instanceof Composite && !(control instanceof Group)){			
			Point clientOrigin = getClientOrigin(control);			
			Rectangle clientArea = ((Composite)control).getClientArea();  
		 	
		 	ImageData clientImageData = new ImageData(clientArea.width,clientArea.height,32,new PaletteData(0x00FF0000,0x00FF00,0x00FF));
			Image clientImage = new Image(control.getDisplay(),clientImageData);
			GC gc = new GC(clientImage);
			gc.setBackground(control.getBackground());
			gc.fillRectangle(clientArea);
												
			try {
				Object eventTableObject = getEventTableField().get(control);
				if(eventTableObject != null){
					Event eventArg = new Event();
					eventArg.type = SWT.Paint;
					eventArg.widget = control;
					eventArg.item = control;
					eventArg.gc = gc;
					Object[] args = new Object[] {eventArg};
					getSendEventMethod(eventTableObject).invoke(eventTableObject,args);
					// Now copy the GC for the user draw in the client area onto the GC for the image
					GC imageGC = new GC(image);
					imageGC.drawImage(clientImage,0,0,clientArea.width,clientArea.height,clientOrigin.x,clientOrigin.y,clientArea.width,clientArea.height);
					clientImage.dispose();
					imageGC.dispose();					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			gc.dispose();			
		}

		return image;
		 
	}
	private void drawTableHeadings(Control aTable, GC aGC){

		int hwndHeader = OS.SendMessage (aTable.handle, OS.LVM_GETHEADER, 0, 0);
		RECT rect = new RECT ();
		OS.GetWindowRect (hwndHeader, rect);
		int columnWidth = rect.right - rect.left;
		int columnHeight = rect.bottom - rect.top;
		int[] tableColumnPixelArray = createPrintedPixels(hwndHeader,columnWidth,columnHeight,aTable.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND).handle);
		// Now merge the table columns into the Image for the table
		ImageData imageData = new ImageData(columnWidth,columnHeight,32,new PaletteData(0x00FF0000,0x00FF00,0x00FF));
		// Because the Table can have a trim we need to find the location of the column heading so we can draw its image correctly over the table iimage
		// This is done by getting the client area
		Point tableClientOrigin = getClientOrigin(aTable);				
		imageData.setPixels(0,0,tableColumnPixelArray.length,tableColumnPixelArray,0);		
		// The GC now contains the client area painted into it
		Image image = new Image(aTable.getDisplay(),imageData);
		aGC.drawImage(image,tableClientOrigin.x,tableClientOrigin.y);
		image.dispose();
		
	}
	
	private static Point getClientOrigin(Control aControl){
		Point displayClientOrigin = aControl.toDisplay(0,0);
		Point locationRelativeToDisplay = aControl.getLocation();
		if(aControl.getParent() != null){
			Point parentClientOrigin = aControl.getParent().toDisplay(0,0);
			locationRelativeToDisplay = new Point(
				locationRelativeToDisplay.x + parentClientOrigin.x,
				locationRelativeToDisplay.y + parentClientOrigin.y
			);
		};	
		return new Point(displayClientOrigin.x - locationRelativeToDisplay.x,displayClientOrigin.y - locationRelativeToDisplay.y);
	}	
	private static Field getEventTableField(){
		if(eventTableField == null){
			try{
				eventTableField = Widget.class.getDeclaredField("eventTable");
				eventTableField.setAccessible(true);							
			} catch (Exception exc){
				System.out.println("Unable to get the eventTable field for SWT printing");
				exc.printStackTrace();
			}
		}
		return eventTableField;
	}
	private static Method getSendEventMethod(Object anEventTable){
		if(sendEventMethod == null){
			try{
				sendEventMethod = anEventTable.getClass().getDeclaredMethod("sendEvent",new Class[] {Event.class} );
				sendEventMethod.setAccessible(true);
			} catch (Exception exc){
				System.out.println("Unable to get the send event method for SWT printing");
				exc.printStackTrace();
			}
		}
		return sendEventMethod;
	}
}
