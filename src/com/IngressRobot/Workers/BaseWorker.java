package com.IngressRobot.Workers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.util.Log;

import com.IngressRobot.Const;
import com.IngressRobot.Settings.Setting;
import com.IngressRobot.Utils.ThreadHandler;
import com.IngressRobot.Utils.StreamGobbler.IStreamGobblerCB;
import com.IngressRobot.Utils.StreamGobbler.StreamGobbler;

public abstract class BaseWorker {
	
	protected abstract String getCommand();
	protected abstract String getRunnerName();
	protected abstract void presetting();
	
	private Thread mWorker = null;
	private Thread mInput = null;
	private Thread mError = null;
	private ArrayList<OnStatusChange> mListener = null;
	protected Setting mSetting = null;
	
	public static final String RUNNNING = "RUNNING";
	public static final String STOP_EXCEPTION = "STOP_EXCEPTION";
	public static final String FINISH = "FINISH";
	
	
	public BaseWorker(Setting setting) {	
		mListener = new ArrayList<OnStatusChange>();
		mSetting = setting;
	}
	
	public boolean isRunning(){
		boolean bRet = false;
		if(mWorker != null){
			if(mWorker.isAlive()){
				bRet = true;
			}else{
				 stop();
			}
		}
		return bRet;
	}
	
	
	public void execute() {
		if (isRunning()) {
			return;
		}
		
		mWorker = ThreadHandler.getInstance().createThread(new Runnable() {
			@Override
			public void run() {
				try {
					presetting();
					
					String cmd = getCommand();
					Log.i(Const.LOG_TAG, "cmd: " + cmd);
					if(cmd == null || cmd.isEmpty()){
						return;
					}
					
					// execute
					Process process = Runtime.getRuntime().exec(cmd);
					//Process process = Runtime.getRuntime().exec("su");
					//DataOutputStream os = new DataOutputStream(process.getOutputStream());
					//os.writeBytes(cmd + "\n");
					//os.flush();
					//os.writeBytes("exit\n");
					
					mInput = ThreadHandler.getInstance().createThread(new StreamGobbler(process.getInputStream(), mInputCB));
					mError = ThreadHandler.getInstance().createThread(new StreamGobbler(process.getErrorStream(), mErrorCB));
					mInput.start();
					mError.start();
					
					notifyListener(RUNNNING);
					process.waitFor();
					notifyListener(FINISH);
					
				} catch (Exception e) {
					notifyListener(STOP_EXCEPTION);
				}
			}
		});
		mWorker.start();
	}
	
	public void stop() {
		Thread thread = ThreadHandler.getInstance().createThread(new Runnable() {
			public void run() {
				if (mWorker != null) {
					ArrayList<Integer> pLisId = getProcessId("uiautomator");
					if (pLisId != null && pLisId.size() > 0) {
						for(int i=0; i<pLisId.size();i++){
						// Kill process
							int pId = pLisId.get(i).intValue();
							try {
								Log.i(Const.LOG_TAG, "kill process:" + pId);
								Runtime.getRuntime().exec(new String[] { "/system/xbin/su", "0", "/system/bin/sh", "-c", "kill -9 "+ pId});
							} catch (Exception e) {
								Log.e(Const.LOG_TAG, e.getMessage(), e);
							}
						}
					}
					ThreadHandler.getInstance().interruptThread(mWorker);
					mWorker = null;
				}

				if (mInput != null) {
					ThreadHandler.getInstance().interruptThread(mInput);
					mInput = null;
				}

				if (mError != null) {
					ThreadHandler.getInstance().interruptThread(mError);
					mError = null;
				}
			}
		});
		thread.start();
	}
	
	public boolean registerListener (OnStatusChange listener){
		boolean bRet = false;
		if(mListener == null){
			return bRet;
		}
		
		if(listener != null){
			bRet = true;
			if(mListener.contains(listener) == false){
				mListener.add(listener);
			}
		}
		return bRet;
	}
	
	public void notifyListener(String status){
		if(mListener == null){
			return;
		}
		
		for(int i=0; i<mListener.size();i++){
			OnStatusChange listener = mListener.get(i);
			listener.onStatusChange(status, getRunnerName());
		}
	}
	
	private ArrayList<Integer> getProcessId(String processName){
		ArrayList<Integer> pListId = new ArrayList<Integer>();
		
		if(processName == null || processName.isEmpty()){
			return pListId;
		}
		
		BufferedReader bufferedReader = null;
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "/system/xbin/su", "0", "/system/bin/sh", "-c", "ps grep " + processName});
			if (process == null) {
				Log.i(Const.LOG_TAG, "process is null");
				return pListId;
			}
			
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			boolean bFirstLine = false;
			while ((line = bufferedReader.readLine()) != null) {
				if(bFirstLine == false){
					bFirstLine = true;
					continue;
				}
				Log.i(Const.LOG_TAG,"ps list:" + line);
				// USER,PID,PPID,VSIZE,RSS,WCHAN,PC,NAME
				String[] content = line.trim().split("\\s+");
				if(content == null || content.length < 8){
					continue;
				}
				
				try{
					Integer pId = Integer.valueOf(content[1].trim());
					pListId.add(pId);
				}catch (NumberFormatException e){
					//do nothing
				}
			}
		} catch (Exception e) {
			//do nothing
		}finally {
			try {
				if(bufferedReader != null){
					bufferedReader.close();
					bufferedReader = null;						
				}
			} catch (IOException ioe) {
				Log.e(Const.LOG_TAG, ioe.getMessage(), ioe);
			}
		}
		return pListId;
	}
	
	
	private IStreamGobblerCB mInputCB = new IStreamGobblerCB() {
		@Override
		public void getMessage(String str) {
			Log.i(Const.LOG_TAG, str);
		}
	};
	
	private IStreamGobblerCB mErrorCB = new IStreamGobblerCB() {
		@Override
		public void getMessage(String str) {
			Log.i(Const.LOG_TAG, str);
		}
	};
}
