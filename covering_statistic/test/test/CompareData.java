package test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.rank.Max;
import org.apache.commons.math3.stat.descriptive.rank.Min;

public class CompareData {
	public static int Mbo_up = 0;
	public static int Mtop_down = 1;
	public static int Missta = 2;
	public static int Mhaming = 3;

	public static int Msize = 0;
	public static int Mtime = 1;
	public static int Mrfd = 2;

	public static String[] names = { "bottom-up", "top-down",
			"bottom-up collartive", "top-down hamming" };

	/**
	 * 第一个observation 就是随着 degree 增大， 生成的困难加大，所需时间和分析时间都大大增强，所以能够在给定时间内生 成完得很少
	 */

	/**
	 * 1. 每个维度具体走向，即 比较 2维维度下 的情形， 3维维度下的情形。
	 * 
	 * 具体指标： 1. 在哪个维度下best 是，具体 比第二要好多少 ，差多少，（每个都要具体列出有哪些） 比剩下的要好多少， 差多少。
	 * 平均每个好多少(百分比，数值)，差多少（百分比，数值） 然后是， 最后是 哪两个 （其中要稍微好一点）
	 * 
	 * 
	 * 2.综合比较不同维度的趋势， 即2到6维 的增长趋势
	 * 
	 * 
	 */

	// method, subject, degree
	public double[][][] size;
	public double[][][] time;
	public double[][][] rfd;

	public int[][] param;

	// public String title;

	public CompareData() {
		ReadParam rp = new ReadParam();
		param = rp.param;
		GetData gd = new GetData();
		gd.processAll();
		size = gd.size;
		time = gd.time;
		rfd = gd.rfd;

	}

	// public void compare(double[][][] data, String title) {
	//
	// }

	// public void getBest() {
	//
	// }

	// public void compareTwoSets(double[][][] data, int degree, int method) {
	//
	// }

	// public ComparativeData StatsABenchComparetive(List<ComparativeData> data)
	// {
	// ComparativeData results = new ComparativeData();
	// // 都要好的
	//
	// for (ComparativeData da : data) {
	// // for(int i = 0; )
	// }
	//
	// return results;
	// }

	public double[] transfer(List<Double> input) {
		double[] result = new double[input.size()];
		for (int i = 0; i < result.length; i++)
			result[i] = input.get(i);

		return result;
	}

	public void compare(double[][][] data, String title, boolean smallIsGoodOrfalse) {

		for (int degree : new int[] { 2, 3, 4, 5, 6 }) {
			System.out.println("degree : " + degree);
			System.out.println("-------------------------------------------");
			System.out.println("-------------------------------------------");
			System.out.println("-------------------------------------------");
			// double[][] bench = new double[4][];
			for (int method1 : new int[] { 0, 1, 2, 3 }) {
				double[] d1 = getMethodAndDegree(data, method1, degree);
				for (int method2 : new int[] { 0, 1, 2, 3 }) {
					if (method2 > method1) {
						double[] d2 = getMethodAndDegree(data, method2, degree);
						ComparativeData da = this.compareTwoSets(d1, d2, smallIsGoodOrfalse);
						statusComparativeData(da, names[method1], names[method2]);
					}
				}
			}
		}

	}

