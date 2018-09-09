package com.csdApp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.csdApp.fileManager.FileManager;

public class ModelNew extends Model{

	public static int winterFactor = 20;
	private int generationCount;
	private long motherCount;
	private int allelNumber;
	private int generationNum;
	private int netSideSize;
	private double dip;
	private int droneDist;
	private int swarmingDist;
	private int fileSeparator;
	private FileManager fileManager = null;
	private volatile Mother[][] net;
	private Mother[][] eggNet;
	private AllelPool allelPool;
	private boolean torus;
	private String dir;
	
	public ModelNew(int allelNumber, int generationNumber, int netSideSize, double dip,
			int droneDist, int swarmingDist, String dir, boolean torus){
		super(swarmingDist, swarmingDist, swarmingDist, dip, swarmingDist, swarmingDist, dir, torus);
		this.generationCount = 1;
		this.motherCount = 0;
		this.allelNumber = allelNumber;
		this.generationNum = generationNumber;
		this.netSideSize = netSideSize;
		this.dip = dip;
		this.droneDist = droneDist;
		this.swarmingDist = swarmingDist;
		this.fileSeparator = 100;
		this.fileManager = new FileManager(dir , "\\simInfo", "\\Generation");
		this.dir = dir;
		this.torus = torus;
		net = new Mother[netSideSize][netSideSize];
		eggNet = new Mother[netSideSize][netSideSize];
		allelPool = new AllelPool(allelNumber);
		innitialNetFill();
		initialWinterSelection();
		collectSpermPool();
		simulationInfoGen();
	}
	
	private synchronized void innitialNetFill(){
		for(int i = 0; i < net.length; i++){
			for(int j = 0; j < net.length; j++){
				Allel[] a = randomAllelPair();
				Mother temp = new Mother(motherCount, a[0],  a[1], generationCount, i, j);
				net[i][j] = temp;
				motherCount++;
			}
		}
		generationCount++;
	}
	
	private Allel[] randomAllelPair(){
		Allel[] temp = new Allel[2];
		do {
			temp[0] = allelPool.getRandomAllel();
			temp[1] = allelPool.getRandomAllel();
		} while (temp[0].equals(temp[1]));
		return temp;
	}
	
	private void initialWinterSelection() {
		double tempDip = getDip();
		setDip(0.0);
		cyclicWinterSelection();
		setDip(tempDip);
	}
	
	private void cyclicWinterSelection() {
		for(int i = 0; i < net.length; i++){
			for(int j = 0; j < net.length; j++){
				if (!winterSelection(net[i][j])){
					Mother tempMother = new Mother(-1, new Allel(-1), new Allel(-1), -1, i, j);
					tempMother.setBlankMother(true);
					tempMother.setYoungMother(false);
					net[i][j]  = tempMother;
				}
			}
		}
	}
	
	private boolean winterSelection(Mother m){
		int tempWinterFactor = winterFactor;
		int selectionFactor = m.getRedundantAllelInSprermPool();
		if (selectionFactor > 0) {
			tempWinterFactor += (selectionFactor * (int) Math.round(dip * 100));
		}
		if ((ThreadLocalRandom.current().nextInt(100) < tempWinterFactor) || ((generationCount - m.getGeneration()) > 5 )) {
			//mother dies
			return false;
		} else {
			//mother survives
			return true;			
		}
	}
	
	private void collectSpermPool() {
		for(int i = 0; i < net.length; i++){
			for(int j = 0; j < net.length; j++){
				if (net[i][j].isYoungMother()) {
					Allel[] tempAllels = neighbouringAllels(net[i][j], 15);
					net[i][j].setSpermPool(tempAllels);
					net[i][j].setYoungMother(false);
					net[i][j].redundandAllelCheck();
				}
			}
		}	
	}
	
