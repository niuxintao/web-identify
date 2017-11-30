package driver;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import output.OutPut;

import com.cs.analyse.ReadInput;
import com.fc.process.DriverForAnalyse;
import com.fc.testObject.TestSuite;
import com.fc.testObject.TestSuiteImplement;

public class StatisticTime {
	public String[] modelissta20 = { "Banking1", "Banking2", "CommProtocol",
			"Concurrency", "Healthcare1", "Healthcare2", "Healthcare3",
			"Healthcare4", "Insurance", "NetworkMgmt", "ProcessorComm1",
			"ProcessorComm2", "Services", "Storage1", "Storage2", "Storage3",
			"Storage4", "Storage5", "SystemMgmt", "Telecom" };

	public String[] modelissta35;

	public String path20ModelIssta = "./results/issta20model";
	public String path35ModelIssta = "./results/issta35model";

	// public String[] models2 ={"SPIN-S","SPIN-V","GCC","Apache","Bugzilla"};

	public String[] model20;
	public String[] model35;

	public String path20Model = "./results/model20";
	public String path35Model = "./results/model35";

	public String bo_up = "result_bo_up";
	public String top_down = "result_top_down";
	public String haiming = "result_haiming_top_down";
	public String issta = "result_issta_bo_up";

	public static int Mbo_up = 0;
	public static int Mtop_down = 1;
	public static int Missta = 2;
	public static int Mhaming = 3;

	public static String[] methodsName = { "bottom_up", "top_down",
			"bottom_up_issta", "top_down_haiming" };

	public List<double[]> sizes;
	public List<double[]> times;
	public List<double[]> rfd;

	public StatisticTime() {
		sizes = new ArrayList<double[]>();
		times = new ArrayList<double[]>();
		rfd = new ArrayList<double[]>();

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
		if (method == Mbo_up || method == Mtop_down || method == Mhaming) {
			result = new String[1];

			if (model < 20) { // 0 to 19
				result[0] = this.path20Model;
				if (method == Mbo_up) {
					result[0] += "/" + this.bo_up;
				} else if (method == Mtop_down) {
					result[0] += "/" + this.top_down;
				} else if (method == Mhaming) {
					result[0] += "/" + this.haiming;
				}

				result[0] += "/" + this.model20[model] + "_" + degree + ".ca";
			} else {// 20 to 34
				result[0] = this.path35Model;

				if (method == Mbo_up) {
					result[0] += "/" + this.bo_up;
				} else if (method == Mtop_down) {
					result[0] += "/" + this.top_down;
				} else if (method == Mhaming) {
					result[0] += "/" + this.haiming;
				}

				result[0] += "/" + this.model35[model - 20] + "_" + degree
						+ ".ca";
			}

		} else if (method == Missta) {

			result = new String[3];

			for (int i = 0; i < 3; i++) {
				if (model < 20) { // 0 to 19
					result[i] = this.path20Model;
					result[i] += "/" + this.issta;
					result[i] += "/" + this.model20[model] + "_" + degree + "_"
							+ i + ".ca";
				} else {// 20 to 34
					result[i] = this.path35Model;
					result[i] += "/" + this.issta;
					result[i] += "/" + this.model35[model - 20] + "_" + degree
							+ "_" + i + ".ca";
				}
			}
		}

		return result;

	}

