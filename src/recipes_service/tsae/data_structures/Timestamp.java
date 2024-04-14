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

package recipes_service.tsae.data_structures;
import java.io.Serializable;

/**
 * @author Joan-Manuel Marques, Daniel Lázaro Iglesias
 * December 2012
 *
 */
public class Timestamp implements Serializable{
	public static final long NULL_TIMESTAMP_SEQ_NUMBER = -1000;
	/**
	 * 
	 */
	private static final long serialVersionUID = 4178027349883987517L;
	/**
	 * This class represents the timestamp of an operation.
	 * Contains the node that issued the operation, and
	 * the sequence number of the operation, relative
	 * to other operations issued by that node.
	 */
	
	private String hostid;
	private long seqnumber;
	
	public Timestamp(String nodeid, long seqnumber){
		this.hostid = nodeid;
		this.seqnumber = seqnumber;
	}
	public String getHostid() {
		return hostid;
	}
	
	public boolean isNullTimestamp(){
		return seqnumber < 0;
	}
	/**
	 * Compares this timestamp to another one
	 * @param t: timestamp to compare
	 * @return The result of subtracting current sequence number and t sequence number. 
	 * This will result in a positive number if this is newer than t, a negative number 
	 * if this is older than t, and 0 if this and t are equal.
	 */
	public long compare(Timestamp t){
		if(t==null){
			return seqnumber;
		}
		return seqnumber-t.seqnumber;
	}
	
	/**
	 * equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timestamp other = (Timestamp) obj;
		if (hostid == null) {
			if (other.hostid != null)
				return false;
		} else if (!hostid.equals(other.hostid))
			return false;
		if (seqnumber != other.seqnumber)
			return false;
		return true;
	}
	
	/**
	 * toString
	 */
	public String toString(){
		return hostid+":   "+seqnumber;
	}
	
}
