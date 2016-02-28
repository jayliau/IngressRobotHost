package com.IngressRobot;

import java.io.File;

public class Const {
	 public static final String LOG_TAG = "IngressRobot";
	 
	 // Config path
	 public static final String IngressRobot_Path = File.separator + "sdcard" + File.separator + "ingressrobot" + File.separator;
	 public static final String Config_file = "reobot.config";
	 
	 public static final String Intent_Action_AutoHack = "com.IngressRobot.Action.Auto_Hack";
	 public static final String Intent_Action_AutoDrop = "com.IngressRobot.Action.Auto_Drop";
	 public static final String Intent_Action_AutoDropKey = "com.IngressRobot.Action.Auto_DropKey";
	 public static final String Intent_Action_AutoAcquire = "com.IngressRobot.Action.Auto_Acquire";
	 public static final String Intent_Action_AutoRecycle = "com.IngressRobot.Action.Auto_Recycle";
	 public static final String Intent_Action_StopRobot = "com.IngressRobot.Action.Stop_Rotbot";
	
}
