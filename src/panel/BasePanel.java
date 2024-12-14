package panel;

import enums.Panels;
import frame.GameFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BasePanel extends JPanel {

    private GameFrame gameFrame = null;

    public BasePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    public void putButton(JPanel jpanel, JLabel jLabel, int xPos, int yPos) {
        jLabel.setSize(200,110);
        jLabel.setLocation(xPos, yPos);
        jpanel.add(jLabel);
    }

    public void changePanel(JLabel jLabel, Panels toPanel){
        jLabel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                gameFrame.changePanel(toPanel);
            }

        });
    }

    public void putChangePanelButton(JPanel jpanel, JLabel jLabel, Panels toPanel, int xPos, int yPos) {

        putButton(jpanel, jLabel, xPos, yPos);
        changePanel(jLabel, toPanel);
    }

}
