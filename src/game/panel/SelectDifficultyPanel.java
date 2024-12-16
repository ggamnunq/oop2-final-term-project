package game.panel;

import game.enums.Difficulty;
import game.enums.PanelType;
import game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectDifficultyPanel extends BasePanel {

    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon easyIcon = new ImageIcon("src/images/easyButton.png");
    private ImageIcon normalIcon = new ImageIcon("src/images/normalButton.png");
    private ImageIcon hardIcon = new ImageIcon("src/images/hardButton.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");
    private Image backgroundImg = backgroundIcon.getImage();

    private JLabel previousLabel = new JLabel(previousIcon);
    private JLabel easyButtonLabel = new JLabel(easyIcon);
    private JLabel normalButtonLabel = new JLabel(normalIcon);
    private JLabel hardButtonLabel = new JLabel(hardIcon);

    private GamePanel gamePanel = null;

    public SelectDifficultyPanel(GameFrame gameFrame, GamePanel gamePanel) {
        super(gameFrame);
        this.gamePanel = gamePanel;
        setLayout(null);
        putChangePanelButton(this, previousLabel, PanelType.SELECT_CHARACTER, 100,650);
        // 난이도 선택 버튼 배치
        putButton(this, easyButtonLabel, 500,200);
        putButton(this, normalButtonLabel, 500,350);
        putButton(this, hardButtonLabel, 500,500);
        // 각 라벨에 난이도 설정하는 마우스 이벤트 리스너 추가
        addDifficultySelectListener(easyButtonLabel, Difficulty.DifficultyEnum.EASY);
        addDifficultySelectListener(normalButtonLabel, Difficulty.DifficultyEnum.NORMAL);
        addDifficultySelectListener(hardButtonLabel, Difficulty.DifficultyEnum.HARD);

    }

    private void addDifficultySelectListener(JLabel jLabel, Difficulty.DifficultyEnum difficulty) {
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gamePanel.setDifficulty(difficulty);
                changePanel(PanelType.INPUT_PLAYER_NAME);
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconHeight(), backgroundIcon.getIconHeight()-100, null);
    }

}
