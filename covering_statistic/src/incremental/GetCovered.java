package incremental;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fc.DataCenter.DataCenter;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;
import com.fc.tuple.Tuple;

public class GetCovered {
	public int getTupleIndex(Tuple tuple, int baseIndex, int[] param) {
		int index = 0;

		for (int i = 0; i < tuple.getDegree(); ++i) {
			int temR = tuple.getParamValue()[i];

			for (int j = i + 1; j < tuple.getDegree(); ++j) {
				temR *= param[tuple.getParamIndex()[j]];
			}

			index += temR;
		}

		index += baseIndex;
		return index;
	}

	public long getTupleIndex(Tuple tuple, long baseIndex, int[] param) {
		long index = 0;

		for (int i = 0; i < tuple.getDegree(); ++i) {
			int temR = tuple.getParamValue()[i];

			for (int j = i + 1; j < tuple.getDegree(); ++j) {
				temR *= param[tuple.getParamIndex()[j]];
			}

			index += temR;
		}

		index += baseIndex;
		return index;
	}

	public List<Tuple> getTuplesFromTestCase(TestCase testCase, int degree) {
		Tuple tuple = new Tuple(testCase.getLength(), testCase);
		int[] a = new int[testCase.getLength()];
		for (int i = 0; i < a.length; i++)
			a[i] = i;
		tuple.setParamIndex(a);
		return tuple.getChildTuplesByDegree(degree);
	}

	public Tuple getSimilar(TestCase A, TestCase B) {
		// System.out.println("size :  " +A.getLength());
		// System.out.println(A.getStringOfTest());
		// System.out.println(B.getStringOfTest());
		Tuple tuple = null;
		List<Integer> indexes = new ArrayList<Integer>();
		List<Integer> values = new ArrayList<Integer>();

		for (int i = 0; i < A.getLength(); i++) {
			if (A.getAt(i) == B.getAt(i)) {
				indexes.add(i);
				values.add(A.getAt(i));
			}
		}

		int degree = indexes.size();
		int size = A.getLength();
		// System.out.println(size);
		int[] testOfcase = new int[size];

		for (int i = 0; i < indexes.size(); i++) {
			testOfcase[indexes.get(i)] = values.get(i);
		}

		TestCase implement = new TestCaseImplement(testOfcase);

		tuple = new Tuple(degree, implement);

		int[] indes = new int[degree];
		int j = 0;
		for (int i : indexes) {
			indes[j] = i;
			j++;
		}
		tuple.setParamIndex(indes);

		return tuple;
	}

	public List<Tuple> getSimilar(TestCase A, List<TestCase> set) {
		List<Tuple> similarTuples = new ArrayList<Tuple>();

		for (TestCase B : set) {
			Tuple similar = this.getSimilar(A, B);
			similarTuples.add(similar);
		}

		return similarTuples;
	}

	public int getCovered(TestCase A, DataCenter dataCenter,
			List<Tuple> similarTuples) {

		// the total number of shemas
		int number = dataCenter.getIndex().length;

		// minus the same covered schemas
		// HashSet<Tuple> sameTuples = new HashSet<Tuple> ();
		// // System.out.println("now");
		//
		// for(Tuple t: similarTuples){
		// if(t.getDegree() == dataCenter.getDegree()){
		// sameTuples.add(t);
		// } else if(t.getDegree() > dataCenter.getDegree()){
		// System.out.println("similar len : " + t.getDegree());
		// sameTuples.addAll(t.getChildTuplesByDegree(dataCenter.getDegree()));
		// }
		// }
		// int size = sameTuples.size();

		int size = this.getUniqueNumber(similarTuples, dataCenter.getDegree());

		number -= size;

		if (number < 0) {
			System.out.println("error!");
		}

		return number;
	}

	public int getCovered_old(TestCase A, DataCenter dataCenter,
			List<Tuple> similarTuples) {

		// the total number of shemas
		int number = dataCenter.getIndex().length;

		// minus the same covered schemas
		HashSet<Tuple> sameTuples = new HashSet<Tuple>();
		// System.out.println("now");

		for (Tuple t : similarTuples) {
			if (t.getDegree() == dataCenter.getDegree()) {
				sameTuples.add(t);
			} else if (t.getDegree() > dataCenter.getDegree()) {
				System.out.println("similar len : " + t.getDegree());
				sameTuples.addAll(t.getChildTuplesByDegree(dataCenter
						.getDegree()));
			}
		}
		int size = sameTuples.size();

		// int size = this.getUniqueNumber(similarTuples,
		// dataCenter.getDegree());

		number -= size;

		if (number < 0) {
			System.out.println("error!");
		}

		return number;
	}

	public int getUniqueNumber(List<Tuple> similarTuples, int degree) {
		int result = 0;

		for (int i = 0; i < similarTuples.size(); i++) {
			List<Tuple> before = new ArrayList<Tuple>();
			for (int j = 0; j < i; j++) {
				before.add(similarTuples.get(j));
			}
			result += this.getUniqueFromThoses(similarTuples.get(i), before,
					degree);
		}

		return result;
	}

