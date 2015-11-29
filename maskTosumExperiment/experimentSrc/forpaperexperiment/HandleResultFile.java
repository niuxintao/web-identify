package forpaperexperiment;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.descriptive.moment.Mean;

class Data {
	public HashMap<String, List<Double>> entrys;

	public Data() {
		entrys = new HashMap<String, List<Double>>();
	}

}

public class HandleResultFile {

	public int subject;

	public static final int metric = 0;
	public static final int accuate = 1;
	public static final int parent = 2;
	public static final int child = 3;
	public static final int ignore = 4;
	public static final int irrlevant = 5;
	public static final int testNum = 6;
	public static final int millions = 7;
	public static final int replaceTime = 8; // 每次 调用 替换， 需要的个数
	public static final int replace = 9; // 需要替换 的 数目

	public static final String[] stringofmetric = { "metric", "accuate", "parent", "child", "ignore", "irrelevant",
			"testNum", "time millions", "needing replacement number", "trial test cases for each replacement" };

	public static final int listNum = 10;

	public static final int column1 = 5;

	public static final int one = 0;
	public static final int dis = 1;
	public static final int ilp = 2;
	public static final int random = 3;

	public static final int pValue = 4;

	public static final int column2 = 7;

	public static final int fd = 0;
	public static final int ilpc = 1;
	public static final int afd = 2;

	public static final int avnova = 3;
	public static final int p1 = 3;
	public static final int p2 = 4;
	public static final int p3 = 5;

	public Data[] datas = new Data[listNum];
	// public ArrayList<String>

	public Data[] datas2degree = new Data[listNum];

	public Data[] datas3degree = new Data[listNum];

	public Data[] datas4degree = new Data[listNum];

	public String path;
	private BufferedReader br;

	public HandleResultFile(String path, int subject) throws Exception {
		this.path = path;
		this.subject = subject;
		for (int i = 0; i < listNum; i++) {
			datas[i] = new Data();
			datas2degree[i] = new Data();
			datas3degree[i] = new Data();
			datas4degree[i] = new Data();
		}

		read();
	}

	public void show(Data[] datas, int i) {
		int subject = datas[0].entrys.keySet().size();
		// for (int i = 0; i < listNum; i++) {
		// System.out.println(stringofmetric[i]);
		Data data = datas[i];
		for (int j = 0; j < subject; j++) {
			String key = j + " ";
			if (data.entrys.containsKey(key)) {
				System.out.print(key + " : ");
				List<Double> da = data.entrys.get(key);
				for (Double d : da)
					System.out.print(d + "  ");
				System.out.println();
			}
		}

		// }
	}

	public void show(Data[] datas) {
		int subject = datas[0].entrys.keySet().size();
		for (int i = 0; i < listNum; i++) {
			System.out.println(stringofmetric[i]);
			Data data = datas[i];
			for (int j = 0; j < subject; j++) {
				String key = j + " ";
				if (data.entrys.containsKey(key)) {
					System.out.print(key + " : ");
					List<Double> da = data.entrys.get(key);
					for (Double d : da)
						System.out.print(d + "  ");
					System.out.println();
				}
			}

		}
	}

