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

  DrownFishGame() {
    playerTongueImage = loadImage("images/drownfishgame/playerTongue.png");
    playerNoTongueImage = loadImage("images/drownfishgame/playerNoTongue.png");
    fishBowl = loadImage("images/drownfishgame/bowl.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    playerState = playerNoTongueImage;
    playerSize = 0.5f;
    waterDrank = 0;

    gameTime = 3f;
  }

  void drawScene() {
    background(255);
    imageMode(CENTER);
    image(playerState, width/2, height/2, playerState.width * playerSize, playerState.height * playerSize);

    image(fishBowl, width/2 - 50, height/2);

    super.drawScene();
  }

  void update() {
    //update fishbowl sprites
    
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

  @Override void gameStartDraw() {
    fill(50);
    text("drink all teh water", width/2, height/2);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

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

  void restartScene() {
    playerState = playerNoTongueImage;
    playerSize = 0.5f;
    waterDrank = 0;
    pressingKey = false;

    gameTime = 3f;

    super.restartScene();
  }
}
