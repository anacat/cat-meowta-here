class HitTheVaseGame extends GameScene {
  PImage backgroundImage;
  Button gameOverBtn;

  PImage playerImage;
  PImage catArm;
  PImage vaseImage;
  PVector pawPosition;
  PVector enemyPosition;
  float enemyRotation;

  HitTheVaseGame() {
    super();

    playerImage = loadImage("images/hitthevasegame/player.png");
    catArm = loadImage("images/hitthevasegame/paw.png");
    vaseImage = loadImage("images/hitthevasegame/vase.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 125, height/2 + 50);
    enemyRotation = 0;

    gameTime = 1f;
  }

  void drawScene() {
    background(255);

    imageMode(CENTER);
    updatePawPosition();
    image(playerImage, width/2 + 125, height/2);

    pushMatrix();
    translate(enemyPosition.x, enemyPosition.y);
    rotate(enemyRotation);
    image(vaseImage, 0, 0);
    popMatrix();

    super.drawScene();
  }

  void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2) + mouseX, 350, width - 250);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 145, height - 150);

    image(catArm, pawPosition.x, pawPosition.y);
  }

  void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (vaseImage.width)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      status = GameStatus.GAME_WIN;
    }
  }

  @Override void gameStartDraw() {
    fill(50);
    text("slap teh vase", width/2, height/2);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    checkColision();
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
    text("oh shit!!!!", width/2, height/2);

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
    enemyPosition = new PVector(width/2 - 125, height/2 + 50);
    enemyRotation = 0;

    super.startScene();
  }
}
