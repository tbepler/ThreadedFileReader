package threaded.file.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.CharBuffer;

public class ReaderThread implements Runnable{
	
	public static final int CAPACITY_BYTES = (int) (8*Math.pow(2,6));
	public static final int CHARSIZE_BYTES = 2;
	public static final int DEFAULT_CAPACITY = CAPACITY_BYTES/CHARSIZE_BYTES;
	
	private BufferedReader reader;
	public String cur;
	private CharBuffer next;
	private int capacity;
	
	private JobThread<?>[] jobs;
	private int threadCount;
	private int completedThreads;
	
	public ReaderThread(BufferedReader reader, JobThread<?>[] processors){
		this(reader, processors, DEFAULT_CAPACITY);
	}
	
	public ReaderThread(BufferedReader reader, JobThread<?>[] jobs, int capacity){
		this.reader = reader;
		this.jobs = jobs;
		this.threadCount = jobs.length;
		this.completedThreads = 0;
		this.capacity = capacity;
		this.next = CharBuffer.allocate(capacity);
	}
	
	public synchronized void jobWaiting(){
		completedThreads++;
	}
	
	private void fillCurWithNext(){
		cur = next.toString();
	}
	
	private int readNext() throws IOException{
		//System.out.println("Reading next.");
		next = CharBuffer.allocate(capacity);
		int read = reader.read(next);
		next.flip();
		return read;
	}
	
	private void waitOnJobThreads(){
		while(completedThreads < threadCount){
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		completedThreads = 0;
	}
	
	private void markJobsReady(){
		for(JobThread<?> p : jobs){
			p.markReady();
		}
	}
	
	public synchronized void jobDone(){
		threadCount--;
	}

	@Override
	public void run() {
		boolean finished = false;
		try {
			if(readNext()==-1){
				finished = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		while(true){
			synchronized(this){
				waitOnJobThreads();
				if(threadCount <= 0){
					return;
				}
				fillCurWithNext();
				markJobsReady();
				this.notifyAll();
				if(finished){
					waitOnJobThreads();
					return;
				}
			}
			try {
				if(readNext()==-1){
					finished = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	
}
