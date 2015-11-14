package output;

import java.util.HashMap;

public class OutputSet {

	public static final String PASS = "PASS";

	private HashMap<String, Integer> record;

	private HashMap<Integer, String> recordReverse;

	private int currentIndex;

	public OutputSet() {
		record = new HashMap<String, Integer>();
		recordReverse = new HashMap<Integer, String>();
		currentIndex = 0;

		this.add(PASS);
	}

	private void add(String str) {
		record.put(str, currentIndex);
		recordReverse.put(currentIndex, str);
		currentIndex++;
	}

	public void pendingStr(String str) {
		if (!record.containsKey(str)) {
			add(str);
		}
	}

	public int get(String str) {
		return this.record.get(str);
	}

	public String get(Integer i) {
		return this.recordReverse.get(i);
	}

	public int getCurIndex() {
		return this.currentIndex;
	}
}
