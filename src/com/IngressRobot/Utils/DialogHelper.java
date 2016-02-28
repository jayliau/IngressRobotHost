package com.IngressRobot.Utils;


import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {

	private AlertDialog mDialog = null;
	private Context mContext = null;
	
	public DialogHelper(Context context) {
		mContext = context;
	}
	
	public void showPositiveDialog(String title, String message){
		showPositiveDialog(title, message, null);
	}
	
	public void showPositiveDialog(String title, String message, DialogInterface.OnClickListener listener){
		showPositiveDialog(title, message, "Ok", false, listener);
	}
	
	public void showPositiveDialog(String title, String message, String btnText, boolean bCancelable,DialogInterface.OnClickListener listener){
		dissmissDialog();
		if(mContext != null){			
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setCancelable(bCancelable);
			builder.setPositiveButton(btnText, listener);
			mDialog = builder.create();
			mDialog.show();
		}
	}
	
	public void showFullDialog(String title, String message, boolean bCancelable, String posText, DialogInterface.OnClickListener posListener, String negText, DialogInterface.OnClickListener negListener){
		dissmissDialog();
		if(mContext != null){
			AlertDialog.Builder builder = new Builder(mContext);
			builder.setTitle(title);
			builder.setMessage(message);
			builder.setCancelable(bCancelable);
			builder.setPositiveButton(posText, posListener);
			builder.setNegativeButton(negText, negListener);
			mDialog = builder.create();
			mDialog.show();
		}	
	}
	
	public void dissmissDialog(){
		if(mDialog != null){
			mDialog.dismiss();				
			mDialog = null;
		}
	}
}
