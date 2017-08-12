class FirstScene implements Scene{
  AnimatedSprite backgroundImage;
  AnimatedSprite title;
  Button okBtn;

  FirstScene() {
    okBtn = new Button("images/firstscene/btn.png", new PVector(0, 0));

    backgroundImage = new AnimatedSprite("images/firstscene/background.png", 1, 2);
    title = new AnimatedSprite("images/firstscene/title.png", 1, 2);

    backgroundImage.setAnimation(0, 1, 2, true);
    title.setAnimation(0, 1, 2, true);

    backgroundImage.position = new PVector(width/2 - backgroundImage.frameWidth, height - backgroundImage.frameHeight);
    title.position = new PVector(width/2 - title.frameWidth/2, 50); //centra a imagem do titulo
    okBtn.btnPosition = new PVector(width - okBtn.btnImage.width + 30, height - okBtn.btnImage.height);
  }

  void drawScene() {
    background(249, 213, 211);
    backgroundImage.update();
    title.update();

    okBtn.render();
  }

  void checkForPresses() {
    if(okBtn.isMouseOnBtn()) {
      okBtn.pressed();
    }
  }

  void checkForReleases() {
    okBtn.released();
  }

  void checkForClicks() {
    if(okBtn.isMouseOnBtn()) {
      currentScene = getNextMiniGame();
    }
  }

  void checkForKeyPresses() {

  }

  void startScene() {

  }
}
