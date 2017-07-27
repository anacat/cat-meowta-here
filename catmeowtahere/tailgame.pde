class TailGame extends GameScene {
  PImage backgroundImage;

  AnimatedSprite player;

  AnimatedSprite instructions;
  AnimatedSprite end;
  AnimatedSprite ohno;

  Button gameOverBtn;

  float rotateAngle = -1f;
  float speed = -0.1f;
  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  TailGame() {
    super();

    backgroundImage = loadImage("images/tailgame/background.png");
    player = new AnimatedSprite("images/tailgame/player.png", 3, 1);
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    instructions = new AnimatedSprite("images/tailgame/instructions.png", 1, 2);
    end = new AnimatedSprite("images/tailgame/end.png", 1, 2);
    ohno = new AnimatedSprite("images/tailgame/ohno.png", 1, 2);

    instructions.position = new PVector(width/2 - instructions.frameWidth/2, height/2 - instructions.frameHeight/2);
    end.position = new PVector(width/2 - end.frameWidth/2, height/2 - end.frameHeight/2);
    ohno.position = new PVector(width/2 - ohno.frameWidth/2, height/2 - ohno.frameHeight/2);

    keyTimerStart = millis();

    player.setAnimation(0, 0, 2, true);
    instructions.setAnimation(0, 1, 2, true);
    ohno.setAnimation(0, 1, 2, true);
    end.setAnimation(0, 1, 2, true);
    gameTime = 7f;

    player.position = new PVector(0, 0);
  }

  void drawScene() {
    background(backgroundImage);

    pushMatrix();
    translate(width/2, height/2);
    rotate(rotateAngle);
    translate(-player.frameWidth/2, -player.frameHeight/2);
    player.update();
    popMatrix();

    inputs();

    super.drawScene();
  }

  @Override void gameStartDraw() {
    imageMode(CORNER);
    instructions.update();
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    rotateAngle += speed;
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

    player.setAnimation(2, 2, 1, false);
  }

  void inputs() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else if (!keyPressed ){
      pressingKey = false;
    }

    if(pressingKey) {
      keyTimer = millis() - keyTimerStart;

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
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  void checkForKeyPresses() {
    if(status == GameStatus.GAME_RUNNING) {
      if(key == ' ' && !pressingKey) {
        pressingKey = true;
        keyTimerStart = millis();
        speed = constrain(speed - 0.05f, -0.5f, -0.1f);
      }
      else if(key != ' '){
        pressingKey = false;
      }
    }
  }

  void startScene() {
    keyTimerStart = millis();
    timerStart = millis();
    rotateAngle = -1f;
    speed = -0.1f;

    player.setAnimation(0, 0, 10, true);
    player.position = new PVector(0, 0);

    super.startScene();
  }
}
