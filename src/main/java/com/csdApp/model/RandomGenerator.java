package com.csdApp.model;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javafx.scene.paint.Color;

public class RandomGenerator {
	
	private List<Color> colorPool = new ArrayList<Color>();
	
	public RandomGenerator(int allelNum){
		for(int i = 0; i < allelNum; i++){
			double r = ThreadLocalRandom.current().nextDouble(1);
			double b = ThreadLocalRandom.current().nextDouble(1);
			double g = ThreadLocalRandom.current().nextDouble(1);
			double o = ThreadLocalRandom.current().nextDouble(1);
			colorPool.add(new Color(r, g, b, o));
		}
	}

	public List<Color> getColorPool() {
		return colorPool;
	}
}
