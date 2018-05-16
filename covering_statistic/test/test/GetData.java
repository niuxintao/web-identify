package test;

public class GetData {
	public String[] methods  = { "bottom_up", "top_down",  "bottom_up_issta",  "topdown_haiming"};
	public String[] metrics = {"size", "time", "rfd"};

	public static int Mbo_up = 0;
	public static int Mtop_down = 1;
	public static int Missta = 2;
	public static int Mhaming = 3;
	
	public static int Msize = 0;
	public static int Mtime = 1;
	public static int Mrfd = 2;

	
	public double[][][] time;
	public double[][][] size;
	public double[][][] rfd;
	
	GetData(){
		time = new double[4][][];
		size =  new double[4][][];
		rfd = new double[4][][];
	}
	
	
	public String getPath(int method, int metric){
		String path = "./";
		path += methods[method];
		path += "/";
		path += metrics[metric];
		path += ".txt";
		return path;
	}
	
	
	public void read(int method, int metric){
		String path = this.getPath(method, metric);
		ReadInput read = new ReadInput();
		read.read(path);
		double[][] result = read.getResult();
		if(metric == Msize){
			this.size[method] = result;
		}else if (metric == Mtime){
			this.time[method] = result;
		}else if (metric == Mrfd){
			this.rfd[method] = result;
		}
	}
	
	public void processAll(){
		for(int method :  new int[] {0, 1, 2, 3}){
			for(int metric : new int[]{0, 1 ,2}){
				this.read(method, metric);
			}
		}
	}
}

