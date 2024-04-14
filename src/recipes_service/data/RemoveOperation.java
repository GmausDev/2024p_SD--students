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
 */
public class RemoveOperation extends Operation implements Serializable{

	private static final long serialVersionUID = 6662533950228454468L;
	
	String recipeTitle;
	Timestamp recipeTimestamp;
	
	public RemoveOperation(String recipeTitle, Timestamp recipeTimestamp, Timestamp ts){
		super(ts);
		this.recipeTitle = recipeTitle;
		this.recipeTimestamp = recipeTimestamp;
	}
	public OperationType getType(){
		return OperationType.REMOVE;
	}
	public String getRecipeTitle() {
		return recipeTitle;
	}
	public Timestamp getRecipeTimestamp(){
		return recipeTimestamp;
	}
	@Override
	public String toString() {
		return "RemoveOperation [recipeTitle=" + recipeTitle
				+ ", recipeTimestamp=" + recipeTimestamp + ", timestamp="
				+ timestamp + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemoveOperation other = (RemoveOperation) obj;
		if (recipeTimestamp == null) {
			if (other.recipeTimestamp != null)
				return false;
		} else if (!recipeTimestamp.equals(other.recipeTimestamp))
			return false;
		if (recipeTitle == null) {
			if (other.recipeTitle != null)
				return false;
		} else if (!recipeTitle.equals(other.recipeTitle))
			return false;
		return true;
	}
}
