package character;

import java.awt.*;

public class Player {

    private int life;
    private int maxBulletAmount;
    private Point position;

    public Player(int life, int maxBulletAmount) {
        this.life = life;
        this.maxBulletAmount = maxBulletAmount;
    }

    public int getLife() {
        return life;
    }

    public int getMaxBulletAmount() {
        return maxBulletAmount;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }
}
