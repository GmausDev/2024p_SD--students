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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class TestServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {	
		System.out.println("start recipes_service.test.TestServer");

		// 
		int listeningPort = 20000;
		boolean logResults= false;
		String path = null;
		boolean forever = false;
		
		try {
			//
			// args
			//
			List<String> argsList = Arrays.asList(args);

			// listening port of TestServer
			listeningPort = Integer.valueOf(args[0]);

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
			
		} catch (Exception e){
			System.err.println("TestServer error. Incorrect arguments");
			System.err.println("arg0: listening port of TestServer");
			System.err.println("optional args:");
			System.err.println("\t--logResults: appends the result of the each execution to a file named as the groupId");
			System.err.println("\t-path <path>: path to directory where store results (if --logResults is activated)");
			System.err.println("\t--forever: runs forever");

			System.exit(1);
		}

		//
		// Bind TestServer to listening port
		//

		// Prepare server socket 
		ServerSocket serverSocket = null;
		try {
			//            serverSocket = new ServerSocket(listeningPort);

			// setReuseAddress to bind a socket to the required SocketAddress
			// even though the SO timeout of a previous (closed) TCP connection is not expired
			serverSocket = new ServerSocket();
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(listeningPort));
		} catch (IOException e) {
			System.err.println("TestServer -- Could not listen on port: " + listeningPort);
			System.exit(1);
		}


		// each experiment execution is associated to a different port
		// 		key: groupId
		//		value: port
		// (it a group launches two experiments with enough time between them to
		// initialize the experiment, these two experiments will run concurrently)
		HashMap<String,Integer> groupPort = new HashMap<String, Integer>();
		
		boolean end = false;
		do{
			try {
				Socket clientSocket = null;
				ObjectInputStream in = null;
				ObjectOutputStream out = null;

				serverSocket.setSoTimeout(10000);
				clientSocket = serverSocket.accept();
				in = new ObjectInputStream(clientSocket.getInputStream());
				
				TestServerMessage msg = (TestServerMessage) in.readObject();
				switch(msg.type()){
				case SET_ARGS:
					// assign a thread of TestServer to deal with this experiment into a port  
					int port = listeningPort+1;
					
					TestServer testServer = new TestServer();
					ServerSocket acceptServerSocket = testServer.servicePublished(port);
					
					// create a thread of TestServer to deal with this experiment
					TestServerExperimentManager testServerExperimentManager = new TestServerExperimentManager();
					testServerExperimentManager.setServerSocket(acceptServerSocket);
					testServerExperimentManager.setExperimentData(msg.getExperimentData());
					testServerExperimentManager.setLogResults(logResults);
					testServerExperimentManager.setPath(path);
					
					// start the thread
					testServerExperimentManager.start();
					
					// Assign port to groupPort (variable that associates each experiment to a port)
					System.out.println("TestServer -- current experiment named "+msg.getTestId()+" will run on port "+acceptServerSocket.getLocalPort());
					groupPort.put(msg.getTestId(),Integer.valueOf(acceptServerSocket.getLocalPort()));
					break;
					
				case GET_PORT:
					// returns the port for this experiment
					out = new ObjectOutputStream(clientSocket.getOutputStream());
					out.writeObject(groupPort.get(msg.getTestId()).intValue());
					out.close();
					break;
				}

				in.close();
				clientSocket.close();
			}catch(java.net.SocketTimeoutException e){
				end = !forever;
			}catch (IOException | ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} while (!end);
}
	
	/**
	 * Auxiliary functions
	 */

	public ServerSocket servicePublished(int port) {
		ServerSocket serverSocket = null;
		boolean end = true;
		do{
			end = true;
			// check if port is used by a UDP service
			DatagramSocket ds = null;
			try {
				ds = new DatagramSocket(null);
				ds.setReuseAddress(true);
				ds.bind(new InetSocketAddress(port));
//				ds = new DatagramSocket(port);
//				ds.setReuseAddress(true);
			} catch (IOException e) {
				end = false;
				continue;
			}finally{
				if (ds != null) {
					ds.close();
				}
			}
			// assign service to port
			// starts a thread to deal with TSAE sessions from partner servers 

			try {
				serverSocket = new ServerSocket();
				serverSocket.setReuseAddress(true);
				serverSocket.bind(new InetSocketAddress(port));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//		e.printStackTrace();
				//			System.err.println("--- ** ---> ServerPartnerSide -- IOException: "+e);

				if (serverSocket != null) {
					try {
						serverSocket.close();
					} catch (IOException e1) {
						/* should not be thrown */
					}
				}
				end = false;
			}
			port++;
		}while(!end);
		return serverSocket;
	}
}
