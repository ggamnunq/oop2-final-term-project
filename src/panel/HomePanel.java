package panel;

import enums.Panels;
import frame.GameFrame;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends BasePanel {

    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon gamestartIcon = new ImageIcon("src/images/gamestartButton.png");
    private ImageIcon howToPlayIcon = new ImageIcon("src/images/howtoplayButton.png");
    private ImageIcon rankingIcon = new ImageIcon("src/images/rankingButton.png");
    private ImageIcon addWordIcon = new ImageIcon("src/images/addWordButton.png");

    private Image backgroundImg = backgroundIcon.getImage();

    private JLabel startLabel = new JLabel(gamestartIcon);
    private JLabel rankingLabel = new JLabel(rankingIcon);
    private JLabel howToPlayLabel = new JLabel(howToPlayIcon);
    private JLabel addWorsdLabel = new JLabel(addWordIcon);

    public HomePanel(GameFrame gameFrame) {
        super(gameFrame);
        setLayout(null);

        // 게임시작 버튼 조절&배치, 이동할 캐릭터 선택 패널로 이동
        putChangePanelButton(this, startLabel, Panels.SELECT_CHARACTER, 500, 300);
        // 게임방법 버튼 조절&배치
        putChangePanelButton(this, howToPlayLabel, Panels.HOW_TO_PLAY, 500, 400);
        // 랭킹 버튼 조절&배치
        putChangePanelButton(this, rankingLabel, Panels.RANKING, 500, 500);
        // 단어 추가 버튼 조절&배치
        putChangePanelButton(this, addWorsdLabel, Panels.ADD_WORDS, 500, 600);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);
    }

}
