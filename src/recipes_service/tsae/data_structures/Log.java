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
		// String idHost = op.getTimestamp().getHostid();
		// List<Operation> operationList = log.get(idHost);
		// Timestamp last;
		// if (!operationList.isEmpty())
		// {
		// 	last = operationList.get(operationList.size()-1).getTimestamp();
		// }else
		// {
		// 	last= null;
		// }
		
		// if (op.getTimestamp().compare(last)<0)
		// {
		// 	return false;
		// }else
		// {
		// 	log.get(idHost).add(op);
		// 	return true;				
		// }
		List<Operation> principalLog = log.get(op.getTimestamp().getHostid());
		if (principalLog.size() > 0) {
			Operation lastOp = principalLog.get(principalLog.size() - 1);
			if (lastOp.getTimestamp().compare(op.getTimestamp()) >= 0) {
				return false;
			}
		}
		principalLog.add(op);
		log.put(op.getTimestamp().getHostid(), principalLog);
		return true;
	}
	
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
	public synchronized void purgeLog(TimestampMatrix ack){

		List<String> participants = new Vector<String>(this.log.keySet());
		TimestampVector min = ack.minTimestampVector();
		for (Iterator<String> it = participants.iterator(); it.hasNext(); ){
			String node = it.next();
			for (Iterator<Operation> opIt = log.get(node).iterator(); opIt.hasNext();) {
				if (min.getLast(node) != null && opIt.next().getTimestamp().compare(min.getLast(node)) <= 0) {
					opIt.remove();
				}
			}
		}
	}

	/**
	 * equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null )
		{
			return false;
		}
		if(this == obj)
		{
			return true;
		}
		if(getClass()!=obj.getClass())
		{
			return false;
		}
		Log newLog=(Log) obj;

		return log.equals(newLog.log);
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
}
