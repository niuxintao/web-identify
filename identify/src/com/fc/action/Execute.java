package com.fc.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fc.model.Chain;
import com.fc.model.ChainAug;
import com.fc.model.FIC;
import com.fc.model.SOFOT;
import com.fc.model.TuplePool;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.CorpTupleWithTestCase;
import com.fc.tuple.Tuple;
import com.opensymphony.xwork2.ActionContext;

public class Execute {
	private String run;
	private String nextTestCase;
	private String result;

	public String getNextTestCase() {
		return nextTestCase;
	}

	public String getRun() {
		return run;
	}

	public void setRun(String run) {
		this.run = run;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Execute() {

	}

	// TRT algorithm
	public void executeTRT() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		String[][] paramstr = (String[][]) session.get("PARAVALUES");

		TestCase wrongCase = (TestCase) session.get("FAILTEST");
		int[] param = (int[]) session.get("PARAM");

		if (session.get("WORKMACHINE") == null) {

			TuplePool tree = new TuplePool(wrongCase, new TestSuiteImplement());
			CorpTupleWithTestCase generate = new CorpTupleWithTestCase(
					wrongCase, param);
			Chain workMachine = new Chain(tree, generate);
			TestCase testcase = workMachine.genNextTest();
			session.put("LASTTESTCASE", testcase);
			session.put("WORKMACHINE", workMachine);
		}

		Chain workMachine = (Chain) session.get("WORKMACHINE");
		TestCase lastCase = (TestCase) session.get("LASTTESTCASE");
		if (lastCase != null) {
			System.out.println("check str: " + run);
			if (run != null && run.equals("on")) {
				lastCase.setTestState(TestCase.PASSED);
				workMachine.setLastTestCase(true);
			} else {
				lastCase.setTestState(TestCase.FAILED);
				workMachine.setLastTestCase(false);
			}
		}

		TestCase testcase = workMachine.genNextTest();
		session.put("LASTTESTCASE", testcase);
		if (testcase == null)
			nextTestCase = null;
		else
			nextTestCase = Transfer.testCaseFromInt(testcase, paramstr);

		Map<String, Object> s = new HashMap<String, Object>();
		s.put("testCase", nextTestCase);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < workMachine.getPool().getExistedBugTuples().size(); i++) {
			Tuple tuple = workMachine.getPool().getExistedBugTuples().get(i);
			jsonArray.add(i, Transfer.tupleFromInt(wrongCase, tuple, paramstr));
		}

		JSONArray jsonArray2 = new JSONArray();
		for (int i = 0; i < workMachine.getExtraCases().getTestCaseNum(); i++) {
			TestCase testCase = workMachine.getExtraCases().getAt(i);
			jsonArray2.add(i, Transfer.testCaseFromInt(testCase, paramstr));
		}

		JSONArray jsonArray3 = new JSONArray();
		for (int i = 0; i < workMachine.getExtraCases().getTestCaseNum(); i++) {
			int pass = workMachine.getExtraCases().getAt(i).testDescription();
			jsonArray3.add(i, pass);
		}

		JSONObject jo = JSONObject.fromObject(s);
		jo.element("bugtuples", jsonArray);
		jo.element("testCases", jsonArray2);
		jo.element("passorfail", jsonArray3);

