package incremental;

import interaction.DataCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import ct.AETG_Seeds;

public class Increase {

	public HashMap<Integer, List<int[]>> incrementalTestCases;
	public  int minDegree;
	public int maxDegree;

	public Increase() {
		incrementalTestCases = new HashMap<Integer, List<int[]>>();
	}

	public void execute(int[] param, int minDegree, int maxDegree) {
		incrementalTestCases.clear();
		this.minDegree = minDegree;
		this.maxDegree  = maxDegree;
				
		for (int i = minDegree; i <= maxDegree; i++) {
			DataCenter dataCenter = new DataCenter(param, i);
			AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
			aetg_seeds.addSeeds(this.getPreviousTestCases(minDegree, i));
			aetg_seeds.process();
			this.incrementalTestCases.put(i, aetg_seeds.coveringArray);
		}

	}

	public List<int[]> getPreviousTestCases(int minDegree, int nowDegree) {
		List<int[]> result = new ArrayList<int[]>();

		for (int i = minDegree; i < nowDegree; i++) {
			result.addAll(this.incrementalTestCases.get(i));
		}

		return result;
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
	
	public void output(){
		for(int i = this.minDegree; i <= this.maxDegree ; i++){
			List<int[]> testCases = this.incrementalTestCases.get(i);
			for(int[] test : testCases)
				this.print(test);
			System.out.println("degreee : " + i);
		}
	}
	
	public void print(int[] s){
		for(int i : s)
           System.out.print(i + "  ");
		System.out.println();
	}
	
	
	public static void main(String[] args){
		Increase incease = new Increase();
		int[] param = new int[] {2, 2, 2, 2};
		incease.execute(param, 2, 3);
		incease.output();
//		DataCenter dataCenter = new DataCenter(param, 3);
//		AETG_Seeds aetg_seeds = new AETG_Seeds(dataCenter);
//		aetg_seeds.process();
	}
}
