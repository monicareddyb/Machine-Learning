import java.io.File;
public class Perceptron {

	public static final String TRAIN = "train";
	public static final String TEST = "test";
public static void main(String[] args) {

		String file_path = args[0];
		File ham_path = new File(file_path + args[1] + "/ham/");
		File spam_path = new File(file_path + args[1] + "/spam/");

		Split train_data = new Split(file_path, ham_path, spam_path);
		train_data.allFiles();

		Classifier c = new Classifier();
		c.train_Set();

		train_data = null;
		ham_path = new File(file_path + args[2] + "/ham/");
		spam_path = new File(file_path + args[2] + "/spam/");

		Split test_data = new Split(file_path, ham_path, spam_path);
		for (String key : Split.wt.keySet()) {
			Double val = Split.wt.get(key);
		}
		c.test_Set(0);

		System.out.println();
		System.out.println("accuracy after removing stop words is");
		c.test_Set(1);

	}

}