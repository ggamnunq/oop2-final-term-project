package character;

public class Player {

    private int life;
    private int maxBulletAmount;
    private int x, y;

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
