class FirstScene implements Scene{
  Button okBtn;

  FirstScene() {
    okBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));
  }

  void drawScene() {
    background(255);

    //maybe put an animated gif and show the text at the end of the gif?

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
