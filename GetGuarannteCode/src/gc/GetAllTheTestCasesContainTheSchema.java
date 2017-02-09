package gc;

import java.util.ArrayList;
import java.util.List;

import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.tuple.Tuple;

public class GetAllTheTestCasesContainTheSchema {

	public List<int[]> bench(int[] param, Tuple tuple) {
		int[] part = new int[param.length];
		for (int i = 0; i < part.length; i++)
			part[i] = 0;
		for (int i = 0; i < tuple.getDegree(); i++)
			part[tuple.getParamIndex()[i]] = tuple.getParamValue()[i];

		int index = 0;

		List<int[]> result = new ArrayList<int[]>();
		this.testBench(result, param, part, index, tuple);
		return result;
		// TODO Auto-generated catch block
	}

	public void testBench(List<int[]> result, int[] set, int[] part, int index,
			Tuple tuple) {
		int nextIndex = index + 1;
		if (isContainIndex(index, tuple)) // omit the index in tuples
		{
			int[] partCur = new int[set.length];
			System.arraycopy(part, 0, partCur, 0, set.length);
			if (nextIndex == set.length) {
				result.add(partCur);
			} else {
				testBench(result, set, partCur, nextIndex, tuple);
			}
		} else {
			for (int i = 0; i < set[index]; i++) {
				int[] partCur = new int[set.length];
				System.arraycopy(part, 0, partCur, 0, set.length);
				partCur[index] = i;
				if (nextIndex == set.length) {
					result.add(partCur);
				} else {
					testBench(result, set, partCur, nextIndex, tuple);
				}
			}
		}
	}

	public boolean isContainIndex(int index, Tuple tuple) {
		for (int i = 0; i < tuple.getDegree(); i++)
			if (index == tuple.getParamIndex()[i])
				return true;
		return false;
	}

	List<TestCase> getAllTheTestCasesContainTheSchema(Tuple tuple, int[] param) {
		List<int[]> tr = this.bench(param, tuple);
		List<TestCase> result = new ArrayList<TestCase>();
		for (int[] tri : tr) {
			TestCase tm = new TestCaseImplement(tri);
			result.add(tm);
		}
		return result;
	}

	public static void main(String[] args) {
		int[] param = { 3, 2, 2, 3, 5, 2 };
		TestCase testCase = new TestCaseImplement(
				new int[] { 1, 1, 1, 1, 1, 1 });
		Tuple tuple = new Tuple(2, testCase);
		tuple.setParamIndex(new int[] { 2, 3 });
		GetAllTheTestCasesContainTheSchema gtcs = new GetAllTheTestCasesContainTheSchema();
		List<TestCase> r = gtcs
				.getAllTheTestCasesContainTheSchema(tuple, param);
		for (TestCase ss : r)
			System.out.println(ss.getStringOfTest());
	}
}
