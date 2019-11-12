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
	ArrayList<String> positive;
	ArrayList<String> negative;
	
	public void setIn(ArrayList<String> input) {
		inputArray = input;
		System.out.println("\n" + "decision tree accessed");
		findEmotion();
	}
	public void findEmotion() {
		for(int i=0; i<inputArray.size(); i++) {
			if(negative.contains(inputArray.get(i))||(negative.contains(inputArray.get(i)) && positive.contains(inputArray.get(i)))){
				System.out.println(":c");
			}else if(positive.contains(inputArray.get(i)) && !(negative.contains(inputArray.get(i)))){
				System.out.println(":)");
			}
		}
	}
	public String getResponse() {
		outputArray.clear();
		output = "";
		//calls Response class using index found in decision tree
		//this is just a test example
			outputArray.add("response");
		//change array of strings into a string
		for(int i = 0; i < outputArray.size(); i++) {
			System.out.print(outputArray.get(i));
			output = output + outputArray.get(i);
		}
		System.out.println("\n" + "Response accessed");
		return output;
	}
	public void loadCatalogs(String p, int y) {
		String filePath = getPath(p);
		Path path = Paths.get(filePath);
		if(y == 1)
			positive = new ArrayList<String>();
		else if(y == 0)
			negative = new ArrayList<String>();
		try {
			List<String> lines = Files.readAllLines(path);
			for(int i=0; i<lines.size(); i++) {
				TextTokenizer tokenizer = new TextTokenizer(lines.get(i));
				Set<String> t = tokenizer.parseSearchText();
				if(y == 1)
					positive.addAll(t);
				else if(y == 0)
					negative.addAll(t);
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
