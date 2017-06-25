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

Scene currentScene; // guarda a cena atual.
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
  
  //define o tipo de render da aplica\u00e7\u00e3o (para poder "limpar" o ecr\u00e3 ao usar uma imagem como fundo de uma cena).
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
//classe para a gest\u00e3o de spritesheets para a anima\u00e7\u00e3o de objectos
//c\u00f3digo adaptado do seguinte exemplo: https://www.openprocessing.org/sketch/119481
class AnimatedSprite {
  //as vari\u00e1veis publicas podem ser acedidas fora da classe. apenas estas s\u00e3o publicas pois as outras n\u00e3o conv\u00e9m ser alteradas.
  public PImage spriteSheet;
  public int frameWidth;
  public int frameHeight;
  public boolean loop = true;
  public float frameSpeed = 12.0f;
  public boolean isInLastFrame;

  //uso do tipo PVector para definir vectores de posi\u00e7\u00e3o e escala da imagem
  public PVector spriteScale = new PVector(1, 1);
  public PVector position = new PVector(0, 0);

  private int frameRow;
  private int frameColumn;
  private int startFrame;
  private int endFrame;
  private float currentFrame;
  private int columns;

  //para inicializarmos uma spriteSheet necessitamos do caminho da imagem e de saber o n\u00famero de colunas e linhas da grelha da imagem.
  AnimatedSprite(String filepath, int columns, int rows) {
    spriteSheet = loadImage(filepath);

    //\u00e9 definido o tamanho de cada sprite individual.
    frameWidth = spriteSheet.width / columns;
    frameHeight = spriteSheet.height / rows;

    this.columns = columns;
  }

  //valores inciais para a anima\u00e7\u00e3o criada: onde come\u00e7a e acaba a anima\u00e7\u00e3o, a velocidade e se faz loop ou n\u00e3o
  public void setAnimation(int start, int end, float speed, boolean looping) {
    startFrame = start;
    endFrame = end;
    currentFrame = startFrame;
    loop = looping;
    frameSpeed = speed;
    isInLastFrame = false;
  }

  public void update() {
    //muda para a frame seguinte
    currentFrame += (frameSpeed/frameRate);

    if((int)currentFrame > endFrame){
      if(loop) {
        currentFrame = startFrame;
      }
      else { //caso n\u00e3o fa\u00e7a loop indica se a anima\u00e7\u00e3o chegou \u00e0 ultima frame
        currentFrame = endFrame;
        isInLastFrame = true;
      }
    }

    frameColumn = (int)currentFrame;

    //matem\u00e1tica e cenas
    if(columns > 0) {
      frameColumn = (int)((int)currentFrame % (columns)); //resto da divis\u00e3o indica a posi\u00e7\u00e3o na coluna
      frameRow = (int)((int)currentFrame / columns); //divis\u00e3o indica a posi\u00e7\u00e3o na linha
    }

    //s\u00e3o aplicadas transforma\u00e7\u00f5es por isso \u00e9 necess\u00e1rio fazer push e pop \u00e0 matriz de transforma\u00e7\u00e3o para n\u00e3o afectar outros objectos desenhados
    pushMatrix();
    translate(position.x, position.y); //move a imagem
    scale(spriteScale.x, spriteScale.y); //escala a imagem
    copy(spriteSheet, frameColumn * frameWidth, frameRow * frameHeight,
      frameWidth, frameHeight, 0, 0, frameWidth, frameHeight); //corta a imagem de modo a renderizar apenas aquilo que \u00e9 indicado, neste caso a sprite/frame atual da spritesheet
    popMatrix();
  }
}
//Classe para facilitar a cria\u00e7\u00e3o de but\u00f5es.
class Button {
  //elementos comuns a todos os bot\u00f5es: imagem, tamanho, posi\u00e7\u00e3o, escala, estado
  PImage btnImage;
  PVector btnSize;
  PVector btnPosition;
  float scale = 1f;
  boolean isPressed;

  //\u00e9 necess\u00e1rio um caminho e vector com a posi\u00e7\u00e3o onde queremos colocar o bot\u00e3o
  Button(String imagePath, PVector position) {
    btnImage = loadImage(imagePath);
    btnPosition = position;
  }

