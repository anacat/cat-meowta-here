//uma classe abstrata explicita para as cenas de jogos. Implementa tudo de uma cena mas acrescenta mais algumas funções.
//Após a criação de uma ou duas cenas de jogos, foi criada esta classe de maneira a abstraír alguns comportamentos e definições comuns como o temporizador, estados do jogo (game start, game running, game over, game win).
//O uso de uma classe abstrata deve-se ao uso de métodos abstratos que facilitam a implementação do jogo.
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

  //função que renderiza o estado atual do jogo
  void drawScene() {
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

  //no inicio do jogo o temporizador corre para dar inicio ao jogo, aqui é apresentada a cena de jogo. Quando o temporizador acaba, o estado muda para game running.
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

  //estado de jogo. Cada jogo tem um tempo limite (definido no inicio). Se o jogador não obter a condição de vitória e o tempo acabar, o jogo passa para o estado game over.
  void gameRunning() {
    if(gameTimer/1000 > gameTime) {
      status = GameStatus.GAME_OVER;
    }
    else {
      gameRunningDraw();
      gameTimer = millis() - timerStart;
    }
  }

  //estado game over. É desenhado o ecrã de jogo perdido.
  void gameOver() {
    gameOverDraw();
  }

  //estado game win. É desenhado o ecrã de vitória. O ecrã de vitória é renderizado durante 3 segundos, quando acaba esse tempo passa para o próximo jogo. A cena atual é também reiniciada para evitar erros ao repetir o jogo.
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
      currentScene = getNextMiniGame(); //chose rnadom scene
    }
  }

  //métodos abstratos para a definição e renderização de objectos especificos a cada jogo. No gameRunningDraw também é verificada a condição de vitória para acabar o jogo.
  abstract void gameStartDraw();
  abstract void gameRunningDraw();
  abstract void gameOverDraw();
  abstract void gameWinDraw();

  //métodos vindos da interface de Scene
  void checkForPresses() {}
  void checkForReleases() {}
  void checkForClicks() {}
  void checkForKeyPresses() {}

  //método para reiniciar a cena
  void startScene() {
    restartedTimer = false;
    timerStart = millis();
    gameTimer = millis() - timerStart;

    status = GameStatus.GAME_STARTING;
  }
}
