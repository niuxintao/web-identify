package com.fc.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GenerateNext extends ActionSupport {

	private String run;
	private String result;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5201556244452326166L;

	private String nextTestCase;

	public String getNextTestCase() {
		return nextTestCase;
	}

	public String execute() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		String method = (String) session.get("APPROACH");
		Execute execute = new Execute();
		execute.setRun(run);
		if (method.equals("Aug_TRT")) {
			execute.executeAugTRT();
		} else if (method.equals("TRT")) {
			execute.executeTRT();
		} else if (method.equals("FIC_BS")) {
			execute.executeFIC_BS();
		} else if (method.equals("OFOT")) {
			execute.executeOFOT();
		}
		result = execute.getResult();
		return SUCCESS;
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

}
