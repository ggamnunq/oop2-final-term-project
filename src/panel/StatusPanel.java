package panel;

import character.Player;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon icon = new ImageIcon("src/images/ammo.jpg");
    private Image ammoImg = icon.getImage();
    private JLabel label = new JLabel();

    private int maxAmmo = 0;
    private int currentAmmo = 0;
    private int life = 0;

    public StatusPanel() {

        setBackground(new Color(229, 228, 228));
        add(label);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ammoImg, 100,120, this.getWidth()-100, this.getHeight()-100,0,0, ammoImg.getWidth(this), ammoImg.getHeight(this), null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("/", 185,320);
        g.drawString(String.valueOf(currentAmmo), 145,320);
        g.drawString(String.valueOf(maxAmmo), 215,320);
    }

}
