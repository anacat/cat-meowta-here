class DrownFishGame extends GameScene {
  PImage playerState;
  PImage fundo;
  Button gameOverBtn;

  AnimatedSprite fishBowl;
  AnimatedSprite catte;

  AnimatedSprite instructions;
  AnimatedSprite end;
  AnimatedSprite ohno;

  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  int waterDrank;

  DrownFishGame() {
    super();

    fundo = loadImage("images/drownfishgame/fundo.png");
    fishBowl = new AnimatedSprite("images/drownfishgame/bowl.png", 3, 3);
    catte = new AnimatedSprite("images/drownfishgame/catte.png", 2, 1);

    instructions = new AnimatedSprite("images/drownfishgame/instructions.png", 1, 2);
    end = new AnimatedSprite("images/drownfishgame/end.png", 1, 2);
    ohno = new AnimatedSprite("images/drownfishgame/ohno.png", 1, 2);

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    fishBowl.setAnimation(0, 0, 1, false);
    catte.setAnimation(1, 1, 1, false);
    instructions.setAnimation(0, 1, 2, true);
    end.setAnimation(0, 1, 2, true);
    ohno.setAnimation(0, 1, 2, true);

    instructions.position = new PVector(width/2 - instructions.frameWidth/2, height/2 - instructions.frameHeight/2);
    end.position = new PVector(width/2 - end.frameWidth/2, height/2 - end.frameHeight/2);
    ohno.position = new PVector(width/2 - ohno.frameWidth/2, height/2 - ohno.frameHeight/2);

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
    instructions.update();
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
    ohno.update();
    gameOverBtn.render();
  }

  @Override void gameWinDraw() {
    imageMode(CORNER);
    end.update();
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