	public void statusComparativeData(ComparativeData data, String method1,
			String method2) {
		Mean mean = new Mean();
		Max max = new Max();
		Min min = new Min();

		double[] betterDegree = transfer(data.betterDegree);
		double[] lessDegree = transfer(data.lessDegree);

		double[] paramNumBetter = new double[betterDegree.length];
		for (int i = 0; i < paramNumBetter.length; i++)
			paramNumBetter[i] = param[data.better.get(i)].length;

		double[] paramNumLess = new double[lessDegree.length];
		for (int i = 0; i < paramNumLess.length; i++)
			paramNumLess[i] = param[data.less.get(i)].length;
		
		
		System.out.println("-------------------------------------------");
		System.out.println(method1 + " vs  " + method2);

		
		DecimalFormat df = new DecimalFormat("######0.00");
		
		
		System.out.println("better number--  " + data.better.size());
		System.out.println("equal number--  " + data.equal.size());
		System.out.println("less number--  " + data.less.size());
		
		System.out.println("-------------------------------------------");
		System.out.println("better degree--  max:" + df.format(max.evaluate(betterDegree))
				+ " min:" + df.format(min.evaluate(betterDegree)) + " average:"
				+ df.format(mean.evaluate(betterDegree)));
		System.out.println("-------------------------------------------");
		System.out.println("better subject paramter number--  max:"
				+ df.format(max.evaluate(paramNumBetter)) + " min:"
				+ df.format(min.evaluate(paramNumBetter)) + " average:"
				+ df.format(mean.evaluate(paramNumBetter)));

		System.out.println("-------------------------------------------");
		System.out.println("less degree--  max:" + df.format(max.evaluate(lessDegree))
				+ " min:" + df.format(min.evaluate(lessDegree)) + " average:"
				+ df.format(mean.evaluate(lessDegree)));
		System.out.println("-------------------------------------------");
		System.out.println("less subject paramter number--  max:"
				+ df.format(max.evaluate(paramNumLess)) + " min:"
				+ df.format(min.evaluate(paramNumLess)) + " average:"
				+ df.format(mean.evaluate(paramNumLess)));
	}

	public ComparativeData compareTwoSets(double[] data1, double[] data2,
			boolean smallisBetterOrNot) {
		List<Integer> better = new ArrayList<Integer>();
		List<Integer> equal = new ArrayList<Integer>();
		List<Integer> less = new ArrayList<Integer>();
		List<Double> betterDegree = new ArrayList<Double>();
		List<Double> lessDegree = new ArrayList<Double>();

		for (int i = 0; i < data1.length; i++) {
			if(data1[i]== -1 ||  data2[i] == -1){
				continue;
			}
			if ((smallisBetterOrNot && (data1[i] < data2[i]))
					|| (!smallisBetterOrNot && (data1[i] > data2[i]))) {
				// better
				better.add(i);
				betterDegree.add(Math.abs(data1[i] - data2[i])
						/ Math.max(data1[i], data2[i]));

			} else if (data1[i] == data2[i]) {
				equal.add(i);
			} else {
				less.add(i);
				lessDegree.add(Math.abs(data1[i] - data2[i])
						/ Math.max(data1[i], data2[i]));
			}
		}
		ComparativeData results = new ComparativeData();
		results.better = better;
		results.less = less;
		results.equal = equal;
		results.betterDegree = betterDegree;
		results.lessDegree = lessDegree;

		return results;
	}

	public void showTrend(double[][][] data, String title) {
		System.out.println("Trend : " + title);
		double[][] result = new double[4][5];

		double[][] benchAll = new double[5][4];
		for (int i = 0; i < 5; i++) {
			// double[] bench = this.getBench(data, 2);
			benchAll[i] = this.getBench(data, i + 2);
		}

		for (int method : new int[] { 0, 1, 2, 3 }) {
			// double[][] Tdata = data[method];
			// double Tbench = benchAll[0][method];
			double[] ttr = new double[5];
			ttr[0] = benchAll[0][method];
			for (int i = 1; i < 5; i++) {
				// System.out.println(benchAll[i][method] + " " +
				// benchAll[i][0]);
				ttr[i] = benchAll[i][method];
				// ttr[i] = result[2][i]*(benchAll[i][method]/benchAll[i][2]);
			}
			result[method] = ttr;
		}

		for (double[] Tr : result) {
			System.out.print("[");
			for (double tr : Tr) {
				System.out.print(tr + ", ");
			}
			System.out.println("], ");
		}
	}