	// the most important function
	public int getUniqueFromThoses(Tuple tuple, List<Tuple> tuples, int degree) {
//		System.out.println("tuples size : " + tuples.size());
		HashSet<Tuple> xiangjiao = new HashSet<Tuple>();
		for (Tuple t : tuples) {
			Tuple a = getIntersect(tuple, t);
			xiangjiao.addAll(a.getChildTuplesByDegree(degree));
		}
//		System.out.println("hash get  : " + xiangjiao.size());

		return getSizeOfChildTuples(tuple, degree) - xiangjiao.size();
	}
	
	public int getSizeOfChildTuples(Tuple t, int degree){
		int indexNum = 1;
		
		for (int k = 0; k < degree; k++) {
			indexNum *= t.getDegree() - degree + k + 1;
			indexNum /= (k+1);
		}
		
		return indexNum;
		
	}

	public Tuple getIntersect(Tuple A, Tuple B) {
		int Aindex = 0;
		int Bindex = 0;
		// int size = 0;
		// int[] temp = new int[A.degree + B.degree];
		List<Integer> indexes = new ArrayList<Integer>();
		List<Integer> values = new ArrayList<Integer>();

		while (Aindex < A.getDegree() && Bindex < B.getDegree()) {
			if (A.getParamIndex()[Aindex] > B.getParamIndex()[Bindex]) {
				// temp[size] = B.getParamIndex()[Bindex];
				Bindex++;
			} else if (A.getParamIndex()[Aindex] < B.getParamIndex()[Bindex]) {
				// temp[size] = A.getParamIndex()[Aindex];
				Aindex++;
			} else if (A.getParamIndex()[Aindex] == B.getParamIndex()[Bindex]) {
				if (A.getParamValue()[Aindex] == B.getParamValue()[Bindex]) {
					indexes.add(Aindex);
					values.add(A.getParamValue()[Aindex]);
				}
				// temp[size] = A.getParamIndex()[Aindex];
				Aindex++;
				Bindex++;
			}
			// size++;
		}
		int[] paramIndex = new int[indexes.size()];
		for (int i = 0; i < paramIndex.length; i++)
			paramIndex[i] = indexes.get(i);
		Tuple result = new Tuple(indexes.size(), A.getTestCase());
		result.setParamIndex(paramIndex);
		return result;

	}

	public int getCovered(int index, TestSuite suite, DataCenter dataCenter) {
		List<TestCase> set = new ArrayList<TestCase>();
		for (int i = 0; i < index; i++) {
			set.add(suite.getAt(i));
		}
		// System.out.println("similar start");
		List<Tuple> simliarTuples = this.getSimilar(suite.getAt(index), set);

		// System.out.println("get covered start");
		int covered = this.getCovered(suite.getAt(index), dataCenter,
				simliarTuples);

		return covered;
	}
	
	
	public int getCovered_old(int index, TestSuite suite, DataCenter dataCenter) {
		List<TestCase> set = new ArrayList<TestCase>();
		for (int i = 0; i < index; i++) {
			set.add(suite.getAt(i));
		}
		// System.out.println("similar start");
		List<Tuple> simliarTuples = this.getSimilar(suite.getAt(index), set);

		// System.out.println("get covered start");
		int covered = this.getCovered_old(suite.getAt(index), dataCenter,
				simliarTuples);

		return covered;
	}

	public int[] dealDetection(int start, int end, TestSuite suite,
			DataCenter dataCenter) {
		int[] result = new int[end - start];

		// System.out.println("all : " + result.length);
		for (int i = start; i < end; ++i) {
			long time = System.currentTimeMillis();
			System.out.println("test case : " + i);

			result[i - start] = this.getCovered(i, suite, dataCenter);

			long time2 = System.currentTimeMillis();
			time2 -= time;
			System.out.println("time (s): " + time2 / 1000);
		}

/*		for(int i : result){
			System.out.print(i + "  ");
		}*/
		System.out.println();
		return result;
	}
	
	
	public int[] dealDetection_old(int start, int end, TestSuite suite,
			DataCenter dataCenter) {
		int[] result = new int[end - start];

		// System.out.println("all : " + result.length);
		for (int i = start; i < end; ++i) {
			long time = System.currentTimeMillis();
			System.out.println("test case : " + i);

			result[i - start] = this.getCovered_old(i, suite, dataCenter);

			long time2 = System.currentTimeMillis();
			time2 -= time;
			System.out.println("time (s): " + time2 / 1000);
		}

//		for(int i : result){
//			System.out.print(i + "  ");
//		}
		System.out.println();
		return result;
	}

	public static void main(String[] args) {
		int[] param = new int[] { 2, 2, 2, 2, 2 };
		int degree = 2;
		DataCenter dataCenter = new DataCenter();
		dataCenter.reset(param, degree);
		TestSuite suite = new TestSuiteImplement();
		int[][] tests = new int[][] { { 0, 0, 0, 0, 0 }, { 1, 1, 1, 1, 1 },
				{ 0, 0, 0, 1, 1 }, { 1, 1, 1, 0, 0 }, { 1, 0, 1, 0, 1 },
				{ 0, 1, 0, 1, 0 } };
		
		for (int[] test : tests){
			TestCase testCase = new TestCaseImplement(test);
			suite.addTest(testCase);
		}
		
		GetCovered gc = new GetCovered();
		gc.dealDetection(0, suite.getTestCaseNum(), suite, dataCenter);
		gc.dealDetection_old(0, suite.getTestCaseNum(), suite, dataCenter);
	}

}
