package interaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class InteractionCov {

	/**
	 * 
	 * @param coverage
	 *            int[][] : one int[] is a covrage {{1 ,2 } { 1, 3} } = { 1 }
	 * @return
	 */
	public int[] interact(List<int[]> coverage) {
		HashSet<Integer> s0 = new HashSet<Integer>();
		this.addAll(s0, coverage.get(0));
		for (int i = 1; i < coverage.size(); i++) {
			if (s0 == null || s0.isEmpty()){
				int[] result = new int[0];
				return result;
			}
			HashSet<Integer> s1 = new HashSet<Integer>();
			this.addAll(s1, coverage.get(i));
			s0.retainAll(s1);
		}

		int j = 0;
		int[] result = new int[s0.size()];
		for (Integer i : s0) {
			result[j] = i;
			j++;
		}

		return result;
	}

	public int[] removeSameElements(int[] coverage, int[] theNeededRemoved) {
		HashSet<Integer> s0 = new HashSet<Integer>();
		this.addAll(s0, coverage);
		HashSet<Integer> s1 = new HashSet<Integer>();
		this.addAll(s1, theNeededRemoved);
		// interaction
		s1.retainAll(s0);
		// remove the same
		s0.removeAll(s1);

		int j = 0;
		int[] result = new int[s0.size()];
		for (Integer i : s0) {
			result[j] = i;
			j++;
		}

		return result;
	}

	public void addAll(HashSet<Integer> s, int[] c) {
		for (int i : c)
			s.add(i);

	}

	public static void main(String[] args){
		InteractionCov ic = new InteractionCov();
		int[] co = {1, 2, 3 ,4 , 5};
		int[] remov = {3 , 4 ,5 ,6 ,7};
		int[] result = ic.removeSameElements(co, remov);
		List<int[]> cop = new ArrayList<int[]> ();
		cop.add(co);
		cop.add(remov);
		for(int i : result ){
			System.out.println(i);
		}
		System.out.println("interact");
		int[] result2 = ic.interact(cop);
		for(int i : result2 ){
			System.out.println(i);
		}
	}
}