	public double[] getBench(double[][][] data, int degree) {
		double[] result = new double[4];
		// for (int degree : new int[] { 2, 3, 4, 5, 6 }) {
		double[][] bench = new double[4][];
		for (int method : new int[] { 0, 1, 2, 3 }) {
			double[] size = getMethodAndDegree(data, method, degree);
			bench[method] = size;
			// this.showSize(size, degree);
		}

		for (int method : new int[] { 0, 1, 2, 3 }) {
			double[] size = getMethodAndDegree(data, method, degree);

			// this.showSize(size, degree, bench);

			double[] Tresult = new double[55];
			double[][] Tbench = getMinmalMaximal(bench);
			for (int i = 0; i < 55; i++) {
				double thisValue = -1;
				if (size[i] != -1) {
					if (Tbench[1][i] - Tbench[0][i] != 0)
						thisValue = (size[i] - Tbench[0][i])
								/ (Tbench[1][i] - Tbench[0][i]);
					else
						thisValue = 0;
				}
				Tresult[i] = thisValue;
			}

			double tempTT = 0;
			double tempN = 0;
			ArrayList<Double> arr = new ArrayList<Double>();
			for (int i = 0; i < 55; i++) {
				if (Tresult[i] != -1) {
					tempTT += Tresult[i];
					tempN++;
					arr.add(Tresult[i]);
				}
			}
			if (tempN != 0)
				tempTT /= tempN;
			else
				tempTT = 0;

			Collections.sort(arr);

			result[method] = tempTT; // arr.get(arr.size()/2);

		}
		// }

		// for (int i = 0; i < data.length; i++) {
		// double[][] temp = data[i];
		//
		// }
		return result;
	}

	/*
	 * trend[0] 2 , 3, 4 ,5, 6 每个 都是 * 比率
	 * 
	 * trend[1] 3, 4, 5, 6 每个都是上面的比下面的
	 * 
	 * bench 是对应方法的2-way的相对于其他方法的比率
	 */
	public double[][] getTrend(double[][] data, double bench) {
		double[] trend = new double[5];
		double[] multiTrend = new double[4];

		trend[0] = bench;
		// double[] bench = this.get2wayBench(data);

		// 55 subjects
		for (int j = 0; j < 5; j++) {
			int k = j + 1;
			if (k >= 5)
				break;
			// for(int k = j+1; k < data[0].length; k++){
			double temp = 0;
			double num = 0;
			for (int i = 0; i < data.length; i++) {
				if (data[i][j] != -1 && data[i][k] != -1) {
					temp += data[i][k] / data[i][j];
					num++;
				}
			}
			if (num != 0) {
				temp /= num;
			}

			multiTrend[j] = temp;
			trend[k] = temp * trend[k - 1];
			// }
		}

		double[][] result = new double[2][];
		result[0] = trend;
		result[1] = multiTrend;

		return result;
	}

	// 是不是 bottom-up 在一起， 然后 top-down 在一起
	public void showCommon(double[][][] data, String title) {
		int[] models = new int[55];
		for (int i = 0; i < 55; i++) {
			models[i] = i;
		}
		System.out.println("show Common: " + title);
		System.out
				.println("Subject & Bottom-up & bottom-up-issta &  Top-down & top-down - haiming");
		DecimalFormat df = new DecimalFormat("######0.0");
		for (int model : models) {
			for (int method : new int[] { 0, 2, 1, 3 }) {
				for (int degree : new int[] { 2, 3, 4, 5, 6 }) {
					if (method == 3 && degree == 6) {
						System.out.println(df
								.format(data[method][model][degree - 2])
								+ "\\\\");
					} else {
						System.out.print(df
								.format(data[method][model][degree - 2]) + "&");
					}
				}
			}
		}
	}

	public double[] getMethodAndDegree(double[][][] data, int method, int degree) {
		double[] result = new double[55];
		for (int i = 0; i < 55; i++)
			result[i] = data[method][i][degree - 2];

		return result;
	}

	// rfd 除以 C(k,t) * v ^{t} 因为这和寻找 schema的多少有关
	public void showRFDAll(double[][][] data) {
		System.out.println("show RFD");
		for (int degree : new int[] { 2, 3, 4, 5, 6 }) {
			double[][] bench = new double[4][];
			for (int method : new int[] { 0, 1, 2, 3 }) {
				double[] rfd = getMethodAndDegree(data, method, degree);
				bench[method] = rfd;
			}
			for (int method : new int[] { 0, 1, 2, 3 }) {
				double[] rfd = getMethodAndDegree(data, method, degree);
				this.showRFD(rfd, degree, bench);
			}
		}
	}

	// time 除以 C(k,t) * v ^{t} 因为这和寻找 schema的多少有关 //有点多
	public void showTimeAll(double[][][] data) {
		System.out.println("show Time");
		for (int degree : new int[] { 2, 3, 4, 5, 6 }) {
			double[][] bench = new double[4][];
			for (int method : new int[] { 0, 1, 2, 3 }) {
				double[] time = getMethodAndDegree(data, method, degree);
				bench[method] = time;
			}

			for (int method : new int[] { 0, 1, 2, 3 }) {
				double[] time = getMethodAndDegree(data, method, degree);
				this.showTime(time, degree, bench);
			}
		}
	}

