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

//"Classe" principal de toda a aplica\u00e7\u00e3o.
//Aqui s\u00e3o criados todos os mini jogos e cenas, entre outros elementos.
//Tamb\u00e9m s\u00e3o guardados elementos essenciais ao jogo.
Scene currentScene; // guarda a cena atual.

//Cenas
MainMenu mainMenu;
FirstScene firstScene;
FinalScene finalScene;

//Jogos
TailGame tailGame;
SlapCatGame slapGame;
DrownFishGame drownFishGame;
HitTheVaseGame hitTheVaseGame;

//Lista com os mini jogos existentes
ArrayList<GameScene> miniGames = new ArrayList<GameScene>();
//Lista com o valor de jogos por jogar (usa valores inteiros para facilitar a busca depois na lista anterior).
ArrayList<Integer> playableGames;

public void setup() {
  
  //define o tipo de render da aplica\u00e7\u00e3o (para poder "limpar" o ecr\u00e3 ao usar uma imagem como fundo de uma cena).
  createGraphics(width, height, JAVA2D);
  textFont(loadFont("MicrosoftYaHeiLight-48.vlw")); //fonte usada pela aplica\u00e7\u00e7\u00e3o

  //Inicializa\u00e7\u00e3o das cenas
  mainMenu = new MainMenu();
  firstScene = new FirstScene();
  finalScene = new FinalScene();

  //Inicializa\u00e7\u00e3o dos mini jogos.
  tailGame = new TailGame();
  slapGame = new SlapCatGame();
  drownFishGame = new DrownFishGame();
  hitTheVaseGame = new HitTheVaseGame();

  //Adiciona os jogos \u00e0 lista respectiva
  miniGames.add(tailGame);
  miniGames.add(slapGame);
  miniGames.add(drownFishGame);
  miniGames.add(hitTheVaseGame);

  //Inicializa a lista de jogos dispon\u00edveis com o mesmo n\u00famero de elementos que a lista de jogos adicionados
  playableGames = new ArrayList<Integer>(miniGames.size());

  //Adiciona os valores
  playableGames.add(0);
  playableGames.add(1);
  playableGames.add(2);
  playableGames.add(3);

  currentScene = mainMenu; //define o menu de jogo com a cena inicial.
}

//Fun\u00e7\u00e3o para retornar o pr\u00f3ximo mini jogo ou cena.
//Retorna cena pois \u00e9 o elemento base de qualquer cena: seja jogo ou apena scene.
public Scene getNextMiniGame() {
  //se a lista ainda tiver valores, ent\u00e3o retorna um mini jogo aleat\u00f3rio
  if(playableGames.size() > 0) {
    int randomScene = (int)random(0, playableGames.size());

    int sceneNumber = playableGames.get(randomScene);

    playableGames.remove(randomScene); //remove o valor do mini jogo escolhido

    Scene scene = miniGames.get(sceneNumber);
    scene.startScene(); //reinicia o jogo, pelo sim pelo n\u00e3o

    return scene; //retorna o jogo selecionado
  }
  else { //se a lista j\u00e1 estiver vazia, ent\u00e3o devolve a cena final do jogo.
    return finalScene;
  }
}

//desenha a cena atual
public void draw() {
  currentScene.drawScene();
}

//verifica as condi\u00e7\u00f5es de mousePressed da cena atual.
public void mousePressed() {
  currentScene.checkForPresses();
}

//verifica as condi\u00e7\u00f5es de mouseReleased da cena atual.
public void mouseReleased() {
  currentScene.checkForReleases();
}

//verifica as condi\u00e7\u00f5es de clique da cena atual.
public void mouseClicked() {
  currentScene.checkForClicks();
}

