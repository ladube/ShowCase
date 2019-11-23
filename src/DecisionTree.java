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
	ArrayList<String> wordsQ = new ArrayList<String>();
	ArrayList<Integer> countQ = new ArrayList<Integer>();
	ArrayList<String> wordsA = new ArrayList<String>();
	ArrayList<Integer> countA = new ArrayList<Integer>();
	
	String output = "";
	int progress = 100; 
	int c = 1;
	
	ArrayList<String> positive;
	ArrayList<String> negative;
	ArrayList<String> trainingQ;
	ArrayList<String> trainingA;
	ArrayList<ArrayList<String>> dataQ = new ArrayList<ArrayList<String>>();
	ArrayList<ArrayList<String>> dataA = new ArrayList<ArrayList<String>>();
	ArrayList<Integer> matches = new ArrayList<Integer>();
	ArrayList<String> rareList = new ArrayList<String>();
	
	Branch<String> root = new Branch<String>();
	
	public void setIn(ArrayList<String> input) {
		inputArray = input;
	}
	public void separateData() {
			ArrayList<String> curToken = new ArrayList<String>();
		for(int i = 0; i < trainingQ.size(); i++) {
			if(!wordsQ.contains(trainingQ.get(i))) {
				wordsQ.add(trainingQ.get(i));
				countQ.add(1);
			}else if(wordsQ.contains(trainingQ.get(i))) {
				c = countQ.get(wordsQ.indexOf(trainingQ.get(i))) + 1;
				countQ.set(wordsQ.indexOf(trainingQ.get(i)), c );
			}
			
			if(!trainingQ.get(i).contentEquals("?") && !trainingQ.get(i).contentEquals(".") && !trainingQ.get(i).contentEquals("!")) {
				curToken.add(trainingQ.get(i));
			}else{
				curToken.add(trainingQ.get(i));
				ArrayList<String> tmp = new ArrayList<String>();
				for(int j = 0; j < curToken.size(); j++)
					tmp.add(curToken.get(j)); 
				dataQ.add(tmp);
				curToken.clear();
			}
		}
		for(int i = 0; i < trainingA.size(); i++) {
			if(!trainingA.get(i).contentEquals("?") && !trainingA.get(i).contentEquals(".") && !trainingA.get(i).contentEquals("!")) {
				curToken.add(trainingA.get(i));
			}else{
				curToken.add(trainingA.get(i));
				ArrayList<String> tmp = new ArrayList<String>();
				for(int j = 0; j < curToken.size(); j++)
					tmp.add(curToken.get(j)); 
				dataA.add(tmp);
				curToken.clear();
			}
		}
		//Check data table
//		for(int i = 0; i < wordsQ.size(); i++) {
//			System.out.println(wordsQ.get(i)+" : "+countQ.get(i));
//		}
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
	public void findRarest() {
		String rarest = inputArray.get(0);
			for(int i = 0; i < inputArray.size()-1; i++) {
				int curRarity = countQ.get(wordsQ.indexOf(inputArray.get(i)));
				int nextRarity = countQ.get(wordsQ.indexOf(inputArray.get(i+1)));
				if(nextRarity < curRarity && !rareList.contains(inputArray.get(i+1))) {
					rarest = inputArray.get(i+1);
				}
			}
			System.out.println(rarest);
			rareList.add(rarest);
			System.out.println(search(rarest));
		//search(rarest);
		//send rarest to determine the response
	}
	public int search(String r) { 
		if(matches.size() == 0) {
			for(int i = 0; i < dataQ.size(); i++) {
				for(int j = 0; j < dataQ.get(i).size(); j++) {
					if(dataQ.get(i).contains(r)) {
						//add i only to an index of matches
						matches.add(i);
					}
				}
			}
		}else if(matches.size() > 1) {
			for(int i = 0; i < matches.size(); i++) {
				for(int j = 0; j < dataQ.get(matches.get(i)).size(); j++) {
					if(!dataQ.get(matches.get(i)).contains(r)) {
						matches.remove(i);
					}
				}
			}
		}
		return matches.size();
	}
	public String getResponse() {
		outputArray.clear();
		output = "";
		//change array of strings into a string
		if(matches.isEmpty()) {
			output = "????";
		}else {
			outputArray = dataA.get(matches.get(0));
			for(int i = 0; i < outputArray.size(); i++) {
				output = output + " " + outputArray.get(i);
			}
		}
		matches.clear(); //clear everything after you have found the result
		rareList.clear();
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
			trainingQ = new ArrayList<String>();
		else if(y == 3)
			trainingA = new ArrayList<String>();
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
					trainingQ.addAll(t);
				else if(y == 3)
					trainingA.addAll(t);
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
