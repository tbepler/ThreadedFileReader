package wordfinder;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class DNAUtils {
	
	public static final String DNA_REGEX = "^[AaTtCcGg]+$";

	/**
	 * Reads intensities from the fourth column of the file.
	 * @param f the file to read from
	 * @return a mapping of sequences to intensities
	 * @throws FileNotFoundException 
	 */
	public static Map<String, Double> readIntensities(File f) throws FileNotFoundException{
		Map<String, Double> intensities = new HashMap<String, Double>();
		Scanner s = new Scanner(f);
		while(s.hasNextLine()){
			String fwd = null;
			String rvs = null;
			double escore = -1;
			double intensity = -1;
			if(s.hasNext()){
				fwd = s.next();
			}
			if(s.hasNext()){
				rvs = s.next();
			}
			if(s.hasNextDouble()){
				escore = s.nextDouble();
			}
			if(s.hasNextDouble()){
				intensity = s.nextDouble();
			}
			s.nextLine();
			if(fwd == null || !isDNA(fwd) || rvs == null || !isDNA(rvs) || escore == -1 || intensity == -1){
				continue;
			}
			intensities.put(fwd.toUpperCase(), intensity);
			intensities.put(rvs.toUpperCase(), intensity);
		}
		s.close();
		return intensities;
	}
	
	/**
	 * Reads e-scores from the third column of the file.
	 * @param f the file to read from
	 * @return a mapping of sequences to e-scores
	 * @throws FileNotFoundException
	 */
	public static Map<String, Double> readEScores(File f) throws FileNotFoundException{
		Map<String, Double> escores = new HashMap<String, Double>();
		Scanner s = new Scanner(f);
		while(s.hasNextLine()){
			String fwd = null;
			String rvs = null;
			double escore = -1;
			if(s.hasNext()){
				fwd = s.next();
			}
			if(s.hasNext()){
				rvs = s.next();
			}
			if(s.hasNextDouble()){
				escore = s.nextDouble();
			}
			s.nextLine();
			if(fwd == null || !isDNA(fwd) || rvs == null || !isDNA(rvs) || escore == -1){
				continue;
			}
			escores.put(fwd, escore);
			escores.put(rvs, escore);
		}
		s.close();
		return escores;
	}
	
	public static boolean isDNA(String s){
		return s.matches(DNA_REGEX);
	}
	
	
	
	
}
