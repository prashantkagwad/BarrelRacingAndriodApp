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
import android.graphics.Paint;
import android.view.View;

/**
 * @info : GameSetup class [User Interface Layer] - This class is used to manage
 *       the over all appearance/background of the game.
 */
public class GameSetup extends View {

	private float width;
	private float height;
	private Paint border = new Paint();
	private Paint background = new Paint();

	// Constructor to initialize rider variables.
	public GameSetup(Context context, Float screenWidth, Float screenHeight,
			int borderColor, int backgroundColor) {

		super(context);

		this.width = screenWidth;
		this.height = screenHeight;

		// Color settings for different areas.
		border.setColor(borderColor);
		background.setColor(backgroundColor);
	}

	public void onDraw(Canvas canvas) {

		// Create the border/fence for the game.
		// yellow-green ~ 14329120 or (218, 165, 32)
		// border.setColor(Color.rgb(218, 165, 32));
		border.setStrokeWidth(5);
		canvas.drawRect(width / 20, height / 12, 19 * width / 20,
				8 * height / 9, border);

		// Create the inside ground for the game.
		// slate + green ~ 9546660 or (145, 171, 164)
		// background.setColor(Color.rgb(145, 171, 164));
		background.setStrokeWidth(0);

		canvas.drawRect(width / 20 + 10, height / 12 + 10,
				19 * width / 20 - 10, 8 * height / 9 - 10, background);

		canvas.drawRect(width / 20, 4 * height / 10, width / 20 + 10,
				6 * height / 10, background);
	}
}
