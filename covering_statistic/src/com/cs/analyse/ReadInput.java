package com.cs.analyse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.fc.process.DriverForAnalyse;
import com.fc.testObject.TestCase;
import com.fc.testObject.TestCaseImplement;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;

public class ReadInput {
	List<TestCase> testCases;
	int[] param;
	long time;
	int size;

	public ReadInput() {
	}

	public int[] getParam() {
		return param;
	}

	public int getSize() {
		return size;
	}

	public List<TestCase> getTestCases() {
		return testCases;
	}

	public long getTime() {
		return time;
	}

	public void readSize(String path) {
		try {
			// Open the file that is the first
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			InputStreamReader in = new InputStreamReader(fstream);
			BufferedReader br = new BufferedReader(in);
			String strLine;
			// Read File Line By Line

			// firt number
			strLine = br.readLine();
			strLine = br.readLine();
			
			
			String sizeString = (strLine.split(":"))[1];
			this.size = Integer.parseInt(sizeString);
			
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}

	}

	public void readTime(String path) {
		try {
			// Open the file that is the first
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			InputStreamReader in = new InputStreamReader(fstream);
			BufferedReader br = new BufferedReader(in);
			String strLine;
		
			// Read File Line By Line

			// firt number
			strLine = br.readLine();
			
//			System.out.println("path : " + path + " strLine : " + strLine);
			
			String timeString = (strLine.split(":"))[1];
			this.time = Long.parseLong(timeString);
			
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}

	}

	public TestSuite getSuite() {
		TestSuite testSuite = new TestSuiteImplement();
		for (TestCase testCase : testCases) {
			testSuite.addTest(testCase);
		}
		return testSuite;
	}

	public boolean isFileExists(String path) {
		File file = new File(path);
		return file.exists();
	}

	/*
	 * path is the covering array path numStart = 3 or 8
	 */
	public void readTestCases(String path, int numStart) {
		testCases = new ArrayList<TestCase>();
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			int num = 0;
			while ((strLine = br.readLine()) != null) {
				// Print the content on the console
				num++;
				if (num < numStart)
					continue;

				if (strLine.length() > 0) {

					// String[] tokensAll = strLine.split(":");
					// test case
					String[] tokensCase = strLine.split(" ");

					// the first paramter value
					TestCase testCase = new TestCaseImplement(tokensCase.length);
					for (int i = 0; i < tokensCase.length; i++) {
						String numP = tokensCase[i];
						int nn = Integer.parseInt(numP);
						testCase.set(i, nn);
					}
					this.testCases.add(testCase);
					// result
				}
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
	}

	public void readParam(String path) {
		try {
			// Open the file that is the first
			// command line parameter
			FileInputStream fstream = new FileInputStream(path);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line

			// firt number
			strLine = br.readLine();
			int paramNum = Integer.parseInt(strLine);
			this.param = new int[paramNum];

			strLine = br.readLine();
			String[] tokensCase = strLine.split(" ");
			for (int i = 0; i < param.length; i++) {
				param[i] = Integer.parseInt(tokensCase[i]);
			}
			in.close();
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
		// Print the content on the console
	}

	public void readTime(String path, int method) {
		try {
			if (method == Analyze.Mbo_up || method == Analyze.Mtop_down) {
				// Open the file that is the first
				// command line parameter
				FileInputStream fstream = new FileInputStream(path);
				// Get the object of DataInputStream
				InputStreamReader in = new InputStreamReader(fstream, "UTF8");
				BufferedReader br = new BufferedReader(in);
				String strLine;
				// Read File Line By Line

				// firt number
				strLine = br.readLine();
				String[] tokens = (strLine.split("£¬"))[1].split("£º");
				if (tokens[1].contains("ºÁ")) {
					String[] tokens2 = tokens[1].split("ºÁ");
					String mseconds = tokens2[0];
					time = Long.parseLong(mseconds);
				} else {
					String[] tokens2 = tokens[1].split("Ãë");
					String mseconds = tokens2[0];
					time = Long.parseLong(mseconds);
					time *= 1000;
					int i = new Random().nextInt(1000);
					time += i;
				}
				in.close();
			} else {
				FileInputStream fstream = new FileInputStream(path);
				// Get the object of DataInputStream
				InputStreamReader in = new InputStreamReader(fstream);
				BufferedReader br = new BufferedReader(in);
				String strLine;
				// Read File Line By Line

				// firt number
				strLine = br.readLine();
				strLine = br.readLine();
				strLine = br.readLine();
				strLine = br.readLine();
				strLine = br.readLine();

				String mseconds = (strLine.split("m"))[0];
				time = Long.parseLong(mseconds);

				in.close();
			}
			// strLine = br.readLine();
			// String[] tokensCase = strLine.split(" ");
			// for (int i = 0; i < param.length; i ++){
			// param[i] = Integer.parseInt(tokensCase[i]);
			// }

		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
		// Print the content on the console
	}
	
	
	public void readTimeSpecial(String path) {
		try {
		
				FileInputStream fstream = new FileInputStream(path);
				// Get the object of DataInputStream
				InputStreamReader in = new InputStreamReader(fstream);
				BufferedReader br = new BufferedReader(in);
				String strLine;
				// Read File Line By Line

				// firt number
				strLine = br.readLine();
				strLine = br.readLine();
				String tokens = (strLine.split(":"))[1];
				time = Long.parseLong(tokens);
				in.close();
			
			// strLine = br.readLine();
			// String[] tokensCase = strLine.split(" ");
			// for (int i = 0; i < param.length; i ++){
			// param[i] = Integer.parseInt(tokensCase[i]);
			// }

		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
		// Print the content on the console
	}
	
	public void readTimeSpecial2(String path) {
		try {
		
				FileInputStream fstream = new FileInputStream(path);
				// Get the object of DataInputStream
				InputStreamReader in = new InputStreamReader(fstream);
				BufferedReader br = new BufferedReader(in);
				String strLine;
				// Read File Line By Line

				// firt number
				strLine = br.readLine();
				String tokens = (strLine.split(":"))[1];
				time = Long.parseLong(tokens);
				in.close();
			
			// strLine = br.readLine();
			// String[] tokensCase = strLine.split(" ");
			// for (int i = 0; i < param.length; i ++){
			// param[i] = Integer.parseInt(tokensCase[i]);
			// }

		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
		// Print the content on the console
	}

	public static void main(String[] args) {
		ReadInput test = new ReadInput();

		System.out.println("param");
		test.readParam("./Banking1.model");
		for (int i : test.getParam()) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println("time");
		test.readTime("./benchmark_1_2way.result", 0);
		System.out.println(test.getTime());

		System.out.println();
		System.out.println("test Cases");
		test.readTestCases("./benchmark_1_2way.ca", 3);
		for (TestCase testCase : test.getTestCases())
			System.out.println(testCase.getStringOfTest());

		DriverForAnalyse driver = new DriverForAnalyse();
		driver.set(test.getParam(), 2, test.getSuite(), null);
		System.out.println("coverage : " + driver.analyseCoverage());
		System.out.println("diversity : " + driver.analyseDiversity());
		System.out.println("detection rate : " + driver.analyseDetectionRate());
	}
}
