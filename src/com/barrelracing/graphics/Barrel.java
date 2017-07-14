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
 * @info : Barrel class [User Interface Layer] - This class is used to manage
 *       the barrel positions and appearance in the game.
 */
public class Barrel extends View {

	public float xAxis;
	public float yAxis;
	public int radius;

	public int paintMasterSettings = 0;
	private final Paint barrelStart = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint barrelCleared = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint barrelBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint barrelHit = new Paint(Paint.ANTI_ALIAS_FLAG);

	// Constructor to initialize barrel variables.
	public Barrel(Context context, float xAxis, float yAxis, int radius,
			int barrelColor) {

		super(context);

		// Color settings for different events.
		barrelBorder.setColor(Color.rgb(33, 33, 33)); // black
		// yellow-green ~ 14329120 or (218, 165, 32)
		// barrelStart.setColor(Color.rgb(218, 165, 32));
		barrelStart.setColor(barrelColor);
		barrelCleared.setColor(Color.rgb(164, 198, 57)); // green
		barrelHit.setColor(Color.rgb(242, 79, 34)); // red

		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.radius = radius;
	}

	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// Draw the barrel in the position and color specified.
		canvas.drawCircle(xAxis, yAxis, radius + 5, barrelBorder);

		if (paintMasterSettings == 0) {

			// Initial Settings, when app starts.
			canvas.drawCircle(xAxis, yAxis, radius, barrelStart);

		} else if (paintMasterSettings == 1) {

			// If the riders circles the barrel, without hitting it.
			canvas.drawCircle(xAxis, yAxis, radius, barrelCleared);

		} else {

			// If the riders hits the barrel.
			canvas.drawCircle(xAxis, yAxis, radius, barrelHit);
		}
	}
}
