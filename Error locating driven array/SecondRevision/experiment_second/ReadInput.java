package experiment_second;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ReadInput {
	int[][][] result;

	public int[][][] getResult() {
		return result;
	}

	public ReadInput() {
	
	}

	public void read(String path) {
		List<List<List<Integer>>> tables = new ArrayList<List<List<Integer>>> ();
//		result = new int[][][];
		try {
			// Open the file that is the first
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			InputStreamReader in = new InputStreamReader(fstream);
			BufferedReader br = new BufferedReader(in);
			String strLine;
			// Read File Line By Line
			// first line tells the name of the method
			strLine = br.readLine();

			// the remaining lines tell the specific number of rfd, or size or
			// time for each of the degree and each of the subject

			List<List<Integer>> tableTemp = new ArrayList<List<Integer>> ();
			while ((strLine = br.readLine()) != null) {
//				System.out.println(strLine);
				String[] stringTokens = strLine.split(" ");
				
				if(stringTokens == null || stringTokens.length == 0 || stringTokens[0].equals("")){
					if(tableTemp.size() != 0){
						tables.add(tableTemp);
//						System.out.println("table size : "+ tableTemp.size());
					}
					tableTemp = new ArrayList<List<Integer>> ();
					continue;
				}
				
				List<Integer> temp = new ArrayList<Integer>();
				for (String num : stringTokens) {
					if (num.equals(" ") || num.equals("")) {
						continue;
					} else {
						temp.add(Integer.parseInt(num));
					}
				}
				tableTemp.add(temp);
			}

			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
		
		result = getTables(tables);
	}
	
	int[] getTest(List<Integer> test){
		int[] result = new int[test.size()];
		for(int i = 0; i < result.length; i++)
			result[i] = test.get(i);
		
		return result;
	}
	
	int[][] getTable(List<List<Integer>> tests){
		int[][] result = new int[tests.size()][];
		for(int i = 0; i < tests.size(); i++)
			result[i] = getTest(tests.get(i));
		
		return result;
	}
	
	int[][][] getTables (List<List<List<Integer>>> testss){
		int[][][] result = new int[testss.size()][][];
		
		for(int i = 0; i < testss.size(); i++)
			result[i] = getTable(testss.get(i));
		
		return result;

	}

	public static void main(String[] args) {
		ReadInput test = new ReadInput();
		System.out.println(test.getResult());
	}
}
