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

  DrownFishGame() {
    super();

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

  void drawScene() {
    background(255);
    imageMode(CORNER);
    image(fundo, 0, 0);

    imageMode(CENTER);
    catte.update();
    fishBowl.update();

    super.drawScene();
  }

  void update() {
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
    imageMode(CORNER);
    image(instructions, 0, 0);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

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
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  void checkForKeyPresses() {
    if(key == ' ' && !pressingKey) {
      pressingKey = true;
      keyTimerStart = millis();

      waterDrank++;
      fishBowl.setAnimation((int)(waterDrank/2.6), (int)(waterDrank/2.6), 1, false);
            
      catte.setAnimation(waterDrank % 2, waterDrank % 2, 1, false);

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
