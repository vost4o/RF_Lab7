package ro.usv.rf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Classifier {
	// The key is the class and the value is the list of indexes
	private Map<Double, ArrayList<Integer>> classesMap;
	// Data set
	private double[][] dataSet;

	public Classifier(double[][] dataSet) {
		this.dataSet = dataSet;
		// Initialize classes map
		initClassesMap();
		createClassesMap();
	}

	private void initClassesMap() {
		classesMap = new HashMap<Double, ArrayList<Integer>>();
	}

	private void createClassesMap() {
		for (int x = 0; x < dataSet.length; ++x) {
			double classType = dataSet[x][dataSet[x].length - 1]; 
			if (!classesMap.containsKey(classType)) {
				classesMap.put(classType, new ArrayList<Integer>());
			}
			classesMap.get(classType).add(x);
		}
	}
	
	private void createWMatrix() {
		int classCount = classesMap.size();
		int parameterCount = dataSet[0].length - 1;
		
		double[][] wMatrix = new double[classCount][parameterCount];
	}
}
