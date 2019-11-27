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
		//size(960,540); //
		fullScreen();
	}
	public void setup() {
		opening = loadImage("opening.jpg");
		background = loadImage("backgroundfit.jpg");
		win = loadImage("winfit.jpg");
		lose = loadImage("losefit.jpg");
		end = loadImage("end.jpg");
		normal = loadImage("normal75.png");
		blink = loadImage("blink75.png");
		shy = loadImage("shy75.png");
		happy = loadImage("talk75.png");
        sad = loadImage("sad75.png");
		mad = loadImage("mad75.png");
		uwu = loadImage("uwu75.png");
		love = loadImage("inlove75.png");
		upset = loadImage("upset75.png");
		
		dt.loadCatalogs("data/emoteCatalog", 1);
		dt.loadCatalogs("data/questionCatalog", 2);
		dt.loadCatalogs("data/answerCatalog", 3);
		//dt.loadCatalogs("data/statementCatalog", 3);
		
	//	file1 = new SoundFile(this, "opening.mp3");
	//	file2 = new SoundFile(this, "soundtrack.mp3");
		
		dt.separateData(); //after separating data it should go directly to train
		//music();
	}
	public void draw() {
		textSize(24);
		if(Case == 1) {
			image(opening,0,0);
			//fill(255, 158, 181);
			rect(width/2 - 150, height-130, 300, 80);
			fill(0,102,153);
			text("Enter your name to start:", width/2 - 147, height - 105);
			fill(0,102,153,175);
			text(input.typed, width/2-138, height - 70);
			fill(255);
			//user input name
		}else if(Case == 2){
			image(background,3,0);
			if(e == 0) {
			    if(!nbcycle())
			    	image(normal,width/2 - 100, -60); 
			    else
			    	image(blink, width/2 - 100, -60);
			}else if(e == 1) {
				image(shy, width/2 - 100,-60); 
			}else if(e == 2) {
				image(sad, width/2 - 100,-60); 
			}else if(e == 3){
				image(happy, width/2 - 100,-60); 
			}else if(e == 4) {
				image(mad, width/2 - 100,-60); 
			}else if(e == 5) {
				image(uwu, width/2 - 100,-60); 
			}else if(e == 6) {
				image(love, width/2 - 100,-60); 
			}else {
				image(upset, width/2 - 100,-60); 
			}
			//draw the progress bar
			stroke(255);
			rect(100, 100, 50, 450, 25, 25, 25, 25);
			int p = dt.progress;
			fill(255, 120, 133, 150);
			rect(102, 300 - p, 44, 245 + p, 0, 0, 20, 20);
			if(p >= 225 ) {
				verdict = true;
				Case = 3;
			}else if(p <= -225) {
				verdict = false; 
				Case = 3;
			}
			//draw the console
			if(speaker == false) {
				fill(109, 247, 238, 150);
				rect(0, height-130, 150, 50, 0, 12, 0, 0);
				fill(255);
					text(name,6,height-107);
				rect(0, height-100, width, 125);
				fill(0,102,153);
				text(input.typed,4,height-65);//typed
				fill(255);
			}else if(speaker == true) {
				fill(255, 120, 133, 150);
				rect(0, height-130, 150, 50, 0, 12, 0, 0);
				fill(255);
					text("E-rika",6,height-107);
				rect(0, height-100, width, 125);
				fill(0,102,153);
				text(out,4,height-65f);//typed
				fill(255);
			}
				
		}else{ //Case changes to 3 when win/lose score reached
			if(verdict)//if(winDecision = true)
				image(win,0,0);
			else if(!verdict)
				image(lose,0,0);
			fill(0);
			text("Press ESC to exit game", width/2 + 100, height - 30);
			fill(255);
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
