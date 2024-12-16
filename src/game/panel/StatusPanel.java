package game.panel;

import game.character.Character;

import javax.swing.*;
import java.awt.*;

// 게임 내에서 체력, 총알 수를 보이기 위한 패널
public class StatusPanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon bulletIcon = new ImageIcon("src/images/bullet.jpg");
    private ImageIcon heartIcon = new ImageIcon("src/images/heart.png");
    private Image bulletImg = bulletIcon.getImage();
    private Image heartImg = heartIcon.getImage();

    // 최대 총알 수 , 현재 총알 수, 생명은 GamePanel에서 Player를 설정할 때 같이 설정해줌
    // 처음에는 0으로 초기화
    private int maxBulletAmount = 0;
    private int currentBulletAmount = 0;
    private int life = 0;

    // 리로딩 중인지 확인하는 변수. false로 초기화
    private boolean isReloading = false;

    // 캐릭터 처음에는 null로 초기화
    // 캐릭터 선택 패널을 통해 주입받을 예정
    private Character character = null;

    // 생성자 생성
    public StatusPanel() {

    }

    // 재장전 Thread
    // reload() 메서드에서 호출과 동시에 시작
    // 재장전 완료 후 스레드 종료
    class ReloadThread extends Thread {

        @Override // 스레드 실행 시 작동
        public void run() {

            try {
                // 재장전 상태로 전환 후 -> repaint
                isReloading = true;
                repaint();
                sleep(character.getReloadingTime()); // 재장전 수행. 캐릭터의 리로딩 스펙 가져옴
                currentBulletAmount = maxBulletAmount; // 현재 총알 수 max로 변경
                isReloading = false; // 리로딩 상태 변경
                repaint();
            }catch (InterruptedException e) {
                return;
            }
        }// run 메서드 종료 시 스레드 종료

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //남은 체력 표시(하트)
        for (int i = 0; i < life; i++) {
            g.drawImage(heartImg, 20+70*i, 50, this.getWidth()-320, this.getHeight()-320,null);
        }

        //총알 사진 그리기
        g.drawImage(bulletImg, 100,120, this.getWidth()-100, this.getHeight()-100,0,0, bulletImg.getWidth(this), bulletImg.getHeight(this), null);

        //총알 사진 밑 글씨( 총알 개수, 재장전 ) 폰트 설정
        g.setFont(new Font("Arial", Font.BOLD, 50));
        if(!isReloading){ // 재장전 중이 아닐 때는 "남은 총알/최대 총알" 출력
            g.drawString("/", 185,320);
            g.drawString(String.valueOf(currentBulletAmount), 115,320); // 남은 총알
            g.drawString(String.valueOf(maxBulletAmount), 215,320); // 최대 총알
        }else{ //재장전일 때의 출력
            g.drawString("reloading...", 75,320);
        }
    }

    //총알 개수 감소 메서드
    public void decreaseBullet() {
        this.currentBulletAmount--;
        repaint(); // 감소 후 상태 패널에 repaint
    }

    // 체력 감소
    public void playerDamaged(){
        this.life--;
        repaint(); // 감소 후 상태 패널에 repaint
    }

    //재장전 메서드
    public void reload(){
        //메서드 호출 떄마다 ReloadThread 생성 후 시작
        new ReloadThread().start();
    }

    //현재 남은 총알 개수 반환. 총알 쏠 수 있는 여부 판단에 사용
    public int getCurrentBulletAmount() {
        return currentBulletAmount;
    }

    // 남은 생명 반환. 게임 오버 판단에 사용
    public int getLife() {
        return life;
    }

    // 캐릭터 설정
    // 캐릭터 선택하면 이 메서드 호출하여 캐릭터 정보 설정함
    public void setCharacter(Character character) {
        this.character = character; // 캐릭터 객체 주입
        this.maxBulletAmount = character.getMaxBulletAmount(); // 최대 총알 설정
        this.currentBulletAmount = maxBulletAmount; // 현재 총알을 최대 총알로 초기화
        this.life = character.getLife(); // 캐릭터의 생명 수로 설정
        repaint();
    }
}
