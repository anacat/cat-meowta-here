Scene currentScene;
MainMenu mainMenu;

void setup() {
  size(720, 480);
  //define o tipo de render da aplicação (para poder limpar o ecrã ao usar uma image como fundo de uma cena).
  createGraphics(width, height, JAVA2D);

  mainMenu = new MainMenu();
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
