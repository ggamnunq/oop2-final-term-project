package character;

import java.awt.*;

public class Character {

    private int life;
    private int maxBulletAmount;
    private Point position;
    private int reloadingTime;

    public Character(int life, int maxBulletAmount, int reloadingTime) {
        this.life = life;
        this.maxBulletAmount = maxBulletAmount;
        this.reloadingTime = reloadingTime;
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

    public int getReloadingTime() {
        return reloadingTime;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
