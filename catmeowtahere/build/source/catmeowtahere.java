import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class catmeowtahere extends PApplet {

Scene currentScene;
MainMenu mainMenu;

public void setup() {
  
  //define o tipo de render da aplica\u00e7\u00e3o (para poder limpar o ecr\u00e3 ao usar uma image como fundo de uma cena).
  createGraphics(width, height, JAVA2D);

  mainMenu = new MainMenu();
  currentScene = mainMenu;
}

public void draw() {
  currentScene.drawScene();
}

public void mousePressed() {
  currentScene.checkForPresses();
}

public void mouseReleased() {
  currentScene.checkForReleases();
}
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

  public void setSize(PVector size) {
    btnImage.resize((int)size.x, (int)size.y);
  }

  public void render() {
    imageMode(CENTER);
    image(btnImage, btnPosition.x, btnPosition.y, btnImage.width * scale, btnImage.height * scale);
  }

  public boolean isMouseOnBtn() {
    boolean xDistance = mouseX > btnPosition.x - btnImage.width/2 && mouseX < btnPosition.x + btnImage.width/2;
    boolean yDistance = mouseY > btnPosition.y - btnImage.height/2 && mouseY < btnPosition.y + btnImage.height/2;

    return xDistance && yDistance;
  }

  public void pressed() {
    scale = 0.8f;
    isPressed = true;
  }

  public void released() {
    scale = 1f;
    isPressed = false;
  }
}
class MainMenu implements Scene {
  PImage backgroundImage;
  Button newGameBtn;
  Button exitBtn;

  ArrayList<Button> buttons = new ArrayList();

  MainMenu(){
    backgroundImage = loadImage("images/mainmenu.png");

    newGameBtn = new Button("images/newgame.png", new PVector(width/2, height/2+60));
    exitBtn = new Button("images/exit.png", new PVector(width/2, height/2+160));

    buttons.add(newGameBtn);
    buttons.add(exitBtn);
  }

  public void drawScene() {
    background(backgroundImage);

    for(Button btn : buttons) {
      btn.render();
    }

    if(newGameBtn.isMouseOnBtn() || exitBtn.isMouseOnBtn()){
      cursor(HAND);
    }
    else {
      cursor(ARROW);
    }
  }

  public void checkForPresses() {
    if(newGameBtn.isMouseOnBtn()) {
      newGameBtn.pressed();
    }
    else if(exitBtn.isMouseOnBtn()) {
      exit();
    }
  }

  public void checkForReleases() {
    for(Button btn : buttons) {
      btn.released();
    }
  }
}
interface Scene {
  public void drawScene();
  public void checkForPresses();
  public void checkForReleases();
}
  public void settings() {  size(720, 480); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "catmeowtahere" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
