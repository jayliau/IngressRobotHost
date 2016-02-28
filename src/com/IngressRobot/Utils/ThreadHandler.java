package com.IngressRobot.Utils;

import java.util.ArrayList;

import com.IngressRobot.Const;

import android.util.Log;



public class ThreadHandler {

	private static ThreadHandler mInstance = null;
	private ArrayList<Thread> mThreadList = new ArrayList<Thread>();

	private ThreadHandler() {
		
	}
	
	public synchronized static ThreadHandler getInstance(){
		if(mInstance == null){
			mInstance =  new ThreadHandler();
		}
		return mInstance;
	}
	
	public Thread createThread(Runnable runnable){
		Thread thread = new Thread(runnable);
		mThreadList.add(thread);
		return thread;
	}
	
	public void interruptThread(Thread thread){
		if(thread == null){
			return;
		}
		
		int index = mThreadList.indexOf(thread);
		if(index >= 0){
			mThreadList.remove(index);
		}
		
		if(thread.isAlive()){
			thread.interrupt();
		}
	}
	
	public void interruptAllThread(){
		for(int i=0;i<mThreadList.size();i++){
			Thread thread = mThreadList.get(i);
			if(thread != null && thread.isInterrupted() == false){
				if(thread.isAlive()){
					Log.i(Const.LOG_TAG,"Thread interrupt:" + thread.getName());
					thread.interrupt();
				}
			}
		}
		mThreadList.clear();
	}
}