//verifica as condi\u00e7\u00f5es de keypressed da cena atual.
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
  PImage playerState;
  PImage fundo;
  Button gameOverBtn;

  AnimatedSprite fishBowl;
  AnimatedSprite catte;

  PImage instructions;
  PImage end;
  PImage ohno;

  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  int waterDrank;

  //inicia os elementos do jogo
  DrownFishGame() {
    super(); //chama o construtor do pai.

    fundo = loadImage("images/drownfishgame/fundo.png");
    fishBowl = new AnimatedSprite("images/drownfishgame/bowl.png", 3, 3);
    catte = new AnimatedSprite("images/drownfishgame/catte.png", 2, 1);

    instructions = loadImage("images/drownfishgame/instructions.png");
    end = loadImage("images/drownfishgame/end.png");
    ohno = loadImage("images/drownfishgame/ohno.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    fishBowl.setAnimation(0, 0, 1, false);
    catte.setAnimation(1, 1, 1, false);

    fishBowl.position = new PVector(width/2 - 120, height/2 - 10);
    catte.position = new PVector(width/2 - 70, height/2 -100);

    waterDrank = 0;

    gameTime = 7f;
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    background(255);
    imageMode(CORNER);
    image(fundo, 0, 0);

    imageMode(CENTER);
    catte.update();
    fishBowl.update();

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
    imageMode(CORNER);
    image(instructions, 0, 0);
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

    imageMode(CORNER);
    image(ohno, 0, 0);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    image(end, 0, 0);
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
      pressingKey = true;
      keyTimerStart = millis();

      waterDrank++; //frame da anima\u00e7\u00e3o do aqu\u00e1rio depende do valor da \u00e1gua bebida.
      fishBowl.setAnimation((int)(waterDrank/2.6f), (int)(waterDrank/2.6f), 1, false); //divide o valor de maneira a obter a mesma frame por mais tempo

      catte.setAnimation(waterDrank % 2, waterDrank % 2, 1, false); //resto da divis\u00e3o por 2 \u00e9 sempre 0 ou 1; s\u00f3 temos 2 frames

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
//Cena de final dos mini jogos
//desenha um bot\u00e3o e apresenta imagens relativas \u00e0 cena final
//cont\u00e9m um bot\u00e3o para voltar para o menu incial
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
//uma classe abstrata explicita para as cenas de jogos. Implementa tudo de uma cena mas acrescenta mais algumas fun\u00e7\u00f5es.
//Ap\u00f3s a cria\u00e7\u00e3o de uma ou duas cenas de jogos, foi criada esta classe de maneira a abstra\u00edr alguns comportamentos e defini\u00e7\u00f5es comuns como o temporizador, estados do jogo (game start, game running, game over, game win).
//O uso de uma classe abstrata deve-se ao uso de m\u00e9todos abstratos que facilitam a implementa\u00e7\u00e3o do jogo.
abstract class GameScene implements Scene {
  //elementos comuns a todos os mini jogos
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
  Button gameOverBtn;

  PImage backgroundImage;
  PImage playerImage;
  PImage catArm;
  PImage vaseImage;
  PVector pawPosition;
  PVector enemyPosition;
  float enemyRotation;

  PImage instructions;
  PImage end;
  PImage ohno;

  //Inicia as vari\u00e1veis do jogo. Mesmo que em todos os outros jogos.
  HitTheVaseGame() {
    super();

    backgroundImage = loadImage("images/hitthevasegame/background.png");
    playerImage = loadImage("images/hitthevasegame/player.png");
    catArm = loadImage("images/hitthevasegame/paw.png");
    vaseImage = loadImage("images/hitthevasegame/vase.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    instructions = loadImage("images/hitthevasegame/instructions.png");
    end = loadImage("images/hitthevasegame/end.png");
    ohno = loadImage("images/hitthevasegame/ohno.png");

    //inicializa a posi\u00e7\u00e3o da patinha e do vaso (enemy)
    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 100, height/2 + 50);
    enemyRotation = 0;

    gameTime = 1f; //tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    imageMode(CORNER);
    image(backgroundImage, 0, 0);

    imageMode(CENTER);
    updatePawPosition(); //atualiza a posi\u00e7\u00e3o da pata
    image(playerImage, width/2 + 75, height/2);

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
    pawPosition.x = constrain(((catArm.width)/2) + mouseX, 360, 400); //limita a posi\u00e7\u00e3o
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 200, 300);

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
    imageMode(CORNER);
    image(instructions, 0, 0);
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

    imageMode(CORNER);
    image(ohno, 0, 0);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    image(end, 0, 0);

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
    enemyPosition = new PVector(width/2 - 100, height/2 + 50);
    enemyRotation = 0;

    super.startScene(); //chama m\u00e9todo do pai
  }
}
//Classe para o menu inicial. Implementa a interface Scene
class MainMenu implements Scene {
  AnimatedSprite backgroundImage;
  AnimatedSprite title;
  Button newGameBtn;
  Button exitBtn;

  //array de bot\u00f5es para facilitar o controlo dos mesmos. Porque sim.
  ArrayList<Button> buttons = new ArrayList<Button>();

  //inicializa os elementos do menu
  MainMenu(){
    backgroundImage = new AnimatedSprite("images/mainmenu/mainmenu.png", 1, 2);
    title = new AnimatedSprite("images/mainmenu/title.png", 1, 4);

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2-40));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+110));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);

    //inicia anima\u00e7\u00f5es da sprite
    backgroundImage.setAnimation(0, 1, 2, true);
    title.setAnimation(0, 3, 2, true);

    title.position = new PVector(width/2 - title.frameWidth/2, 10); //centra a imagem do titulo
  }

  //desenha os elementos do menu
  public void drawScene() {
    imageMode(CENTER);
    backgroundImage.update();
    title.update();

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
      currentScene = getNextMiniGame();
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

  AnimatedSprite otherCat;

  PImage instructions;
  PImage end;
  PImage ohno;

  PVector pawPosition;

  float enemySpeed = 7f;

  //Inicia as vari\u00e1veis do jogo. Mesmo que em todos os outros jogos.
  SlapCatGame() {
    super();

    backgroundImage = loadImage("images/slapcatgame/background.png");
    playerImage = loadImage("images/slapcatgame/player.png");
    catArm = loadImage("images/slapcatgame/catpaw.png");
    otherCat = new AnimatedSprite("images/slapcatgame/othercat.png", 1, 2);

    instructions = loadImage("images/slapcatgame/instructions.png");
    end = loadImage("images/drownfishgame/end.png");
    ohno = loadImage("images/drownfishgame/ohno.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    otherCat.setAnimation(0, 0, 1, false);
    //inicializa posi\u00e7\u00f5es dos elementos de jogo.
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    gameTime = 2.5f; //inicia o tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    background(backgroundImage);

    imageMode(CENTER);
    updatePawPosition(); //atualiza a posi\u00e7\u00e3o da pata

    image(playerImage, width/2+125, height/2, playerImage.width, playerImage.height);

    imageMode(CORNER);
    otherCat.update();

    super.drawScene(); //chama o m\u00e9todo draw do pai
  }

  //faz a atualiza\u00e7\u00e3o da pata que \u00e9 controlada pela posi\u00e7\u00e3o do rato. Como o image mode \u00e9 centrado \u00e9 necess\u00e1rio fazer c\u00e1lculos para que o pivot da pata seja no canto inferior esquerdo.
  public void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2)+mouseX, 400, width - 280);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 230, height - 170);

    image(catArm, pawPosition.x, pawPosition.y, catArm.width, catArm.height);
  }

  //verifica se houve uma colis\u00e3o da pata com o inimigo (enemy). \u00c9 verificada a sobreposi\u00e7\u00e3o de imagens para isto.
  //se houver colis\u00e3o passa para o estado game win.
  public void checkColision() {
    if(pawPosition.x > otherCat.position.x && pawPosition.x < otherCat.position.x + (otherCat.frameWidth)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      otherCat.setAnimation(1, 1, 1, false);
      status = GameStatus.GAME_WIN;
    }
  }

  //override dos m\u00e9todos abstratos do pai para conter elementos espeficos do jogo
  public @Override void gameStartDraw() {
    imageMode(CORNER);
    image(instructions, 0, 0);
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    otherCat.position.x += enemySpeed;  //move o inimigo
    checkColision();  //verifica a colis\u00e3o: condi\u00e7\u00e3o de vit\u00f3ria
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    image(ohno, 0, 0);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    image(end, 0, 0);
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
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    otherCat.setAnimation(0, 0, 1, false);

    super.startScene(); //chama m\u00e9todo do pai
  }
}
//Mini jogo de dar uma patada a outro gato. Classe filha de GameScene.
class TailGame extends GameScene {
  PImage backgroundImage;

