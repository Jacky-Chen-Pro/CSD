package com.android.incongress.cd.conference.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池
 */
public class ThreadPool {
    private static final int CORE_POOL_SIZE = 20;
    private static final int MAX_POOL_SIZE = 40;
    private static final int KEEP_ALIVE_TIME = 10; 
    private static ThreadPoolExecutor mExecutor;
    private static ThreadPool mPool;
    
    static{
        mExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new PriorityThreadFactory("thread-pool",android.os.Process.THREAD_PRIORITY_BACKGROUND));
    }
    
    public static ThreadPool getThreadPool(){
    	if (mPool == null) {
			mPool = new ThreadPool();
		}
    	return mPool;
    }
    
    
    public interface Job<T> {
    	public boolean execuding = false;
        public T run();
    }
    
    public interface JobDoneListener<A>{
    	
    	public A onJobDone(A a);
    }
    
    public <T> JobRunner<T> execute(Job<T> job){
    	
    	JobRunner<T> jobrunner = new JobRunner<T>(job);
    	mExecutor.execute(jobrunner);
    	return jobrunner;
    }
    
  public <T> JobRunner<T> execute(Job<T> job,JobDoneListener<T> listener){
    	
    	JobRunner<T> jobrunner = new JobRunner<T>(job,listener);
    	mExecutor.execute(jobrunner);
    	return jobrunner;
    }
    
    class JobRunner<T> implements Runnable{

    	private T result;
    	private boolean isDone;
    	private JobDoneListener<T> listener;
    	private Job<T> job;
    	private boolean executing;
    	
    	public boolean isExecuting() {
			return executing;
		}

		public void setExecuting(boolean executing) {
			this.executing = executing;
		}

		public JobRunner(Job<T> job){
    		this.job = job;
    	}
    	
    	public JobRunner(Job<T> job,JobDoneListener<T> t){
    		this.job = job;
    		listener = t;
    	}
    	
		@Override
		public void run() {
			T t = null;
			try {
				t = job.run();
				if (listener!=null) {
					listener.onJobDone(t);
				}
			} catch (Exception e) {
				isDone = true;
			}
			
			synchronized (this) {
				isDone = true;
				result = t;
				notifyAll();
			}
		}
		
		public boolean isDone(){
			return isDone;
		}
		
		public void cancle(){
			mExecutor.remove(this);
			isDone = false;
		}
		public T getResult(){
			if (!isDone) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return result;
		}
    	
    }

}
