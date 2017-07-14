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
 * @info : Score class - helps in storing information about top scorers like
 *       name and time.
 */
public class Score {

	private String name;
	private String stringTime;
	private int intTime;

	public Score() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStringTime() {
		return stringTime;
	}

	public void setStringTime(String stringTime) {
		this.stringTime = stringTime;
	}

	public int getIntTime() {
		return intTime;
	}

	public void setIntTime(int intTime) {
		this.intTime = intTime;
	}

}
