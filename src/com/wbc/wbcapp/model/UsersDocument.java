package com.wbc.wbcapp.model;

public class UsersDocument {

	String file;
	String date;
	String size;
	String extension;
	
	public UsersDocument(String file, String date, String size, String extension) {
		super();
		this.file = file;
		this.date = date;
		this.size = size;
		this.extension = extension;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
	
}
