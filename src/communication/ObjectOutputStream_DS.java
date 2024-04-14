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

package communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import recipes_service.activity_simulation.SimulationData;


/**
 * Implements a modification of the ObjectOutputStream to simulate failures.
 * 
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class ObjectOutputStream_DS {
	private ObjectOutputStream out;
	
	public ObjectOutputStream_DS(OutputStream outStream) throws IOException{
		this.out = new ObjectOutputStream(outStream);
	}

	public void writeObject(Object obj) throws IOException{
		if (SimulationData.getInstance().isConnected()){
			out.writeObject(obj);
		} else {
			out.close();
			throw new IOException("Trying to write into a closed ObjectOutputStream_DS");
		}
	}
}
