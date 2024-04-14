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

/**
 * 
 */
package recipes_service.communication;

import java.io.Serializable;

/**
 * @author Joan-Manuel Marques
 * December 2012
 */
public class Host implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1672552888208355506L;
	private String address;
	private int port;
	private String id;

	public Host(String address, int port){
		this.address = address;
		this.port = port;
		this.id = address + ":" + String.valueOf(port);
	}
	public Host(String address, int port, String id){
		this.address = address;
		this.port = port;
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public String getAddress() {
		return address;
	}
	public int getPort() {
		return port;
	}
	
	
	public String toString(){
		return "[" + address + "," + port + "," + id + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Host other = (Host) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (port != other.port)
			return false;
		return true;
	}
}
