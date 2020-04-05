package ro.usv.rf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Classifier {
	// The key is the class and the value is the list of indexes
	private Map<Integer, ArrayList<Integer>> classesMap;
	// Data set
	private double[][] dataSet;
	// W Matrix
	private double[][] wMatrix;

	public Classifier(double[][] dataSet) {
		this.dataSet = dataSet;
		// Initialize classes map
		initClassesMap();
		createClassesMap();
	}

	private void initClassesMap() {
		classesMap = new HashMap<Integer, ArrayList<Integer>>();
	}

	/**
	 * Remember all the classes indices to find them more easily at the calculations
	 */
	private void createClassesMap() {
		for (int x = 0; x < dataSet.length; ++x) {
			int classType = (int) dataSet[x][dataSet[x].length - 1];
			// Ignore the -1 class ( the pattern class we need to find out )
			if (classType != -1) {
				if (!classesMap.containsKey(classType)) {
					classesMap.put((int) classType, new ArrayList<Integer>());
				}
				classesMap.get(classType).add(x);
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
				for (int classType : classesMap.keySet()) {
					// Go through all classes
					List<Integer> indexes = classesMap.get(classType);
					for (int j = 0; j < indexes.size(); ++j) {
						wMatrix[classType - 1][i] += dataSet[indexes.get(j)][i];
					}
					// Don't forget to divide to number of pattern's of the coresponding class
					wMatrix[classType - 1][i] /= indexes.size();
				}
			}
		}
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
