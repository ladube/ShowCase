import java.util.ArrayList;
import java.util.Random;

import processing.core.*;
import processing.sound.*;
public class ShowcaseMain extends PApplet {
	
	UserInput input = new UserInput();
	DecisionTree dt = new DecisionTree();
	ArrayList<String> in = new ArrayList<String>();
	String out = "";
	String name = "";
	int Case = 1; //1:op  2:play  3:win/lose 4:end
	int e = 0; //0:normal 1:shy 2:sad 3:happy 4:mad 5:uwu 6:love 7:upset
	int cycle = 0;
    SoundFile file1, file2;
	boolean speaker = false;
	boolean verdict = false;
	PImage opening, background, win, lose, end;
	PImage normal, blink, shy, sad, happy, mad, uwu, love, upset;
	
	public static void main(String[] args) {
		PApplet.main("ShowcaseMain");

	}
	public void settings() {
		size(960,540); //fullScreen();
	}
	public void setup() {
		opening = loadImage("opening.jpg");
		background = loadImage("backgroundsmall.png");
		win = loadImage("winsmall.jpg");
		lose = loadImage("losesmall.jpg");
		end = loadImage("endsmall.jpg");
		normal = loadImage("normal75.png");
		blink = loadImage("blink75.png");
		shy = loadImage("shy75.png");
		happy = loadImage("talk75.png");
		//sad = loadImage("sad75.png");
		//mad = loadImage("mad75.png");
		//uwu = loadImage("uwu75.png");
		love = loadImage("inlove75.png");
		//upset = loadImage("upset75.png");
		
		dt.loadCatalogs("data/emoteCatalog", 1);
		dt.loadCatalogs("data/questionCatalog", 2);
		dt.loadCatalogs("data/answerCatalog", 3);
		//dt.loadCatalogs("data/statementCatalog", 3);
		
		file1 = new SoundFile(this, "opening.mp3");
		file2 = new SoundFile(this, "soundtrack.mp3");
		
		dt.separateData(); //after separating data it should go directly to train
		music();
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
			image(background,3,0);
			if(e == 0) {
			    if(!nbcycle())
			    	image(normal,width/2 - 100, -80); 
			    else
			    	image(blink, width/2 - 100, -80);
			}else if(e == 1) {
				image(shy, width/2 - 100,-80); 
			}else if(e == 2) {
				image(happy, width/2 - 100,-80); 
			}else {
				image(love, width/2 - 100,-80); 
			}
			//draw the progress bar
			stroke(255);
			rect(50, 50, 40, 400, 20, 20, 20, 20);
			int p = dt.progress;
			fill(255, 120, 133, 150);
			rect(53, 255 - p, 34, 193 + p, 0, 0, 20, 20);
			if(p >= 200 ) {
				verdict = true;
				Case = 3;
			}else if(p <= 0) {
				verdict = false; 
				Case = 3;
			}
			//draw the console
			if(speaker == false) {
				fill(109, 247, 238, 150);
				rect(0, height-75, 100, 30, 0, 12, 0, 0);
				fill(255);
					text(name,6,height-60);
				rect(0, height-50, width, 50);
				fill(0,102,153);
				text(input.typed,3,height-30);//typed
				fill(255);
			}else if(speaker == true) {
				fill(255, 120, 133, 150);
				rect(0, height-75, 100, 30, 0, 12, 0, 0);
				fill(255);
					text("E-rika",6,height-60);
				rect(0, height-50, width, 50);
				fill(0,102,153);
				text(out,3,height-30);//typed
				fill(255);
			}
				
		}else if(Case == 3){ //Case changes to 3 when win/lose score reached
			if(verdict)//if(winDecision = true)
				image(win,0,0);
			else if(!verdict)
				image(lose,0,0);
			fill(0);
			text("To move on, click anywhere -->", width/2, 0);
			fill(255);
		}else {
			image(end,0,0);
		}
	}
	public void music() {
		if(Case == 2) {
			System.out.println("main music accessed");
			//file2.loop();
		}else {
			System.out.println("openning/closing music accessed");
				//file1.loop();
		}
	}
	public boolean nbcycle() {
		boolean nb = false;
//		Random rand = new Random();
//		int r = rand.nextInt(1000);
//		if(r%17 == 0)
//			nb = true;
//		if(nb == true && cycle <= 10) {
//			cycle++;
//			return true;
//		}
//		cycle = 0;
		return nb;
	}
	public void keyPressed() {
		//typing
		speaker = false;
		if(key == '\n') { //user presses enter
			if(Case == 1) {
				name = input.word;
				input.word = "";
				input.typed = "";
				Case = 2;
				music();
			}else if(Case == 2) {
			in = input.enterInput();
			dt.setIn(in);
			//emotion = dt.findEmotion();
			dt.findRarest();
			speaker = true;
			e = dt.findEmotion();
			out = dt.getResponse();
			}else if(Case == 3){
				Case = 4;
			}
		}else if(key == BACKSPACE) {
			input.backSpace();
		}else if(key == ' '){
			if(Case == 1) {
				input.word = input.word + key;
			    input.typed = input.typed + key;
			}else {
				input.stringToArray();
			}
		}else {
			if(keyCode != SHIFT) {
				input.word = input.word + key; //Concatenate
				input.typed = input.typed + key;
			}
		}
	}
	public void mouseClicked() {
        if(Case == 3) {
        	Case = 4;
        }
	}

}
