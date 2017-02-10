package gc;

import java.util.HashSet;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.tuple.Tuple;

public class GetTheWeakGuannteedCodeOfSchema {

	public int[] wgc(Tuple tuple, int[] param, List<List<Integer>> CoveredLinesOfTestCase) {
		GetAllTheTestCasesContainTheSchema gtcs = new GetAllTheTestCasesContainTheSchema();
		List<TestCase> r = gtcs
				.getAllTheTestCasesContainTheSchema(tuple, param);
		int[] index = new int[r.size()];
		GetTheIndexOfTestCases a = new GetTheIndexOfTestCases();
		for (int i = 0; i < index.length; i++)
			index[i] = a.getIndexofTestCase(r.get(i), param);

		return this.getInterSect(index, CoveredLinesOfTestCase);

	}

	public HashSet<Integer> getHSfint(List<Integer> r) {
		HashSet<Integer> a = new HashSet<Integer>();
		for (int i : r)
			a.add(i);
		return a;
	}

	public int[] getInterSect(int[] index, List<List<Integer>> CoveredLinesOfTestCase) {
		int i = 0;
		HashSet<Integer> cur = getHSfint(CoveredLinesOfTestCase.get(index[i]));
		for (int j = i + 1; j < index.length; j++) {
			HashSet<Integer> curN = getHSfint(CoveredLinesOfTestCase.get(index[j]));
			cur.retainAll(curN);
			if (cur.size() == 0)
				break;
		}
		int[] result = new int[cur.size()];
		int k = 0;
		for (Integer cr : cur) {
			result[k] = cr;
			k++;
		}
		return result;
	}

	// find all the test cases

	// find all the index of test cases

	// find the covered code of the test cases

	// intersection
}
