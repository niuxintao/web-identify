package experiment;

import java.util.List;

import com.fc.tuple.Tuple;

public class SimilarityMFS {

	public static double getSimilarity(Tuple a, Tuple b) {
		double result = 0;
		double max = a.getDegree() > b.getDegree() ? a.getDegree() : b
				.getDegree();
		result = (double) (getSameElements(a, b)) / max;
		return result;
	}

	public static int getSameElements(Tuple A, Tuple B) {
		int Aindex = 0;
		int Bindex = 0;

		int result = 0;

		while (Aindex < A.getDegree() || Bindex < B.getDegree()) {
			if (Aindex == A.getDegree()) {
				Bindex++;
			} else if (Bindex == B.getDegree()) {
				Aindex++;
			} else if (A.getParamIndex()[Aindex] > B.getParamIndex()[Bindex]) {
				Bindex++;
			} else if (A.getParamIndex()[Aindex] < B.getParamIndex()[Bindex]) {
				Aindex++;
			} else if (A.getParamIndex()[Aindex] == B.getParamIndex()[Bindex]) {
				if (A.getParamValue()[Aindex] == B.getParamValue()[Aindex])
					result++;
				Aindex++;
				Bindex++;
			}
		}

		return result;
	}

	public static double getSimilarity(Tuple t, List<Tuple> MFS) {
		double result = 0;
		for (Tuple mfs : MFS) {
			double temp = getSimilarity(t, mfs);
			if (temp > result) {
				result = temp;
				if (result == 1.0)
					break;
			}
		}

		return result;
	}

	public static double getSimilarity(List<Tuple> identified, List<Tuple> MFS) {
		double result = 0;
		for (Tuple t : identified) {
			result += getSimilarity(t, MFS);
		}
		result /= identified.size();

		return result;
	}

}
