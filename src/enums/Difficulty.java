package enums;

public class Difficulty {

    public enum DifficultyEnum{
        EASY, NORMAL, HARD
    }

    private int monsterMovingSpeed;
    private int monsterLife;

    public Difficulty(DifficultyEnum difficulty) {
        switch (difficulty) {
            case DifficultyEnum.EASY:
                monsterMovingSpeed = 1;
                monsterLife = 2;
                break;
            case DifficultyEnum.NORMAL:
                monsterMovingSpeed = 2;
                monsterLife = 2;
                break;
            case DifficultyEnum.HARD:
                monsterMovingSpeed = 2;
                monsterLife = 3;
                break;
            default:
                monsterMovingSpeed = 10;
                break;
        }
    }

    public int getMonsterMovingSpeed() {
        return monsterMovingSpeed;
    }

    public int getMonsterLife() {
        return monsterLife;
    }
}
