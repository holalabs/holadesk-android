package com.holalabs.desk;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

public class NativeInterface {

	private static final int NOTI_ID = 1;
	
	Context mContext;
	
	NativeInterface(Context c) {
		mContext = c;
	}
	
	public void notify (String title, String msg, int duration) {
		Integer dur = Integer.valueOf(duration);
		duration = dur == null ? Toast.LENGTH_SHORT : duration;
		Toast.makeText(mContext, msg, duration).show();
		/*String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(ns);

	    Bitmap notiIcon = BitmapFactory.decodeFile(icon);
	    Intent notiIntent = new Intent(mContext, MainActivity.class);
	    PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notiIntent, 0);
		Builder notiBuilder = new Notification.Builder(mContext)
			.setContentTitle("holadesk :" + title)
			.setContentText(msg)
			.setContentIntent(contentIntent)
			.setLargeIcon(notiIcon)
			.setSmallIcon(R.drawable.ic_launcher);
		
		@SuppressWarnings("deprecation")
		Notification noti = notiBuilder.getNotification();
		
		mNotificationManager.notify(NOTI_ID, noti);*/
	}
	
	public AlertDialog.Builder buildPopup (String title, String text) {
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.AppTheme));
		builder.setTitle(title)
		       .setMessage(text)
		       .setCancelable(false);
		return builder;
	}
	
	public void showConfirm (String title, String text, String buttonText, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = buildPopup(title, text);
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
				})
		       .setPositiveButton(buttonText, listener);
		Dialog dialog = builder.create();
		dialog.show();
	}
	
	public void showInfo (String title, String text) {
		AlertDialog.Builder builder = buildPopup(title, text);
		builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
	       public void onClick(DialogInterface dialog, int id) {
	            dialog.cancel();
	       }
		});
		Dialog dialog = builder.create();
		dialog.show();
	}
	
}
