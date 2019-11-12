import processing.core.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.jaunt.JauntException;

public class DecisionTree {
	ArrayList<String> inputArray = new ArrayList<String>();
	ArrayList<String> positive;
	ArrayList<String> negative = new ArrayList<String>();
	
	public void setIn(ArrayList<String> input) {
		inputArray = input;
		System.out.println("\n" + "decision tree accessed :) ");
		findEmotion();
	}
	public void findEmotion() {
		for(int i=0; i<inputArray.size(); i++) {
			if(positive.contains(inputArray.get(i))){
				System.out.println("<3");
			}else if(negative.contains(inputArray.get(i))){
				System.out.println(":c");
			}
		}
	}
	
	public void loadCatalogs(String p) {
		String filePath = getPath(p);
		Path path = Paths.get(filePath);
		positive = new ArrayList<String>();
		try {
			List<String> lines = Files.readAllLines(path);
			for(int i=0; i<lines.size(); i++) {
				TextTokenizer tokenizer = new TextTokenizer(lines.get(i));
				Set<String> t = tokenizer.parseSearchText();
				positive.addAll(t);
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