		System.out.println(jo.toString());
		setResult(jo.toString());
	}

	// FIC_BS algorithm
	public void executeFIC_BS() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		String[][] paramstr = (String[][]) session.get("PARAVALUES");
		TestCase wrongCase = (TestCase) session.get("FAILTEST");
		int[] param = (int[]) session.get("PARAM");
		if (session.get("FIC") == null) {
			FIC fic = new FIC(wrongCase, param, null);
			session.put("FIC", fic);
		}
		FIC fic = (FIC) session.get("FIC");
		TestCase lastCase = (TestCase) session.get("LASTTESTCASE");
		if (lastCase != null) {
			System.out.println("check str: " + run);
			if (run != null && run.equals("on")) {
				lastCase.setTestState(TestCase.PASSED);
				fic.setLastTestCase(true);
			} else {
				lastCase.setTestState(TestCase.FAILED);
				fic.setLastTestCase(false);
			}
		}

		TestCase testcase = fic.generateNext();
		session.put("LASTTESTCASE", testcase);
		if (testcase == null)
			nextTestCase = null;
		else
			nextTestCase = Transfer.testCaseFromInt(testcase, paramstr);

		Map<String, Object> s = new HashMap<String, Object>();
		s.put("testCase", nextTestCase);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < fic.getBugs().size(); i++) {
			Tuple tuple = fic.getBugs().get(i);
			jsonArray.add(i, Transfer.tupleFromInt(wrongCase, tuple, paramstr));
		}

		JSONArray jsonArray2 = new JSONArray();
		for (int i = 0; i < fic.getExtraCases().getTestCaseNum(); i++) {
			TestCase testCase = fic.getExtraCases().getAt(i);
			jsonArray2.add(i, Transfer.testCaseFromInt(testCase, paramstr));
		}

		JSONArray jsonArray3 = new JSONArray();
		for (int i = 0; i < fic.getExtraCases().getTestCaseNum(); i++) {
			int pass = fic.getExtraCases().getAt(i).testDescription();
			jsonArray3.add(i, pass);
		}

		JSONObject jo = JSONObject.fromObject(s);
		jo.element("bugtuples", jsonArray);
		jo.element("testCases", jsonArray2);
		jo.element("passorfail", jsonArray3);

		System.out.println(jo.toString());
		setResult(jo.toString());

	}

	// OFOT algorithm
	public void executeOFOT() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		String[][] paramstr = (String[][]) session.get("PARAVALUES");
		TestCase wrongCase = (TestCase) session.get("FAILTEST");
		int[] param = (int[]) session.get("PARAM");
		if (session.get("OFOT") == null) {
			SOFOT ofot = new SOFOT(param, wrongCase);
			session.put("OFOT", ofot);
		}
		SOFOT ofot = (SOFOT) session.get("OFOT");
		TestCase lastCase = (TestCase) session.get("LASTTESTCASE");
		if (lastCase != null) {
			System.out.println("check str: " + run);
			if (run != null && run.equals("on")) {
				lastCase.setTestState(TestCase.PASSED);
				ofot.setLastTestCase(true);
			} else {
				lastCase.setTestState(TestCase.FAILED);
				ofot.setLastTestCase(false);
			}
		}

		TestCase testcase = ofot.generateNextTestCase();
		session.put("LASTTESTCASE", testcase);
		if (testcase == null)
			nextTestCase = null;
		else
			nextTestCase = Transfer.testCaseFromInt(testcase, paramstr);

		Map<String, Object> s = new HashMap<String, Object>();
		s.put("testCase", nextTestCase);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < ofot.getBugs().size(); i++) {
			Tuple tuple = ofot.getBugs().get(i);
			jsonArray.add(i, Transfer.tupleFromInt(wrongCase, tuple, paramstr));
		}

		JSONArray jsonArray2 = new JSONArray();
		for (int i = 0; i < ofot.getExecuted().size(); i++) {
			TestCase testCase = ofot.getExecuted().get(i);
			jsonArray2.add(i, Transfer.testCaseFromInt(testCase, paramstr));
		}

		JSONArray jsonArray3 = new JSONArray();
		for (int i = 0; i < ofot.getExecuted().size(); i++) {
			int pass = ofot.getExecuted().get(i).testDescription();
			jsonArray3.add(i, pass);
		}

		JSONObject jo = JSONObject.fromObject(s);
		jo.element("bugtuples", jsonArray);
		jo.element("testCases", jsonArray2);
		jo.element("passorfail", jsonArray3);

		System.out.println(jo.toString());
		setResult(jo.toString());

	}

	public void executeAugTRT() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		String[][] paramstr = (String[][]) session.get("PARAVALUES");

		TestCase wrongCase = (TestCase) session.get("FAILTEST");
		int[] param = (int[]) session.get("PARAM");

		if (session.get("AUGWORKMACHINE") == null) {

			TuplePool tree = new TuplePool(wrongCase, new TestSuiteImplement());
			CorpTupleWithTestCase generate = new CorpTupleWithTestCase(
					wrongCase, param);
			ChainAug workMachine = new ChainAug(tree, generate);
			TestCase testcase = workMachine.genNextTest();
			session.put("LASTTESTCASE", testcase);
			session.put("AUGWORKMACHINE", workMachine);
		}

		ChainAug workMachine = (ChainAug) session.get("AUGWORKMACHINE");
		TestCase lastCase = (TestCase) session.get("LASTTESTCASE");
		if (lastCase != null) {
			System.out.println("check str: " + run);
			if (run != null && run.equals("on")) {
				lastCase.setTestState(TestCase.PASSED);
				workMachine.setLastTestCase(true);
			} else {
				lastCase.setTestState(TestCase.FAILED);
				workMachine.setLastTestCase(false);
			}
		}
		TestCase testcase = workMachine.genNextTest();
		session.put("LASTTESTCASE", testcase);
		if (testcase == null)
			nextTestCase = null;
		else
			nextTestCase = Transfer.testCaseFromInt(testcase, paramstr);

		Map<String, Object> s = new HashMap<String, Object>();
		s.put("testCase", nextTestCase);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < workMachine.getPool().getExistedBugTuples().size(); i++) {
			Tuple tuple = workMachine.getPool().getExistedBugTuples().get(i);
			jsonArray.add(i, Transfer.tupleFromInt(wrongCase, tuple, paramstr));
		}

		JSONArray jsonArray2 = new JSONArray();
		for (int i = 0; i < workMachine.getExtraCases().getTestCaseNum(); i++) {
			TestCase testCase = workMachine.getExtraCases().getAt(i);
			jsonArray2.add(i, Transfer.testCaseFromInt(testCase, paramstr));
		}

		JSONArray jsonArray3 = new JSONArray();
		for (int i = 0; i < workMachine.getExtraCases().getTestCaseNum(); i++) {
			int pass = workMachine.getExtraCases().getAt(i).testDescription();
			jsonArray3.add(i, pass);
		}

		JSONObject jo = JSONObject.fromObject(s);
		jo.element("bugtuples", jsonArray);
		jo.element("testCases", jsonArray2);
		jo.element("passorfail", jsonArray3);

		System.out.println(jo.toString());
		setResult(jo.toString());

	}

}