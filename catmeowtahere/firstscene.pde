class FirstScene implements Scene{
  Button okBtn;
  //AnimatedSprite cat;

  FirstScene() {
    okBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));
    /*cat = new AnimatedSprite("images/cat.png", 6, 2);

    cat.setAnimation(0, 11, 10, true);
    cat.spriteScale = new PVector(1, 1);
    cat.position = new PVector(100, 20);*/
  }

  void drawScene() {
    background(255);

    //cat.update();

    fill(0);
    textSize(20);
    textAlign(CENTER);
    text("u are a catte and ur home alone to do as u please.\ncause some mayhem or whatever", width/2, height/2);

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
