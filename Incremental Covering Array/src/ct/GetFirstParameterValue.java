package ct;

import java.util.HashSet;

import interaction.DataCenter;

import com.fc.tuple.DealTupleOfIndex;
import com.fc.tuple.Tuple;

public class GetFirstParameterValue {
	private DataCenter dataCenter;
	public GetFirstParameterValue(DataCenter dataCenter){
		this.dataCenter = dataCenter;
	}
	public IJ selectFirst(int[] coveredMark, DealTupleOfIndex DOI) {

		int[][] paramStatic = new int[this.dataCenter.n][];
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
		
		for(int i = 0; i < dataCenter.n; i++){
			for(int j = 0; j < dataCenter.param[i]; j ++){
				if(paramStatic[i][j] > bestUncovered){
					bestUncovered = paramStatic[i][j];
					bestI = i;
					bestJ = j;
				}
			}
		}
		

		ij.parameter = bestI;
		ij.value = bestJ;
		return ij;
	}
	
	public IJ selectFirst(HashSet<IJ> cannot, int[] coveredMark, DealTupleOfIndex DOI) {
		int[][] paramStatic = new int[this.dataCenter.n][];
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
		
		for(int i = 0; i < dataCenter.n; i++){
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
		return ij;
	}

}