	public String[] getPathsOfTime(int method, int model, int degree) {
		return this.getPathsOfSuite(method, model, degree);
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

	// 表格，然后平均增幅

	public void batchForAllSize() {

		ReadInput read = new ReadInput();

		int[] models = new int[55];
		for (int i = 0; i < models.length; i++) {
			models[i] = i;
		}

		OutPut output = new OutPut("./size.txt");

		int[] degrees = new int[] { 2, 3, 4, 5, 6 };

		DecimalFormat df = new DecimalFormat("#.##");

		for (int method : new int[] { 0, 1, 2, 3 }) {
			output.println("method: " + methodsName[method]);
//			System.out.println("method: " + methodsName[method]);
			for (int model : models) {
				// System.out.println("model: " + model);
				String pathParam = this.getPathOfParmater(model);
				read.readParam(pathParam);
				// output.print("");
				for (int degree : degrees) {
					// System.out.println("degree: " + degree);
					String[] sutiePaths = this.getPathsOfSuite(method, model,
							degree);
					List<Integer> result = new ArrayList<Integer>();
					double finalResult = 0;
					for (String suitePath : sutiePaths) {
						// System.out.println("path :" + suitePath);
						if (read.isFileExists(suitePath)) {
							read.readSize(suitePath);
							result.add(read.getSize());
						}
					}
					for (int j = 0; j < result.size(); j++) {
						finalResult += result.get(j);
					}
					if (result.size() != 0)
						finalResult /= result.size();
					else
						finalResult = -1;
					if (method == 2)
						output.print(df.format(finalResult) + " ");
					else
						output.print(finalResult + " ");
				}
				output.println();
				// output.println("],");
			}
		}
		output.close();
	}

	public void batchForAllTime() {

		ReadInput read = new ReadInput();

		int[] models = new int[55];
		for (int i = 0; i < models.length; i++) {
			models[i] = i;
		}

		int[] degrees = new int[] { 2, 3, 4, 5, 6 };
		
		OutPut output = new OutPut("./time.txt");

		DecimalFormat df = new DecimalFormat("#.##");

		for (int method : new int[] { 0}) {
			output.println("method: " +   methodsName[method]);
			for (int model : models) {
				// System.out.println("model: " + model);
				String pathParam = this.getPathOfParmater(model);
				read.readParam(pathParam);
//				System.out.print("[");
				for (int degree : degrees) {
					// System.out.println("degree: " + degree);
					String[] sutiePaths = this.getPathsOfSuite(method, model,
							degree);
					List<Long> result = new ArrayList<Long>();
					double finalResult = 0;
					for (String suitePath : sutiePaths) {
						// System.out.println("path :" + suitePath);
						if (read.isFileExists(suitePath)) {
							read.readTime(suitePath);
							result.add(read.getTime());
							// System.out.println(read.getTime());
						}
					}
					for (int j = 0; j < result.size(); j++) {
						finalResult += result.get(j);
					}
					if (result.size() != 0)
						finalResult /= result.size();
					else
						finalResult = -1;
					if (method == 2)
						output.print(df.format(finalResult) + " ");
					else
						output.print(finalResult + " ");
				}
				output.println();
//				System.out.println("],");
			}
		}
		output.close();
	}

	public void batchForAllRfd() {

		ReadInput read = new ReadInput();

		int[] models = new int[35];
		for (int i = 0; i < models.length; i++) {
			models[i] = 20+i;
//			if(i >= 21)
//			models[i] = i+1;
//			else
//			models[i] = i;
		}

		int[] degrees = new int[] { 2, 3, 4, 5, 6};
		
		OutPut output = new OutPut("./rfd.txt");

		DecimalFormat df = new DecimalFormat("#.##");

		for (int method : new int[] { 0 }) {
			output.println("method: " + methodsName[method]);
			
			System.out.println("method: " + methodsName[method]);
		
			
			for (int model : models) {
//				if(model == 22){
//					output.println();
//				}
				System.out.println("model: " + model);
				String pathParam = this.getPathOfParmater(model);
				read.readParam(pathParam);
//				System.out.print("[");
				for (int degree : degrees) {
					 System.out.println("degree: " + degree);
					String[] sutiePaths = this.getPathsOfSuite(method, model,
							degree);
					List<Double> result = new ArrayList<Double>();
					double finalResult = 0;
					for (String suitePath : sutiePaths) {
						// System.out.println("path :" + suitePath);
						if (read.isFileExists(suitePath)) {
							read.readTestCases(suitePath, 3);
							DriverForAnalyse driver = new DriverForAnalyse();
							System.out.println("test cases number:" + read.getSuite().getTestCaseNum());
							TestSuite single = new TestSuiteImplement();
							single.addTest(read.getSuite().getAt(0));
							driver.set(read.getParam(), degree,
									single, null);
							result.add(driver.analyseDetectionRate());
						}
					}
					for (int j = 0; j < result.size(); j++) {
						finalResult += result.get(j);
					}
					if (result.size() != 0)
						finalResult /= result.size();
					else
						finalResult = -1;
					
					if (method == 2)
						output.print(df.format(finalResult) + " ");
					else
						output.print(finalResult + " ");
					
					//System.out.print(finalResult + ", ");
				}
				output.println();
//				System.out.println("],");
			}
		}
		output.close();
	}
	
	public static void main(String[] args) {
		StatisticTime anlayse = new StatisticTime();
//	    anlayse.batchForAllSize();
//		anlayse.batchForAllTime();
		anlayse.batchForAllRfd();

	}
}
