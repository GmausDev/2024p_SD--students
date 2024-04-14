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

package recipes_service.data;

import java.io.Serializable;

import recipes_service.tsae.data_structures.Timestamp;

/**
 * @author Joan-Manuel Marques
 * February 2013
 *
 */
public abstract class Operation implements Serializable{

	private static final long serialVersionUID = -591830258037667352L;
	
	Timestamp timestamp;
	
	public Operation(Timestamp ts){
		this.timestamp = ts;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	public abstract OperationType getType();
}
