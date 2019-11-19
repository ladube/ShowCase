import processing.core.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.jaunt.JauntException;

public class DecisionTree {
	public ArrayList<String> inputArray = new ArrayList<String>();
	ArrayList<String> outputArray = new ArrayList<String>();
	String output = "";
	int progress = 100; 
	ArrayList<String> positive;
	ArrayList<String> negative;
	ArrayList<String> training;
	ArrayList<ArrayList<String>> dataQ = new ArrayList<ArrayList<String>>();
	Branch<String> root = new Branch<String>();
	
	public void setIn(ArrayList<String> input) {
		inputArray = input;
	}
	public void separateData() {
			ArrayList<String> curToken = new ArrayList<String>();
		for(int i = 0; i < training.size(); i++) {
			if(!training.get(i).contentEquals("?")) {
				curToken.add(training.get(i));
			}else if(training.get(i).contentEquals("?")) {
				ArrayList<String> tmp = new ArrayList<String>();
				for(int j = 0; j < curToken.size(); j++)
					tmp.add(curToken.get(j)); 
				dataQ.add(tmp);
				curToken.clear();
			}
		}
		train();
	}
	public int findEmotion() {
		boolean eNegative = false;
		boolean ePositive = false;
		
		for(int i=0; i<inputArray.size(); i++) {
			if(negative.contains(inputArray.get(i))){
				if(eNegative == true) { //double negative
					eNegative = false; 
				}else 
				    eNegative = true;
			}else if(positive.contains(inputArray.get(i))){
				ePositive = true; 
			}
		}
		if(ePositive == true && eNegative == false) {
			progress = progress + 5;
			return 1;
		}else if( ePositive == false && eNegative == true) {
			progress = progress - 5;
			return 0;
		}
		return 2;
	}
	public void train() {
//		boolean lineEnd = false;
		for(int i = 0; i < dataQ.size(); i++) {
			ArrayList<String> curSequence = new ArrayList<String>();
		    for(int j = 0; j < dataQ.get(i).size(); j++) {
			 
	//			if(j == dataQ.get(i).size()-1)
	//				lineEnd = true;
	//			else
	//				lineEnd = false;
				 curSequence.add(dataQ.get(i).get(j));
				Branch<String> nood = new Branch<String>(curSequence);
				root.addNode(nood);	
		    }
		}
	}
	public void print() { //check tree
		root.print();
    }
	public String getResponse() {
		outputArray.clear();
		output = "";
		//calls Response class using index found in decision tree
		if(findEmotion() == 1) {
			outputArray.add(";)");
		}else if(findEmotion() == 0) {
			outputArray.add(":c");
		}else
			outputArray.add(":/");
		//this is just a test example
			//outputArray.add("response");
		//change array of strings into a string
		for(int i = 0; i < outputArray.size(); i++) {
			System.out.println("Response: " + outputArray.get(i));
			output = output + outputArray.get(i);
		}
		return output;
	}
	public void loadCatalogs(String p, int y) {
		String filePath = getPath(p);
		Path path = Paths.get(filePath);
		if(y == 1)
			positive = new ArrayList<String>();
		else if(y == 0)
			negative = new ArrayList<String>();
		else if(y == 2)
			training = new ArrayList<String>();
		try {
			List<String> lines = Files.readAllLines(path);
			for(int i=0; i<lines.size(); i++) {
				TextTokenizer tokenizer = new TextTokenizer(lines.get(i));
				Set<String> t = tokenizer.parseSearchText();
				if(y == 1)
					positive.addAll(t);
				else if(y == 0)
					negative.addAll(t);
				else if(y == 2)
					training.addAll(t);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Problem reading the file.");
		}
	}
	public String getPath(String path) {
		String filePath = "";
		try {
			filePath = URLDecoder.decode(getClass().getResource(path).getPath(), "UTF-8");
			filePath = filePath.substring(1, filePath.length());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filePath;
	}
}
