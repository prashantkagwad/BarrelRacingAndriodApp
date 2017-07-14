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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.barrelracing.ColorPickerDialog.OnColorChangedListener;
import com.barrelracing.data.Settings;
import com.barrelracing.io.FileIO;

/**
 * @info : SettingsPage class - This class is used to display some features that
 *       the user can change about the game.
 */
public class SettingsPage extends ActionBarActivity {

	int backgroundColor = 0;
	int barrelColor = 0;
	int barrelRadius = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// Read the settings from the settings.txt file.
		final FileIO fileIO = new FileIO();
		final Settings settings = fileIO.readFromSettingsFile();
		backgroundColor = settings.getBackgroundColor();
		barrelColor = settings.getBarrelColor();
		barrelRadius = settings.getBarrelRadius();

		// Set the colors to the fields as read from settings file.
		final Button buttonBackgroundColor = (Button) findViewById(R.id.buttonBackgroundColorPicker);
		buttonBackgroundColor.setBackgroundColor(settings.getBackgroundColor());

		// Button click listener for back ground button.
		buttonBackgroundColor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Listener to fetch the color from the newly open color
				// selector dialog.
				Paint paint = new Paint();
				OnColorChangedListener listener = new OnColorChangedListener() {

					@Override
					public void colorChanged(int color) {

						// Toast.makeText(getApplicationContext(),
						// "Color : " + color, Toast.LENGTH_LONG).show();

						backgroundColor = color;
						// Change the color of the button.
						buttonBackgroundColor.setBackgroundColor(color);
					}
				};

				// Open a new dialog from where the user can select the color.
				new ColorPickerDialog(SettingsPage.this, listener, paint
						.getColor()).show();
			}
		});

		final Button buttonBarrelColor = (Button) findViewById(R.id.buttonBarrelColorPicker);
		buttonBarrelColor.setBackgroundColor(settings.getBarrelColor());
		// Button click listener for back ground button.
		buttonBarrelColor.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// Listener to fetch the color from the newly open color
				// selector dialog.
				Paint paint = new Paint();
				OnColorChangedListener listener = new OnColorChangedListener() {

					@Override
					public void colorChanged(int color) {

						// Toast.makeText(getApplicationContext(),
						// "Color : " + color, Toast.LENGTH_LONG).show();

						// Change the color of the button.
						barrelColor = color;
						buttonBarrelColor.setBackgroundColor(color);
					}
				};

				// Open a new dialog from where the user can select the color.
				new ColorPickerDialog(SettingsPage.this, listener, paint
						.getColor()).show();
			}
		});

		// Set the barrel size saved from the settings.txt file.
		final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioBarrelSize);
		if (barrelRadius == 30) {
			RadioButton radioButton = (RadioButton) findViewById(R.id.radioSmall);
			radioButton.setChecked(true);
		} else if (barrelRadius == 50) {
			RadioButton radioButton = (RadioButton) findViewById(R.id.radioMedium);
			radioButton.setChecked(true);
		} else if (barrelRadius == 70) {
			RadioButton radioButton = (RadioButton) findViewById(R.id.radioLarge);
			radioButton.setChecked(true);
		}

		// New intent for Main Page.
		final Intent mainIntent = new Intent(this, MainActivity.class);

		Button buttonDefaults = (Button) findViewById(R.id.buttonDefaults);
		// Button click listener for default button.
		buttonDefaults.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click

				// Set the default settings.
				backgroundColor = (((145 & 0x0ff) << 16) | ((171 & 0x0ff) << 8) | (164 & 0x0ff)) - 16777215;
				barrelColor = (((218 & 0x0ff) << 16) | ((165 & 0x0ff) << 8) | (32 & 0x0ff)) - 16777215;
				barrelRadius = 50;

				// Changes the button color and options to reflect the default
				// changes.
				buttonBackgroundColor.setBackgroundColor(backgroundColor);
				buttonBarrelColor.setBackgroundColor(barrelColor);
				RadioButton radioButton = (RadioButton) findViewById(R.id.radioMedium);
				radioButton.setChecked(true);

				settings.setBackgroundColor(backgroundColor);
				settings.setBarrelColor(barrelColor);
				settings.setBarrelRadius(barrelRadius);

				// Write the default changes to settings.txt file.
				fileIO.writeToSettingsFile(settings);

				Toast.makeText(getApplicationContext(),
						"Settings are changed to default.", Toast.LENGTH_SHORT)
						.show();

				// fileIO.writeDefaultToSettingsFile();
				// startActivity(mainIntent);
			}
		});

		Button buttonDone = (Button) findViewById(R.id.buttonDone);
		// Button click listener for done button.
		buttonDone.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				// Perform action on click

				settings.setBackgroundColor(backgroundColor);
				settings.setBarrelColor(barrelColor);

				int barrelID = radioGroup.getCheckedRadioButtonId();

				RadioButton radioButton = (RadioButton) findViewById(barrelID);
				String size = radioButton.getText().toString();
				if (size.equalsIgnoreCase("Small")) {
					barrelRadius = 30;
				} else if (size.equalsIgnoreCase("Medium")) {
					barrelRadius = 50;
				} else if (size.equalsIgnoreCase("Large")) {
					barrelRadius = 70;
				}
				settings.setBarrelRadius(barrelRadius);

				// Write the new changes to settings.txt file.
				fileIO.writeToSettingsFile(settings);
				startActivity(mainIntent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
}

