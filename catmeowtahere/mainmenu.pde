class MainMenu implements Scene {
  PImage backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  ArrayList<Button> buttons = new ArrayList<Button>();

  MainMenu(){
    backgroundImage = loadImage("images/mainmenu/mainmenu.png");

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2+60));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);
  }

  void drawScene() {
    background(backgroundImage);

    for(Button btn : buttons) {
      btn.render();
    }

    if(newGameBtn.isMouseOnBtn() || exitBtn.isMouseOnBtn()){
      cursor(HAND);
    }
    else {
      cursor(ARROW);
    }
  }

  void checkForPresses() {
    for(Button btn : buttons) {
      if(btn.isMouseOnBtn()) {
        btn.pressed();
      }
    }
  }

  void checkForReleases() {
    for(Button btn : buttons) {
      btn.released();
    }
  }

  void checkForClicks() {
    if(newGameBtn.isMouseOnBtn()) {
      currentScene = slapGame;
      cursor(ARROW);
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit();
    }
  }

  void checkForKeyPresses() {
  }

  void restartScene() {
  }
}
