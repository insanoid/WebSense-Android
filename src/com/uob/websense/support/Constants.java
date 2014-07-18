/* **************************************************
Copyright (c) 2014, University of Birmingham
Karthikeya Udupa, kxu356@bham.ac.uk

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.uob.websense.support;

import java.util.Calendar;

public class Constants {

	public static Boolean IS_DEBUG = false;
	
	public final static String AUTH_KEY_TOKEN = "AUTHKEY";
	
	
	public final static String APP_NAME_TAG = "APP_NAME";
	public final static String APP_PACKAGE_NAME_TAG = "APP_PACKAGE_NAME";
	public final static String LOG_TAG = "WEB_SENSE_LOG";
	public final static String SYNC_LOG_TAG = "SYNC_MGR";
	
	public static long BG_SERVICE_ALARM_RELOAD_TIME = 25*1000L;
	public static long TASK_POLLER_TIMER = 4*1000L;
	public static long ANIMATION_DURATION = 2;
	
	public static long DAY_MONTH = 31*(24*60*60*1000L);
	public static long DAY_WEEK = 7*(24*60*60*1000L);

	public static int MIN_RECORD_FOR_SYNC = 20;
	public static int RECORD_THRESHOLD_FOR_FORCED_SYNCED = 100;
	public static int RECORD_BATCH_COUNT = 50;
	

	//Database names
	public final static String APP_INFO = "APP_INFO.sqlite";
	public final static String APP_INFO_TABLE = "APP_INFO";
	public static final String CONTEXT_INFO_TABLE = "CONTEXT_INFO";
	public final static String APP_INFO_CACHE_TABLE = "APP_INFO_CACHE";

	//Notification Keys.
	public final static String APP_CONTENT_CHANGE = "KAPP_CONTENT_CHANGE";
	public final static String APP_INFO_KEY = "KAPP_INFO_KEY";

	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public static final String BASE_URL = "http://147.188.197.13:21129/";
	//"http://websense.karthikeya.co.uk/";//http://147.188.197.13:21129/";
	public static final String EULA_URL = "http://websense.karthikeya.co.uk/eula";
	
	static long currentSeconds = System.currentTimeMillis();
	static Calendar rightNow = Calendar.getInstance();
	static long offset = rightNow.get(Calendar.ZONE_OFFSET) + rightNow.get(Calendar.DST_OFFSET);
	static long sinceMidnight = (rightNow.getTimeInMillis() + offset) %(24 * 60 * 60 * 1000L);
	
	static long timeAtMonthStart = currentSeconds - (Constants.DAY_MONTH);
	static long timeAtMidnight = currentSeconds - (sinceMidnight);
	static long timeAtWeekStart = currentSeconds - (Constants.DAY_WEEK);
	
	public final static String APP_TRENDS_MONTHLY = "app/trends/"+timeAtMonthStart;
	public final static String APP_TRENDS_DAILY  = "app/trends/"+timeAtMidnight;
	public final static String APP_TRENDS_WEEKLY = "app/trends/"+timeAtWeekStart;
	
	public final static String APP_NEARBY_MONTHLY = "app/nearby/"+timeAtMonthStart;
	public final static String APP_NEARBY_DAILY  = "app/nearby/"+timeAtMidnight;
	public final static String APP_NEARBY_WEEKLY = "app/nearby/"+timeAtWeekStart;

	public final static String WEB_TRENDS_DAILY  = "web/trends/"+timeAtMidnight;
	public final static String WEB_TRENDS_WEEKLY = "web/trends/"+timeAtWeekStart;
	public final static String WEB_TRENDS_MONTHLY = "web/trends/"+timeAtMonthStart;

	public final static String WEB_NEARBY_DAILY  = "web/nearby/"+timeAtMidnight;
	public final static String WEB_NEARBY_WEEKLY = "web/nearby/"+timeAtWeekStart;
	public final static String WEB_NEARBY_MONTHLY = "web/nearby/"+timeAtMonthStart;
	
	public static final String CONTEXT_PUSH_METHOD = "context/update";
	public final static String APP_USAGE_METHOD = "app/update";
	public final static String REGISTERATION_METHOD = "user/create";
	public final static String AUTHENTICATE_METHOD = "user/authenticate";
	
	public final static String FONT_BOLD = "ProximaNova-Semibold.otf";
	public final static String FONT_LIGHT = "ProximaNova-Light.otf";
	public final static String FONT_REGLUAR = "ProximaNova-Regular.otf";
	
	public final static String ACRA_MONITORING_URL = "http://54.201.84.206:4444/logs/WebSenseAndroid/";

}