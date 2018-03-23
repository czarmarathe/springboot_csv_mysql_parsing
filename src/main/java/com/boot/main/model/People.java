package com.boot.main.model;

import java.util.Arrays;

// Class instance stores record information from the reader
public class People {
	
	public String name;
	public String image;
	public byte[] blob_image;
	public String b1;
	public boolean b1_out;
	public String location;
	
	static public int data_received = 0;
	static public int data_successful = 0;
	static public int data_failed = 0;
	
	
	public People() {
		
	}
	
	public People(People s) {
		
		name 	= s.name;
		image 		= s.image;
		blob_image	= s.blob_image;
		b1 			= s.b1;
		location 	= s.location;
	}
	

	public People(String name, String image, byte[] blob_image, String b1, boolean b1_out, String location) {
		super();
		this.name = name;
		this.image = image;
		this.blob_image = blob_image;
		this.b1 = b1;
		this.b1_out = b1_out;
		this.location = location;
	}

	
	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public byte[] getBlob_image() {
		return blob_image;
	}

	public void setBlob_image(byte[] blob_image) {
		this.blob_image = blob_image;
	}

	public String getB1() {
		return b1;
	}

	public void setB1(String b1) {
		this.b1 = b1;
	}

	public boolean isB1_out() {
		return b1_out;
	}

	public void setB1_out(boolean b1_out) {
		this.b1_out = b1_out;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	
	@Override
	public String toString() {
		return "People [name=" + name + ", image=" + image + ", blob_image=" + Arrays.toString(blob_image) + ", b1="
				+ b1 + ", b1_out=" + b1_out + ", location=" + location + "]";
	}

	// Check for incompleteness . Can be modified further based on usage
	public boolean recordIncomplete() {
		if (name.equals("") || image.equals("") || 	b1.equals("")	||	 location.equals("") ) {
			return true;
		}
		else {
			return false;
		}
	}


}
