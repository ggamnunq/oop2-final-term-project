package panel;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon bulletIcon = new ImageIcon("src/images/bullet.jpg");
    private Image bulletImg = bulletIcon.getImage();
    private JLabel label = new JLabel();

    private int maxBulletAmount = 10;
    private int currentBulletAmount = 10;
    private int life = 0;
    private boolean isReloading = false;

    public StatusPanel() {

        setBackground(new Color(229, 228, 228));
        add(label);

    }

    // 재장전 Thread
    // reload() 메서드에서 호출과 동시에 시작
    // 재장전 완료 후 스레드 종료
    class ReloadThread extends Thread {

        @Override
        public void run() {

            try {
                // 재장전 상태로 전환 후 -> repaint
                isReloading = true;
                repaint();
                // 재장전 수행
                sleep(3000);
                // 현재 총알 수 변경 & 재장전 상태 변경 -> repaint
                currentBulletAmount = maxBulletAmount;
                isReloading = false;
                repaint();
            }catch (InterruptedException e) {
                return;
            }
        }// run 메서드 종료 시 스레드 종료

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //총알 사진 그리기
        g.drawImage(bulletImg, 100,120, this.getWidth()-100, this.getHeight()-100,0,0, bulletImg.getWidth(this), bulletImg.getHeight(this), null);
        //총알 사진 밑 글씨( 총알 개수, 재장전 ) 폰트 설정
        g.setFont(new Font("Arial", Font.BOLD, 50));

        if(!isReloading){ // 재장전 중이 아닐 떄의 출력
            g.drawString("/", 185,320);
            g.drawString(String.valueOf(currentBulletAmount), 115,320);
            g.drawString(String.valueOf(maxBulletAmount), 215,320);
        }else{ //재장전일 때의 출력
            g.drawString("reloading...", 75,320);
        }
    }

    //총알 개수 감소 메서드
    public void decreaseBullet() {
        this.currentBulletAmount--;
        repaint(); // 감소 후 상태 패널에 repaint
    }

    //현재 남은 총알 개수 반환
    public int getCurrentBulletAmount() {
        return currentBulletAmount;
    }

    //재장전 메서드
    public void reload(){
        //메서드 호출 떄마다 ReloadThread 생성 후 시작
        new ReloadThread().start();
    }
}
