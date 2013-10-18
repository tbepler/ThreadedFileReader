package threaded.file.reader;

import java.util.concurrent.Callable;

public abstract class JobThread<T> implements Callable<T>{
	
	private ReaderThread reader = null;
	private boolean ready = false;
	
	public abstract boolean process(T prev, String cur);
	
	public void setReader(ReaderThread reader){
		this.reader = reader;
	}
	
	public void markReady(){
		ready = true;
	}
	
	private void waitOnReader(){
		synchronized(reader){
			reader.notifyAll();
			while(!ready){
				try {
					reader.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			ready = false;
		}
	}
	
	@Override
	public T call() throws Exception{
		if(reader == null){
			throw new Exception("Error: no reader thread set.");
		}
		T r = null;
		reader.jobWaiting();
		while(true){
			waitOnReader();
			if(!process(r, reader.cur)){
				reader.jobDone();
				return r;
			}
			reader.jobWaiting();
		}
	}
	
}		
