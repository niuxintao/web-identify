package showData;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import sensity.DataForNumberOfMFS;
import assumption.DataForSafeValueAssumption;
import assumption.DataForUndeterministic;

import com.fc.tuple.Tuple;

import expForRevision.DataForNumberOfOptions;
import expForRevision.TestSensityOfNumberOfOpitions;
import experimentData.ExperimentData;

public class ShowExperimentData {

	public String showTuple(Tuple tuple) {
		String result = "";
		result += "(";
		for (int i = 0; i < tuple.getDegree(); i++) {
			result += (tuple.getParamIndex()[i]+1) + ":" + tuple.getParamValue()[i];
			if (i != tuple.getDegree() - 1)
				result += ",";
		}
		result += ")";

		return result;
	}

	public void showData(String syn, ExperimentData ed) {
		String result =syn + " & " + "$" + ed.getParam()[0] + "^{" + ed.getParam().length
				+ "}$ & ";
		for (Tuple t : ed.getRealMFS()) {
			result += showTuple(t) + " ";
		}
		System.out.println(result + " \\\\ \\hline");
	}
	
	
	public void showData2(String syn, ExperimentData ed, double pro) {
		String result =syn + " & " + "$" + ed.getParam()[0] + "^{" + ed.getParam().length
				+ "}$ &  ";
		for (Tuple t : ed.getRealMFS()) {
			result += showTuple(t) + " ";
		}
		
		result += " & " + pro;
		System.out.println(result+ " \\\\ \\hline");
	}

	public void getAllData() {
		// number of MFS
		System.out.println("num of mfs");
		System.out.println("\\hline");
		System.out.println("Subject & Inputs & MFS  \\\\\\hline");
		int[] param = new int[] { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 }; // 12 / 5
		//
		int[] num = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 20, 30, 40, 50,
				60, 70, 80, 90 };
		
		int i = 1;
		for (int n : num) {
			DataForNumberOfMFS dfm = new DataForNumberOfMFS(param, n);
			String syn = "syn" + i;
			showData(syn, dfm);
			i++;
		}
		
		System.out.println("\\hline");

		System.out.println("num of options");
		
		System.out.println("\\hline");
		System.out.println("Subject & Inputs & MFS  \\\\\\hline");
		// num of options
		num = new int[] { 8, 9, 10, 12, 16, 20, 30, 40, 50, 60, 70, 80, 90, 100 };
		i = 1;
		for (int nu : num) {
			int[] pm = TestSensityOfNumberOfOpitions.getParam(nu);
			// System.out.println("start : the number of options is :" + nu);
			DataForNumberOfOptions dfo = new DataForNumberOfOptions(pm);
			String syn = "syn" + i;
			showData(syn, dfo);
			i++;
		}
		System.out.println("\\hline");
		
		
		System.out.println("num of problities");
		System.out.println("\\hline");
		System.out.println("Subject & Inputs & MFS  & Probability \\\\\\hline");
		// number of problities
		double[] numPro = new double[] { 0.01, 0.05, 0.1, 0.15, 0.2, 0.3, 0.4,
				0.5, 0.6, 0.7, 0.80, 0.9, 0.98 };
		i = 1;
		for (double nu : numPro) {
			DataForUndeterministic dfp = new DataForUndeterministic(nu);
			String syn = "syn" + i;
			showData2(syn, dfp, nu);
			i++;
		}

		// number of safe value
		System.out.println("\\hline");
		
		
		System.out.println("num of safe values");
		
		System.out.println("\\hline");
		System.out.println("Subject & Inputs & MFS  \\\\\\hline");
		num = new int[] { 8, 10, 12, 16, 20, 25, 30, 35, 40, 50};
		
		for (int id : new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }) {
			// System.out.println("start : the number of MFS is :" + id);
			int param_length = num[id];
			DataForSafeValueAssumption dfs = new DataForSafeValueAssumption(
					param_length, id);
			
			String syn = "syn" + (id + 1);
			
			showData(syn, dfs);
		}
		System.out.println("\\hline");

	}

	public static void main(String[] args) {
		try {

			System.setOut(new PrintStream(
					new FileOutputStream("system_out.txt")));

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		ShowExperimentData sd = new ShowExperimentData();
		sd.getAllData();
	}
}
