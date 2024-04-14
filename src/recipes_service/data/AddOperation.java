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

import recipes_service.tsae.data_structures.Timestamp;

/**
 * @author Joan-Manuel Marques
 * February 2013
 *
 * Add operation
 * 
 */
public class AddOperation extends Operation implements Serializable{

	private static final long serialVersionUID = -4812014190011512987L;
	Recipe recipe;
	
	/*
	 * Create and add operation
	 */
	public AddOperation(Recipe recipe, Timestamp ts){
		super(ts);
		this.recipe = recipe;
	}
	
	/**
	 * Operation type
	 */
	public OperationType getType(){
		return OperationType.ADD;
	}
	
	/*
	 * Gets the recipe included on the add operation
	 */
	public Recipe getRecipe() {
		return recipe;
	}
	
	@Override
	public String toString() {
		return "AddOperation [recipe=" + recipe + ", timestamp=" + timestamp
				+ "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AddOperation other = (AddOperation) obj;
		if (recipe == null) {
			if (other.recipe != null)
				return false;
		} else if (!recipe.equals(other.recipe))
			return false;
		return true;
	}
}
