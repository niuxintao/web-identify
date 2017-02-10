package gc;

import java.util.HashSet;
import java.util.List;

import com.fc.tuple.Tuple;

public class GetTheStrongGuannteedCodeOfSchema {

	// weak - all the biggest child weak
	public int[] sgc(Tuple tuple, int[] param,
			List<List<Integer>> CoveredLinesOfTestCase) {
		GetTheWeakGuannteedCodeOfSchema gwgc = new GetTheWeakGuannteedCodeOfSchema();
		int[] wgc = gwgc.wgc(tuple, param, CoveredLinesOfTestCase);
		List<Tuple> childs = tuple
				.getChildTuplesByDegree(tuple.getDegree() - 1);
		for (Tuple child : childs) {
			int[] wgcc = gwgc.wgc(child, param, CoveredLinesOfTestCase);
			wgc = remove(wgc, wgcc);
			if (wgc.length == 0)
				break;
		}
		return wgc;
	}

	public HashSet<Integer> getHSfint(int[] r) {
		HashSet<Integer> a = new HashSet<Integer>();
		for (int i : r)
			a.add(i);
		return a;
	}

	public int[] remove(int[] wgc, int[] wgcc) {
		HashSet<Integer> wgch = getHSfint(wgc);
		HashSet<Integer> wgcch = getHSfint(wgcc);
		wgch.removeAll(wgcch);
		int[] result = new int[wgch.size()];
		int k = 0;
		for (Integer s : wgch) {
			result[k] = s;
			k++;
		}
		return result;
	}
}
