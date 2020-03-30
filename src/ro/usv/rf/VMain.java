package ro.usv.rf;

public class VMain {
	
	public static void main(String[] args) {
		try {
			double[][] data = FileUtils.readLearningSetFromFile("in.txt");
			Classifier classifier = new Classifier(data);
			System.err.println();
		} catch (USVInputFileCustomException e) {
			e.printStackTrace();
		}
	}
}
