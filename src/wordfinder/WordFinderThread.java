package wordfinder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import threaded.file.reader.JobThread;

public class WordFinderThread extends JobThread<LinkedHashSet<String>>{
	
	private Map<String, Double> words;
	private String seq = "";
	private String chr = "";
	private int pos = 1;
	private int longest;

	public WordFinderThread(Map<String, Double> words) {
		this.words = words;
		longest = findLongestWord();
	}
	
	private int findLongestWord(){
		int len = 0;
		for(String word : words.keySet()){
			if(word.length()>len) len = word.length();
		}
		return len;
	}
	
	private boolean match(String word, String seq, int index){
		if(index+word.length()<=seq.length()){
			return word.equals(seq.substring(index,index+word.length()));
		}
		return false;
	}
	
	private List<String> findMatches(String seq){
		List<String> matches = new ArrayList<String>();
		for(int i=0; i<seq.length(); i++){
			for(String word :  words.keySet()){
				if(match(word, seq, i)){
					matches.add(chr+"\t"+(pos+i)+"\t"+(pos+i+word.length()-1)+"\t"+word+"\t"+words.get(word));
				}
			}
		}
		return matches;
	}

	@Override
	public boolean process(LinkedHashSet<String> prev, String cur){
		if(cur.length() == 0){
			return false;
		}
		if(prev == null){
			prev = new LinkedHashSet<String>();
		}
		Scanner s = new Scanner(cur);
		while(s.hasNextLine()){
			String line = s.nextLine();
			if(line.startsWith(">")){
				chr = line;
				System.out.println("Searching "+chr);
				seq = "";
				pos = 1;
				continue;
			}
			seq+=line.toUpperCase();
			prev.addAll(findMatches(seq));
			pos += seq.length()-longest+1;
			seq = seq.substring(seq.length()-longest+1);
		}
		s.close();
		return true;
	}

}
