package org.eclipse.ve.examples.java.vm;

import java.awt.Canvas;
import java.awt.Graphics;

public class UsesJPanelForCustomizer_EXPLICIT_FALSE extends Canvas implements UsesJPanelForCustomizer {
	
	/**
	 * Comment for <code>serialVersionUID</code>
	 * 
	 * @since 1.1.0
	 */
	private static final long serialVersionUID = -2987647968206122056L;
	String fTitle;

	
	public String getTitle() {
		return fTitle;
	}
	
	public void setTitle(String title) {
		fTitle = title;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(fTitle != null){
			g.drawString(fTitle,0,getHeight()/2);
		}
	}

}