	public void read() throws Exception {
		// command line parameter
		FileInputStream fstream = new FileInputStream(path);
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
		br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		// Read File Line By Line

		int start = -1;
		int start1 = -1;
		int start2 = -1;
		int start3 = -1;
		int start4 = -1;

		int next = 0;

		int stage = 0;
		while ((strLine = br.readLine()) != null) {
			if (strLine.length() > 0) {
				next++;
				if (strLine.contains("**************************")) {
					stage = 0;
					start++;
					next = 0;
					continue;
				} else if (strLine.contains("##############################")) {
					stage = 1;
					start1++;
					next = 0;
					continue;
				} else if (strLine.contains("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&1")) {
					start2++;
					stage = 2;
					next = 0;
					continue;
				} else if (strLine.contains("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&2")) {
					stage = 3;
					start3++;
					next = 0;
					continue;
				} else if (strLine.contains("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&3")) {
					stage = 4;
					start4++;
					next = 0;
					continue;
				}

				if (stage == 0) {
					if (next == 4) {
						// regarded as one test num
						Data data = this.datas[testNum];
						data.entrys.put(start + " ", new ArrayList<Double>());
						data.entrys.get(start + " ").add(Double.parseDouble(strLine));
					}
					if (next == 6) {
						// distin test num
						Data data = this.datas[testNum];
						data.entrys.get(start + " ").add(Double.parseDouble(strLine));
					}

					if (next == 9 || next == 16) {
						// regarded as one metric
						Data data = this.datas[metric];
						if (next == 9)
							data.entrys.put(start + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						data.entrys.get(start + " ").add(Double.parseDouble(tokensAll[1]));
					}
					if (next == 10 || next == 17) {
						Data data = this.datas[accuate];
						if (next == 10)
							data.entrys.put(start + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						data.entrys.get(start + " ").add(Double.parseDouble(tokensAll[1]));
					}
					if (next == 11 || next == 18) {
						Data data = this.datas[child];
						if (next == 11)
							data.entrys.put(start + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						data.entrys.get(start + " ").add(Double.parseDouble(tokensAll[1]));
					}
					if (next == 12 || next == 19) {
						Data data = this.datas[parent];
						if (next == 12)
							data.entrys.put(start + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						data.entrys.get(start + " ").add(Double.parseDouble(tokensAll[1]));
					}
					if (next == 13 || next == 20) {
						Data data = this.datas[irrlevant];
						if (next == 13)
							data.entrys.put(start + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						data.entrys.get(start + " ").add(Double.parseDouble(tokensAll[1]));
					}
					if (next == 14 || next == 21) {
						Data data = this.datas[ignore];
						if (next == 14)
							data.entrys.put(start + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						data.entrys.get(start + " ").add(Double.parseDouble(tokensAll[1]));
					}
				} else if (stage == 1) {
					// the ilp and random
					if (next == 2 || next == 4 || next == 5) {
						Data data = this.datas[testNum];
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						// for(String str : tokensAll)
						// System.out.println(str);
						int k = (next == 4 ? 2 : 3);
						// System.out.print(k);
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					// next = 5 p value

					if (next == 6 || next == 8 || next == 9) {
						Data data = this.datas[replaceTime];
						// ilp testNum
						if (next == 6)
							data.entrys.put(start1 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = (next == 8 ? 2 : 3);
						if (next == 6)
							k = 8;
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 10 || next == 12 || next == 13) {
						Data data = this.datas[replace];
						// ilp testNum
						if (next == 10)
							data.entrys.put(start1 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = (next == 12 ? 2 : 3);
						if (next == 10)
							k = 5;
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 14 || next == 16 || next == 17) {
						Data data = this.datas[metric];
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						int k = (next == 16 ? 2 : 3);
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 18 || next == 20 || next == 21) {
						Data data = this.datas[parent];
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						int k = (next == 20 ? 2 : 3);
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 22 || next == 24 || next == 25) {
						Data data = this.datas[child];
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						int k = (next == 24 ? 2 : 3);
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 26 || next == 28 || next == 29) {
						Data data = this.datas[ignore];
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						int k = (next == 28 ? 2 : 3);
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 30 || next == 32 || next == 33) {
						Data data = this.datas[irrlevant];
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						int k = (next == 32 ? 2 : 3);
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}
					if (next == 34 || next == 36 || next == 37) {
						Data data = this.datas[accuate];
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						int k = (next == 36 ? 2 : 3);
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}
					if (next == 38 || next == 40 || next == 41) {
						Data data = this.datas[millions];
						if (next == 38)
							data.entrys.put(start1 + " ", new ArrayList<Double>());
						// ilp testNum
						String[] tokensAll = strLine.split(" ");
						int k = (next == 40 ? 2 : 3);
						if (next == 38)
							k = 4;
						data.entrys.get(start1 + " ").add(Double.parseDouble(tokensAll[k]));
					}
				} else if (stage == 2) {
					if (next == 3 || next == 5 || next == 7 || next == 8 || next == 9 || next == 10 || next == 11) {
						Data data = this.datas2degree[metric];
						if (next == 3)
							data.entrys.put(start2 + " ", new ArrayList<Double>());

						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 8)
							k = 4;
						else if (next == 9 || next == 10 || next == 11)
							k = 8;
						data.entrys.get(start2 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					// 8 avonoa 9 fb - ilp 10 afb -ilp 11 fb afb

					if (next == 13 || next == 15 || next == 17 || next == 18 || next == 19 || next == 20
							|| next == 21) {
						Data data = this.datas2degree[accuate];
						if (next == 13)
							data.entrys.put(start2 + " ", new ArrayList<Double>());

						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 18)
							k = 4;
						else if (next == 19 || next == 20 || next == 21)
							k = 8;
						data.entrys.get(start2 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 23 || next == 25 || next == 27 || next == 28 || next == 29 || next == 30
							|| next == 31) {
						Data data = this.datas2degree[parent];
						if (next == 23)
							data.entrys.put(start2 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 28)
							k = 4;
						else if (next == 29 || next == 30 || next == 31)
							k = 8;
						data.entrys.get(start2 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 33 || next == 35 || next == 37 || next == 38 || next == 39 || next == 40
							|| next == 41) {
						Data data = this.datas2degree[child];
						if (next == 33)
							data.entrys.put(start2 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 38)
							k = 4;
						else if (next == 39 || next == 40 || next == 41)
							k = 8;
						data.entrys.get(start2 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 43 || next == 45 || next == 47 || next == 48 || next == 49 || next == 50
							|| next == 51) {
						Data data = this.datas2degree[ignore];
						if (next == 43)
							data.entrys.put(start2 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 48)
							k = 4;
						else if (next == 49 || next == 50 || next == 51)
							k = 8;
						data.entrys.get(start2 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 53 || next == 55 || next == 57 || next == 58 || next == 59 || next == 60
							|| next == 61) {
						Data data = this.datas2degree[irrlevant];
						if (next == 53)
							data.entrys.put(start2 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 58)
							k = 4;
						else if (next == 59 || next == 60 || next == 61)
							k = 8;
						data.entrys.get(start2 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 63 || next == 65 || next == 67 || next == 68 || next == 69 || next == 70
							|| next == 71) {
						Data data = this.datas2degree[testNum];
						if (next == 63)
							data.entrys.put(start2 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 68)
							k = 4;
						else if (next == 69 || next == 70 || next == 71)
							k = 8;
						data.entrys.get(start2 + " ").add(Double.parseDouble(tokensAll[k]));
					}

				} else if (stage == 3) {
					if (next == 3 || next == 5 || next == 7 || next == 8 || next == 9 || next == 10 || next == 11) {
						Data data = this.datas3degree[metric];
						if (next == 3)
							data.entrys.put(start3 + " ", new ArrayList<Double>());

						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 8)
							k = 4;
						else if (next == 9 || next == 10 || next == 11)
							k = 8;
						// System.out.println(k);
						// for(String str : tokensAll)
						// System.out.println(str);
						data.entrys.get(start3 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					// 8 avonoa 9 fb - ilp 10 afb -ilp 11 fb afb

					if (next == 13 || next == 15 || next == 17 || next == 18 || next == 19 || next == 20
							|| next == 21) {
						Data data = this.datas3degree[accuate];
						if (next == 13)
							data.entrys.put(start3 + " ", new ArrayList<Double>());

						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 18)
							k = 4;
						else if (next == 19 || next == 20 || next == 21)
							k = 8;
						data.entrys.get(start3 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 23 || next == 25 || next == 27 || next == 28 || next == 29 || next == 30
							|| next == 31) {
						Data data = this.datas3degree[parent];
						if (next == 23)
							data.entrys.put(start3 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 28)
							k = 4;
						else if (next == 29 || next == 30 || next == 31)
							k = 8;
						data.entrys.get(start3 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 33 || next == 35 || next == 37 || next == 38 || next == 39 || next == 40
							|| next == 41) {
						Data data = this.datas3degree[child];
						if (next == 33)
							data.entrys.put(start3 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 38)
							k = 4;
						else if (next == 39 || next == 40 || next == 41)
							k = 8;
						data.entrys.get(start3 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 43 || next == 45 || next == 47 || next == 48 || next == 49 || next == 50
							|| next == 51) {
						Data data = this.datas3degree[ignore];
						if (next == 43)
							data.entrys.put(start3 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 48)
							k = 4;
						else if (next == 49 || next == 50 || next == 51)
							k = 8;
						data.entrys.get(start3 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 53 || next == 55 || next == 57 || next == 58 || next == 59 || next == 60
							|| next == 61) {
						Data data = this.datas3degree[irrlevant];
						if (next == 53)
							data.entrys.put(start3 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 58)
							k = 4;
						else if (next == 59 || next == 60 || next == 61)
							k = 8;
						data.entrys.get(start3 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 63 || next == 65 || next == 67 || next == 68 || next == 69 || next == 70
							|| next == 71) {
						Data data = this.datas3degree[testNum];
						if (next == 63)
							data.entrys.put(start3 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 68)
							k = 4;
						else if (next == 69 || next == 70 || next == 71)
							k = 8;
						data.entrys.get(start3 + " ").add(Double.parseDouble(tokensAll[k]));
					}
				} else if (stage == 4) {
					if (next == 3 || next == 5 || next == 7 || next == 8 || next == 9 || next == 10 || next == 11) {
						Data data = this.datas4degree[metric];
						if (next == 3)
							data.entrys.put(start4 + " ", new ArrayList<Double>());

						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 8)
							k = 4;
						else if (next == 9 || next == 10 || next == 11)
							k = 8;
						data.entrys.get(start4 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					// 8 avonoa 9 fb - ilp 10 afb -ilp 11 fb afb

					if (next == 13 || next == 15 || next == 17 || next == 18 || next == 19 || next == 20
							|| next == 21) {
						Data data = this.datas4degree[accuate];
						if (next == 13)
							data.entrys.put(start4 + " ", new ArrayList<Double>());

						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 18)
							k = 4;
						else if (next == 19 || next == 20 || next == 21)
							k = 8;
						data.entrys.get(start4 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 23 || next == 25 || next == 27 || next == 28 || next == 29 || next == 30
							|| next == 31) {
						Data data = this.datas4degree[parent];
						if (next == 23)
							data.entrys.put(start4 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 28)
							k = 4;
						else if (next == 29 || next == 30 || next == 31)
							k = 8;
						data.entrys.get(start4 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 33 || next == 35 || next == 37 || next == 38 || next == 39 || next == 40
							|| next == 41) {
						Data data = this.datas4degree[child];
						if (next == 33)
							data.entrys.put(start4 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 38)
							k = 4;
						else if (next == 39 || next == 40 || next == 41)
							k = 8;
						data.entrys.get(start4 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 43 || next == 45 || next == 47 || next == 48 || next == 49 || next == 50
							|| next == 51) {
						Data data = this.datas4degree[ignore];
						if (next == 43)
							data.entrys.put(start4 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 48)
							k = 4;
						else if (next == 49 || next == 50 || next == 51)
							k = 8;
						data.entrys.get(start4 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 53 || next == 55 || next == 57 || next == 58 || next == 59 || next == 60
							|| next == 61) {
						Data data = this.datas4degree[irrlevant];
						if (next == 53)
							data.entrys.put(start4 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 58)
							k = 4;
						else if (next == 59 || next == 60 || next == 61)
							k = 8;
						data.entrys.get(start4 + " ").add(Double.parseDouble(tokensAll[k]));
					}

					if (next == 63 || next == 65 || next == 67 || next == 68 || next == 69 || next == 70
							|| next == 71) {
						Data data = this.datas4degree[testNum];
						if (next == 63)
							data.entrys.put(start4 + " ", new ArrayList<Double>());
						String[] tokensAll = strLine.split(" ");
						int k = 2;
						if (next == 68)
							k = 4;
						else if (next == 69 || next == 70 || next == 71)
							k = 8;
						data.entrys.get(start4 + " ").add(Double.parseDouble(tokensAll[k]));
					}
				}

			}
		}
	}

	public void coduct() throws Exception {
		// HandleResultFile handle = new HandleResultFile("the10_15.txt");

		System.out.println("case study 2  & 3");
		System.out.println("one" + " distin" + " ilp" + " radnom" + "  p-value");
		this.show(this.datas);

		System.out.println("case study 4 2-way");
		System.out.println(
				"fda-cit" + " ilp" + " afda-cit" + " avnoa" + " pvalue(1,2)" + " pvalue(2,3)" + " pvalue(1,3)");

		this.show(this.datas2degree);

		System.out.println("case study 4 3-way");
		this.show(this.datas3degree);

		System.out.println("case study 4 4-way");
		this.show(this.datas4degree);

	}

	public void show(List<Data[]> list) {
		for (int i = 0; i < listNum; i++) {
			System.out.println(stringofmetric[i]);
			for (Data[] data : list) {
				this.show(data, i);
			}
			// showAverage(list, i);
		}

		System.out.println("avg");
		System.out.print("[");
		for (int i = 0; i < listNum; i++) {
			showAverage(list, i);
		}
		System.out.println("]");
	}
	
	public void showWithlist(List<Data[]> list) {
		for (int i = 0; i < listNum; i++) {
			System.out.println(stringofmetric[i]);
			for (Data[] data : list) {
				this.show(data, i);
			}
			// showAverage(list, i);
		}

		System.out.println("list");
		System.out.print("[");
		for (int i = 0; i < listNum; i++) {
			showList(list, i);
		}
		System.out.println("]");
	}

	public void showAverage(List<Data[]> list, int i) {
		Mean mean = new Mean();
		if (list == null || list.size() <= 0 || list.get(0).length == 0 || list.get(0)[i].entrys == null
				|| list.get(0)[i].entrys.keySet() == null || list.get(0)[i].entrys.get(0 + " ") == null)
			return;

		int size = list.get(0)[i].entrys.get(0 + " ").size();
		double[] result = new double[size];

		for (int j = 0; j < size; j++) {
			List<Double> listDouble = new ArrayList<Double>();
			for (Data[] data : list) {
				for (int k = 0; k < data[i].entrys.keySet().size(); k++) {
					String key = k + " ";
					// if (data[i].entrys.containsKey(key)) {
					// System.out.print(key + " : ");
					List<Double> da = data[i].entrys.get(key);
					listDouble.add(da.get(j));
					// }
				}
			}
			double[] temp = new double[listDouble.size()];
			for (int k = 0; k < temp.length; k++)
				temp[k] = listDouble.get(k);
			result[j] = mean.evaluate(temp);
		}

		// System.out.println("avg: ");
		int k = 0;
		for (double r : result) {
			k++;
			if (k == 1)
				System.out.print("[");
			if (k < 3)
				System.out.print(r + ",");
			else {
				System.out.print(r + "]");
				break;
			}
		}
		System.out.println(",");
	}

	public void showList(List<Data[]> list, int i) {
		if (list == null || list.size() <= 0 || list.get(0).length == 0 || list.get(0)[i].entrys == null
				|| list.get(0)[i].entrys.keySet() == null || list.get(0)[i].entrys.get(0 + " ") == null)
			return;

		int size = list.get(0)[i].entrys.get(0 + " ").size();
		double[][] result = new double[size][];

		for (int j = 0; j < size; j++) {
			List<Double> listDouble = new ArrayList<Double>();
			for (Data[] data : list) {
				for (int k = 0; k < data[i].entrys.keySet().size(); k++) {
					String key = k + " ";
					// if (data[i].entrys.containsKey(key)) {
					// System.out.print(key + " : ");
					List<Double> da = data[i].entrys.get(key);
					listDouble.add(da.get(j));
					// }
				}
			}
			result[j] = new double[listDouble.size()];
			for (int k = 0; k < result[j].length; k++)
				result[j][k] = listDouble.get(k);
			// result[j] = mean.evaluate(temp);
		}

		// System.out.println("avg: ");
		int count = 4;
		if(i >= millions)
			count = 3;
		int k = 0;
		for (double[] r : result) {
			k++;
			if (k == 1)
				System.out.print("[");
			if (k < count) {
				showarray(r);
				System.out.print(",");
			} else {
				showarray(r);
				System.out.print("]");
				break;
			}
		}
		System.out.println(",");
	}

	public void showarray(double[] array) {
		System.out.print("[");
		for (double a : array)
			System.out.print(a + ", ");

		System.out.print("]");
	}

	public void conduct(List<HandleResultFile> list) {
		List<Data[]> first = new ArrayList<Data[]>();
		List<Data[]> second = new ArrayList<Data[]>();
		List<Data[]> third = new ArrayList<Data[]>();
		List<Data[]> fourth = new ArrayList<Data[]>();
		for (HandleResultFile hf : list) {
			first.add(hf.datas);
			second.add(hf.datas2degree);
			third.add(hf.datas3degree);
			fourth.add(hf.datas4degree);
		}

		System.out.println("case study 2  & 3");
		System.out.println("one" + " distin" + " ilp" + " radnom" + "  p-value");
		this.showWithlist(first);

		System.out.println("case study 4 2-way");
		System.out.println(
				"fda-cit" + " ilp" + " afda-cit" + " avnoa" + " pvalue(1,2)" + " pvalue(2,3)" + " pvalue(1,3)");

		this.show(second);

		System.out.println("case study 4 3-way");
		this.show(third);

		System.out.println("case study 4 4-way");
		this.show(fourth);

	}

	public static void main(String[] args) throws Exception {
		HandleResultFile handle7_11 = new HandleResultFile("the10_15.txt", 5);
		// handle.coduct();
		HandleResultFile handle5_6 = new HandleResultFile("grep.txt", 2);

		HandleResultFile handle3_4 = new HandleResultFile("the3_4.txt", 2);

		HandleResultFile handle0_2 = new HandleResultFile("the0_2.txt", 3);

		List<HandleResultFile> list = new ArrayList<HandleResultFile>();
		list.add(handle0_2);
		list.add(handle3_4);
		list.add(handle5_6);
		list.add(handle7_11);

		handle5_6.conduct(list);
	}
}
