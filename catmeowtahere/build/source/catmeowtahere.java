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

Scene currentScene;

MainMenu mainMenu;
FirstScene firstScene;
FinalScene finalScene;

TailGame tailGame;
SlapCatGame slapGame;
DrownFishGame drownFishGame;
HitTheVaseGame hitTheVaseGame;

ArrayList<GameScene> miniGames = new ArrayList<GameScene>();
ArrayList<Integer> playableGames;

public void setup() {
  
  surface.setTitle("Cat Meowta Here");
  createGraphics(width, height, JAVA2D);
  textFont(loadFont("MicrosoftYaHeiLight-48.vlw"));

  mainMenu = new MainMenu();
  firstScene = new FirstScene();
  finalScene = new FinalScene();

  tailGame = new TailGame();
  slapGame = new SlapCatGame();
  drownFishGame = new DrownFishGame();
  hitTheVaseGame = new HitTheVaseGame();

  miniGames.add(tailGame);
  miniGames.add(slapGame);
  miniGames.add(drownFishGame);
  miniGames.add(hitTheVaseGame);

  playableGames = new ArrayList<Integer>(miniGames.size());

  playableGames.add(0);
  playableGames.add(1);
  playableGames.add(2);
  playableGames.add(3);

  currentScene = mainMenu;
}

