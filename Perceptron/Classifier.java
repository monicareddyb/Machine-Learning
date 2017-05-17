import java.util.ArrayList;

public class Classifier {
public static final int ham = 1;
public static final int spam = -1;
public static final int without_stop = 0;
public static final int withstop = 1;
Split s = new Split();

public final int no_iterations = 25;
public final double learning_rate = 0.001;
int w0 = 1;

public void train_Set() {

ArrayList<String> item_doc = new ArrayList<String>();
int count = 0;
System.out.println("the accuracy with  " + learning_rate + "  learning rate and  "+ no_iterations +" iterations is");

for (int i = 0; i < Split.doc_count; i++) {
if (i < Split.ham_files.length) {
item_doc = s.create_Items(Split.ham_files[i], without_stop);
update_Wt(ham, item_doc);
}
else {
item_doc = s.create_Items(Split.spam_files[count], without_stop);
update_Wt(spam,item_doc);
count++;
}
}
}
public void test_Set(int a) {
int inham = 0;
int inspam = 0;
ArrayList<String> item_doc = new ArrayList<String>();
int count = 0;
for (int i = 0; i < Split.doc_count; i++) {
if (i < Split.ham_files.length) {
item_doc = s.create_Items(Split.ham_files[i], a);
int classifiedAs = classify(ham, item_doc);
if (classifiedAs == 1)
inham++;
}
else {
item_doc = s.create_Items(Split.spam_files[count], a);
int classifiedAs = classify(spam, item_doc);
if (classifiedAs == -1)
inspam++;
count++;
}
}
accuracy(inham, Split.ham_files.length,inspam, Split.spam_files.length);
s.find_Stop();
s.remove_Stop();
}
public void accuracy(int inham, int no_files_ham,int inspam, int no_files_spam) {
		double accuracy = 0.0;
		double hamAccuracy = 0.0;
		double spamAccuracy = 0.0;
		hamAccuracy = ((double) inham / no_files_ham) * 100;
		spamAccuracy = ((double) inspam / no_files_spam) * 100;
		accuracy = ((double) (inham + inspam) / (no_files_ham + no_files_spam)) * 100;
		System.out.println(accuracy);

	}
public int classify(int type, ArrayList<String> vocab) {
int classifiedAs = ((w0 + dotProduct(vocab)) > 0.0 ? 1 : -1);
return classifiedAs;
}

public void update_Wt(int classification, ArrayList<String> vocab) {

for (int i = 0; i< no_iterations; i++) {
double currDocClassification = ((w0 + dotProduct(vocab)) > 0.0 ? 1.0: -1.0);

for (int j = 0; j < vocab.size(); j++) {
String w = vocab.get(j);
double weight = (learning_rate * (classification - currDocClassification) * Split.wt.get(w));
if (weight != 0.0) {
Split.wt.put(w, weight);
}
}
}
}

public double dotProduct(ArrayList<String> vocab) {
double sum = 0.0;
for (int i = 0; i < vocab.size(); i++) {
double frequency = 1;
double wordWeight = -1;
if ((Split.words.containsKey(vocab.get(i)))&& (Split.wt.containsKey(vocab.get(i)))) {
frequency = Split.words.get(vocab.get(i));
wordWeight = Split.wt.get(vocab.get(i));
}
sum += (frequency * wordWeight);
}
return sum;
}
}



