package game.character;

import java.awt.*;

// 적 클래스
public class Enemy {

    // 적 스펙
    private int life; // 생명
    private Point position; // 위치

    //생성자 - 목숨과 위치 설정
    public Enemy(int life, Point position) {
        this.life = life;
        this.position = position;
    }

    // 몬스터 데미지 입음
    public void damaged(int damage) {
        life -= damage;
    }

    // 몬스터 위치 반환
    public Point getPosition() {
        return position;
    }

    // 몬스터 생명 반환
    public int getLife() {
        return life;
    }



}
