package wordfinder;

public class Main {
	
	public static void main(String[] args){
		
		if(args.length==0){
			System.out.println("Args should be [genome file] [pbm file] [nThreads] [output file]");
			return;
		}
		
		String genome = args[0];
		String pbm = args[1];
		int nThreads = Integer.parseInt(args[2]);
		String out = args[3];
		
		Processing p = new Processing();
		
		p.generateDisplay(pbm, genome, out, nThreads);
		
		
	}
	
}