public Scene getNextMiniGame() {
  if(playableGames.size() > 0) {
    int randomScene = (int)random(0, playableGames.size());

    int sceneNumber = playableGames.get(randomScene);

    playableGames.remove(randomScene);

    Scene scene = miniGames.get(sceneNumber);
    scene.startScene();

    return scene;
  }
  else {
    return finalScene;
  }
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
class AnimatedSprite {
  public PImage spriteSheet;
  public int frameWidth;
  public int frameHeight;
  public boolean loop = true;
  public float frameSpeed = 12.0f;
  public boolean isInLastFrame;

  public PVector spriteScale = new PVector(1, 1);
  public PVector position = new PVector(0, 0);

  private int frameRow;
  private int frameColumn;
  private int startFrame;
  private int endFrame;
  private float currentFrame;
  private int columns;

  AnimatedSprite(String filepath, int columns, int rows) {
    spriteSheet = loadImage(filepath);

    frameWidth = spriteSheet.width / columns;
    frameHeight = spriteSheet.height / rows;

    this.columns = columns;
  }

  public void setAnimation(int start, int end, float speed, boolean looping) {
    startFrame = start;
    endFrame = end;
    currentFrame = startFrame;
    loop = looping;
    frameSpeed = speed;
    isInLastFrame = false;
  }

  public void update() {
    currentFrame += (frameSpeed/frameRate);

    if((int)currentFrame > endFrame){
      if(loop) {
        currentFrame = startFrame;
      }
      else {
        currentFrame = endFrame;
        isInLastFrame = true;
      }
    }

    frameColumn = (int)currentFrame;

    if(columns > 0) {
      frameColumn = (int)((int)currentFrame % (columns));
      frameRow = (int)((int)currentFrame / columns);
    }

    pushMatrix();
    translate(position.x, position.y);
    scale(spriteScale.x, spriteScale.y);
    copy(spriteSheet, frameColumn * frameWidth, frameRow * frameHeight,
      frameWidth, frameHeight, 0, 0, frameWidth, frameHeight);
    popMatrix();
  }
}
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
    translate(btnPosition.x, btnPosition.y);
    imageMode(CENTER);
    image(btnImage, 0, 0, btnImage.width * scale, btnImage.height * scale);
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
class DrownFishGame extends GameScene {
  PImage playerState;
  PImage fundo;
  Button gameOverBtn;

  AnimatedSprite fishBowl;
  AnimatedSprite catte;

  AnimatedSprite instructions;
  AnimatedSprite end;
  AnimatedSprite ohno;

  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  int waterDrank;

  DrownFishGame() {
    super();

    fundo = loadImage("images/drownfishgame/fundo.png");
    fishBowl = new AnimatedSprite("images/drownfishgame/bowl.png", 3, 3);
    catte = new AnimatedSprite("images/drownfishgame/catte.png", 2, 1);

    instructions = new AnimatedSprite("images/drownfishgame/instructions.png", 1, 2);
    end = new AnimatedSprite("images/drownfishgame/end.png", 1, 2);
    ohno = new AnimatedSprite("images/drownfishgame/ohno.png", 1, 2);

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    fishBowl.setAnimation(0, 0, 1, false);
    catte.setAnimation(1, 1, 1, false);
    instructions.setAnimation(0, 1, 2, true);
    end.setAnimation(0, 1, 2, true);
    ohno.setAnimation(0, 1, 2, true);

    instructions.position = new PVector(width/2 - instructions.frameWidth/2, height/2 - instructions.frameHeight/2);
    end.position = new PVector(width/2 - end.frameWidth/2, height/2 - end.frameHeight/2);
    ohno.position = new PVector(width/2 - ohno.frameWidth/2, height/2 - ohno.frameHeight/2);

    fishBowl.position = new PVector(width/2 - 120, height/2 - 10);
    catte.position = new PVector(width/2 - 70, height/2 -100);

    waterDrank = 0;

    gameTime = 7f;
  }

  public void drawScene() {
    background(255);
    imageMode(CORNER);
    image(fundo, 0, 0);

    imageMode(CENTER);
    catte.update();
    fishBowl.update();

    super.drawScene();
  }

  public void update() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else {
      pressingKey = false;
    }

    if(pressingKey) {
      keyTimer = millis() - keyTimerStart;

      if(keyTimer/1000 > 1f) {
        pressingKey = false;
      }
    }
  }

  public @Override void gameStartDraw() {
    imageMode(CORNER);
    instructions.update();
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    update();
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    ohno.update();
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    end.update();
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
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  public void checkForKeyPresses() {
    if(key == ' ' && !pressingKey) {
      pressingKey = true;
      keyTimerStart = millis();

      waterDrank++;
      fishBowl.setAnimation((int)(waterDrank/2.6f), (int)(waterDrank/2.6f), 1, false);

      catte.setAnimation(waterDrank % 2, waterDrank % 2, 1, false);

      if(waterDrank == 20) {
        status = GameStatus.GAME_WIN;
      }
    }
    else if (key != ' ') {
      pressingKey = false;
    }
  }

  public void startScene() {
    waterDrank = 0;
    pressingKey = false;
    fishBowl.setAnimation(0, 0, 1, false);
    catte.setAnimation(1, 1, 1, false);

    super.startScene();
  }
}
class FinalScene implements Scene {
  AnimatedSprite backgroundImage;
  Button okBtn;

  FinalScene() {
    okBtn = new Button("images/lastscene/btn.png", new PVector(width/2, height/2+160));

    backgroundImage = new AnimatedSprite("images/lastscene/background.png", 1, 2);
    backgroundImage.setAnimation(0, 1, 2, true);
    okBtn.btnPosition = new PVector(width - okBtn.btnImage.width + 30, height - okBtn.btnImage.height);
  }

  public void drawScene() {
    backgroundImage.update();

    okBtn.render();
  }

  public void checkForPresses() {
    if(okBtn.isMouseOnBtn()) {
      okBtn.pressed();
    }
  }

  public void checkForReleases() {
    okBtn.released();
  }

  public void checkForClicks() {
    if(okBtn.isMouseOnBtn()) {
      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  public void checkForKeyPresses() {

  }

  public void startScene() {

  }
}
//Cena de inicial dos mini jogos para dar algum contexto
//desenha um bot\u00e3o e apresenta imagens relativas \u00e0 cena incial
//cont\u00e9m um bot\u00e3o para come\u00e7ar os jogos
class FirstScene implements Scene{
  AnimatedSprite backgroundImage;
  AnimatedSprite title;
  Button okBtn;

  FirstScene() {
    okBtn = new Button("images/firstscene/btn.png", new PVector(0, 0));

    backgroundImage = new AnimatedSprite("images/firstscene/background.png", 1, 2);
    title = new AnimatedSprite("images/firstscene/title.png", 1, 2);

    backgroundImage.setAnimation(0, 1, 2, true);
    title.setAnimation(0, 1, 2, true);

    backgroundImage.position = new PVector(width/2 - backgroundImage.frameWidth, height - backgroundImage.frameHeight);
    title.position = new PVector(width/2 - title.frameWidth/2, 50); //centra a imagem do titulo
    okBtn.btnPosition = new PVector(width - okBtn.btnImage.width + 30, height - okBtn.btnImage.height);
  }

  public void drawScene() {
    background(249, 213, 211);
    backgroundImage.update();
    title.update();

    okBtn.render();
  }

  //verifica inputs do rato para os bot\u00f5es da cena
  public void checkForPresses() {
    if(okBtn.isMouseOnBtn()) {
      okBtn.pressed();
    }
  }

  public void checkForReleases() {
    okBtn.released();
  }

  public void checkForClicks() {
    if(okBtn.isMouseOnBtn()) {
      currentScene = getNextMiniGame();
    }
  }

  public void checkForKeyPresses() {

  }

  public void startScene() {

  }
}
abstract class GameScene implements Scene {
  protected float gameTimer;
  protected float timerStart;

  protected int status;
  protected boolean restartedTimer;
  protected float gameTime;

  GameScene() {
    timerStart = millis();

    status = GameStatus.GAME_STARTING;
  }

  public void drawScene() {
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
      gameStartDraw();
    }
    else {
      timerStart = millis();
      status = GameStatus.GAME_RUNNING;
    }
    gameTimer = millis() - timerStart;
  }

  public void gameRunning() {
    if(gameTimer/1000 > gameTime) {
      status = GameStatus.GAME_OVER;
    }
    else {
      gameRunningDraw();
      gameTimer = millis() - timerStart;
    }
  }

  public void gameOver() {
    gameOverDraw();
  }

  public void gameWin() {
    gameWinDraw();

    if(!restartedTimer) {
      timerStart = millis();
      gameTimer = millis() - timerStart;
      restartedTimer = true;
    }
    else {
      gameTimer = millis() - timerStart;
    }

    if (gameTimer/1000 > 3f) {
      startScene();
      currentScene = getNextMiniGame();
    }
  }

  public abstract void gameStartDraw();
  public abstract void gameRunningDraw();
  public abstract void gameOverDraw();
  public abstract void gameWinDraw();

  public void checkForPresses() {}
  public void checkForReleases() {}
  public void checkForClicks() {}
  public void checkForKeyPresses() {}

  public void startScene() {
    restartedTimer = false;
    timerStart = millis();
    gameTimer = millis() - timerStart;

    status = GameStatus.GAME_STARTING;
  }
}
static class GameStatus {  
  static final int GAME_STARTING = 0;
  static final int GAME_RUNNING = 1;
  static final int GAME_WIN = 2;
  static final int GAME_OVER = 3;
}
class HitTheVaseGame extends GameScene {
  Button gameOverBtn;

  PImage backgroundImage;
  PImage playerImage;
  PImage catArm;
  PImage vaseImage;
  PVector pawPosition;
  PVector enemyPosition;
  float enemyRotation;

  AnimatedSprite instructions;
  AnimatedSprite end;
  AnimatedSprite ohno;

  HitTheVaseGame() {
    super();

    backgroundImage = loadImage("images/hitthevasegame/background.png");
    playerImage = loadImage("images/hitthevasegame/player.png");
    catArm = loadImage("images/hitthevasegame/paw.png");
    vaseImage = loadImage("images/hitthevasegame/vase.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    instructions = new AnimatedSprite("images/hitthevasegame/instructions.png", 1, 2);
    end = new AnimatedSprite("images/hitthevasegame/end.png", 1, 2);
    ohno = new AnimatedSprite("images/hitthevasegame/ohno.png", 1, 2);

    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 100, height/2 + 50);
    enemyRotation = 0;

    instructions.setAnimation(0, 1, 2, true);
    end.setAnimation(0, 1, 2, true);
    ohno.setAnimation(0, 1, 2, true);

    instructions.position = new PVector(width/2 - instructions.frameWidth/2, height/2 - instructions.frameHeight/2);
    end.position = new PVector(width/2 - end.frameWidth/2, height/2 - end.frameHeight/2);
    ohno.position = new PVector(width/2 - ohno.frameWidth/2, height/2 - ohno.frameHeight/2);

    gameTime = 1f;
  }

  public void drawScene() {
    imageMode(CORNER);
    image(backgroundImage, 0, 0);

    imageMode(CENTER);
    updatePawPosition();
    image(playerImage, width/2 + 75, height/2);

    pushMatrix();
    translate(enemyPosition.x, enemyPosition.y);
    rotate(enemyRotation);
    image(vaseImage, 0, 0);
    popMatrix();

    super.drawScene();
  }

  public void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2) + mouseX, 360, 390);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 230, 300);

    image(catArm, pawPosition.x, pawPosition.y);
  }

  public void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (vaseImage.width)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      status = GameStatus.GAME_WIN;
    }
  }

  public @Override void gameStartDraw() {
    imageMode(CORNER);
    instructions.update();
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    checkColision();
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    ohno.update();
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    end.update();

    enemyPosition = new PVector(width/2 - 100, height/2 + 150);
    enemyRotation = -HALF_PI;
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
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  public void startScene() {
    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 100, height/2 + 50);
    enemyRotation = 0;

    super.startScene();
  }
}
class MainMenu implements Scene {
  AnimatedSprite backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  ArrayList<Button> buttons = new ArrayList<Button>();

