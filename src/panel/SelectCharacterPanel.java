package panel;

import character.John;
import character.Michael;
import character.Milner;
import character.Character;
import enums.Panels;
import frame.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectCharacterPanel extends BasePanel{

    private ImageIcon backgroundIcon = new ImageIcon("src/images/characterSelectBackground.png");
    private ImageIcon selectButton = new ImageIcon("src/images/selectButton.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");
    private Image backgroundImg = backgroundIcon.getImage();

    private JLabel previousLabel = new JLabel(previousIcon);
    private JLabel selectJohnButtonLabel = new JLabel(selectButton);
    private JLabel selectMichaelButtonLabel = new JLabel(selectButton);
    private JLabel selectMilnerButtonLabel = new JLabel(selectButton);

    private GamePanel gamePanel;

    public SelectCharacterPanel(GameFrame gameFrame, GamePanel gamePanel) {

        super(gameFrame);
        this.gamePanel = gamePanel;

        setLayout(null);
        // 이전 버튼
        putChangePanelButton(this, previousLabel, Panels.HOME, 100,650);
        // 캐릭터 선택 버튼 배치
        putButton(this, selectJohnButtonLabel, 150, 500);
        putButton(this, selectMichaelButtonLabel, 480, 500);
        putButton(this, selectMilnerButtonLabel, 820, 500);
        // 각 버튼에 캐릭터 선택하는 리스너 생성
        addCharacterSelectListener(selectJohnButtonLabel, new John());
        addCharacterSelectListener(selectMichaelButtonLabel, new Michael());
        addCharacterSelectListener(selectMilnerButtonLabel, new Milner());
    }

    private void addCharacterSelectListener(JLabel jLabel, Character character){
        jLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gamePanel.setCharacter(character);
                changePanel(jLabel, Panels.SELECT_DIFFICULTY);
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);
    }
}
