package com.IngressRobot.Receiver;

import com.IngressRobot.Const;
import com.IngressRobot.Settings.RobotStatus;
import com.IngressRobot.Settings.Setting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(Const.LOG_TAG, "receive boot complete.");
		if(intent != null && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Setting setting = new Setting(context);
			setting.SetRobootStatus(RobotStatus.STOP.name());
        }
		
	}

}
