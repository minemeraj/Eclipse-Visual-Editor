package org.eclipse.ve.examples.java.vm;

import java.awt.Canvas;
import java.awt.Graphics;

public class UsesJPanelForCustomizer_EXPLICIT_FALSE extends Canvas implements UsesJPanelForCustomizer {
	
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