  MainMenu(){
    backgroundImage = new AnimatedSprite("images/mainmenu/mainmenu.png", 1, 2);

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2-40));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+110));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);

    backgroundImage.setAnimation(0, 1, 2, true);
  }

  public void drawScene() {
    imageMode(CENTER);
    backgroundImage.update();

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
      currentScene = firstScene;
      cursor(ARROW);
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit();
    }
  }

  public void checkForKeyPresses() {
  }

  public void startScene() {
    playableGames.add(0);
    playableGames.add(1);
    playableGames.add(2);
    playableGames.add(3);
  }
}
interface Scene {
  public void drawScene();
  public void checkForPresses();
  public void checkForReleases();
  public void checkForClicks();
  public void checkForKeyPresses();
  public void startScene();
}
class SlapCatGame extends GameScene {
  Button gameOverBtn;
  PImage playerImage;
  PImage backgroundImage;
  PImage catArm;

  AnimatedSprite otherCat;

  AnimatedSprite instructions;
  AnimatedSprite end;
  AnimatedSprite ohno;

  PVector pawPosition;

  float enemySpeed = 7f;

  SlapCatGame() {
    super();

    backgroundImage = loadImage("images/slapcatgame/background.png");
    playerImage = loadImage("images/slapcatgame/player.png");
    catArm = loadImage("images/slapcatgame/catpaw.png");
    otherCat = new AnimatedSprite("images/slapcatgame/othercat.png", 1, 2);

    instructions = new AnimatedSprite("images/slapcatgame/instructions.png", 1, 2);
    end = new AnimatedSprite("images/slapcatgame/end.png", 1, 2);
    ohno = new AnimatedSprite("images/slapcatgame/ohno.png", 1, 2);

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    otherCat.setAnimation(0, 0, 1, false);
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    instructions.setAnimation(0, 1, 2, true);
    end.setAnimation(0, 1, 2, true);
    ohno.setAnimation(0, 1, 2, true);

    instructions.position = new PVector(10, 10);
    end.position = new PVector(width/2 - end.frameWidth/2, height/2 - end.frameHeight/2);
    ohno.position = new PVector(width/2 - ohno.frameWidth/2, height/2 - ohno.frameHeight/2);

    gameTime = 2.5f;
  }

