package game.character;

import java.awt.*;

// 캐릭터 클래스
public class Character {

    //캐릭터 스펙
    private int life; // 생명
    private int maxBulletAmount; // 최대 총알 수
    private Point position; // 위치
    private int reloadingTime; // 재장전 소요시간

    // 생성자 - Character 객체를 상속받는 클래스에서 호출함
    public Character(int life, int maxBulletAmount, int reloadingTime) {
        // 캐릭터 생성 시, 생명, 최대 총알 수, 재장전 시간을 정의함
        this.life = life;
        this.maxBulletAmount = maxBulletAmount;
        this.reloadingTime = reloadingTime;
    }

    // 캐릭터 남은 생명 반환
    public int getLife() {
        return life;
    }

    // 캐릭터의 최대 총알 수 반환
    public int getMaxBulletAmount() {
        return maxBulletAmount;
    }

    // 캐릭터 위치 반환
    public Point getPosition() {
        return position;
    }

    // 캐릭터의 재장선 소요시간 반환
    public int getReloadingTime() {
        return reloadingTime;
    }

    // 캐릭터 위치 설정
    public void setPosition(Point position) {
        this.position = position;
    }

}
