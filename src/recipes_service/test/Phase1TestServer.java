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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import lsim.library.api.LSimParameters;
import recipes_service.data.AddOperation;
import recipes_service.data.Operation;
import recipes_service.data.Recipe;
import recipes_service.tsae.data_structures.Log;
import recipes_service.tsae.data_structures.Timestamp;
import recipes_service.tsae.data_structures.TimestampVector;
import util.Serializer;

/**
 * @author Joan-Manuel Marques
 * March 2015
 *
 */
public class Phase1TestServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		// arg0: listeningPort
		// arg1: number of operations to test
		
		System.out.println("start recipes_service.test.Phase1TestServer");
		
		// params 
		LSimParameters params = null;

		// result
		TimestampVector remoteSummary = null;
		Log remoteLog = null;

		
		// 
		int listeningPort = 39825;
		boolean logResults= false;
		String path = ".";
		boolean forever = false;
		
		//
		String executionMode = "localMode";
		
		// Simulation
		int numOperations = 50; // number of operations to generate
		int numUsers = 5; // number of users that participate in the experiment

		try {
			//
			// args
			//
			List<String> argsList = Arrays.asList(args);

			// listenint port of TestServer
			listeningPort = Integer.valueOf(args[0]);

			// to indicate if all Servers will run in a single computer
			// or they will run Servers hosted in different computers (or more than one 
			// Server in a single computer but this computer having the same internal and external IP address)
			// * localMode: all Server run in a single computer
			// * remoteMode: Servers running in different computers (or more than one Server in a single computer but
			// 			this computer having the same internal and external IP address)
			if (argsList.contains("--remoteMode")){
				executionMode = "remoteMode";
			}

			// to log results in a file
			logResults = false;
			if (argsList.contains("--logResults")){
				logResults = true;
			}

			// path to directory where store results (if --logResults is set)
			if (argsList.contains("-path")){
				int i = argsList.indexOf("-path");
				path = args[i+1];
			}

			// --forever: to run TestServer forever (untill it is killed)
			forever = argsList.contains("--forever");

			
			//
			// Initial values for simulating TSAE data structures 
			//

			// if -nOps arg
			if (argsList.contains("-nOps")){
				int i = argsList.indexOf("-nOps");
				numOperations = Integer.valueOf(args[i+1]);
				if (numOperations < 15){
					throw new Exception();
				}
			}

			// if -nUsrs arg
			if (argsList.contains("-nUsrs")){
				int i = argsList.indexOf("-nUsrs");
				numUsers = Integer.valueOf(args[i+1]);
				if (numUsers < 5){
					throw new Exception();
				}
			}
		} catch (Exception e){
			System.err.println("TestServer error. Incorrect arguments");
			System.err.println("arg0: listening port of Phase1TestServer");
			System.err.println("optional args:");
			System.err.println("\t--remoteMode: Server will run in different computers (or more than one Server in a single computer but this computer having the same internal and external IP address)");
			System.err.println("\t--localMode: (default running mode. If no mode is specified it will suppose local mode) all Serves will run in the same computer");
			System.err.println("\t--logResults: appends the result of the each execution to a file named Results_Phase1");
			System.err.println("\t-path <path>: path to directory where store results (if --logResults is activated)");
			System.err.println("\t--forever: runs forever");

			// simulating TSAE data structures
			System.err.println("\t-nOps <number of operations>: (default value: 50. Minimal value 15) number of operations to generate to test the correct behaviour of TimestampVector and Log TSAE data structures");
			System.err.println("\t-nUsrs <number of operations>: (default value: 5. Minimal value 5) number of simulated users to test the correct behaviour of TimestampVector and Log TSAE data structures");

			System.exit(1);
		}

 		// Prepare server socket 
        ServerSocket serverSocket = null;
        try {
        	// setReuseAddress to bind a socket to the required SocketAddress
        	// even though the SO timeout of a previous (closed) TCP connection is not expired
        	serverSocket = new ServerSocket();
        	serverSocket.setReuseAddress(true);
        	serverSocket.bind(new InetSocketAddress(listeningPort));
        } catch (IOException e) {
            System.err.println("LocalTestServer Could not listen on port: " + listeningPort);
            System.exit(1);
        }

       	// Declare TimestampVector and Log
    	TimestampVector summary = null;
    	Log log = null;

    	// Declare users and operations
    	List<String> users = null;
    	List<Operation> operations = null;

    	do{
    		params = new LSimParameters();
    		
    		users = null;
        	operations = new Vector<Operation>();
    		
        	if (executionMode.equals("localMode")){
    			System.out.println("localMode");
    			File file = new File("phase1.data");
    			try {
    				ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
    				// read users and operations
    				users = (List<String>) in.readObject();
    				operations = (List<Operation>) in.readObject();

    				// read TimestampVector and Log
    				summary = (TimestampVector) in.readObject();
    				log = (Log) in.readObject();

    				in.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (ClassNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}

    			System.out.println(users);
    			System.out.println(operations);
    			System.out.println(summary);
    			System.out.println(log);
    		} else{ // remoteMode
    			
    			// create users
    			users = new Vector<String>();
    			int seqnum[] = new int[numUsers];

    			for (int i=0; i<numUsers; i++){
    				users.add("user"+String.valueOf(i));
    				seqnum[i] = 0;
    			}


    			// create local TimestampVector and Log
    			summary = new TimestampVector(users);
    			log = new Log(users);

    			// create list of operations
    			operations = new Vector<Operation>();
    			Random rnd = new Random();

    			for (int i=0; i<numOperations; i++){
    				byte[] bytes=new byte[8];
    				char[] chars=new char[8];
    				byte mod=((byte)'z'-(byte)'a');
    				rnd.nextBytes(bytes);
    				for(int ii=0; ii<8; ii++){
    					byte b=bytes[ii];
    					if(b<0)
    						b*=-1;
    					b%=mod;
    					chars[ii]=(char)((byte)'a'+b);
    				}


    				// apply operations locally
    				int user = (((int)(rnd.nextDouble() *10000))%numUsers);
    				Timestamp ts = new Timestamp(users.get(user), seqnum[user]++);

    				Recipe rcpe = new Recipe(String.valueOf(chars), "Content--"+String.valueOf(chars), users.get(user), ts);

    				log.add(new AddOperation(rcpe, ts));
    				summary.updateTimestamp(ts);

    				operations.add(new AddOperation(rcpe, ts));
    			}
    		}

    		//
    		// create params list
    		//
    		params.put("groupId", "phase1-noLSim");
    		params.put("executionMode", executionMode);

    		//LSimParameters params_server = new LSimParameters();
    		//params_server.put("users", users);
    		params.put("users", users); //sgeag_2018
    		try {
    			//params_server.put("operations", Serializer.serialize(operations));
    			params.put("operations", Serializer.serialize(operations)); //sgeag_2018
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
    		
    		//params.put("coordinatorLSimParameters", params_server);


    		System.out.println("TestServer -- params: "+params);

    		// sends sequence of operations to testing node
    		try {
    			Socket clientSocket = serverSocket.accept();
    			ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
    			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
    			out.writeObject(Serializer.serialize(params));

    			// receives results from nodes 
    			try {
    				ServerResult sr = (ServerResult)Serializer.deserialize((byte []) in.readObject());
    				remoteLog = sr.getLog();
    				remoteSummary = sr.getSummary();
    			} catch (ClassNotFoundException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			in.close();
    			clientSocket.close();
    		} catch (IOException e) {
    			System.err.println("Accept failed.");
    			e.printStackTrace();
    		}

     		// evaluate
    		System.out.println("\n\n======\n\nrecipes_service.test.Phase1TestServer");
    		System.out.println("COMPARING summary and log ...\n");
    		
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

    		boolean equal = false;
    		String result = "";

    		if (summary.equals(remoteSummary)){
    			equal = true;
    		} else{
    			equal = false;
    			result = "Summaries are NOT equal";
    			System.out.println("Summary: " + summary);
    			System.out.println("\n Your summary: " + remoteSummary);
    		}
    		if (!log.equals(remoteLog)){
    			if (!result.isEmpty()){
    				result += " and ";
    			}
    			equal = false;
    			result += "Logs are NOT equal";
    			System.out.println("Log: " + log);
    			System.out.println("\n Your Log: " + remoteLog);
    		}

    		if (equal){
    			result = "Result is correct: Log and Summary are correct";
    		}

    		System.out.println(result);

    		FileWriter outputStream = null;
    		if (logResults){
    			File file = new File(path, "Results_Phase1");
    			try {
    				//				outputStream = new FileWriter(results.get(0).getGroupId(),true);
    				outputStream = new FileWriter(file,true);
    				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    				outputStream.append((dateFormat.format(new java.util.Date())).toString() 
    						+ '\t' + result
    						+ '\n');
    				outputStream.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} finally{
    				if (outputStream != null) {
    					try {
    						outputStream.close();
    					} catch (IOException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
    				}
    			}
    		}

    				//
    				// Uncomment to create phase1.dat file
    				// (from scripts folder) execute: ./start.sh 20004 --phase1 --remoteMode
    				//
//    		        File file = new File("phase1.data");
//    		        try {
//    		        	ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
//    		        	out.writeObject(users);
//    		        	out.writeObject(operations);
//    		        	out.writeObject(summary);
//    		        	out.writeObject(log);
//    		
//    		        	out.flush();
//    		        	out.close();
//    		        } catch (IOException e) {
//    		        	// TODO Auto-generated catch block
//    		        	e.printStackTrace();
//    		        }

    	} while (forever);

    	try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    	System.exit(0);
	}
}
