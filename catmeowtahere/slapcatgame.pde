//Mini jogo de dar uma patada a outro gato. Classe filha de GameScene.
class SlapCatGame extends GameScene {
  Button gameOverBtn;
  PImage playerImage;
  PImage backgroundImage;
  PImage catArm;

  PImage otherCatImage1;
  PImage otherCatImage2;
  PImage enemyImage;

  PVector enemyPosition;
  PVector pawPosition;

  float playerSize;
  float enemySpeed = 7f;

  //Inicia as variáveis do jogo. Mesmo que em todos os outros jogos.
  SlapCatGame() {
    super();

    playerImage = loadImage("images/slapcatgame/player.png");
    catArm = loadImage("images/slapcatgame/catpaw.png");
    otherCatImage1 = loadImage("images/slapcatgame/othercat1.png");
    otherCatImage2 = loadImage("images/slapcatgame/othercat2.png");

    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    playerSize = 0.7f;

    //inicializa posições dos elementos de jogo.
    enemyPosition = new PVector(-otherCatImage1.width * playerSize, 200);
    pawPosition = new PVector();

    enemyImage = otherCatImage1;
    gameTime = 2.5f; //inicia o tempo de jogo
  }

  //desenha os elementos comuns a todos os estados de jogo
  void drawScene() {
    background(255);

    imageMode(CENTER);
    updatePawPosition(); //atualiza a posição da pata

    image(playerImage, width/2+125, 200, playerImage.width * playerSize, playerImage.height * playerSize);

    imageMode(CORNER);
    image(enemyImage, enemyPosition.x, enemyPosition.y, enemyImage.width * playerSize, enemyImage.height * playerSize);

    super.drawScene(); //chama o método draw do pai
  }

  //faz a atualização da pata que é controlada pela posição do rato. Como o image mode é centrado é necessário fazer cálculos para que o pivot da pata seja no canto inferior esquerdo.
  void updatePawPosition() {
    pawPosition.x = constrain(((catArm.width*playerSize)/2)+mouseX, 375, width - 275);
    pawPosition.y = constrain(mouseY - ((catArm.height*playerSize)/2), 145, height - 150);

    image(catArm, pawPosition.x, pawPosition.y, catArm.width * playerSize, catArm.height * playerSize);
  }

  //verifica se houve uma colisão da pata com o inimigo (enemy). É verificada a sobreposição de imagens para isto.
  //se houver colisão passa para o estado game win.
  void checkColision() {
    if(pawPosition.x > enemyPosition.x && pawPosition.x < enemyPosition.x + (enemyImage.width * playerSize)
      && pawPosition.y > 225 && pmouseX != mouseX) {
      enemyImage = otherCatImage2;

      status = GameStatus.GAME_WIN;
    }
  }

  //override dos métodos abstratos do pai para conter elementos espeficos do jogo
  @Override void gameStartDraw() {
    fill(50);
    textSize(50);
    text("slap teh cat", width/2, height/2);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    enemyPosition.x += enemySpeed;  //move o inimigo
    checkColision();  //verifica a colisão: condição de vitória
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
    text("hope ur happy with ur self", width/2, height/2);
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
    playerSize = 0.7f;
    enemyPosition = new PVector(-otherCatImage1.width * playerSize, 200);
    pawPosition = new PVector();

    enemyImage = otherCatImage1;

    super.startScene(); //chama método do pai
  }
}
