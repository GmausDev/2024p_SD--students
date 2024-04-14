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

package recipes_service.data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */
public class Recipes implements Serializable{
	
	private static final long serialVersionUID = -8117147242301640951L;
	private TreeMap<String,Recipe> recipes = null;
	
	static Random rnd = new Random();
	
	
	public Recipes(){
		this.recipes = new TreeMap<String,Recipe> (); 
	}
	
	public synchronized void add(Recipe recipe){
		recipes.put(recipe.getTitle(),recipe);
	}
	
	public synchronized void remove(String recipeTitle){
		recipes.remove(recipeTitle);
	}
	public synchronized Recipe get(String recipeTitle){
		return recipes.get(recipeTitle);
	}
	public synchronized boolean contains(String recipeTitle){
		return recipes.containsKey(recipeTitle);
	}
	
	public synchronized String getRandomRecipeTitle(){
		if (recipes.isEmpty())
			return null;
		int n= (((int)(rnd.nextDouble() *10000))%recipes.size());
		
		Iterator<String> it = recipes.keySet().iterator();
		String result = it.next();
		for (int i = 0 ; i < n; i++){
			result = it.next();
		}
		return result;
	}
	@Override
	public synchronized boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipes other = (Recipes) obj;
		if (recipes == null) {
			if (other.recipes != null)
				return false;
			else 
				return true;
		} else {
			if (recipes.size() != other.recipes.size()){
				return false;
			}
			boolean equal = true;
			for (Iterator<String> it = recipes.keySet().iterator(); it.hasNext() && equal; ){
				String rcp = it.next();
				equal = recipes.get(rcp).equals(other.recipes.get(rcp));
				if (!equal){
				}
			}
			return equal;
		}
	}

	public synchronized String toString(){
		return recipes.toString();
	}
	
	public synchronized Recipes clone(){
		Recipes clone = new Recipes();
		
		for (Iterator<Recipe> it = recipes.values().iterator(); it.hasNext();){
			clone.add(it.next());
		}
		return clone;
	}
}
