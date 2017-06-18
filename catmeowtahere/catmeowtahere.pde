Scene currentScene; //objecto para guardar a cena atual.
MainMenu mainMenu;
TailGame tailGame;
SlapCatGame slapGame;
DrownFishGame drownFishGame;

void setup() {
  size(720, 480);
  //define o tipo de render da aplicação (para poder "limpar" o ecrã ao usar uma imagem como fundo de uma cena).
  createGraphics(width, height, JAVA2D);
  textFont(loadFont("MicrosoftYaHeiLight-48.vlw"));

  mainMenu = new MainMenu();
  tailGame = new TailGame();
  slapGame = new SlapCatGame();
  drownFishGame = new DrownFishGame();

  currentScene = mainMenu;
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
