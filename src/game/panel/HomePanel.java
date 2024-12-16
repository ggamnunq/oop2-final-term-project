package game.panel;

import game.enums.PanelType;
import game.GameFrame;
import game.resource.AudioPlayer;

import javax.swing.*;
import java.awt.*;

// 홈화면 패널
public class HomePanel extends BasePanel {

    // 이미 불러옴
    private ImageIcon backgroundIcon = new ImageIcon("src/images/mainBackground.png");
    private ImageIcon gamestartIcon = new ImageIcon("src/images/gamestartButton.png");
    private ImageIcon howToPlayIcon = new ImageIcon("src/images/howtoplayButton.png");
    private ImageIcon rankingIcon = new ImageIcon("src/images/rankingButton.png");
    private ImageIcon addWordIcon = new ImageIcon("src/images/addWordButton.png");

    // 배경화면 ImageIcon -> Image
    private Image backgroundImg = backgroundIcon.getImage();

    // 홈화면 버튼들 ( 누르면 해당 화면으로 넘어감 )
    private JLabel startLabel = new JLabel(gamestartIcon); // 게임시작 버튼
    private JLabel rankingLabel = new JLabel(rankingIcon); // 랭킹 보기 버튼
    private JLabel howToPlayLabel = new JLabel(howToPlayIcon); // 게임방법 버튼
    private JLabel addWorsdLabel = new JLabel(addWordIcon); // 단어 추가 버튼

    // 생성자
    public HomePanel(GameFrame gameFrame) {
        super(gameFrame); // BasePanel 생성자
        setLayout(null); // 버튼을 임의의 위치에 배치하기 위해 layout null로 설정

        // 게임방법 버튼 조절&배치 ( 캐릭터 선택 화면으로 이동 )
        putChangePanelButton(this, howToPlayLabel, PanelType.HOW_TO_PLAY, 200, 500);
        // 게임시작 버튼 조절&배치,( 이동할 캐릭터 선택 패널로 이동)
        putChangePanelButton(this, startLabel, PanelType.SELECT_CHARACTER, 400, 500);
        // 랭킹 버튼 조절&배치 ( 랭킹 보는 화면으로 이동 )
        putChangePanelButton(this, rankingLabel, PanelType.RANKING, 600, 500);
        // 단어 추가 버튼 조절&배치 ( 단어 추가하는 화면으로 이동 )
        putChangePanelButton(this, addWorsdLabel, PanelType.ADD_WORDS, 800, 500);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);
    }

}
