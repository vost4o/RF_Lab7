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

	private void createClassesMap() {
		for (int x = 0; x < dataSet.length; ++x) {
			int classType = (int) dataSet[x][dataSet[x].length - 1];
			if (!classesMap.containsKey(classType)) {
				classesMap.put((int) classType, new ArrayList<Integer>());
			}
			classesMap.get(classType).add(x);
		}

		createWMatrix();
	}

	private void createWMatrix() {
		int classCount = classesMap.size();
		int parameterCount = dataSet[0].length;

		wMatrix = new double[classCount][parameterCount];
		for (int i = 0; i < parameterCount; ++i) {
			for (int classType : classesMap.keySet()) {
				List<Integer> indexes = classesMap.get(classType);
				for (int j = 0; j < indexes.size(); ++j) {
					wMatrix[classType - 1][i] += dataSet[indexes.get(j)][i];
				}
				wMatrix[classType - 1][i] /= indexes.size();
			}
		}
	}

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
