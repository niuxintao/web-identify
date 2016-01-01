package runGrep;

import output.BenchExecute;

public class BenchTestGrep2_22 extends BenchExecute {
	public String test(int[] set) {
		GrepTestCase gt = new GrepTestCase();
		return gt.testBoth33080_28588(set);
	}
	
	/** 
	 * parameter   --help  -A  1  --only-matching  --count  -E  -i
	 *		       --color -C  2	 
	 *			   --	   -B  3
	 * 						   5
	 * @return
	 */
	
	public static void main(String[] args){
		BenchExecute tj = new BenchExecute();
		int[] param = new int[] { 3, 3, 4, 2, 2, 2, 2 };
		tj.bench(param);
		tj.showResult();
	}
}
