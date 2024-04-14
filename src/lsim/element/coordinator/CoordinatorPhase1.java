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


import java.io.IOException;

import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.LSimDispatcherHandler;
import lsim.application.ApplicationManager;
import lsim.library.api.LSimCoordinator;
import lsim.library.api.LSimLogger;
import util.Serializer;

/*
* @author Joan-Manuel Marques
* December 2012
*
*/

public class CoordinatorPhase1 implements ApplicationManager{

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(LSimDispatcherHandler disp) {
		// TODO Auto-generated method stub

//		LSimCoordinator lsim=LSimFactory.getCoordinatorInstance();
//		lsim.setDispatcher(disp);

		// set maximum time (minutes) that is expected the experiment will last
//		lsim.setExperimentTime(30);

		// Parameters to initialize the workers moved to the CoordinatorPhase1InitHandler. paramsServer removed. sgeag_2018
		//LSimParameters paramsServer = new LSimParameters();
		//lsim.addInitParam("Wphase10","coordinatorLSimParameters",paramsServer);

		// init coordinator and workers
		//CoordinatorPhase1InitHandler init=new CoordinatorPhase1InitHandler(paramsServer);
		CoordinatorPhase1InitHandler init=new CoordinatorPhase1InitHandler();
//		lsim.init(init);
		LSimCoordinator lsim = LSimCoordinator.init(disp, init);

		// start workers
		lsim.start();

		// stop
		lsim.stop();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
