package wordfinder;


import java.util.*;
import java.io.*;

import threaded.file.reader.ThreadedFileReader;

public class Processing {
	
	public static final int NUM_WORDS = 100;
	public static final int SEQ_OVERLAP = 10;


	
	private Map<String, Double> filterTopIntensities(Map<String, Double> intensities, int num){
		
		return MapUtil.getEntriesByValue(intensities, num, false);
		
	}
	
	private void scaleValues(Map<String, Double> map){
		double topScore = getTopScore(map);
		for(String word : map.keySet()){
			double val = map.get(word);
			map.put(word, (val/topScore)*1000);
		}
	}
	
	private double getTopScore(Map<String, Double> map){
		double score = Double.NEGATIVE_INFINITY;
		
		for(double val : map.values()){
			if(val > score) score = val;
		}
		
		return score;
	}
	
	private void writeLocations(LinkedHashSet<String> results, String out) throws IOException{
		Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out),"utf-8"));
		System.out.println("Writing file: \""+out+"\"");
		for(String s : results){
			w.write(s+"\n");
		}
		w.close();
	}
	
	public void generateDisplay(String pbmFile, String genomeFile, String outFile, int nThreads){
		
		Map<String, Double> intensities;
		try {
			System.out.println("Reading PBM file: \""+pbmFile+"\"");
			intensities = DNAUtils.readIntensities(new File(pbmFile));
		} catch (FileNotFoundException e) {
			System.err.println("PBM file not found.");
			return;
		}
		
		Map<String, Double> filtered = filterTopIntensities(intensities, NUM_WORDS);
		scaleValues(filtered);
		
		int wordsPerThread = filtered.size()/nThreads;
		
		WordFinderThread[] jobs = new WordFinderThread[nThreads];
		for(int i=0; i<jobs.length; i++){
			Map<String, Double> w = new HashMap<String, Double>();
			int count = 0;
			Iterator<String> iter = filtered.keySet().iterator();
			while(iter.hasNext()){
				String word = iter.next();
				
				if(count >= wordsPerThread && i!=jobs.length-1){
					break;
				}
				w.put(word, filtered.get(word));
				iter.remove();
				count++;
			}
			jobs[i] = new WordFinderThread(w);
		}
		
		try {
			System.out.println("Searching genome file: \""+genomeFile+"\" with "+nThreads+" threads");
			BufferedReader r = new BufferedReader(new FileReader(new File(genomeFile)));
			Collection<LinkedHashSet<String>> results = ThreadedFileReader.read(r, jobs);
			LinkedHashSet<String> condensed = new LinkedHashSet<String>();
			for(LinkedHashSet<String> set : results){
				condensed.addAll(set);
			}
			writeLocations(condensed, outFile);
		} catch (FileNotFoundException e) {
			System.err.println("Error: genome file \""+genomeFile+"\" not found");
		} catch (IOException e) {
			System.err.println("Error writing file: \""+outFile+"\"");
		}

	}
	
	
	
	
}
