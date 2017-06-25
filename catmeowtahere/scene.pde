//Uso de uma inteface para facilitar o manuseamento de cenas na classe principal. Todas as cenas têm os mesmos métodos: desenhar cena, verificar inputs e reiniciar a cena para o seu estado inicial.
interface Scene {
  void drawScene();
  void checkForPresses();
  void checkForReleases();
  void checkForClicks();
  void checkForKeyPresses();
  void startScene();
}
//Porquê uma interface? Não sei, quis experimentar.
