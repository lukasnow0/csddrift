package com.csdapp.model;

import javafx.scene.paint.Color;


public class Allel{
	
	private int allelId;
	private Color color;

	public Allel(int id){
		this.allelId = id;
	}
	
	public Allel(int id, Color c){
		this.allelId = id;
		this.color = c;
	}

	public int getAllelId() {
		return allelId;
	}

	public void setAllelId(int allelId) {
		this.allelId = allelId;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	@Override
	public String toString() {
		return "" + allelId;
	}
}
