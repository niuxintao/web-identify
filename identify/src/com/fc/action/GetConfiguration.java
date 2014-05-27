package com.fc.action;

import java.util.List;
import java.util.Map;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetConfiguration extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String nextTestCase;
	private String param_name;
	private String param_value;
	private String fail_test;
	private List<String> approach;

	private String result;

	public String getNextTestCase() {
		return nextTestCase;
	}

	private int[] strtointoftest(String[][] pstr, String[] failTest) {
		int[] result = new int[failTest.length];
		for (int i = 0; i < failTest.length; i++) {
			String[] va = pstr[i];
			for (int j = 0; j < va.length; j++) {
				if (va[j].equals(failTest[i])) {
					result[i] = j;
					break;
				}
			}
		}
		return result;

	}

	public String execute() {
		String[] p_names = param_name.split(" ");
		String[] p_values = param_value.split("\n");
		for (int i = 0; i < p_values.length; i++) {
			p_values[i] = p_values[i].split("\r")[0];
		}

		for (String str : approach) {
			System.out.println(str);
		}
		int[] param = new int[p_values.length];
		String[][] paramstr = new String[p_values.length][];
		for (int i = 0; i < p_values.length; i++) {
			String[] s = p_values[i].split(" ");
			paramstr[i] = s;
			param[i] = s.length;
		}
		int[] f_int = strtointoftest(paramstr, fail_test.split(" "));
		TestCase wrongCase = new TestCaseImplement();
		((TestCaseImplement) wrongCase).setTestCase(f_int);
		// System.out.println(wrongCase.getStringOfTest());
		Map<String, Object> session = ActionContext.getContext().getSession();
		session.clear();
		session.put("PARANAMES", p_names);
		session.put("PARAVALUES", paramstr);
		session.put("PARAM", param);
		session.put("FAILTEST", wrongCase);

		String method = approach.get(0);
		session.put("APPROACH", method);

		Execute execute = new Execute();
		if (method.equals("Aug_TRT")) {
			execute.executeAugTRT();
		} else if (method.equals("TRT")) {
			execute.executeTRT();
		} else if (method.equals("FIC_BS")) {
			execute.executeFIC_BS();
		} else if (method.equals("OFOT")) {
			execute.executeOFOT();
		}

		// TuplePool tree = new TuplePool(wrongCase, new TestSuiteImplement());
		// CorpTupleWithTestCase generate = new CorpTupleWithTestCase(wrongCase,
		// param);
		//
		// Chain workMachine = new Chain(tree, generate);
		//
		// TestCase testcase = workMachine.genNextTest();
		// session.put("LASTTESTCASE", testcase);
		// session.put("WORKMACHINE", workMachine);
		// if (testcase == null)
		// nextTestCase = null;
		// else
		// nextTestCase = Transfer.testCaseFromInt(testcase, paramstr);
		// Map<String, Object> s = new HashMap<String, Object>();
		// s.put("testCase", nextTestCase);
		//
		// JSONArray jsonArray = new JSONArray();
		// for (int i = 0; i <
		// workMachine.getPool().getExistedBugTuples().size(); i++) {
		// Tuple tuple = workMachine.getPool().getExistedBugTuples().get(i);
		// jsonArray.add(i, Transfer.tupleFromInt(wrongCase, tuple, paramstr));
		// }
		//
		// JSONArray jsonArray2 = new JSONArray();
		// for (int i = 0; i < workMachine.getExtraCases().getTestCaseNum();
		// i++) {
		// TestCase testCase = workMachine.getExtraCases().getAt(i);
		// jsonArray2.add(i, Transfer.testCaseFromInt(testCase, paramstr));
		// }
		//
		// JSONArray jsonArray3 = new JSONArray();
		// for (int i = 0; i < workMachine.getExtraCases().getTestCaseNum();
		// i++) {
		// int pass = workMachine.getExtraCases().getAt(i).testDescription();
		// jsonArray3.add(i, pass);
		// }
		//
		// JSONObject jo = JSONObject.fromObject(s);
		// jo.element("bugtuples", jsonArray);
		// jo.element("testCases", jsonArray2);
		// jo.element("passorfail", jsonArray3);
		// result = jo.toString();
		// System.out.println(jo.toString());
		result = execute.getResult();
		nextTestCase = execute.getNextTestCase();
		return SUCCESS;
	}

	public String getParam_name() {
		return param_name;
	}

	public void setParam_name(String param_name) {
		this.param_name = param_name;
	}

	public String getParam_value() {
		return param_value;
	}

	public void setParam_value(String param_value) {
		this.param_value = param_value;
	}

	public String getFail_test() {
		return fail_test;
	}

	public void setFail_test(String fail_test) {
		this.fail_test = fail_test;
	}

	public List<String> getApproach() {
		return approach;
	}

	public void setApproach(List<String> approach) {
		this.approach = approach;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
