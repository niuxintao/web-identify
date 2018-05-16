package test;

import com.cs.analyse.Analyze2;
import com.cs.analyse.ReadInput;

public class ReadParam {
	
	public int[][] param;
	public ReadParam(){
		param = new int[55][];
		for(int i = 0; i < 55; i++){
			param[i] = this.getParam(i);
		}
	}
	
	public String getPathOfParmater(int model) {
		String result;
		Analyze2 analyze = new Analyze2();
		if (model < 20) {
			result = analyze.path20ModelIssta;
			result += "/" + analyze.modelissta20[model] + ".model";
		} else {
			result = analyze.path35ModelIssta;
			result += "/" + analyze.modelissta35[model - 20] + ".model";
		}
		return result;
	}
	
	public int[] getParam(int model){
			ReadInput read = new ReadInput();
			String pathParam = this.getPathOfParmater(model);
			read.readParam(pathParam);
			return read.getParam();
	}

}