  //fun\u00e7\u00e3o para alterar o tamanho do bot\u00e3o
  public void setSize(PVector size) {
    btnImage.resize((int)size.x, (int)size.y);
  }

  //fun\u00e7\u00e3o para renderizar o bot\u00e3o no ecr\u00e3
  public void render() {
    //s\u00e3o aplicadas transforma\u00e7\u00f5es por isso \u00e9 necess\u00e1rio fazer push e pop \u00e0 matriz de transforma\u00e7\u00e3o para n\u00e3o afectar outros objectos renderizados
    pushMatrix();
    translate(btnPosition.x, btnPosition.y);
    imageMode(CENTER);
    image(btnImage, 0, 0, btnImage.width * scale, btnImage.height * scale); //desenha bot\u00e3o com escala aplicada \u00e0 imagem
    popMatrix();
  }

  //verifica se o rato est\u00e1 em cima do bot\u00e3o. Caso o cursor do rato se encontrar dentro dos limites do bot\u00e3o ent\u00e3o retorna verdadeiro.
  public boolean isMouseOnBtn() {
    boolean xDistance = mouseX > btnPosition.x - btnImage.width/2 && mouseX < btnPosition.x + btnImage.width/2;
    boolean yDistance = mouseY > btnPosition.y - btnImage.height/2 && mouseY < btnPosition.y + btnImage.height/2;

    return xDistance && yDistance;
  }

  //estado pressionado do bot\u00e3o: a escala aumenta, o estado muda.
  public void pressed() {
    scale = 0.8f;
    isPressed = true;
  }

  //estado solto do bot\u00e3o: a escala volta ao normal, o estado volta ao normal.
  public void released() {
    scale = 1f;
    isPressed = false;
  }
}
//Mini jogo de beber a \u00e1gua do aqu\u00e1rio. Classe filha de GameScene.
class DrownFishGame extends GameScene {
  PImage playerTongueImage;
  PImage playerNoTongueImage;
  PImage playerState;
  Button gameOverBtn;

  PImage fishBowl; //sprites

  float playerSize;

  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  int waterDrank;

  //inicia os elementos do jogo
  DrownFishGame() {
    super(); //chama o construtor do pai.

    playerTongueImage = loadImage("images/drownfishgame/playerTongue.png");
    playerNoTongueImage = loadImage("images/drownfishgame/playerNoTongue.png");
    fishBowl = loadImage("images/drownfishgame/bowl.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    playerState = playerNoTongueImage;
    playerSize = 0.5f;
    waterDrank = 0;

    gameTime = 3f;
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    background(255);
    imageMode(CENTER);
    image(playerState, width/2, height/2, playerState.width * playerSize, playerState.height * playerSize);

    image(fishBowl, width/2 - 50, height/2);

    //chama a fun\u00e7\u00e3o de draw do pai
    super.drawScene();
  }

  //update da l\u00f3gica de jogo
  public void update() {
    //update fishbowl sprites

    //verifica inputs de teclas
    if(keyPressed) {
      checkForKeyPresses();
    }
    else {
      pressingKey = false;
    }

    //temporizador que impede que o utilizador possa estar a carregar sem largar a tecla. Se estiver a carregar passados mais de x segundos ent\u00e3o o pressed key passa a false, impedindo assim um jogo facilitado.
    if(pressingKey) {
      keyTimer = millis() - keyTimerStart;

      if(keyTimer/1000 > 1f) {
        pressingKey = false;
      }
    }
  }

  //override dos m\u00e9todos abstratos do pai para conter elementos espeficos do jogo
  public @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("drink all teh water", width/2, height/2);
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    //chama update da l\u00f3gica de jogo
    update();
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    textSize(32);
    fill(50);
    text("u monster", width/2, height/2);
  }

  //verifica inputs de rato para os bot\u00f5es da cena
  public void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  public void checkForReleases() {
    gameOverBtn.released();
  }

