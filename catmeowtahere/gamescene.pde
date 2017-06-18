abstract class GameScene implements Scene {
  PImage backgroundImage;

  protected float gameTimer;
  protected float timerStart;

  protected int status;

  GameScene() {
    timerStart = millis();

    status = GameStatus.GAME_STARTING;
  }

  void drawScene() {
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

  abstract void gameStart();
  abstract void gameRunning();
  abstract void gameOver();
  abstract void gameWin();

  void checkForPresses() {

  }

  void checkForReleases() {

  }

  void checkForClicks() {

  }

 void checkForKeyPresses() {

  }

  void restartScene() {

  }
}
