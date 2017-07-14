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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @info : MainActivity class - This class is used to display the main screen
 *       (Home Screen) of the application/game.
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// New intent for Racing Page.
		final Intent racingIntent = new Intent(this, RacingPage.class);

		Button buttonStart = (Button) findViewById(R.id.buttonStart);
		// Button click listener for edit button.
		buttonStart.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				startActivity(racingIntent);
			}
		});

		// New intent for Racing Page.
		final Intent scoresIntent = new Intent(this, TopScoresPage.class);
		Button buttonScores = (Button) findViewById(R.id.buttonScores);
		// Button click listener for delete button.
		buttonScores.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				startActivity(scoresIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.top_scorers) {

			// New intent for Top scorers Page.
			Intent intent = new Intent(this, TopScoresPage.class);
			startActivity(intent);

		} else if (id == R.id.settings) {

			// New intent for Settings Page.
			Intent intent = new Intent(this, SettingsPage.class);
			startActivity(intent);

		} else if (id == R.id.info) {

			// New intent for Info Page.
			Intent intent = new Intent(this, InfoPage.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		// Control actions for back button.
		Intent setIntent = new Intent(Intent.ACTION_MAIN);
		setIntent.addCategory(Intent.CATEGORY_HOME);
		setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(setIntent);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
