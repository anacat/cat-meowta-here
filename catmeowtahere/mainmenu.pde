class MainMenu implements Scene {
  AnimatedSprite backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  ArrayList<Button> buttons = new ArrayList<Button>();

  MainMenu(){
    backgroundImage = new AnimatedSprite("images/mainmenu/mainmenu.png", 1, 2);

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2-40));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+110));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);

    backgroundImage.setAnimation(0, 1, 2, true);
  }

  void drawScene() {
    imageMode(CENTER);
    backgroundImage.update();

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
      currentScene = firstScene;
      cursor(ARROW);
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit();
    }
  }

  void checkForKeyPresses() {
  }

  void startScene() {
    playableGames.add(0);
    playableGames.add(1);
    playableGames.add(2);
    playableGames.add(3);
  }
}
