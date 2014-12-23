package incremental;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ct.AETG;
import ct.GenCoveringArray_From_Existing;

public class Decrease {
	public HashMap<Integer, List<int[]>> rawTestCases;
	public int minDegree;
	public int maxDegree;

	public HashMap<Integer, List<int[]>> incrementalTestCases;

	public Decrease() {
		rawTestCases = new HashMap<Integer, List<int[]>>();
		incrementalTestCases = new HashMap<Integer, List<int[]>>();
	}

	public void execute(int[] param, int minDegree, int maxDegree) {
		rawTestCases.clear();
		this.minDegree = minDegree;
		this.maxDegree = maxDegree;

		for (int i = maxDegree; i >= minDegree; i--) {
			DataCenter dataCenter = new DataCenter(param, i);
			if (i == maxDegree) {
				AETG aetg = new AETG(dataCenter);
				aetg.process();
				this.rawTestCases.put(i, aetg.coveringArray);
			} else {
				GenCoveringArray_From_Existing ge = new GenCoveringArray_From_Existing(
						dataCenter, rawTestCases.get(i + 1));
				ge.process();
				this.rawTestCases.put(i, ge.coveringArray);
			}
		}

		transform();

	}

	public void transform() {
		for (int i = this.minDegree; i <= this.maxDegree; i++) {
			if (i == this.minDegree) {
				this.incrementalTestCases.put(i, rawTestCases.get(i));
			} else {
				List<int[]> t1 = this.rawTestCases.get(i);
				List<int[]> t2 = this.rawTestCases.get(i - 1);
				List<int[]> result = new ArrayList<int[]>();
				for (int[] t : t1) {
					if (!contains(t2, t))
						result.add(t);
				}
				this.incrementalTestCases.put(i, result);
			}
		}
	}

	public boolean contains(List<int[]> base, int[] test) {
		for (int[] c : base) {
			if (c.length != test.length)
				continue;

			boolean equal = true;
			for (int i = 0; i < c.length; i++) {
				if (c[i] != test[i]) {
					equal = false;
					break;
				}
			}
			if (equal)
				return true;

		}
		return false;
	}

	public List<int[]> getPreviousTestCases(int minDegree, int nowDegree) {
		List<int[]> result = new ArrayList<int[]>();

		for (int i = minDegree; i < nowDegree; i++) {
			result.addAll(this.incrementalTestCases.get(i));
		}

		return result;
	}

	public void outputRaw() {
		for (int i = this.minDegree; i <= this.maxDegree; i++) {
			List<int[]> testCases = this.rawTestCases.get(i);
			for (int[] test : testCases)
				this.print(test);
			System.out.println("degreee : " + i);
		}
	}

	public void outputSize(){
		System.out.println("size");
		int size = 0;
		for(int i = this.minDegree; i <= this.maxDegree ; i++){
			List<int[]> testCases = this.incrementalTestCases.get(i);
			size += testCases.size();
			System.out.print("  degree : " + size);
		}
		System.out.println();
	}
	
	public int[] getSize(){
		int[] result = new int[maxDegree - minDegree + 1];
		int size = 0;
		for(int i = this.minDegree; i <= this.maxDegree ; i++){
			List<int[]> testCases = this.incrementalTestCases.get(i);
			size += testCases.size();
			result[i - minDegree] =   size;
		}
		
		return result;
	}
	
	public void output() {
		for (int i = this.minDegree; i <= this.maxDegree; i++) {
			List<int[]> testCases = this.incrementalTestCases.get(i);
			for (int[] test : testCases)
				this.print(test);
			System.out.println("degreee : " + i);
		}
	}

	public void print(int[] s) {
		for (int i : s)
			System.out.print(i + "  ");
		System.out.println();
	}

	public static void main(String[] args) {
		Decrease decrease = new Decrease();
		int[] param = new int[] { 2, 2, 2, 2 };
		decrease.execute(param, 2, 3);
		decrease.output();
		System.out.println();
		decrease.outputRaw();
		// DataCenter dataCenter = new DataCenter(param, 3);
		// AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
		// aetg_seeds.process();
	}
}