  public void drawScene() {
    background(backgroundImage);

    imageMode(CENTER);
    updatePawPosition();

    image(playerImage, width/2+125, height/2, playerImage.width, playerImage.height);

    imageMode(CORNER);
    otherCat.update();

    super.drawScene();
  }

  public void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2)+mouseX, 400, width - 280);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 230, height - 170);

    image(catArm, pawPosition.x, pawPosition.y, catArm.width, catArm.height);
  }

  public void checkColision() {
    if(pawPosition.x > otherCat.position.x && pawPosition.x < otherCat.position.x + (otherCat.frameWidth)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      otherCat.setAnimation(1, 1, 1, false);
      status = GameStatus.GAME_WIN;
    }
  }

  public @Override void gameStartDraw() {
    imageMode(CORNER);
    instructions.update();
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    otherCat.position.x += enemySpeed;
    checkColision();
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    ohno.update();
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    end.update();
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
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  public void checkForKeyPresses() {

  }

  public void startScene() {
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    otherCat.setAnimation(0, 0, 1, false);

    super.startScene();
  }
}
class TailGame extends GameScene {
  PImage backgroundImage;

  AnimatedSprite player;

  AnimatedSprite instructions;
  AnimatedSprite end;
  AnimatedSprite ohno;

  Button gameOverBtn;