/**
 * @info : ColorPickerDialog class - This class is used to display a custom
 *       dialog to user in order to pick colors for features.
 */
@SuppressLint("DrawAllocation")
class ColorPickerDialog extends Dialog {

	public interface OnColorChangedListener {

		void colorChanged(int color);
	}

	private OnColorChangedListener colorChangedListener;
	private int initialColorSelected;

	private static class ColorPickerView extends View {

		private Paint paint;
		private Paint centrePaint;
		private final int[] colorSet;
		private OnColorChangedListener onColorChangedListener;

		ColorPickerView(Context context, OnColorChangedListener listener,
				int color) {

			super(context);

			onColorChangedListener = listener;
			colorSet = new int[] { 0xFFFF0000, 0xFFFF00FF, 0xFF0000FF,
					0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000 };
			Shader shader = new SweepGradient(0, 0, colorSet, null);

			// Creating a outer circle for color chooser.
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setShader(shader);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(64);

			// Create a inner circle to show the selected color.
			centrePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			centrePaint.setColor(color);
			centrePaint.setStrokeWidth(5);
		}

		private boolean trackingCenter;
		private boolean highlightCenter;

		protected void onDraw(Canvas canvas) {

			// Compute radius.
			float rad = CENTER_X - paint.getStrokeWidth() * 0.5f;

			canvas.translate(CENTER_X, CENTER_X);
			canvas.drawOval(new RectF(-rad, -rad, rad, rad), paint);
			canvas.drawCircle(0, 0, CENTER_RADIUS, centrePaint);

			// Tracking the color selected on the outer circle.
			if (trackingCenter) {

				int cent = centrePaint.getColor();
				centrePaint.setStyle(Paint.Style.STROKE);

				if (highlightCenter) {

					centrePaint.setAlpha(0xFF);
				} else {

					centrePaint.setAlpha(0x80);
				}

				canvas.drawCircle(0, 0,
						CENTER_RADIUS + centrePaint.getStrokeWidth(),
						centrePaint);
				centrePaint.setStyle(Paint.Style.FILL);
				centrePaint.setColor(cent);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			setMeasuredDimension(CENTER_X * 2, CENTER_Y * 2);
		}

		// Custom Dimensions for the dialog.
		private static final int CENTER_X = 250;
		private static final int CENTER_Y = 250;
		private static final int CENTER_RADIUS = 64;

		private int floatToByte(float floatNumber) {

			int intNumber = java.lang.Math.round(floatNumber);
			return intNumber;
		}

		// Converting pin to byte.
		private int pinToByte(int pin) {

			if (pin < 0) {

				pin = 0;

			} else if (pin > 255) {

				pin = 255;
			}
			return pin;
		}

		// Compute superficial average
		private int average(int sx, int dx, float px) {

			return sx + java.lang.Math.round(px * (dx - sx));
		}

		// Get the actual color [integer -> argb]
		private int interpColor(int colors[], float unit) {

			if (unit <= 0) {
				return colors[0];
			}

			if (unit >= 1) {
				return colors[colors.length - 1];
			}

			float point = unit * (colors.length - 1);
			int intPoint = (int) point;
			point -= intPoint;

			int colorFact0 = colors[intPoint];
			int colorFact1 = colors[intPoint + 1];

			int alpha = average(Color.alpha(colorFact0),
					Color.alpha(colorFact1), point);
			int red = average(Color.red(colorFact0), Color.red(colorFact1),
					point);
			int green = average(Color.green(colorFact0),
					Color.green(colorFact1), point);
			int blue = average(Color.blue(colorFact0), Color.blue(colorFact1),
					point);

			return Color.argb(alpha, red, green, blue);
		}

		@SuppressWarnings("unused")
		private int rotateColor(int color, float rad) {

			float deg = rad * 180 / 3.1415927f;
			int red = Color.red(color);
			int green = Color.green(color);
			int blue = Color.blue(color);

			ColorMatrix colorMatrix = new ColorMatrix();
			ColorMatrix tempMatrix = new ColorMatrix();

			colorMatrix.setRGB2YUV();
			tempMatrix.setRotate(0, deg);
			colorMatrix.postConcat(tempMatrix);
			tempMatrix.setYUV2RGB();
			colorMatrix.postConcat(tempMatrix);

			final float[] array = colorMatrix.getArray();

			int intRed = floatToByte(array[0] * red + array[1] * green
					+ array[2] * blue);
			int intGreen = floatToByte(array[5] * red + array[6] * green
					+ array[7] * blue);
			int intBlue = floatToByte(array[10] * red + array[11] * green
					+ array[12] * blue);

			return Color.argb(Color.alpha(color), pinToByte(intRed),
					pinToByte(intGreen), pinToByte(intBlue));
		}

		private static final float PI = 3.1415926f;

		@Override
		public boolean onTouchEvent(MotionEvent event) {

			float xAxis = event.getX() - CENTER_X;
			float yAxis = event.getY() - CENTER_Y;
			boolean inCenter = java.lang.Math.sqrt(xAxis * xAxis + yAxis
					* yAxis) <= CENTER_RADIUS;

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:
				trackingCenter = inCenter;
				if (inCenter) {
					highlightCenter = true;
					invalidate();
					break;
				}
			case MotionEvent.ACTION_MOVE:
				if (trackingCenter) {

					if (highlightCenter != inCenter) {

						highlightCenter = inCenter;
						invalidate();
					}
				} else {

					float angle = (float) java.lang.Math.atan2(yAxis, xAxis);
					// need to turn angle [-PI ... PI] into unit [0....1]
					float unit = angle / (2 * PI);

					if (unit < 0) {
						unit += 1;
					}

					centrePaint.setColor(interpColor(colorSet, unit));
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:

				if (trackingCenter) {
					if (inCenter) {
						onColorChangedListener.colorChanged(centrePaint
								.getColor());
					}
					trackingCenter = false; // so we draw w/o halo
					invalidate();
				}
				break;
			}
			return true;
		}
	}

	public ColorPickerDialog(Context context, OnColorChangedListener listener,
			int initialColor) {
		super(context);

		colorChangedListener = listener;
		initialColorSelected = initialColor;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		OnColorChangedListener listerner = new OnColorChangedListener() {

			public void colorChanged(int color) {

				colorChangedListener.colorChanged(color);
				dismiss();
			}
		};

		setContentView(new ColorPickerView(getContext(), listerner,
				initialColorSelected));
		setTitle("  Choose Color ");
	}
}
