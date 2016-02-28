package com.IngressRobot.Utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.IngressRobot.Const;

/*
 * Configuration file for saving golden data
 */
public class Config {
	
	private static Properties mProps = null;
	private static File mConfig = null;

	static {
		mProps = new Properties();
		createConfig();
	}

	
	public static void setProperty(String key, String value) {
		if (key == null || key.isEmpty()){
			return;
		}
		mProps.setProperty(key, value);
	}
	
	public static void storeProperty(){
		try {
			mProps.store(new FileOutputStream(mConfig), null);
		} catch (FileNotFoundException e) {
			Log.i(Const.LOG_TAG, "Config: Exception message= " + e.getMessage());
		} catch (IOException e) {
			Log.i(Const.LOG_TAG, "Config: Exception message= " + e.getMessage());
		}
	}
	
	private static void createConfig() {
		FileInputStream fis = null;
		try {
			File file = new File(Const.IngressRobot_Path);
			if(file.exists() == false){
				file.mkdirs();
			}
			
			String ConfigfilePath = Const.IngressRobot_Path + Const.Config_file;
			mConfig = new File(ConfigfilePath);
			if(mConfig.exists()){
				mConfig.delete();
			}
			mConfig.createNewFile();
			
			fis = new FileInputStream(ConfigfilePath);
			mProps.load(fis);
		} catch (Exception e) {
			mProps = null;
			Log.i(Const.LOG_TAG, "Config: Exception message= " + e.getMessage());
			
		} finally {
			if(fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
