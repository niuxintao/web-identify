package test;

import java.io.*;


public class ReadInput {
	double[][] result;

	public double[][] getResult() {
		return result;
	}

	public ReadInput() {
	
	}

	public void read(String path) {
		result = new double[55][6];
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

			int j = 0;
			while ((strLine = br.readLine()) != null) {
				double[] temple = new double[6];
				int i = 0;
				String[] stringTokens = strLine.split(" ");
				for (String num : stringTokens) {
					if (num.contains("-")) {
						temple[i] = -1;

					} else {
						temple[i] = Double.parseDouble(num);
					}
					i++;
				}
				result[j] = temple;
				j++;
			}

			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}

	}

	public static void main(String[] args) {
		ReadInput test = new ReadInput();
		System.out.println(test.getResult());
	}
}
