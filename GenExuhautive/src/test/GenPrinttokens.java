package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//
import com.fc.simulateAnneling.Process;

public class GenPrinttokens {

	static String[][] param = {  
			{ "0",   "4",  "8", "9", "10" },
			{ "0", "1", "2", "3"},
			{ "0", "4",  "8", "10"},
			{ "0", "2"},
			{ "0", "1", "4"},
			{ "0", "1"},
			{ "0", "2"},
			{ "0",  "4",  "8", "9", "10" },
			{ "0", "1"},
			{ "0", "1" } };

	public static void main(String[] args) {
		 BenchExecute tj = new BenchExecute(GenPrinttokens.param);
		int[] num = { 5, 4, 4, 2, 3, 2, 2, 5, 2, 2};
		 tj.bench(num);
//		int[][] table = Process.gen(num, 4);
//
//		try {
//			FileWriter fw = new FileWriter("result_of_testCase.txt");
//			BufferedWriter bw = new BufferedWriter(fw);
//			try {
//				for (int[] test : table) {
//					for (int j = 0; j < test.length; j++) {
//						bw.write(param[j][test[j]] + " ");
//					}
//					bw.newLine();
//				}
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			bw.flush();
//			bw.close();
//			fw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
