package example;

import interaction.DataCenter;
import ct.AETG;
import ct.AETG_HashSet;

public class TestAETG {

	public static void main(String[] args) {
		int[] param = new int[100];
		for (int i = 0; i < 100; i++)
			param[i] = 2;
		DataCenter dataCenter = new DataCenter(param,2);
		
		
		long   current = System.currentTimeMillis();
		AETG_HashSet aetg2 = new AETG_HashSet(dataCenter);
		aetg2.process();
		System.out.println(aetg2.coveringArray.size());
		current = System.currentTimeMillis() - current;
		System.out.println(current/ 1000);
		
		current = System.currentTimeMillis();
		AETG aetg = new AETG(dataCenter);
		aetg.process();
		System.out.println(aetg.coveringArray.size());
		current = System.currentTimeMillis() - current;
		System.out.println(current/ 1000);
		
	}
}
