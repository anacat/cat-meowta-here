Scene currentScene; //objecto para guardar a cena atual.
MainMenu mainMenu;

TailGame tailGame;
SlapCatGame slapGame;
DrownFishGame drownFishGame;
HitTheVaseGame hitTheVaseGame;

ArrayList<GameScene> miniGames = new ArrayList<GameScene>();
ArrayList<Integer> playableGames;

void setup() {
  size(720, 480);
  //define o tipo de render da aplicação (para poder "limpar" o ecrã ao usar uma imagem como fundo de uma cena).
  createGraphics(width, height, JAVA2D);
  textFont(loadFont("MicrosoftYaHeiLight-48.vlw"));

  mainMenu = new MainMenu();
  tailGame = new TailGame();
  slapGame = new SlapCatGame();
  drownFishGame = new DrownFishGame();
  hitTheVaseGame = new HitTheVaseGame();

  miniGames.add(tailGame);
  miniGames.add(slapGame);
  miniGames.add(drownFishGame);
  miniGames.add(hitTheVaseGame);

  playableGames = new ArrayList<Integer>(miniGames.size());

  playableGames.add(0);
  playableGames.add(1);
  playableGames.add(2);
  playableGames.add(3);

  currentScene = mainMenu;
}

Scene getNextMiniGame() {
  if(playableGames.size() > 0) {
    int randomScene = (int)random(0, playableGames.size());

    int sceneNumber = playableGames.get(randomScene);

    playableGames.remove(randomScene);

    Scene scene = miniGames.get(sceneNumber);
    scene.startScene();

    return scene;
  }
  else {
    mainMenu.startScene();
    return mainMenu;
  }
}

void draw() {
  currentScene.drawScene();
}

void mousePressed() {
  currentScene.checkForPresses();
}

void mouseReleased() {
  currentScene.checkForReleases();
}

void mouseClicked() {
  currentScene.checkForClicks();
}

void keyPressed() {
}
