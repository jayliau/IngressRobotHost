package com.IngressRobot.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.IngressRobot.Const;
import com.IngressRobot.R;
import com.IngressRobot.Settings.RobotStatus;
import com.IngressRobot.Settings.Setting;
import com.IngressRobot.Utils.NotificationHelper;
import com.IngressRobot.Utils.ThreadHandler;
import com.IngressRobot.Workers.AutoAcquire;
import com.IngressRobot.Workers.AutoDrop;
import com.IngressRobot.Workers.AutoDropKey;
import com.IngressRobot.Workers.AutoHack;
import com.IngressRobot.Workers.AutoRecycle;
import com.IngressRobot.Workers.BaseWorker;
import com.IngressRobot.Workers.OnStatusChange;

public class RobotService extends Service implements OnStatusChange{
	
	private Setting mSetting = null;
	private NotificationHelper mNotifyHelper = null;
	private RobotStatus mStatus = RobotStatus.STOP;
	private BaseWorker mAutoHack = null;
	private BaseWorker mAutoDrop = null;
	private BaseWorker mAutoDropKey = null;
	private BaseWorker mAutoAcquire = null;
	private BaseWorker mAutoRecycle = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mSetting = new Setting(getApplicationContext());
		mNotifyHelper = new NotificationHelper(this);
		
		mAutoHack = new AutoHack(mSetting);
		mAutoHack.registerListener(this);

		mAutoDrop = new AutoDrop(mSetting);
		mAutoDrop.registerListener(this);
		
		mAutoDropKey = new AutoDropKey(mSetting);
		mAutoDropKey.registerListener(this);
		
		mAutoRecycle = new AutoRecycle(mSetting);
		mAutoRecycle.registerListener(this);
		
		mAutoAcquire = new AutoAcquire(mSetting);
		mAutoAcquire.registerListener(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int nRet = super.onStartCommand(intent, flags, startId);
		if(intent == null){
			return nRet;
		}
		
		String action = intent.getAction();
		if(action == null || action.isEmpty()){
			return nRet;
		}
		
		if(mStatus == RobotStatus.STOP){
			if(action.equals(Const.Intent_Action_AutoHack)){
				mAutoHack.execute();
				setStatus(RobotStatus.AUTOHACK);
			}else if(action.equals(Const.Intent_Action_AutoDrop)){
				mAutoDrop.execute();
				setStatus(RobotStatus.AUTODROP);
			}else if(action.equals(Const.Intent_Action_AutoDropKey)){
				mAutoDropKey.execute();
				setStatus(RobotStatus.AUTODROPKEY);
			}else if(action.equals(Const.Intent_Action_AutoAcquire)){
				mAutoAcquire.execute();
				setStatus(RobotStatus.AUTOACQUIRE);
			}else if(action.equals(Const.Intent_Action_AutoRecycle)){
				mAutoRecycle.execute();
				setStatus(RobotStatus.AUTORECYCLE);
			}
		}else{
			if(action.equals(Const.Intent_Action_StopRobot)){
				if(mStatus == RobotStatus.AUTOHACK){
					mAutoHack.stop();
				}else if(mStatus == RobotStatus.AUTODROP){
					mAutoDrop.stop();
				}else if(mStatus == RobotStatus.AUTODROPKEY){
					mAutoDropKey.stop();
				}else if(mStatus == RobotStatus.AUTOACQUIRE){
					mAutoAcquire.stop();
				}else if(mStatus == RobotStatus.AUTORECYCLE){
					mAutoRecycle.stop();
				}
				setStatus(RobotStatus.STOP);
			}
		}
		return nRet;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		//Set status as stop
		if(mStatus == RobotStatus.AUTOHACK){
			mAutoHack.stop();
		}else if(mStatus == RobotStatus.AUTODROP){
			mAutoDrop.stop();
		}else if(mStatus == RobotStatus.AUTODROPKEY){
			mAutoDropKey.stop();
		}else if(mStatus == RobotStatus.AUTOACQUIRE){
			mAutoAcquire.stop();
		}else if(mStatus == RobotStatus.AUTORECYCLE){
			mAutoRecycle.stop();
		}
		
		setStatus(RobotStatus.STOP);
		ThreadHandler.getInstance().interruptAllThread();
	}
	
	private void setStatus(RobotStatus status){
		mStatus = status;
		if(mSetting != null){
			mSetting.SetRobootStatus(status.name());
		}
		
		if(status == RobotStatus.STOP){
		    sendBroadcast(new Intent(Const.Intent_Action_StopRobot));
		    mNotifyHelper.cancelNotification();
		} else if (status == RobotStatus.AUTOHACK
				|| status == RobotStatus.AUTODROP
				|| status == RobotStatus.AUTODROPKEY
				|| status == RobotStatus.AUTOACQUIRE
				|| status == RobotStatus.AUTORECYCLE) {
			mNotifyHelper.setNotification(R.drawable.icon, "Running", true);
		}
		
		// update notification icons.
		Log.i(Const.LOG_TAG, "setStatus:" + status);
	}

	@Override
	public void onStatusChange(String status, String whichWorker) {
		Log.i(Const.LOG_TAG, "onStatusChange:" + status + ", worker:" + whichWorker);
		if(status == null || whichWorker == null){
			return;
		}
		
		if(status.equals(BaseWorker.STOP_EXCEPTION) || status.equals(BaseWorker.FINISH)){
			if(whichWorker.equals(AutoHack.class.getName())){
				mAutoHack.stop();
			}else if(whichWorker.equals(AutoDrop.class.getName())){
				mAutoDrop.stop();
			}else if(whichWorker.equals(AutoDropKey.class.getName())){
				mAutoDropKey.stop();
			}else if(whichWorker.equals(AutoAcquire.class.getName())){
				mAutoAcquire.stop();
			}else if(whichWorker.equals(AutoRecycle.class.getName())){
				mAutoRecycle.stop();
			}
			setStatus(RobotStatus.STOP);
		}
	}
}
