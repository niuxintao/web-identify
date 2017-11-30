package getCovered;


import java.util.List;


public class GetCovered {
	DataCenter dataCenter;
	public int[] coveredMark; 
	public Integer unCovered;
	
	public GetCovered(int degree, int[] param){
		dataCenter = new DataCenter(param, degree);
		coveredMark = new int[dataCenter.coveringArrayNum];
		unCovered = this.coveredMark.length;
	}
	public int getUncovered(int[] testCase, int degree) {
		int tempCover = 0;
		TestCase testCaseForTuple = new TestCaseImplement(dataCenter.n);
		for (int i = 0; i < testCase.length; i++)
			testCaseForTuple.set(i, testCase[i]);

		Tuple tuple = new Tuple(testCase.length, testCaseForTuple);
		int[] indexset = new int[testCase.length];
		for (int i = 0; i < indexset.length; i++)
			indexset[i] = i;
		tuple.setParamIndex(indexset);

		// System.out.print(tuple.toString());;

		List<Tuple> child = tuple.getChildTuplesByDegree(dataCenter.degree);

		for (Tuple ch : child) {
			int ind = this.getIndexOfTuple(ch);
			// System.out.println(ind + " " +ch.toString());
			// System.out.println(coveredMark[ind]);
			if (coveredMark[ind] == 0)
				tempCover++;
		}

		return tempCover;
	}
	
	
	public void setTestCaseCov(int[] testCase){
		CoveringManage cm = new CoveringManage(dataCenter);
		unCovered = cm.setCover(unCovered, coveredMark, testCase);
	}
	
	public int getIndexOfTuple(Tuple tuple) {
		int result = 0;
		int[] values = tuple.getParamValue();

		// System.out.println("tuple" + tuple.toString());
		// print(tuple.getParamIndex());
		// System.out.println(tuple.toString() + " " +
		// CoveringManage.getIndex(tuple));
		CoveringManage cm = new CoveringManage(dataCenter);

		int basicIndex = dataCenter.index[cm.getIndex(tuple)];

		for (int j = 0; j < dataCenter.degree; j++) {
			int k = j + 1;
			int temR = values[j];
			while (k < dataCenter.degree) {
				temR *= dataCenter.param[tuple.getParamIndex()[k]];
				k++;
			}
			basicIndex += temR;
		}
		result = basicIndex;

		return result;
	}


}