  float rotateAngle = -1f;
  float speed = -0.1f;
  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  TailGame() {
    super();

    backgroundImage = loadImage("images/tailgame/background.png");
    player = new AnimatedSprite("images/tailgame/player.png", 3, 1);
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    instructions = new AnimatedSprite("images/tailgame/instructions.png", 1, 2);
    end = new AnimatedSprite("images/tailgame/end.png", 1, 2);
    ohno = new AnimatedSprite("images/tailgame/ohno.png", 1, 2);

    instructions.position = new PVector(width/2 - instructions.frameWidth/2, height/2 - instructions.frameHeight/2);
    end.position = new PVector(width/2 - end.frameWidth/2, height/2 - end.frameHeight/2);
    ohno.position = new PVector(width/2 - ohno.frameWidth/2, height/2 - ohno.frameHeight/2);

    keyTimerStart = millis();

    player.setAnimation(0, 0, 2, true);
    instructions.setAnimation(0, 1, 2, true);
    ohno.setAnimation(0, 1, 2, true);
    end.setAnimation(0, 1, 2, true);
    gameTime = 7f;

    player.position = new PVector(0, 0);
  }

  public void drawScene() {
    background(backgroundImage);

    pushMatrix();
    translate(width/2, height/2);
    rotate(rotateAngle);
    translate(-player.frameWidth/2, -player.frameHeight/2);
    player.update();
    popMatrix();

    inputs();

    super.drawScene();
  }

  public @Override void gameStartDraw() {
    imageMode(CORNER);
    instructions.update();
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    rotateAngle += speed;
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    ohno.update();
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    end.update();

    player.setAnimation(2, 2, 1, false);
  }

  public void inputs() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else if (!keyPressed ){
      pressingKey = false;
    }

    if(pressingKey) {
      keyTimer = millis() - keyTimerStart;

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
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  public void checkForKeyPresses() {
    if(status == GameStatus.GAME_RUNNING) {
      if(key == ' ' && !pressingKey) {
        pressingKey = true;
        keyTimerStart = millis();
        speed = constrain(speed - 0.05f, -0.5f, -0.1f);
      }
      else if(key != ' '){
        pressingKey = false;
      }
    }
  }

  public void startScene() {
    keyTimerStart = millis();
    timerStart = millis();
    rotateAngle = -1f;
    speed = -0.1f;

    player.setAnimation(0, 0, 10, true);
    player.position = new PVector(0, 0);

    super.startScene();
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
