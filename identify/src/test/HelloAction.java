package test;

import com.opensymphony.xwork2.Action;

public class HelloAction implements Action {

	private String message;

	public String getMessage() {

		return message;

	}

	public String execute() throws Exception {

		message = "Hello,Struts2!";

		return SUCCESS;

	}

}