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

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.barrelracing.data.Settings;
import com.barrelracing.graphics.Barrel;
import com.barrelracing.graphics.CountDownTimer;
import com.barrelracing.graphics.GameSetup;
import com.barrelracing.graphics.Rider;
import com.barrelracing.io.FileIO;

/**
 * @info : RacingPage class - This class is used to control all the activities
 *       in the game.
 */
public class RacingPage extends Activity {

	Handler redrawHandler = new Handler(); // so redraw occurs in main thread
	int screenWidth, screenHeight;
	PointF riderCoordinates, riderSpeed;
	PathDrawer pathDrawer;

	GameSetup gameSetup = null;
	Rider rider = null;

	// Barrel variables
	Barrel barrel1 = null;
	Barrel barrel2 = null;
	Barrel barrel3 = null;
	int[] barrel1Coordinates = new int[6];
	int[] barrel2Coordinates = new int[6];
	int[] barrel3Coordinates = new int[6];
	int[] barrel1Cross = new int[6];
	int[] barrel2Cross = new int[6];
	int[] barrel3Cross = new int[6];
	int quad1 = 0;
	int quad2 = 0;
	int quad3 = 0;
	int pquad1 = 0;
	int cnt = 0;

	// Count down timer.
	CountDownTimer countDown = null;
	Timer startTimer = null;
	Timer pauseTimer = null;
	TimerTask timerTask = null;
	int timerMin = 0;
	int timerSec = 0;
	int timerTen = 0;
	String timerTenDisplay = "00";
	String timerSecDisplay = "00";
	String timerMinDisplay = "00";
	String stringTime = "";

	// Custom Timers Task
	StartTimerTask startTimerTask;
	ResumeTimerTask resumeTimerTask;

	// Game variables.
	float x1, x2, x3, y1, y2, y3;
	int riderRadius = 50, barrelRadius = 50;
	int collide1 = 0, collide2 = 0, collide3 = 0;
	int flag = 0;
	int startFlag = 0, scoreFlag = 0, pauseFlag = 0, doneFlag = 0;
	int circleCount = 0;
	int circleFlag = 0;
	int[][] path;
	String scoreTime = null;
	int startCountDown = 4, resumeCountDown = 6;
	String countDownDisplayMessage = "";
	double angle = 90;
	float riderXAxis = 0;
	float riderYAxis = 0;

	FrameLayout mainView;
	CheckBox optSingleShot;
	Button buttonStart, buttonNewGame;
	TextView textCounter;
	TextView displayTimer;
	TextView countdownTimerHolderNOTSHOWN;

