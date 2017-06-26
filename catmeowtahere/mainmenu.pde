//Classe para o menu inicial. Implementa a interface Scene
class MainMenu implements Scene {
  AnimatedSprite backgroundImage;
  AnimatedSprite title;
  Button newGameBtn;
  Button exitBtn;

  //array de botões para facilitar o controlo dos mesmos. Porque sim.
  ArrayList<Button> buttons = new ArrayList<Button>();

  //inicializa os elementos do menu
  MainMenu(){
    backgroundImage = new AnimatedSprite("images/mainmenu/mainmenu.png", 1, 2);
    title = new AnimatedSprite("images/mainmenu/title.png", 1, 4);

    newGameBtn = new Button("images/mainmenu/newgame.png", new PVector(width/2, height/2-40));
    exitBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2+110));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);

    //inicia animações da sprite
    backgroundImage.setAnimation(0, 1, 2, true);
    title.setAnimation(0, 3, 2, true);

    title.position = new PVector(width/2 - title.frameWidth/2, 10); //centra a imagem do titulo
  }

  //desenha os elementos do menu
  void drawScene() {
    imageMode(CENTER);
    backgroundImage.update();
    title.update();

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
      currentScene = getNextMiniGame();
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
