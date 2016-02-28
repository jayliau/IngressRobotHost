package com.IngressRobot.Utils.StreamGobbler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

import com.IngressRobot.Const;

public class StreamGobbler implements Runnable {
	private InputStream mInputStream = null;
	private IStreamGobblerCB mCallBack = null;
	private String mWriteFilePath = null;
	
	public StreamGobbler(InputStream inputStream, IStreamGobblerCB callBack) {
		mInputStream = inputStream;
		mCallBack = callBack;
	}
	
	public StreamGobbler(InputStream inputStream, IStreamGobblerCB callBack, String writeFilePath) {
		mInputStream = inputStream;
		mCallBack = callBack;
		mWriteFilePath = writeFilePath;
	}
	
	public void run() {
		BufferedReader bufferedReader = null;
		BufferedWriter bufferedWriter = null;
		
		try {
			
			bufferedReader = new BufferedReader(new InputStreamReader(mInputStream, "UTF-8"));
			if(mWriteFilePath != null && mWriteFilePath.isEmpty() == false){
				bufferedWriter = new BufferedWriter(new FileWriter(mWriteFilePath));
			}
			
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				if(mCallBack != null){
					mCallBack.getMessage(line);
				}
				if(bufferedWriter != null){
					bufferedWriter.write(line + "\n");
				}
			}
		} catch (IOException ioe) {
			Log.e(Const.LOG_TAG, ioe.getMessage(), ioe);
		} finally {
			try {
				if(bufferedReader != null){
					bufferedReader.close();
					bufferedReader = null;						
				}
			} catch (IOException ioe) {
				Log.e(Const.LOG_TAG, ioe.getMessage(), ioe);
			}
			
			try {
				if(bufferedWriter != null){
					bufferedWriter.flush();
					bufferedWriter.close();
					bufferedWriter = null;
				}
			} catch (IOException ioe) {
				Log.e(Const.LOG_TAG, ioe.getMessage(), ioe);
			}
		}
	}
}
