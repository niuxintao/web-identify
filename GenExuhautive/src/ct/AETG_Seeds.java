package ct;

import java.util.ArrayList;
import java.util.List;

import interaction.CoveringManage;
import interaction.DataCenter;

public class AETG_Seeds extends AETG {

//	public static final int M = 10;
//
//	public int[] coveredMark; //
//	public Integer unCovered;//
//
//	public List<int[]> coveringArray;
//	
//	public  DataCenter dataCenter;
//
	public List<int[]> seeds;
//	
//	private DealTupleOfIndex DOI;
//	
//	private GetFirstParameterValue gpv;
	
	public AETG_Seeds(DataCenter dataCenter) {
		super(dataCenter);
		
		seeds = new ArrayList<int[]> ();
		
	}

	public void init() {

	}
	
	public void addSeeds(List<int[]> seeds){
		this.seeds.addAll(seeds);
		for(int[] seed : seeds){
			CoveringManage cm = new CoveringManage(dataCenter);
			unCovered = cm.setCover(unCovered, coveredMark, seed);
		}
	}

	
	public static void main(String[] args) {
		int[] param = new int[] { 2, 2, 2, 2 };
		DataCenter dataCenter = new DataCenter(param, 3);
		AETG_Seeds aetg = new AETG_Seeds(dataCenter);
		
		List<int[]> a = new ArrayList<int[]> ();
		a.add(new int[] {0,0,1,0});
		a.add(new int[] {1,0,0,0});
		a.add(new int[] {1,1,1,0});
		a.add(new int[] {0,1,0,1});
		a.add(new int[] {1,0,1,1});
		aetg.addSeeds(a);
		aetg.process();
		for(int[] i : aetg.coveringArray)
			aetg.print(i);

	}

}

