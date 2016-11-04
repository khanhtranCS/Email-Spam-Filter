import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class NaiveBayes {
	public static HashMap<String, Double> spam_count = new HashMap<String, Double>();
	public static HashMap<String, Double> ham_count = new HashMap<String, Double>();
	public static double P_spam;
	public static double P_ham;
	public static double total_spam;
	public static double total_ham;
	//	This function reads in a file and returns a 
	//	set of all the tokens. It ignores the subject line
	//
	//	If the email had the following content:
	//
	//	Subject: Get rid of your student loans
	//	Hi there ,
	//	If you work for us, we will give you money
	//	to repay your student loans . You will be 
	//	debt free !
	//	FakePerson_22393
	//
	//	This function would return to you
	//	[hi, be, student, for, your, rid, we, get, of, free, if, you, us, give, !, repay, will, loans, work, fakeperson_22393, ,, ., money, there, to, debt]
	public static HashSet<String> tokenSet(File filename) throws IOException {
		HashSet<String> tokens = new HashSet<String>();
		Scanner filescan = new Scanner(filename);
		filescan.next(); //Ignoring "Subject"
		while(filescan.hasNextLine() && filescan.hasNext()) {
			tokens.add(filescan.next());
		}
		filescan.close();
		return tokens;
	}
	
	public static void main(String[] args) throws IOException {
		//TODO: Implement the Naive Bayes Classifier
		File f_ham = new File("/Users/ktran035/Desktop/cse312/NaiveBayes/data/train/ham");
		File f_spam = new File("/Users/ktran035/Desktop/cse312/NaiveBayes/data/train/ham");
		
		// process file and populate data to ham and spam map
		processFiles(f_ham, f_spam);
		
		// get Ham and Spam probability
		getHamSpamProb(f_ham, f_spam);
		
		// get probability of 
		System.out.println(probOfWord("feedback", true));
		
	}
	
	// find probability of word given that the mail is either spam or ham
	public static double probOfWord(String word, boolean ham_huh) {
		double result = 0;
		// if it's a ham
		if(ham_huh) {
			// if word exist in ham file
			if (ham_count.containsKey(word)) {
				result = ham_count.get(word) / total_ham;
			} else { // if word doesn't exist
				result = 1.0 / (total_ham + 2);
			}
		} else {
			// if word exist in ham file
			if (spam_count.containsKey(word)) {
				result = spam_count.get(word) / total_ham;
			} else { // if word doesn't exist
				result = 1.0 / (total_ham + 2);
			}
		}
		return result;
	}
	
	// prevent code duplication issue
	public static double probOfWordCD(String word, HashMap<String, Double> count_map) {
		double result = 0;
		// if word exist in ham file
		if (count_map.containsKey(word)) {
			result = count_map.get(word) / total_ham;
		} else { // if word doesn't exist
			result = 1.0 / (total_ham + 2);
		}
		return result;
	}
	
	public static void processFiles(File f_ham, File f_spam) throws IOException {
		// For some reason .DS_store is included in list of files path
		parseDataFromFile(f_ham.listFiles(), true);
		parseDataFromFile(f_spam.listFiles(), false);
		//System.out.println(f_ham.listFiles().length);

	}
	
	public static void getHamSpamProb(File f_ham, File f_spam) {
		// -1 because of .DS_Store file
		total_spam = f_ham.listFiles().length - 1.0;
		total_ham = f_spam.listFiles().length - 1.0;
		double total_file = total_ham + total_spam;
		P_spam = total_spam / total_file;
		P_ham = total_ham / total_file;
	}
	
	public static void parseDataFromFile(File[] files, boolean ham_huh) throws IOException {
		//HashMap<String, Double> result = new HashMap<String, Double>();
		// process each text file, start i = 1 because of .DS_Store file
		for (int i = 1; i < files.length; i++) {
			//File curr_txt = new File(files[i]);
			// set of all word in files
			HashSet<String> word_in_file = tokenSet(files[i]);
			parseCountData(word_in_file, ham_huh);
		}
	}
	
	// populate data for two ham and spam hash table
	// where key is word, value is number of file contain that word
	public static void parseCountData(HashSet<String> word_in_file, boolean ham_huh) {
		for (String word: word_in_file) {
			if (ham_huh) { // this file is a ham
				if (ham_count.containsKey(word)) {
					ham_count.put(word, ham_count.get(word) + 1);
				} else { // new word
					ham_count.put(word, 1.0);
				}
			} else { // this email file is not a ham, it's spam
				if (spam_count.containsKey(word)) {
					spam_count.put(word, spam_count.get(word) + 1);
				} else { // new word
					spam_count.put(word, 1.0);
				}
			}
		}
		
	}
	
	
}
