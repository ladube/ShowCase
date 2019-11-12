import java.util.ArrayList;
import processing.core.*;
public class ShowcaseMain extends PApplet {
	
	UserInput input = new UserInput();
	DecisionTree dt = new DecisionTree();
	ArrayList<String> in = new ArrayList<String>();
	String name = "";
	int Case = 1; //1:op  2:play  3:win
	PImage opening, background, win, lose;
	PImage normal, happy, sad, mad, blink, chibi;
	
	public static void main(String[] args) {
		PApplet.main("ShowcaseMain");

	}
	public void settings() {
		size(960,540); //fullScreen();
	}
	public void setup() {
		opening = loadImage("opening.jpg");
		background = loadImage("backgroundsmall.png");
		normal = loadImage("normalsmall.png");
		//win = loadImage("win.jpg");
		//lose = loadImage("lose.jpg");
		
		//dt.loadCatalogs("data/positiveCatalog.txt");
		//dt.loadCatalogs("data/negativeCatalog.txt");
	}
	public void draw() {
		if(Case == 1) {
			image(opening,0,0);
			//fill(255, 158, 181);
			rect(width/2 - 75, height-125, 150, 70);
			fill(0,102,153);
			text("Enter your name to start:", width/2 - 70, height - 105);
			text(input.typed, width/2-65, height - 85);
			fill(255);
			//user input name
		}else if(Case == 2){
			image(background,0,0);
			image(normal,width-375,50);
			//draw the console
				fill(109, 247, 238, 150);
				rect(0, height-75, 100, 30, 0, 12, 0, 0);
				fill(255);
					text(name,6,height-60);
				rect(0, height-50, width, 50);
				fill(0,102,153);
				text(input.typed,3,height-30);//typed
				fill(255);
		}else { //Case changes to 3 when win/lose score reached
			//if(winDecision = true)
			image(win,0,0);
		}
	}
	public void keyPressed() {
		//typing
		if(key == '\n') { //user presses enter
			if(Case == 1) {
				name = input.word;
				System.out.println("name is: " + name);
				input.word = "";
				input.typed = "";
				Case = 2;
			}else {
			in = input.enterInput();
			//dt.setIn(in);
			}
		}else if(key == BACKSPACE) {
			input.backSpace();
		}else if(key == ' '){
			input.stringToArray();
		}else {
			input.word = input.word + key; //Concatenate
			input.typed = input.typed + key;
		}
	}
	public void mouseClicked() {

	}

}
