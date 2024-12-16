package game.panel;

import game.enums.PanelType;
import game.GameFrame;

import javax.swing.*;
import java.awt.*;

// 게임 방법 보는 패널
public class HowToPlayPanel extends BasePanel {

    //이미지 불러옴
    private ImageIcon backgroundIcon = new ImageIcon("src/images/howtoplay.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");

    // 배경화면 ImageIcon -> Image
    private Image backgroundImg = backgroundIcon.getImage();

    // 이전으로 가는 버튼
    private JLabel previousLabel = new JLabel(previousIcon);

    // 생성자
    public HowToPlayPanel(GameFrame gameFrame) {
        super(gameFrame); // BasePanel의 생성자
        setLayout(null); // 버튼을 임의의 위치에 배치하기 위해 layout null로 설정
        // 이전 버튼 배치 & 누르면 홈으로 이동
        putChangePanelButton(this, previousLabel, PanelType.HOME, 0,650);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconHeight(), backgroundIcon.getIconHeight()-100, null);
    }

}
