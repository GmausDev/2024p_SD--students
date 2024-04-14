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

package recipes_service;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.LSimDispatcherHandler;
import lsim.application.ApplicationManager;
import lsim.application.handler.InitHandlerWorkerGetParams;
import lsim.library.api.LSimLogger;
import lsim.library.api.LSimWorker;
import lsim.library.api.LSimParameters;
import recipes_service.data.Operation;
import recipes_service.data.Recipes;
import recipes_service.test.ServerResult;
import recipes_service.tsae.data_structures.Log;
import recipes_service.tsae.data_structures.TimestampMatrix;
import recipes_service.tsae.data_structures.TimestampVector;
import util.Serializer;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class Phase1 implements ApplicationManager {

	// Needed for the log system
//	private static LSimWorker lsim = LSimFactory.getWorkerInstance();

	public static void main (String[] args){
		

		// Configure the local logger manager for this server sgeag_2018p
//		lsim.setIdent("phase1");
//		lsim.setLoggerManager(new edu.uoc.dpcs.LSimLogger.logger.StoreLocalFileLoggerManager());
//		lsim.setLocalLoggerManager("phase1", new edu.uoc.dpcs.LSimLogger.logger.StoreLocalFileLoggerManager());
		
		LSimLogger.setLoggerAsLocalLogger("Phase_1", "../lsimLogs", true);
		
		// remote node 
	    String phase1TestServerAddress = "localhost";
		int phase1TestServerPort = 39825;

		String groupId = null;
		
		try {
			List<String> argsList = Arrays.asList(args);

			if (argsList.contains("-h")){
				int i = argsList.indexOf("-h");
				phase1TestServerAddress = args[i+1];
			}

			// Phase1TestServerPort
			phase1TestServerPort = Integer.parseInt(args[0]);
			
			// groupId
			groupId = args[1];

		} catch (Exception e){
			System.err.println("Server error. Incorrect arguments");
			System.err.println("arg0: port (phase 1 test server port)");
			System.err.println("arg1: group id");
			System.err.println("optional args:");
			System.err.println("\t-h <IP address of Phase1TestServer>: IP Address of Phase1TestServer");
//			System.exit(1);
			return;
		}

		// Obtain list of operations
    	List<Operation> operations = null;
    	List<String> users = null;
        try {
        	System.out.println("Phase1.java -- Connecting to "+phase1TestServerAddress+":"+phase1TestServerPort);
        	Socket socket = new Socket(phase1TestServerAddress, phase1TestServerPort);
        	ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        	ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        	// get initialization information from TestServer
        	// (initialization is done using a WorkerInitHandler to maintain consistency with LSim mode of execution)
        	// get users and operations
    		try {
         		LSimParameters params = (LSimParameters) Serializer.deserialize((byte []) in.readObject());
    			users = (List<String>) params.get("users");
    			operations = (List<Operation>) Serializer.deserialize((byte[]) params.get("operations"));
    		} catch (ClassNotFoundException | IOException e) {
    			e.printStackTrace();
    		}

        	LSimLogger.log(Level.INFO, "List of users: " + users.toString());
        	LSimLogger.log(Level.INFO, "List of operations:\n" + operations.toString());

        	// apply operations locally
        	Log log = new Log(users);
        	TimestampVector summary = new TimestampVector(users);
        	for (int i=0; i<operations.size(); i++){
        		log.add(operations.get(i));
        		LSimLogger.log(Level.TRACE, "Log updated:\n" +log.toString());
        		summary.updateTimestamp(operations.get(i).getTimestamp());
        		LSimLogger.log(Level.TRACE, "Summary updated:\n" + summary.toString());
        	}

            LSimLogger.log(Level.INFO, "Log:\n" + log.toString());
            LSimLogger.log(Level.INFO, "Summary:\n" + summary.toString());

        	// send result to localTestServer
        	ServerResult serverResult = new ServerResult(null, new Recipes(), log, summary, null);
            out.writeObject(Serializer.serialize(serverResult));
            out.close();
            in.close();
            socket.close();
        } catch (UnknownHostException e) {
            System.err.println("Phase1.java -- Unknown server: " + phase1TestServerAddress);
//            System.exit(1);
        } catch (IOException e) {
            System.err.println("Phase1.java -- Couldn't get I/O for "
                               + "the connection to: " + phase1TestServerAddress);
//            System.exit(1);
        }
             
        System.out.println("Phase1.java -- Execution finished");
//		System.exit(0);
	}

	
	// -------------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------------------

	
	// ****************************************************
	// ****************************************************
	// ******** LSIM methods. 
	// ****************************************************
	// ****************************************************
	
	// From this point to the end of the file is only used when
	// deployed at DSLab. NOT WHEN RUNNING IN LOCAL
	

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
	public void start(LSimDispatcherHandler dispatcher) {
		// TODO Auto-generated method stub
		try{
			process(dispatcher);
		}catch(RuntimeException e){
//			LSimFactory.getEvaluatorInstance().logException(new LSimExceptionMessage(null, e, null));
		}
	}

	private void process(LSimDispatcherHandler dispatcher){
//		LSimWorker lsim = LSimFactory.getWorkerInstance();
//		lsim.setDispatcher(dispatcher);

		// set maximum time (minutes) that is expected the experiment will last
//		lsim.setExperimentTime(30);
		
	
		// ------------------------------------------------
		// init
		// ------------------------------------------------

    	InitHandlerWorkerGetParams init = new InitHandlerWorkerGetParams();
//    	lsim.init(init);
    	LSimWorker lsim = LSimWorker.init(dispatcher, init);
    	
		// Obtain list of operations
    	List<String> users = null;
    	List<Operation> operations = null;
    	// get users and operations
		try {
			users = (List<String>) init.getParameter("users");
			operations = (List<Operation>) Serializer.deserialize((byte[]) init.getParameter("operations"));
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    	LSimLogger.log(Level.INFO, "List of users: " + users.toString());
    	LSimLogger.log(Level.INFO, "List of operations:\n" + operations.toString());


		// ------------------------------------------------
		// start
		// ------------------------------------------------
		
		lsim.start();

		// ------------------------------------------------
		// Phase 1
		// ------------------------------------------------
	
		// apply operations locally
        Log log = new Log(users);
        TimestampVector summary = new TimestampVector(users);
        for (int i=0; i<operations.size(); i++){
        	log.add(operations.get(i));
        	LSimLogger.log(Level.TRACE, "Log updated:\n" +log.toString());
        	summary.updateTimestamp(operations.get(i).getTimestamp());
        	LSimLogger.log(Level.TRACE, "Summary updated:\n" + summary.toString());
        }
        
        LSimLogger.log(Level.INFO, "Log:\n" + log.toString());
        LSimLogger.log(Level.INFO, "Summary:\n" + summary.toString());
		// ------------------------------------------------
		// sendResults
		// ------------------------------------------------

		// send result to localTestServer
		// create a result's object that contains the TSAE data structures of this server
        ServerResult serverResult = new ServerResult(" ", new Recipes(), log, summary, new TimestampMatrix(new ArrayList<String>()));
  
		// send result's object to the evaluator
        lsim.sendResult(serverResult);
            
        
		// ------------------------------------------------
		// stop
		// ------------------------------------------------
		
		LSimLogger.log(Level.INFO, "END");
		lsim.stop();		
	}
	
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
