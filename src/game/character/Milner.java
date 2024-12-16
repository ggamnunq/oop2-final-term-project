package game.character;

// Milner 캐릭터 객체
public class Milner extends Character {

    // Milner의 스펙 정의
    private static int LIFE = 5;
    private static int MAXBULLET = 5;
    private static int RELOADING_TIME = 1000;

    // 생성자
    public Milner(){
        // Character 객체의 생성자
        // Milner의 목숨, 최대 총알 수 , 재장전 소요시간 넘김
        super(LIFE, MAXBULLET, RELOADING_TIME);
    }

}
