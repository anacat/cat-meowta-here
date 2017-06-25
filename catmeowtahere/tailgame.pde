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

  //Inicia as variáveis do jogo. Mesmo que em todos os outros jogos.
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
  void drawScene() {
    background(255);

    //push e pop matrix porque são feitas transformações no jogador
    pushMatrix();
    imageMode(CENTER);
    translate(width/2f, height/2f);
    rotate(rotateAngle);
    image(playerImageToDraw, 0, 0);
    popMatrix();

    inputs(); //verifica os inputs

    textAlign(CENTER);

    super.drawScene(); //chama método do pai
  }

  //override dos métodos abstratos do pai para conter elementos espeficos do jogo
  @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("catch a tail", width/2, height/2);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    rotateAngle += speed; //gato vai rodando ao longo do tempo
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
    text("u have caught ur tail", width/2, height/2);

    playerImageToDraw = playerFinishImage;
  }

  //verifica os inputs
  void inputs() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else if (!keyPressed ){
      pressingKey = false;
    }

    //usa a mesma logica que o drownfishgame
    //evita que o jogador apenas clique na tecla para atingir a velocidade desejada.
    //reinicia a velocidade após x segundos a carregar ou quando nenhuma tecla é pressionada.
    if(pressingKey) {
      keyTimer = millis() - keyTimerStart; //conta o tempo passado desde que a tecla espaço foi premida.

      if((keyTimer/1000) > 0.5f && speed < -0.1f) {
        speed = constrain(speed + 0.1f, -1f, -0.1f);
      }
    }
    else {
      speed = constrain(speed + 0.01f, -0.5f, -0.1f); //limita a velocidade do gato
    }

    if(speed == -0.5f) {
       status = GameStatus.GAME_WIN; //se a velocidade chegar ao velor definido então a condição de vitória é alcançada.
    }
  }

  //verifica inputs do rato para os botões desenhados
  void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  void checkForReleases() {
    gameOverBtn.released();
  }

  //na ação de clique do botão, passa para o menu inicial
  void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu; //atualiza a cena atual
    }
  }

  //verifica as teclas que são premidas
  void checkForKeyPresses() {
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
  void startScene() {
    keyTimerStart = millis();
    timerStart = millis();
    playerImageToDraw = playerImage;
    rotateAngle = -1f;
    speed = -0.1f;

    super.startScene(); //chama o método do pai
  }
}
