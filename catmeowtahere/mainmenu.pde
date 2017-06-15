class MainMenu implements Scene {
  PImage backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  ArrayList<Button> buttons = new ArrayList();

  MainMenu(){
    backgroundImage = loadImage("images/mainmenu.png");

    newGameBtn = new Button("images/newgame.png", new PVector(width/2, height/2+60));
    exitBtn = new Button("images/exit.png", new PVector(width/2, height/2+160));

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
    if(newGameBtn.isMouseOnBtn()) {
      newGameBtn.pressed();
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit();
    }
  }

  void checkForReleases() {
    for(Button btn : buttons) {
      btn.released();
    }
  }
}
