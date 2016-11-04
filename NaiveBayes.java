import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class NaiveBayes {
	// spam_count and ham_count, where each store number of files contain a word w
	public static HashMap<String, Double> spam_count = new HashMap<String, Double>();
	public static HashMap<String, Double> ham_count = new HashMap<String, Double>();
	// probablity of spam and ham, with total number of file
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
		File f_spam = new File("/Users/ktran035/Desktop/cse312/NaiveBayes/data/train/spam");
		
		File test_file = new File("/Users/ktran035/Desktop/Personal_project/Email-Spam-Filter/data/test");
		//File test_file = new File("")
		// process file and populate data to ham and spam map
		processFiles(f_ham, f_spam);
		
		// get Ham and Spam probability
		getHamSpamProb(f_ham, f_spam);
		
		// print of the result 
		IsSpamOrHam(test_file);
		
	}
	
	// print the result after checking if the text email is spam or ham
	public static void IsSpamOrHam(File test_file) throws IOException {
		File[] test_files = test_file.listFiles();
		ArrayList<String> lst_result = new ArrayList<String>();
		for (int i = 0; i < test_files.length; i++) {
			boolean spamHuh = HamOrSpam(test_files[i]);
			if (spamHuh) {// the file is a spam
				lst_result.add(test_files[i].getName() + " spam");
			} else {
				lst_result.add(test_files[i].getName() + " ham");
			}
		}
		Collections.sort(lst_result);
		for (String result: lst_result) {
			System.out.println(result);
		}
	}
	
	// helper function for IsSpamOrHam
	public static boolean HamOrSpam(File curr_txt_file) throws IOException {
		HashSet<String> set_str = tokenSet(curr_txt_file);
		// probability of product set of xi given spam
		double product_over_spam = 0.0;
		double product_over_ham = 0.0;
		for (String word: set_str) {
			product_over_spam += Math.log(probOfWord(word, false));
			product_over_ham += Math.log(probOfWord(word, true));
		}
		double sum_prob_ham = Math.log(P_ham) + product_over_ham;
		double sum_prob_spam = Math.log(P_spam) + product_over_spam;
		double result = (Math.log(P_spam) + product_over_spam) / (Math.log(P_spam) + product_over_spam + Math.log(P_ham) + product_over_ham);

		return sum_prob_spam > sum_prob_ham;
	}
	
	// find probability of word given that the mail is either spam or ham
	public static double probOfWord(String word, boolean ham_huh) {
		double result = 0.0;
		// if it's a ham
		if(ham_huh) {
			// if word exist in ham file
			if (ham_count.containsKey(word)) {
				result = (ham_count.get(word) + 1) / (total_ham + 2);
			} else { // if word doesn't exist
				result = 1.0 / (total_ham + 2);
			}
		} else {
			// if word exist in ham file
			if (spam_count.containsKey(word)) {
				result = (spam_count.get(word) + 1) / (total_spam + 2);
			} else { // if word doesn't exist
				result = 1.0 / (total_spam + 2);
			}
		}
		
		return result;
	}
	
	// Process list of file
	public static void processFiles(File f_ham, File f_spam) throws IOException {
		// For some reason .DS_store is included in list of files path
		parseDataFromFile(f_ham.listFiles(), true);
		parseDataFromFile(f_spam.listFiles(), false);

	}
	
	// populate data for ham and spam probability
	public static void getHamSpamProb(File f_ham, File f_spam) {
		// -1 because of .DS_Store file
		total_spam = f_spam.listFiles().length - 1.0;
		total_ham = f_ham.listFiles().length - 1.0;
		double total_file = total_ham + total_spam;
		P_spam = total_spam / total_file;
		P_ham = total_ham / total_file;
	}
	
	// parse email file into data format for map
	public static void parseDataFromFile(File[] files, boolean ham_huh) throws IOException {
		// process each text file, start i = 1 because of .DS_Store file
		for (int i = 1; i < files.length; i++) {
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
