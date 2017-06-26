//Cena de final dos mini jogos
//desenha um botão e apresenta imagens relativas à cena final
//contém um botão para voltar para o menu incial
class FinalScene implements Scene {
  AnimatedSprite backgroundImage;
  Button okBtn;

  FinalScene() {
    okBtn = new Button("images/lastscene/btn.png", new PVector(width/2, height/2+160));

    backgroundImage = new AnimatedSprite("images/lastscene/background.png", 1, 2);
    backgroundImage.setAnimation(0, 0, 2, true);
    okBtn.btnPosition = new PVector(width - okBtn.btnImage.width + 30, height - okBtn.btnImage.height);
  }

  void drawScene() {
    backgroundImage.update();

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
      mainMenu.startScene(); //reinicia menu inicial
      currentScene = mainMenu;
    }
  }

  void checkForKeyPresses() {

  }

  void startScene() {

  }
}
