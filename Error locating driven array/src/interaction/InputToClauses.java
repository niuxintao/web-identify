package interaction;

import java.util.ArrayList;
import java.util.List;

public class InputToClauses {

	int[] param;

	int[] indexOfFactor;

	int allValue;

	public int getAllValue() {
		return this.allValue;
	}

	// the constraints for the SUT's parameter (to transform it to the boolean
	// variable)
	private List<int[]> clauses;

	public List<int[]> getClauses() {
		return clauses;
	}

	public InputToClauses(int[] param) {
		this.param = param;
		int cur = 0;
		indexOfFactor = new int[param.length];
		clauses = new ArrayList<int[]>();
		for (int i = 0; i < param.length; i++) {
			// System.out.println(i);
			indexOfFactor[i] = cur;
			cur += param[i];

			getAtLeast(i);
			getAtMost(i);

		}
		this.allValue = cur;

	}

	private void getAtMost(int i) {
		for (int j = 0; j < param[i]; j++) {
			for (int k = j + 1; k < param[i]; k++) {
				int[] atMost = new int[2];
				atMost[0] = -(indexOfFactor[i] + j + 1);
				atMost[1] = -(indexOfFactor[i] + k + 1);
				clauses.add(atMost);
			}
		}
	}

	private void getAtLeast(int i) {
		int[] claueseAtLeast = new int[param[i]];
		for (int j = 0; j < param[i]; j++) {
			claueseAtLeast[j] = indexOfFactor[i] + j + 1;
		}
		clauses.add(claueseAtLeast);
	}

	public int[] combinationToClause(int[] index, int[] value) {
		int[] clause = new int[index.length];
		for (int i = 0; i < clause.length; i++) {
			clause[i] = this.indexOfFactor[index[i]] + value[i] + 1;
		}
		return clause;

	}

	public List<int[]> clauseToCombination(int[] combination) {
		List<int[]> result = new ArrayList<int[]>();
		int[] indexs = new int[combination.length];
		int[] values = new int[combination.length];
		result.add(indexs);
		result.add(values);

		int i = 0;

		for (int clause : combination) {
			int index = this.getIndex(clause, indexOfFactor);
			int value = clause - 1 - indexOfFactor[index];

			indexs[i] = index;
			values[i] = value;
			// int[] c = new int[2];
			// c[0] = index;
			// c[1] = value;
			// result.add(c);

			i++;
		}
		// binary search to find the index
		return result;
	}

	public int getIndex(int clause, int[] values) {
		// clause is from 1, and the index is from 0
		// System.out.println(clause);
		int t_clause = clause - 1;

		int start = 0;
		int tail = values.length - 1;
		int result = tail; // intial to be the maximal, the following find if
		// not find, must be the one that is the maximal

		while (tail > start) {
			int middle = (int) (0.5 * (start + tail));
			// System.out.println("middle : " + middle);
			if (t_clause >= values[middle] && t_clause < values[middle + 1]) {
				result = middle;
				break;
			} else if (t_clause >= values[middle + 1]) {
				start = middle + 1;

			} else if (t_clause < values[middle]) {
				tail = middle - 1;
			}
			// System.out.println("start : " + start + " tail : " + tail);
		}

		if (tail == 0) {
			result = 0;
		}

		return result;
	}

	public List<int[]> getParentCombination(int[] combination) {
		List<int[]> result = new ArrayList<int[]>();
		int insertLocation = 0;

		List<int[]> pair = this.clauseToCombination(combination);
		int[] index = pair.get(0);
		// for (int i = 0; i < pair.size(); i++)
		// index[i] = pair.get(i)[0];

		for (int i = 0; i < this.param.length; i++) {
			// insertLocation = 0;

			// find a location to insert the new value
			while (insertLocation < combination.length
					&& i == index[insertLocation]) {
				i++;
				insertLocation++;
			}

			// System.out.println(i + " |  " + insertLocation);

			// the last factor
			if (i >= this.param.length)
				break;

			for (int j = 0; j < this.param[i]; j++) {
				int clause = this.indexOfFactor[i] + j + 1;

				int[] father = new int[combination.length + 1];
				for (int k = 0; k < father.length; k++) {
					if (k < insertLocation)
						father[k] = combination[k];
					else if (k == insertLocation)
						father[k] = clause;
					else
						father[k] = combination[k - 1];
				}

				result.add(father);
			}

		}
		return result;
	}

	public List<int[]> getChildCombination(int[] combination) {
		List<int[]> result = new ArrayList<int[]>();

		for (int i = 0; i < combination.length; i++) {
			// sequential delete the ith factor in the combination
			int[] child = new int[combination.length - 1];
			for (int j = 0; j < combination.length - 1; j++) {
				if (i > j)
					child[j] = combination[j];
				else
					child[j] = combination[j + 1];
			}
			result.add(child);
		}

		return result;
	}

	public static void main(String[] args) {

		int[] param = new int[] { 2, 2, 2, 2, 2 };
		InputToClauses ic = new InputToClauses(param);
		int[] clause = new int[] { 3, 8 };
		// List<int[]> index = ic.clauseToCombination(clause);
		// for (int[] pair : index) {
		// System.out.println(pair[0] + " " + pair[1]);
		// }

		List<int[]> child = ic.getChildCombination(clause);
		for (int[] ch : child) {
			for (int i : ch) {
				System.out.print(i + " ");
			}
			System.out.println();
		}

		List<int[]> father = ic.getParentCombination(clause);
		for (int[] ch : father) {
			for (int i : ch) {
				System.out.print(i + " ");
			}
			System.out.println();
		}

	}
}
