//Mini jogo de dar uma patada a outro gato. Classe filha de GameScene.
class TailGame extends GameScene {
  PImage backgroundImage;

  AnimatedSprite player;

  PImage instructions;
  PImage end;
  PImage ohno;

  Button gameOverBtn;

  float rotateAngle = -1f;
  float speed = -0.1f;
  float keyTimerStart;
  float keyTimer;
  boolean pressingKey = false;

  //Inicia as variáveis do jogo. Mesmo que em todos os outros jogos.
  TailGame() {
    super();

    backgroundImage = loadImage("images/tailgame/background.png");
    player = new AnimatedSprite("images/tailgame/player.png", 3, 1);
    gameOverBtn = new Button("images/mainmenu/exit.png", new PVector(width/2, height/2 + 70));

    instructions = loadImage("images/tailgame/instructions.png");
    end = loadImage("images/tailgame/end.png");
    ohno = loadImage("images/tailgame/ohno.png");

    keyTimerStart = millis();

    player.setAnimation(0, 0, 10, true);
    gameTime = 7f; //tempo de jogo

    player.position = new PVector(0, 0);
  }

  //desenha os elementos comuns a todos os estados de jogo
  void drawScene() {
    background(backgroundImage);

    //push e pop matrix porque são feitas transformações no jogador
    pushMatrix();
    translate(width/2, height/2);
    rotate(rotateAngle);
    translate(-player.frameWidth/2, -player.frameHeight/2); //roda no centro; imagem move-se metade do seu tamanho para ficar centrada no ponto rodado
    player.update();
    popMatrix();

    inputs(); //verifica os inputs

    super.drawScene(); //chama método do pai
  }

  //override dos métodos abstratos do pai para conter elementos espeficos do jogo
  @Override void gameStartDraw() {
    imageMode(CORNER);
    image(instructions, 0, 0);
  }

  @Override void gameRunningDraw() {
    fill(0);
    rect(10, 10, ((width-20)/gameTime) * (gameTimer/1000), 10);

    rotateAngle += speed; //gato vai rodando ao longo do tempo
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

    player.setAnimation(2, 2, 1, false);
  }

  //verifica os inputs
  void inputs() {
    if(keyPressed) {
      checkForKeyPresses();
    }
    else if (!keyPressed ){
      pressingKey = false;
    }

    //usa a mesma logica que o drownfishgame
    //evita que o jogador apenas clique na tecla para atingir a velocidade desejada.
    //reinicia a velocidade após x segundos a carregar ou quando nenhuma tecla é pressionada.
    if(pressingKey) {
      keyTimer = millis() - keyTimerStart; //conta o tempo passado desde que a tecla espaço foi premida.

      if((keyTimer/1000) > 0.5f && speed < -0.1f) {
        speed = constrain(speed + 0.1f, -1f, -0.1f);
      }
    }
    else {
      speed = constrain(speed + 0.01f, -0.5f, -0.1f); //limita a velocidade do gato
    }

    if(speed == -0.5f) {
       status = GameStatus.GAME_WIN; //se a velocidade chegar ao velor definido então a condição de vitória é alcançada.
    }
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

  //na ação de clique do botão, passa para o menu inicial
  void checkForClicks() {
    if(status == GameStatus.GAME_OVER && gameOverBtn.isMouseOnBtn()) {
      startScene();

      mainMenu.startScene();
      currentScene = mainMenu; //atualiza a cena atual
    }
  }

  //verifica as teclas que são premidas
  void checkForKeyPresses() {
    if(status == GameStatus.GAME_RUNNING) {
      if(key == ' ' && !pressingKey) {
        pressingKey = true;
        keyTimerStart = millis(); //reinicia o contador
        speed = constrain(speed - 0.05f, -0.5f, -0.1f);
      }
      else if(key != ' '){
        pressingKey = false;
      }
    }
  }

  //reinicia os valores da cena
  void startScene() {
    keyTimerStart = millis();
    timerStart = millis();
    rotateAngle = -1f;
    speed = -0.1f;

    player.setAnimation(0, 0, 10, true);
    player.position = new PVector(0, 0);

    super.startScene(); //chama o método do pai
  }
}