  AnimatedSprite player;

  PImage instructions;
  PImage end;
  PImage ohno;

  Button gameOverBtn;

  float rotateAngle = -1f;
  float speed = -0.1f;
  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  //Inicia as vari\u00e1veis do jogo. Mesmo que em todos os outros jogos.
  TailGame() {
    super();

    backgroundImage = loadImage("images/tailgame/background.png");
    player = new AnimatedSprite("images/tailgame/player.png", 3, 1);
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    instructions = loadImage("images/tailgame/instructions.png");
    end = loadImage("images/tailgame/end.png");
    ohno = loadImage("images/tailgame/ohno.png");

    keyTimerStart = millis();

    player.setAnimation(0, 0, 10, true);
    gameTime = 7f; //tempo de jogo

    player.position = new PVector(0, 0);
  }

  //desenha os elementos comuns a todos os estados de jogo
  public void drawScene() {
    background(backgroundImage);

    //push e pop matrix porque s\u00e3o feitas transforma\u00e7\u00f5es no jogador
    pushMatrix();
    translate(width/2, height/2);
    rotate(rotateAngle);
    translate(-player.frameWidth/2, -player.frameHeight/2); //roda no centro; imagem move-se metade do seu tamanho para ficar centrada no ponto rodado
    player.update();
    popMatrix();

    inputs(); //verifica os inputs

    super.drawScene(); //chama m\u00e9todo do pai
  }

