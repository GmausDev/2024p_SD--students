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
 * December 2012
 *
 */
public class Recipe implements Serializable{
	
	private static final long serialVersionUID = -4586737429673625621L;
	private String title;
	private String recipe;
	private String author;
	private Timestamp timestamp;
	
	public Recipe (String title, String recipe, String author, Timestamp timestamp){
		this.title = title;
		this.recipe = recipe;
		this.author = author;
		this.timestamp = timestamp;
	}
	
	public String getTitle(){
		return title;
	}

	public String getRecipe(){
		return recipe;
	}

	public String getAuthor(){
		return author;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (recipe == null) {
			if (other.recipe != null)
				return false;
		} else if (!recipe.equals(other.recipe))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		return true;
	}

	public String toString(){
		return "[" + this.title + ", " + this.recipe + ", " + this.author + "]";
	}
}
