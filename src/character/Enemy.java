package character;

public class Enemy {

    private int life;
    private int x, y;
    private String word;

    public Enemy(int life) {
        this.life = life;
    }

    public Enemy(int life, int x, int y) {
        this(life);
        this.x = x;
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getLife() {
        return life;
    }

    public String getWord() {
        return word;
    }

    public void damage(int damage) {
        life -= damage;
    }

}
