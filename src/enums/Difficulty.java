package enums;

public class Difficulty {

    public enum DifficultyEnum{
        EASY, NORMAL, HARD
    }

    private int monsterMovingSpeed;
    private int monsterLife;
    private DifficultyEnum difficultyEnum;

    public Difficulty(DifficultyEnum difficultyEnum) {

        this.difficultyEnum = difficultyEnum;

        switch (difficultyEnum) {
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

    public DifficultyEnum getDifficultyEnum() {
        return difficultyEnum;
    }

    public void setDifficultyEnum(DifficultyEnum difficultyEnum) {
        this.difficultyEnum = difficultyEnum;
    }

    public String getDifficultyString() {
        switch (difficultyEnum) {
            case DifficultyEnum.EASY:
                return "Easy";
            case DifficultyEnum.NORMAL:
                return "Normal";
            case DifficultyEnum.HARD:
                return "Hard";
            default:
                return "";
        }
    }

}
