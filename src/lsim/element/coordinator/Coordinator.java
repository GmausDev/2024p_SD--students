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

import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.LSimDispatcherHandler;
import lsim.application.ApplicationManager;
import lsim.library.api.LSimCoordinator;

/**
* @author Joan-Manuel Marques
* December 2012
*
*/
public class Coordinator implements ApplicationManager{

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(LSimDispatcherHandler disp) {

		// Initial parameters		
		//LSimParameters paramsServer = new LSimParameters();

		// add init params to server 
		// Now the params to server are added from the CoordinatorInitHandlear sgeag_2017
//		lsim.addInitParam("Wserver_0","coordinatorLSimParameters",paramsServer);
//		lsim.addInitParam("Wserver_1","coordinatorLSimParameters",paramsServer);
//
//		
//		// add init params to serverSD
		// Now the params to serverSD are added from the CoordinatorInitHandlear sgeag_2017
//		lsim.addInitParam("WserverSD_0","coordinatorLSimParameters",paramsServer);
//		lsim.addInitParam("WserverSD_1","coordinatorLSimParameters",paramsServer);
//		lsim.addInitParam("WserverSD_2","coordinatorLSimParameters",paramsServer);
		//+++++++++++++++++++++++++++++++++
		
		// init coordinator
		//CoordinatorInitHandler init=new CoordinatorInitHandler(paramsServer);
		CoordinatorInitHandler init=new CoordinatorInitHandler();
		// InitHandler init=new InitHandler(lsim,30);
		LSimCoordinator lsim = LSimCoordinator.init(disp, init);

		// start workers
		CoordinatorStartHandler startHandler = new CoordinatorStartHandler();
		lsim.start(startHandler);
		
		// stop
		lsim.stop();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
