package ct;

import interaction.CoveringManage;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

import interaction.DataCenter;

public class AETG_Constraints_Seeds extends AETG_Constraints {

	private List<int[]> seeds;

	public AETG_Constraints_Seeds(DataCenter dataCenter) {
		super(dataCenter);
		seeds = new ArrayList<int[]>();
	}

	public void addSeeds(List<int[]> seeds) {
		this.seeds.addAll(seeds);
		for (int[] seed : seeds) {
			CoveringManage cm = new CoveringManage(dataCenter);
			unCovered = cm.setCover(unCovered, coveredMark, seed);
		}
	}

	public void addSeeds(int[] coveredMark, int uncovred) {
		for (int i = 0; i < coveredMark.length; i++)
			this.coveredMark[i] = coveredMark[i];
		this.unCovered = uncovred;
	}

	public static void main(String[] args) {
		int[] param = new int[] { 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		DataCenter dataCenter = new DataCenter(param, 2);
		AETG_Constraints_Seeds aetg = new AETG_Constraints_Seeds(dataCenter);

		// next implicat (- , -, 1, 1,- , -, -, - )
		TestCaseImplement testCaseForTuple = new TestCaseImplement(
				dataCenter.param_num);
		int[] test = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		testCaseForTuple.setTestCase(test);

		Tuple tuple = new Tuple(2, testCaseForTuple);
		tuple.set(0, 1);
		tuple.set(1, 2);

		TestCaseImplement testCaseForTupl2e = new TestCaseImplement(
				dataCenter.param_num);
		int[] test2 = new int[] { 1, 0, 1, 1, 1, 1, 1, 1, 1 };
		testCaseForTupl2e.setTestCase(test2);
		Tuple tuple2 = new Tuple(2, testCaseForTupl2e);
		tuple2.set(0, 1);
		tuple2.set(1, 3);

		// child
		Tuple tuple3 = new Tuple(1, testCaseForTuple);
		tuple3.set(0, 7);

		// tuple.set(2, 3);

		List<Tuple> MFS = new ArrayList<Tuple>();
		MFS.add(tuple);
		MFS.add(tuple2);
		MFS.add(tuple3);

		aetg.addConstriants(MFS);
		aetg.process();
		// int index = aetg.getIndexOfTuple(tuple);
		// System.out.println(index);
		// Tuple tu = aetg.getTupleFromIndex(63);
		// System.out.println(tu.toString());

	}

}
