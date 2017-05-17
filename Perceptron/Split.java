import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Split {

	public static HashMap<String, Integer> words = new HashMap<String, Integer>();
	public static HashMap<String, Double> wt = new HashMap<String, Double>();
	public static ArrayList<String> items = new ArrayList<String>();
	public static ArrayList<String> stopwords = new ArrayList<String>();

	public static String file_path = null;
	public static File[] ham_files = null;
	public static File[] spam_files = null;

	public static int doc_count = 0;
Split() {

	}
	Split(String file_path, File data_ham, File data_spam) {
		this.file_path = file_path;
		this.ham_files = data_ham.listFiles();
		this.spam_files = data_spam.listFiles();
		doc_count = getDocCount(ham_files, spam_files);

	}
public void allFiles() {
		int count = 0;
		for (int i = 0; i < doc_count; i++) {

			if (i < ham_files.length) {
				create_Words(ham_files[i]);
			}
			else {
				create_Words(spam_files[count]);
				count++;
			}
		}
		for (String key : words.keySet()) {
			wt.put(key, 0.5);

		}
	}
public void create_Words(File fileName) {
     Misc m = new Misc();
		try {

			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);

			String line = null;

			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = m.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					String w = tokens.nextToken();

					if (words.containsKey(w)) {
						int value = words.get(w);
						words.put(w, value + 1);
					}

					else {
						words.put(w, 1);
					}
				}

			}

		} catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}

	}
	public void find_Stop() {
		
		
		stopwords.clear();
		Misc m = new Misc();

		try {

			FileReader file = new FileReader(file_path + "stopwords.txt");
			BufferedReader br = new BufferedReader(file);

			String line = null;

			while ((line = br.readLine()) != null) {
				StringTokenizer tokens = m.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					String w = tokens.nextToken();

					if (!stopwords.contains(w)) {
						stopwords.add(w);
					}
				}
			}			
		}

		catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}

	}

	public void remove_Stop() {

		
		for (int i = 0; i < stopwords.size(); i++) {
			if (wt.containsKey(stopwords.get(i))) {
				wt.remove(stopwords.get(i));
			}
		}
	}
	public ArrayList<String> create_Items(File fileName,
			int a) {

		items.clear();
		Misc m = new Misc();
		try {

			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);

			String line = null;

			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = m.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					String w = tokens.nextToken();

					if (!items.contains(w)
							&& (0 == a)) {
						items.add(w);
					}

					if (1 == a) {
						if (!items.contains(w)
								&& (!stopwords.contains(w))) {
							items.add(w);
						}
					}
				}

			}

		} catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}

		return items;
	}

	public int getDocCount(File[] ham_files, File[] spam_files) {
		return ham_files.length + spam_files.length;
	}

}