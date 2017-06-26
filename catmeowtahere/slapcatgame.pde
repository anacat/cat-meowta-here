//Mini jogo de dar uma patada a outro gato. Classe filha de GameScene.
class SlapCatGame extends GameScene {
  Button gameOverBtn;
  PImage playerImage;
  PImage backgroundImage;
  PImage catArm;

  AnimatedSprite otherCat;

  PImage instructions;
  PImage end;
  PImage ohno;

  PVector pawPosition;

  float enemySpeed = 7f;

  //Inicia as variáveis do jogo. Mesmo que em todos os outros jogos.
  SlapCatGame() {
    super();

    backgroundImage = loadImage("images/slapcatgame/background.png");
    playerImage = loadImage("images/slapcatgame/player.png");
    catArm = loadImage("images/slapcatgame/catpaw.png");
    otherCat = new AnimatedSprite("images/slapcatgame/othercat.png", 1, 2);

    instructions = loadImage("images/slapcatgame/instructions.png");
    end = loadImage("images/drownfishgame/end.png");
    ohno = loadImage("images/drownfishgame/ohno.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    otherCat.setAnimation(0, 0, 1, false);
    //inicializa posições dos elementos de jogo.
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    gameTime = 2.5f; //inicia o tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  void drawScene() {
    background(backgroundImage);

    imageMode(CENTER);
    updatePawPosition(); //atualiza a posição da pata

    image(playerImage, width/2+125, height/2, playerImage.width, playerImage.height);

    imageMode(CORNER);
    otherCat.update();

    super.drawScene(); //chama o método draw do pai
  }

  //faz a atualização da pata que é controlada pela posição do rato. Como o image mode é centrado é necessário fazer cálculos para que o pivot da pata seja no canto inferior esquerdo.
  void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2)+mouseX, 400, width - 280);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 230, height - 170);

    image(catArm, pawPosition.x, pawPosition.y, catArm.width, catArm.height);
  }

  //verifica se houve uma colisão da pata com o inimigo (enemy). É verificada a sobreposição de imagens para isto.
  //se houver colisão passa para o estado game win.
  void checkColision() {
    if(pawPosition.x > otherCat.position.x && pawPosition.x < otherCat.position.x + (otherCat.frameWidth)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      otherCat.setAnimation(1, 1, 1, false);
      status = GameStatus.GAME_WIN;
    }
  }

  //override dos métodos abstratos do pai para conter elementos espeficos do jogo
  @Override void gameStartDraw() {
    imageMode(CORNER);
    image(instructions, 0, 0);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    otherCat.position.x += enemySpeed;  //move o inimigo
    checkColision();  //verifica a colisão: condição de vitória
  }

  @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    imageMode(CORNER);
    image(ohno, 0, 0);
    gameOverBtn.render();
  }

  @Override void gameWinDraw() {
    imageMode(CORNER);
    image(end, 0, 0);
  }

  //verifica inputs do rato para os botões desenhados
  void checkForPresses() {
    if(gameOverBtn.isMouseOnBtn()) {
      gameOverBtn.pressed();
    }
  }

  void checkForReleases() {
    gameOverBtn.released();
  }

  //na ação de clique passa para o menu inicial
  void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu;
    }
  }

  void checkForKeyPresses() {

  }

  //reinicia valores da cena
  void startScene() {
    otherCat.position = new PVector(-otherCat.frameWidth, height-otherCat.frameHeight);
    pawPosition = new PVector();

    otherCat.setAnimation(0, 0, 1, false);

    super.startScene(); //chama método do pai
  }
}
