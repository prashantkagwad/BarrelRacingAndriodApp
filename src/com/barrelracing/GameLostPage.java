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
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * @info : GameLostPage class - This class is used to display game lost screen
 *       and control all the activities on the screen.
 */
public class GameLostPage extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_lost);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// Textview to display game lost message.
		TextView messageText = (TextView) findViewById(R.id.textViewMessage);
		messageText.setText(R.string.lost_message);

		// New intent for Main Page.
		final Intent mainIntent = new Intent(this, MainActivity.class);
		Button buttonBack = (Button) findViewById(R.id.buttonBack);
		// Button click listener for back button.
		buttonBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				startActivity(mainIntent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_lost, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// int id = item.getItemId();
		// if (id == R.id.action_settings) {
		// return true;
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {

		// Control actions for back button.
		Intent mainIntent = new Intent(this, MainActivity.class);
		startActivity(mainIntent);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
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
			View rootView = inflater.inflate(R.layout.fragment_game_lost,
					container, false);
			return rootView;
		}
	}
}
