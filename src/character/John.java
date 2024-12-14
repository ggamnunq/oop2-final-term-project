package character;

public class John extends Character {

    private static int LIFE = 5;
    private static int MAXBULLET = 10;
    private static int RELOADING_TIME = 3000;

    public John() {
        super(LIFE, MAXBULLET, RELOADING_TIME);
    }

}
