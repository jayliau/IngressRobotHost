package com.IngressRobot.Workers;

import com.IngressRobot.Settings.Setting;
import com.IngressRobot.Utils.Config;

public class AutoHack extends BaseWorker{
	public AutoHack(Setting setting) {
		super(setting);
	}

	@Override
	protected String getCommand() {
		return "uiautomator runtest IngressRobotScript.jar -c com.autoingress.run.AutoHack";
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
		
		Config.setProperty(Setting.KEY_HACK_NUMBER, String.valueOf(mSetting.GetHackNumber()));
		Config.setProperty(Setting.KEY_HACK_PORTAL_1, String.valueOf(mSetting.GetHackPortal_1()));
		Config.setProperty(Setting.KEY_HACK_PORTAL_2, String.valueOf( mSetting.GetHackPortal_2()));
		Config.setProperty(Setting.KEY_HACK_PORTAL_3, String.valueOf(mSetting.GetHackPortal_3()));
		Config.setProperty(Setting.KEY_HACK_PORTAL_4, String.valueOf(mSetting.GetHackPortal_4()));
		Config.storeProperty();
	}

}
