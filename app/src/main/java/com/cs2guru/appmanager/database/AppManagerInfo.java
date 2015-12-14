package com.cs2guru.appmanager.database;

public class AppManagerInfo {

	// private variables
	int _id;
	String _name;
	String _pkg_name;
	int _count;
	String _date;
	byte[] _icon;

	// Empty constructor
	public AppManagerInfo() {

	}

	// constructor
	public AppManagerInfo(int id, String name, String pkg_name, int count,
			String date, byte[] icon) {
		this._id = id;
		this._name = name;
		this._pkg_name = pkg_name;
		this._count = count;
		this._date = date;
		this._icon = icon;
	}

	// constructor
	public AppManagerInfo(String name, String pkg_name, int count, String date,
			byte[] icon) {
		this._name = name;
		this._pkg_name = pkg_name;
		this._count = count;
		this._date = date;
		this._icon = icon;
	}

	public int getID() {
		return this._id;
	}

	public void setID(int id) {
		this._id = id;
	}

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public String getPackageName() {
		return this._pkg_name;
	}

	public void setPackageName(String pkg_name) {
		this._pkg_name = pkg_name;
	}

	public int getCount() {
		return this._count;
	}

	public void setCount(int count) {
		this._count = count;
	}

	public String getDate() {
		return this._date;
	}

	public void setDate(String date) {
		this._date = date;
	}

	public void setImage(byte[] icon) {
		this._icon = icon;
	}

	public byte[] getImage() {
		return this._icon;
	}
}
