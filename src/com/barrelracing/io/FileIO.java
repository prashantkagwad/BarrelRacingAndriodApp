/**
 * @Author - Prashant Kagwad
 * @Date - 11/21/2014
 * @Project Description : This program is written as a part of UI
 * Design Assignment to develop a  barrel race game. It is a rodeo
 * event in which the rider starts at a gate and must ride completely
 * around three barrels.  The objective is to get the fastest time
 * without knocking over any of the barrels.
 */
package com.barrelracing.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.StringTokenizer;
import com.barrelracing.data.Score;
import com.barrelracing.data.Settings;
import android.annotation.SuppressLint;
import android.os.Environment;

/**
 * @info : FileIO class [Tech. Layer] - helps in reading/write records from/to
 *       the text files.
 */
@SuppressLint({ "NewApi", "DefaultLocale" })
public class FileIO {

	public FileIO() {

		super();
	}

	/**
	 * @function : readFromFile() - function to read all the scorers from
	 *           scorers.txt file.
	 * @return : ArrayList<Scores> - array list of scorers objects read from the
	 *         file.
	 */
	public ArrayList<Score> readFromFile() {

		ArrayList<Score> scoreList = new ArrayList<Score>();
		BufferedReader bufferedReader = null;
		String delimiter = "||";

		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			File myFile = new File(path + "/scores.txt");

			// Create a input stream to read the data in.
			FileInputStream inputStream = new FileInputStream(myFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));

