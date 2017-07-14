/**
 * @Author - Prashant Kagwad
 * @Date - 11/21/2014
 * @Project Description : This program is written as a part of UI
 * Design Assignment to develop a  barrel race game. It is a rodeo
 * event in which the rider starts at a gate and must ride completely
 * around three barrels.  The objective is to get the fastest time
 * without knocking over any of the barrels.
 */

package com.barrelracing.data;

/**
 * @info : Settings class - helps in storing information about application
 *       settings like backgroundColor, barrelColor, barrelRadius.
 */
public class Settings {

	private int backgroundColor;
	private int barrelColor;
	private int barrelRadius;

	public Settings() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getBarrelColor() {
		return barrelColor;
	}

	public void setBarrelColor(int barrelColor) {
		this.barrelColor = barrelColor;
	}

	public int getBarrelRadius() {
		return barrelRadius;
	}

	public void setBarrelRadius(int barrelRadius) {
		this.barrelRadius = barrelRadius;
	}

}
