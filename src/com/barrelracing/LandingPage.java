/**
 * @Author - Prashant Kagwad
 * @Date - 11/21/2014
 * @Project Description : This program is written as a part of UI
 * Design Assignment to develop a  barrel race game. It is a rodeo
 * event in which the rider starts at a gate and must ride completely
 * around three barrels.  The objective is to get the fastest time
 * without knocking over any of the barrels.
 */
package com.barrelracing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * @info : LandingPage class to display the opening screen of the application.
 */
public class LandingPage extends Activity {

	private static String TAG = LandingPage.class.getName();
	private static long SLEEP_TIME = 2; // Sleep for some time

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar.
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar.
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_landing);

		// Start timer and launch main activity
		IntentLauncher launcher = new IntentLauncher();
		launcher.start();
	}

	/**
	 * Sleep for some time and than start new activity.
	 */
	private class IntentLauncher extends Thread {

		@Override
		public void run() {

			try {

				// Sleeping
				Thread.sleep(SLEEP_TIME * 1000);

			} catch (Exception e) {

				Log.e(TAG, e.getMessage());
			}

			// Start main activity
			Intent intent = new Intent(LandingPage.this, MainActivity.class);
			LandingPage.this.startActivity(intent);
			LandingPage.this.finish();
		}
	}
}
