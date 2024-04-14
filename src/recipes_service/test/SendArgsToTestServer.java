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

package recipes_service.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import lsim.library.api.LSimParameters;

/**
 * @author Joan-Manuel Marques
 * February 2012
 *
 */

public class SendArgsToTestServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int testServerPort = -1;
		String testServerAddress = "no host";
		
		// params list
//		List<String> params = new Vector<String>();

		// properties
		Properties properties = new Properties();

		try {
               //load a properties file
    		properties.load(new FileInputStream("config.properties"));

			//
			// args
			//
			List<String> argsList = Arrays.asList(args);

			// listening port of TestServer
			testServerPort = Integer.valueOf(args[0]);
			
			// num nodes
			int numHosts = Integer.valueOf(args[1]);
			if (numHosts < 2){
				throw new Exception();
			}

			// percentage of (required) received results prior to perform evaluation 
			int percentageRequiredResults = 50;
			// if -pResults arg
			if (argsList.contains("-pResults")){
				int i = argsList.indexOf("-pResults");
				percentageRequiredResults = Integer.valueOf(args[i+1]);
			}

			// Address where TestServer is hosted 
			testServerAddress = "localhost";
			if (argsList.contains("-h")){
				int i = argsList.indexOf("-h");
				testServerAddress = args[i+1];
			}

			// to indicate if all Servers will run in a single computer
			// or they will run Servers hosted in different computers (or more than one 
			// Server in a single computer but this computer having the same internal and external IP address)
			// * localMode: all Server run in a single computer
			// * remoteMode: Servers running in different computers (or more than one Server in a single computer but
			// 			this computer having the same internal and external IP address)
			String executionMode = properties.getProperty("executionMode");
			if (argsList.contains("--remoteMode")){
				executionMode = "remoteMode";
			}


			//
			// Initial values for TSAE sessions
			//

			// purge:
			//	* true: purge is activated
			//	* false: purge deactivated
			// if --nopurge arg
			String purge = "purge";
			if (("Off").equals(properties.getProperty("purge"))){
				purge = "nopurge"; 
			}
			if (argsList.contains("--nopurge")){
				purge = "nopurge";
			}

			// TSAE timers
			// --remove: if no remove argument, no remove
			boolean removeOperationDeactivated = argsList.contains("--noremove");
//			boolean removeOperationActivated = ("On").equals(properties.getProperty("remove"));


			//
			// create params to send to TestServer
			//
			//LSimParameters paramsServer = new LSimParameters();
			LSimParameters params = new LSimParameters();
			
			params.put("serverBasePort",properties.getProperty("serverBasePort"));
			params.put("sessionDelay",properties.getProperty("sessionDelay"));
			params.put("sessionPeriod",properties.getProperty("sessionPeriod"));
			params.put("numSes",properties.getProperty("numSes"));
			params.put("propDegree",properties.getProperty("propDegree"));
			params.put("simulationStop",properties.getProperty("simulationStop"));
			params.put("executionStop",properties.getProperty("executionStop"));
			params.put("simulationDelay",properties.getProperty("simulationDelay"));
			params.put("simulationPeriod",properties.getProperty("simulationPeriod"));
			params.put("probDisconnect",properties.getProperty("probDisconnect"));
			params.put("probReconnect",properties.getProperty("probReconnect"));
			params.put("probCreate",properties.getProperty("probCreate"));
			
			if (removeOperationDeactivated){
				params.put("probDel","0");
			}else{
				params.put("probDel",properties.getProperty("probDel"));
			}
			params.put("samplingTime",String.valueOf(properties.getProperty("samplingTime")));
			params.put("purge",purge);
			params.put("executionMode",executionMode);

			//params.put("coordinatorLSimParameters", paramsServer);
			ExperimentData experimentData = new ExperimentData();
			experimentData.setParams(params);
			experimentData.setNumNodes(numHosts);
			experimentData.setPercentageRequiredResults(percentageRequiredResults);
			
			Socket socket = new Socket(testServerAddress, testServerPort);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

			TestServerMessage testServerMessage = new TestServerMessage(TestServerMsgType.SET_ARGS, "TSAE", experimentData);
			out.writeObject(testServerMessage);
			
			out.close();
			socket.close();
		}catch (UnknownHostException e) {
			System.err.println("--- SendArgsToTestServer ---> Unknown server: " + testServerAddress);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("--- SendArgsToTestServer ---> IOException Error ");
			e.printStackTrace();
			System.exit(1);
		} catch (Exception e){
			System.err.println("SendArgsToTestServer error. Incorrect arguments");
			System.err.println("arg0: TestServer port");
			System.err.println("arg1: number of server nodes (minimum 2)");
			System.err.println("optional args:");
			System.err.println("\t-h <IP address of TestServer>: IP Address of TestServer [defaul value: localhost]");
			System.err.println("\t-pResults <percentageRequiredResults>: percentage of received results prior to perform evaluation (e.g. 50 means 50%, 75 means 75%). Default value 50%");
			System.err.println("\t--remoteMode: Servers will run in different computers (or more than one Server in a single computer but this computer having the same internal and external IP address)");
			System.err.println("\t--localMode: (default running mode. If no mode is specified it will suppose local mode) all Servers will run in the same computers");
			System.err.println("\t--remoteTestServer: indicates that the TestServer runs in a different computer that Servers");

			// Recipes application
			System.err.println("\t--noremove: deactivates the generation by simulation of operations that remove recipes");

			// TSAE arguments
			System.err.println("\t--nopurge: deactivates purge");

			System.exit(1);
		}
	}
}