	@SuppressWarnings("deprecation")
	@SuppressLint({ "ResourceAsColor", "ClickableViewAccessibility" })
	public void onCreate(Bundle savedInstanceState) {

		// Hide the title bar.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
				.setFlags(
						0xFFFFFFFF,
						LayoutParams.FLAG_FULLSCREEN
								| LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_racing);

		// Create reference to main screen
		mainView = (android.widget.FrameLayout) findViewById(R.id.main_view);
		mainView.setBackgroundColor(Color.argb(255, 33, 33, 33));
		// mainView.setBackgroundColor(R.color.background);

		// Get screen dimensions.
		Display display = getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();

		// Create variables for rider position and speed
		riderCoordinates = new android.graphics.PointF();
		riderSpeed = new android.graphics.PointF();
		riderCoordinates.x = screenWidth / 5;
		riderCoordinates.y = screenHeight / 2 - 10;
		riderSpeed.x = 0;
		riderSpeed.y = 0;

		// Initialize coordinate set.
		for (int itr = 0; itr < 6; itr++) {

			barrel1Coordinates[itr] = 0;
			barrel2Coordinates[itr] = 0;
			barrel3Coordinates[itr] = 0;
			barrel1Cross[itr] = 0;
			barrel2Cross[itr] = 0;
			barrel3Cross[itr] = 0;
		}

		quad1 = 0;
		quad2 = 0;
		quad3 = 0;

		// Initialize the path
		path = new int[(int) screenWidth + 2][(int) screenHeight + 2];
		for (int itrW = 0; itrW < (int) screenWidth + 2; itrW++) {
			for (int itrH = 0; itrH < (int) screenHeight + 2; itrH++) {

				path[itrW][itrH] = 0;
			}
		}

		// ------------------------------------------------------------------ //
		// Game Setup.
		FileIO fileIO = new FileIO();
		Settings settings = fileIO.readFromSettingsFile();

		int backgroundColor = settings.getBackgroundColor();
		int barrelColor = settings.getBarrelColor();
		barrelRadius = settings.getBarrelRadius();

		// Create border with in which the game is played.
		gameSetup = new GameSetup(this, (float) screenWidth,
				(float) screenHeight, barrelColor, backgroundColor);
		mainView.addView(gameSetup);

		// Create count down timer view.
		countDown = new CountDownTimer(this, (float) screenWidth,
				(float) screenHeight);
		mainView.addView(countDown);

		// Create initial circle.
		rider = new Rider(this, riderCoordinates.x, riderCoordinates.y,
				riderRadius);
		mainView.addView(rider);
		rider.invalidate();

		// Create coordinates for barrel 1
		x1 = screenWidth / 3;
		y1 = screenHeight / 4;

		// Create barrel1 and add it to main screen
		barrel1 = new Barrel(this, x1, y1, barrelRadius, barrelColor);
		mainView.addView(barrel1);
		barrel1.invalidate();

		// Create coordinates for barrel 2
		x2 = screenWidth / 3;
		y2 = 3 * screenHeight / 4;

		// Create barrel2 and add it to main screen
		barrel2 = new Barrel(this, x2, y2, barrelRadius, barrelColor);
		mainView.addView(barrel2);
		barrel2.invalidate();

		// Create coordinates for barrel 3
		x3 = 2 * screenWidth / 3;
		y3 = screenHeight / 2;

		// Create barrel3 and add it to main screen
		barrel3 = new Barrel(this, x3, y3, barrelRadius, barrelColor);
		mainView.addView(barrel3);
		barrel3.invalidate();

		// ------------------------------------------------------------------ //

		buttonStart = (Button) findViewById(R.id.buttonStart);
		// Button click listener for start button.
		buttonStart.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				if (scoreFlag == 0) {

					buttonStart.setEnabled(false);
					// buttonNewGame.setEnabled(false);

					startFlag = 1;
					pauseFlag = 0;

					// Kill any old timer.
					if (startTimer != null) {

						startTimer.cancel();
						startTimer = null;
					}

					// Start a new timer.
					startTimer = new Timer();
					startTimerTask = new StartTimerTask();
					startTimer.schedule(startTimerTask, 1, 20);

				} else {
					restart();
				}

				// Listener to disable physical moment of rider by the user
				// using finger movements.
				OnClickListener riderOnClickListener = new OnClickListener() {

					public void onClick(View view) {

					}
				};
				gameSetup.setOnClickListener(riderOnClickListener);
			}
		});

		// buttonNewGame = (Button) findViewById(R.id.buttonNewGame);
		// Button click listener for new game button.
		// buttonNewGame.setOnClickListener(new OnClickListener() {

		// public void onClick(View view) {

		// startFlag = 0;

		// Kill any old timer.
		// if (startTimer != null) {

		// startTimer.cancel();
		// startTimer = null;
		// }

		// restart();
		// }
		// });

		displayTimer = (TextView) findViewById(R.id.timer);
		countdownTimerHolderNOTSHOWN = (TextView) findViewById(R.id.countdown);

		// Path drawer to trace the path taken by the user.
		pathDrawer = new PathDrawer(this);
		mainView.addView(pathDrawer);
		pathDrawer.invalidate();

		// Listener for accelerometer.
		((SensorManager) getSystemService(Context.SENSOR_SERVICE))
				.registerListener(new SensorEventListener() {

					@Override
					public void onSensorChanged(SensorEvent event) {

						// Get rider speed based on phone tilt.
						riderSpeed.x = -event.values[0];
						riderSpeed.y = event.values[1];
					}

					@Override
					public void onAccuracyChanged(Sensor sensor, int accuracy) {

						// Don't do anything - Ignore event.
					}
				}, ((SensorManager) getSystemService(Context.SENSOR_SERVICE))
						.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
						SensorManager.SENSOR_DELAY_NORMAL);

		// Listener for touch event.
		mainView.setOnTouchListener(new android.view.View.OnTouchListener() {

			public boolean onTouch(View view, android.view.MotionEvent event) {

				// Sets riders position based on screen touch
				riderCoordinates.x = event.getX();
				riderCoordinates.y = event.getY();

				return true;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		// menu.add("Abt"); //only one menu item
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.notelist_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * @function : gameWon() - function to reset the flags and go to gamewonpage
	 *           once the game on finished.
	 */
	private void gameWon() {

		// Reset the flags.
		startFlag = 0;
		pauseFlag = 0;
		doneFlag = 1;

		// Go to game won page.
		Intent gameWonIntent = new Intent(this, GameWonPage.class);
		gameWonIntent.putExtra("time", stringTime);
		startActivity(gameWonIntent);
	}

	/**
	 * @function : gameLost() - function to reset the flags and go to
	 *           gamelostpage if the rodeo knocked any of the barrels.
	 */
	private void gameLost() {

		// Reset the flags.
		startFlag = 0;
		pauseFlag = 0;
		doneFlag = 1;

		// Kill the start timer.
		if (startTimer != null) {

			startTimer.cancel();
			startTimer = null;
		}
		// restart();

		// Go to game won page.
		Intent gameOverIntent = new Intent(this, GameLostPage.class);
		startActivity(gameOverIntent);
	}

	/**
	 * @function : onPause() - (overridding) function to control actions when
	 *           the game is moved to background, stopping background threads.
	 */
	@Override
	public void onPause() {

		// Kill/Pause the timer.
		pauseTimer.cancel();
		pauseTimer = null;
		timerTask = null;
		pauseFlag = 1;
		resumeCountDown = 6;

		if (startTimer != null) {

			startTimer.cancel();
			startTimer = null;
		}

		super.onPause();
	}

	/**
	 * @function : onResume() - (overridding) function to control actions when
	 *           the game is started or when moved to foreground.
	 */
	@Override
	public void onResume() {

		// Called when app starts or when it comes to foreground from
		// background.
		// Create a new timer.
		timerTask = new TimerTask() {

			public void run() {

				//
				if (pauseFlag == 1 && doneFlag == 0) {

					// When game comes from background to foreground.
					timerTen = timerTen + 2;
					if (resumeCountDown < 1) {

						// Once countdown is done, run the top timer.
						if (resumeCountDown == 0) {
							countDownDisplayMessage = "";
						}

						pauseFlag = 0;
						resumeCountDown = 5;
						timerTen = timerTen + 5;

						// Kill/Pause the previous timer.
						if (startTimer != null) {

							startTimer.cancel();
							startTimer = null;
						}

						// Start a new timer that will run after the pause.
						startTimer = new Timer();
						startTimerTask = new StartTimerTask();
						resumeTimerTask = new ResumeTimerTask();
						startTimer.schedule(resumeTimerTask, 1, 20);

						runGame();
						// Toast.makeText(this, " Paused",
						// Toast.LENGTH_SHORT)
						// .show();

					} else {

						// Starts resume count down.
						if (timerTen >= 100) {

							resumeCountDown--;
							countDownDisplayMessage = "Resumes in  "
									+ String.format("%01d", resumeCountDown);
							timerTen = timerTen - 100;
						}
					}

					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							// Display the count down timer and message after
							// pause.
							countDown.message = countDownDisplayMessage;
							countDown.invalidate();
						}
					});

				} else {

					// When game starts.
					if (startFlag == 1 && startCountDown < 1) {

						// pauseFlag = 0;
						runGame();
					}
				}
			}
		};

		// Create a timer start the game.
		pauseTimer = new Timer();
		pauseTimer.schedule(timerTask, 1, 20);
		super.onResume();

	}

	/**
	 * @function : runGame() - function to stimulate the game.
	 */
	public void runGame() {

		// android.util.Log.d("TiltBall", "Timer Hit - " + riderCoordinates.x
		// + ":" + riderCoordinates.y);

		// Move rider based on current speed.
		if (riderCoordinates.x <= 9 * screenWidth / 10 - 5
				&& riderCoordinates.x >= screenWidth / 10 + 5) {

			riderCoordinates.x += riderSpeed.x * 2;

			if (Math.abs(riderXAxis - riderSpeed.x) > 0.1)
				riderXAxis = riderSpeed.x;
		}

		if (riderCoordinates.y <= 8 * screenHeight / 9 - 5
				&& riderCoordinates.y >= screenHeight / 12 + 5) {
			riderCoordinates.y += riderSpeed.y * 2;
			if (Math.abs(riderYAxis - riderSpeed.y) > 0.1)
				riderYAxis = riderSpeed.y;
		}

		if ((riderCoordinates.x >= 9 * screenWidth / 10 - 5))
			riderCoordinates.x--;

		if ((riderCoordinates.x <= screenWidth / 10 + 5))
			riderCoordinates.x++;

		if ((riderCoordinates.y >= 8 * screenHeight / 9 - 5))
			riderCoordinates.y--;

		if ((riderCoordinates.y <= screenHeight / 12 + 5))
			riderCoordinates.y++;

		if (riderXAxis != 0) {

			angle = Math.toDegrees(Math.atan((riderYAxis) / (riderXAxis)));

			if (riderXAxis < 0)
				angle = angle - 180;

		}
		rider.direction = (float) angle + 90;

		// Update the riders position.
		rider.xAxis = riderCoordinates.x;
		rider.yAxis = riderCoordinates.y;

		// Redraw rider and barrels in background thread to prevent
		// thread lock.
		redrawHandler.post(new Runnable() {

			public void run() {

				rider.invalidate();
				barrel1.invalidate();
				barrel2.invalidate();
				barrel3.invalidate();
			}
		});

		// Check for game constraints.
		checkBarrel1();
		checkBarrel2();
		checkBarrel3();
		checkCollision();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onDestroy() {

		// When main thread is stopped.
		super.onDestroy();

		// Wait for threads to exit before clearing game.
		System.runFinalizersOnExit(true);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	/**
	 * @function : checkCollision() - function to check whether or not the rider
	 *           has knocked any barrel.
	 */
	private void checkCollision() {

		float radiiDistanceSquared = (riderRadius + barrelRadius)
				* (riderRadius + barrelRadius);

		// Compute the distance between rider and barrel.
		float centersDistanceSquared1 = (riderCoordinates.x - x1)
				* (riderCoordinates.x - x1) + (riderCoordinates.y - y1)
				* (riderCoordinates.y - y1);

		float centersDistanceSquared2 = (riderCoordinates.x - x2)
				* (riderCoordinates.x - x2) + (riderCoordinates.y - y2)
				* (riderCoordinates.y - y2);

		float centersDistanceSquared3 = (riderCoordinates.x - x3)
				* (riderCoordinates.x - x3) + (riderCoordinates.y - y3)
				* (riderCoordinates.y - y3);

		// Check to determine did rider ran into collision with barrel1
		if (centersDistanceSquared1 <= (radiiDistanceSquared + 50)
				&& collide1 == 0) {

			collide1 = 1;
			barrel1.paintMasterSettings = 2;
			gameLost();
		}

		// Check to determine did rider ran into collision with barrel2
		if (centersDistanceSquared2 <= (radiiDistanceSquared + 50)
				&& collide2 == 0) {

			collide2 = 1;
			barrel2.paintMasterSettings = 2;
			gameLost();
		}

		// Check to determine did rider ran into collision with barrel3
		if (centersDistanceSquared3 <= (radiiDistanceSquared + 50)
				&& collide3 == 0) {

			collide3 = 1;
			barrel3.paintMasterSettings = 2;
			gameLost();

		}

		if (!((riderCoordinates.x >= screenWidth / 10 && riderCoordinates.x <= screenWidth / 10 + 20) && (riderCoordinates.y >= 4 * screenHeight / 10 && riderCoordinates.y <= 6 * screenHeight / 10))) {

			// Add 5 sec to timer if rider hits the boundary on x axis.
			if (((riderCoordinates.x >= 9 * screenWidth / 10 - 10) || (riderCoordinates.x <= screenWidth / 10 + 10))
					&& (flag == 0 || flag == 2)) {

				flag = 1;
				rider.paintMasterSettings = 1;
				timerSec = timerSec + 5;
			}

			// Add 5 sec to timer if rider hits the boundary on y axis.
			if (((riderCoordinates.y >= 8 * screenHeight / 9 - 10) || (riderCoordinates.y <= screenHeight / 12 + 10))
					&& (flag == 0 || flag == 2)) {

				flag = 1;
				rider.paintMasterSettings = 1;
				timerSec = timerSec + 5;
			}

			// Check for rider entering the gate once started.
			if (((riderCoordinates.y < 8 * screenHeight / 9 - 10) && (riderCoordinates.y > screenHeight / 12 + 10))
					&& ((riderCoordinates.x < 9 * screenWidth / 10 - 10) && (riderCoordinates.x > screenWidth / 10 + 10))
					&& (flag == 1 || flag == 2)) {

				rider.paintMasterSettings = 0;
				flag = 0;
			}

		} else if (flag != 2) {

			// If the rider completes the course without hitting the barrels.
			flag = 2;
			checkComplete();
		}

		// Keep a trace of the track taken by the user.
		if (path[(int) riderCoordinates.x][(int) riderCoordinates.y] == 0) {

			path[(int) riderCoordinates.x][(int) riderCoordinates.y] = 1;
		}
	}

	/**
	 * @function : checkComplete() - function to check whether or not the rider
	 *           has finished the course.
	 */
	public void checkComplete() {

		startFlag = 0;

		// Kill/Pause the previous timer.
		if (startTimer != null) {

			startTimer.cancel();
			startTimer = null;
		}

		if (barrel1.paintMasterSettings == 1
				&& barrel2.paintMasterSettings == 1
				&& barrel3.paintMasterSettings == 1) {

			// If rider circles all barrels without touching them.
			scoreFlag = 1;
			rider.paintMasterSettings = 3;
			gameWon();

		} else {

			// If rider hits one of the barrels.
			rider.paintMasterSettings = 2;
			gameLost();
		}
	}

	/**
	 * @function : restart() - function to restart the game.
	 */
	public void restart() {

		// Finish this activity.
		finish();

		// Create a new intent to itself.
		Intent racingIntent = new Intent(this, RacingPage.class);
		startActivity(racingIntent);
	}

	/**
	 * @function : checkBarrel1() - function to check whether or not the rider
	 *           knocks the barrel 1.
	 */
	public void checkBarrel1() {

		if (quad1 != 1 && riderCoordinates.y > y1 && riderCoordinates.x > x1) {

			if (barrel1Coordinates[5] == 4) {
				barrel1Cross[barrel1Coordinates[5]] = (int) riderCoordinates.x;
			}

			barrel1Coordinates[0] = barrel1Coordinates[1];
			barrel1Coordinates[1] = barrel1Coordinates[2];
			barrel1Coordinates[2] = barrel1Coordinates[3];
			barrel1Coordinates[3] = barrel1Coordinates[4];
			barrel1Coordinates[4] = barrel1Coordinates[5];
			barrel1Coordinates[5] = 1;
			quad1 = 1;
		}

		if (quad1 != 2 && riderCoordinates.y < y1 && riderCoordinates.x > x1) {

			barrel1Coordinates[0] = barrel1Coordinates[1];
			barrel1Coordinates[1] = barrel1Coordinates[2];
			barrel1Coordinates[2] = barrel1Coordinates[3];
			barrel1Coordinates[3] = barrel1Coordinates[4];
			barrel1Coordinates[4] = barrel1Coordinates[5];
			barrel1Coordinates[5] = 2;
			quad1 = 2;
		}

		if (quad1 != 3 && riderCoordinates.y < y1 && riderCoordinates.x < x1) {

			barrel1Coordinates[0] = barrel1Coordinates[1];
			barrel1Coordinates[1] = barrel1Coordinates[2];
			barrel1Coordinates[2] = barrel1Coordinates[3];
			barrel1Coordinates[3] = barrel1Coordinates[4];
			barrel1Coordinates[4] = barrel1Coordinates[5];
			barrel1Coordinates[5] = 3;
			quad1 = 3;
		}

		if (quad1 != 4 && riderCoordinates.y > y1 && riderCoordinates.x < x1) {

			barrel1Coordinates[0] = barrel1Coordinates[1];
			barrel1Coordinates[1] = barrel1Coordinates[2];
			barrel1Coordinates[2] = barrel1Coordinates[3];
			barrel1Coordinates[3] = barrel1Coordinates[4];
			barrel1Coordinates[4] = barrel1Coordinates[5];
			barrel1Coordinates[5] = 4;
			quad1 = 4;
		}

		circleCount = 0;
		for (int itr = 0; itr < 5; itr++) {

			if (barrel1Coordinates[itr + 1] == barrel1Coordinates[itr] + 1
					|| (barrel1Coordinates[itr] == 4 && barrel1Coordinates[itr + 1] == 1)) {

				circleCount++;
			}
		}

		if (circleCount > 4) {

			barrel1.paintMasterSettings = 1;
		}

		circleCount = 0;
		for (int itr = 0; itr < 5; itr++) {

			if (barrel1Coordinates[itr + 1] == barrel1Coordinates[itr] - 1
					|| (barrel1Coordinates[itr] == 1 && barrel1Coordinates[itr + 1] == 4)) {

				circleCount++;
			}
		}

		if (circleCount > 4) {

			barrel1.paintMasterSettings = 1;
		}
		circleCount = 0;
	}

	/**
	 * @function : checkBarrel2() - function to check whether or not the rider
	 *           knocks the barrel 2.
	 */
	public void checkBarrel2() {

		if (quad2 != 1 && riderCoordinates.y > y2 && riderCoordinates.x > x2) {

			barrel2Coordinates[0] = barrel2Coordinates[1];
			barrel2Coordinates[1] = barrel2Coordinates[2];
			barrel2Coordinates[2] = barrel2Coordinates[3];
			barrel2Coordinates[3] = barrel2Coordinates[4];
			barrel2Coordinates[4] = barrel2Coordinates[5];
			barrel2Coordinates[5] = 1;
			quad2 = 1;
		}

		if (quad2 != 2 && riderCoordinates.y < y2 && riderCoordinates.x > x2) {

			barrel2Coordinates[0] = barrel2Coordinates[1];
			barrel2Coordinates[1] = barrel2Coordinates[2];
			barrel2Coordinates[2] = barrel2Coordinates[3];
			barrel2Coordinates[3] = barrel2Coordinates[4];
			barrel2Coordinates[4] = barrel2Coordinates[5];
			barrel2Coordinates[5] = 2;
			quad2 = 2;
		}

		if (quad2 != 3 && riderCoordinates.y < y2 && riderCoordinates.x < x2) {

			barrel2Coordinates[0] = barrel2Coordinates[1];
			barrel2Coordinates[1] = barrel2Coordinates[2];
			barrel2Coordinates[2] = barrel2Coordinates[3];
			barrel2Coordinates[3] = barrel2Coordinates[4];
			barrel2Coordinates[4] = barrel2Coordinates[5];
			barrel2Coordinates[5] = 3;
			quad2 = 3;
		}

		if (quad2 != 4 && riderCoordinates.y > y2 && riderCoordinates.x < x2) {

			barrel2Coordinates[0] = barrel2Coordinates[1];
			barrel2Coordinates[1] = barrel2Coordinates[2];
			barrel2Coordinates[2] = barrel2Coordinates[3];
			barrel2Coordinates[3] = barrel2Coordinates[4];
			barrel2Coordinates[4] = barrel2Coordinates[5];
			barrel2Coordinates[5] = 4;
			quad2 = 4;
		}

		circleCount = 0;
		for (int itr = 0; itr < 5; itr++) {

			if (barrel2Coordinates[itr + 1] == barrel2Coordinates[itr] + 1
					|| (barrel2Coordinates[itr] == 4 && barrel2Coordinates[itr + 1] == 1)) {

				circleCount++;
			}
		}

		if (circleCount > 4) {
			barrel2.paintMasterSettings = 1;
		}

		circleCount = 0;
		for (int itr = 0; itr < 5; itr++) {

			if (barrel2Coordinates[itr + 1] == barrel2Coordinates[itr] - 1
					|| (barrel2Coordinates[itr] == 1 && barrel2Coordinates[itr + 1] == 4)) {

				circleCount++;
			}
		}

		if (circleCount > 4) {
			barrel2.paintMasterSettings = 1;
		}

		circleCount = 0;
	}

	/**
	 * @function : checkBarrel3() - function to check whether or not the rider
	 *           knocks the barrel 3.
	 */
	public void checkBarrel3() {

		if (quad3 != 1 && riderCoordinates.y > y3 && riderCoordinates.x > x3) {

			barrel3Coordinates[0] = barrel3Coordinates[1];
			barrel3Coordinates[1] = barrel3Coordinates[2];
			barrel3Coordinates[2] = barrel3Coordinates[3];
			barrel3Coordinates[3] = barrel3Coordinates[4];
			barrel3Coordinates[4] = barrel3Coordinates[5];
			barrel3Coordinates[5] = 1;
			quad3 = 1;
		}

		if (quad3 != 2 && riderCoordinates.y < y3 && riderCoordinates.x > x3) {

			barrel3Coordinates[0] = barrel3Coordinates[1];
			barrel3Coordinates[1] = barrel3Coordinates[2];
			barrel3Coordinates[2] = barrel3Coordinates[3];
			barrel3Coordinates[3] = barrel3Coordinates[4];
			barrel3Coordinates[4] = barrel3Coordinates[5];
			barrel3Coordinates[5] = 2;
			quad3 = 2;
		}

		if (quad3 != 3 && riderCoordinates.y < y3 && riderCoordinates.x < x3) {

			barrel3Coordinates[0] = barrel3Coordinates[1];
			barrel3Coordinates[1] = barrel3Coordinates[2];
			barrel3Coordinates[2] = barrel3Coordinates[3];
			barrel3Coordinates[3] = barrel3Coordinates[4];
			barrel3Coordinates[4] = barrel3Coordinates[5];
			barrel3Coordinates[5] = 3;
			quad3 = 3;
		}

		if (quad3 != 4 && riderCoordinates.y > y3 && riderCoordinates.x < x3) {

			barrel3Coordinates[0] = barrel3Coordinates[1];
			barrel3Coordinates[1] = barrel3Coordinates[2];
			barrel3Coordinates[2] = barrel3Coordinates[3];
			barrel3Coordinates[3] = barrel3Coordinates[4];
			barrel3Coordinates[4] = barrel3Coordinates[5];
			barrel3Coordinates[5] = 4;
			quad3 = 4;
		}

		circleCount = 0;
		for (int itr = 0; itr < 5; itr++) {

			if (barrel3Coordinates[itr + 1] == barrel3Coordinates[itr] + 1
					|| (barrel3Coordinates[itr] == 4 && barrel3Coordinates[itr + 1] == 1)) {

				circleCount++;
			}
		}

		if (circleCount > 4) {

			barrel3.paintMasterSettings = 1;
		}

		circleCount = 0;
		for (int itr = 0; itr < 5; itr++) {

			if (barrel3Coordinates[itr + 1] == barrel3Coordinates[itr] - 1
					|| (barrel3Coordinates[itr] == 1 && barrel3Coordinates[itr + 1] == 4)) {

				circleCount++;
			}
		}

		if (circleCount > 4) {
			barrel3.paintMasterSettings = 1;
		}

		circleCount = 0;
	}

	/**
	 * @info : PathDrawer class - This class is used to trace the path taken by
	 *       the rider to complete the course.
	 */
	class PathDrawer extends View {

		Canvas canvas;
		Bitmap pathTracerBitmap;

		Path pathTrace;
		Paint paint;
		Paint paintR;

		int pathX = 50;
		int pathY = 50;

		@SuppressLint("ResourceAsColor")
		public PathDrawer(Context context) {

			super(context);

			// Trace the path taken by the rider.
			paintR = new Paint(Paint.ANTI_ALIAS_FLAG);
			pathTracerBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
					Bitmap.Config.ARGB_8888);
			paintR.setColor(R.color.halo_dark);

			paint = new Paint(Paint.FILTER_BITMAP_FLAG);
			canvas = new Canvas(pathTracerBitmap);
			paint.setColor(R.color.halo_dark);
		}

		@Override
		protected void onDraw(Canvas canvas) {

			super.onDraw(canvas);

			// Displays only the path if the rider has completed the game.
			if (scoreFlag == 1) {

				for (int itrW = 0; itrW < screenWidth; itrW++) {

					for (int itrH = 0; itrH < screenHeight; itrH++) {

						if (path[itrW][itrH] == 1) {

							canvas.drawCircle(itrW, itrH, 2, paintR);
						}
					}
				}
			}
		}
	}

	/**
	 * @info : StartTimerTask class - This class is used to manage timer once
	 *       the game is started.
	 */
	class StartTimerTask extends TimerTask {

		public void run() {

			timerTen = timerTen + 2;
			if (startCountDown < 1) {

				// Once countdown is done, run the top timer.
				if (startCountDown == 0) {

					countDownDisplayMessage = "";
				}

				if (timerTen >= 100) {

					timerSec++;
					timerTen = timerTen - 100;
				}

				if (timerSec >= 60) {

					timerMin++;
					timerSec = timerSec - 60;
				}

				// Format the timer.
				timerSecDisplay = String.format("%02d", timerSec);
				timerMinDisplay = String.format("%02d", timerMin);
				timerTenDisplay = String.format("%02d", timerTen);

			} else {

				// Starts count down
				if (timerTen >= 100) {

					startCountDown--;
					countDownDisplayMessage = "  Starts  in  "
							+ String.format("%01d", startCountDown);
					timerTen = timerTen - 100;
				}
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					// Display the top timer.
					stringTime = timerMinDisplay + ":" + timerSecDisplay + ":"
							+ timerTenDisplay;
					displayTimer.setText(stringTime);

					// Display the count down timer.
					countDown.message = countDownDisplayMessage;
					countDown.invalidate();
				}
			});
		}
	}

	/**
	 * @info : ResumeTimerTask class - This class is used to manage timer once
	 *       the game is resumed [from background to foreground].
	 */
	class ResumeTimerTask extends TimerTask {

		public void run() {

			timerTen = timerTen + 2;
			if (startCountDown < 1) {

				// Once count down is done, run the top timer.
				if (startCountDown == 0) {

					countDownDisplayMessage = "";
				}

				if (timerTen >= 100) {

					timerSec++;
					timerTen = timerTen - 100;
				}

				if (timerSec >= 60) {

					timerMin++;
					timerSec = timerSec - 60;
				}

				// Format the timer.
				timerSecDisplay = String.format("%02d", timerSec);
				timerMinDisplay = String.format("%02d", timerMin);
				timerTenDisplay = String.format("%02d", timerTen);
			}

			runOnUiThread(new Runnable() {

				@Override
				public void run() {

					// Display the top timer.
					stringTime = timerMinDisplay + ":" + timerSecDisplay + ":"
							+ timerTenDisplay;
					displayTimer.setText(stringTime);

					// Display the count down timer.
					countDown.message = countDownDisplayMessage;
					countDown.invalidate();
				}
			});
		}
	}
}
