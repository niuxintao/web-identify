package experimentDataMultipleDefects;

public class DataRecord {
	public int[] param;
	public int[][] wrongs;
	public int[][] bugs;
	public int[] faults;
	public int[][] priority;

	public void set(int[] param, int[][] wrongs, int[][] bugs, int[] faults,
			int[][] priority) {
		this.param = param;
		this.wrongs = wrongs;
		this.bugs = bugs;
		this.faults = faults;
		this.priority = priority;
	}
}