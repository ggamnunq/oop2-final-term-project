package panel;

import enums.Panels;
import frame.GameFrame;

import javax.swing.*;
import java.awt.*;

public class SelectCharacterPanel extends BasePanel{

    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon nextIcon = new ImageIcon("src/images/nextButton.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");
    private Image backgroundImg = backgroundIcon.getImage();

    private JLabel nextLabel = new JLabel(nextIcon);
    private JLabel previousLabel = new JLabel(previousIcon);

    public SelectCharacterPanel(GameFrame gameFrame) {
        super(gameFrame);
        setLayout(null);
        putChangePanelButton(this, nextLabel, Panels.SELECT_DIFFICULTY, 900,650);
        putChangePanelButton(this, previousLabel, Panels.HOME, 100,650);
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconHeight(), backgroundIcon.getIconHeight()-100, null);
    }
}
