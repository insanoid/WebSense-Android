package com.uob.websense.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.uob.websense.ui.RegisterActivity;

public class BootBroadcastReceiver  extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
			Intent i = new Intent(context, RegisterActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}
} 

