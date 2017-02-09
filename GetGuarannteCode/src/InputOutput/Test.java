package InputOutput;

import java.util.HashSet;

public class Test {
  public static void main(String[] args){
	  HashSet<Integer> a = new HashSet<Integer> ();
	  a.add(1);
	  a.add(2);
	  HashSet<Integer> b = new HashSet<Integer> ();
	 b.add(3);
	  b.add(4);
	  a.retainAll(b);
	  System.out.println(a.size());
	  int[] result = new int[ a.size()];
		int k = 0;
		for (Integer cr :  a) {
			result[k] = cr;
			k++;
		}
	  for(int s : result)
		  System.out.println(s);
  }
}
