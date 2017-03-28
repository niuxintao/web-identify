package test;

//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//
//import com.fc.simulateAnneling.Process;

public class GenReplace {

	static String[][] param = {  
			{ "0",  "2" }, { "0"},
			{ "0",  "2" }, { "0", "2" },

			{ "0"}, { "3" },
			{ "0"}, { "0" },

			{  "3" }, { "0"},
			{ "0", "2" }, { "0", "3" },

			{ "0", "1" }, { "0" }, { "0"}, { "0", "1" },

			{ "0",  "3" }, { "0", "3" },
			{ "0",  "3" }, { "0", "3" } };

	public static void main(String[] args) {
		 BenchExecute tj = new BenchExecute(GenReplace.param);
		int[] num = { 2, 1, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 1, 1, 2, 2, 2, 2,
				2 };
		 tj.bench(num);
//		int[][] table = Process.gen(num, 3);
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
