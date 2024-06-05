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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;

/**
 * @author Joan-Manuel Marques, Daniel Lázaro Iglesias
 * December 2012
 *
 */
public class TimestampMatrix implements Serializable{
	
	private static final long serialVersionUID = 3331148113387926667L;
	ConcurrentHashMap<String, TimestampVector> timestampMatrix = new ConcurrentHashMap<String, TimestampVector>();
	
	public TimestampMatrix(List<String> participants){
		// create and empty TimestampMatrix
		for (Iterator<String> it = participants.iterator(); it.hasNext(); ){
			timestampMatrix.put(it.next(), new TimestampVector(participants));
		}
	}
	
	/**
	 * Represents a matrix of timestamps.
	 * This class provides methods to manipulate and access the timestamps in the matrix.
	 */
	private TimestampMatrix() {
    }

	/**
	 * Represents a vector of timestamps associated with a node.
	 * This class is used in the TimestampMatrix to store and retrieve timestamps for each node.
	 */
	/**
	 * Retrieves the TimestampVector associated with the specified node.
	 *
	 * @param node the node for which to retrieve the TimestampVector
	 * @return the TimestampVector associated with the specified node
	 */
	TimestampVector getTimestampVector(String node){
		
		return timestampMatrix.get(node);
	}
	

	/**
	 * Updates the maximum value in the current TimestampMatrix with the corresponding values from the given TimestampMatrix.
	 * 
	 * @param tsMatrix The TimestampMatrix containing the values to update the maximum with.
	 */
	public synchronized void updateMax(TimestampMatrix tsMatrix){
        for (Map.Entry<String, TimestampVector> entry : tsMatrix.timestampMatrix.entrySet()) {
            String key = entry.getKey();
            TimestampVector otherValue = entry.getValue();


			// TimestampVector thisValue = getTimestampVector(key);

            TimestampVector thisValue = this.timestampMatrix.get(key);
            if (thisValue != null) {
                thisValue.updateMax(otherValue);
            }
        }
	}
	

	/**
	 * Updates the timestamp vector for a given node in the timestamp matrix.
	 *
	 * @param node the node for which to update the timestamp vector
	 * @param tsVector the new timestamp vector to be associated with the node
	 */
	public synchronized void update(String node, TimestampVector tsVector){
		this.timestampMatrix.put(node, tsVector);
	}
	/**
	 * 
	 * @return a timestamp vector containing, for each node, 
	 * the timestamp known by all participants
	 */
	public TimestampVector minTimestampVector(){

		TimestampVector min = null;		
		for (Iterator<String> it = timestampMatrix.keySet().iterator(); it.hasNext(); ){
			String node = it.next();
			if (min == null) {
				min = timestampMatrix.get(node).clone();
			} else {
				min.mergeMin(timestampMatrix.get(node));
			}
		}
		return min;
	}
	

	/**
	* Clone
	 */
	public syncTimestampMatrix clone(){
        TimestampMatrix matrix = new TimestampMatrix();

        for (Map.Entry<String, TimestampVector> entry : timestampMatrix.entrySet()) {
            matrix.timestampMatrix.put(entry.getKey(), entry.getValue().clone());
        }
		return matrix;
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
		TimestampMatrix other = (TimestampMatrix) obj;
		return other.timestampMatrix.equals(timestampMatrix);
	}

	
	/**
	 * toString
	 */
	@Override
	public synchronized String toString() {
		String all="";
		if(timestampMatrix==null){
			return all;
		}
		for(Enumeration<String> en=timestampMatrix.keys(); en.hasMoreElements();){
			String name=en.nextElement();
			if(timestampMatrix.get(name)!=null)
				all+=name+":   "+timestampMatrix.get(name)+"\n";
		}
		return all;
	}
}
