package test;

public class GenTotoinfo {

	static String[][] param = {
			{ "1", "2", "3" },
			{ "1", "2", "3" },
			{ "1", "2", "3"},
			{ "1", "2", "3","4", "5", "6"},
			{ "1", "2", "3","4", "5"},
			{ "1", "2", "3","4", "5"}};

	public static void main(String[] args) {
		BenchExecute tj = new BenchExecute(GenTotoinfo.param);
		int[] num = { 3, 3, 3, 6, 5, 5};
		tj.bench(num);
	}
}
