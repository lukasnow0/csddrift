package com.csdApp.model;

public class Mother{
	
	private boolean youngMother;
	private boolean blankMother;
	private int redundantAllelInSprermPool;
	private long motherId;
	private int generation;
	private int x, y;
	private Allel allelOne, allelTwo;
	
	private Allel[] spermPool;
	
	public Mother(long id, Allel ao, Allel at, int gen, int x, int y){
		this.youngMother = true;
		this.blankMother = false;
		this.redundantAllelInSprermPool = 0;
		this.motherId = id;
		this.allelOne = ao;
		this.allelTwo = at;
		this.generation = gen;
		this.x = x;
		this.y = y;
	}
	
	
	public int redundandAllelCheck() {
		int temp = 0;
		if (spermPool.length == 0) {
			return temp;
		} else { 
			for (Allel a : spermPool) {
				if (a.getAllelId() == allelOne.getAllelId()){
					temp++;
				}
				if (a.getAllelId() == allelTwo.getAllelId()){
					temp++;
				}
			}
		}
		setRedundantAllelInSprermPool(temp);
		return temp;
	}
	
	public boolean isYoungMother() {
		return youngMother;
	}

	public void setYoungMother(boolean youngMother) {
		this.youngMother = youngMother;
	}

	public long getMotherId() {
		return motherId;
	}
	public void setMotherId(long motherId) {
		this.motherId = motherId;
	}
	public int getGeneration() {
		return generation;
	}
	public void setGeneration(int generation) {
		this.generation = generation;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Allel getAllelOne() {
		return allelOne;
	}

	public void setAllelOne(Allel allelOne) {
		this.allelOne = allelOne;
	}

	public Allel getAllelTwo() {
		return allelTwo;
	}

	public void setAllelTwo(Allel allelTwo) {
		this.allelTwo = allelTwo;
	}
	
	public Allel[] getSpermPool() {
		return spermPool;
	}

	public void setSpermPool(Allel[] spermPool) {
		this.spermPool = spermPool;
	}
	public int getRedundantAllelInSprermPool() {
		return redundantAllelInSprermPool;
	}
	
	public void setRedundantAllelInSprermPool(int redundantAllelInSprermPool) {
		this.redundantAllelInSprermPool = redundantAllelInSprermPool;
	}

	public boolean isBlankMother() {
		return blankMother;
	}

	public void setBlankMother(boolean blankMother) {
		this.blankMother = blankMother;
	}

	@Override
	public String toString() {
		if(isBlankMother()){
			return "null;null;null;null;" + x + ";" + y + "\r\n";
		}
		return "" + motherId + ";" + allelOne + ";" + allelTwo + ";" + generation + ";" + x + ";" + y + "\r\n";
	}
}