	private Allel[] neighbouringAllels(Mother m, int amount){
		int x = m.getX();
		int y = m.getY();
		int left = x - droneDist;
		int right = x + droneDist;
		int top = y - droneDist;
		int bottom = y + droneDist;
		List<Allel> list = new ArrayList<Allel>();
		
		if(torus){
			for(int i = left; i < right; i++){
				for(int j = top; j < bottom; j++){
					if(i == x && j == y){
						continue;
					}
					if (i < 0 || i >= net.length || j < 0 || j >= net.length) {
						if (! net[getTorusValue(i)][getTorusValue(j)].isBlankMother()) {
							list.add(net[getTorusValue(i)][getTorusValue(j)].getAllelOne());
							list.add(net[getTorusValue(i)][getTorusValue(j)].getAllelTwo());
//							int index = ThreadLocalRandom.current().nextInt(2);
//							if(index == 0){
//								list.add(net[getTorusValue(i)][getTorusValue(j)].getAllelOne());						
//							} else {
//								list.add(net[getTorusValue(i)][getTorusValue(j)].getAllelTwo());						
//							}
						}
					} else {
						if (! net[i][j].isBlankMother()) {
							list.add(net[i][j].getAllelOne());
							list.add(net[i][j].getAllelTwo());
//							int index = ThreadLocalRandom.current().nextInt(2);
//							if(index == 0){
//								list.add(net[i][j].getAllelOne());						
//							} else {
//								list.add(net[i][j].getAllelTwo());						
//							}
						}
					}		
				}
			}		
		} else {
			while(left < 0){
				left++;
			}
			while(right > netSideSize){
				right--;
			}
			while(top < 0){
				top++;
			}
			while(bottom > netSideSize){
				bottom--;
			}

			for(int i = left; i < right; i++){
				for(int j = top; j < bottom; j++){
					if(i == x && j == y){
						continue;
					}
					if (! net[i][j].isBlankMother()) {
						list.add(net[i][j].getAllelOne());
						list.add(net[i][j].getAllelTwo());
//						int index = ThreadLocalRandom.current().nextInt(2);
//						if(index == 0){
//							list.add(net[i][j].getAllelOne());						
//						} else {
//							list.add(net[i][j].getAllelTwo());						
//						}
					}
				}
			}
		}
		
		Allel[] temp = new Allel[amount];
		AllelPool tempAllelPool = new AllelPool(list);
		for(int k = 0; k < amount; k++){
			temp[k] = tempAllelPool.getRandomAllel();
		}
		return temp;
	}
	
	private int getTorusValue(int i) {
		return (i % netSideSize + netSideSize) % netSideSize;
	}
	
	private void eggCreator() {
		for(int i = 0; i < net.length; i++){
			for(int j = 0; j < net.length; j++){
				if (! net[i][j].isBlankMother()) {
					Allel[] selectedAllels = allelSelector(net[i][j]);
					Mother temp = new Mother(motherCount, selectedAllels[0], selectedAllels[1], generationCount, i, j);
					eggNet[i][j] = temp;
					motherCount++;
				} else {
					eggNet[i][j] = net[i][j];
				}
			}
		}
	}
	
	private Allel[] allelSelector(Mother m) {
		AllelPool tempAllelPool = new AllelPool(Arrays.asList(m.getSpermPool()));
		Allel[] motherAllels = { m.getAllelOne(), m.getAllelTwo() };
		AllelPool motherAllelPool = new AllelPool(Arrays.asList(motherAllels));
		Allel[] selectedAllels = new Allel[2];
		do {
			selectedAllels[0] = tempAllelPool.getRandomAllel();
			selectedAllels[1] = motherAllelPool.getRandomAllel();
		} while (selectedAllels[0].equals(selectedAllels[1]));
		return selectedAllels;
	}
	
	private List<Mother> oldMotherCollector(){
		List<Mother> tempOldMothers = new ArrayList<>();
		for(int i = 0; i < netSideSize; i++){
			for(int j = 0; j < netSideSize; j++){
				if(! net[i][j].isBlankMother()){
					tempOldMothers.add(net[i][j]);
				}
			}
		}
		return tempOldMothers;
	}
	
