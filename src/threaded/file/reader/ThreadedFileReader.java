package threaded.file.reader;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadedFileReader {
	
	public static <T> Collection<T> read(BufferedReader r, JobThread<T>[] jobs){
		Collection<Future<T>> futures = new ArrayList<Future<T>>();
		ReaderThread reader = new ReaderThread(r, jobs);
		for(JobThread<T> j : jobs){
			j.setReader(reader);
		}
		new Thread(reader).start();
		ExecutorService pool = Executors.newFixedThreadPool(jobs.length);
		for(JobThread<T> j : jobs){
			Future<T> f = pool.submit(j);
			futures.add(f);
		}
		Collection<T> c = new ArrayList<T>();
		for(Future<T> f : futures){
			try {
				c.add(f.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return c;
	}
	
	
	
}
