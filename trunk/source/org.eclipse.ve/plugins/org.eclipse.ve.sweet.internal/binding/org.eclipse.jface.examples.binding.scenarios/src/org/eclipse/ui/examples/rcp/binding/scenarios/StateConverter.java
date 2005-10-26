/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.examples.rcp.binding.scenarios;

import org.eclipse.jface.binding.IConverter;
 
public class StateConverter implements IConverter {
	
	public final String[] stateLetters = new String[] {
			  "AL","AK","AZ","AR","CA","CO","CT","DE","DC","FL","GA","GU","HI",
			  "ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS",
			  "MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR",
			  "PA","PR","RI","SC","SD","TN","TX","UT","VA","VI","WA","WV","WI" 
	};
	public final String[] stateNames = new String[] {
			"Alabama","Arkansaw","Arizona","??AR??","California","Colombia","Conetticut","Delaware","Washington DC","Florida","Georgia","??GU??","??HI??",
			"Idaho","Illinois","Indiana","??","Kansas","Kentucyky","??LA??","??ME??","??MD??","Michigan","Maryland","Minnesota","??MS??",
			"Montana","Massachutets","??NE??","Nevada","New Hampshire","New Jersey","New Mexico","New York","North Carolina","??ND??","Ohio","Oklahmoa","Oregon",
			"Pensylvania","??PR??","Rhode Island","South Carolina","??SD??","Tenesee","Texas","Utah","Vancouver","Virginia","Washington","West Virginia","Wisconsin"
	};

	public Class getModelType() {
		return String.class;
	}
	public Class getTargetType() {
		return String.class;
	}
	public Object convertTargetToModel(Object targetObject) {
		String stateName = (String)targetObject;
		if(stateName != null){
			for (int i = 0; i < stateNames.length; i++) {
				if(stateNames[i].equalsIgnoreCase(stateName)){
					return stateLetters[i];
				}
			}
		}
		return null;
	}
	public Object convertModelToTarget(Object modelObject) {
		// Return the state name for the letter
		String stateLetter = (String)modelObject;
		if(stateLetter != null){
			for (int i = 0; i < stateLetters.length; i++) {
				if(stateLetters[i].equals(stateLetter)){
					return stateNames[i];
				}
			}
		}
		return "??" + stateLetters + "??";
	}
}