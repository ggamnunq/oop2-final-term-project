package game.character;

// Michael 캐릭터 객체
public class Michael extends Character {

    // Michael의 스펙 정의
    private static int LIFE = 3;
    private static int MAXBULLET = 20;
    private static int RELOADING_TIME = 3000;

    // 생성자
    public Michael() {
        // Character 객체의 생성자
        // Michael의 목숨, 최대 총알 수 , 재장전 소요시간 넘김
        super(LIFE, MAXBULLET, RELOADING_TIME);
    }
}
