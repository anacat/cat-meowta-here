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

public class catmeowtahere extends PApplet {

Scene currentScene; //objecto para guardar a cena atual.
MainMenu mainMenu;
TailGame tailGame;

public void setup() {
  
  //define o tipo de render da aplica\u00e7\u00e3o (para poder "limpar" o ecr\u00e3 ao usar uma imagem como fundo de uma cena).
  createGraphics(width, height, JAVA2D);
  textFont(loadFont("MicrosoftYaHeiLight-48.vlw"));

  mainMenu = new MainMenu();
  tailGame = new TailGame();

  currentScene = mainMenu;
}

public void draw() {
  currentScene.drawScene();
}

public void mousePressed() {
  currentScene.checkForPresses();
}

public void mouseReleased() {
  currentScene.checkForReleases();
}

public void mouseClicked() {
  currentScene.checkForClicks();
}

public void keyPressed() {
}
//Classe para facilitar a cria\u00e7\u00e3o de but\u00f5es.
class Button {
  PImage btnImage;
  PVector btnSize;
  PVector btnPosition;
  float scale = 1f;
  boolean isPressed;

  Button(String imagePath, PVector position) {
    btnImage = loadImage(imagePath);
    btnPosition = position;
  }

  public void setSize(PVector size) {
    btnImage.resize((int)size.x, (int)size.y);
  }

  public void render() {
    pushMatrix();
    imageMode(CENTER);
    image(btnImage, btnPosition.x, btnPosition.y, btnImage.width * scale, btnImage.height * scale);
    popMatrix();
  }

  public boolean isMouseOnBtn() {
    boolean xDistance = mouseX > btnPosition.x - btnImage.width/2 && mouseX < btnPosition.x + btnImage.width/2;
    boolean yDistance = mouseY > btnPosition.y - btnImage.height/2 && mouseY < btnPosition.y + btnImage.height/2;

    return xDistance && yDistance;
  }

  public void pressed() {
    scale = 0.8f;
    isPressed = true;
  }

  public void released() {
    scale = 1f;
    isPressed = false;
  }
}
static class GameStatus {
  static final int GAME_OVER = 3;
  static final int GAME_WIN = 2;
  static final int  GAME_RUNNING = 1;
  static final int GAME_STARTING = 0;
}
class MainMenu implements Scene {
  PImage backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  ArrayList<Button> buttons = new ArrayList<Button>();

  MainMenu(){
    backgroundImage = loadImage("images/mainmenu/mainmenu.png");

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2+60));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);
  }

  public void drawScene() {
    background(backgroundImage);

    for(Button btn : buttons) {
      btn.render();
    }

    if(newGameBtn.isMouseOnBtn() || exitBtn.isMouseOnBtn()){
      cursor(HAND);
    }
    else {
      cursor(ARROW);
    }
  }

  public void checkForPresses() {
    for(Button btn : buttons) {
      if(btn.isMouseOnBtn()) {
        btn.pressed();
      }
    }
  }

  public void checkForReleases() {
    for(Button btn : buttons) {
      btn.released();
    }
  }

  public void checkForClicks() {
    if(newGameBtn.isMouseOnBtn()) {
      currentScene = tailGame;
      cursor(ARROW);
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit();
    }
  }

  public void checkForKeyPresses() {
  }

  public void restartScene() {
  }
}
//Uso de uma inteface para facilitar o manuseamento de cenas na classe principal. Todas as cenas t\u00eam os mesmos m\u00e9todos.
interface Scene {
  public void drawScene();
  public void checkForPresses();
  public void checkForReleases();
  public void checkForClicks();
  public void checkForKeyPresses();
  public void restartScene();
}
class TailGame implements Scene {
  PImage backgroundImage;
  PImage playerImage;
  PImage playerFinishImage;
  PImage playerImageToDraw;

  Button gameOverBtn;

  float rotateAngle = -1f;
  float speed = -0.1f;
  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;
  boolean restartedTimer;

