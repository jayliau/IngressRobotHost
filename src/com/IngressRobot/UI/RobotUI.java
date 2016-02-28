package com.IngressRobot.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.IngressRobot.Const;
import com.IngressRobot.R;
import com.IngressRobot.Services.RobotService;
import com.IngressRobot.Settings.RobotStatus;
import com.IngressRobot.Settings.Setting;
import com.IngressRobot.Utils.DialogHelper;


public class RobotUI extends PreferenceActivity implements OnPreferenceClickListener, OnPreferenceChangeListener{

	private Setting mSetting = null;
	private DialogHelper mDialogHelper = null;
	
	//UI
	private PreferenceScreen   mPref_StopRobot = null;
	//hack
	private CheckBoxPreference mPref_RunHack = null;
	private EditTextPreference mPref_HackNumber = null;
	private EditTextPreference mPref_Portal_1 = null;
	private EditTextPreference mPref_Portal_2 = null;
	private EditTextPreference mPref_Portal_3 = null;
	private EditTextPreference mPref_Portal_4 = null;
	//drop
	private CheckBoxPreference mPref_RunDrop = null;
	private CheckBoxPreference mPref_RunDropKey = null;
	private EditTextPreference mPref_DropNumber = null;
	//acquire
	private CheckBoxPreference mPref_RunAcquire = null;
	private EditTextPreference mPref_AcquireNumber = null;
	private EditTextPreference mPref_Acquire_Pos = null;
	//recycle
	private CheckBoxPreference mPref_RunRecycle = null;
	private EditTextPreference mPref_RecycleNumber = null;
	
	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent != null && intent.getAction().equals(Const.Intent_Action_StopRobot)){
				onStopRobotUIAction();
			}
		}
    };
	
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.ingress_robot_settings);

       mSetting = new Setting(getApplicationContext());
       mDialogHelper = new DialogHelper(this);
       
       initMainUI();
    }
    
    
	@SuppressWarnings("deprecation")
	private void initMainUI(){
		// Stop robot
		mPref_StopRobot = (PreferenceScreen) findPreference(Setting.KEY_STOP_ROBOT);
		mPref_StopRobot.setOnPreferenceClickListener(this);
		
    	// Hack preference
    	mPref_RunHack = (CheckBoxPreference) findPreference(Setting.KEY_HACK);
    	mPref_RunHack.setOnPreferenceChangeListener(this);
    	
    	mPref_HackNumber = (EditTextPreference)findPreference(Setting.KEY_HACK_NUMBER);
    	mPref_HackNumber.setSummary(mSetting.GetHackNumber());
    	mPref_HackNumber.setOnPreferenceChangeListener(this);
    	
    	mPref_Portal_1 = (EditTextPreference)findPreference(Setting.KEY_HACK_PORTAL_1);
    	mPref_Portal_1.setSummary(mSetting.GetHackPortal_1());
    	mPref_Portal_1.setOnPreferenceChangeListener(this);
    	
    	mPref_Portal_2 = (EditTextPreference)findPreference(Setting.KEY_HACK_PORTAL_2);
    	mPref_Portal_2.setSummary(mSetting.GetHackPortal_2());
    	mPref_Portal_2.setOnPreferenceChangeListener(this);
    	
    	mPref_Portal_3 = (EditTextPreference)findPreference(Setting.KEY_HACK_PORTAL_3);
    	mPref_Portal_3.setSummary(mSetting.GetHackPortal_3());
    	mPref_Portal_3.setOnPreferenceChangeListener(this);
    	
    	mPref_Portal_4 = (EditTextPreference)findPreference(Setting.KEY_HACK_PORTAL_4);
    	mPref_Portal_4.setSummary(mSetting.GetHackPortal_4());
    	mPref_Portal_4.setOnPreferenceChangeListener(this);
    	
    	// Drop preference
    	mPref_RunDrop = (CheckBoxPreference) findPreference(Setting.KEY_DROP);
    	mPref_RunDrop.setOnPreferenceChangeListener(this);
    	// Drop key preference
    	mPref_RunDropKey = (CheckBoxPreference) findPreference(Setting.KEY_DROPKEY);
    	mPref_RunDropKey.setOnPreferenceChangeListener(this);
    	
    	mPref_DropNumber = (EditTextPreference)findPreference(Setting.KEY_DROP_NUMBER);
    	mPref_DropNumber.setSummary(mSetting.GetDropNumber());
    	mPref_DropNumber.setOnPreferenceChangeListener(this);
    	
    	// Recycle preference
    	mPref_RunRecycle = (CheckBoxPreference) findPreference(Setting.KEY_RECYCLE);
    	mPref_RunRecycle.setOnPreferenceChangeListener(this);
    	
    	mPref_RecycleNumber = (EditTextPreference)findPreference(Setting.KEY_RECYCLE_NUMBER);
    	mPref_RecycleNumber.setSummary(mSetting.GetRecycleNumber());
    	mPref_RecycleNumber.setOnPreferenceChangeListener(this);
    	
    	//Acquire preference
    	mPref_RunAcquire = (CheckBoxPreference) findPreference(Setting.KEY_ACQUIRE);
    	mPref_RunAcquire.setOnPreferenceChangeListener(this);
    	
    	mPref_AcquireNumber = (EditTextPreference)findPreference(Setting.KEY_ACQUIRE_NUMBER);
    	mPref_AcquireNumber.setSummary(mSetting.GetAcquireNumber());
    	mPref_AcquireNumber.setOnPreferenceChangeListener(this);
    	
    	mPref_Acquire_Pos = (EditTextPreference)findPreference(Setting.KEY_ACQUIRE_POS);
    	mPref_Acquire_Pos.setSummary(mSetting.GetAquirePos());
    	mPref_Acquire_Pos.setOnPreferenceChangeListener(this);
    }
	

	@Override
	protected void onResume() {
		super.onResume();
		try{
			IntentFilter intentFilter = new IntentFilter(Const.Intent_Action_StopRobot);
			registerReceiver(mReceiver, intentFilter);
		}catch (IllegalArgumentException e){
			// do nothing
		}
		
		 
		// set defaylt first
		mPref_RunHack.setEnabled(false);
		mPref_RunDrop.setEnabled(false);
		mPref_RunDropKey.setEnabled(false);
		mPref_RunAcquire.setEnabled(false);
		mPref_RunRecycle.setEnabled(false);
		 
		// Check the check box every time.
		if (mSetting.GetRobootStatus().equals(RobotStatus.STOP.name())) {
			mPref_RunHack.setEnabled(true);
			mPref_RunDrop.setEnabled(true);
			mPref_RunDropKey.setEnabled(true);
			mPref_RunAcquire.setEnabled(true);
			mPref_RunRecycle.setEnabled(true);

			mPref_RunHack.setChecked(false);
			mPref_RunDrop.setChecked(false);
			mPref_RunDropKey.setChecked(false);
			mPref_RunAcquire.setChecked(false);
			mPref_RunRecycle.setChecked(false);
		} else if (mSetting.GetRobootStatus().equals(RobotStatus.AUTOHACK.name())) {
			mPref_RunHack.setChecked(true);
		} else if (mSetting.GetRobootStatus().equals(RobotStatus.AUTODROP.name())) {
			mPref_RunDrop.setChecked(true);
		} else if (mSetting.GetRobootStatus().equals(RobotStatus.AUTODROPKEY.name())) {
			mPref_RunDropKey.setChecked(true);
		} else if (mSetting.GetRobootStatus().equals(RobotStatus.AUTOACQUIRE.name())) {
			mPref_RunAcquire.setChecked(true);
		} else if(mSetting.GetRobootStatus().equals(RobotStatus.AUTORECYCLE.name())){
			mPref_RunRecycle.setChecked(false);
		}
	}
	
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}


	public boolean onPreferenceChange(Preference preference, Object newValue) {
		boolean bRet = true;
		if (preference == mPref_RunHack){
			final boolean isCheck = ((Boolean) newValue).booleanValue();
			if (isCheck) {
				//check portal positions has set
				if(mPref_Portal_1.getSummary().length() == 0 &&
				   mPref_Portal_2.getSummary().length() == 0 &&
				   mPref_Portal_3.getSummary().length() == 0 &&
				   mPref_Portal_4.getSummary().length() == 0){
					
				    mDialogHelper.showPositiveDialog("Warning", "Portal position not set.");
					bRet = false;
				}else{
					startRobotService(Const.Intent_Action_AutoHack);
				}
			}
		}else if(preference == mPref_RunDrop){
			startRobotService(Const.Intent_Action_AutoDrop);
		}else if(preference == mPref_RunDropKey){
			startRobotService(Const.Intent_Action_AutoDropKey);
		}else if (preference == mPref_RunRecycle){
			startRobotService(Const.Intent_Action_AutoRecycle);
		}else if(preference == mPref_RunAcquire){
			final boolean isCheck = ((Boolean) newValue).booleanValue();
			if (isCheck) {
				if (mPref_Acquire_Pos.getSummary().length() == 0) {
					mDialogHelper.showPositiveDialog("Warning", "Portal position not set.");
					bRet = false;
				}else {
					startRobotService(Const.Intent_Action_AutoAcquire);
				}
			} 
		} else if (preference == mPref_HackNumber
				|| preference == mPref_DropNumber
				|| preference == mPref_AcquireNumber
				|| preference == mPref_RecycleNumber) {
			String number = (String) newValue;
			try{
				Integer.parseInt(number);
				preference.setSummary(number);
			}catch (NumberFormatException nume){
				mDialogHelper.showPositiveDialog("Warning", "This is not integer.");
				bRet = false;
			}
		}else if (preference == mPref_Portal_1 || preference == mPref_Portal_2
				|| preference == mPref_Portal_3 || preference == mPref_Portal_4 
				|| preference == mPref_Acquire_Pos) {
			String strPos = (String) newValue;
			if(checkPosFormat(strPos) == false){
				mDialogHelper.showPositiveDialog("Warning", "Format should be x,y");
				bRet = false;
			}else{
				preference.setSummary(strPos);
			}
		}
		return bRet;
	}

	public boolean onPreferenceClick(Preference preference) {
		boolean bRet = true;
		if (preference == mPref_StopRobot){
			startRobotService(Const.Intent_Action_StopRobot);
			onStopRobotUIAction();
		}
		return bRet;
	}

	private void onStopRobotUIAction() {
		// set auto hack
		if(mPref_RunHack.isEnabled() == false){
			mPref_RunHack.setEnabled(true);
			mPref_RunHack.setChecked(false);
		}
		
		if(mPref_RunDrop.isEnabled() == false){
			mPref_RunDrop.setEnabled(true);
			mPref_RunDrop.setChecked(false);
		}
		
		if(mPref_RunDropKey.isEnabled() == false){
			mPref_RunDropKey.setEnabled(true);
			mPref_RunDropKey.setChecked(false);
		}
		
		if(mPref_RunRecycle.isEnabled() == false){
			mPref_RunRecycle.setEnabled(true);
			mPref_RunRecycle.setChecked(false);
		}
		
		if(mPref_RunAcquire.isEnabled() == false){
			mPref_RunAcquire.setEnabled(true);
			mPref_RunAcquire.setChecked(false);
		}
	}
	
	private boolean checkPosFormat(String strPos){
		if (strPos == null || strPos.isEmpty()){
			return true;
		}
		
		String[] split = strPos.split(",");
		if(split == null || split.length != 2){
			return false;
		}
		
		boolean bRet = true;
		try{
			Integer.parseInt(split[0].trim());
			Integer.parseInt(split[1].trim());
		}catch(NumberFormatException nume){
			bRet = false;
		}
		return bRet;
	}
	
	private void startRobotService(final String action) {
		// update UI
		if (action != null && action.equals(Const.Intent_Action_StopRobot) == false) {
			mPref_RunHack.setEnabled(false);
			mPref_RunDrop.setEnabled(false);
			mPref_RunDropKey.setEnabled(false);
			mPref_RunRecycle.setEnabled(false);
			mPref_RunAcquire.setEnabled(false);
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(RobotUI.this, RobotService.class);
				intent.setAction(action);
				startService(intent);
			}
		}).start();
	}
}
