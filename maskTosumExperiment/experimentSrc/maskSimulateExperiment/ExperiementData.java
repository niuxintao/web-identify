package maskSimulateExperiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class ExperiementData {

	private int[] parameter;
	private List<TestCase> allCases;
	private List<Integer> result;

	private HashMap<Integer, List<Integer>> lowerPriority;

	private List<TestCase> rightCases;

	private HashMap<Integer, List<TestCase>> wrongCases;

	private HashMap<Integer, List<Tuple>> bugsTable;

	public ExperiementData() {
		this.init();
	}

	public void init() {
		this.allCases = new ArrayList<TestCase>();
		this.result = new ArrayList<Integer>();
		this.lowerPriority = new HashMap<Integer, List<Integer>>();
		this.rightCases = new ArrayList<TestCase>();
		this.wrongCases = new HashMap<Integer, List<TestCase>>();
		this.bugsTable = new HashMap<Integer, List<Tuple>>();
	}

	public void setParam(int[] param) {
		this.parameter = param;
	}

	public void setLowerPriority(HashMap<Integer, List<Integer>> lowerPriority) {
		this.lowerPriority = lowerPriority;
	}

	public void addCodeAndPriority(Integer code, List<Integer> lower) {
		this.lowerPriority.put(code, lower);
	}

	public void setBugs(HashMap<Integer, List<Tuple>> bugsTable) {
		this.bugsTable = bugsTable;
		for (Integer code : bugsTable.keySet()) {
			List<Tuple> mfs = bugsTable.get(code);
			List<Integer> higher = this.lowerPriority.get(code);
			List<Tuple> higherBugs = new ArrayList<Tuple>();
			for (Integer high : higher) {
				higherBugs.addAll(this.bugsTable.get(high));
			}

			this.setWrongCases(code, mfs, higherBugs);
		}
	}

	public void setWrongCases(Integer code, List<Tuple> mfs,
			List<Tuple> higherBugs) {

	}

	// public void addBug(Integer code, List<Tuple> bugs) {
	// this.bugsTable.put(code, bugs);
	//
	// }

	public List<Tuple> getMFS() {
		List<Tuple> result = new ArrayList<Tuple>();

		for (Entry<Integer, List<Tuple>> entry : bugsTable.entrySet())
			result.addAll(entry.getValue());

		return result;
	}

	public int[] getParameter() {
		return parameter;
	}

	public List<TestCase> getAllCases() {
		return allCases;
	}

	public List<Integer> getResult() {
		return result;
	}

	// public List<Integer> getBugCode() {
	// return BugCode;
	// }

	public HashMap<Integer, List<Integer>> getLowerPriority() {
		return lowerPriority;
	}

	public List<TestCase> getRightCases() {
		return rightCases;
	}

	public HashMap<Integer, List<TestCase>> getWrongCases() {
		return wrongCases;
	}

	public HashMap<Integer, List<Tuple>> getBugsTable() {
		return bugsTable;
	}

}