	private void oldMothersSwarming() {
		List<Mother> tempOldMothers = oldMotherCollector();
		A: while (tempOldMothers.size() > 0) {
			int index = ThreadLocalRandom.current().nextInt(tempOldMothers.size());
			Mother tempMother = tempOldMothers.get(index);
			int x = tempMother.getX();
			int y = tempMother.getY();
			int left = x - swarmingDist;
			int right = x + swarmingDist;
			int top = y - swarmingDist;
			int bottom = y + swarmingDist;
			
			if(torus) {
				//torus
				for (int k = 0; k < 5; k++) {
					int tempX = ThreadLocalRandom.current().nextInt(left, right + 1);//not sure if +1 is necessary
					int tempY = ThreadLocalRandom.current().nextInt(top, bottom + 1);//same here
					if(tempX < 0 || tempX >= netSideSize || tempY < 0 || tempY >= netSideSize) {
						tempX = getTorusValue(tempX);
						tempY = getTorusValue(tempY);	
					}
					if (eggNet[tempX][tempY].isBlankMother()) {
						tempMother.setX(tempX);
						tempMother.setY(tempY);
						eggNet[tempX][tempY] = tempMother;
						tempOldMothers.remove(index);
						continue A;
					}
				}
			} else {
				// non torus
				while (left < 0) {
					left++;
				}
				while (right > netSideSize) {
					right--;
				}
				while (top < 0) {
					top++;
				}
				while (bottom > netSideSize) {
					bottom--;
				}
				
				int tempX = 0;
				int tempY = 0;
				for (int k = 0; k < 5; k++) {
					
					tempX = ThreadLocalRandom.current().nextInt(left, right);
					tempY = ThreadLocalRandom.current().nextInt(top, bottom);
					if (eggNet[tempX][tempY].isBlankMother()) {
						tempMother.setX(tempX);
						tempMother.setY(tempY);
						eggNet[tempX][tempY] = tempMother;
						tempOldMothers.remove(index);
						continue A;
					}
				}
			}
			tempOldMothers.remove(index);
		}
	}

	private void generationCycle() {
		/*
		 * 1. new egg creation
		 * 2. old mothers's migration
		 * 3. collection of neighboring allels into a spermpool in an young mother
		 * 4. winter selection 
		 */
		eggCreator();
		generationCount++;
		oldMothersSwarming();
		net = eggNet;
		eggNet = new Mother[netSideSize][netSideSize];
		collectSpermPool();
		cyclicWinterSelection();
	}

	private void runModel(){
		fileManager.saveUserFile(formatOutputLine(), generationCount - 1);
		for (int i = 0; i < generationNum - 1; i++) {
			generationCycle();
			if((i + 2) % fileSeparator == 0){
				fileManager.saveUserFile(formatOutputLine(), generationCount - 1);				
			}
			try{
				Thread.sleep(1);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
		fileManager.saveUserFile(formatOutputLine(), generationCount - 1);
	}

	private String formatOutputLine(){
		String tempA = Arrays.deepToString(getNet());
		tempA = tempA.replace("[", "");
		tempA = tempA.replace("]", "");
		tempA = tempA.replace(", ", "");
		String temp = "Generation: " + (generationCount - 1) + "\r\n" + "Net:\r\n" + tempA + "\r\n";
		return temp;
	}
	
	private void simulationInfoGen() {
		StringBuilder sb = new StringBuilder();
		sb.append("Date and time: "+ LocalDateTime.now().toString() + "\r\n");
		sb.append("Project directory: " + dir + "\r\n");
		sb.append("Allele number: " + allelNumber + "\r\n");
		sb.append("Net side size: " + netSideSize + "\r\n");
		sb.append("Generation nubmer: " + generationNum + "\r\n");
		sb.append("DIP: " + dip + "\r\n");
		sb.append("Drone distance: " + droneDist + "\r\n");
		sb.append("Swarming distance: " + swarmingDist + "\r\n");
		if(torus){			
			sb.append("Net type: torus\r\n");
		} else {
			sb.append("Net type: regular\r\n");
		}
		fileManager.saveSimInfoFile(sb.toString());
	}

	public synchronized Mother[][] getNet() {
		return net;
	}


	private double getDip() {
		return dip;
	}
	
	
	private void setDip(double dip) {
		this.dip = dip;
	}
	
	public int getGenerationCount() {
		return generationCount;
	}
	
	@Override
	public void run() {
		runModel();
	}

}
