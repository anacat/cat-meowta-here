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

  void drawScene() {
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

  void gameStart() {
    if(gameTimer/1000 < 3f) {
      gameStartDraw();
    }
    else {
      timerStart = millis();
      status = GameStatus.GAME_RUNNING;
    }
    gameTimer = millis() - timerStart;
  }

  void gameRunning() {
    if(gameTimer/1000 > gameTime) {
      status = GameStatus.GAME_OVER;
    }
    else {
      gameRunningDraw();
      gameTimer = millis() - timerStart;
    }
  }

  void gameOver() {
    gameOverDraw();
  }

  void gameWin() {
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

  abstract void gameRunningDraw();
  abstract void gameOverDraw();
  abstract void gameWinDraw();

  void checkForPresses() {}
  void checkForReleases() {}
  void checkForClicks() {}
  void checkForKeyPresses() {}

  void startScene() {
    restartedTimer = false;
    timerStart = millis();
    gameTimer = millis() - timerStart;

    status = GameStatus.GAME_STARTING;
  }
}
