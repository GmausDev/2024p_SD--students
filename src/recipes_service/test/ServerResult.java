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

import java.io.Serializable;

import recipes_service.data.Recipes;
import recipes_service.tsae.data_structures.Log;
import recipes_service.tsae.data_structures.TimestampMatrix;
import recipes_service.tsae.data_structures.TimestampVector;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */

public class ServerResult implements Serializable{

	private static final long serialVersionUID = 1334487840616410385L;
	private String nodeId;
	private Recipes recipes;
	private Log log;
	private TimestampVector summary;
	private TimestampMatrix ack;
	
	public ServerResult (String nodeId, Recipes recipes, Log log, TimestampVector tsVector, TimestampMatrix tsMatrix){
		this.nodeId = nodeId;
		this.recipes = recipes.clone();
		this.log = log;
		this.summary = tsVector;
		this.ack = tsMatrix;
	}
	
	public String getNodeId(){
		return this.nodeId;
	}
	public Recipes getRecipes() {
		return recipes;
	}
	public Log getLog() {
		return log;
	}
	public TimestampVector getSummary() {
		return summary;
	}
	public TimestampMatrix getAck() {
		return ack;
	}
	public String toString(){
		return "\nNode id: " + nodeId + "\nRecipes: " + recipes.toString() 
				+ "\nLog: " + log.toString() + "\nSummary: " + summary.toString()
				+ "\nAck: " + ack.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerResult other = (ServerResult) obj;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary)){
//			System.out.println("ServerResult --- equals: summaries are not equals");
//			System.out.println("ServerResult --- ! equals -- summary: "+summary);
//			System.out.println("ServerResult --- ! equals -- summary2: "+other.summary);
			return false;
		}
		if (log == null) {
			if (other.log != null)
				return false;
		} else if (!log.equals(other.log)){
//			System.out.println("ServerResult --- equals: logs are not equals");
			return false;
		}
		if (ack == null) {
			if (other.ack != null)
				return false;
		} else if (!ack.equals(other.ack)){
//			System.out.println("ServerResult --- equals: acks are not equals");
//			System.out.println("ServerResult --- ! equals -- ack: "+ack);
//			System.out.println("ServerResult --- ! equals -- ack2: "+other.ack);
			return false;
		}
		if (recipes == null) {
			if (other.recipes != null)
				return false;
		} else if (!recipes.equals(other.recipes)){
//			System.out.println("ServerResult --- equals: recipes are not equals");
			return false;
		}
		return true;
	}
	
	public boolean equalsNoACK(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerResult other = (ServerResult) obj;
		if (summary == null) {
			if (other.summary != null)
				return false;
		} else if (!summary.equals(other.summary)){
//			System.out.println("ServerResult --- equals: summaries are not equals");
//			System.out.println("ServerResult --- ! equals -- summary: "+summary);
//			System.out.println("ServerResult --- ! equals -- summary2: "+other.summary);
			return false;
		}
		if (log == null) {
			if (other.log != null)
				return false;
		} else if (!log.equals(other.log)){
//			System.out.println("ServerResult --- equals: logs are not equals");
			return false;
		}
		if (recipes == null) {
			if (other.recipes != null)
				return false;
		} else if (!recipes.equals(other.recipes)){
//			System.out.println("ServerResult --- equals: recipes are not equals");
			return false;
		}
		return true;
	}
}
