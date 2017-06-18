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
  boolean restartedTimer;


  TailGame() {
    playerImage = loadImage("images/tailgame/player.png");
    playerFinishImage = loadImage("images/tailgame/playerFinish.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));
    keyTimerStart = millis();

    playerImageToDraw = playerImage;
  }

  void drawScene() {
    background(255);

    pushMatrix();
    imageMode(CENTER);
    translate(width/2f, height/2f);
    rotate(rotateAngle);
    image(playerImageToDraw, 0, 0);
    popMatrix();

    inputs();

    textAlign(CENTER);

    super.drawScene();
  }

  @Override void gameStart() {
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

  @Override void gameRunning() {
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

  @Override void gameOver() {
    fill(0);
    rect(10, 10, width-20, 10);

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  @Override void gameWin() {
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

  void inputs() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else if (!keyPressed ){
      pressingKey = false;
    }

    //evita que o jogador apenas clique na tecla para atingir a velocidade desejada.
    //reinicia a velocidade após x segundos a carregar ou quando nenhuma tecla é pressionada.
    if(pressingKey) {
      keyTimer = millis() - keyTimerStart; //conta o tempo passado desde que a tecla espaço foi premida.

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

  void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  void checkForReleases() {
    gameOverBtn.released();
  }

  void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      currentScene = mainMenu;
      restartScene();
    }
  }

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

  void restartScene() {
    keyTimerStart = millis();
    timerStart = millis();
    playerImageToDraw = playerImage;
    rotateAngle = -1f;
    speed = -0.1f;
    restartedTimer = false;

    gameTimer = millis() - timerStart;
    status = GameStatus.GAME_STARTING;
  }
}
