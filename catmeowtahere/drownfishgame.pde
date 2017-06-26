//Mini jogo de beber a água do aquário. Classe filha de GameScene.
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
  void drawScene() {
    background(255);
    imageMode(CORNER);
    image(fundo, 0, 0);

    imageMode(CENTER);
    catte.update();
    fishBowl.update();

    //chama a função de draw do pai
    super.drawScene();
  }

  //update da lógica de jogo
  void update() {
    //update fishbowl sprites

    //verifica inputs de teclas
    if(keyPressed) {
      checkForKeyPresses();
    }
    else {
      pressingKey = false;
    }

    //temporizador que impede que o utilizador possa estar a carregar sem largar a tecla. Se estiver a carregar passados mais de x segundos então o pressed key passa a false, impedindo assim um jogo facilitado.
    if(pressingKey) {
      keyTimer = millis() - keyTimerStart;

      if(keyTimer/1000 > 1f) {
        pressingKey = false;
      }
    }
  }

  //override dos métodos abstratos do pai para conter elementos espeficos do jogo
  @Override void gameStartDraw() {
    imageMode(CORNER);
    image(instructions, 0, 0);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    //chama update da lógica de jogo
    update();
  }

  @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    image(ohno, 0, 0);
    gameOverBtn.render();
  }

  @Override void gameWinDraw() {
    imageMode(CORNER);
    image(end, 0, 0);
  }

  //verifica inputs de rato para os botões da cena
  void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  void checkForReleases() {
    gameOverBtn.released();
  }

  //a ação do botão só acontece quando há uma ação de clique
  //se estiver em game over volta para o menu inicial
  void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  void checkForKeyPresses() {
    if(key == ' ' && !pressingKey) {
      pressingKey = true;
      keyTimerStart = millis();

      waterDrank++; //frame da animação do aquário depende do valor da água bebida.
      fishBowl.setAnimation((int)(waterDrank/2.6), (int)(waterDrank/2.6), 1, false); //divide o valor de maneira a obter a mesma frame por mais tempo

      catte.setAnimation(waterDrank % 2, waterDrank % 2, 1, false); //resto da divisão por 2 é sempre 0 ou 1; só temos 2 frames

      if(waterDrank == 20) {
        status = GameStatus.GAME_WIN;
      }
    }
    else if (key != ' ') {
      pressingKey = false;
    }
  }

  void startScene() {
    waterDrank = 0;
    pressingKey = false;
    fishBowl.setAnimation(0, 0, 1, false);
    catte.setAnimation(1, 1, 1, false);

    super.startScene();
  }
}
