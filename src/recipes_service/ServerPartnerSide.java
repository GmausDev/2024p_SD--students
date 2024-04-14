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
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.library.api.LSimLogger;
import recipes_service.tsae.sessions.TSAESessionPartnerSide;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class ServerPartnerSide  extends Thread{
	private int port;
	private ServerData serverData = null;
	
	private ServerSocket serverSocket = null;

	boolean servicePublished = false;

	public ServerPartnerSide(int port, ServerData serverData) {
		super("TSAEPartnerSide");
		this.port = port;
		this.serverData = serverData;
	}
	public void run() {

		servicePublished = false;
		
		// assign service to port
		while (!servicePublished(port)){
			port++;
		}
		// wakes up WorkerInitHandler waiting for the assignment of the service 
		synchronized (this){
			servicePublished=true;
			notify();
		}
	
		// waits until the Server is ready to receive TSAE sessions from partner servers
		serverData.waitServerConnected();
		
		// accept remote TSAE connections
		// starts a new thread for each TSAE sessions from a partner server 

		while (!serverData.end()){
			try {
				// accept will block for this amount of time.
				// After this time a SocketTimeoutException will rise.
				// if server should stop it will close and finish.
				// In other case it will block again. 
				serverSocket.setSoTimeout(20000);
				new TSAESessionPartnerSide(serverSocket.accept(), this.serverData).start();
			} catch (java.net.SocketTimeoutException e){
				;
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				LSimLogger.log(Level.ERROR,
						e1.getMessage()
						);
				e1.printStackTrace();
			}
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			LSimLogger.log(Level.ERROR,
					e.getMessage()
					);
			e.printStackTrace();
		}
	}
	
	public int getPort(){
		return this.port;
	}
	
	/**
	 * waits until the serverPartnerSide is published in a port 
	 */
	public synchronized void waitServicePublished(){
		while (!servicePublished){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//			e.printStackTrace();
			}
		}
	}

	/**
	 * Auxiliary functions
	 */

	public boolean servicePublished(int port) {
		// check if port is used by a UDP service
		DatagramSocket ds = null;
		try {
			ds = new DatagramSocket(null);
			ds.setReuseAddress(true);
			ds.bind(new InetSocketAddress(port));
//			ds = new DatagramSocket(port);
//			ds.setReuseAddress(true);
		} catch (IOException e) {
			return false;
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
//			serverSocket = new ServerSocket(port);
//			serverSocket.setReuseAddress(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//		e.printStackTrace();
			if (serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e1) {
					/* should not be thrown */
				}
			}
			return false;
		}
		return true;
	}
}

