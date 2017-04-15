package com.uet.fries.tmq.edoo.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class PrefManager {
	private static String TAG = PrefManager.class.getSimpleName();
	private static int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "edoo_session";

	private static final String KEY_IS_LOGGED_IN = "is_login";
	private static final String KEY_IS_FIRST_LOGGED_IN = "is_first_login";
	private static final String KEY_TOKEN_LOG_IN = "token_login";
	private static final String KEY_IS_SAVE_CLASS = "is_save_class";

	private static SharedPreferences pref = null;

	private static Editor editor;

	public PrefManager(Context context) {
		if (pref== null) {
			pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
			editor = pref.edit();
		}
	}

	public static void setIsFirstLogin(boolean isFirstLogin) {
		editor.putBoolean(KEY_IS_FIRST_LOGGED_IN, isFirstLogin);
		editor.commit();
	}

	public static void setLogin(boolean isLoggedIn) {
		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
		editor.commit();
	}

	public static void setTokenLogin(String token) {
		editor.putString(KEY_TOKEN_LOG_IN, token);
		editor.commit();
	}

	public static boolean isFirstLoggedIn(){
		return pref.getBoolean(KEY_IS_FIRST_LOGGED_IN, true);
	}
	
	public static boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}

	public static String getTokenLogin() {
		String token = "";
		try {
			token = pref.getString(KEY_TOKEN_LOG_IN, "none");
		} catch (Exception e) {
			Log.e(TAG, "Token error");
		}

		return token;
	}

	// ---------------------------------------------------------------------------------------------

	public static void setIsSaveClass(boolean isSave) {
		editor.putBoolean(KEY_IS_SAVE_CLASS, isSave);
		editor.commit();
	}

	public static boolean isSaveClass(){
		return pref.getBoolean(KEY_IS_SAVE_CLASS, false);
	}
}
