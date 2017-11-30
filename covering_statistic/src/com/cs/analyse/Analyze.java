package com.cs.analyse;

import com.fc.process.DriverForAnalyse;

public class Analyze {

	public String[] modelissta20 = { "Banking1", "Banking2", "CommProtocol",
			"Concurrency", "Healthcare1", "Healthcare2", "Healthcare3",
			"Healthcare4", "Insurance", "NetworkMgmt", "ProcessorComm1",
			"ProcessorComm2", "Services", "Storage1", "Storage2", "Storage3",
			"Storage4", "Storage5", "SystemMgmt", "Telecom" };

	public String[] modelissta35;

	// public String[] models2 ={"SPIN-S","SPIN-V","GCC","Apache","Bugzilla"};

	public String[] model20;
	public String[] model35;

	public String path20Model = "./results/20model";
	public String path35Model = "./results/35model";

	public String bo_up = "result_bo_up";
	public String top_down = "result_top_down";

	public String path20ModelIssta = "./results/issta20model";
	public String path35ModelIssta = "./results/issta35model";

	public static int Mbo_up = 0;
	public static int Mtop_down = 1;
	public static int Missta = 2;

	public Analyze() {
		model20 = new String[20];
		for (int i = 0; i < 20; i++) {
			model20[i] = "benchmark" + "_" + (i + 1);
		}
		model35 = new String[35];
		for (int i = 0; i < 35; i++) {
			model35[i] = "benchmark" + "_" + (i + 1);
		}

		modelissta35 = new String[35];
		for (int i = 0; i < 35; i++) {
			modelissta35[i] = "benchmark" + "_" + (i + 1);
		}
	}

	public String[] getPathsOfSuite(int method, int model, int degree) {
		String[] result = null;
		if (method == Mbo_up || method == Mtop_down) {
			result = new String[1];

			if (model < 20) { // 0 to 19
				result[0] = this.path20Model;
				if (method == Mbo_up) {
					result[0] += "/" + this.bo_up;
				} else {
					result[0] += "/" + this.top_down;
				}

				result[0] += "/" + this.model20[model] + "_" + degree
						+ "way.ca";
			} else {// 20 to 34
				result[0] = this.path35Model;

				if (method == Mbo_up) {
					result[0] += "/" + this.bo_up;
				} else {
					result[0] += "/" + this.top_down;
				}

				result[0] += "/" + this.model35[model - 20] + "_" + degree
						+ "way.ca";
			}

		} else {

			result = new String[3];

			for (int i = 0; i < 3; i++) {
				if (model < 20) { // 0 to 19
					result[i] = this.path20ModelIssta;
					result[i] += "/" + this.modelissta20[model] + "_result_"
							+ degree + "_" + (i+1);
				} else {// 20 to 34
					result[i] = this.path35ModelIssta;
					result[i] += "/" + this.modelissta35[model - 20]
							+ "_result_" + degree + "_" + (i+1);
				}
			}
		}

		return result;

	}
	
	
	public String[] getPathsOfTime(int method, int model, int degree) {
		String[] result = null;
		if (method == Mbo_up || method == Mtop_down) {
			result = new String[1];

			if (model < 20) { // 0 to 19
				result[0] = this.path20Model;
				if (method == Mbo_up) {
					result[0] += "/" + this.bo_up;
				} else {
					result[0] += "/" + this.top_down;
				}

				result[0] += "/" + this.model20[model] + "_" + degree
						+ "way.result";
			} else {// 20 to 34
				result[0] = this.path35Model;

				if (method == Mbo_up) {
					result[0] += "/" + this.bo_up;
				} else {
					result[0] += "/" + this.top_down;
				}

				result[0] += "/" + this.model35[model - 20] + "_" + degree
						+ "way.result";
			}

		} else {

			result = new String[3];

			for (int i = 0; i < 3; i++) {
				if (model < 20) { // 0 to 19
					result[i] = this.path20ModelIssta;
					result[i] += "/" + this.modelissta20[model] + "_result_"
							+ degree + "_" + (i+1);
				} else {// 20 to 34
					result[i] = this.path35ModelIssta;
					result[i] += "/" + this.modelissta35[model - 20]
							+ "_result_" + degree + "_" + (i+1);
				}
			}
		}

		return result;

	}
	
	

	public String getPathOfParmater(int model) {
		String result;
		if (model < 20) {
			result = this.path20ModelIssta;
			result += "/" + this.modelissta20[model] + ".model";
		} else {
			result = this.path35ModelIssta;
			result += "/" + this.modelissta35[model - 20] + ".model";
		}
		return result;
	}

	public void batchForAllMode() {
		
		ReadInput read= new ReadInput();

//		int[] methods = {this.Mbo_up, this.Mtop_down, this.Missta};
		
		int[] models = new int[1];
		for (int i = 0; i < models.length; i++) {
			models[i] = 20 + i;
		}
		
		int[] degrees = new int[] {2,3, 4, 5, 6 };

		for(int method : new int[] {1, 2}){
		System.out.println("method: " + method);
			for (int model : models) {
				//System.out.println("model: " + model);
				String pathParam = this.getPathOfParmater(model);
				read.readParam(pathParam);
				System.out.print("[");
				for(int degree : degrees){
					//System.out.println("degree: " + degree);
					String[] sutiePaths = this.getPathsOfSuite(method, model, degree);
					double[] result = new double[sutiePaths.length];
					double finalResult = 0;
					int i =0;
					for(String suitePath : sutiePaths ){
//						System.out.println("path :" + suitePath);
						result[i] = -1;
						if(read.isFileExists(suitePath)){
							read.readTestCases(suitePath, method== Missta ? 8 : 3 );
							DriverForAnalyse driver = new DriverForAnalyse();
							driver.set(read.getParam(), degree, read.getSuite(), null);
//							System.out.println("coverage : " + driver.analyseCoverage());
//							System.out.println("diversity : " + driver.analyseDiversity());
//							System.out.println("detection rate : " + driver.analyseDetectionRate());
							result[i] = driver.analyseDetectionRate();
							i++;
						}
					}
					for (int j = 0 ; j < result.length; j ++)
						finalResult += result[j];
					if(i == 0)
						finalResult/= result.length;
					else
						finalResult/= i;
					System.out.print( finalResult+ ", ");
				}
				System.out.println("],");
			}
		}
	}
	
	public static void main(String[] args){
		Analyze anlayse = new Analyze();
		anlayse.batchForAllMode();
	}
}
