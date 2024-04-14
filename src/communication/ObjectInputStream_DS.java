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
import java.io.InputStream;
import java.io.ObjectInputStream;

import recipes_service.activity_simulation.SimulationData;


/**
 * Implements a modification of the ObjectInputStream to simulate failures.
 * 
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class ObjectInputStream_DS {
	private ObjectInputStream in;
	
	public ObjectInputStream_DS(InputStream inStream) throws IOException{
		in = new ObjectInputStream(inStream);
	}

	public Object readObject() throws IOException, ClassNotFoundException{
		if (SimulationData.getInstance().isConnected()){
			return in.readObject();
		}
		in.close();
		throw new IOException("Trying to read from a closed ObjectInputStream_DS");
	}
}
