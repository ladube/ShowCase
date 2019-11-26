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
	int progress = 190; 
	int c = 1;
	
	ArrayList<String> emotes;
	ArrayList<String> trainingQ;
	ArrayList<String> trainingA;
	ArrayList<Integer> emoteList = new ArrayList<Integer>();
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
		int n = 0;
		for(int i = 0; i < emotes.size(); i++) {
			if(!emotes.get(i).contentEquals(".")) {
				n = Integer.parseInt(emotes.get(i));
			}else {
				int tmpNum = n;
				emoteList.add(tmpNum);
			}
		}
		//Check data table
//		for(int i = 0; i < emoteList.size(); i++)
//			System.out.println(emoteList.get(i));
//		for(int i = 0; i < wordsQ.size(); i++) {
//			System.out.println(wordsQ.get(i)+" : "+countQ.get(i));
//		}
	}
	public void findRarest() {
		String rarest = inputArray.get(0);
			for(int i = 0; i < inputArray.size()-1; i++) {
				int curRarity = countQ.get(wordsQ.indexOf(inputArray.get(i)));
				int nextRarity = curRarity + 1;
				if(wordsQ.contains(inputArray.get(i))) {
					nextRarity = countQ.get(wordsQ.indexOf(inputArray.get(i+1)));
					System.out.println("Word: " + inputArray.get(i+1) + " Rarity: " + nextRarity);
				}
				if(nextRarity == 0) {
					return;
				}
				if(nextRarity < curRarity && !rareList.contains(inputArray.get(i+1))) {
					rarest = inputArray.get(i+1);
				}
			}
			rareList.add(rarest);
			int searchReturn = search(rarest);
			if(searchReturn > 1 && searchReturn != 0)
				findRarest();
	}
	public int search(String r) { 
		int num = 0;
		if(matches.size() == 0) {
			for(int i = 0; i < dataQ.size(); i++) {
				for(int j = 0; j < dataQ.get(i).size(); j++) {
					if(dataQ.get(i).contains(r)) {
						if(num != i) {
							num = i;
						    matches.add(num);
						}
					}
				}
			}
		}else if(matches.size() > 1) {
			int i = 0;
			while(i < matches.size()) {
					if(!dataQ.get(matches.get(i)).contains(r)) {
							num = i; 
							matches.remove(num);
					}else
						i++;
			}
		}
//		for(int j = 0; j < matches.size(); j++)
//			System.out.println("matches: " + matches.get(j));
		return matches.size();
	}
	public String getResponse() {
		outputArray.clear();
		output = "";
		//change array of strings into a string
		if(matches.isEmpty()) {
			Random rand = new Random();
			int r = rand.nextInt(4);
			if(r == 0) {
				if(inputArray.contains("you") || inputArray.contains("me"))
					output = "More input necessary before decision can be reached.";
				else
					output = "I don't know the answer to that, so maybe try asking Google-sama!";
			}else if(r == 1) {
				output = "English isn't my first language so I don't understand what you said.";
			}else if(r == 2){
				output = "Error 404: response not found.";
			}else {
				output = "Sorry, you're so cute it's making me nervous and my CPU is maxing out!";
			}
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
	public int findEmotion() {
		int emotion = 0;
		if(!matches.isEmpty()) {
			emotion = emoteList.get(matches.get(0));
		}
		if(emotion == 1 || emotion == 3 || emotion == 5 || emotion == 6) {
			progress = progress + 5;
		}else if(emotion == 2 || emotion == 4 || emotion == 7) {
			progress = progress - 5;
		}
		return emotion;
	}
	public void loadCatalogs(String p, int y) {
		String filePath = getPath(p);
		Path path = Paths.get(filePath);
		if(y == 1)
			emotes = new ArrayList<String>();
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
					emotes.addAll(t);
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
