//"Classe" principal de toda a aplicação.
//Aqui são criados todos os mini jogos e cenas, entre outros elementos.
//Também são guardados elementos essenciais ao jogo.
Scene currentScene; // guarda a cena atual.

//Cenas
MainMenu mainMenu;
FirstScene firstScene;
FinalScene finalScene;

//Jogos
TailGame tailGame;
SlapCatGame slapGame;
DrownFishGame drownFishGame;
HitTheVaseGame hitTheVaseGame;

//Lista com os mini jogos existentes
ArrayList<GameScene> miniGames = new ArrayList<GameScene>();
//Lista com o valor de jogos por jogar (usa valores inteiros para facilitar a busca depois na lista anterior).
ArrayList<Integer> playableGames;

void setup() {
  size(720, 480);
  //define o tipo de render da aplicação (para poder "limpar" o ecrã ao usar uma imagem como fundo de uma cena).
  createGraphics(width, height, JAVA2D);
  textFont(loadFont("MicrosoftYaHeiLight-48.vlw")); //fonte usada pela aplicaçção

  //Inicialização das cenas
  mainMenu = new MainMenu();
  firstScene = new FirstScene();
  finalScene = new FinalScene();

  //Inicialização dos mini jogos.
  tailGame = new TailGame();
  slapGame = new SlapCatGame();
  drownFishGame = new DrownFishGame();
  hitTheVaseGame = new HitTheVaseGame();

  //Adiciona os jogos à lista respectiva
  miniGames.add(tailGame);
  miniGames.add(slapGame);
  miniGames.add(drownFishGame);
  miniGames.add(hitTheVaseGame);

  //Inicializa a lista de jogos disponíveis com o mesmo número de elementos que a lista de jogos adicionados
  playableGames = new ArrayList<Integer>(miniGames.size());

  //Adiciona os valores
  playableGames.add(0);
  playableGames.add(1);
  playableGames.add(2);
  playableGames.add(3);

  currentScene = mainMenu; //define o menu de jogo com a cena inicial.
}

//Função para retornar o próximo mini jogo ou cena.
//Retorna cena pois é o elemento base de qualquer cena: seja jogo ou apena scene.
Scene getNextMiniGame() {
  //se a lista ainda tiver valores, então retorna um mini jogo aleatório
  if(playableGames.size() > 0) {
    int randomScene = (int)random(0, playableGames.size());

    int sceneNumber = playableGames.get(randomScene);

    playableGames.remove(randomScene); //remove o valor do mini jogo escolhido

    Scene scene = miniGames.get(sceneNumber);
    scene.startScene(); //reinicia o jogo, pelo sim pelo não

    return scene; //retorna o jogo selecionado
  }
  else { //se a lista já estiver vazia, então devolve a cena final do jogo.
    return finalScene;
  }
}

//desenha a cena atual
void draw() {
  currentScene.drawScene();
}

//verifica as condições de mousePressed da cena atual.
void mousePressed() {
  currentScene.checkForPresses();
}

//verifica as condições de mouseReleased da cena atual.
void mouseReleased() {
  currentScene.checkForReleases();
}

//verifica as condições de clique da cena atual.
void mouseClicked() {
  currentScene.checkForClicks();
}

//verifica as condições de keypressed da cena atual.
void keyPressed() {
}
