package ct;

import java.util.HashSet;
import java.util.List;

import com.fc.tuple.DealTupleOfIndex;
import com.fc.tuple.Tuple;

public class GetFirstParameterValue {
//	private DataCenter dataCenerMinus1;
	
	
	public GetFirstParameterValue(){
//		this.dataCenerMinus1 = dataCenterMinus1;
	}
	
	public Tuple selectFirstTmiunus1(int[] coveredMark, int t_1pairs, DealTupleOfIndex DOI, DealTupleOfIndex DOIminus1) {
//		long current = System.currentTimeMillis();
		int[] paramStatic = new int[t_1pairs];

		int bestIndex = -1;
		int bestUncovered = -1;

		for (int i = 0; i < coveredMark.length; i++) {
			if (coveredMark[i] == 0) {
				Tuple tuple = DOI.getTupleFromIndex(i);
				List<Tuple> childminus1 = tuple.getChildTuplesByDegree(tuple.getDegree() - 1);
				for(Tuple child : childminus1){
					int index = DOIminus1.getIndexOfTuple(child);
					paramStatic[index] += 1;
				}

			}
		}
		
		for(int i = 0; i < paramStatic.length; i++){
			if(paramStatic[i] > bestUncovered){
				bestUncovered = paramStatic[i];
				bestIndex = i;
			}
		}
		
		Tuple result = DOIminus1.getTupleFromIndex(bestIndex);
		return result;
	}
	
	public Tuple selectFirst(HashSet<Tuple> cannot, int[] coveredMark, int t_1pairs, DealTupleOfIndex DOI,  DealTupleOfIndex DOIminus1) {
//		long current = System.currentTimeMillis();
		
		int[] paramStatic = new int[t_1pairs];

		int bestIndex = -1;
		int bestUncovered = -1;

		for (int i = 0; i < coveredMark.length; i++) {
			if (coveredMark[i] == 0) {
				Tuple tuple = DOI.getTupleFromIndex(i);
				List<Tuple> childminus1 = tuple.getChildTuplesByDegree(tuple.getDegree() - 1);
				for(Tuple child : childminus1){
					int index = DOIminus1.getIndexOfTuple(child);
					paramStatic[index] += 1;
				}

			}
		}
		
		for(int i = 0; i < paramStatic.length; i++){
			Tuple tempij = DOIminus1.getTupleFromIndex(i);
			if (cannot.contains(tempij))
				continue;
			
			if(paramStatic[i] > bestUncovered){
				bestUncovered = paramStatic[i];
				bestIndex = i;
			}
		}
		
		Tuple result = DOIminus1.getTupleFromIndex(bestIndex);
		return result;
		
	}

}