  float gameTimer;
  float timerStart;

  int status;

  TailGame() {
    playerImage = loadImage("images/tailgame/player.png");
    playerFinishImage = loadImage("images/tailgame/playerFinish.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));
    keyTimerStart = millis();
    timerStart = millis();

    playerImageToDraw = playerImage;
    status = GameStatus.GAME_STARTING;
  }

  public void drawScene() {
    background(255);

    pushMatrix();
    imageMode(CENTER);
    translate(width/2f, height/2f);
    rotate(rotateAngle);
    image(playerImageToDraw, 0, 0);
    popMatrix();

    inputs();

    textAlign(CENTER);

    switch(status) {
      case GameStatus.GAME_STARTING:
        gameStart();
        break;
      case GameStatus.GAME_RUNNING:
        gameRunning();
        break;
      case GameStatus.GAME_OVER:
        gameOver();
        break;
      case GameStatus.GAME_WIN:
        gameWin();
        break;
    }
  }

  public void gameStart() {
    if(gameTimer/1000 < 3f) {
      fill(50);
      text("catch a tail", width/2, height/2);
      gameTimer = millis() - timerStart;
    }
    else {
      timerStart = millis();
      status = GameStatus.GAME_RUNNING;
    }
  }

  public void gameRunning() {
    fill(0);
    rect(10, 10, ((width-20)/7f) * (gameTimer/1000), 10);

    if(gameTimer/1000 > 7f) {
      status = GameStatus.GAME_OVER;
    }
    else {
      rotateAngle += speed;
      gameTimer = millis() - timerStart;
    }
  }

  public void gameOver() {
    fill(0);
    rect(10, 10, width-20, 10);

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  public void gameWin() {
    textSize(32);
    fill(50);
    text("u have caught ur tail", width/2, height/2);
    playerImageToDraw = playerFinishImage;

    if(!restartedTimer) {
      timerStart = millis();
      gameTimer = millis() - timerStart;
      restartedTimer = true;
    }
    else {
      gameTimer = millis() - timerStart;
    }

    if (gameTimer/1000 > 3f) {
      currentScene = mainMenu;
      restartScene();
    }
  }

  public void inputs() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else if (!keyPressed ){
      pressingKey = false;
    }

    //evita que o jogador apenas clique na tecla para atingir a velocidade desejada.
    //reinicia a velocidade ap\u00f3s x segundos a carregar ou quando nenhuma tecla \u00e9 pressionada.
    if(pressingKey) {
      keyTimer = millis() - keyTimerStart; //conta o tempo passado desde que a tecla espa\u00e7o foi premida.

      if((keyTimer/1000) > 0.5f && speed < -0.1f) {
        speed = constrain(speed + 0.1f, -1f, -0.1f);
      }
    }
    else {
      speed = constrain(speed + 0.01f, -0.5f, -0.1f);
    }

    if(speed == -0.5f) {
       status = GameStatus.GAME_WIN;
    }
  }

  public void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  public void checkForReleases() {
    gameOverBtn.released();
  }

  public void checkForClicks() {
    if(gameOverBtn.isMouseOnBtn()) {
      currentScene = mainMenu;
      restartScene();
    }
  }

  public void checkForKeyPresses() {
    if(key == ' ' && !pressingKey) {
      pressingKey = true;
      keyTimerStart = millis(); //reinicia o contador
      speed = constrain(speed - 0.05f, -0.5f, -0.1f);
    }
    else if(key != ' '){
      pressingKey = false;
    }
  }

  public void restartScene() {
    keyTimerStart = millis();
    timerStart = millis();
    playerImageToDraw = playerImage;
    rotateAngle = -1f;
    speed = -0.1f;

    gameTimer = millis() - timerStart;    
    status = GameStatus.GAME_STARTING;
  }
}
  public void settings() {  size(720, 480); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "catmeowtahere" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
