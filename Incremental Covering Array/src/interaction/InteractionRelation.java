package interaction;

import java.util.ArrayList;
import java.util.List;

public class InteractionRelation {
	private int[] param;
	private InputToClauses ic;

	private List<int[]> interactions;

	public InputToClauses getIc() {
		return ic;
	}

	public List<int[]> getInteractions() {
		return this.interactions;
	}

	public InteractionRelation(int[] param) {
		this.param = param;
		this.ic = new InputToClauses(param);
		interactions = new ArrayList<int[]>();
	}

	public void getTwayInetarction(int t) {
		interactions = new ArrayList<int[]>();
		this.loopIndex(null, t);
	}

	public void loopIndex(int[] current, int degree) {
		int len = 0;
		if (current != null)
			len = current.length;
		if (len == degree) {
			int[] index = current;

			this.loopValue(index, null);

		} else {
			// beyond the possibility

			if (len > 0 && current[len - 1] + 1 + (degree - len) > param.length)
				return;

			for (int i = len > 0 ? current[len - 1] + 1 : 0; i < param.length; i++) {
				int[] newCurrent = new int[len + 1];
				for (int j = 0; j < len; j++)
					newCurrent[j] = current[j];
				newCurrent[len] = i;
				this.loopIndex(newCurrent, degree);
			}
		}
	}

	public void loopValue(int[] index, int[] current) {
		int len = 0;
		int degree = index.length;

		if (current != null)
			len = current.length;

		if (len == degree) {
			int[] value = current;

			int[] clause = this.ic.combinationToClause(index, value);
			this.interactions.add(clause);

		} else {
			// beyond the possibility
			for (int k = 0; k < param[len]; k++) {
				int[] newCurrent = new int[len + 1];
				for (int j = 0; j < len; j++)
					newCurrent[j] = current[j];
				newCurrent[len] = k;
				this.loopValue(index, newCurrent);
			}
		}
	}

	//
	//
	// public int[][] itsFather(int[] combination){
	//
	// }

	public static void main(String[] args) {
		int[] param = new int[] { 2, 2, 2 };
		InteractionRelation ir = new InteractionRelation(param);
		ir.getTwayInetarction(2);
		for (int[] cm : ir.interactions) {
			for (int i : cm)
				System.out.print(i + " ");

			System.out.println();
		}
	}
}
