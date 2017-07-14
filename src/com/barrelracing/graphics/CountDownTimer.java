/**
 * @Author - Prashant Kagwad
 * @Date - 11/21/2014
 * @Project Description : This program is written as a part of UI
 * Design Assignment to develop a  barrel race game. It is a rodeo
 * event in which the rider starts at a gate and must ride completely
 * around three barrels.  The objective is to get the fastest time
 * without knocking over any of the barrels.
 */
package com.barrelracing.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * @info : CountDownTimer class [User Interface Layer] - This class is used to
 *       to create a placeholder for a countdown timer at the center of the
 *       screen.
 */
public class CountDownTimer extends View {

	public float xAxis;
	public float yAxis;
	public String message = "";
	private Paint paint = new Paint();

	// Constructor to initialize rider variables.
	public CountDownTimer(Context context, Float width, Float height) {

		super(context);
		this.xAxis = width;
		this.yAxis = height;
	}

	public void onDraw(Canvas canvas) {

		// Display the count down timer at the center of the screen.
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(5);
		paint.setTextSize(100);
		canvas.drawText(message, xAxis / 2 - 300, yAxis / 2 - 150, paint);
	}
}
