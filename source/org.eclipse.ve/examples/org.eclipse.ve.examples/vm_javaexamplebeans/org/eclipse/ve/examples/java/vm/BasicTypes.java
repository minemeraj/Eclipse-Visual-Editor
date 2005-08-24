/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.examples.java.vm;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class BasicTypes {

	protected Frame fFrame;

	protected Label fStringLabel;
	protected String fString;
	protected Label fintLabel;
	protected int fInt;
	protected Label fIntegerLabel;
	protected Integer fInteger;
	protected Label fbooleanLabel;
	protected boolean fboolean;
	protected Label fBooleanLabel;
	protected Boolean fBoolean;
	protected Label ffloatLabel;
	protected float ffloat;
	protected Label fFloatLabel;
	protected Float fFloat;
	protected long flong;;
	protected Label flongLabel;
	protected Long fLong;
	protected Label fLongLabel;
	protected short fshort;
	protected Label fshortLabel;
	protected Short fShort;
	protected Label fShortLabel;
	protected double fdouble;
	protected Label fdoubleLabel;
	protected Double fDouble;
	protected Label fDoubleLabel;

	public static void main(String[] args) {
		if ( args == null && args.length > 0 ) {
			new BasicTypes();
		} else { 
			new BasicTypes(args[0]);
		}
	}
	
	public BasicTypes(String aString){
		  this();
		  setString(aString);
	}
	public void setString(String aString){
		fString = aString;
		fStringLabel.setText(aString);
		fFrame.pack();
	}
	public String getString(){
		return fString;
	}
	// boolean
	public boolean isbooleanPrim(){
		return fboolean;
	}
	public void setbooleanPrim(boolean aboolean){
		fboolean = aboolean;
		fbooleanLabel.setText("" + aboolean);
		fFrame.pack();
	}
	// Boolean
	public void setBoolean(Boolean aBoolean){
		fBoolean = aBoolean;
		fBooleanLabel.setText(aBoolean.toString());
		fFrame.pack();
	}
	public Boolean getBoolean(){
		return fBoolean;
	}
	// float
	public void setfloatPrim(float afloat){
		ffloat = afloat;
		ffloatLabel.setText(String.valueOf(afloat));
		fFrame.pack();
	}
	public float getfloatPrim(){
		return ffloat;
	}
	// Float
	public void setFloat(Float aFloat){
		fFloat = aFloat;
		fFloatLabel.setText(aFloat.toString());
		fFrame.pack();
	}
	public Float getFloat(){
		return fFloat;
	}
	// long
	public void setlongPrim(long along){
		flong = along;
		flongLabel.setText(String.valueOf(along));
		fFrame.pack();
	}
	public long getlongPrim(){
		return flong;
	}
	// Long
	public void setLong(Long aLong){
		fLong = aLong;
		fLongLabel.setText(aLong.toString());
		fFrame.pack();
	}
	public Long getLong(){
		return fLong;
	}
	// short
	public void setshortPrim(short ashort){
		fshort = ashort;
		fshortLabel.setText(String.valueOf(ashort));
		fFrame.pack();
	}
	public short getshortPrim(){
		return fshort;
	}
	// Short
	public void setShort(Short aShort){
		fShort = aShort;
		fShortLabel.setText(aShort.toString());
		fFrame.pack();
	}
	public Short getShort(){
		return fShort;
	}
	// double
	public void setdoublePrim(double adouble){
		fdouble = adouble;
		fdoubleLabel.setText(String.valueOf(adouble));
		fFrame.pack();
	}
	public double getdoublePrim(){
		return fdouble;
	}
	// Double
	public void setDouble(Double aDouble){
		fDouble = aDouble;
		fDoubleLabel.setText(aDouble.toString());
		fFrame.pack();
	}
	public Double getDouble(){
		return fDouble;
	}
	// int
	public void setintPrim(int anInt){
		fInt = anInt;
		fintLabel.setText(String.valueOf(anInt));
		fFrame.pack();
	}
	public int getintPrim(){
		return fInt;
	}
	// Integer
	public void setInteger(Integer anInteger){
		fInteger = anInteger;
		fIntegerLabel.setText(anInteger.toString());
		fFrame.pack();
	}
	public Integer getInteger(){
		return fInteger;
	}
	
	public BasicTypes(){
		fFrame = new Frame("Test");
		fFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent event){
				fFrame.dispose();
			}
		});
		fFrame.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		// String
		fFrame.add(new Label("String"),constraints);
		fStringLabel = createValueLabel();
		fFrame.add(fStringLabel,constraints);
		
		// int
		constraints.gridy = 1;
		fFrame.add(new Label("int"),constraints);
		fintLabel = createValueLabel();
		fFrame.add(fintLabel,constraints);
	
		// integer
		fFrame.add(new Label("Integer"),constraints);
		fIntegerLabel = createValueLabel();
		fFrame.add(fIntegerLabel,constraints);
		
		// float
		constraints.gridy = 2;
		fFrame.add(new Label("float"),constraints);
		ffloatLabel = createValueLabel();
		fFrame.add(ffloatLabel,constraints);
		// Float
		fFrame.add(new Label("Float"),constraints);
		fFloatLabel = createValueLabel();
		fFrame.add(fFloatLabel,constraints);
		
		// short
		constraints.gridy = 3;
		fFrame.add(new Label("short"),constraints);
		fshortLabel = createValueLabel();
		fFrame.add(fshortLabel,constraints);
		// Short
		fFrame.add(new Label("Short"),constraints);
		fShortLabel = createValueLabel();
		fFrame.add(fShortLabel,constraints);
		
		// long
		constraints.gridy = 4;
		fFrame.add(new Label("long"),constraints);
		flongLabel = createValueLabel();
		fFrame.add(flongLabel,constraints);
		// Long
		fFrame.add(new Label("Long"),constraints);
		fLongLabel = createValueLabel();
		fFrame.add(fLongLabel,constraints);
		
		// double
		constraints.gridy = 5;
		fFrame.add(new Label("double"),constraints);
		fdoubleLabel = createValueLabel();
		fFrame.add(fdoubleLabel,constraints);
		// Double
		fFrame.add(new Label("Double"),constraints);
		fDoubleLabel = createValueLabel();
		fFrame.add(fDoubleLabel,constraints);
		
		// boolean
		constraints.gridy = 6;
		fFrame.add(new Label("boolean"),constraints);
		fbooleanLabel = createValueLabel();
		fFrame.add(fbooleanLabel,constraints);
		// Boolean
		fFrame.add(new Label("Boolean"),constraints);
		fBooleanLabel = createValueLabel();
		fFrame.add(fBooleanLabel,constraints);
		
		fFrame.pack();
		fFrame.setVisible(true);	
	}	
	
	protected Label createValueLabel(){
		Label result = new Label("");
		result.setBackground(Color.gray);
		return result;
	}
	
public void disposeFrame(){
	fFrame.dispose();
}	
}