	// size 除以 logk * v^{t} , 因为这和寻找covering array 的大小有关
	public void showSizeAll(double[][][] data) {
		System.out.println("show Size");
		for (int degree : new int[] { 2, 3, 4, 5, 6 }) {
			double[][] bench = new double[4][];
			for (int method : new int[] { 0, 1, 2, 3 }) {
				double[] size = getMethodAndDegree(data, method, degree);
				bench[method] = size;
				// this.showSize(size, degree);
			}

			for (int method : new int[] { 0, 1, 2, 3 }) {
				double[] size = getMethodAndDegree(data, method, degree);
				this.showSize(size, degree, bench);
			}
		}
	}

	public double[][] getMinmalMaximal(double[][] data) {
		double[][] result = new double[2][data[0].length];
		for (int i = 0; i < data[0].length; i++) {
			double min = -1;
			double max = -1;
			for (int j = 0; j < data.length; j++) {
				if (data[j][i] > max) {
					max = data[j][i];
				}
				if ((data[j][i] != -1 && min == -1)
						|| (data[j][i] != -1 && data[j][i] < min)) {
					min = data[j][i];
				}
			}
			double max2 = -1;
			for (int j = 0; j < data.length; j++) {
				if (data[j][i] > max2 && data[j][i] != max) {
					max2 = data[j][i];
				}
			}

			result[0][i] = min;
			result[1][i] = max;
		}
		return result;
	}

	public void showSize(double[] data, int degree, double[][] sizeTwo) {
		double[] result = new double[55];
		double[][] bench = getMinmalMaximal(sizeTwo);
		for (int i = 0; i < 55; i++) {
			double thisValue = -1;
			if (data[i] != -1) {
				if (bench[1][i] - bench[0][i] != 0)
					thisValue = (data[i] - bench[0][i])
							/ (bench[1][i] - bench[0][i]);
				else
					thisValue = 0;
			}
			result[i] = thisValue;
		}

		DecimalFormat df = new DecimalFormat("######0.00");
		System.out.print("[");
		for (double thisValue : result) {
			System.out.print(df.format(thisValue) + ", ");
		}
		System.out.println("], ");
	}

	public void showTime(double[] data, int degree, double[][] sizeTwo) {
		double[] result = new double[55];
		double[][] bench = getMinmalMaximal(sizeTwo);
		for (int i = 0; i < 55; i++) {
			double thisValue = -1;
			if (data[i] != -1) {
				if (bench[1][i] - bench[0][i] != 0)
					thisValue = (data[i] - bench[0][i])
							/ (bench[1][i] - bench[0][i]);
				else
					thisValue = 0;
			}
			result[i] = thisValue;
		}

		DecimalFormat df = new DecimalFormat("######0.00");
		System.out.print("[");
		for (double thisValue : result) {
			System.out.print(df.format(thisValue) + ", ");
		}
		System.out.println("], ");
	}

	public void showRFD(double[] data, int degree, double[][] sizeTwo) {
		double[] result = new double[55];
		double[][] bench = getMinmalMaximal(sizeTwo);
		for (int i = 0; i < 55; i++) {
			double thisValue = -1;
			if (data[i] != -1) {
				if (bench[1][i] - bench[0][i] != 0)
					thisValue = (data[i] - bench[0][i])
							/ (bench[1][i] - bench[0][i]);
				else
					thisValue = 0;
			}
			result[i] = thisValue;
		}

		DecimalFormat df = new DecimalFormat("######0.00");
		System.out.print("[");
		for (double thisValue : result) {
			System.out.print(df.format(thisValue) + ", ");
		}
		System.out.println("], ");
	}

	public void showRFD(double[] data, int degree) {
		double[] result = new double[55];
		for (int i = 0; i < 55; i++) {
			int[] param = this.getParam(i);
			double benchStandlineRFD = this.theMinimalStandardLineRFD(param,
					degree);
			double thisValue = -1;
			if (data[i] != -1) {
				thisValue = data[i] / benchStandlineRFD;
			}
			result[i] = thisValue;
		}

		DecimalFormat df = new DecimalFormat("######0.00");
		System.out.print("[");
		for (double thisValue : result) {
			System.out.print(df.format(thisValue) + ", ");
		}
		System.out.println("], ");
	}

