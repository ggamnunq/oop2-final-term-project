package game.panel;

import javax.swing.*;
import java.awt.*;

// 게임 내에서 점수를 보이기 위한 패널
public class ScorePanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon icon = new ImageIcon("src/images/soilder.jpg");
    private Image soilderImg = icon.getImage();

    private int score; // 점수 변수 생성

    //생성자
    public ScorePanel() {
        score = 0; // 처음에는 0으로 초기화
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 그리기
        g.drawImage(soilderImg, 0,0, this.getWidth(), this.getHeight(), null);
        g.setFont(new Font("Arial", Font.BOLD, 30)); // 폰트 설정
        g.drawString("점수 " + score, 150, 30); // 점수 출력
    }

    // 점수 증가 메서드. GamePanel에서 호출
    public void increaseScore(int score) {
        this.score += score;
        repaint();
    }

    // 점수 반환 메서드. GamePanel에서 게임 오버 시 점수 기록 할 때 호출
    public int getScore() {
        return score;
    }

    public void reset(){
        score = 0;
    }

}
