class AnimatedSprite {
  public PImage spriteSheet;
  public int frameWidth;
  public int frameHeight;
  public boolean loop = true;
  public float frameSpeed = 12.0f;
  public boolean isInLastFrame;

  public PVector spriteScale = new PVector(1, 1);
  public PVector position = new PVector(0, 0);

  private int frameRow;
  private int frameColumn;
  private int startFrame;
  private int endFrame;
  private float currentFrame;
  private int columns;

  AnimatedSprite(String filepath, int columns, int rows) {
    spriteSheet = loadImage(filepath);

    frameWidth = spriteSheet.width / columns;
    frameHeight = spriteSheet.height / rows;

    this.columns = columns;
  }

  void setAnimation(int start, int end, float speed, boolean looping) {
    startFrame = start;
    endFrame = end;
    currentFrame = startFrame;
    loop = looping;
    frameSpeed = speed;
    isInLastFrame = false;
  }

  void update() {
    currentFrame += (frameSpeed/frameRate);

    if((int)currentFrame > endFrame){
      if(loop) {
        currentFrame = startFrame;
      }
      else {
        currentFrame = endFrame;
        isInLastFrame = true;
      }
    }

    frameColumn = (int)currentFrame;

    if(columns > 0) {
      frameColumn = (int)((int)currentFrame % (columns));
      frameRow = (int)((int)currentFrame / columns);
    }

    pushMatrix();
    translate(position.x, position.y);
    scale(spriteScale.x, spriteScale.y);
    copy(spriteSheet, frameColumn * frameWidth, frameRow * frameHeight,
      frameWidth, frameHeight, 0, 0, frameWidth, frameHeight);
    popMatrix();
  }
}
