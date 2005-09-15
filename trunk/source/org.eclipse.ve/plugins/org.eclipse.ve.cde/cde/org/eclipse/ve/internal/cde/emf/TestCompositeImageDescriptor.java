package org.eclipse.ve.internal.cde.emf;

import java.util.*;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

import org.eclipse.ve.internal.cde.core.ImageOverlay;


public class TestCompositeImageDescriptor extends CompositeImageDescriptor {
	
	private ImageData fBaseImage;
	private List imageOverlays;

	public TestCompositeImageDescriptor(ImageData baseImage){
		fBaseImage = baseImage;
	}

	protected void drawCompositeImage(int width, int height) {
		drawImage(fBaseImage, 0, 0);
		Point size = getSize();
		
		if(imageOverlays != null){
			Iterator iter = imageOverlays.iterator();
			ImageOverlay overlay = (ImageOverlay)iter.next();
			Point location = overlay.location;
			drawImage(overlay.imageData, size.x - location.x, size.y - location.y);
		}
	}
	
	public void addOverlay(ImageOverlay anOverlay){
		if(imageOverlays == null){
			imageOverlays = new ArrayList(1);
		}
		imageOverlays.add(anOverlay);
	}

	protected Point getSize() {
		return new Point(fBaseImage.width, fBaseImage.height);
	}

}
