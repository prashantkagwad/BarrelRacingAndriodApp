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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import com.barrelracing.R;

/**
 * @info : Rider class [User Interface Layer] - This class is used to manage the
 *       rider positions and appearance in the game.
 */
public class Rider extends View {

	public int paintMasterSettings = 0;
	public float xAxis;
	public float yAxis;
	public float direction = 90;

	private final int riderRadius;
	private final Paint circleHit = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint circleStart = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint circleFinishComplete = new Paint(Paint.ANTI_ALIAS_FLAG);
	private final Paint circleFinishIncomplete = new Paint(
			Paint.ANTI_ALIAS_FLAG);
	private Paint bitmapCircle;

	// Constructor to initialize rider variables.
	@SuppressLint("ResourceAsColor")
	public Rider(Context context, float xAxis, float yAxis, int radius) {

		super(context);

		// Color settings for different events.
		circleHit.setColor(Color.rgb(242, 79, 34)); // red
		circleStart.setColor(R.color.halo_dark); // start
		circleFinishComplete.setColor(Color.rgb(164, 198, 57)); // green
		circleFinishIncomplete.setColor(Color.rgb(242, 79, 34)); // red

		this.xAxis = xAxis;
		this.yAxis = yAxis;
		this.riderRadius = radius;
	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		// Create a circle to show the space around the rider.
		bitmapCircle = new Paint();
		Bitmap bitmapRider = BitmapFactory.decodeResource(getResources(),
				R.drawable.horse);
		bitmapCircle.setColor(Color.RED);

		Matrix matrix = new Matrix();
		matrix.postRotate((float) direction);

		Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapRider, 0, 0,
				bitmapRider.getWidth(), bitmapRider.getHeight(), matrix, true);

		if (paintMasterSettings == 0) {

			// Initial Settings, when app starts.
			canvas.drawCircle(xAxis, yAxis, riderRadius, circleStart);

		} else if (paintMasterSettings == 1) {

			// If the riders hits to the walls.
			canvas.drawCircle(xAxis, yAxis, riderRadius, circleHit);

		} else if (paintMasterSettings == 2) {

			// If rider enters the gate without completing circling all the 3
			// barrels.
			canvas.drawCircle(xAxis, yAxis, riderRadius, circleFinishIncomplete);

		} else if (paintMasterSettings == 3) {

			// If rider enters the gate after completing circling all the 3
			// barrels.
			canvas.drawCircle(xAxis, yAxis, riderRadius, circleFinishComplete);
		}

		// Draw the circle around the rider
		canvas.drawBitmap(
				rotatedBitmap,
				xAxis
						- (float) (Math.abs(Math.sin(direction + 90)) + Math
								.abs(Math.cos(direction + 90)))
						* bitmapRider.getWidth() / (2),
				yAxis
						- (float) (Math.abs(Math.sin(direction - 90)) + Math
								.abs(Math.cos(direction - 90)))
						* bitmapRider.getHeight() / (2), bitmapCircle);
	}
}
