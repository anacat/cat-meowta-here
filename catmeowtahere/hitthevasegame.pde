class HitTheVaseGame extends GameScene {
  Button gameOverBtn;

  PImage backgroundImage;
  PImage playerImage;
  PImage catArm;
  PImage vaseImage;
  PVector pawPosition;
  PVector enemyPosition;
  float enemyRotation;

  AnimatedSprite instructions;
  AnimatedSprite end;
  AnimatedSprite ohno;

  HitTheVaseGame() {
    super();

    backgroundImage = loadImage("images/hitthevasegame/background.png");
    playerImage = loadImage("images/hitthevasegame/player.png");
    catArm = loadImage("images/hitthevasegame/paw.png");
    vaseImage = loadImage("images/hitthevasegame/vase.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    instructions = new AnimatedSprite("images/hitthevasegame/instructions.png", 1, 2);
    end = new AnimatedSprite("images/hitthevasegame/end.png", 1, 2);
    ohno = new AnimatedSprite("images/hitthevasegame/ohno.png", 1, 2);

    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 100, height/2 + 50);
    enemyRotation = 0;

    instructions.setAnimation(0, 1, 2, true);
    end.setAnimation(0, 1, 2, true);
    ohno.setAnimation(0, 1, 2, true);

    instructions.position = new PVector(width/2 - instructions.frameWidth/2, height/2 - instructions.frameHeight/2);
    end.position = new PVector(width/2 - end.frameWidth/2, height/2 - end.frameHeight/2);
    ohno.position = new PVector(width/2 - ohno.frameWidth/2, height/2 - ohno.frameHeight/2);

    gameTime = 1f;
  }

  void drawScene() {
    imageMode(CORNER);
    image(backgroundImage, 0, 0);

    imageMode(CENTER);
    updatePawPosition();
    image(playerImage, width/2 + 75, height/2);

    pushMatrix();
    translate(enemyPosition.x, enemyPosition.y);
    rotate(enemyRotation);
    image(vaseImage, 0, 0);
    popMatrix();

    super.drawScene();
  }

  void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2) + mouseX, 360, 390);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 230, 300);

    image(catArm, pawPosition.x, pawPosition.y);
  }

  void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (vaseImage.width)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      status = GameStatus.GAME_WIN;
    }
  }

  @Override void gameStartDraw() {
    imageMode(CORNER);
    instructions.update();
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    checkColision();
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

    enemyPosition = new PVector(width/2 - 100, height/2 + 150);
    enemyRotation = -HALF_PI;
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

  void startScene() {
    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 100, height/2 + 50);
    enemyRotation = 0;

    super.startScene();
  }
}
