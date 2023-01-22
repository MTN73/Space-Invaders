import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SpaceInvaders extends PApplet {

/*******************
*Jason Elisei      *
*ICS 3U1           *
*Assignment 4      *
*Space invaders    *
*with levels       *
*******************/

PImage tank; //image variables for objects in game
PImage invader; //
PImage invader2; //
PImage spaceship; //
PImage invaderBIG; //
PImage gameover; //

int state = 0; //variable to control what screen is shown

boolean leftArrow = false; //boolean variables for arrow presses
boolean rightArrow = false; //

boolean spacePressed = false; //boolean variable for space press

int tankXpos = 700; //variable for tank x position
int tankRate = 10; //variable for tank speed

static int tankWidth = 80; //static value of the tank's width

int animationState = 0; //variable for invader animation

int laserSpeed = 4; //variable for lazer speed
int lazerXpos = 0; //variable for lazer x position
int lazerY = 0; //variable for lazer y position

int timer = 0; //timer variables
int timerLap = 0; //timer "lap" variable

boolean canShoot = true ; //boolean for whether or not the tank can shoot
boolean laserVisible = false; //boolean that controls if the lazer is drawn to the screen

boolean movingRight = true; //boolean variable to keep track of the aliens' direction of movement

int arrayLength = 14; //length of all arrays, equal to the number of columns in the 'matrix' of aliens

boolean invadersAlive[][] = {new boolean[arrayLength], //2d array to keep track of if aliens are still alive
  new boolean[arrayLength], 
  new boolean[arrayLength]};


int invadersX[][] = {new int[arrayLength], //2d array to keep track of alien x positions
  new int[arrayLength], 
  new int[arrayLength]};

int invadersY[][] = {new int[arrayLength], //2d array to keep track of alien x positions
  new int[arrayLength], 
  new int[arrayLength]};

float starsY[] = new float[20]; //array to keep track of the y positions of randomly generated stars
float starsX[] = new float[20]; //array to keep track of the x positions of randomly generated stars

int min = 1400; //variable used to find maximum values in arrays
int max = 0; //variable used to find minimum values in arrays

int maxY = 0; //variable used to find maximum y value of aliens

boolean gameOver = false; //boolean used when game is over

int collisionRow; //variables used to keep track of what alien needs to die when laser hits one
int collisionColumn; //

int score = 0; //variable used for score

int lazerHealth = 1; //variable that makes sure that lazer can only shoot one alien at a time

boolean mouseIsClicked = false; //boolean to store whether or not mouse is clicked

float randomStarPosX; //variables for randomly generated star positions
float randomStarPosY; //

boolean anyLeft = true; //boolean to store if there are any aliens still alive
boolean win = false; //boolean to store if the player won or not
boolean lose = false; //boolean variable to store whether or not the player lost

int level = 1; //variable for the current level

int shipLap = 0; //variable to keep track of when the spaceship was last seen

int spaceX = -100; //variable for spaceship x position

int highscore[]; //array to keep track of highscore

String path; //string variable to store the path to the text file that stores the highscore

int randomPoints; //variable to keep track of how many points the player 

int spaceshipHit = -2000; //variable to store when the spaceship was hit
int spaceshipHitX; //variable to store where the spaceship was hit

boolean powerup = false; //boolean to store whether or not the powerup is activated

boolean doubleClick = false; //variable to fix a bug*


//* the bug was that when you pressed the 'menu' button on the game over screen, the 'start' button on the homescreen would automatically be pressed and you 
// would just restart the game. This was because the two buttons were in the same position on the screen.

public void setup() {

  

  background(0xff000000);

  path = dataPath("hs.txt"); //set path variable to the path of the highscore text file

  tank = loadImage("SItankSMALL.jpg"); //load and resize all the images

  tank.resize(tankWidth, 70); //

  invader = loadImage("space_invader.jpg"); //

  invader.resize(50, 50); //

  invader2 = loadImage("space.png"); //

  invader2.resize(50, 50); //
  
  spaceship = loadImage("spaceship.png"); //
  
  spaceship.resize(80,50); //

  invaderBIG = loadImage("space_invader.jpg"); //
  
  invaderBIG.resize(70,70); //
  
  gameover = loadImage("gameover.png"); //
  
  gameover.resize(800,300); //
  
  for (int r = 0; r<3; r++) {
    for (int c = 0; c<arrayLength; c++) {

      invadersAlive[r][c] = true; // set all values in boolean array to true
    }
  }

  for (int r = 0; r<3; r++) {
    for (int c = 0; c<arrayLength; c++) {

      invadersX[r][c] = 130+(80*c); //initializes 2d array which stores alien x positions
    }
  }

  for (int r = 0; r<3; r++) {
    for (int c = 0; c<arrayLength; c++) {

      invadersY[r][c] = 100 * (r+1); //initializes 2d array that stores alien y positions
    }
  }

  for (int i = 0; i<20; i++) {

    randomStarPosX = random(100, 1300); //generates random positions for the stars in the background

    randomStarPosY = random(100, 700); //

    starsY[i] = randomStarPosY; //

    starsX[i] = randomStarPosX; //
  }
}

public void draw() {

  if (state == 0) {

    homeScreen(); //homescreen / splash screen
  }
  if (state == 1) {

    game(); //actual game
  }
  if (state == 2) {

    instructions(); //instructions screen
  }
}

public void keyPressed() {

  if (keyCode == 39) {

    rightArrow = true;
  }
  if (keyCode == 37) {

    leftArrow = true;
  }
  if (keyCode == 32) {

    spacePressed = true;
  }

  // 39 - right arrow
  // 37 - left arrow
  // 32 - spacebar
}
public void keyReleased() {

  if (keyCode == 39) {

    rightArrow = false;
  }
  if (keyCode == 37) {

    leftArrow = false;
  }

  if (keyCode == 32) {

    spacePressed = false;
  }
}

public void mousePressed() {

  mouseIsClicked = true;
}
public void mouseReleased() {

  mouseIsClicked = false;
  
  doubleClick = false; // makes it so that you have to click and release to be able to click something else again. This solved the bug mentioned above.
}

public void homeScreen() {

  background(50); //grey background

  fill(0xff000000); //black

  noStroke();

  rect(50, 50, 1300, 700); //black rectangle in center to form grey border

  drawStars(); //draws stars under all the text and buttons

  textSize(100);

  fill(0xffFFFFFF); //white

  stroke(0xffFFFFFF); //white

  text("SPACE INVADERS", 330, 300);

  textSize(60);

  if (mouseX > 580 && mouseX < 830 && mouseY > 400 && mouseY < 500) { //if mouse is on 'start' button

    fill(0xffFFFFFF); //white
    rect(580, 400, 250, 100, 30);
    fill(0xff000000); //black
    text("START", 610, 470);
  } else {

    fill(0xff000000); //black
    rect(580, 400, 250, 100, 30);
    fill(0xffFFFFFF); //white
    text("START", 610, 470);
  }

  if (mouseIsClicked == true && mouseX > 580 && mouseX < 830 && mouseY > 400 && mouseY < 500 && doubleClick == false) { //if 'start' button is clicked

    init(); //initialize all variables
    state = 1; //set the state to 1
    score = 0; //reset the score
    level = 1; //reset the level
  }

  textSize(35);

  if (mouseX > 580 && mouseX < 830 && mouseY > 550 && mouseY < 650) { //if the mouse is on the 'instructions' button
    fill(0xffFFFFFF); //white
    rect(580, 550, 250, 100, 30);
    fill(0xff000000); //black
    text("Instructions", 610, 610);
    if (mouseIsClicked == true) { // if the mouse is clicked

      state = 2; //set the state to 2 (instructions screen)
    }
  } else {
    fill(0xff000000);//black
    rect(580, 550, 250, 100, 30);
    fill(0xffFFFFFF);//white
    text("Instructions", 610, 610);
  }
  fill(0xffFFFFFF); //white
  text("v.3.3",80,720); //prints the version in the bottom left
}

public void instructions() { //instructions screen

  background(0xff000000); //black background

  
  fill(0xff000000); //black
  stroke(0xffFFFFFF); //white stroke
  
  rect(1,1,600,400); //rectangles around each section of the instructions
  rect(600,1,798,400); //
  rect(1,400,1397,399); //
  
  image(gameover,15,450); //image in bottom left that shows when the game is over
  
  fill(0xffFFFFFF); //white
  text("Controls", 10,50); //the titles for all the sections on the instructions page
  text("Points",610,50); //
  text("Winning & Losing",15,450); //
  
  image(invaderBIG,750,70); //larger image of an alien
  image(invaderBIG,750,195); //same image, different location
  rect(830,185,5,100); //wall beside alien to show an alien reaching the edge of the screen
  image(spaceship,750,330); //larger image of the spaceship
  
  fill(0xff000000); //black
  rect(50,100,100,100,20); //rectangles that surround the arrows in the "control" section
  rect(200,100,100,100,20); //
  
  rect(80,265,200,70,20); //rectangle around "SPACE"
  
  fill(0xffFFFFFF); //white
  rect(80,148,50,5); //"stems" of arrows in "contol" section
  rect(220,148,50,5); //
  triangle(100,135,100,165,70,150); // left triangle
  triangle(250,135,250,165,280,150); //right triangle
  
  text("SPACE",102,320); //additional text for "control" section
  text(":",340,165); //
  text(":",340,315); //
  text("+",155,165); //
  
  textSize(32);
  text("Move tank",400,165); //
  text("Shoot lazer",400,310); //
  
  
  fill(0xffFFFFFF); //white
  text("+50 (Hit alien)",850,120); //text showing points gained/lost for each event
  text("-50 (Aliens reach edge)",845,250); //
  text("+100/200/300 (Hit spaceship)",850,340); //
  text("(Gives 1.5 second powerup)",885,385); //

  text("Aliens reach bottom : game over",165,775); //text for bottom section of instructions page
  text("Hit all the aliens to move to the",855,550); //
  text("next level. Each level the aliens",855,600); //
  text("and the spaceship move faster.",855,650); //
  
  rect(820,450,5,300,20); //divider between losing condition and winning condition
  
  exitButton(); //exit button in top right corner
  
}

public void drawTank() {

  image(tank, tankXpos, 700); //draw tank
}

public void moveTank() {

  if (rightArrow == true && tankXpos < 1400 - tankWidth) { //if right arrow pressed and tank not at the right side of the screen

    tankXpos = tankXpos + tankRate; //move tank right by 10
  }
  if (leftArrow == true && tankXpos > 0) { //if left arrow pressed and tank not at left edge of the screen

    tankXpos = tankXpos - tankRate; //move tank left by 10
  }
}

public void spaceship(){ //method for the spaceship

  timer = millis(); //updates timer
  
 if(timer - shipLap > 15000 && invadersY[0][0] > 150){ //if its been more than 15 secs since the spaceship last passed by and the top row of aliens is lower than 150px
  
   shipLap = timer; //update shipLap variable
   spaceX = width; //set the x position of the spaceship to be at the right edge of the screen
   
 }
 
image(spaceship,spaceX,100); //draw spaceship

if(abs(lazerXpos - spaceX) < 40 && abs(lazerY - 150) < 25){ //if the lazer is less than 40px away from the center of the spaceship on the x axis
                                                              //and the lazer is less than 25px away from the center of the spaceship in the y axis
   
    randomPoints = round(random(1,3));
    randomPoints = randomPoints*100; //set the random points you get from the spaceship to be 100,200, or 300
    score += randomPoints; //add that amount of points to the score

    spaceshipHit = millis(); //update the time that the spaceship was hit to be millis()
    spaceshipHitX = spaceX; //update where the spaceship was hit to be where the spaceship is now
    spaceX = -100; // move the spaceship offscreen (I could have done this a better way)
    
  }
 
 spaceX -= level + 2; //spaceship moves left ; speed depends on level
  
if(timer-spaceshipHit < 800){ // if it's been less than 0.8 seconds since the spaceship was hit
      
  text(randomPoints,spaceshipHitX,120); //print how many points the player got from hitting the spaceship
      
    }  
    
if(timer-spaceshipHit < 1500){ //if it's been less than 1.5 seconds since the spaceship was hit
  
 powerup = true; //activate the powerup
 
 text("POWER UP",40,700); //print "POWER UP" in the bottom left corner of the screen
 
 fill(0xff000000); //black
 stroke(0xffFFFFFF); //white
 rect(60,730,150,30); //outline for bar that shows how much time is left in the powerup
 fill(0xffFFFFFF); //white
 rect(62,732,150-((timer-spaceshipHit)/10),26); //moving part of the bar that shows how much time is left in the powerup
  
}
else{
 
  powerup = false; //otherwise disable the powerup
  
}
  
}


public void shootLaser() {

  if (spacePressed == true && canShoot == true) { //if the spacebar is pressed and you are allowed to shoot

    lazerXpos = tankXpos + tankWidth/2 - 5; //set the lazer's x position to be where the tank's cannon is

    laserVisible = true; //make the lazer visible

    lazerHealth = 1; //give the lazer 1 health

    lazerY = height-150; //set the lazer's y position to the top of the tank's cannon

    canShoot = false; //make it so you can't shoot again (for now)
  }
}
public void drawLaser() {

  if (laserVisible == true) { //if the lazer is supposed to be visible

    fill(0xffFFFFFF); //white

    rect(lazerXpos, lazerY, 10, 50, 50); //draw the lazer
  }
}
public void moveLaser() {

  if (laserVisible == true) { //if the lazer is supposed to be visible
if(powerup == false){ //if the powerup is not activated

      lazerY = lazerY - 15; //move the lazer up by 15
}
else if(powerup == true){ //if the powerup is enabled
 
  lazerY = lazerY - 30; //move the lazer up by 30
  
}
  }

  if (lazerY < 90) { //if the lazer (almost) reaches the top of the screen

    laserVisible = false; //make the lazer 'invisible'
    canShoot = true; //let the player shoot again
    
  }
}

public void drawInvaders() { //method for drawing the aliens

  for (int r = 0; r<3; r++) { //loop through the whole 2d array of alien x positions
    for (int c = 0; c<arrayLength; c++) {

      if (animationState == 0) {

        if (invadersAlive[r][c] == true) {

          image(invader, invadersX[r][c], invadersY[r][c]); //draw invader (state 0)
        }
      } else if (animationState == 1) {

        if (invadersAlive[r][c] == true) { 

          image(invader2, invadersX[r][c], invadersY[r][c]); //draw invader (state 1)
        } 
      }
    }
  }

  timer = millis(); //update timer
  
  if (timer-timerLap > 500/level) {  //if it's been 0.5 seconds since the aliens last moved

    timerLap = timer; //update the timerLap variable

    if (animationState == 0) { //invert the animationState

      animationState = 1;
    } else {

      animationState = 0;
    }

    for (int r = 0; r<3; r++) { //loop through the 2d array of alien x positions
      for (int c = 0; c<arrayLength; c++) {

        if (movingRight == true) { // if the aliens are moving right

          invadersX[r][c] = invadersX[r][c] + 20; //increase each value in the array by 20
          
        } else {

          invadersX[r][c] = invadersX[r][c] - 20; //otherwise decrease each value in the array by 20
        }
      }
    }
  }
}

public void drawScore() { //method for drawing the score

  fill(0xffFFFFFF); //white

  textSize(40);

  text("Score : ", 50, 50);

  text(score, 200, 50); //print the score

  text("Level : " + level, 350, 50); // print the level

  highscore = PApplet.parseInt(loadStrings(path)); //load the highscore from 'hs.txt'. It's path is already stored in the path variable
  
  text("Highscore : " + highscore[0], 550,50); //print the highscore
  
  if(lose == true && score > highscore[0]){ //if the player has lost and the score is higher than the highscore
    
    highscore[0] = score; //set the highscore to the score
    
    saveStrings(path,str(highscore)); //save the score to the external file
    
    
}
}
public void checkInvadersHitWall() { //method to check if the aliens have reached either edge of the screen

  for (int r = 0; r<3; r++) { //loop through the 2d array of x positions and find the highest and lowest value
    for (int c = 0; c<arrayLength; c++) { 

      if (invadersX[r][c] > max && invadersAlive[r][c] == true) { //if the value at the current index is greater than the highest value so far
                                                                  //and the alien at the current position is still alive

        max = invadersX[r][c]; //set the max variable the the value at the current index
      }
      if (invadersX[r][c] < min && invadersAlive[r][c] == true) { //if the value at the current index is less than the lowest value so far
                                                                  //and the alien at the current position is still alive

        min = invadersX[r][c]; //set the min variable to the value at the current index
      }
    }
  }
  if (movingRight == true && max > 1320) { //if the aliens are moving right and the rightmost alien that is alive has a position greater than 1320

    if (score!=0) { //if the score isn't 0
      score -= 50; //decrease the score by 50
    }

    for (int r = 0; r<3; r++) { //loop through the entire 2d array of alien y positions
      for (int c = 0; c<arrayLength; c++) { 

        invadersY[r][c] = invadersY[r][c] + 40; //increase each value by 40
      }
    }
    min = 1400; //reset max and min variables for next time
    max = 0; //
    movingRight = false; //make the aliens start moving left
    
  } else if (movingRight == false && min < 50) { //if the aliens are moving left and the leftmost alien that is alive has a position less than 50
    if (score!=0) { //if the score isn't 0
      score-=50; //decrease the score by 50
    }

    for (int r = 0; r<3; r++) { //loop through the entire 2d array of alien y positions
      for (int c = 0; c<arrayLength; c++) { 

        invadersY[r][c] = invadersY[r][c] + 40; //increase each value by 40
      }
    }
    min = 1400; //reset max and min for next time
    max = 0; //
    movingRight = true; //make the aliens start moving right
  }
}

public void checkGameOver() {


  for (int r = 0; r<3; r++) { //loop through the entire 2d array of alien y positions
    for (int c = 0; c<arrayLength; c++) { 

      if (invadersY[r][c] > maxY && invadersAlive[r][c] == true) { //if the value at the current position has a value greater than the highest value so far
                                                                   //and the alien at this position is still alive

        maxY = invadersY[r][c]; //set the maxY variable to the current value
      }
    }
  }

  if (maxY > 650) { //if the highest y position of the aliens that are alive is greater than 650

    gameOver = true; //it's gameover
    lose = true; //the player has lost
    
  } else {

    maxY = 0; //otherwise reset maxY for next time
  }
  for (int r = 0; r<3; r++) { //loop through the entire boolean 2d array that stores whether or not the aliens are alive
    for (int c = 0; c<arrayLength; c++) {

      if (invadersAlive[r][c] == true) { //if the alien at the current position is still alive

        anyLeft = true; //there is/are alien(s) left
      }
    }
  }
  if (anyLeft == false) { //if there aren't any aliens left

    gameOver = true; //it's game over
    win = true; //the player wins
    
  } else if (anyLeft == true) {

    anyLeft = false; //reset anyLeft for next time
  }
}


public void checkLazerHitAlien(int row, int column) { //method to check if the lazer has hit an alien

//this method is inside of a for loop

  if (abs(lazerXpos - invadersX[row][column]) < 40 && abs(lazerY - invadersY[row][column]) < 50) { //if the lazer is within 40px on the x axis of an alien
                                                                                                   //and the lazer is within 50px on the y axis of an alien

    if (invadersAlive[row][column] == true && lazerHealth == 1) { //if the alien at this position is still alive and the lazer still has "health" left

      lazerHealth = 0; //make it so that the lazer has no more "health", and therefore can't hit any more aliens in the same shot

      laserVisible = false; //make the lazer disappear
      canShoot = true; //let the player shoot again

      invadersAlive[row][column] = false; //"kill" the alien at the current position
      score += 50; //increase the score by 50
    }
  }
}

public void exitButton() { //method for the exit button in the top right of some screens

  textSize(50);

  if (mouseX > 1250 && mouseX < 1400 && mouseY > 0 && mouseY < 80) { //if the mouse is on the exit button

    fill(0xffFFFFFF);//white
    rect(1250, 0, 150, 80, 20); 
    fill(0xff000000);//black
    text("EXIT", 1275, 55);
  } else {

    fill(0xff000000);//black
    rect(1250, 0, 150, 80, 20); 
    fill(0xffFFFFFF);//white
    text("EXIT", 1275, 55);
  }

  if (mouseIsClicked && mouseX > 1250 && mouseX < 1400 && mouseY > 0 && mouseY < 80) { //if the mouse is clicked inside the exit button

    state = 0; //bring the user back to the homescreen
  }
}

public void init() { //method that resets variables used in game

  for (int r = 0; r<3; r++) { //loop through the 2d array of whether the aliens are alive
    for (int c = 0; c<arrayLength; c++) {

      invadersAlive[r][c] = true; //set all the values to true
    }
  }

  for (int r = 0; r<3; r++) { //loop through the 2d array of alien x positions
    for (int c = 0; c<arrayLength; c++) {

      invadersX[r][c] = 130+(80*c); //reset the position of each alien to it's starting position
    }
  }

  for (int r = 0; r<3; r++) { //loop through the 2d array of alien y positions
    for (int c = 0; c<arrayLength; c++) {

      invadersY[r][c] = 100 * (r+1); //reset the position of each alien to it's starting position
    }
  }

  for (int i = 0; i<20; i++) { //randomly generate the positions of the stars again

    randomStarPosX = random(100, 1300);

    randomStarPosY = random(100, 700);

    starsY[i] = randomStarPosY;

    starsX[i] = randomStarPosX;
  }

  //////////////////////////////////
  
  //reset some other variables to their starting values
  
  rightArrow = false;
  leftArrow = false;
  
  win = false;
 
  lose = false;

  gameOver = false;

  movingRight = true;
  
  timerLap = timer;
  
  maxY = 0;
  
  spaceX = -100;
  
  shipLap = timer;
  
  max = 0;
  
  min = 1400;
  
  tankXpos = width/2;
  
  powerup = false;
  
  lazerXpos = -50;
}

public void drawStars() { //method to draw the stars

  for (int i = 0; i<20; i++) {

    fill(0xffFFFFFF);
    ellipse(starsX[i], starsY[i], 5, 5); //draw a star at each position, using the 2 arrays of random positions
  }
}

public void game() { //method used to play the game

  background(0xff000000); //black background

  drawStars();
  exitButton();
  drawTank();
  moveTank();
  drawScore();
  spaceship();
  if (gameOver == false) {
    drawInvaders();
    checkInvadersHitWall();
    shootLaser();
    drawLaser();
    moveLaser();
    checkGameOver();
    for (int r = 0; r<3; r++) { //loop through each alien's position
    for (int c = 0; c<arrayLength; c++) {
      
      checkLazerHitAlien(r, c); //check if the lazer is touching each alien using the checkLazerHitAlien method
    }
  }
  } 
  else if(gameOver == true){ //if the game is over

    textSize(70);
    textSize(35);
    
    if(win == true){ //if the player won
    text("YOU WON!", 600, 380); 
        if (mouseX > 480 && mouseX < 680 && mouseY > 420 && mouseY < 500) { //if the mouse is on the "Next Level" button

      fill(0xffFFFFFF); //white
      rect(480, 420, 200, 80, 30);
      fill(0xff000000); //black
      text("Next Level", 495, 470);
    } 
    else {

      fill(0xff000000); //black
      rect(480, 420, 200, 80, 30);
      fill(0xffFFFFFF); //white
      text("Next Level", 495, 470);
    }
    }
    
    if(lose == true){ //if the player lost
     text("YOU LOSE!",600,380); 
         if (mouseX > 480 && mouseX < 680 && mouseY > 420 && mouseY < 500) { //if the mouse is on the "Play again" button

      fill(0xffFFFFFF);//white
      rect(480, 420, 200, 80, 30);
      fill(0xff000000);//black
      text("Play again", 495, 470);
    } else {

      fill(0xff000000);//black
      rect(480, 420, 200, 80, 30);
      fill(0xffFFFFFF);//white
      text("Play again", 495, 470);
    } 
    }

    if (mouseX>720 && mouseX < 920 && mouseY > 420 && mouseY < 500) { //if the mouse is on the "Menu" button

      fill(0xffFFFFFF);//white
      rect(720, 420, 200, 80, 30);
      fill(0xff000000);//black
      text("Menu", 770, 470);
    } else {

      fill(0xff000000);//black
      rect(720, 420, 200, 80, 30);
      fill(0xffFFFFFF);//white
      text("Menu", 770, 470);
    }

    if (mouseIsClicked == true && mouseX > 480 && mouseX < 680 && mouseY > 420 && mouseY < 500 && lose == false) { //if the "Next Level" button is clicked

      level++; //increase the level by one
      init();  //reset variables
      
    }
    else if(mouseIsClicked == true && mouseX > 480 && mouseX < 680 && mouseY > 420 && mouseY < 500 && lose == true){ //if the "Play again" button is clicked
      
     init(); //reset variables
     level = 1; //reset the level to 1
     score = 0; //reset the score
      
    }
    if (mouseIsClicked == true && mouseX>720 && mouseX < 920 && mouseY > 420 && mouseY < 500) { //if the "Menu" button is clicked

      state = 0; //return to the homescreen
      init(); //reset variables
      doubleClick = true; //set this variable to true so that the user has to release the mouse to click on something again. Fixed a bug mentioned earlier.
      
    }
  }
}
  public void settings() {  size(1400, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SpaceInvaders" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
