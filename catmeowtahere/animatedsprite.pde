//classe para a gestão de spritesheets para a animação de objectos
//código adaptado do seguinte exemplo: https://www.openprocessing.org/sketch/119481
class AnimatedSprite {
  //as variáveis publicas podem ser acedidas fora da classe. apenas estas são publicas pois as outras não convém ser alteradas.
  public PImage spriteSheet;
  public int frameWidth;
  public int frameHeight;
  public boolean loop = true;
  public float frameSpeed = 12.0f;
  public boolean isInLastFrame;

  //uso do tipo PVector para definir vectores de posição e escala da imagem
  public PVector spriteScale = new PVector(1, 1);
  public PVector position = new PVector(0, 0);

  private int frameRow;
  private int frameColumn;
  private int startFrame;
  private int endFrame;
  private float currentFrame;
  private int columns;

  //para inicializarmos uma spriteSheet necessitamos do caminho da imagem e de saber o número de colunas e linhas da grelha da imagem.
  AnimatedSprite(String filepath, int columns, int rows) {
    spriteSheet = loadImage(filepath);

    //é definido o tamanho de cada sprite individual.
    frameWidth = spriteSheet.width / columns;
    frameHeight = spriteSheet.height / rows;

    this.columns = columns;
  }

  //valores inciais para a animação criada: onde começa e acaba a animação, a velocidade e se faz loop ou não
  void setAnimation(int start, int end, float speed, boolean looping) {
    startFrame = start;
    endFrame = end;
    currentFrame = startFrame;
    loop = looping;
    frameSpeed = speed;
    isInLastFrame = false;
  }

  void update() {
    //muda para a frame seguinte
    currentFrame += (frameSpeed/frameRate);

    if((int)currentFrame > endFrame){
      if(loop) {
        currentFrame = startFrame;
      }
      else { //caso não faça loop indica se a animação chegou à ultima frame
        currentFrame = endFrame;
        isInLastFrame = true;
      }
    }

    frameColumn = (int)currentFrame;

    //matemática e cenas
    if(columns > 0) {
      frameColumn = (int)((int)currentFrame % (columns)); //resto da divisão indica a posição na coluna
      frameRow = (int)((int)currentFrame / columns); //divisão indica a posição na linha
    }

    //são aplicadas transformações por isso é necessário fazer push e pop à matriz de transformação para não afectar outros objectos desenhados
    pushMatrix();
    translate(position.x, position.y); //move a imagem
    scale(spriteScale.x, spriteScale.y); //escala a imagem
    copy(spriteSheet, frameColumn * frameWidth, frameRow * frameHeight,
      frameWidth, frameHeight, 0, 0, frameWidth, frameHeight); //corta a imagem de modo a renderizar apenas aquilo que é indicado, neste caso a sprite/frame atual da spritesheet
    popMatrix();
  }
}
