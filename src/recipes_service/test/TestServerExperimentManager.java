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
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import lsim.library.api.LSimParameters;
import recipes_service.communication.Host;
import util.Serializer;

/**
 * @author Joan-Manuel Marques
 * February 2012
 *
 */

public class TestServerExperimentManager extends Thread{
	private ServerSocket serverSocket;
	private ExperimentData experimentData;
	private boolean logResults;
	private String path;
	
	private boolean purge_log = false;

	public TestServerExperimentManager(){
	}
	
	public void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void setExperimentData(ExperimentData experimentData) {
		this.experimentData = experimentData;
	}

	public void setLogResults(boolean logResults) {
		this.logResults = logResults;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void run() {
		//
		// Configuration of participating servers
		//

		List<Object> participants = new Vector<Object>();
		// receives info of participating servers
		// sends initial values for TSAE sessions and simulation
		Socket clientSocket = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;

		LSimParameters params = experimentData.getParams();
		System.out.println("TestServerExperimentManager -- params: "+params);

		purge_log = ((String)params.get("purge")).equals("purge");

		int numNodes = experimentData.getNumNodes();
		int numRequiredResults = ( (numNodes * experimentData.getPercentageRequiredResults()) / 100 + 1 );
		
		for (int i = 0 ; i<numNodes ; i++){
			try {
				serverSocket.setSoTimeout(45000);// sets a timeout. A read() call on the InputStream associated with this Socket will block for only this amount of time (milliseconds) 
				clientSocket = serverSocket.accept();
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				in = new ObjectInputStream(clientSocket.getInputStream());

				// send initialization parameters
				out.writeObject(params);

				// obtain the address of the remote participant server
				// ** method Serializer.serialize() is used to serialize node information
				// ** used to maintain compatibility with LSim, that requires a serialization
				participants.add(Serializer.serialize((Host) in.readObject()));

				in.close();
				out.close();
				clientSocket.close();
			} catch (SocketTimeoutException acceptException) {
				System.out.println("Less than "+ numNodes+" Serveres asked the initialization parameters");
				if (logResults){
					File file = new File(path, "Results");
					try {
						//				outputStream = new FileWriter(results.get(0).getGroupId()+".data",true);
						FileWriter outputStream = new FileWriter(file,true);
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						outputStream.append((dateFormat.format(new java.util.Date())).toString() 
								+ "\tLess than "+ numNodes+" Serveres asked the initialization parameters"
								+ '\n');
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				System.exit(50);
			} catch (IOException e) {
				System.err.println("Accept failed.");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// sends the list of participating servers to servers
		for (int i = 0 ; i<numNodes ; i++){
			try {
				serverSocket.setSoTimeout(45000);// sets a timeout. A read() call on the InputStream associated with this Socket will block for only this amount of time (milliseconds) 
				clientSocket = serverSocket.accept();
				out = new ObjectOutputStream(clientSocket.getOutputStream());
				out.writeObject(participants);
				out.close();
				clientSocket.close();
			} catch (SocketTimeoutException acceptException) {
				System.out.println("Less than "+ numNodes+" Serveres asked the list of participants");
				if (logResults){
					File file = new File(path, "Results");
					try {
						//				outputStream = new FileWriter(results.get(0).getGroupId()+".data",true);
						FileWriter outputStream = new FileWriter(file,true);
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						outputStream.append((dateFormat.format(new java.util.Date())).toString() 
								+ "\tLess than "+ numNodes+" Serveres asked the list of participants"
								+ '\n');
						outputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				System.exit(50);
			} catch (IOException e) {
				System.err.println("Accept failed.");
				e.printStackTrace();
			}
		}

		// ************
		// ** Results
		// ************

		// receives results from nodes 
		List<ServerResult> finalResults = new Vector<ServerResult>();
		boolean end = false;
		HashMap<Integer, List<ServerResult>> allResults = new HashMap<Integer, List<ServerResult>>();
		
		try {
			serverSocket.setSoTimeout(3600000);// sets a timeout. A read() call on the InputStream associated with this Socket will block for only this amount of time (milliseconds) 
			do{
				clientSocket = serverSocket.accept();
				in = new ObjectInputStream(clientSocket.getInputStream());
				try {
					ResultBase result = (ResultBase)in.readObject();
					switch(result.type()){
					case PARTIAL:
						Integer iteration = ((PartialResult)result).getIteration();
						List<ServerResult> results = null;
						if (allResults.containsKey(iteration)){
							results = allResults.get(iteration);
						} else{
							results = new Vector<ServerResult>();
						}
						results.add(result.getServerResult());
						allResults.put(iteration, results);
						System.out.println("##### [iteration: "+iteration
								+"] partial result from server: " + result.getServerResult().getNodeId());
						break;
					case FINAL:
						finalResults.add(result.getServerResult());
						System.out.println("##### Final result from server: " + result.getServerResult().getNodeId());
						if (finalResults.size() == numRequiredResults){
							end = true;
						}
						break;
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				in.close();
				clientSocket.close();
				serverSocket.setSoTimeout(45000);// sets a timeout. A read() call on the InputStream associated with this Socket will block for only this amount of time (milliseconds) 
			}while(!end);
		} catch (SocketTimeoutException acceptException) {
			System.out.println("*********** Accept timeout");
		} catch (IOException e){
			e.printStackTrace();
		}
		
		if (finalResults.size() < numRequiredResults){
			System.err.println("Unable to evaluate results due to: Not enough Servers where connected at the moment of finishing the Activity Simulation phase.");
			System.err.println("Recieved Results: "+finalResults.size());
			System.err.println("numRequiredResults: "+numRequiredResults);
			System.exit(30);
		}

		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("\n\n");
		System.out.println("================================================");
		System.out.println("END OF EVALUATION");
		System.out.println("\n");
		System.out.println("RESULTS");
		System.out.println("=======");

		// evaluate final results
		boolean equal = false;

		System.out.println("##### [" + finalResults.get(0).getNodeId() + "] Result:\n " + finalResults.get(0));

		FileWriter outputStream = null;
		if (logResults){
			File file = new File(path, "Results_"+System.currentTimeMillis()+".data");
			try {
				//				outputStream = new FileWriter(results.get(0).getGroupId()+".data",true);
				outputStream = new FileWriter(file,true);
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				outputStream.append("\n##### " + (dateFormat.format(new java.util.Date())).toString()
						);
				outputStream.append("\n----- [" + finalResults.get(0).getNodeId() + "] Result:\n " + finalResults.get(0));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		equal = true;
		for (int i = 1 ; i<finalResults.size() /*&& equal*/; i++){
			if (purge_log) equal = equal && finalResults.get(0).equals(finalResults.get(i));
			else equal = equal && finalResults.get(0).equalsNoACK(finalResults.get(i));
//			if (!equal){
				System.out.println("\n##### ["+finalResults.get(i).getNodeId()+"] Result:\n " + finalResults.get(i));
				if (logResults){
					try {
						DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						outputStream.append("\n##### " + (dateFormat.format(new java.util.Date())).toString());
						outputStream.append("\n----- ["+finalResults.get(i).getNodeId()+"] Result:\n " + finalResults.get(i));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//			}
		}

		if (logResults){
			try {
				outputStream.append("================================================\n");
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
		
		// calculate in which iteration nodes converged
		boolean converged = false;
		int convergenceIteration = -1;
		for (int it = 0; allResults.containsKey(Integer.valueOf(it))&& !converged; it++){
			List<ServerResult> results = allResults.get(Integer.valueOf(it));
			converged = (allResults.size() >= finalResults.size());
			for (int i = 1 ; i<results.size() && converged; i++){
				if (purge_log) converged = converged && results.get(0).equals(results.get(i));
				else converged = converged && results.get(0).equalsNoACK(results.get(i));
			}
			if (converged){
				convergenceIteration = it;
			}
		}

		// write final result
		System.out.println("\n\n");
		String result;
		if (equal){
			result = "Results are equal";
			if (convergenceIteration == -1){
				result += "\t Nodes converged at the last iteration ";
			} else{
				result += "\t Nodes converged at the iteration " + convergenceIteration;
			}
		} else{
			result = "Results are NOT equal";
		}
		System.out.println(result);

		System.out.println("\n\n");
		System.out.println("================================================");
		System.out.println("\n\n");

		if (logResults){
			File file = new File(path, "Results");
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
		System.out.println("\n\n");
		System.out.println("*********** num received results: "+finalResults.size());
		System.out.println("*********** % received results: "+(finalResults.size()*100)/numNodes);
		System.out.println("*********** minimal required number of results: "+numRequiredResults);
		System.out.println("\n\n");

		if (equal){
			System.exit(10);
		} else{
			System.exit(20);
		}
	}
}
