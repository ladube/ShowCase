import java.util.ArrayList;
import processing.core.*;

public class UserInput {
	PFont f; 
	String word = "";
	String typed = "";
	ArrayList<String> typing = new ArrayList<String>();
	ArrayList<String> saved = new ArrayList<String>();
	
	public void displayInput() {
		
	}
	public void backSpace() {
		//typing.remove(typing.size()-1);
		word = word.substring(0, word.length()-1);
		typed = typed.substring(0,typed.length()-1);
	}
	public void stringToArray() {
		typed = typed + " ";
		typing.add(word); //adds each word as an element of the input ArrayList
		word = ""; //clears word
	}
	public ArrayList<String> enterInput() {
		typing.add(word); //adds the last word
		saved = typing;
			for(int i = 0; i < saved.size(); i++)
				System.out.print(saved.get(i) + " ");
			
		//clear all for next input
		typing.clear(); //clears typing Array
		typed = ""; //clears typing String
		word = ""; //clears word
		
		//send saved to be analyzed by the decision tree
		return saved;
	}
}
