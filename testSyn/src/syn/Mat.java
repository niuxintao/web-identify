package syn;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

public class Mat {
	Stack<Integer> list = new Stack<Integer>();

	HashSet<List<Integer>> result = new HashSet<List<Integer>>();

	public void find_factor(int sum, int n, int[] A, int i) {

		// ����ҵ��Ľ��
		if (sum == 0) {
			List<Integer> temp = new ArrayList<Integer>();

			for (Integer k : list)
				temp.add(k);

			result.add(temp);
			// System.out.print(k + " ");
			// System.out.println();
		}

		if (i >= n || sum < 0)
			return;

		list.push(i); // ���͵�01��������
		find_factor(sum - A[i], n, A, i + 1); // ��i��i+1��������sum-n
		list.pop();
		find_factor(sum, n, A, i + 1); // ����i��i+1��������sum
	}

	public static void main(String[] args) {
		int n = 5;
		int[] A = { 1, 2, 3, 4, 5 };
		int sum = 6;
		Mat mat = new Mat();
		mat.find_factor(sum, n, A, 0);
		System.out.println(mat.result.size());
	}
}
