class Button {
  PImage btnImage;
  PVector btnSize;
  PVector btnPosition;
  float scale = 1f;
  boolean isPressed;

  Button(String imagePath, PVector position) {
    btnImage = loadImage(imagePath);
    btnPosition = position;
  }

  void setSize(PVector size) {
    btnImage.resize((int)size.x, (int)size.y);
  }

  void render() {
    pushMatrix();
    translate(btnPosition.x, btnPosition.y);
    imageMode(CENTER);
    image(btnImage, 0, 0, btnImage.width * scale, btnImage.height * scale);
    popMatrix();
  }

  boolean isMouseOnBtn() {
    boolean xDistance = mouseX > btnPosition.x - btnImage.width/2 && mouseX < btnPosition.x + btnImage.width/2;
    boolean yDistance = mouseY > btnPosition.y - btnImage.height/2 && mouseY < btnPosition.y + btnImage.height/2;

    return xDistance && yDistance;
  }

  void pressed() {
    scale = 0.8f;
    isPressed = true;
  }

  void released() {
    scale = 1f;
    isPressed = false;
  }
}
