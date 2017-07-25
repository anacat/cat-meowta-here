class SlapCatGame extends GameScene {
  Button gameOverBtn;
  PImage playerImage;
  PImage backgroundImage;
  PImage catArm;

  AnimatedSprite otherCat;

  PImage instructions;
  PImage end;
  PImage ohno;

  PVector pawPosition;

  float enemySpeed = 7f;

  SlapCatGame() {
    super();

    backgroundImage = loadImage("images/slapcatgame/background.png");
    playerImage = loadImage("images/slapcatgame/player.png");
    catArm = loadImage("images/slapcatgame/catpaw.png");
    otherCat = new AnimatedSprite("images/slapcatgame/othercat.png", 1, 2);

    instructions = loadImage("images/slapcatgame/instructions.png");
    end = loadImage("images/drownfishgame/end.png");
    ohno = loadImage("images/drownfishgame/ohno.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    otherCat.setAnimation(0, 0, 1, false);
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    gameTime = 2.5f;
  }

  void drawScene() {
    background(backgroundImage);

    imageMode(CENTER);
    updatePawPosition();

    image(playerImage, width/2+125, height/2, playerImage.width, playerImage.height);

    imageMode(CORNER);
    otherCat.update();

    super.drawScene();
  }

  void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2)+mouseX, 400, width - 280);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 230, height - 170);

    image(catArm, pawPosition.x, pawPosition.y, catArm.width, catArm.height);
  }

  void checkColision() {
    if(pawPosition.x > otherCat.position.x && pawPosition.x < otherCat.position.x + (otherCat.frameWidth)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      otherCat.setAnimation(1, 1, 1, false);
      status = GameStatus.GAME_WIN;
    }
  }

  @Override void gameStartDraw() {
    imageMode(CORNER);
    image(instructions, 0, 0);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    otherCat.position.x += enemySpeed;
    checkColision();
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

  }

  void startScene() {
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    otherCat.setAnimation(0, 0, 1, false);

    super.startScene();
  }
}
