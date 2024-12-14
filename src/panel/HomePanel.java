package panel;

import enums.Panels;
import frame.GameFrame;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends BasePanel {

    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon gamestartButton = new ImageIcon("src/images/gamestartButton.png");
    private ImageIcon howToPlayButton = new ImageIcon("src/images/howtoplayButton.png");
    private ImageIcon rankingButton = new ImageIcon("src/images/rankingButton.png");
    private Image backgroundImg = backgroundIcon.getImage();

    private JLabel startLabel = new JLabel(gamestartButton);
    private JLabel rankingLabel = new JLabel(rankingButton);
    private JLabel howToPlayLabel = new JLabel(howToPlayButton);

    public HomePanel(GameFrame gameFrame) {
        super(gameFrame);
        setLayout(null);

        // 게임시작 버튼 조절&배치, 이동할 캐릭터 선택 패널로 이동
        putChangePanelButton(this, startLabel, Panels.SELECT_CHARACTER, 500, 400);
        // 게임방법 버튼 조절&배치
        putChangePanelButton(this, howToPlayLabel, Panels.HOW_TO_PLAY, 500, 500);
        // 랭킹 버튼 조절&배치
        putChangePanelButton(this, rankingLabel, Panels.RANKING, 500, 600);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);
    }

}
