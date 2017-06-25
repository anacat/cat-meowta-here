//Classe para o menu inicial. Implementa a interface Scene
class MainMenu implements Scene {
  PImage backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  //array de botões para facilitar o controlo dos mesmos. Porque sim.
  ArrayList<Button> buttons = new ArrayList<Button>();

  //inicializa os elementos do menu
  MainMenu(){
    backgroundImage = loadImage("images/mainmenu/mainmenu.png");

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2+60));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+160));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);
  }

  //desenha os elementos do menu
  void drawScene() {
    background(backgroundImage);

    //renderiza os botões
    for(Button btn : buttons) {
      btn.render();
    }

    //se tiver em cima do botão o cursor passa a ser a mão, se não é a
    if(newGameBtn.isMouseOnBtn() || exitBtn.isMouseOnBtn()){
      cursor(HAND);
    }
    else {
      cursor(ARROW);
    }
  }

  //verifica inputs do rato para os botões da cena
  void checkForPresses() {
    for(Button btn : buttons) {
      if(btn.isMouseOnBtn()) {
        btn.pressed(); //passa para o estado pressed quando no mouseDown
      }
    }
  }

  void checkForReleases() {
    for(Button btn : buttons) {
      btn.released(); //passa para o estado released quando o botão do rato é largado
    }
  }

  //efectua a ação quando o rato está em cima do botão e é efectuado um evento de clique
  void checkForClicks() {
    if(newGameBtn.isMouseOnBtn()) {
      currentScene = firstScene;
      cursor(ARROW);
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit(); //fecha o jogo
    }
  }

  //não há eventos de teclado por isso fica vazio. Este método é obrigatório de ter pois pretence à interface que implementamos
  void checkForKeyPresses() {
  }

  //reinicia o número de mini jogos disponíveis
  void startScene() {
    playableGames.add(0);
    playableGames.add(1);
    playableGames.add(2);
    playableGames.add(3);
  }
}
