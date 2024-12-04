package character;

public class Player {

    private int life;
    private int maxBulletAmount;

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

}
