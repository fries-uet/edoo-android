package com.uet.fries.tmq.edoo.app;

import android.support.multidex.MultiDexApplication;

public class AppController extends MultiDexApplication {
	public static final String TAG = AppController.class.getSimpleName();

	private static AppController mInstance;

	@Override
	public void onCreate() {
		super.onCreate();

		mInstance = this;
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

}