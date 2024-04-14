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

package recipes_service.communication;

import java.io.Serializable;
import java.util.List;
import java.util.Random;
import java.util.Vector;



/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class Hosts{
	private static final long serialVersionUID = 3974926142898691472L;

	private List<Host> nodes;
	private Host localNode;
	
	private List<String> listIds;

	static Random rnd = new Random();

	public Hosts(Host localNode){
		this.nodes = new Vector<Host>();
		this.localNode = localNode;
		
		this.listIds = new Vector<String>();
	}

	public void add(Host node){
		this.nodes.add(node);
		this.listIds.add(node.getId());
	}
	
	public int size(){
		return nodes.size();
	}
	/**
	 * Returns a list of num random partners
	 * @param num
	 * @return
	 */
	public List<Host> getRandomPartners(int num){
		List<Host> v = new Vector<Host>();

		if (nodes.size() == 1 || num < 1){
			return v;
		}

		num = Math.min(num, nodes.size()-1);

		@SuppressWarnings("unchecked")
		List<Host> auxNodes=(Vector<Host>)((Vector<Host>) nodes).clone();

		auxNodes.remove(localNode);

		int n;
		while(v.size()<num){
			n= (((int)(rnd.nextDouble() *10000))%auxNodes.size());
			v.add(auxNodes.get(n));
			auxNodes.remove(n);
		}
		return v;		
	}

	public List<String> getIds(){
		return listIds;
	}
	public String toString(){
		return localNode + "-" + nodes.toString();
	}
}
