package gc;

import com.fc.tuple.Tuple;

import InputOutput.ReadInput;

public class Main {
	// weak
	ReadInput in = new ReadInput();
	
	public void print(String pathOftestCase, String pathOfMFS, String pathofcoveredLines){
		in.readParam(pathOftestCase);
		in.readBugs(pathOfMFS);
		in.readCoveredLines(pathofcoveredLines);
		GetTheWeakGuannteedCodeOfSchema gwg = new GetTheWeakGuannteedCodeOfSchema();
		GetTheStrongGuannteedCodeOfSchema gsg = new GetTheStrongGuannteedCodeOfSchema();
		for (Tuple tuple : in.getBugs()) {
			System.out.println(tuple.toString());
			System.out.println("weak");
			int[] wg = gwg.wgc(tuple, in.getParam(), in.getCoveredLines());
			for (int i : wg)
				System.out.print(i + " ");
			System.out.println();
			System.out.println("Strong");
			int[] sg = gsg.sgc(tuple, in.getParam(), in.getCoveredLines());
			for (int i : sg)
				System.out.print(i + " ");
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.print("v1/result_of_testCase.txt", "v1/bug_ot1.txt", "v1/spectra.txt");
	}
	// strong
}
