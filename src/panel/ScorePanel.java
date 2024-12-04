package panel;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon icon = new ImageIcon("src/images/soilder.jpg");
    private Image soilderImg = icon.getImage();
    private int score;

    public ScorePanel() {
        score = 0;
        this.setBackground(Color.GREEN);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(soilderImg, 0,0, this.getWidth(), this.getHeight(), null);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(score + "명 구함", 130, 30);
    }

}
