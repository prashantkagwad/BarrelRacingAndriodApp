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
import java.util.Iterator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import com.barrelracing.data.Score;
import com.barrelracing.io.FileIO;

/**
 * @info : TopScoresPage class - This class is used to display a list of top 10
 *       scorers.
 */
public class TopScoresPage extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		// Suppress keyboard on start
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		// Read the file and get the score list.
		final FileIO fileIO = new FileIO();
		final ArrayList<Score> scoreList = fileIO.readFromFile();

		// Display the list on the screen.
		displayScoreList(scoreList);

		// New intent for Main Page.
		final Intent intentMain = new Intent(this, MainActivity.class);
		Button buttonBack = (Button) findViewById(R.id.buttonBack);

		// Button click listener for back button.
		buttonBack.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click
				startActivity(intentMain);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.score, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_score,
					container, false);
			return rootView;
		}
	}

	/**
	 * @function : displayScoreList(scoreList) - function to display top 10
	 *           scorers from scorers.txt file.
	 * @param : ArrayList<scoreList> - array list of scorers objects read from
	 *        the file.
	 */
	@SuppressLint("NewApi")
	public void displayScoreList(final ArrayList<Score> scoreList) {

		// TableLayout in which the list of contacts are displayed.
		TableLayout tableLayout = (TableLayout) findViewById(R.id.contactList);
		tableLayout.removeAllViews();

		// Displaying each contact as a Table Row.
		Iterator<Score> iterator = scoreList.iterator();
		int counter = 0;
		while (iterator.hasNext()) {

			// Alternating background color for contact list.
			int backgroundColor = getResources().getColor(R.color.grey);
			if (counter % 2 == 0) {
				backgroundColor = getResources().getColor(R.color.white);
			}

			Score score = iterator.next();

			// Create a new inner TableLayout to hold two rows.
			TableLayout table = new TableLayout(this);
			TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f);
			table.setLayoutParams(layoutParams);
			table.setPadding(20, 20, 20, 20);
			table.setClickable(true);
			table.setId(counter);
			// table.setOnClickListener(tablerowOnClickListener);
			table.setBackgroundColor(backgroundColor);

			// Create a new TableRow to hold TextView that has name.
			TableRow rowName = new TableRow(this);
			rowName.setLayoutParams(layoutParams);
			rowName.setGravity(Gravity.START);

			String name = score.getName();
			SpannableString spanName = new SpannableString(" " + (counter + 1)
					+ ". " + name);
			spanName.setSpan(new StyleSpan(Typeface.BOLD), 0,
					spanName.length(), 0);
			spanName.setSpan(new StyleSpan(Typeface.ITALIC), 0,
					spanName.length(), 0);

			// Create a new TextView to hold name.
			TextView textViewName = new TextView(this);
			textViewName.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			LayoutParams paramsViewName = (LayoutParams) textViewName
					.getLayoutParams();
			paramsViewName.height = 50;
			textViewName.setLayoutParams(paramsViewName);
			textViewName.setTextSize(14);
			textViewName.setText(spanName);
			textViewName.setGravity(Gravity.START);
			// textViewName.setCompoundDrawablesWithIntrinsicBounds(
			// R.drawable.contact, 0, 0, 0);
			rowName.addView(textViewName);

			// Create a new TableRow to hold TextView that has time.
			TableRow rowTime = new TableRow(this);
			rowTime.setLayoutParams(layoutParams);
			rowTime.setGravity(Gravity.END);

			String time = score.getStringTime();
			SpannableString spanTime = new SpannableString("  " + time);

			spanTime.setSpan(new StyleSpan(Typeface.ITALIC), 0,
					spanTime.length(), 0);

			// Create a new TextView to hold time.
			TextView textViewTime = new TextView(this);
			textViewTime.setLayoutParams(new LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT,
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
			LayoutParams paramsViewTime = (LayoutParams) textViewTime
					.getLayoutParams();
			paramsViewTime.height = 40;
			textViewTime.setLayoutParams(paramsViewTime);
			textViewTime.setTextSize(14);
			textViewTime.setText(spanTime);
			textViewTime.setGravity(Gravity.END);
			// textViewNumber.setCompoundDrawablesWithIntrinsicBounds(
			// R.drawable.call, 0, 0, 0);
			rowTime.addView(textViewTime);

			// Add the new created rows to the TableLayout.
			table.addView(rowName);
			table.addView(rowTime);

			// Add the TableLayout to the Global TableLayout at index - counter.
			tableLayout.addView(table, counter);
			counter++;
		}
	}
}
