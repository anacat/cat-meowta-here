//Classe para facilitar a criação de butões.
class Button {
  //elementos comuns a todos os botões: imagem, tamanho, posição, escala, estado
  PImage btnImage;
  PVector btnSize;
  PVector btnPosition;
  float scale = 1f;
  boolean isPressed;

  //é necessário um caminho e vector com a posição onde queremos colocar o botão
  Button(String imagePath, PVector position) {
    btnImage = loadImage(imagePath);
    btnPosition = position;
  }

  //função para alterar o tamanho do botão
  void setSize(PVector size) {
    btnImage.resize((int)size.x, (int)size.y);
  }

  //função para renderizar o botão no ecrã
  void render() {
    //são aplicadas transformações por isso é necessário fazer push e pop à matriz de transformação para não afectar outros objectos renderizados
    pushMatrix();
    translate(btnPosition.x, btnPosition.y);
    imageMode(CENTER);
    image(btnImage, 0, 0, btnImage.width * scale, btnImage.height * scale); //desenha botão com escala aplicada à imagem
    popMatrix();
  }

  //verifica se o rato está em cima do botão. Caso o cursor do rato se encontrar dentro dos limites do botão então retorna verdadeiro.
  boolean isMouseOnBtn() {
    boolean xDistance = mouseX > btnPosition.x - btnImage.width/2 && mouseX < btnPosition.x + btnImage.width/2;
    boolean yDistance = mouseY > btnPosition.y - btnImage.height/2 && mouseY < btnPosition.y + btnImage.height/2;

    return xDistance && yDistance;
  }

  //estado pressionado do botão: a escala aumenta, o estado muda.
  void pressed() {
    scale = 0.8f;
    isPressed = true;
  }

  //estado solto do botão: a escala volta ao normal, o estado volta ao normal.
  void released() {
    scale = 1f;
    isPressed = false;
  }
}