  //a a\u00e7\u00e3o do bot\u00e3o s\u00f3 acontece quando h\u00e1 uma a\u00e7\u00e3o de clique
  //se estiver em game over volta para o menu inicial
  public void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  public void checkForKeyPresses() {
    if(key == ' ' && !pressingKey) {
      playerState = playerState == playerTongueImage ? playerNoTongueImage : playerTongueImage;
      pressingKey = true;
      keyTimerStart = millis();

      waterDrank++;

      if(waterDrank == 20) {
        status = GameStatus.GAME_WIN;
      }
    }
    else if (key != ' ') {
      pressingKey = false;
    }
  }

  public void startScene() {
    playerState = playerNoTongueImage;
    playerSize = 0.5f;
    waterDrank = 0;
    pressingKey = false;

    super.startScene();
  }
}
//Cena de final dos mini jogos
//desenha um bot\u00e3o e apresenta imagens relativas \u00e0 cena final
//cont\u00e9m um bot\u00e3o para voltar para o menu incial
class FinalScene implements Scene {
  Button okBtn;

  FinalScene() {
    okBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));
  }

  public void drawScene() {
    background(255);

    //maybe put an animated gif and show the text at the end of the gif?

    fill(0);
    textSize(20);
    textAlign(CENTER);
    text("u have done it!!!", width/2, height/2);

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
      mainMenu.startScene(); //reinicia menu inicial
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
  Button okBtn;
  //AnimatedSprite cat;

  FirstScene() {
    okBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));
    /*cat = new AnimatedSprite("images/cat.png", 6, 2);

    cat.setAnimation(0, 11, 10, true);
    cat.spriteScale = new PVector(1, 1);
    cat.position = new PVector(100, 20);*/
  }

  public void drawScene() {
    background(255);

    //cat.update();

    fill(0);
    textSize(20);
    textAlign(CENTER);
    text("u are a catte and ur home alone to do as u please.\ncause some mayhem or whatever", width/2, height/2);

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
//uma classe abstrata explicita para as cenas de jogos. Implementa tudo de uma cena mas acrescenta mais algumas fun\u00e7\u00f5es.
//Ap\u00f3s a cria\u00e7\u00e3o de uma ou duas cenas de jogos, foi criada esta classe de maneira a abstra\u00edr alguns comportamentos e defini\u00e7\u00f5es comuns como o temporizador, estados do jogo (game start, game running, game over, game win).
//O uso de uma classe abstrata deve-se ao uso de m\u00e9todos abstratos que facilitam a implementa\u00e7\u00e3o do jogo.
abstract class GameScene implements Scene {
  //elementos comuns a todos os mini jogos
  PImage backgroundImage;

  protected float gameTimer;
  protected float timerStart;

  protected int status;
  protected boolean restartedTimer;
  protected float gameTime;

  //construtor da cena
  GameScene() {
    timerStart = millis();

    status = GameStatus.GAME_STARTING;
  }

  //fun\u00e7\u00e3o que renderiza o estado atual do jogo
  public void drawScene() {
    //centra todo o texto
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

  //no inicio do jogo o temporizador corre para dar inicio ao jogo, aqui \u00e9 apresentada a cena de jogo. Quando o temporizador acaba, o estado muda para game running.
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

  //estado de jogo. Cada jogo tem um tempo limite (definido no inicio). Se o jogador n\u00e3o obter a condi\u00e7\u00e3o de vit\u00f3ria e o tempo acabar, o jogo passa para o estado game over.
  public void gameRunning() {
    if(gameTimer/1000 > gameTime) {
      status = GameStatus.GAME_OVER;
    }
    else {
      gameRunningDraw();
      gameTimer = millis() - timerStart;
    }
  }

  //estado game over. \u00c9 desenhado o ecr\u00e3 de jogo perdido.
  public void gameOver() {
    gameOverDraw();
  }

  //estado game win. \u00c9 desenhado o ecr\u00e3 de vit\u00f3ria. O ecr\u00e3 de vit\u00f3ria \u00e9 renderizado durante 3 segundos, quando acaba esse tempo passa para o pr\u00f3ximo jogo. A cena atual \u00e9 tamb\u00e9m reiniciada para evitar erros ao repetir o jogo.
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
      currentScene = getNextMiniGame(); //chose rnadom scene
    }
  }

  //m\u00e9todos abstratos para a defini\u00e7\u00e3o e renderiza\u00e7\u00e3o de objectos especificos a cada jogo. No gameRunningDraw tamb\u00e9m \u00e9 verificada a condi\u00e7\u00e3o de vit\u00f3ria para acabar o jogo.
  public abstract void gameStartDraw();
  public abstract void gameRunningDraw();
  public abstract void gameOverDraw();
  public abstract void gameWinDraw();

  //m\u00e9todos vindos da interface de Scene
  public void checkForPresses() {}
  public void checkForReleases() {}
  public void checkForClicks() {}
  public void checkForKeyPresses() {}

  //m\u00e9todo para reiniciar a cena
  public void startScene() {
    restartedTimer = false;
    timerStart = millis();
    gameTimer = millis() - timerStart;

    status = GameStatus.GAME_STARTING;
  }
}
//Classe est\u00e1tica apenas para guardar strings com o valor do estado do jogo. Facilita cenas.
static class GameStatus {  
  static final int GAME_STARTING = 0;
  static final int GAME_RUNNING = 1;
  static final int GAME_WIN = 2;
  static final int GAME_OVER = 3;
}
//Mini jogo de atirar com o vaso para o ch\u00e3o. Classe filha de GameScene.
class HitTheVaseGame extends GameScene {
  PImage backgroundImage;
  Button gameOverBtn;

