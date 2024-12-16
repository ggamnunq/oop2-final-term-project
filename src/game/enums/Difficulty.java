package game.enums;

// 난이도 관리하는 클래스
public class Difficulty {

    // 난이도를 간단하게 정하기 위한 열거형 타입
    public enum DifficultyEnum{
        EASY, NORMAL, HARD
    }

    // 난이도 스펙
    private int monsterMovingSpeed; //몬스터 이동 속도
    private int monsterLife; // 몬스터 생명 수
    private DifficultyEnum difficultyEnum; // 난이도 타입

    // 생성자
    public Difficulty(DifficultyEnum difficultyEnum) {

        // 매개변수로 받은 DifficultyEnum 주입( 난이도 설정 )
        this.difficultyEnum = difficultyEnum;

        // 난이도 별로 난이도 스펙 ( 몬스터 이동속도, 몬스터 생명 )
        switch (difficultyEnum) {
            case DifficultyEnum.EASY: // 쉬움 모드
                monsterMovingSpeed = 1;
                monsterLife = 2;
                break;
            case DifficultyEnum.NORMAL: // 보통 모드
                monsterMovingSpeed = 2;
                monsterLife = 2;
                break;
            case DifficultyEnum.HARD: // 어려움 모드
                monsterMovingSpeed = 2;
                monsterLife = 3;
                break;
            default: // 그 외의 값
                monsterMovingSpeed = 1;
                monsterLife = 2;
                break;
        }
    }

    // GamePanel에 몬스터 이동속도 적용하기 위해 이동속도 반환
    public int getMonsterMovingSpeed() {
        return monsterMovingSpeed;
    }

    // GamePanel에 몬스터 생명 적용하기 위해 생명 반환
    public int getMonsterLife() {
        return monsterLife;
    }

    // 난이도 반환. 현재 세팅된 난이도 알기 위해 반환
    public DifficultyEnum getDifficultyEnum() {
        return difficultyEnum;
    }

    // 난이도 변경하기 위한 메서드
    public void setDifficultyEnum(DifficultyEnum difficultyEnum) {
        this.difficultyEnum = difficultyEnum;
    }

}
