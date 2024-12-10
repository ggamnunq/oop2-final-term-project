package panel;

import enums.Panels;
import frame.GameFrame;

import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends BasePanel {

    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");

    private Image backgroundImg = backgroundIcon.getImage();

    private JLabel nextLabel = new JLabel(previousIcon);
    private JLabel previousLabel = new JLabel(previousIcon);

    public HowToPlayPanel(GameFrame gameFrame) {
        super(gameFrame);
        setLayout(null);
        putChangePanelButton(this, previousLabel, Panels.HOME, 100,650);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconHeight(), backgroundIcon.getIconHeight()-100, null);
    }

}
