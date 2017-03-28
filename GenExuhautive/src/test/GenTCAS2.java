package test;

public class GenTCAS2 {

	static String[][] param = {
			{ "299", "300", "601" },
			{ "0", "1" },
			{ "0", "1" },
			{ "1", "2" },
			{ "600", "601" },
			{ "1", "2" },
			{ "0", "1", "2", "3" },
			{ "0", "399", "400", "499", "500", "639", "640", "739", "740",
					"840" },
			{ "0", "399", "400", "499", "500", "639", "640", "739", "740",
					"840" }, { "0", "1", "2" }, { "1", "2" }, { "0", "1" } };

	public static void main(String[] args) {
		BenchExecute tj = new BenchExecute(GenTCAS2.param);
		int[] num = { 3, 2, 2, 2, 2, 2, 4, 10, 10, 3, 2, 2 };
		tj.bench(num);
	}
}
