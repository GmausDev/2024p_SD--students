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

package lsim.element.coordinator;

import java.util.ArrayList;
import java.util.List;

import lsim.application.handler.HandlerStartCoordinator;
import lsim.library.api.LSimCoordinator; 
import lsim.library.api.LSimFactory;
import lsim.library.api.LSimLogger;
import lsim.worker.data.WorkerInitAnswer;
//Needed for log system
import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class CoordinatorStartHandler implements HandlerStartCoordinator {
	
	private List<Object> participants;

	@Override
	public void execute(List<WorkerInitAnswer> list_answers_of_init_workers) {
		System.out.println("resposta workers: "+list_answers_of_init_workers);
		
		participants = new ArrayList<Object>();
		for (WorkerInitAnswer wia : list_answers_of_init_workers)
			if (wia.hasReturnValue())
				participants.add(wia.getReturnObject());
		
		LSimCoordinator lsim = LSimFactory.getCoordinatorInstance();
		// add start parameter to workers
		lsim.addStartParamToAllWorkers("participants", participants);
		
		// add init parameter to evaluator init parameters
		lsim.addInitParamToEvaluator("numServers", Integer.valueOf(list_answers_of_init_workers.size()));
	}
}