  PImage playerImage;
  PImage catArm;
  PImage vaseImage;
  PVector pawPosition;
  PVector enemyPosition;
  float enemyRotation;

  //Inicia as vari\u00e1veis do jogo. Mesmo que em todos os outros jogos.
  HitTheVaseGame() {
    super();

    playerImage = loadImage("images/hitthevasegame/player.png");
    catArm = loadImage("images/hitthevasegame/paw.png");
    vaseImage = loadImage("images/hitthevasegame/vase.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    //inicializa a posi\u00e7\u00e3o da patinha e do vaso (enemy)
    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 125, height/2 + 50);
    enemyRotation = 0;

    gameTime = 1f; //tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    background(255);

    imageMode(CENTER);
    updatePawPosition(); //atualiza a posi\u00e7\u00e3o da pata
    image(playerImage, width/2 + 125, height/2);

    //push e pop matrix porque s\u00e3o feitas transforma\u00e7\u00f5es no vaso
    pushMatrix();
    translate(enemyPosition.x, enemyPosition.y);
    rotate(enemyRotation);
    image(vaseImage, 0, 0);
    popMatrix();

    super.drawScene(); //chama o m\u00e9todo draw do pai
  }

  //faz a atualiza\u00e7\u00e3o da pata que \u00e9 controlada pela posi\u00e7\u00e3o do rato. Como o image mode \u00e9 centrado \u00e9 necess\u00e1rio fazer c\u00e1lculos para que o pivot da pata seja no canto inferior esquerdo.
  public void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2) + mouseX, 350, width - 250);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 145, height - 150);

    image(catArm, pawPosition.x, pawPosition.y);
  }

  //verifica se houve uma colis\u00e3o da pata com o vaso (enemy). \u00c9 verificada a sobreposi\u00e7\u00e3o de imagens para isto.
  //se houver colis\u00e3o passa para o estado game win.
  public void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (vaseImage.width)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      status = GameStatus.GAME_WIN;
    }
  }

  //override dos m\u00e9todos abstratos do pai para conter elementos espeficos do jogo
  public @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("slap teh vase", width/2, height/2);
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    //verifica a colis\u00e3o: condi\u00e7\u00e3o de vit\u00f3ria
    checkColision();
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    textSize(32);
    fill(50);
    text("oh shit!!!!", width/2, height/2);

    //vaso roda como se tivesse ca\u00eddo
    enemyPosition = new PVector(width/2 - 100, height/2 + 150);
    enemyRotation = -HALF_PI;
  }

  //verifica inputs do rato para os bot\u00f5es desenhados
  public void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  public void checkForReleases() {
    gameOverBtn.released();
  }

  //na a\u00e7\u00e3o de clique passa para o menu inicial
  public void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  //reinicia valores da cena
  public void startScene() {
    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 125, height/2 + 50);
    enemyRotation = 0;

    super.startScene(); //chama m\u00e9todo do pai
  }
}
//Classe para o menu inicial. Implementa a interface Scene
class MainMenu implements Scene {
  PImage backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  //array de bot\u00f5es para facilitar o controlo dos mesmos. Porque sim.
  ArrayList<Button> buttons = new ArrayList<Button>();

