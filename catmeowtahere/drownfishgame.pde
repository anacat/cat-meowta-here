//Mini jogo de beber a água do aquário. Classe filha de GameScene.
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
  void drawScene() {
    background(255);
    imageMode(CENTER);
    image(playerState, width/2, height/2, playerState.width * playerSize, playerState.height * playerSize);

    image(fishBowl, width/2 - 50, height/2);

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
    fill(50);
    textSize(50);
    text("drink all teh water", width/2, height/2);
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

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  @Override void gameWinDraw() {
    textSize(32);
    fill(50);
    text("u monster", width/2, height/2);
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

  void startScene() {
    playerState = playerNoTongueImage;
    playerSize = 0.5f;
    waterDrank = 0;
    pressingKey = false;

    super.startScene();
  }
}
