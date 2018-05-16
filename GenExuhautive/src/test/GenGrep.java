package test;

import interaction.DataCenter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ct.AETG;

//
public class GenGrep {

//	static String[][] param = {
//			{ "1", "2" },
//			{ "1", "2" },
//			{ "1", "2", "3", "4" },
//			{ "1", "2" },
//			{ "1", "2", "3", "4", "5", "6" },
//			{ "1", "2", "3", "4", "5", "6" },
//			{ "1", "2", "3", "4", "5", "6", "7", "8" },
//			{ "1", "2"},  // 可能是个2
//			{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13" }, 
//			{ "1", "2" }, 
//			{ "1", "2", "3", "4", "5", "6" }, 
//			{ "1", "2", "3", "4", "5", "6", "7", "8", "9" }, 
//			{ "1", "2" },
//			{ "1", "2" },
//			{ "1", "2" }, 
//			};

	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 2 1 4 1 4 5 2 2 4  1 3 1 2 2 2
	 * 
	 * 
	 * 
	 * 2 2 4 2 1 3 1 2 8  2 1 9 2 2 2 
	 * 
	 * 
	 */
	 static String[][] param = {
	 { "2"},
	 { "1", "2" },
	 { "4" },
	 { "1", "2"},
	 { "1", "2", "3", "4" },
	 { "1", "2", "3", "4", "5"},
	 { "1", "2" },
	 { "2" },
	 { "4", "8"},
	 { "1", "2"},
	 { "1",  "3" },
	 { "1", "9"},
	 { "2" },
	 { "2"},
	 { "2"},
	 };
	//

	public static void main(String[] args) {
		 BenchExecute tj = new BenchExecute(GenGrep.param);
//		int[] num = { 2, 2, 4, 2, 6, 6, 8, 2, 13, 2, 6, 9, 2, 2, 2 };
		 int[] num = { 1, 2, 1, 2, 4, 5, 2, 1, 2, 2, 2, 2, 1, 1, 1};
		 tj.bench(num);

//		DataCenter dataCenter = new DataCenter(num, 2);
//		AETG aetg = new AETG(dataCenter);
//		aetg.process();
//		List<int[]> corrvery = aetg.coveringArray;
//		try {
//			FileWriter fw = new FileWriter("testCase_CA.txt");
//			BufferedWriter bw = new BufferedWriter(fw);
//
//			for (int[] row : corrvery) {
//				for (int j = 0; j < row.length; j++) {
//						bw.write(param[j][row[j]] + " ");
//				} 
//				bw.newLine();
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