	public void showTime(double[] data, int degree) {
		double[] result = new double[55];
		for (int i = 0; i < 55; i++) {
			int[] param = this.getParam(i);
			double benchStandlineTime = this.theMinimalStandardLineTime(param,
					degree);
			double thisValue = -1;
			if (data[i] != -1) {
				thisValue = data[i] * 1000 / benchStandlineTime;
			}
			result[i] = thisValue;
		}

		DecimalFormat df = new DecimalFormat("######0.00");
		System.out.print("[");
		for (double thisValue : result) {
			System.out.print(df.format(thisValue) + ", ");
		}
		System.out.println("], ");
	}

	public void showSize(double[] data, int degree) {
		double[] result = new double[55];
		for (int i = 0; i < 55; i++) {
			int[] param = this.getParam(i);
			double benchStandlineSize = this.theMinimalStandardLineSize(param,
					degree);
			double thisValue = -1;
			if (data[i] != -1) {
				thisValue = data[i] / benchStandlineSize;
			}
			result[i] = thisValue;
		}

		DecimalFormat df = new DecimalFormat("######0.00");
		System.out.print("[");
		for (double thisValue : result) {
			System.out.print(df.format(thisValue) + ", ");
		}
		System.out.println("], ");
	}

	public int[] getParam(int model) {
		return param[model];
	}

	public double theMinimalStandardLineRFD(int[] param, int degree) {
		int[] maximaElements = this.getMaximaElements(param, degree);

		double multi = this.getMultiple(maximaElements);

		int indexNum = 1;
		// int i = 1;
		for (int k = 0; k < degree; k++) {
			indexNum *= param.length - degree + k + 1;
			indexNum /= (k + 1);
		}

		multi *= indexNum;

		return multi;
	}

	public double theMinimalStandardLineTime(int[] param, int degree) {
		int[] maximaElements = this.getMaximaElements(param, degree);

		double multi = this.getMultiple(maximaElements);

		int indexNum = 1;
		// int i = 1;
		for (int k = 0; k < degree; k++) {
			indexNum *= param.length - degree + k + 1;
			indexNum /= (k + 1);
		}

		multi *= indexNum;

		return multi;
	}

	public double theMinimalStandardLineSize(int[] param, int degree) {
		int[] maximaElements = this.getMaximaElements(param, degree);

		double multi = this.getMultiple(maximaElements);

		multi *= (Math.log(param.length) + 1);

		return multi;
	}

	public int getMultiple(int[] elements) {
		int result = 1;
		for (int i : elements)
			result *= i;

		return result;
	}

	public int[] getMaximaElements(int[] param, int degree) {
		int[] result = new int[degree];
		int[] resultIndex = new int[degree];

		for (int i = 0; i < degree; i++) {
			int maxi = -1;
			int maxj = -1;
			for (int j = 0; j < param.length; j++) {
				boolean conit = false;
				for (int k = 0; k < i; k++) {
					if (resultIndex[k] == j) {
						conit = true;
						break;
					}
				}
				if (conit)
					continue;
				else {
					if (param[j] > maxi) {
						maxi = param[j];
						maxj = j;
					}
				}
			}
			result[i] = maxi;
			resultIndex[i] = maxj;
		}

		return result;
	}

	public static void main(String[] args) {
		CompareData cd = new CompareData();
		// cd.showCommon(cd.size, "size");
		cd.showSizeAll(cd.size);

		// cd.showCommon(cd.time, "time");
		cd.showTimeAll(cd.time);

		// cd.showCommon(cd.rfd, "rfd");
		cd.showRFDAll(cd.rfd);

		cd.showTrend(cd.size, "size");
		cd.showTrend(cd.time, "time");
		cd.showTrend(cd.rfd, "rfd");
		
		
		cd.compare(cd.rfd, "rfd", false);
	}

}

class ComparativeData {
	public List<Integer> better;
	public List<Integer> equal;
	public List<Integer> less;
	public List<Double> betterDegree;
	public List<Double> lessDegree;
}
