package example;

import interaction.DataCenter;
import ct.AETG_OLD;
import ct.AETG;

public class TestAETG {

	public static void main(String[] args) {
		int[] param = new int[14];
		for (int i = 0; i < 14; i++)
			param[i] = 2;
		DataCenter dataCenter = new DataCenter(param,6);
		long current = System.currentTimeMillis();
		AETG aetg = new AETG(dataCenter);
		aetg.process();
		System.out.println(aetg.coveringArray.size());
		current = System.currentTimeMillis() - current;
		System.out.println(current/ 1000);
		
		
		 current = System.currentTimeMillis();
		AETG_OLD aetg2 = new AETG_OLD(dataCenter);
		aetg2.process();
		System.out.println(aetg2.coveringArray.size());
		current = System.currentTimeMillis() - current;
		System.out.println(current/ 1000);
	}
}
