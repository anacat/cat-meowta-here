//Uso de uma inteface para facilitar o manuseamento de cenas na classe principal. Todas as cenas têm os mesmos métodos.
interface Scene {
  void drawScene();
  void checkForPresses();
  void checkForReleases();
  void checkForClicks();
  void checkForKeyPresses();
  void restartScene();
}
