/*
* Copyright (c) Joan-Manuel Marques 2013. All rights reserved.
* DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
*
* This file is part of the practical assignment of Distributed Systems course.
*
* This code is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This code is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this code.  If not, see <http://www.gnu.org/licenses/>.
*/

package recipes_service.communication;

import java.io.Serializable;


/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public abstract class Message implements Serializable{
	/**
	 * 
	 */
	private int session_number = -1;
	
	private static final long serialVersionUID = -2818439733708345266L;

	public void setSessionNumber(int session_number){
		this.session_number = session_number;
	}
	
	public int getSessionNumber(){
		return session_number;
	}
	
	public abstract MsgType type();
}
