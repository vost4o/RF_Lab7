package ro.usv.rf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Classifier {
	// The key is the class and the value is the list of indexes
	private Map<String, Integer> classWMatrix;
	// Data set
	private String[][] dataSet;
	// W Matrix
	private double[][] wMatrix;
	// Unknown class pattern
	private String[] unknownClassPattern;
	// Classes map
	private Map<String, ArrayList<Integer>> classesMap;

	public Classifier(String[][] dataSet) {
		this.dataSet = dataSet;
		// Initialize classes map
		initClassesMap();
		createClassesMap();
	}

	private void initClassesMap() {
		classesMap = new HashMap<String, ArrayList<Integer>>();
		classWMatrix = new HashMap<String, Integer>();
	}

	/**
	 * Remember all the classes indices to find them more easily at the calculations
	 */
	private void createClassesMap() {
		int index = 0;
		for (int x = 0; x < dataSet.length; ++x) {
			String classType = dataSet[x][dataSet[x].length - 1];
			// Ignore the -1 class ( the pattern class we need to find out )
			if (!classType.equals("x")) {
				if (!classesMap.containsKey(classType)) {
					classesMap.put(classType, new ArrayList<Integer>());
					classWMatrix.put(classType, index++);
				}
				classesMap.get(classType).add(x);
			} else {
				unknownClassPattern = dataSet[x];
			}
		}

		createWMatrix();
	}

	/**
	 * Creates the W-Matrix
	 */
	private void createWMatrix() {
		int classCount = classesMap.size();
		int parameterCount = dataSet[0].length;

		wMatrix = new double[classCount][parameterCount];
		for (int i = 0; i < parameterCount; ++i) {
			if (i == parameterCount - 1) {
				// Calculate the free term
				for (int x = 0; x < wMatrix.length; x++) {
					for (int y = 0; y < wMatrix[0].length - 1; y++) {
						wMatrix[x][i] += (-1 / 2.0) * Math.pow(wMatrix[x][y], 2);
					}
				}
			} else {
				// Calculate the w1..n term
				for (String classType : classesMap.keySet()) {
					// Go through all classes
					List<Integer> indexes = classesMap.get(classType);
					int classIndex = classWMatrix.get(classType);
					for (int j = 0; j < indexes.size(); j++) {
						wMatrix[classIndex][i] += Double.parseDouble(dataSet[indexes.get(j)][i]);
					}
					// Don't forget to divide to number of pattern's of the coresponding class
					wMatrix[classIndex][i] /= indexes.size();
				}
			}
		}

		calculateFunctions();
	}

	/**
	 * Calculate the functions value
	 */
	private void calculateFunctions() {
		int patternClassIndex = dataSet[0].length - 1;

		int maxFunctionValueIndex = -1;
		double maxFunctionValue = Integer.MIN_VALUE;
		for (int x = 0; x < wMatrix.length; ++x) {
			// Calculate the function value
			double functionValue = 0;
			for (int y = 0; y < wMatrix[0].length; ++y) {
				functionValue += wMatrix[x][y] * Double.valueOf(unknownClassPattern[y]);
			}

			// Check if the current function value is the biggest and remember the index
			// ( it will be considered as the class of the pattern )
			if (maxFunctionValue < functionValue) {
				maxFunctionValue = functionValue;
				maxFunctionValueIndex = x;
			}
		}

		if (maxFunctionValueIndex != -1) {
			String foundClass = "";
			for (String key : classWMatrix.keySet()) {
				int keyIndex = classWMatrix.get(key);
				if (keyIndex == maxFunctionValueIndex) {
					foundClass = key;
				}
			}
			// Increment the index by one to make sure it is a class
			unknownClassPattern[patternClassIndex] = foundClass;
		}

		System.out.println("The pattern class is " + (maxFunctionValueIndex + 1));
	}

	/**
	 * A method to check the W-Matrix content (for debug purposes)
	 */
	@Override
	public String toString() {
		int wMatrixRows = wMatrix.length;
		int wMatrixCols = wMatrix[0].length;

		StringBuilder strBuilder = new StringBuilder(wMatrixRows * wMatrixCols);

		for (int x = 0; x < wMatrixRows; ++x) {
			for (int y = 0; y < wMatrixCols; ++y) {
				strBuilder.append(wMatrix[x][y] + (y == wMatrixCols - 1 ? "" : ","));
			}
			strBuilder.append("\n");
		}

		return strBuilder.toString();
	}
}
