package com.IngressRobot.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.IngressRobot.UI.RobotUI;

public final class NotificationHelper
{

	public static final int NOTIFICATION_ID = 1938;
	
	private NotificationManager mNotificationManager;
	private PendingIntent mPendingIntent;
	private Notification mNotification;
	private Context mContext = null;

	public NotificationHelper(Context context) {
		mContext = context;
	}

	@SuppressWarnings("deprecation")
	public void setNotification(int icon, String msg, boolean visible) {
		if (mContext == null) {
			
		}

		if (mNotificationManager == null) {
			mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		if (visible) {
			if (mNotification == null) {
				mNotification = new Notification();
			}
			mNotification.icon = icon;
			mNotification.defaults &= ~Notification.DEFAULT_SOUND;
			mNotification.flags = Notification.FLAG_NO_CLEAR;
			mNotification.tickerText = "IngressRobot";

			if (mPendingIntent == null) {
				Intent intent = new Intent();
				intent.setClass(mContext, RobotUI.class);
				mPendingIntent = PendingIntent.getActivity(mContext, 0, intent,	0);
			}

			mNotification.setLatestEventInfo(mContext, "Ingress Robot", msg, mPendingIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mNotification);
		} else {
			mNotificationManager.cancel(NOTIFICATION_ID);
		}
	}
	
	public void cancelNotification(){
		mNotificationManager.cancel(NOTIFICATION_ID);
	}
}
