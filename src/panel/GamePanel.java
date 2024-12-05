package panel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon walking_soldierIcon = new ImageIcon("src/images/walking_soldier.png");
    private ImageIcon shooting_soldierIcon = new ImageIcon("src/images/shooting_soldier.png");
    private Image backgroundImg = backgroundIcon.getImage();
    private Image walking_soldierImg = walking_soldierIcon.getImage();

    private InputPanel inputPanel = new InputPanel();

    public GamePanel() {
        setLayout(new BorderLayout());
        inputPanel.setLocation(200, 200);
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    class InputPanel extends JPanel {

        private JTextField tf = new JTextField(20);

        public InputPanel() {
            //textfield 내의 글씨를 가운데 정렬
            tf.setHorizontalAlignment(SwingConstants.CENTER);
            add(tf);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(), null);
        g.drawImage(walking_soldierImg,40, 580, 150,150,null);
    }

}
