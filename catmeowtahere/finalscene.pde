//Cena de final dos mini jogos
//desenha um botão e apresenta imagens relativas à cena final
//contém um botão para voltar para o menu incial
class FinalScene implements Scene {
  Button okBtn;

  FinalScene() {
    okBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));
  }

  void drawScene() {
    background(255);

    //maybe put an animated gif and show the text at the end of the gif?

    fill(0);
    textSize(20);
    textAlign(CENTER);
    text("u have done it!!!", width/2, height/2);

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
