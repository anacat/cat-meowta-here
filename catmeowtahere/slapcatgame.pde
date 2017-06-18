class SlapCatGame extends GameScene {
  Button gameOverBtn;
  PImage playerImage;
  PImage backgroundImage;
  PImage catArm;

  PImage otherCatImage1;
  PImage otherCatImage2;
  PImage enemyImage;

  PVector enemyPosition;
  PVector pawPosition;

  float playerSize;
  float enemySpeed = 7f;

  SlapCatGame() {
    super();

    playerImage = loadImage("images/slapcatgame/player.png");
    catArm = loadImage("images/slapcatgame/catpaw.png");
    otherCatImage1 = loadImage("images/slapcatgame/othercat1.png");
    otherCatImage2 = loadImage("images/slapcatgame/othercat2.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    playerSize = 0.7f;
    enemyPosition = new PVector(-otherCatImage1.width * playerSize, 200);
    pawPosition = new PVector();

    enemyImage = otherCatImage1;
    gameTime = 2.5f;
  }

  void drawScene() {
    background(255);

    imageMode(CENTER);
    updatePawPosition();

    image(playerImage, width/2+125, 200, playerImage.width * playerSize, playerImage.height * playerSize);

    imageMode(CORNER);
    image(enemyImage, enemyPosition.x, enemyPosition.y, enemyImage.width * playerSize, enemyImage.height * playerSize);

    super.drawScene();
  }

  void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width*playerSize)/2)+mouseX, 375, width - 275);
    pawPosition.y = constrain(mouseY - ((catArm.height*playerSize)/2), 145, height - 150);

    image(catArm, pawPosition.x, pawPosition.y, catArm.width * playerSize, catArm.height * playerSize);
  }

  void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (enemyImage.width * playerSize)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      enemyImage = otherCatImage2;

      status = GameStatus.GAME_WIN;
    }
  }

  @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("slap teh cat", width/2, height/2);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    enemyPosition.x += enemySpeed;
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
    text("hope ur happy with ur self", width/2, height/2);
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

  }

  void startScene() {
    playerSize = 0.7f;
    enemyPosition = new PVector(-otherCatImage1.width * playerSize, 200);
    pawPosition = new PVector();

    enemyImage = otherCatImage1;

    super.startScene();
  }
}
