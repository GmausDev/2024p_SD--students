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

import lsim.application.handler.HandlerInitCoordinator;
import lsim.library.api.LSimCoordinator;
import lsim.library.api.LSimFactory;
import lsim.library.api.LSimParameters;

/**
 * @author Joan-Manuel Marques, Sergi Gea
 * July 2013
 *
 */
public class CoordinatorInitHandler implements HandlerInitCoordinator {
	
	LSimParameters params;
	
//	LSimParameters paramsServer;
	
//	public CoordinatorInitHandler(){
//		this.paramsServer = paramsServer;
//	}
	
	@Override
	public void execute(LSimParameters params) {
		this.params = params;
		
//		LSimParameters workerParams;
//		int numWorkersByType;
//		
//		// For each type of worker add the initial parameters
//		for(String worker_type : lsim.getWorkerTypes()){
//			numWorkersByType = lsim.getNumWorkersByType(worker_type);
//			workerParams = lsim.getWorkerParams(worker_type);
//			if (numWorkersByType > 0){
//				for(String instance_name : lsim.getAllWorkersByType(worker_type)){ 
//					// Add initial parameters for this worker instance
//					
		
//		// Add initial parameters to workers
		LSimCoordinator lsim=LSimFactory.getCoordinatorInstance();
		
		lsim.addInitParamToAllWorkers("serverBasePort",params.get("serverBasePort"));
		lsim.addInitParamToAllWorkers("sessionDelay",params.get("sessionDelay"));
		lsim.addInitParamToAllWorkers("sessionPeriod",params.get("sessionPeriod"));
		lsim.addInitParamToAllWorkers("numSes",params.get("numSes"));
		lsim.addInitParamToAllWorkers("propDegree",params.get("propDegree"));
		lsim.addInitParamToAllWorkers("simulationStop",params.get("simulationStop"));
		lsim.addInitParamToAllWorkers("executionStop",params.get("executionStop"));
		lsim.addInitParamToAllWorkers("simulationDelay",params.get("simulationDelay"));
		lsim.addInitParamToAllWorkers("simulationPeriod",params.get("simulationPeriod"));
		lsim.addInitParamToAllWorkers("probDisconnect",params.get("probDisconnect"));
		lsim.addInitParamToAllWorkers("probReconnect",params.get("probReconnect"));
		lsim.addInitParamToAllWorkers("probCreate",params.get("probCreate"));
		lsim.addInitParamToAllWorkers("probDel",params.get("probDel"));
		lsim.addInitParamToAllWorkers("samplingTime",params.get("samplingTime"));
		lsim.addInitParamToAllWorkers("purge",params.get("purge"));
		lsim.addInitParamToAllWorkers("executionMode",params.get("executionMode"));
//				}
//			}
//		}
		
		// Initial parameters	
		// new!!!
//		paramsServer.put("expIdDSLab",params.get("expIdDSLab"));
//		
//		paramsServer.put("groupId",params.get("groupId"));
//		paramsServer.put("serverBasePort",params.get("serverBasePort"));
//		paramsServer.put("sessionDelay",params.get("sessionDelay"));
//		paramsServer.put("sessionPeriod",params.get("sessionPeriod"));
//		paramsServer.put("numSes",params.get("numSes"));
//		paramsServer.put("propDegree",params.get("propDegree"));
//		paramsServer.put("simulationStop",params.get("simulationStop"));
//		paramsServer.put("executionStop",params.get("executionStop"));
//		paramsServer.put("simulationDelay",params.get("simulationDelay"));
//		paramsServer.put("simulationPeriod",params.get("simulationPeriod"));
//		paramsServer.put("probDisconnect",params.get("probDisconnect"));
//		paramsServer.put("probReconnect",params.get("probReconnect"));
//		paramsServer.put("probCreate",params.get("probCreate"));
//		paramsServer.put("probDel",params.get("probDel"));
//		paramsServer.put("samplingTime",params.get("samplingTime"));
//		paramsServer.put("purge",params.get("purge"));
//		paramsServer.put("executionMode",params.get("executionMode"));
//		paramsServer.put("phase",params.get("phase"));
//	
//		return null;
		
		// add parameter to evaluator parameters
		lsim.addInitParamToEvaluator("purge", params.get("purge"));
	}
	
	public String getPurge(){
		return (String) params.get("purge");
	}
}