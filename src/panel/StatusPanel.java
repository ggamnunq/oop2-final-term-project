package panel;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon bulletIcon = new ImageIcon("src/images/bullet.jpg");
    private Image bulletImg = bulletIcon.getImage();
    private JLabel label = new JLabel();

    private int maxBulletAmount = 10;
    private int currentBulletAmount = 10;
    private int life = 0;
    private boolean isReloading = false;

    private ReloadThread reloadThread = null;
    private JLabel reloadingLabel = new JLabel("reloading...");

    public StatusPanel() {

        setBackground(new Color(229, 228, 228));
        add(label);
        reloadingLabel.setFont(new Font("Arial", Font.BOLD, 20));
        reloadingLabel.setLocation(10, 10);

    }

    class ReloadThread extends Thread {


        @Override
        public void run() {

            try {
                isReloading = true;
                repaint();
                sleep(3000);
                currentBulletAmount = maxBulletAmount;
                isReloading = false;
                repaint();
            }catch (InterruptedException e) {
                return;
            }
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bulletImg, 100,120, this.getWidth()-100, this.getHeight()-100,0,0, bulletImg.getWidth(this), bulletImg.getHeight(this), null);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        if(!isReloading){
            g.drawString("/", 185,320);
            g.drawString(String.valueOf(currentBulletAmount), 115,320);
            g.drawString(String.valueOf(maxBulletAmount), 215,320);
        }else{
            g.drawString("reloading...", 75,320);
        }
    }

    public void decreaseBullet() {
        this.currentBulletAmount--;
        repaint();
    }

    public int getCurrentBulletAmount() {
        return currentBulletAmount;
    }

    public void reload(){
        reloadThread = new ReloadThread();
        reloadThread.start();
    }
}
