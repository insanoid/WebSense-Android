package com.uob.websense;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

@ReportsCrashes(
		formKey = "", // This is required for backward compatibility but not used
		formUri = "http://54.186.15.10:3002/logs/WebSenseAndroid/"
		)
public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// The following line triggers the initialization of ACRA
		ACRA.init(this);
	}

}
