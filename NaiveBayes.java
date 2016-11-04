import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class NaiveBayes {
	public static HashMap<String, Double> spam_count;
	public static HashMap<String, Double> ham_count;
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
		
		processFiles(f_ham, f_spam);
	}
	
	public static void processFiles(File f_ham, File f_spam) throws IOException {
		// For some reason .DS_store is included in list of files path
		parseDataFromFile(f_ham.listFiles());
		parseDataFromFile(f_spam.listFiles());
	}
	
	public static void parseDataFromFile(File[] files) throws IOException {
		//HashMap<String, Double> result = new HashMap<String, Double>();
		// process each text file
		for (int i = 1; i < files.length; i++) {
			//File curr_txt = new File(files[i]);
			// set of all word in files
			HashSet<String> word_in_file = tokenSet(files[i]);
			
			System.out.println(word_in_file);
		}
	}
}