  //inicializa os elementos do menu
  MainMenu(){
    backgroundImage = loadImage("images/mainmenu/mainmenu.png");

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2+60));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);
  }

  //desenha os elementos do menu
  public void drawScene() {
    background(backgroundImage);

    //renderiza os bot\u00f5es
    for(Button btn : buttons) {
      btn.render();
    }

    //se tiver em cima do bot\u00e3o o cursor passa a ser a m\u00e3o, se n\u00e3o \u00e9 a
    if(newGameBtn.isMouseOnBtn() || exitBtn.isMouseOnBtn()){
      cursor(HAND);
    }
    else {
      cursor(ARROW);
    }
  }

  //verifica inputs do rato para os bot\u00f5es da cena
  public void checkForPresses() {
    for(Button btn : buttons) {
      if(btn.isMouseOnBtn()) {
        btn.pressed(); //passa para o estado pressed quando no mouseDown
      }
    }
  }

  public void checkForReleases() {
    for(Button btn : buttons) {
      btn.released(); //passa para o estado released quando o bot\u00e3o do rato \u00e9 largado
    }
  }

  //efectua a a\u00e7\u00e3o quando o rato est\u00e1 em cima do bot\u00e3o e \u00e9 efectuado um evento de clique
  public void checkForClicks() {
    if(newGameBtn.isMouseOnBtn()) {
      currentScene = firstScene;
      cursor(ARROW);
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit(); //fecha o jogo
    }
  }

  //n\u00e3o h\u00e1 eventos de teclado por isso fica vazio. Este m\u00e9todo \u00e9 obrigat\u00f3rio de ter pois pretence \u00e0 interface que implementamos
  public void checkForKeyPresses() {
  }

  //reinicia o n\u00famero de mini jogos dispon\u00edveis
  public void startScene() {
    playableGames.add(0);
    playableGames.add(1);
    playableGames.add(2);
    playableGames.add(3);
  }
}
//Uso de uma inteface para facilitar o manuseamento de cenas na classe principal. Todas as cenas t\u00eam os mesmos m\u00e9todos: desenhar cena, verificar inputs e reiniciar a cena para o seu estado inicial.
interface Scene {
  public void drawScene();
  public void checkForPresses();
  public void checkForReleases();
  public void checkForClicks();
  public void checkForKeyPresses();
  public void startScene();
}
//Porqu\u00ea uma interface? N\u00e3o sei, quis experimentar.
//Mini jogo de dar uma patada a outro gato. Classe filha de GameScene.
class SlapCatGame extends GameScene {
  Button gameOverBtn;
  PImage playerImage;
  PImage backgroundImage;
  PImage catArm;

  PImage otherCatImage1;
  PImage otherCatImage2;
  PImage enemyImage;

  PVector enemyPosition;
  PVector pawPosition;

  float playerSize;
  float enemySpeed = 7f;

