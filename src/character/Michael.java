package character;

public class Michael extends Character {

    private static int LIFE = 3;
    private static int MAXBULLET = 20;
    private static int RELOADING_TIME = 3000;

    public Michael() {
        super(LIFE, MAXBULLET, RELOADING_TIME);
    }
}