  //override dos m\u00e9todos abstratos do pai para conter elementos espeficos do jogo
  public @Override void gameStartDraw() {
    imageMode(CORNER);
    image(instructions, 0, 0);
  }

  public @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    rotateAngle += speed; //gato vai rodando ao longo do tempo
  }

  public @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    image(ohno, 0, 0);
    gameOverBtn.render();
  }

  public @Override void gameWinDraw() {
    imageMode(CORNER);
    image(end, 0, 0);

    player.setAnimation(2, 2, 1, false);
  }

  //verifica os inputs
  public void inputs() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else if (!keyPressed ){
      pressingKey = false;
    }

    //usa a mesma logica que o drownfishgame
    //evita que o jogador apenas clique na tecla para atingir a velocidade desejada.
    //reinicia a velocidade ap\u00f3s x segundos a carregar ou quando nenhuma tecla \u00e9 pressionada.
    if(pressingKey) {
      keyTimer = millis() - keyTimerStart; //conta o tempo passado desde que a tecla espa\u00e7o foi premida.

      if((keyTimer/1000) > 0.5f && speed < -0.1f) {
        speed = constrain(speed + 0.1f, -1f, -0.1f);
      }
    }
    else {
      speed = constrain(speed + 0.01f, -0.5f, -0.1f); //limita a velocidade do gato
    }

    if(speed == -0.5f) {
       status = GameStatus.GAME_WIN; //se a velocidade chegar ao velor definido ent\u00e3o a condi\u00e7\u00e3o de vit\u00f3ria \u00e9 alcan\u00e7ada.
    }
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

  //na a\u00e7\u00e3o de clique do bot\u00e3o, passa para o menu inicial
  public void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu; //atualiza a cena atual
    }
  }

  //verifica as teclas que s\u00e3o premidas
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

  //reinicia os valores da cena
  public void startScene() {
    keyTimerStart = millis();
    timerStart = millis();
    rotateAngle = -1f;
    speed = -0.1f;

    player.setAnimation(0, 0, 10, true);
    player.position = new PVector(0, 0);

    super.startScene(); //chama o m\u00e9todo do pai
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
