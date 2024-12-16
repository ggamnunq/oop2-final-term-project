package game.character;

// John 캐릭터 객체
public class John extends Character {

    // John의 스펙 정의
    private static int LIFE = 5;
    private static int MAXBULLET = 10;
    private static int RELOADING_TIME = 3000;

    // 생성자
    public John() {
        // Character 객체의 생성자
        // John의 목숨, 최대 총알 수 , 재장전 소요시간 넘김
        super(LIFE, MAXBULLET, RELOADING_TIME);
    }

}
