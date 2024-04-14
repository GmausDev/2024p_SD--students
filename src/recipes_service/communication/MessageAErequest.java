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

import recipes_service.tsae.data_structures.TimestampMatrix;
import recipes_service.tsae.data_structures.TimestampVector;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class MessageAErequest extends Message implements Serializable{
	private static final long serialVersionUID = 3626351664901270873L;
	private TimestampVector summary;
	private TimestampMatrix ack;

	public MessageAErequest (TimestampVector summary, TimestampMatrix ack){
		this.summary = summary;
		this.ack = ack;
	}
	
	public TimestampVector getSummary(){
		return this.summary;
	}
	public TimestampMatrix getAck(){
		return this.ack;
	}
	
	public MsgType type(){
		return MsgType.AE_REQUEST;
	}
	
	@Override
	public String toString() {
		 String str = "MessageAErequest [session: "+getSessionNumber()+", summary=" + summary;
		 if (ack != null){
			 str += ", ack=" + ack;
		 }
		 return str + "]";
	}

}