  //Inicia as vari\u00e1veis do jogo. Mesmo que em todos os outros jogos.
  SlapCatGame() {
    super();

    playerImage = loadImage("images/slapcatgame/player.png");
    catArm = loadImage("images/slapcatgame/catpaw.png");
    otherCatImage1 = loadImage("images/slapcatgame/othercat1.png");
    otherCatImage2 = loadImage("images/slapcatgame/othercat2.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    playerSize = 0.7f;

    //inicializa posi\u00e7\u00f5es dos elementos de jogo.
    enemyPosition = new PVector(-otherCatImage1.width * playerSize, 200);
    pawPosition = new PVector();

    enemyImage = otherCatImage1;
    gameTime = 2.5f; //inicia o tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    background(255);

    imageMode(CENTER);
    updatePawPosition(); //atualiza a posi\u00e7\u00e3o da pata

    image(playerImage, width/2+125, 200, playerImage.width * playerSize, playerImage.height * playerSize);

    imageMode(CORNER);
    image(enemyImage, enemyPosition.x, enemyPosition.y, enemyImage.width * playerSize, enemyImage.height * playerSize);

    super.drawScene(); //chama o m\u00e9todo draw do pai
  }

  //faz a atualiza\u00e7\u00e3o da pata que \u00e9 controlada pela posi\u00e7\u00e3o do rato. Como o image mode \u00e9 centrado \u00e9 necess\u00e1rio fazer c\u00e1lculos para que o pivot da pata seja no canto inferior esquerdo.
  public void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width*playerSize)/2)+mouseX, 375, width - 275);
    pawPosition.y = constrain(mouseY - ((catArm.height*playerSize)/2), 145, height - 150);

    image(catArm, pawPosition.x, pawPosition.y, catArm.width * playerSize, catArm.height * playerSize);
  }

  //verifica se houve uma colis\u00e3o da pata com o inimigo (enemy). \u00c9 verificada a sobreposi\u00e7\u00e3o de imagens para isto.
  //se houver colis\u00e3o passa para o estado game win.
  public void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (enemyImage.width * playerSize)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      enemyImage = otherCatImage2;

      status = GameStatus.GAME_WIN;
    }
  }

  //override dos m\u00e9todos abstratos do pai para conter elementos espeficos do jogo
  public @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("slap teh cat", width/2, height/2);
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    enemyPosition.x += enemySpeed;  //move o inimigo
    checkColision();  //verifica a colis\u00e3o: condi\u00e7\u00e3o de vit\u00f3ria
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    textSize(32);
    fill(50);
    text("hope ur happy with ur self", width/2, height/2);
  }

  //verifica inputs do rato para os bot\u00f5es desenhados
  public void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  public void checkForReleases() {
    gameOverBtn.released();
  }

  //na a\u00e7\u00e3o de clique passa para o menu inicial
  public void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  public void checkForKeyPresses() {

  }

  //reinicia valores da cena
  public void startScene() {
    playerSize = 0.7f;
    enemyPosition = new PVector(-otherCatImage1.width * playerSize, 200);
    pawPosition = new PVector();

    enemyImage = otherCatImage1;

    super.startScene(); //chama m\u00e9todo do pai
  }
}
//Mini jogo de dar uma patada a outro gato. Classe filha de GameScene.
class TailGame extends GameScene {
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

  //Inicia as vari\u00e1veis do jogo. Mesmo que em todos os outros jogos.
  TailGame() {
    super();

    playerImage = loadImage("images/tailgame/player.png");
    playerFinishImage = loadImage("images/tailgame/playerFinish.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));
    keyTimerStart = millis();

    playerImageToDraw = playerImage;
    gameTime = 7f; //tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    background(255);

    //push e pop matrix porque s\u00e3o feitas transforma\u00e7\u00f5es no jogador
    pushMatrix();
    imageMode(CENTER);
    translate(width/2f, height/2f);
    rotate(rotateAngle);
    image(playerImageToDraw, 0, 0);
    popMatrix();

    inputs(); //verifica os inputs

    textAlign(CENTER);

    super.drawScene(); //chama m\u00e9todo do pai
  }

  //override dos m\u00e9todos abstratos do pai para conter elementos espeficos do jogo
  public @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("catch a tail", width/2, height/2);
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    rotateAngle += speed; //gato vai rodando ao longo do tempo
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    textSize(32);
    fill(50);
    text("u have caught ur tail", width/2, height/2);

    playerImageToDraw = playerFinishImage;
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
        keyTimerStart = millis(); //reinicia o contador
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
    playerImageToDraw = playerImage;
    rotateAngle = -1f;
    speed = -0.1f;

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
