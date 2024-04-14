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

package lsim.element.evaluator;


import java.io.IOException;

import edu.uoc.dpcs.lsim.logger.LoggerManager.Level;
import lsim.LSimDispatcherHandler;
import lsim.application.ApplicationManager;
import lsim.application.handler.InitHandlerEvaluatorGetParams;
import lsim.application.handler.evaluator.DummyEvaluatorTimeOutAction;
import lsim.evaluator.DefaultResultHandler;
import lsim.evaluator.GetResultTimeoutException;
import lsim.library.api.LSimEvaluator;
import lsim.library.api.LSimLogger;
import recipes_service.test.ServerResult;
import recipes_service.tsae.data_structures.Log;
import recipes_service.tsae.data_structures.TimestampVector;
import storage.data.ResultStorage;
import util.Serializer;

/**
 * @author Joan-Manuel Marques
 * December 2012
 *
 */

public class EvaluatorPhase1 implements ApplicationManager {

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void start(LSimDispatcherHandler disp) {
//		LSimFactory.getEvaluatorInstance().setDispatcher(disp);
//		try{
//			process(disp);
//		}catch(RuntimeException e){
//			LSimFactory.getEvaluatorInstance().logException(new LSimExceptionMessage("IO exception reading socket", e, null));
//		}
//	}
//
//	public void process(LSimDispatcherHandler hand) {
//		LSimEvaluator lsim = LSimFactory.getEvaluatorInstance();
		
		// set maximum time (minutes) that is expected the evaluation of experiment will last
//		lsim.startTimer(30);
		

		System.out.println("========= EVALUATOR");
		InitHandlerEvaluatorGetParams init = new InitHandlerEvaluatorGetParams();

		LSimEvaluator lsim = LSimEvaluator.init(disp, init);

		// Sets the action to perform if the evaluator timeout expires
		DummyEvaluatorTimeOutAction timeOutHandler = new DummyEvaluatorTimeOutAction();
		lsim.setTimeOutAction(timeOutHandler);

		// get users and operations
		String users = (String) init.getParameter("Users");
		String operations = (String) init.getParameter("Operations");

		// receive Log and TimestampVector calculated with Professors' solution
		Log log = null;
		TimestampVector summary = null;
		try {
			log = (Log) Serializer.deserialize((byte []) init.getParameter("Log"));
			summary = (TimestampVector) Serializer.deserialize((byte []) init.getParameter("TimestampVector"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Set result handler to standard (receive results and return one result
		// a time)
		DefaultResultHandler rh = new DefaultResultHandler();

		// Must be done before start
		lsim.setResultHandler(rh);
		lsim.start();

		// receive result from node
    	ServerResult sr = null;
		try {
			sr = (ServerResult) lsim.getResult().getResultValue();
		} catch (GetResultTimeoutException e1) {
			e1.printStackTrace();
		}
    	
    	// extract information from received ServerResult
		Log remoteLog = sr.getLog();
		TimestampVector remoteSummary = sr.getSummary();
		
        // ------------------------------------------------
        // final result
        // ------------------------------------------------

		// evaluate
		System.out.println("\n\n======\n\nrecipesService.test.Phase1LocalTestServer");
		System.out.println("COMPARE summary and log");

		boolean equal = true;
		String resultSum = "";
		String resultDetail = "";
		String lineSeparator ="\n\n================================================\n\n";
		String resultData = "\n\nList of users: " + users +
				lineSeparator +
				"List of operations:\n\n" + operations +
				lineSeparator;
		String logIsCorrect = "";
		String summaryIsCorrect="";
		
		
		if (!summary.equals(remoteSummary)){
			equal = false;
			resultSum = "Summaries are NOT equal";
			summaryIsCorrect = "(is not correct)";
			//resultData += "\nSummary:\n" + summary + "\nYour Summary:\n" + remoteSummary;
			System.out.println("Summary: " + summary);
			System.out.println("\n Your summary: " + remoteSummary);
		}
		if (!log.equals(remoteLog)){
			if (!equal){
				resultSum += " and ";
			}
			equal = false;
			resultSum += "Logs are NOT equal";
			logIsCorrect = "(is not correct)";
			//resultData += "\nLog:\n" + log + "\nYour Log:\n" + remoteLog;
			System.out.println("Log: " + log);
			System.out.println("\n Your Log: " + remoteLog);
		}
		
		ResultStorage res = new ResultStorage();
		res.setSuccess(equal);

		String result = null; 
		// store results (and print)
		if (equal){
			result= "\n\n Correct: Phase1 Summaries and Logs are equal.";
			resultData += "Log:\n\n" + log + lineSeparator + "Summary:\n\n" + summary;
//			System.out.println("\n\n"+sr.getGroupId()+" Phase1: Results are equal");
		} else{
            result = "\n\n Phase1 "+resultSum;
            resultData += "\nYour Log" + logIsCorrect + ":\n\n" + log + lineSeparator +
            		"Log:\n\n" + remoteLog + lineSeparator +
            		"Your Summary" + summaryIsCorrect + ":\n\n" + summary + lineSeparator +
            		"Summary:\n\n" + remoteSummary;
//			System.out.println("\n\n"+sr.getGroupId()+" Phase1: "+result);
		}
		resultDetail += result.trim();
		resultDetail +=resultData;
		
		System.out.println("result summary: "+result);
		System.out.println("result details: "+resultDetail);
		
		res.setResult(resultDetail);
        lsim.store(result, res);
//		System.out.println(resultDetail);

		System.out.println("\n\n");
        System.out.println("================================================");
        System.out.println("\n\n");
                
        // <--- ?????????????????? Cal pensar si cal treure-ho
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
 
        // ------------------------------------------------
        // stop
        // ------------------------------------------------

        lsim.stop();
//		lsim.Finish();
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
}