package runGrep;

import output.BenchExecute;

public class BenchTestGrep2_22 extends BenchExecute {
	public BenchTestGrep2_22(){
		super();
	}
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
		BenchTestGrep2_22 tj = new BenchTestGrep2_22();
		int[] param = new int[] { 3, 3, 4, 2, 2, 2, 2 };
		tj.bench(param);
		tj.showResult();
	}
}
