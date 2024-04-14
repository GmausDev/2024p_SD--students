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

/**
 * @author Joan-Manuel Marques
 * January 2012
 *
 */

public class FinalResult extends ResultBase implements Serializable{

	private static final long serialVersionUID = 8109894826923035975L;

	public FinalResult(ServerResult serverResult){
		super(serverResult);
	}
	
	public ResultType type(){
		return ResultType.FINAL;
	}
	
	public String toString(){
		return "[" + this.type() + "] " +this.getServerResult();
	}
}
