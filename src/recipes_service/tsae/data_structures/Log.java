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
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import recipes_service.data.Operation;
//LSim logging system imports sgeag@2017
//import lsim.coordinator.LSimCoordinator;
import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.library.api.LSimLogger;

/**
 * @author Joan-Manuel Marques, Daniel Lázaro Iglesias
 * December 2012
 *
 */
public class Log implements Serializable{
	// Only for the zip file with the correct solution of phase1.Needed for the logging system for the phase1. sgeag_2018p 
//	private transient LSimCoordinator lsim = LSimFactory.getCoordinatorInstance();
	// Needed for the logging system sgeag@2017
//	private transient LSimWorker lsim = LSimFactory.getWorkerInstance();

	private static final long serialVersionUID = -4864990265268259700L;
	/**
	 * This class implements a log, that stores the operations
	 * received  by a client.
	 * They are stored in a ConcurrentHashMap (a hash table),
	 * that stores a list of operations for each member of 
	 * the group.
	 */
	private ConcurrentHashMap<String, List<Operation>> log= new ConcurrentHashMap<String, List<Operation>>();  

	public Log(List<String> participants){
		// create an empty log
		for (Iterator<String> it = participants.iterator(); it.hasNext(); ){
			log.put(it.next(), new Vector<Operation>());
		}
	}

	/**
	 * inserts an operation into the log. Operations are 
	 * inserted in order. If the last operation for 
	 * the user is not the previous operation than the one 
	 * being inserted, the insertion will fail.
	 * 
	 * @param op
	 * @return true if op is inserted, false otherwise.
	 */
	public synchronized boolean add(Operation op){
		// ....
        String hostId = op.getTimestamp().getHostid();
        Timestamp lastTimestamp = this.getLastTimestamp(hostId);
        long timestampDifference = op.getTimestamp().compare(lastTimestamp);
        
        if ((lastTimestamp == null && timestampDifference == 0)
                || (lastTimestamp != null && timestampDifference == 1)) {
            this.log.get(hostId).add(op);
            return true;
        } else {
            return false;
        }
	}

	
	
	/**
	 * @param sum The sum of timestamps to compare against.
	 * @return A list of operations that are newer than the given sum of timestamps.
	 */
	public synchronized List<Operation> listNewer(TimestampVector sum){	
		List<Operation> list = new Vector<Operation>();
		List<String> participants = new Vector<String>(this.log.keySet());

		for (Iterator<String> it = participants.iterator(); it.hasNext(); ){
			String node = it.next();
			List<Operation> operations = new Vector<Operation>(this.log.get(node));
			Timestamp timestampToCompare = sum.getLast(node);

			for (Iterator<Operation> opIt = operations.iterator(); opIt.hasNext(); ) {
				Operation op = opIt.next();
				if (op.getTimestamp().compare(timestampToCompare) > 0) {
					list.add(op);
				}
			}
		}
		return list;
	}
	
	/**
	 * Removes from the log the operations that have
	 * been acknowledged by all the members
	 * of the group, according to the provided
	 * ackSummary. 
	 * @param ack: ackSummary.
	 */
	// JQ REVIEW
	public synchronized void purgeLog(TimestampMatrix ack){
        // TimestampVector minTimestampVector = ack.minTimestampVector();


        // for (Map.Entry<String, List<Operation>> entry : log.entrySet()) {
        //     String participant = entry.getKey();
        //     List<Operation> operations = entry.getValue();
        //     Timestamp lastTimestamp = minTimestampVector.getLast(participant);
  
        //     if (lastTimestamp == null) {
        //         continue;
        //     }

  
        //     for (int i = operations.size() - 1; i >= 0; i--) {
        //         Operation op = operations.get(i);

        //         if (op.getTimestamp().compare(lastTimestamp) < 0) {
        //             operations.remove(i);
        //         }
        //     }
		// }

		String auxKey;
		List<String> keyList = new Vector<String>(this.log.keySet());
		TimestampVector timestampVectorMin = ack.minTimestampVector();
		for(Iterator<String> key = keyList.iterator(); key.hasNext();) {
			auxKey = key.next();
			for (Iterator<Operation> ops = log.get(auxKey).iterator();ops.hasNext();) {
				if(!(timestampVectorMin.getLast(auxKey)== null) && !(ops.next().getTimestamp().compare(timestampVectorMin.getLast(auxKey))>0)) {
					ops.remove();
				}
			}
		}

	}

	/**
	 * equals
	 */
	@Override
	public synchronized boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof Log)) {
            return false;
        }

        Log other = (Log) obj;

        if (this.log == other.log) {
            return true;
        } else if (this.log == null || other.log == null) {
            return false;
        } else {
            return this.log.equals(other.log);
        }
	}

	/**
	 * toString
	 */
	@Override
	public synchronized String toString() {
		String name="";
		for(Enumeration<List<Operation>> en=log.elements();
		en.hasMoreElements(); ){
		List<Operation> sublog=en.nextElement();
		for(ListIterator<Operation> en2=sublog.listIterator(); en2.hasNext();){
			name+=en2.next().toString()+"\n";
		}
	}
		
		return name;
	}
	private Timestamp getLastTimestamp(String hostId) {
        List<Operation> operations = this.log.get(hostId);

        if (operations == null || operations.isEmpty()) {
            return null;
        } else {
            return operations.get(operations.size() - 1).getTimestamp();
        }
    }
}
