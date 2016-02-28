package com.IngressRobot.Workers;

import com.IngressRobot.Settings.Setting;
import com.IngressRobot.Utils.Config;

public class AutoDropKey extends BaseWorker{

	public AutoDropKey(Setting setting) {
		super(setting);
	}
	
	@Override
	protected String getCommand() {
		return "uiautomator runtest IngressRobotScript.jar -c com.autoingress.run.AutoDropKey";
	}

	@Override
	protected String getRunnerName() {
		return AutoHack.class.getName();
	}

	@Override
	protected void presetting() {
		if(mSetting == null){
			return;
		}
		
		Config.setProperty(Setting.KEY_DROP_NUMBER, String.valueOf(mSetting.GetDropNumber()));
		Config.storeProperty();
	}
}
