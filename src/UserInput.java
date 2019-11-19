import java.util.ArrayList;
import processing.core.*;

public class UserInput {
	PFont f; 
	String word = "";
	String typed = "";
	int QorA = 0; // 1 for question, 0 for statement
	ArrayList<String> typing = new ArrayList<String>();
	ArrayList<String> saved = new ArrayList<String>();
	
	public void displayInput() {
		
	}
	public void backSpace() {
		//typing.remove(typing.size()-1);
		if(word.length() > 0) {
		word = word.substring(0, word.length()-1);
		typed = typed.substring(0,typed.length()-1);
		}
	}
	public void stringToArray() {
		typed = typed + " ";
		typing.add(word); //adds each word as an element of the input ArrayList
		word = ""; //clears word
	}
	public ArrayList<String> enterInput() {
		if(word.endsWith("?") || word.endsWith(".") || word.endsWith("!")) {
			if(word.endsWith("?"))
				QorA = 1;
			else if(word.endsWith(".") || word.endsWith("!"))
				QorA = 0;
			word = word.substring(0, word.length() -1);
		}
		System.out.println(word);
		typing.add(word); //adds the last word
		saved.clear(); //clears the previous saved user input
		for(int i = 0; i < typing.size(); i++) 
			saved.add(typing.get(i));
		//clear all for next input
		typing.clear(); //clears typing Array
		typed = ""; //clears typing String
		word = ""; //clears word
		
		//send saved to be analyzed by the decision tree
		return saved;
	}
	public int getTreeType() {
		return QorA;
	}
}
