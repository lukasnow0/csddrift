package com.csdapp.model;

public abstract class Model implements Runnable{
	public Model(int allelNumber, int generationNumber, int netSideSize, double dip,
			int droneDist, int swarmingDist, String dir, boolean torus, int winterFartor, int droneNumber){}
	
	public abstract Mother[][] getNet();
	public abstract int getGenerationCount();
}
