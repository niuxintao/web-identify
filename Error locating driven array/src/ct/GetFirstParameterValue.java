package ct;

import java.util.HashSet;
import java.util.List;

import interaction.DataCenter;

import com.fc.tuple.DealTupleOfIndex;
import com.fc.tuple.Tuple;

public class GetFirstParameterValue {
	private DataCenter dataCenter;
	private DataCenter dataCenerMinus1;
	
	public GetFirstParameterValue(DataCenter dataCenter){
		this.dataCenter = dataCenter;
	}
	
	public GetFirstParameterValue(DataCenter dataCenter,DataCenter dataCenterMinus1){
		this.dataCenter = dataCenter;
		this.dataCenerMinus1 = dataCenterMinus1;
	}
	
	public Tuple selectFirstTmiunus1(int[] coveredMark, DealTupleOfIndex DOI, DealTupleOfIndex DOIminus1) {
//		long current = System.currentTimeMillis();
		int[] paramStatic = new int[dataCenerMinus1.coveringArrayNum];

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
	
	public Tuple selectFirst(HashSet<Tuple> cannot, int[] coveredMark, DealTupleOfIndex DOI,  DealTupleOfIndex DOIminus1) {
//		long current = System.currentTimeMillis();
		
		int[] paramStatic = new int[dataCenerMinus1.coveringArrayNum];

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
	
	public IJ selectFirst(HashSet<IJ> cannot, int[] coveredMark, DealTupleOfIndex DOI) {
//		long current = System.currentTimeMillis();
		
		int[][] paramStatic = new int[this.dataCenter.param_num][];
		for (int i = 0; i < paramStatic.length; i++) {
			paramStatic[i] = new int[dataCenter.param[i]];
		}

		IJ ij = new IJ();

		int bestI = -1;
		int bestJ = -1;
		int bestUncovered = -1;
		
	


		for (int i = 0; i < coveredMark.length; i++) {
			if (coveredMark[i] == 0) {
				Tuple tuple = DOI.getTupleFromIndex(i);
				int[] index = tuple.getParamIndex();
				int[] value = tuple.getParamValue();
				for (int j = 0; j < index.length; j++) {
					paramStatic[index[j]][value[j]] += 1;
				}
			}
		}
		
		for(int i = 0; i < dataCenter.param_num; i++){
			for(int j = 0; j < dataCenter.param[i]; j ++){
				IJ tempij = new IJ();
				tempij.parameter = i;
				tempij.value = j;
				if (cannot.contains(tempij))
					continue;
				
				if(paramStatic[i][j] > bestUncovered){
					bestUncovered = paramStatic[i][j];
					bestI = i;
					bestJ = j;
				}
			}
		}
		
		ij.parameter = bestI;
		ij.value = bestJ;
		
//		current = System.currentTimeMillis() - current;
//		System.out.println(current/ 1000);
		
		return ij;
	}

}
