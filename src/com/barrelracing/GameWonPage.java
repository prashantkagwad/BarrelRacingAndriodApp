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

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.barrelracing.data.Score;
import com.barrelracing.io.FileIO;

/**
 * @info : GameWonPage class - This class is used to display game won screen and
 *       control all the activities on the screen.
 */
@SuppressLint("NewApi")
public class GameWonPage extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_won);

		// Extract the contact details from the Main Page.
		final String time = (String) getIntent().getExtras().get("time");

		// Display time.
		TextView timeText = (TextView) findViewById(R.id.textViewTime);
		timeText.setText("Your time was " + time);

		TextView messageText = (TextView) findViewById(R.id.textViewMessage);

		TableRow nameTableRow = (TableRow) findViewById(R.id.tableRowName);
		nameTableRow.setVisibility(View.INVISIBLE);

		TableRow backTableRow = (TableRow) findViewById(R.id.tableRowButtonBack);
		backTableRow.setVisibility(View.INVISIBLE);

		TableRow saveTableRow = (TableRow) findViewById(R.id.tableRowButtonSave);
		saveTableRow.setVisibility(View.INVISIBLE);

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

		Button buttonSave = (Button) findViewById(R.id.buttonSave);
		// Button click listener for save button.
		buttonSave.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				EditText nameText = (EditText) findViewById(R.id.editTextName);
				String name = nameText.getText().toString();

				// Enter the name.
				if (!(name.trim().isEmpty() || name.trim().equalsIgnoreCase(""))) {

					Score score = new Score();
					score.setName(name);
					score.setStringTime(time);
					score.setIntTime(Integer.parseInt(time.replace(":", "")));

					if (save(score)) {

						// If the name and time was saved successfully, proceed.
						// Toast.makeText(getApplicationContext(),
						// "Saved!! Time : " + time, Toast.LENGTH_SHORT)
						// .show();

						startActivity(mainIntent);
					}

				} else {

					// Mandatory Field - Name field cannot be empty.
					nameText.requestFocus();
					Toast.makeText(getBaseContext(),
							"Name field cannot be empty!", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		if (check(time)) {

			// If the time eligible to be in top 10 scorers.
			nameTableRow.setVisibility(View.VISIBLE);
			saveTableRow.setVisibility(View.VISIBLE);
			messageText.setText(R.string.won_message_in);

		} else {

			// If the time was more then the last entry in the scoreList.
			backTableRow.setVisibility(View.VISIBLE);
			messageText.setText(R.string.won_message_out);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_won, menu);
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	/**
	 * @function : check(time) - function to check the current time against the
	 *           top 10 scorers from scorers.txt file.
	 * @param : time - current time.
	 * @return : True - if the user's time was less then the lowest time in the
	 *         list, False - otherwise.
	 */
	public boolean check(String time) {

		// Read the file and get the top 10 scorers list.
		final FileIO fileIO = new FileIO();
		final ArrayList<Score> scoreList = fileIO.readFromFile();

		if (scoreList.size() < 10) {

			// If number of scorers are less then 10, return true.
			return true;

		} else {

			// Else, compare the user time to the number 10 scorer(bottom
			// scorer).
			int newTime = Integer.parseInt(time.replace(":", ""));

			Score score = scoreList.get(scoreList.size() - 1);

			if (newTime <= score.getIntTime()) {

				return true;
			}

			return false;
		}
	}

	/**
	 * @function : save(score) - function to save the new time into top 10
	 *           scorers list.
	 * @param : score - object containing user name and new time.
	 * @return : True - if the score object was successfully saved else, False -
	 *         otherwise.
	 */
	public boolean save(Score score) {

		try {

			int time = score.getIntTime();

			// Read the file and get the scorers list.
			final FileIO fileIO = new FileIO();
			final ArrayList<Score> scoreList = fileIO.readFromFile();

			if (scoreList.size() < 10) {

				// If number of scorers are less then 10, then simply add.
				scoreList.add(score);
				fileIO.writeToFile(scoreList);

			} else {

				// Else, remove the bottom entry and insert the new record to
				// the list. Sorting takes place before writing to file.

				Score bottomScore = scoreList.get(scoreList.size() - 1);

				if (time <= bottomScore.getIntTime()) {

					scoreList.remove(scoreList.size() - 1);
					scoreList.add(score);
					fileIO.writeToFile(scoreList);
				}
			}

			// Return true if new score was saved successfully.
			return true;

		} catch (Exception e) {

			// Return false if new score was not saved successfully.
			e.printStackTrace();
			return false;
		}
	}
}
