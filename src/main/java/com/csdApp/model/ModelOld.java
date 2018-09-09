package com.csdapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.csdapp.fileManager.FileManager;

public class ModelOld implements Runnable{
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
	private AllelPool allelPool;
	private boolean torus;
	private String dir;
	

	
	public ModelOld(int allelNumber, int generationNumber, int netSideSize, double dip,
			int droneDist, int swarmingDist, String dir, boolean torus){
		this.generationCount = 1;
		this.motherCount = 0;
		this.allelNumber = allelNumber;
		this.generationNum = generationNumber;
		this.netSideSize = netSideSize;
		this.dip = dip;
		this.droneDist = droneDist;
		this.swarmingDist = swarmingDist;
		this.fileSeparator = 100;
//		if(generationNumber < 2000){
//			this.fileSeparator = 4;
//		} else {
//			this.fileSeparator = (int) Math.ceil((double) generationNumber * 0.002);						
//		}
		this.fileManager = new FileManager(dir , "\\simInfo", "\\Generation");
		this.dir = dir;
		this.torus = torus;
		net = new Mother[netSideSize][netSideSize];
		allelPool = new AllelPool(allelNumber);
		innitialNetFill();
		simulationInfoGen();
	}
	

	public synchronized void innitialNetFill(){
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
		do{
			temp[0] = allelPool.getRandomAllel();
			temp[1] = allelPool.getRandomAllel();
		}while(temp[0].equals(temp[1]));
		return temp;
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
							int index = ThreadLocalRandom.current().nextInt(2);
							if(index == 0){
								list.add(net[getTorusValue(i)][getTorusValue(j)].getAllelOne());						
							} else {
								list.add(net[getTorusValue(i)][getTorusValue(j)].getAllelTwo());						
							}
						}
					} else {
						if (! net[i][j].isBlankMother()) {
							int index = ThreadLocalRandom.current().nextInt(2);
							if(index == 0){
								list.add(net[i][j].getAllelOne());						
							} else {
								list.add(net[i][j].getAllelTwo());						
							}
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
						int index = ThreadLocalRandom.current().nextInt(2);
						if(index == 0){
							list.add(net[i][j].getAllelOne());						
						} else {
							list.add(net[i][j].getAllelTwo());						
						}
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
	
	
	public Mother[][] fertilisation(){
		Mother[][] newNet = new Mother[netSideSize][netSideSize];
		for(int i = 0; i < net.length; i++){
			for(int j = 0; j < net.length; j++){
				if (! net[i][j].isBlankMother()) {
					if (net[i][j].isYoungMother()) {
						Allel[] tempAllels = neighbouringAllels(net[i][j], 15);
						net[i][j].setSpermPool(tempAllels);
						net[i][j].setYoungMother(false);
						
						if (winterSelection(net[i][j])){
							AllelPool tap;
							Allel selectedAllelA;
							Allel selectedAllelB;
							do {
								tap = new AllelPool(Arrays.asList(tempAllels));
								selectedAllelA = tap.getRandomAllel();
								Allel[] motherAllels = { net[i][j].getAllelOne(), net[i][j].getAllelTwo() };
								tap.setAllelPool(Arrays.asList(motherAllels));
								selectedAllelB = tap.getRandomAllel();
							} while (selectedAllelA.equals(selectedAllelB));
							Mother temp = new Mother(motherCount, selectedAllelA, selectedAllelB, generationCount, i, j);
							newNet[i][j] = temp;
						} else {
							Mother temp = new Mother(-1, new Allel(-1), new Allel(-1), -1, i, j);
							temp.setBlankMother(true);
							newNet[i][j]  = temp;
						}
						
					} else {
						Allel[] tempAllels = net[i][j].getSpermPool();
						
						if (winterSelection(net[i][j])){
							AllelPool tap;
							Allel selectedAllelA;
							Allel selectedAllelB;
							do {
								tap = new AllelPool(Arrays.asList(tempAllels));
								selectedAllelA = tap.getRandomAllel();
								Allel[] motherAllels = { net[i][j].getAllelOne(), net[i][j].getAllelTwo() };
								tap.setAllelPool(Arrays.asList(motherAllels));
								selectedAllelB = tap.getRandomAllel();
							} while (selectedAllelA.equals(selectedAllelB));
							Mother temp = new Mother(motherCount, selectedAllelA, selectedAllelB, generationCount, i, j);
							newNet[i][j] = temp;
						} else {
							Mother temp = new Mother(-1, new Allel(-1), new Allel(-1), -1, i, j);
							temp.setBlankMother(true);
							newNet[i][j]  = temp;
						}
					}
					motherCount++;
				} else {
					newNet[i][j] = net[i][j];
				}
			}
		}
		generationCount++;
		
		return newNet;
	}

	
	public boolean winterSelection(Mother m){
		int selectionFactor;
		int tempWF = winterFactor;
		int tempDip = (int) Math.round(dip * 100);
		
//		int redundantAllel = m.redundandAllelCheck();
//		m.setRedundantAllelInSprermPool(redundantAllel);
		
		m.redundandAllelCheck();
		selectionFactor = m.getRedundantAllelInSprermPool();
		
		if(selectionFactor > 0) {
			tempWF = tempWF + (selectionFactor * tempDip);
		}
		int randNum = ThreadLocalRandom.current().nextInt(100);
		if (randNum < tempWF) {
			//matka ginie
			return false;
		}
		//matka prze�ywa
		return true;
	}
		
	
	public List<Mother> oldMotherCollector(){
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
	
	
	public void netSwitch(){
		List<Mother> tempOldMothers = oldMotherCollector();
		Mother[][] tempNewNet = fertilisation();
		net = tempNewNet;
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
				//z torus
				for (int k = 0; k < 5; k++) {
					int tempX = ThreadLocalRandom.current().nextInt(left, right);
					int tempY = ThreadLocalRandom.current().nextInt(top, bottom);
					if(tempX < 0 || tempX >= netSideSize || tempY < 0 || tempY >= netSideSize) {
						tempX = getTorusValue(tempX);
						tempY = getTorusValue(tempY);	
					}
					if (net[tempX][tempY].isBlankMother()) {
						tempMother.setX(tempX);
						tempMother.setY(tempY);
						net[tempX][tempY] = tempMother;
						tempOldMothers.remove(index);
						continue A;
					}
				}
			} else {
				// bez torus
				while (left < 0) {
					left++;
				}
				while (right > (netSideSize)) {
					right--;
				}
				while (top < 0) {
					top++;
				}
				while (bottom > (netSideSize)) {
					bottom--;
				}
				
				int tempX = 0;
				int tempY = 0;
				for (int k = 0; k < 5; k++) {
					
					tempX = ThreadLocalRandom.current().nextInt(left, right);
					tempY = ThreadLocalRandom.current().nextInt(top, bottom);
					if (net[tempX][tempY].isBlankMother()) {
						tempMother.setX(tempX);
						tempMother.setY(tempY);
						net[tempX][tempY] = tempMother;
						tempOldMothers.remove(index);
						continue A;
					}
					
				}
			}
			
			tempOldMothers.remove(index);
		}
	}
	

	public void runModel(){
		fileManager.saveUserFile(formatOutputLine(), generationCount - 1);
		for (int i = 0; i < generationNum - 1; i++) {
			netSwitch();
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
	
	protected int getTorusValue(int i) {
		return (i % netSideSize + netSideSize) % netSideSize;
	}

	public int getAllelNumber() {
		return allelNumber;
	}

	public void setAllelNumber(int allelNumber) {
		this.allelNumber = allelNumber;
	}

	public int getGenerationNum() {
		return generationNum;
	}

	public void setGenerationNum(int generationNum) {
		this.generationNum = generationNum;
	}

	public int getGenerationCount() {
		return generationCount;
	}

	public void setGenerationCount(int generationCount) {
		this.generationCount = generationCount;
	}

	public long getMotherCount() {
		return motherCount;
	}

	public void setMotherCount(long motherCount) {
		this.motherCount = motherCount;
	}

	public int getNetSideSize() {
		return netSideSize;
	}

	public void setNetSideSize(int netSideSize) {
		this.netSideSize = netSideSize;
	}

	public AllelPool getAllelPool() {
		return allelPool;
	}

	public void setAllelPool(AllelPool allelPool) {
		this.allelPool = allelPool;
	}

	public synchronized Mother[][] getNet() {
		return net;
	}

	public void setNet(Mother[][] net) {
		this.net = net;
	}

	@Override
	public void run() {
		runModel();
	}
}
