package ct;

import java.util.HashSet;
import java.util.List;

import com.fc.tuple.DealTupleOfIndex;
import com.fc.tuple.Tuple;

public class GetFirstParameterValue {
	// private DataCenter dataCenerMinus1;

	public GetFirstParameterValue() {
		// this.dataCenerMinus1 = dataCenterMinus1;
	}

	public Tuple selectFirst(int[] coveredMark, int t_1pairs,
			DealTupleOfIndex DOI, DealTupleOfIndex DOIminus1) {
		// long current = System.currentTimeMillis();
		int[] paramStatic = new int[t_1pairs];

		int bestIndex = -1;
		int bestUncovered = -1;

		for (int i = 0; i < coveredMark.length; i++) {
			if (coveredMark[i] == 0) {
				Tuple tuple = DOI.getTupleFromIndex(i);
				List<Tuple> childminus1 = tuple.getChildTuplesByDegree(tuple
						.getDegree() - 1);
				for (Tuple child : childminus1) {
					int index = DOIminus1.getIndexOfTuple(child);
					paramStatic[index] += 1;
				}

			}
		}

		for (int i = 0; i < paramStatic.length; i++) {
			if (paramStatic[i] > bestUncovered) {
				bestUncovered = paramStatic[i];
				bestIndex = i;
			}
		}

		Tuple result = DOIminus1.getTupleFromIndex(bestIndex);
		return result;
	}

	public Tuple selectFirst(HashSet<Tuple> cannot, int[] coveredMark,
			int t_1pairs, DealTupleOfIndex DOI, DealTupleOfIndex DOIminus1) {
		// long current = System.currentTimeMillis();

		int[] paramStatic = new int[t_1pairs];

		int bestIndex = -1;
		int bestUncovered = -1;

		for (int i = 0; i < coveredMark.length; i++) {
			if (coveredMark[i] == 0) {
				Tuple tuple = DOI.getTupleFromIndex(i);
				List<Tuple> childminus1 = tuple.getChildTuplesByDegree(tuple
						.getDegree() - 1);
				for (Tuple child : childminus1) {
					int index = DOIminus1.getIndexOfTuple(child);
					paramStatic[index] += 1;
				}

			}
		}

		for (int i = 0; i < paramStatic.length; i++) {
			Tuple tempij = DOIminus1.getTupleFromIndex(i);
			if (cannot.contains(tempij))
				continue;

			if (paramStatic[i] > bestUncovered) {
				bestUncovered = paramStatic[i];
				bestIndex = i;
			}
		}

		Tuple result = DOIminus1.getTupleFromIndex(bestIndex);
		return result;

	}

	/**
	 * 
	 * @param part   should be kept
	 * @param cannot   should not contain 
	 * @param coveredMark   the covered pairs array.
	 * @param t_1pairs    the number of all the  t-1 degree schemas
 	 * @param DOI           get Tuple from index (t)
	 * @param DOIminus1    get Tuple from index (t - 1)
	 * @return
	 */
	public Tuple selectFirst(Tuple part, HashSet<Tuple> cannot,
			int[] coveredMark, int t_1pairs, DealTupleOfIndex DOI,
			DealTupleOfIndex DOIminus1 , int[] param) {
		// long current = System.currentTimeMillis();

		int[] paramStatic = new int[t_1pairs];

		Tuple bestTuple = null;
		int bestUncovered = -1;

		for (int i = 0; i < coveredMark.length; i++) {
			if (coveredMark[i] == 0) {
				Tuple tuple = DOI.getTupleFromIndex(i);
				List<Tuple> childminus1 = tuple.getChildTuplesByDegree(tuple
						.getDegree() - 1);
				for (Tuple child : childminus1) {
					int index = DOIminus1.getIndexOfTuple(child);
					paramStatic[index] += 1;
				}

			}
		}

		List<Tuple> fathersOfPart = part.getFatherTuplesByDegree(DOIminus1
				.getDegree(), param );
		for (Tuple father : fathersOfPart) {
			if (cannot.contains(father))
				continue;
			int index = DOIminus1.getIndexOfTuple(father);
			if (paramStatic[index] > bestUncovered) {
				bestUncovered = paramStatic[index];
				bestTuple = father;
			}
		}

		return bestTuple;
	}

}
