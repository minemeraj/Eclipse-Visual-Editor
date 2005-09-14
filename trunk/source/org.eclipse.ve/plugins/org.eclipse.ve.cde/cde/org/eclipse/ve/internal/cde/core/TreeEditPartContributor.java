package org.eclipse.ve.internal.cde.core;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

public interface TreeEditPartContributor {
	
	public class ImageOverlay{
		public ImageData fImageData;
		public Point location;
	}

	ImageOverlay getImageOverlay();

	void appendToText(StringBuffer buffer);
	
}
