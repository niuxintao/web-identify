package com.fc.action;

import java.util.List;

import com.fc.model.CTA;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;
import com.opensymphony.xwork2.ActionSupport;

public class ClassifcationAnalysis extends ActionSupport {

	/**
	 * 
	 */
	private String result;
	private static final long serialVersionUID = 1L;

	private String coveringArray;
	private String outCome;

	public String execute() {
		CTA cta = new CTA();
		int[] param = { 3, 3, 3 };
		String[] classes = { "pass", "err1", "err2", "err3" };

		int[][] suites = { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 0, 2 }, { 0, 1, 0 },
				{ 0, 1, 1 }, { 0, 1, 2 }, { 0, 2, 0 }, { 0, 2, 1 },
				{ 0, 2, 2 }, { 1, 0, 0 }, { 1, 0, 1 }, { 1, 0, 2 },
				{ 1, 1, 0 }, { 1, 1, 1 }, { 1, 1, 2 }, { 1, 2, 0 },
				{ 1, 2, 1 }, { 1, 2, 2 }, { 2, 0, 0 }, { 2, 0, 1 },
				{ 2, 0, 2 }, { 2, 1, 0 }, { 2, 1, 1 }, { 2, 1, 2 },
				{ 2, 2, 0 }, { 2, 2, 1 }, { 2, 2, 2 } };
		TestSuite suite = new TestSuiteImplement();

		for (int[] test : suites) {
			TestCaseImplement testCase = new TestCaseImplement(test.length);
			testCase.setTestCase(test);
			suite.addTest(testCase);
		}

		String[] state = { "pass", "pass", "err3", "pass", "pass", "pass",
				"pass", "pass", "pass", "err1", "err1", "err1", "err3", "err1",
				"err1", "err1", "err1", "err1", "err2", "err2", "err2", "err2",
				"err2", "err2", "err3", "err2", "err2" };

		try {
			cta.process(param, classes, suite, state);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		result = "";
		List<Tuple> bugs = cta.getBugs();
		for (Tuple bug : bugs)
			result += bug.toString() + "\n";

		return SUCCESS;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCoveringArray() {
		return coveringArray;
	}

	public void setCoveringArray(String coveringArray) {
		this.coveringArray = coveringArray;
	}

	public String getOutCome() {
		return outCome;
	}

	public void setOutCome(String outCome) {
		this.outCome = outCome;
	}
}
