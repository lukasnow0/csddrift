package com.csdapp.model;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.paint.Color;

public class AllelPool {
	private List<Allel> allelPool = new ArrayList<Allel>();
	private int allelNumber;
	private RandomGenerator rand;
	
	public AllelPool(int allelNumber){
		this.allelNumber = allelNumber;
		rand = new RandomGenerator(allelNumber);
		allelPoolGen();
	}
	
	public AllelPool(List<Allel> list){
		this.allelPool = list;
	}
	
	public Allel getRandomAllel(){
		int index = ThreadLocalRandom.current().nextInt(allelPool.size());
		Allel tempAllel = allelPool.get(index);
		return tempAllel;
	}
	
	public void allelPoolGen(){
		if(!allelPool.isEmpty()){
			return;
		}
		try {
			List<Color> colorPool = rand.getColorPool();
			for(int i = 1; i <= allelNumber; i++){
						int index = ThreadLocalRandom.current().nextInt(colorPool.size());
						Allel a = new Allel(i,  colorPool.remove(index));
						allelPool.add(a);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Allel> getAllelPool() {
		return allelPool;
	}

	public void setAllelPool(List<Allel> allelPool) {
		this.allelPool = allelPool;
	}

	public int getAllelNumber() {
		return allelNumber;
	}

	public void setAllelNumber(int allelNumber) {
		this.allelNumber = allelNumber;
	}

	@Override
	public String toString() {
		return allelPool.toString();
	}
}
