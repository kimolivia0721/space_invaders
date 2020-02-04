//Olivia Kim's Space Invaders
//Realistic Space Invaders
//You can play Space Invaders

package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Block.SmallBlock;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch; //set all the variables
	ShapeRenderer shaperender;
	ShapeRenderer shape;
	Texture img; 
	Sprite mon1,mon2,mon3,mon11,mon22,mon33,ship,logo,press,shield,ufo,gameover,victory,shield1,shield2,shield3,
	shieldleft,shieldright,shieldtopr,shieldtopl;
	BitmapFont font;
	int score = 0;
	int fps = 0;
	int sec = 0;
	int beat = 0;
	int x = 50; //user default coordinate
	int y = 80; //user default coordinate
	int mx,my,ufosec,ufonum,ufodir; //used later on
	int ufox = 0; 
	int dir = 1;
	int lives = 3; //default live number
	int moving = 2;
	boolean start = true; 
	boolean ufoexist = false;
	Music music; //bgm
	Sound sound1; //user shoot sound
	Sound sound2; //aliens shoot sound
	private static ArrayList<Monster> mon1list; //first row monster
	private static ArrayList<Monster> mon2list; //second row monster
	private static ArrayList<Monster> mon3list; //third row monster 
	private static ArrayList<Monster> alllist; //all the moster in this list
	private static ArrayList<Block> blocklist; //all the blocks
	private static Random rand = new Random();
	private static Shots shot;
	
	@Override
	public void create () {
		mon1list = new ArrayList<Monster>();
		mon2list = new ArrayList<Monster>();
		mon3list = new ArrayList<Monster>();
		alllist = new ArrayList<Monster>();
		blocklist = new ArrayList<Block>();
		shaperender = new ShapeRenderer();
		shape = new ShapeRenderer();
		batch = new SpriteBatch(); 
		
		//convert all the images to sprites 
		img = new Texture("mon1.png");
		mon1 = new Sprite(img);
		img = new Texture("mon2.png");
		mon2 = new Sprite(img);
		img = new Texture("mon3.png");
		mon3 = new Sprite(img);
		img = new Texture("mon11.png");
		mon11 = new Sprite(img);
		img = new Texture("mon22.png");
		mon22 = new Sprite(img);
		img = new Texture("mon33.png");
		mon33 = new Sprite(img);
		img = new Texture("ship.png");
		ship = new Sprite(img);
		img = new Texture("logo.png");
		logo = new Sprite(img);
		img = new Texture("start.png");
		press = new Sprite(img);
		img = new Texture("shield.jpg");
		shield = new Sprite(img);
		img = new Texture("shield1.jpg");
		shield1 = new Sprite(img);
		img = new Texture("shield2.jpg");
		shield2 = new Sprite(img);
		img = new Texture("shield3.jpg");
		shield3 = new Sprite(img);
		img = new Texture("ufo.png");
		ufo = new Sprite(img);
		img = new Texture("gameover.jpg");
		gameover =new Sprite(img);
		img = new Texture("Victory.png");
		victory = new Sprite(img);
		img = new Texture("shieldleft.png");
		shieldleft = new Sprite(img);
		img = new Texture("shieldright.png");
		shieldright = new Sprite(img);
		img = new Texture("shieldtopl.png");
		shieldtopl = new Sprite(img);
		img = new Texture("shieldtopr.png");
		shieldtopr= new Sprite(img);
		music = Gdx.audio.newMusic(Gdx.files.internal("spaceinvaders1.mp3"));
		//sound1 = Gdx.audio.newSound(Gdx.files.internal("invaderkilled.mp3"));
		//sound2 = Gdx.audio.newSound(Gdx.files.internal("shoot.mp3"));
		
		ufonum = rand.nextInt(1000);
		Gdx.input.setInputProcessor(this);
		
		for(int i = 0; i<11; i++){ //create all the monsters and add them to ArrayLists
			alllist.add(new Monster((int)(i*50+mon3.getWidth())+33,550,3,30));
			mon3list.add(new Monster((int)(i*50+mon3.getWidth())+33,550,3,30));
			for(int w=0; w<2;w++){
				mon2list.add(new Monster((int)(i*50+mon2.getWidth())+25,450+w*50,2,10));
				alllist.add(new Monster((int)(i*50+mon1.getWidth())+25,350+w*50,1,10));
				mon1list.add(new Monster((int)(i*50+mon1.getWidth())+25,350+w*50,1,20));
				alllist.add(new Monster((int)(i*50+mon2.getWidth())+25,450+w*50,2,20));
			}
		}
		
		for(int i = 0; i<4; i++){
			blocklist.add(new Block((int)(i*175+mon2.getWidth())+50,150)); //4 blocks
		}
		for(int i = 0; i<4; i++){
			Block bl = blocklist.get(i);
			int width = (int)(shield.getWidth());
			int height = (int)(shield.getHeight());
			bl.createblock(bl.x,bl.y,width,height,0,1); //recleft
			bl.createblock(bl.x+width,bl.y,width,height,0,2);//recright
			bl.createblock(bl.x,bl.y+height,width,height+2,0,3);//rectoplefy
			bl.createblock(bl.x+width,bl.y+height,width,height+2,0,4);//rectopright
		}
		
		font = new BitmapFont(); //font loaded
		//font.setScale(4); My libgdx is not updated for this i think
		music.play(); 
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 0); //background set black
		Gdx.gl.glLineWidth(4);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if(alllist.size()>=1){ //user wins when you kill all the people
			if(lives>=0){//game over when there's no live
				if(start){
					start = beginning(); //loop this until the user presses play start
				}
				else{
					sec++;
					ufosec++;
					if(sec-fps>30){ //every 50 frame
						sec = 0;
						if(beat==0){ //sprite changes ;)
							beat = 1;
						}
						else if(beat==1){
							beat = 0;
						}
						moveene(ufoexist); //enemies move
					}
					if(ufosec-fps>1){
						if(!ufoexist){
							if(5>rand.nextInt(1000)){ //randomly spawns ufo
								ufoexist = true;
								if(rand.nextInt(2)==1){ //
									ufox = 0; 
									ufodir = 1; //direction depends on where the ufo starts
								}
								else{
									ufox = Gdx.graphics.getWidth(); 
									ufodir = -1;
								}
							}
						}
						else{
							if(ufox<-100 || ufox>Gdx.graphics.getWidth()){ //ufo disappears 
								ufoexist = false;
							}
							moveufo();
						}	
						ufosec = 0;
					}
					moveuser(); //user can move left and right 
					shotcreate();
					shootingene(x,y);
					shooting(ufox);
					touching(x,y);
					drawing(beat);
					if(ufoexist){
						drawufo();
					}
					drawene();
				}
			}
			else{
				if(lives<0){ //when there's no live
					gameover(); //GAME IS DONE
				}
			}
		}
		else if(alllist.size()==0){
			youwon();
		}
		dispose();
	}
	public void youwon(){ //display victory and score
		batch.begin();
		victory.setPosition(120, 200);
		victory.draw(batch);
		press.setPosition(280,200);
		press.draw(batch);
		batch.end();
		Rectangle rec = new Rectangle(280,350,press.getWidth(),press.getHeight()); 
		if(rec.contains(Gdx.input.getX(),Gdx.input.getY())){
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){ //game starts
				create();
				fps-=5;
			}
		}
		batch.begin();
		font.draw(batch, "Your Score:", 300, 200);
		font.draw(batch,Integer.toString(score),390,200);
		batch.end();
	}
	public void moveufo(){ //move the ufo in the direction
		if(ufoexist){
			ufox+= 5 * ufodir;
		}
	}
	public void immune(){ //resets to this position after the ship died
		x = 80;
		y = 80;
	}
	public void touching(int x,int y){ //checks if the user is touching the monster
		Rectangle rec1 = new Rectangle(x,Gdx.graphics.getHeight()-y,ship.getWidth(),ship.getHeight()); 
		for(Monster mon: alllist){
			if(mon.type==1){//different rectangle depending on the type
				if (mon.recit(mon1).overlaps(rec1)){
					lives-=1; //live decreases
					immune(); //reset position
					break; //you have to break the loop in order to not lose multiple lives
				}
			}
			else if(mon.type==2){
				if (mon.recit(mon2).overlaps(rec1)){
					lives-=1;
					immune();
					break;
				}
			}
			else if(mon.type==3){
				if (mon.recit(mon3).overlaps(rec1)){
					lives-=1;
					immune();
					break;
				}
			}
		}
	}
	public void moveene(boolean ufoexist){
		for(Monster mon: alllist){
			if(moving==1){
				if(mon.posx>Gdx.graphics.getWidth()-50){
					for(Monster mon2: alllist){
						mon2.down();
						dir = -1;
					}
					moving = 2;
					break;
				}
				else if(mon.posx<25){
					for(Monster mon2: alllist){
						mon2.down();
						dir = 1;
					}
					moving = 2;
					break;
				} 
			}
			else if(moving==2){
				moving = 1;
				break;
			}
		}
		if (moving==1){
			for(Monster mon: alllist){
				if(dir == -1){
					mon.leftmove();	
					moving = 1;
				}
				else if(dir==1){
					mon.rightmove();
					moving = 1;
				}
			}
		}
	}
	public void shotcreate(){ //shoots when pressed space
		if(Gdx.input.isKeyPressed(Keys.SPACE)){ 
			if(shot==null){ //the user can only shoot once at a time
				shot = new Shots((int)(x+ship.getWidth()/2),(int)(y+ship.getHeight()));
			}
		}
	}
	public void moveuser(){ //move the user left and right
		if(Gdx.input.isKeyPressed(Keys.LEFT)){ 
			if(x-5>0){ //user can't go over 
				x-=5;
			}
		}
		else if(Gdx.input.isKeyPressed(Keys.RIGHT)){
			if(x+5<Gdx.graphics.getWidth()-ship.getWidth()){	
				x+=5;
			}
		}
	}
	public boolean beginning(){ //title screen
		batch.begin();
		logo.setPosition(80,350); 
		logo.draw(batch);
		press.setPosition(280,200);
		press.draw(batch);
		Rectangle rec = new Rectangle(280,350,press.getWidth(),press.getHeight()); 
		if(rec.contains(Gdx.input.getX(),Gdx.input.getY())){ //if the user presses the start button
			if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){ //game starts
				batch.end();
				return false;
			}
		}
		else if(Gdx.input.isKeyPressed(Keys.ENTER)){ //press enter to start
			batch.end();
			return false;
		}
		batch.end();
		return true; //if not it stays on the title screen
	}
	public void drawing(int beat){
		batch.begin();
		shaperender.begin(ShapeType.Filled);
		//draws ship
		ship.setPosition(x,y);
		ship.draw(batch);
		//draws monsters
		for(Monster mon:alllist){
			if(beat == 0){
				if(mon.type==1){ //different sprites for different types
					mon1.setPosition(mon.posx,mon.posy);
					mon1.draw(batch);
				}
				else if(mon.type==2){
					mon2.setPosition(mon.posx,mon.posy);
					mon2.draw(batch);
				}
				else if(mon.type==3){
					mon3.setPosition(mon.posx,mon.posy);
					mon3.draw(batch);
				}
			}
			else if(beat == 1){ //different sprite 
				if(mon.type==1){
					mon11.setPosition(mon.posx,mon.posy);
					mon11.draw(batch);
				}
				else if(mon.type==2){
					mon22.setPosition(mon.posx,mon.posy);
					mon22.draw(batch);
				}
				else if(mon.type==3){
					mon33.setPosition(mon.posx,mon.posy);
					mon33.draw(batch);
				}
			}
	    }
		for(Block bl: blocklist){
			for(SmallBlock sb: bl.blocks){
				if(sb.hit==0){ //different sprites depending on how many times they got hit
					if(sb.pos==1){
						shieldleft.setPosition(sb.x,sb.y);
						shieldleft.draw(batch);
					}
					else if(sb.pos==2){
						shieldright.setPosition(sb.x,sb.y);
						shieldright.draw(batch);
					}
					else if(sb.pos==3){
						shieldtopl.setPosition(sb.x,sb.y);
						shieldtopl.draw(batch);
					}
					else if(sb.pos==4){
						shieldtopr.setPosition(sb.x,sb.y);
						shieldtopr.draw(batch);
					}
				}
				else if(sb.hit==1){
					shield1.setPosition(sb.x,sb.y);
					shield1.draw(batch);
				}
				else if(sb.hit==2){
					shield2.setPosition(sb.x,sb.y);
					shield2.draw(batch);
				}
				else if(sb.hit==3){
					shield3.setPosition(sb.x,sb.y);
					shield3.draw(batch);
				}
			}
		}
		for(int i = 0; i<lives; i++){ //draws how many lives are left
			ship.setPosition(i*50+60, 25);
			ship.draw(batch);
		}
		font.draw(batch, "Lives:", 20, 40);
		font.draw(batch,"Score:", 30, 630);
		font.draw(batch, Integer.toString(score), 80,630);
		batch.end();
		shaperender.end();
	}
	public void drawufo(){ //draws ufo if it exists
		batch.begin();
		ufo.setPosition(ufox,600);
		ufo.draw(batch);
		batch.end();
	}
	@Override
	public void dispose(){
		music.dispose();
		//sound1.dispose();
		//sound2.dispose();
	}
	public boolean killship(Rectangle rec1,int x, int y){ //kills affected by the enemy
		Rectangle rec2 = new Rectangle(x,Gdx.graphics.getHeight()-y,ship.getWidth(),ship.getHeight()); 
		if(rec1.overlaps(rec2)){
			lives-=1;//if the user collides with the enemy life decreases
			immune();//resets position
			return true;
		}
		return false;
	}
	public void drawene(){
		for(Monster mon: alllist){ //draw all the enemy's shots
			if(mon.shot){
				mon.moveene();
				Gdx.gl.glLineWidth(3);
				shaperender.begin(ShapeType.Line);
				shaperender.setColor(new Color(255,255,255,0));
				shaperender.line(mon.shotpoint.x, mon.shotpoint.y, mon.shotpoint.x,mon.shotpoint.y-25);
				shaperender.end();	
			}
		}
	}
	public void shootingene(int x,int y){ //enemies shoot randomly	
		for(Monster mon: alllist){
			if(!mon.shot){ //randomly creates a shot if the enemy doesn't have one
				if(1==rand.nextInt(10000)){
					mon.addshot(mon.posx+10,mon.posy); //make a shot in shotss class in monster
					mon.shot = true;
				}	
			}
			else{
				Rectangle rec1 = new Rectangle(mon.shotpoint.x,Gdx.graphics.getHeight()-mon.shotpoint.y,5,25);
				if(mon.shotpoint.y<0){ //if the shot reaches the bottom 
					mon.deleteshot();
					mon.shot = false; //delete and false
				}
				if(killship(rec1,x,y)){ //if the shot collides with the ship
					mon.deleteshot();
					mon.shot = false;//delete and false
				}
				for(int i = 0; i<blocklist.size();i++){
					for(int num = 0; num<blocklist.get(i).blocks.size();num++){ //go thru all the SmallBlock in blocklist
						if(rec1.overlaps(blocklist.get(i).getrec(num))){ //check if they overlap
							if(blocklist.get(i).blocks.get(num).hit==4){//delete the block if it got hit 4 times
								blocklist.get(i).blocks.remove(num);
								break; //break so you don't hit it twice
							}
							else if(blocklist.get(i).blocks.get(num).hit<4){//
								mon.shot = false;
								mon.deleteshot();
								blocklist.get(i).change(num);
								break;
							}
						}
					}
				}
			}
		}
	}
	public void shooting(int ufox){ //ship shooting
		if(shot!=null){
			shot.move();
			//sound1.play();
			shaperender.begin(ShapeType.Line);
			shaperender.setColor(new Color(0,255,0,0));
			shaperender.line(shot.x, shot.y, shot.x,shot.y+20); 
			shaperender.end();
			Rectangle rec1 = new Rectangle(shot.x,Gdx.graphics.getHeight()-shot.y,5,20);
			if(shot.y>Gdx.graphics.getHeight()+25){ //if the shot reaches the top it disappears
				shot=null;
			}
			kill(rec1,ufox); //check if it kills anything
		}	
	}
	public void kill(Rectangle rec1,int ufox){ //kills affected by the user
		for(int i = alllist.size()-1;i>-1;i--){
			Monster mon = alllist.get(i);
			if(mon.type==1){//Check because the sizes are different
				if (rec1.overlaps(mon.recit(mon1))){ 
					alllist.remove(mon);//remove monster if it overlaps
					shot = null;
					score+=mon.point; //score increase ;)
				}
			}
			if(mon.type==2){
				if (rec1.overlaps(mon.recit(mon2))){
					alllist.remove(mon);
					shot = null;
					score+=mon.point;
				}
			}
			if(mon.type==3){
				if (rec1.overlaps(mon.recit(mon3))){
					alllist.remove(mon);
					shot = null;
					score+=mon.point;
				}
			}
		}
		for(int i = 0; i<blocklist.size();i++){
			for(int num = 0; num<blocklist.get(i).blocks.size();num++){
				if(rec1.overlaps(blocklist.get(i).getrec(num))){//same thing is happening as in shootingene
					if(blocklist.get(i).blocks.get(num).hit==4){
						blocklist.get(i).blocks.remove(num);
						break;
					}
					else if(blocklist.get(i).blocks.get(num).hit<4){
						shot = null;
						blocklist.get(i).change(num); //increase hit number 
						break;
					}
				}
			}
		}
		if(ufoexist){ //only occur when ufo exists
			Rectangle recufo = new Rectangle(ufox,Gdx.graphics.getHeight()-600, ufo.getWidth(),ufo.getHeight());
			if(rec1.overlaps(recufo)){
				shot = null;
				score+=rand.nextInt(5)*10+50; //random score 
				ufoexist = false; //ufo doesn't exist anymore
			}
		}
	}
	public void gameover(){ //display game over and score
		batch.begin();
		gameover.setPosition(0, -200);
		gameover.draw(batch);
		font.draw(batch, "Your Score:", 300, 200);
		font.draw(batch,Integer.toString(score),390,200);
		batch.end();
	}
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
class Shots{ //shots for the ship
	int x,y;
	public Shots(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void move(){ //move up
		y+=7;
	}
}

class Block{
	int x,y;
	ArrayList<SmallBlock> blocks = new ArrayList<SmallBlock>();
	public Block(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void createblock(int x, int y, int width, int height,int hit,int pos){ //add small blocks
		blocks.add(new SmallBlock(x,y,width,height,hit,pos)); 
	}
	public Rectangle getrec(int i){  //return a rectangle according to the number that the user gives
		return new Rectangle(blocks.get(i).x,Gdx.graphics.getHeight()-blocks.get(i).y,blocks.get(i).width,blocks.get(i).height);
	}
	public void change(int i){ //add one hit
		blocks.get(i).hit+=1;
	}
	class SmallBlock{
		int x,y,width,height,hit,pos;
		public SmallBlock(int x, int y, int width, int height, int hit, int pos){
			this.x=x;
			this.y=y;
			this.width=width;
			this.height=height;
			this.hit=hit;
			this.pos = pos;
		}
	}
}

class Monster{
	int posx,posy,type,point;
	boolean shot;
	Shotss shotpoint;
	public Monster(int x,int y,int type,int point){
		posx = x; 
		posy = y;
		this.type = type;
		this.point = point;
		shot = false;
	}
	public void deleteshot(){
		shotpoint = null;
	}
	public void addshot(int x,int y){
		shotpoint = new Shotss(x,y); 
	}
	public void leftmove(){
		posx-=8;
	}
	public void rightmove(){
		posx+=8;
	}
	public void down(){
		posy -=20;
	}
	public Rectangle recit(Sprite pic){
		Rectangle rec = new Rectangle(posx,Gdx.graphics.getHeight()-posy,pic.getWidth(),pic.getHeight());
		return rec;
	}
	public void moveene(){
		shotpoint.move();
	}
	class Shotss{ //shots for the monster
		int x,y;
		public Shotss(int x, int y){
			this.x = x;
			this.y = y;
		}
		public void move(){
			y-=5;
		}	
	}
	
}

