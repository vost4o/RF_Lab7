package ro.usv.rf;

public class VMain {
	
	public static void main(String[] args) {
		try {
			String[][] data = FileUtils.readLearningSetFromFile("in.txt");
			Classifier classifier = new Classifier(data);
			System.err.println(classifier);
		} catch (USVInputFileCustomException e) {
			e.printStackTrace();
		}
	}
}
