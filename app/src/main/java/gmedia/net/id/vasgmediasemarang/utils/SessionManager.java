package gmedia.net.id.vasgmediasemarang.utils;

/**
 * Created by Bayu on 29/12/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashSet;
import java.util.Set;

import gmedia.net.id.vasgmediasemarang.Login;
import gmedia.net.id.vasgmediasemarang.MainActivity;


public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;

	// Editor for Shared preferences
	Editor editor;

	// Context
	Context _context;

	// Shared pref mode

	int PRIVATE_MODE = 0;

	// Sharedpref file name
	private static final String PREF_NAME = "AndroidHivePref";

	// All Shared Preferences Keys
	private static final String IS_LOGIN = "IsLoggedIn";

	// User name (make variable public to access from outside)
	public static final String KEY_USER_ID = "name";
	public static final String KEY_TOKEN = "token";
	public static final String KEY_NAME = "hahaha";
	public static final String KEY_MENU = "menu";

	// Constructor
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
//		editorIDPerusahaan = pref.edit();
	}

	/**
	 * Create login session
	 */
	public void createLoginSession(String user_id, String nama, String token, Set<String> menu) {
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_USER_ID, user_id);
		editor.putString(KEY_NAME, nama);
		editor.putString(KEY_TOKEN, token);
		editor.putStringSet(KEY_MENU, menu);
		editor.commit();
	}

	/*public void createLoginSessionIDPerusahaan(String id_perusahaan) {
	 *//*prefIDPerusahaan = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editorIDPerusahaan = prefIDPerusahaan.edit();
		editorIDPerusahaan.putString(KEY_ID_PERUSAHAAN, id_perusahaan);
		editorIDPerusahaan.commit();*//*
		editor.putBoolean(IS_ID_PERUSAHAAN, true);
		editor.putString(KEY_ID_PERUSAHAAN, id_perusahaan);
		editor.commit();
	}*/

	/**
	 * Check login method wil check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 */
	public void checkLogin() {
		// Check login status
		if (this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, MainActivity.class);
			// Closing all the Activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

			// Add new Flag to start new Activity
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// Staring Login Activity
			((Activity) _context).startActivity(i);
			((Activity) _context).finish();
		}/* else if (!isLoggedIn()){
			Intent i = new Intent(_context, Login.class);
			((Activity) _context).startActivity(i);
			((Activity) _context).finish();
		}*/
	}

	/*public void checkIDPerusahaan() {
		if (this.isIDPerusahaan()) {
			Login.isIDPerusahaan = true;
		} else if (!this.isIDPerusahaan()) {
			Login.isIDPerusahaan = false;
		}
	}*/

	public HashSet<String> getMenu(){

		Set<String> result = new HashSet<String>();
		return (HashSet<String>) pref.getStringSet(KEY_MENU, result);
	}

	public String getKeyUserId() {
		return pref.getString(KEY_USER_ID, "");
	}

	public String getKeyToken() {
		return pref.getString(KEY_TOKEN, "");
	}

	public String getKeyName() {
		return pref.getString(KEY_NAME, "");
	}

	/**
	 * Clear session details
	 */
	public void logoutUser() {
		editor.putBoolean(IS_LOGIN, false);
		editor.putString(KEY_USER_ID, "");
		editor.putString(KEY_NAME, "");
		editor.putString(KEY_TOKEN, "");
		editor.commit();
		// After logout redirect user to Loing Activity
		Intent i = new Intent(_context, Login.class);
		// Closing all the Activities
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// Add new Flag to start new Activity
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// Staring Login Activity
		((Activity) _context).startActivity(i);
		((Activity) _context).finish();
	}

	/*public void deleteIDPerusahaan() {
		// Clearing all data from Shared Preferences
		editor.putBoolean(IS_ID_PERUSAHAAN, false);
		editor.putString(KEY_ID_PERUSAHAAN, "");
		editor.commit();
	}*/

	/**
	 * Quick check for login
	 **/
	// Get Login State
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}

//	public boolean isIDPerusahaan() {
//		return pref.getBoolean(IS_ID_PERUSAHAAN, false);
//	}
}