package com.fc.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

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

		result = "";
		List<Tuple> bugs = this.deal();
		for (Tuple bug : bugs)
			result += bug.toString();
		Map<String, Object> s = new HashMap<String, Object>();
		s.put("result", result);
		JSONObject jo = JSONObject.fromObject(s);
		System.out.println(jo.toString());
		result = jo.toString();
		return SUCCESS;
	}

	public List<Tuple> deal() {
		// String[] p_names = param_name.split(" ");
		String[] arrays = coveringArray.split("\n");
		// System.out.println(coveringArray);
		TestSuite suite = new TestSuiteImplement();
		for (String str : arrays) {
			String[] numbs = str.split(" ");
			int[] test = new int[numbs.length];
			for (int i = 0; i < numbs.length; i++) {
				test[i] = Integer.parseInt(numbs[i]);
			}
			TestCaseImplement testCase = new TestCaseImplement(numbs.length);
			testCase.setTestCase(test);
			suite.addTest(testCase);
		}

		// System.out.println(outCome);
		String[] outComes = outCome.split("\n");
		for(int i = 0; i < outComes.length; i++)
			if(outComes[i].equals("0")){
				outComes[i] = "pass";
			}else
				outComes[i] = "fail";

		HashSet<String> set = new HashSet<String>();
		for (String str : outComes)
			set.add(str);

		String[] classes = new String[set.size()];
		classes = set.toArray(classes);

		int[] param = new int[suite.getAt(0).getLength()];

		for (int i = 0; i < param.length; i++) {
			HashSet<Integer> nums = new HashSet<Integer>();
			for (int j = 0; j < suite.getTestCaseNum(); j++) {
				nums.add(suite.getAt(j).getAt(i));
			}
			param[i] = nums.size();
		}

		CTA cta = new CTA();
		try {
			cta.process(param, classes, suite, outComes);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cta.getBugs();
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
