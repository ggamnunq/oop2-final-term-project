package character;

import java.awt.*;

public class Enemy {

    private int life;
    private Point position;
    private String word;

    public Enemy(int life) {
        this.life = life;
    }

    public Enemy(int life, Point position) {
        this(life);
        this.position = position;
    }

    public void damaged(int damage) {
        life -= damage;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getLife() {
        return life;
    }

    public String getWord() {
        return word;
    }

}
