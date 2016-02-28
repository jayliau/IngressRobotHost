package com.IngressRobot.Workers;

import com.IngressRobot.Settings.Setting;
import com.IngressRobot.Utils.Config;

public class AutoRecycle extends BaseWorker{
	
	public AutoRecycle(Setting setting) {
		super(setting);
	}

	@Override
	protected String getCommand() {
		// TODO Auto-generated method stub
		return "uiautomator runtest IngressRobotScript.jar -c com.autoingress.run.AutoRecycle";
	}

	@Override
	protected String getRunnerName() {
		return AutoRecycle.class.getName();
	}

	@Override
	protected void presetting() {
		if(mSetting == null){
			return;
		}
		
		Config.setProperty(Setting.KEY_RECYCLE_NUMBER, String.valueOf(mSetting.GetRecycleNumber()));
		Config.storeProperty();
		
	}

}
