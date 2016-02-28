package com.IngressRobot.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Setting {
	public final static String KEY_ROBOT_STATUS = "ROBOT_STATUS";
	public final static String KEY_STOP_ROBOT = "stop_robot";
	
	//hack
	public final static String KEY_HACK = "hack";
	public final static String KEY_HACK_NUMBER = "hack_number";
	public final static String KEY_HACK_PORTAL_1 = "portal_1";
	public final static String KEY_HACK_PORTAL_2 = "portal_2";
	public final static String KEY_HACK_PORTAL_3 = "portal_3";
	public final static String KEY_HACK_PORTAL_4 = "portal_4";
	
	// drop
	public final static String KEY_DROP = "drop";
	public final static String KEY_DROPKEY = "drop_key";
	public final static String KEY_DROP_NUMBER = "drop_number";
	
	// acquire
	public final static String KEY_ACQUIRE = "acquire";
	public final static String KEY_ACQUIRE_NUMBER = "acquire_number";
	public final static String KEY_ACQUIRE_POS = "acquire_pos";
	
	//recycle
	public final static String KEY_RECYCLE = "recycle";
	public final static String KEY_RECYCLE_NUMBER = "recycle_number";
	
	private SharedPreferences msharePref = null;
	private Editor mEditor = null;
	
	public Setting(Context context) {
		if(context == null){
			return;
		}
		
		msharePref = PreferenceManager.getDefaultSharedPreferences(context);
		mEditor = msharePref.edit();
	}
	  
	public String GetRobootStatus() {
		if(msharePref == null){
			return RobotStatus.STOP.name();
		}
        return msharePref.getString(KEY_ROBOT_STATUS, RobotStatus.STOP.name());
    }
	
	public void SetRobootStatus(String status) {
		if(msharePref == null){
			return;
		}
		mEditor.putString(KEY_ROBOT_STATUS, status);
		mEditor.commit();
    }
	
	public String GetHackNumber(){
		if(msharePref == null){
			return "0";
		}
        
        return msharePref.getString(KEY_HACK_NUMBER, "0");
	}
	
	public String GetHackPortal_1(){
		if(msharePref == null){
			return "";
		}
        return msharePref.getString(KEY_HACK_PORTAL_1, "");
	}
	
	public String GetHackPortal_2(){
		if(msharePref == null){
			return "";
		}
        return msharePref.getString(KEY_HACK_PORTAL_2, "");
	}
	
	public String GetHackPortal_3(){
		if(msharePref == null){
			return "";
		}
        return msharePref.getString(KEY_HACK_PORTAL_3, "");
	}
	
	public String GetHackPortal_4(){
		if(msharePref == null){
			return "";
		}
        return msharePref.getString(KEY_HACK_PORTAL_4, "");
	}
	
	public String GetDropNumber(){
		if(msharePref == null){
			return "0";
		}
        
        return msharePref.getString(KEY_DROP_NUMBER, "0");
	}
	
	public String GetAcquireNumber(){
		if(msharePref == null){
			return "0";
		}
        
        return msharePref.getString(KEY_ACQUIRE_NUMBER, "0");
	}
	
	public String GetAquirePos(){
		if(msharePref == null){
			return "";
		}
        return msharePref.getString(KEY_ACQUIRE_POS, "");
	}
	
	public String GetRecycleNumber(){
		if(msharePref == null){
			return "0";
		}
        
        return msharePref.getString(KEY_RECYCLE_NUMBER, "0");
	}
}