			// For each line read the store the contact info in a contact
			// object.
			String singleLine = "";
			while ((singleLine = bufferedReader.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(singleLine, delimiter);

				if (st.countTokens() == 3) {

					Score score = new Score();
					score.setName(checkString(st.nextToken()));
					score.setStringTime(checkString(st.nextToken()));
					score.setIntTime(Integer.parseInt(st.nextToken()));

					// Add the new object to the array list.
					scoreList.add(score);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
		return scoreList;
	}

	/**
	 * @function : checkString() - filter function which either returns the
	 *           original value or "" if NULL stored for that value in the text
	 *           file.
	 * @param : token - string to check for NULL/empty values.
	 * @return : string - "" if NULL else the original value.
	 */
	public String checkString(String token) {

		String checkText = "";

		if (!token.equalsIgnoreCase("NULL"))
			checkText = token;

		return checkText;
	}

	/**
	 * @function : writeToFile(ArrayList<Scores>) - function to write all the
	 *           contact records to scorers.txt file.
	 * @param : scoreList - array list of top 10 scorers.
	 */
	public void writeToFile(ArrayList<Score> scoreList) {

		OutputStreamWriter writer = null;
		File file = null;
		FileOutputStream fileOutputStream = null;
		String delimiter = "||";
		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			file = new File(path + "/scores.txt");

			// If file doesn't exists, then create it
			if (!file.exists()) {
				// file.mkdirs();
				file.createNewFile();
			}

			// Return if the scorers list is empty
			if (scoreList.isEmpty() || scoreList.size() <= 0
					|| scoreList.size() > 10) {
				return;
			}

			fileOutputStream = new FileOutputStream(file);
			writer = new OutputStreamWriter(fileOutputStream);

			// Sort the input array list before writing the file.
			Collections.sort(scoreList, new Comparator<Score>() {
				// Compared only on basics of time stored as integer

				// @Override
				public int compare(Score v1, Score v2) {

					return v1.getIntTime() - v2.getIntTime();
				}
			});

			// For score object create a line and store the object in text
			// file.
			Iterator<Score> iterator = scoreList.iterator();
			while (iterator.hasNext()) {

				String tempScorer = "";
				Score contact = iterator.next();

				tempScorer = tempScorer + contact.getName() + delimiter;
				tempScorer = tempScorer + contact.getStringTime() + delimiter;
				tempScorer = tempScorer + contact.getIntTime() + "\n";

				writer.write(tempScorer);
			}

			writer.close();
			fileOutputStream.close();

		} catch (Exception e) {

			// TODO Auto-generated method stub
			e.printStackTrace();

		} finally {
			try {
				writer.close();
				fileOutputStream.close();

			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
	}

	/**
	 * @function : printData(ArrayList<Scores>) - function to print details of
	 *           all the records read from scores.txt file.
	 * @param : contacts - array list of contacts.
	 */
	public void printData(ArrayList<Score> scoreList) {

		System.out.println("Printing records...");
		Iterator<Score> iterator = scoreList.iterator();

		int counter = 0;
		while (iterator.hasNext()) {

			counter++;
			System.out.println("User " + counter + " Information : ");

			Score scorer = iterator.next();
			System.out.println("Name      : " + scorer.getName());
			System.out.println("Time      : " + scorer.getStringTime());
			System.out.println();
		}
	}

	/**
	 * @function : readFromSettingsFile() - function to read the application
	 *           settings from settings.txt
	 * @return : settings - object that contains application settings
	 */
	public Settings readFromSettingsFile() {

		Settings settings = new Settings();
		BufferedReader bufferedReader = null;
		String delimiter = "||";

		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			File myFile = new File(path + "/settings.txt");

			// Create a input stream to read the data in.
			FileInputStream inputStream = new FileInputStream(myFile);
			bufferedReader = new BufferedReader(new InputStreamReader(
					inputStream));

			// For each line read the store the contact info in a Settings
			// object.

			String singleLine = "";
			while ((singleLine = bufferedReader.readLine()) != null) {

				StringTokenizer st = new StringTokenizer(singleLine, delimiter);

				if (st.countTokens() == 3) {

					settings.setBackgroundColor(Integer.parseInt(st.nextToken()));
					settings.setBarrelColor(Integer.parseInt(st.nextToken()));
					settings.setBarrelRadius(Integer.parseInt(st.nextToken()));

				}
			}

		} catch (Exception e) {
			// TODO Auto-generated method stub
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
		return settings;
	}

	/**
	 * @function : writeToSettingsFile(settings) - function to write any changed
	 *           settings back to settings.txt file.
	 * @param : settings - an object that contains all the application settings.
	 */
	public void writeToSettingsFile(Settings settings) {

		OutputStreamWriter writer = null;
		File file = null;
		FileOutputStream fileOutputStream = null;
		String delimiter = "||";
		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			file = new File(path + "/settings.txt");

			// If file doesn't exists, then create it
			if (!file.exists()) {
				// file.mkdirs();
				file.createNewFile();
			}

			fileOutputStream = new FileOutputStream(file);
			writer = new OutputStreamWriter(fileOutputStream);

			String tempSettings = "";

			tempSettings = tempSettings + settings.getBackgroundColor()
					+ delimiter;
			tempSettings = tempSettings + settings.getBarrelColor() + delimiter;
			tempSettings = tempSettings + settings.getBarrelRadius() + "\n";

			writer.write(tempSettings);

			writer.close();
			fileOutputStream.close();

		} catch (Exception e) {

			// TODO Auto-generated method stub
			e.printStackTrace();

		} finally {
			try {
				writer.close();
				fileOutputStream.close();

			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
	}

	/**
	 * @function : writeDefaultToSettingsFile() - function to write any changed
	 *           settings back to default settings in the settings.txt file.
	 */
	public void writeDefaultToSettingsFile() {

		OutputStreamWriter writer = null;
		File file = null;
		FileOutputStream fileOutputStream = null;
		String delimiter = "||";
		try {

			// Get the path for SD card.
			String path = Environment.getExternalStorageDirectory().getPath();
			// String path = "/sdcard";
			file = new File(path + "/settings.txt");

			// If file doesn't exists, then create it
			if (!file.exists()) {
				// file.mkdirs();
				file.createNewFile();
			}

			fileOutputStream = new FileOutputStream(file);
			writer = new OutputStreamWriter(fileOutputStream);

			String tempSettings = "";

			tempSettings = tempSettings + "9546660" + delimiter; // background
																	// color
			tempSettings = tempSettings + "14329120" + delimiter; // barrel
																	// color
			tempSettings = tempSettings + "50" + "\n"; // barrel radius - medium
														// ~ 50

			writer.write(tempSettings);

			writer.close();
			fileOutputStream.close();

		} catch (Exception e) {

			// TODO Auto-generated method stub
			e.printStackTrace();

		} finally {
			try {
				writer.close();
				fileOutputStream.close();

			} catch (Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}
	}
}
