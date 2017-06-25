//Mini jogo de atirar com o vaso para o chão. Classe filha de GameScene.
class HitTheVaseGame extends GameScene {
  PImage backgroundImage;
  Button gameOverBtn;

  PImage playerImage;
  PImage catArm;
  PImage vaseImage;
  PVector pawPosition;
  PVector enemyPosition;
  float enemyRotation;

  //Inicia as variáveis do jogo. Mesmo que em todos os outros jogos.
  HitTheVaseGame() {
    super();

    playerImage = loadImage("images/hitthevasegame/player.png");
    catArm = loadImage("images/hitthevasegame/paw.png");
    vaseImage = loadImage("images/hitthevasegame/vase.png");
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    //inicializa a posição da patinha e do vaso (enemy)
    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 125, height/2 + 50);
    enemyRotation = 0;

    gameTime = 1f; //tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  void drawScene() {
    background(255);

    imageMode(CENTER);
    updatePawPosition(); //atualiza a posição da pata
    image(playerImage, width/2 + 125, height/2);

    //push e pop matrix porque são feitas transformações no vaso
    pushMatrix();
    translate(enemyPosition.x, enemyPosition.y);
    rotate(enemyRotation);
    image(vaseImage, 0, 0);
    popMatrix();

    super.drawScene(); //chama o método draw do pai
  }

  //faz a atualização da pata que é controlada pela posição do rato. Como o image mode é centrado é necessário fazer cálculos para que o pivot da pata seja no canto inferior esquerdo.
  void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width)/2) + mouseX, 350, width - 250);
    pawPosition.y = constrain(mouseY - ((catArm.height)/2), 145, height - 150);

    image(catArm, pawPosition.x, pawPosition.y);
  }

  //verifica se houve uma colisão da pata com o vaso (enemy). É verificada a sobreposição de imagens para isto.
  //se houver colisão passa para o estado game win.
  void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (vaseImage.width)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      status = GameStatus.GAME_WIN;
    }
  }

  //override dos métodos abstratos do pai para conter elementos espeficos do jogo
  @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("slap teh vase", width/2, height/2);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    //verifica a colisão: condição de vitória
    checkColision();
  }

  @Override void gameOverDraw() {
    fill(0);
    rect(10, 10, width-20, 10);

    fill(50);
    text("game over binch", width/2, height/2);
    gameOverBtn.render();
  }

  @Override void gameWinDraw() {
    textSize(32);
    fill(50);
    text("oh shit!!!!", width/2, height/2);

    //vaso roda como se tivesse caído
    enemyPosition = new PVector(width/2 - 100, height/2 + 150);
    enemyRotation = -HALF_PI;
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

  //reinicia valores da cena
  void startScene() {
    pawPosition = new PVector();
    enemyPosition = new PVector(width/2 - 125, height/2 + 50);
    enemyRotation = 0;

    super.startScene(); //chama método do pai
  }
}
